/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.xd.dirt.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.util.Assert;
import org.springframework.xd.dirt.zookeeper.Paths;

/**
 * Builder object for paths under {@link Paths#REQUESTED}. {@code ModuleDeploymentRequestsPath}
 * represents all the requested modules upon deployment.
 *
 * @author Ilayaperumal Gopinathan
 */
public class ModuleDeploymentRequestsPath {

	/**
	 * Index for {@link Paths#DEPLOYMENTS} in {@link #elements} array.
	 */
	private static final int DEPLOYMENTS = 0;

	/**
	 * Index for {@code modules} in {@link #elements} array.
	 */
	private static final int MODULES = 1;

	/**
	 * Index for {@link Paths#REQUESTED} node in {@link #elements} array.
	 */
	private static final int REQUESTED = 2;

	/**
	 * Index for dot delimited module deployment description in {@link #elements} array.
	 */
	private static final int DEPLOYMENT_DESC = 3;

	/**
	 * Index for deployment unit (stream/job) name in dot delimited deployment description.
	 */
	private static final int DEPLOYMENT_UNIT_NAME = 0;

	/**
	 * Index for module type in dot delimited deployment description.
	 */
	private static final int MODULE_TYPE = 1;

	/**
	 * Index for module label in dot delimited deployment description.
	 */
	private static final int MODULE_LABEL = 2;

	/**
	 * Index for module sequence in dot delimited deployment description.
	 */
	private static final int MODULE_SEQUENCE = 3;

	/**
	 * Array of path elements.
	 */
	private final String[] elements = new String[4];

	/**
	 * Array of module deployment description elements.
	 */
	private final String[] deploymentDesc = new String[4];

	/**
	 * Construct a {@code ModuleDeploymentRequestsPath}.
	 * Use of this constructor means that a path will be created via {@link #build()}.
	 */
	public ModuleDeploymentRequestsPath() {
		elements[DEPLOYMENTS] = Paths.DEPLOYMENTS;
		elements[MODULES] = Paths.MODULES;
		elements[REQUESTED] = Paths.REQUESTED;
	}

	/**
	 * Construct a {@code ModuleDeploymentRequestsPath}.
	 * Use of this constructor means that an existing path will be provided and
	 * this object will be used to extract the individual elements of the path.
	 * Both full paths (including and excluding the {@link Paths#XD_NAMESPACE XD namespace prefix}) are supported.
	 *
	 * @param path stream path
	 */
	public ModuleDeploymentRequestsPath(String path) {
		Assert.hasText(path);

		String[] pathElements = path.split("\\/");

		// offset is the element array that contains the 'deployments'
		// path element; the location may vary depending on whether
		// the path string includes the '/xd' namespace
		int offset = -1;
		for (int i = 0; i < pathElements.length; i++) {
			if (pathElements[i].equals(Paths.DEPLOYMENTS)) {
				offset = i;
				break;
			}
		}

		if (offset == -1) {
			throw new IllegalArgumentException(String.format(
					"Path '%s' does not include a '%s' element", path, Paths.DEPLOYMENTS));
		}

		System.arraycopy(pathElements, offset, elements, 0, elements.length);

		Assert.noNullElements(elements);
		Assert.state(elements[DEPLOYMENTS].equals(Paths.DEPLOYMENTS));
		Assert.state(elements[MODULES].equals(Paths.MODULES));
		Assert.state(elements[REQUESTED].equals(Paths.REQUESTED));

		if (elements[DEPLOYMENT_DESC] != null) {
			int deploymentDescCount = deploymentDesc.length;
			String[] deploymentElements = elements[DEPLOYMENT_DESC].split(" ")[0].split("\\.");
			Assert.state(deploymentElements.length == deploymentDescCount);
			System.arraycopy(deploymentElements, 0, deploymentDesc, 0, deploymentDescCount);
		}
	}

	/**
	 * Return the string representation of the module instance that has the following dot limited
	 * values.
	 * <ul>
	 * <li>Module Type</li>
	 * <li>Module Label</li>
	 * <li>Module Sequence</li>
	 * </ul>
	 * @return the string representation of the module instance.
	 */
	public String getModuleInstanceAsString() {
		return String.format("%s.%s.%s", this.getModuleType(), this.getModuleLabel(), this.getModuleSequence());
	}

	/**
	 * Return the deployment unit name.
	 *
	 * @return the deployment unit (stream/job) name
	 */
	public String getDeploymentUnitName() {
		return deploymentDesc[DEPLOYMENT_UNIT_NAME];
	}

	/**
	 * Set the deployment unit name.
	 *
	 * @param deploymentUnitName  the deployment unit name
	 *
	 * @return this object
	 */
	public ModuleDeploymentRequestsPath setDeploymentUnitName(String deploymentUnitName) {
		deploymentDesc[DEPLOYMENT_UNIT_NAME] = deploymentUnitName;
		return this;
	}

	/**
	 * Return the module type.
	 *
	 * @return module type
	 */
	public String getModuleType() {
		return deploymentDesc[MODULE_TYPE];
	}

	/**
	 * Set the module type.
	 *
	 * @param moduleType module type
	 *
	 * @return this object
	 */
	public ModuleDeploymentRequestsPath setModuleType(String moduleType) {
		deploymentDesc[MODULE_TYPE] = moduleType;
		return this;
	}

	/**
	 * Return the module label.
	 *
	 * @return module label
	 */
	public String getModuleLabel() {
		return deploymentDesc[MODULE_LABEL];
	}

	/**
	 * Set the module label.
	 *
	 * @param moduleLabel module label
	 *
	 * @return this object
	 */
	public ModuleDeploymentRequestsPath setModuleLabel(String moduleLabel) {
		deploymentDesc[MODULE_LABEL] = moduleLabel;
		return this;
	}

	/**
	 * Return the module sequence.
	 *
	 * @return module sequence
	 */
	public String getModuleSequence() {
		return deploymentDesc[MODULE_SEQUENCE];
	}

	/**
	 * Set the module sequence.
	 *
	 * @param moduleSequence module sequence
	 *
	 * @return this object
	 */
	public ModuleDeploymentRequestsPath setModuleSequence(String moduleSequence) {
		deploymentDesc[MODULE_SEQUENCE] = moduleSequence;
		return this;
	}

	/**
	 * Build the path string using the field values.
	 *
	 * @return path string
	 *
	 * @see Paths#build
	 */
	public String build() {
		elements[DEPLOYMENT_DESC] = String.format("%s.%s.%s.%s",
				deploymentDesc[DEPLOYMENT_UNIT_NAME], deploymentDesc[MODULE_TYPE], deploymentDesc[MODULE_LABEL],
				deploymentDesc[MODULE_SEQUENCE]);
		return Paths.build(elements);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return build();
	}

	/**
	 * Return all the modules for a given deployment unit name.
	 *
	 * @param requestedModulesPaths the collection of module deployment requests' paths
	 * @param unitName the deployment unit (stream/job) name
	 * @return the modules that correspond to the given deployment unit.
	 */
	public static List<ModuleDeploymentRequestsPath> getModulesForDeploymentUnit(
			Collection<ModuleDeploymentRequestsPath> requestedModulesPaths, String unitName) {
		List<ModuleDeploymentRequestsPath> pathsToReturn = new ArrayList<ModuleDeploymentRequestsPath>();
		for (ModuleDeploymentRequestsPath path : requestedModulesPaths) {
			if (path.getDeploymentUnitName().equals(unitName)) {
				pathsToReturn.add(path);
			}
		}
		return pathsToReturn;
	}

}
