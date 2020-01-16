package com.tokopedia.plugin

import GraphStr
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import toVersion
import versionToInt
import java.io.File
import java.util.*
import kotlin.collections.HashSet

open class PublishCompositeTask : DefaultTask() {

    // this will be populated from previous tasks
    var moduleLatestLogMap = hashMapOf<String, Date>()
    var candidateModuleListToUpdate = hashSetOf<String>()
    var dependenciesProjectNameHashSet = HashSet<Pair<String, String>>()
    var projectToArtifactInfoList = hashMapOf<String, ArtifactInfo>()
    var artifactIdToProjectNameList = hashMapOf<String, String>()

    lateinit var sortedDependency: List<String>
    var versionConfigMap = mutableMapOf<String, Int>()

    var successModuleList: MutableList<String> = mutableListOf()
    var failModuleList: MutableList<String> = mutableListOf()
    var noChangeModuleList: MutableList<String> = mutableListOf()
    var changedModuleList: MutableList<String> = mutableListOf()
    var successPublish = false
    val logFile = File(LOG_FILE_PATH)

    companion object {
        const val LOG_FILE_PATH = "tools/version/log.txt"
        const val LIBRARIES_PATH = "../../buildconfig/dependencies/dependency-libraries.gradle"
        const val LIBRARIES_BACKUP_PATH = "../../buildconfig/dependencies/dependency-libraries-backup.gradle"
        const val LIBRARIES_WRITER_PATH = "../../buildconfig/dependencies/dependency-libraries-writer.gradle"
    }

    @TaskAction
    fun run() {
        println("Latest Log Date: $moduleLatestLogMap")
        println("Candidate: $candidateModuleListToUpdate")
        println("Dep Hash Set: $dependenciesProjectNameHashSet")
        println("Project to Artifact: ${projectToArtifactInfoList.map {
            it.key + "-" + it.value.groupId + ":" + it.value.artifactId +
                ":" + it.value.versionName + "/" + it.value.maxCurrentVersionName
        }}")
        println("Artifact to Project: $artifactIdToProjectNameList")

        // get sorted dependency
        sortedDependency = sortGraph().reversed()
        println("After Topological Sort $sortedDependency")

