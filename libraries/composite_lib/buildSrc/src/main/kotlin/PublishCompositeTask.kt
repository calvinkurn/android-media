package com.tokopedia.plugin

import GraphStr
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashSet

open class PublishCompositeTask : DefaultTask() {

    // this will be populated from previous tasks
    var latestReleaseDate: Date = Date()
    var candidateModuleListToUpdate = hashSetOf<String>()
    var dependenciesHashSet = HashSet<Pair<String, String>>()
    var versionProjectToArtifactList = hashMapOf<String, VersionModelB>()
    var versionArtifactToProjectList = hashMapOf<String, String>()

    val dateFormatter = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
    lateinit var sortedDependency:List<String>

    companion object {
        const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
        const val LOG_FILE_PATH = "tools/version/log.txt"
    }

    @TaskAction
    fun run() {
        println("Latest Release Date: $latestReleaseDate")
        println("Candidate: $candidateModuleListToUpdate")
        println("Dep Hash Set: $dependenciesHashSet")
        println("Project to Artifact: ${versionProjectToArtifactList.map { it.key + "-" + it.value.groupId + ":" + it.value.artifactId + ":" + it.value.versionName }}")
        println("Artifact to Project: $versionArtifactToProjectList")

        // get sorted dependency
        sortedDependency = sortGraph().reversed()
        println("After Topological Sort $sortedDependency")

        publishToArtifactory()
    }

    fun sortGraph(): List<String> {
        val graph = GraphStr()
        versionProjectToArtifactList.forEach{
            graph.addVertices(it.value.projectName)
        }
        dependenciesHashSet.forEach {
            graph.addEdge(it.first, it.second)
        }
        graph.topologicalSort()
        return graph.listTop
    }

    fun publishToArtifactory(){
        deletePreviousLog()
        // loop all sortedDependency
        sortedDependency.forEach {
            // check if it is a candidate for version increase
            if (candidateModuleListToUpdate.contains(it)) {
                //it is a valid candidate; publish to artifactory
            }
        }
    }

    fun deletePreviousLog(){
        val logFile = File(LOG_FILE_PATH)
        logFile.delete()
    }
}