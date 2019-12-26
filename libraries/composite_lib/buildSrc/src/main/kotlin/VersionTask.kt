package com.tokopedia.plugin

import org.codehaus.groovy.runtime.IOGroovyMethods
import org.codehaus.groovy.runtime.ProcessGroovyMethods
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.concurrent.TimeUnit

open class VersionTask : DefaultTask() {

    var listReleaseDate = mutableListOf<String>()
    val listTree: List<Pair<String, String>> = mutableListOf()
    val listVersion: List<Pair<String, String>> = mutableListOf()

    val file = File ("tools/version/release_date.txt")

    @TaskAction
    fun run() {
        println("[Start Version Task]")
        file.forEachLine {
            listReleaseDate.add(it)
        }
        println(listReleaseDate)
    }

    fun getVersionName(module:String):String {
        val gitLog = "git log -1 --pretty=\'%ad\' --date=format:\'%Y-%m-%d,%H:%M:%S\' $module"
        val text = ProcessGroovyMethods.getText(Runtime.getRuntime().exec(gitLog))
        return text.trim().replace("'", "").replace(","," ")
    }
}