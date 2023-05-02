package com.tokopedia.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import java.io.File

open class ScanLatestVersionTask : DefaultTask() {
    @Internal
    var moduleLatestVersionMap = hashMapOf<String, ArtifactLatestVersionInfo>()

    @Internal
    val PROPERTY_KEY = "moduleLatestVersion"

    @Internal
    var successScan = false

    @TaskAction
    fun run() {
        if (project.hasProperty(PROPERTY_KEY)) {
            val pathString: String = project.properties[PROPERTY_KEY].toString()
            val file = File(pathString)
            if (file.exists()) {
                println("Read File $pathString")
                file.forEachLine { line ->
                    if (line.isNotEmpty() && !line.startsWith("//")) {
                        val lineSplit = line.split("#")
                        if (lineSplit.size == 2) {
                            val artifactId = lineSplit[0]
                            val latestVersion = lineSplit[1]
                            moduleLatestVersionMap.put(artifactId, ArtifactLatestVersionInfo(artifactId, latestVersion, ""))
                        } else if (lineSplit.size == 3) {
                            val artifactId = lineSplit[0]
                            val latestVersion = lineSplit[1]
                            val commidId = lineSplit[2]
                            moduleLatestVersionMap.put(artifactId, ArtifactLatestVersionInfo(artifactId, latestVersion, commidId))
                        }
                    }
                }
                if (moduleLatestVersionMap.isEmpty()) {
                    println("File latest version is Empty.")
                    successScan = false
                } else {
                    successScan = true
                }
            } else {
                println("File $pathString does not exist")
                successScan = false
            }
        } else {
            println("Please add property -P$PROPERTY_KEY=[Latest Version File path]")
            successScan = false
        }
    }
}
