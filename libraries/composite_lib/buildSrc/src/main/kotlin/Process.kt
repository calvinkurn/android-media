package com.tokopedia.plugin

import org.codehaus.groovy.runtime.ProcessGroovyMethods
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

fun String.runCommand(workingDir: File): String? {
    try {
        val parts = this.split("\\s".toRegex())
        val proc = ProcessBuilder(*parts.toTypedArray())
            .directory(workingDir)
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start()

        proc.waitFor(60, TimeUnit.MINUTES)
        return proc.inputStream.bufferedReader().readText()
    } catch (e: IOException) {
        e.printStackTrace()
        return null
    }
}

fun String.runCommandGroovy(workingDir: File): String? {
    try {
        return ProcessGroovyMethods.getText(Runtime.getRuntime().exec(this))
    } catch (e: IOException) {
        e.printStackTrace()
        return null
    }
}

fun String.trimSpecial(): String {
    return trim()
        .replace("'", "")
        .replace(",", " ")
}