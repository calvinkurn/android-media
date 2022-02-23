package com.tokopedia.play.broadcaster.helper

import android.view.View
import android.view.ViewGroup

/**
 * Created by kenny.hadisaputra on 23/02/22
 */
class ViewHierarchyPrinter(
    private val printConditions: List<PrintCondition> = emptyList(),
) {

    fun print(view: View) {
        printInternal(view = view)
    }

    private fun printInternal(level: Int = 0, view: View) {
        if (printConditions.any { !it.shouldPrint(view) }) return

        val viewInfo = buildString {
            for (i in 0 until level) {
                append("--")
            }
            if (isNotEmpty()) append(" ")
            append("${view::class.java.name} - ID: ${
                try {
                    view.resources.getResourceEntryName(view.id)
                } catch (e: Throwable) { "UNKNOWN" }
            }")
        }

        println(viewInfo)

        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                printInternal(level + 1, view.getChildAt(i))
            }
        }
    }
}

fun interface PrintCondition {

    fun shouldPrint(view: View): Boolean
}