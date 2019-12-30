package com.tokopedia.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashSet

open class PublishCompositeTask : DefaultTask() {

    // this will be populated from previous tasks
    var latestReleaseDateString: String = ""
    var candidateModuleListToUpdate = mutableListOf<String>()
    var dependenciesHashSet = HashSet<Pair<String, String>>()
    var versionList = hashMapOf<String, VersionModelB>()

    val dateFormatter = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())

    lateinit var latestReleaseDate:Date

    companion object {
        const val TOKOPEDIA = "tokopedia"
        const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
    }

    @TaskAction
    fun run() {
        latestReleaseDate = dateFormatter.parse(latestReleaseDateString)

        println(latestReleaseDateString)
        println(candidateModuleListToUpdate)
        println(dependenciesHashSet)
        println(versionList)
    }
}