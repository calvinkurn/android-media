package com.tokopedia.plugin

import GraphStr
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File

open class PublishCompositeTask : DefaultTask() {

    //input:
    var dependenciesProjectNameHashSet = HashSet<Pair<String, String>>()
    var projectToArtifactInfoList = hashMapOf<String, ArtifactInfo>()
    var artifactIdToProjectNameList = hashMapOf<String, String>()
    var moduleToPublishList = mutableSetOf<String>()

    lateinit var sortedDependency: List<String>

    var successModuleList: MutableList<String> = mutableListOf()
    var failModuleList: MutableList<String> = mutableListOf()
    var noChangeModuleList: MutableList<String> = mutableListOf()
    var changedModuleList: MutableList<String> = mutableListOf()

    var successPublish = false

    companion object {
        const val LIBRARIES_PATH = "../../buildconfig/dependencies/dependency-libraries.gradle"
        const val LIBRARIES_BACKUP_PATH = "../../buildconfig/dependencies/dependency-libraries-backup.gradle"
        const val LIBRARIES_WRITER_PATH = "../../buildconfig/dependencies/dependency-libraries-writer.gradle"

        const val TKPD_INTERNAL_LIB_DEPENDENCIES = "tkpdInternalLibDependencies"
    }

    @TaskAction
    fun run() {
        println("Dep Hash Set: $dependenciesProjectNameHashSet")
        println("Project to Artifact: ${projectToArtifactInfoList.map {
            it.key + "-" + it.value.groupId + ":" + it.value.artifactId +
                ":" + it.value.versionName + "/" + it.value.maxCurrentVersionName
        }}")
        println("Artifact to Project: $artifactIdToProjectNameList")
        println("Module to Publish: $moduleToPublishList")

        if (moduleToPublishList.isEmpty()){
            return
        }

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
        backupRootDependencyLibraryFile()

        // loop all sortedDependency
        // ex: [config, url, gql]
        for (it in sortedDependency) {
            // check if it is a candidate for version increase
            if (moduleToPublishList.contains(it)) {
                //it is a valid candidate; publish to artifactory
                print("\n$it module - Start Updating...\n")
                val readerFile = File("${it}/build.gradle")
                val backupFile = File("${it}/.build_backup")
                val writerFile = File("${it}/build_temp.gradle")
                backupFile.delete()
                writerFile.delete()
                readerFile.copyTo(backupFile, true)
                print("$it module BACK-UP SUCCESS\n")

                //increase the version
                val artifactInfo = projectToArtifactInfoList[it]
                if (artifactInfo == null) {
                    print("$it module artifact is not defined in the project. FAILED.\n\n")
                } else {
                    print("Start increasing version:${artifactInfo.versionName} -> ${artifactInfo.increaseVersionString}\n")

                    var textToPut: String
                    readerFile.forEachLine { line ->
                        textToPut = ""
                        //search for "versionName =" and change the value, but in the writer file
                        val lineTrim = line.trim()
                        if (lineTrim.startsWith("versionName =")) {
                            textToPut = "    versionName = \"${artifactInfo.increaseVersionString}\""
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
                        successModuleList.add(it)
                        changedModuleList.add(it)
                        changeRootDependencyFile(artifactInfo)
                    } else {
                        val logStr = "$it PUBLISH - FAILED\n"
                        print(logStr)
                        failModuleList.add(it)
                        changedModuleList.add(it)
                        returnBackup()
                        break
                    }
                }
            } else {
                // here is no op. Module is not changed.
                noChangeModuleList.add(it)
            }
        }
        successPublish = successModuleList.isNotEmpty() && failModuleList.isEmpty()
    }

    private fun backupRootDependencyLibraryFile() {
        if (moduleToPublishList.isNotEmpty()) {
            //backup rootDependencyFile
            val rootDependencyFile = File(LIBRARIES_PATH)
            val backupFile = File(LIBRARIES_BACKUP_PATH)
            backupFile.delete()
            rootDependencyFile.copyTo(backupFile, true)
            print("depencency-libraries.gradle BACK-UP SUCCESS\n")
        }
    }

    private fun changeRootDependencyFile(artifactInfo: ArtifactInfo) {
        val rootDependencyFile = File(LIBRARIES_PATH)
        val writerFile = File(LIBRARIES_WRITER_PATH)
        var textToPut: String
        var found = false
        var isInDependenciesLib = false
        rootDependencyFile.forEachLine { line ->
            textToPut = ""
            val lineTrim = line.trim().replace("\\s".toRegex(), "")
            if (!found && !lineTrim.startsWith("//")) {
                if (isInDependenciesLib) {
                    val strSplit = lineTrim.split(":".toPattern(), 2)
                    if (strSplit.size == 2) {
                        val strProject = strSplit[1].substring(strSplit[1].indexOf("\""), strSplit[1].lastIndexOf("\""))
                        val projectSplit = strProject.split(":".toPattern(), 3)
                        if (projectSplit.size == 3) {
                            val artifactId = projectSplit[1]
                            if (artifactId == artifactInfo.artifactId) {
                                val versionInRoot = projectSplit[2]
                                textToPut = line.replaceFirst(versionInRoot, artifactInfo.increaseVersionString)
                                val textToPutTrim = textToPut.trim().replace("\\s".toRegex(), "")
                                print("Change root Dependency-libraries from \n$lineTrim\nto\n$textToPutTrim\n")
                                found = true
                            }
                        }
                    }
                    if (lineTrim.contains("]")) {
                        isInDependenciesLib = false
                    }
                } else { // we check if found tkpdInternalLibDependencies
                    if (lineTrim.contains(TKPD_INTERNAL_LIB_DEPENDENCIES)) {
                        isInDependenciesLib = true
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
            print("No change for root Dependency-libraries for " + artifactInfo.artifactId)
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
                print("Returning backup for $it\n")
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
            print("Returning backup for dependency-libraries.gradle\n")
        } else {
            backup.delete()
        }
    }

    private fun publishModule(module: String): Boolean {

        val outputFile = File("$module/build/outputs/aar/$module.aar")
        val outputFile2 = File("$module/build/outputs/aar/$module-debug.aar")
        if (outputFile.exists()) {
            outputFile.delete()
        }
        if (outputFile2.exists()) {
            outputFile2.delete()
        }
        
        val gitCommandAssembleString = "./gradlew assembleDebug  -p $module --stacktrace"
        val gitCommandAssembleResultString = gitCommandAssembleString.runCommandGroovy(project.projectDir.absoluteFile)?.trimSpecial() ?: ""
        if (!gitCommandAssembleResultString.contains("BUILD SUCCESSFUL")) {
            return false
        }
        if (outputFile2.exists()) {
            outputFile2.copyTo(outputFile, true)
        }

        val gitCommandString = "./gradlew artifactoryPublish  -p $module --stacktrace"
        val gitResultLog = gitCommandString.runCommandGroovy(project.projectDir.absoluteFile)?.trimSpecial() ?: ""
        print(gitResultLog)
        return gitResultLog.contains("BUILD SUCCESSFUL")
    }

}