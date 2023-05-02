package com.tokopedia.plugin

import getCommitId
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import versionToInt

open class ScanProjectTask : DefaultTask() {

    // input:
    @Internal
    var versionConfigMap = mutableMapOf<String, Int>()

    @Internal
    var versionSuffix = ""

    @Internal
    var moduleLatestVersionMap = hashMapOf<String, ArtifactLatestVersionInfo>()

    @Internal
    var moduleToPublishList = mutableSetOf<String>()

    // output:
    @Internal
    val dependenciesProjectNameHashSet = HashSet<Pair<String, String>>()

    @Internal
    val projectToArtifactInfoList = hashMapOf<String, ArtifactInfo>()

    @Internal
    val artifactIdToProjectNameList = hashMapOf<String, String>()

    @TaskAction
    fun run() {
        // get project info
        // result stored in projectToArtifactInfoList and artifactIdToProjectNameList
        populateProject()

        // get dependencies [project to project]
        // stored in dependenciesProjectNameHashSet
        populateProjectDependencies()

        // artifact version calibrating
        // dependency-libraries version and version defined in project extension might be different
        // if it is different, take the maximum between those 2
        calibratingVersion()

        checkCommitModuleToPublishAndUpdate()
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
                var isAndroid = false
                if (projectItem.plugins.hasPlugin("com.android.library")) {
                    isAndroid = true
                }
                projectToArtifactInfoList[projectName] = ArtifactInfo(projectName, groupId, artifactId, artifactName, versionName, isAndroid)
                artifactIdToProjectNameList[artifactId] = projectName
            }
        }
    }

    private fun populateProjectDependencies() {
        val dependenciesHashSet = HashSet<Pair<String, String>>()
        val queue = mutableListOf<Project>(project)
        // BFS to get all projects
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
                artifactItem.value.maxCurrentVersionName = moduleLatestVersionMap[artifactItem.value.artifactId]?.versionName
                    ?: "0.0.0"
                // version from file
                var currentMaxVersion = artifactItem.value.maxCurrentVersionName.versionToInt(versionConfigMap).first
                // version from artifact (key: versionName)
                val versionArtifact = artifactItem.value.versionName.versionToInt(versionConfigMap).first
                // get the version, bigger either from the versionName of artifact or from the file in server.
                if (versionArtifact >= currentMaxVersion) {
                    currentMaxVersion = versionArtifact
                }
                val versionSuffixString = (
                    if (versionSuffix.isNotEmpty()) {
                        "-$versionSuffix"
                    } else {
                        ""
                    }
                    )
                val increasedVersionString = (
                    currentMaxVersion + (
                        versionConfigMap["Step"]
                            ?: 1
                        )
                    ).toVersion(versionConfigMap) + versionSuffixString
                artifactItem.value.increaseVersionString = increasedVersionString
                println(artifactItem.value.projectName + currentMaxVersion + " - " + increasedVersionString)
            }
        } catch (ignored: Exception) {
        }
    }

    fun checkCommitModuleToPublishAndUpdate() {
        val moduleToPublishEligible = mutableSetOf<String>()
        for (key in moduleToPublishList) {
            val currentCommitId = getCommitId(project, key)
            println("$key - $currentCommitId")
            val artifactId = projectToArtifactInfoList[key]?.artifactId ?: ""
            val commitIdInServer = moduleLatestVersionMap[artifactId]?.commidId ?: ""
            if (currentCommitId != commitIdInServer) {
                // eligible to publish because the commit id is different
                moduleToPublishEligible.add(key)
            } else {
                println("Module $key is not eligible to Publish because the commit in local and server is same.")
            }
        }
        moduleToPublishList = moduleToPublishEligible
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