        publishToArtifactory()
    }

    private fun sortGraph(): List<String> {
        val graph = GraphStr()
        projectToArtifactInfoList.forEach {
            graph.addVertices(it.value.projectName)
        }
        dependenciesProjectNameHashSet.forEach {
            graph.addEdge(it.first, it.second)
        }
        graph.topologicalSort()
        return graph.listTop
    }

    private fun publishToArtifactory() {
        deletePreviousLog()

        backupRootDependencyLibraryFile()

        // loop all sortedDependency
        // ex: [config, url, gql]
        for (it in sortedDependency) {
            // check if it is a candidate for version increase
            if (candidateModuleListToUpdate.contains(it)) {
                //it is a valid candidate; publish to artifactory
                logFile.appendText("\n$it module - Start Updating...\n")
                val readerFile = File("${it}/build.gradle")
                val backupFile = File("${it}/.build_backup")
                val writerFile = File("${it}/build_temp.gradle")
                backupFile.delete()
                writerFile.delete()
                readerFile.copyTo(backupFile, true)
                logFile.appendText("$it module BACK-UP SUCCESS\n")

                //increase the version
                val artifactInfo = projectToArtifactInfoList[it]
                if (artifactInfo == null) {
                    logFile.appendText("$it module artifact is not defined in the project. FAILED.\n\n")
                } else {
                    val currentMaxVersion = artifactInfo.maxCurrentVersionName.versionToInt(versionConfigMap).first
                    val increasedVersionString = (currentMaxVersion + (versionConfigMap["Step"]
                        ?: 1)).toVersion(versionConfigMap)
                    artifactInfo.increaseVersionString = increasedVersionString

                    logFile.appendText("Start increasing version:${artifactInfo.versionName} -> ${artifactInfo.increaseVersionString}\n")

                    var textToPut: String
                    readerFile.forEachLine { line ->
                        textToPut = ""
                        //search for "versionName =" and change the value, but in the writer file
                        val lineTrim = line.trim()
                        if (lineTrim.startsWith("versionName =")) {
                            textToPut = "    versionName = \"$increasedVersionString\""
                        } else {
                            textToPut = line
                        }
                        writerFile.appendText("$textToPut\n")
                    }
                    readerFile.delete()
                    writerFile.renameTo(readerFile)

                    val successPublishModule = publishModule(it)
                    if (successPublishModule) {
                        val logStr = "$it PUBLISH - SUCCESS\n"
                        print(logStr)
                        logFile.appendText(logStr)
                        successModuleList.add(it)
                        changedModuleList.add(it)
                        changeRootDependencyFile(artifactInfo)
                    } else {
                        val logStr = "$it PUBLISH - FAILED\n"
                        print(logStr)
                        logFile.appendText(logStr)
                        failModuleList.add(it)
                        changedModuleList.add(it)
                        returnBackup()
                        break
                    }
                }
            } else {
                // here is no op. the module is not changed at all.
                val logStr = "\n$it module is UP-TO-DATE.\n"
                print(logStr)
                logFile.appendText(logStr)
                noChangeModuleList.add(it)
            }
        }
        successPublish = successModuleList.isNotEmpty() && failModuleList.isEmpty()
    }

    private fun backupRootDependencyLibraryFile() {
        if (candidateModuleListToUpdate.isNotEmpty()) {
            //backup rootDependencyFile
            val rootDependencyFile = File(LIBRARIES_PATH)
            val backupFile = File(LIBRARIES_BACKUP_PATH)
            backupFile.delete()
            rootDependencyFile.copyTo(backupFile, true)
            logFile.appendText("depencency-libraries.gradle BACK-UP SUCCESS\n")
        }
    }

    private fun changeRootDependencyFile(artifactInfo: ArtifactInfo) {
        val rootDependencyFile = File(LIBRARIES_PATH)
        val writerFile = File(LIBRARIES_WRITER_PATH)
        var textToPut: String
        var found = false
        rootDependencyFile.forEachLine { line ->
            textToPut = ""
            val lineTrim = line.trim().replace("\\s".toRegex(), "")
            if (!found && !lineTrim.startsWith("//")) {
                val strSplit = lineTrim.split(":".toPattern(), 2)
                if (strSplit.size == 2) {
                    val strProject = strSplit[1].substring(strSplit[1].indexOf("\""), strSplit[1].lastIndexOf("\""))
                    val projectSplit = strProject.split(":".toPattern(), 3)
                    if (projectSplit.size == 3 && projectSplit[0].contains(ScanProjectTask.TOKOPEDIA)) {
                        val artifactId = projectSplit[1]
                        if (artifactId == artifactInfo.artifactId) {
                            val versionInRoot = projectSplit[2]
                            textToPut = line.replaceFirst(versionInRoot, artifactInfo.increaseVersionString)
                            val textToPutTrim = textToPut.trim().replace("\\s".toRegex(), "")
                            logFile.appendText("Change root Dependency-libraries from \n$lineTrim\nto\n$textToPutTrim\n")
                            found = true
                        }
                    }
                }
            }
            if (textToPut.isEmpty()) {
                textToPut = line
            }
            writerFile.appendText("$textToPut\n")
        }
        rootDependencyFile.delete()
        writerFile.renameTo(rootDependencyFile)
        if (!found) {
            logFile.appendText("No change for root Dependency-libraries for " + artifactInfo.artifactId)
        }
    }

    private fun returnBackup() {
        returnBackupForCompositeProject()
        returnBackupForRootDependencyLibraries()
    }

    private fun returnBackupForCompositeProject() {
        changedModuleList.forEach {
            val reader = File("${it}/build.gradle")
            val backup = File("${it}/.build_backup")
            if (backup.exists()) {
                reader.delete()
                backup.renameTo(reader)
                logFile.appendText("Returning backup for $it\n")
            } else {
                backup.delete()
            }
        }
    }

    private fun returnBackupForRootDependencyLibraries() {
        val reader = File(LIBRARIES_PATH)
        val backup = File(LIBRARIES_BACKUP_PATH)
        if (backup.exists()) {
            reader.delete()
            backup.renameTo(reader)
            logFile.appendText("Returning backup for dependency-libraries.gradle\n")
        } else {
            backup.delete()
        }
    }

    private fun publishModule(module: String): Boolean {
        val moduleLogFile = File("${module}/${module}_log.txt")
        moduleLogFile.delete()

        val outputFile = File("$module/build/outputs/aar/$module.aar")
        if (outputFile.exists()) {
            outputFile.delete()
        }
        
        val gitCommandAssembleString = "./gradlew assembleDebug  -p $module --stacktrace"
        val gitCommandAssembleResultString = gitCommandAssembleString.runCommandGroovy(project.projectDir.absoluteFile)?.trimSpecial() ?: ""
        if (!gitCommandAssembleResultString.contains("BUILD SUCCESSFUL")) {
            return false
        }
        val outputFile2 = File("$module/build/outputs/aar/$module-debug.aar")
        if (outputFile2.exists()) {
            outputFile2.renameTo(outputFile)
        }
        val gitCommandString = "./gradlew artifactoryPublish  -p $module --stacktrace"
        val gitResultLog = gitCommandString.runCommandGroovy(project.projectDir.absoluteFile)?.trimSpecial() ?: ""
        moduleLogFile.appendText(gitResultLog)
        return gitResultLog.contains("BUILD SUCCESSFUL")
    }

    private fun deletePreviousLog() {
        val logFile = File(LOG_FILE_PATH)
        logFile.delete()
    }
}