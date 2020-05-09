package com.tokopedia.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import versionToInt
import java.io.File

open class ScanProjectTaskNew : DefaultTask() {

    //input:
    var versionConfigMap = mutableMapOf<String, Int>()
    var versionSuffix = ""
    var moduleLatestVersionMap = hashMapOf<String, ArtifactLatestVersionInfo>()

    //output:
    val dependenciesProjectNameHashSet = HashSet<Pair<String, String>>()
    val projectToArtifactInfoList = hashMapOf<String, ArtifactInfo>()
    val artifactIdToProjectNameList = hashMapOf<String, String>()

    companion object {
        const val LIBRARIES_PATH = "../../buildconfig/dependencies/dependency-libraries.gradle"
        const val TKPD_INTERNAL_LIB_DEPENDENCIES = "tkpdInternalLibDependencies"
        //define who is responsible to change the version
        //if the author contains the name in here, it will be ignored and the version will not change.
//        val CHANGER_EMAIL = listOf("jenkins")
    }

    @TaskAction
    fun run() {
        //get shild project info
        //result stored in projectToArtifactInfoList and artifactIdToProjectNameList
        populateProject()

        //get dependencies [project to project]
        // stored in dependenciesProjectNameHashSet
        populateProjectDependencies()

        // this is to update all possible candidate version increase
        // for example, the change of global config will also
        // increase the version of the project that dependent on it
        // calibratingCandidateList()

        // artifact version calibrating
        // dependency-libraries version and version defined in project extension might be different
        // if it is different, take the maximum between those 2
        calibratingVersion()

        println(projectToArtifactInfoList)
        println(artifactIdToProjectNameList)
        println(dependenciesProjectNameHashSet)
    }

    private fun populateProject() {
        val queue = mutableListOf<Project>(project)
        while (queue.isNotEmpty()) {
            val projectItem = queue.removeAt(0)
            queue.addAll(projectItem.childProjects.values)

            if (projectItem == project) {
                // this is to ignore the root project
                continue
            }
            val projectName = projectItem.name
            println(projectName)

            val artifactId = projectItem.properties["artifactId"].toString()
            if (artifactId.isNotEmpty()) {
                val groupId = projectItem.properties["groupId"].toString()
                val artifactName = projectItem.properties["artifactName"].toString()
                val versionName = projectItem.properties["versionName"].toString()
                projectToArtifactInfoList[projectName] = ArtifactInfo(projectName, groupId, artifactId, artifactName, versionName)
                artifactIdToProjectNameList[artifactId] = projectName
            }
        }
        println(projectToArtifactInfoList)
        println(artifactIdToProjectNameList)
    }

    private fun populateProjectDependencies() {
        val dependenciesHashSet = HashSet<Pair<String, String>>()
        val queue = mutableListOf<Project>(project)
        //BFS to get all projects
        while (queue.isNotEmpty()) {
            val projectItem = queue.removeAt(0)
            queue.addAll(projectItem.childProjects.values)

            if (projectItem == project) {
                // this is to ignore the root project
                continue
            }

            val projectName = projectItem.name
            println(projectName)

            projectItem.configurations.all { conf ->
                conf.dependencies.forEach {
                    // this is to make hashCode of combination of libraries and those version
                    // If there are version changes in the libraries, the module will become a candidate for version increase.
                    val artifactName = it.name
                    // the artifact dependency is part of composite lib project, so add in the list
                    if (artifactIdToProjectNameList.containsKey(artifactName)) {
                        dependenciesHashSet.add(projectName to artifactName)
                    }
                }
            }
        }
        dependenciesHashSet.forEach {
            val projectDependant = artifactIdToProjectNameList[it.second] ?: ""
            dependenciesProjectNameHashSet.add(it.first to projectDependant)
        }
    }

    // get latest version and increase to 1
    private fun calibratingVersion() {
        try {
            projectToArtifactInfoList.forEach { artifactItem ->
                //check if there is the library in root project
//                if (rootArtifactVersionMap.containsKey(artifactItem.value.artifactId)) {
//                    //compare artifact on the each compositelib project.ext to artifact in dependencies-libraries in the root proj
//                    //if found, will update the value to the maximum between 2
//                    val version = rootArtifactVersionMap.get(artifactItem.value.artifactId) ?: "1"
//                    val versionInCompositeLib = artifactItem.value.versionName.versionToInt(versionConfigMap)
//                    val versionInRoot = version.versionToInt(versionConfigMap)
//                    val chosenVersion = if (versionInRoot.first > versionInCompositeLib.first) {
//                        versionInRoot
//                    } else {
//                        versionInCompositeLib
//                    }
//                    artifactItem.value.maxCurrentVersionName = chosenVersion.second
//                } else {
//                    artifactItem.value.maxCurrentVersionName = artifactItem.value.versionName
//                }
//                val currentMaxVersion = artifactItem.value.maxCurrentVersionName.versionToInt(versionConfigMap).first
//                val versionSuffixString = (if (versionSuffix.isNotEmpty()) {
//                    "-$versionSuffix"
//                } else "")
//                val increasedVersionString = (currentMaxVersion + (versionConfigMap["Step"]
//                    ?: 1)).toVersion(versionConfigMap) + versionSuffixString
//                artifactItem.value.increaseVersionString = increasedVersionString
                artifactItem.value.maxCurrentVersionName = moduleLatestVersionMap[artifactItem.value.artifactId]?.versionName ?: "0.0.0"
                val currentMaxVersion = artifactItem.value.maxCurrentVersionName.versionToInt(versionConfigMap).first
                val versionSuffixString = (if (versionSuffix.isNotEmpty()) {
                    "-$versionSuffix"
                } else "")
                val increasedVersionString = (currentMaxVersion + (versionConfigMap["Step"]
                    ?: 1)).toVersion(versionConfigMap) + versionSuffixString
                artifactItem.value.increaseVersionString = increasedVersionString
            }
        } catch (ignored: Exception) {

        }
    }

    fun Int.toVersion(configMap: Map<String, Int>): String {
        val tail = configMap["Tail"] ?: 100
        val mid = configMap["Mid"] ?: 10
        val head = 1
        return toVersion(head, mid, tail)
    }

    fun Int.toVersion(head: Int, mid: Int, tail: Int): String {
        val unitMultiply = listOf(mid * tail, tail, 1)
        val result = mutableListOf<Int>()
        var tempInt = this
        for (i in unitMultiply.indices) {
            result.add(tempInt / unitMultiply[i])
            tempInt %= unitMultiply[i]
        }
        return result.joinToString(".")
    }

}