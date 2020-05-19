package com.tokopedia.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File

open class PrintLatestVersionTask : DefaultTask() {
    //input
    var moduleLatestVersionMap = hashMapOf<String, ArtifactLatestVersionInfo>()
    var successModuleList: MutableList<String> = mutableListOf()
    var projectToArtifactInfoList = hashMapOf<String, ArtifactInfo>()
    var artifactIdToProjectNameList = hashMapOf<String, String>()

    companion object {
        const val VERSION_OUTPUT_PATH = "../../latest_version_output.txt"
    }

    @TaskAction
    fun run() {
        val outputVersionFile = File(VERSION_OUTPUT_PATH)
        if (outputVersionFile.exists() && outputVersionFile.isFile) {
            outputVersionFile.delete()
        }
        outputVersionFile.createNewFile()

        // to sustain the old version in file, we loop from the original and only update for the success module
        for ((key, value) in moduleLatestVersionMap) {
            // because the successModuleList is "projectName", we need to map from id to name
            val projectName = artifactIdToProjectNameList[key] ?: ""
            if (successModuleList.contains(projectName)) {
                //instead writing for the original version, we overwrite with the success published version
                val commitId = getCommitId(projectName)
                outputVersionFile.appendText("$key#${projectToArtifactInfoList[projectName]?.increaseVersionString}#${commitId}\n")
            } else {
                outputVersionFile.appendText("$key#${value.versionName}#${value.commidId}\n")
            }
        }
        //add the version for the new modules
        //loop for every module is successModuleList that not in the moduleLatestVersionMap
        for (successModule in successModuleList) {
            //get artifact Info for the project
            val artifactInfo = projectToArtifactInfoList[successModule]
            val artifactId = artifactInfo?.artifactId
            if (!moduleLatestVersionMap.contains(artifactId)) {
                val commitId = getCommitId(successModule)
                outputVersionFile.appendText("$artifactId#${artifactInfo?.increaseVersionString}#${commitId}\n")
            }
        }
    }

    fun getCommitId (projectName:String):String {
        val gitLog = "git log -1 --pretty=format:\'%h\' ${projectName}"
        return gitLog.runCommand(project.projectDir.absoluteFile)?.trimSpecial() ?: ""
    }
}