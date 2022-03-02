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

    fun printAsCSV(view: View): String {
        val header = "Parent ID, Parent Class, View ID, View Class"
        val body = printAsCSVInternal(view)

        return buildString {
            appendLine(header)
            appendLine(body)
        }
    }

    private fun printAsCSVInternal(view: View): String? {
        if (printConditions.any { !it.shouldPrint(view) }) return null

        val viewInfo = buildString {
            val parent = view.parent
            if (parent is ViewGroup) {
                append(
                    try {
                        parent.resources.getResourceEntryName(parent.id)
                    } catch (e: Throwable) { "-" }
                )
                append(", ")
                append(parent::class.java.name)
            } else append("-, -")

            append(", ")

            append(
                try {
                    view.resources.getResourceEntryName(view.id)
                } catch (e: Throwable) { "-" }
            )
            append(", ")
            appendLine(view::class.java.name)

            if (view is ViewGroup) {
                for (i in 0 until view.childCount) {
                    val info = printAsCSVInternal(view.getChildAt(i))
                    if (info != null) append(info)
                }
            }
        }

        return viewInfo
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