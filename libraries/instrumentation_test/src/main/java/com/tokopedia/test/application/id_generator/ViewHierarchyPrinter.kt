package com.tokopedia.test.application.id_generator

import android.view.View
import android.view.ViewGroup

/**
 * Created by kenny.hadisaputra on 15/04/22
 */
class ViewHierarchyPrinter(
    private val printConditions: List<PrintCondition> = emptyList(),
    private val customIdPrefix: String = "",
    private val packageName: String = "",
) {

    private val mapOfViews = mutableMapOf<View, Int>()

    fun print(view: View) {
        printInternal(view = view)
    }

    fun printAsCSV(view: View): String {
        val header = "Parent Custom ID, Parent ID, Parent Class, View Custom ID, View ID, View Class, Package Name"
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

            /**
             * Parent Custom ID
             */
            append(getViewCustomID(parent as View))
            append(", ")

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

            /**
             * View Custom ID
             */
            append(getViewCustomID(view))
            append(", ")

            append(
                try {
                    view.resources.getResourceEntryName(view.id)
                } catch (e: Throwable) { "-" }
            )
            append(", ")
            append(view::class.java.name)
            append(", ")

            appendLine(packageName.replace('.', ';'))

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

    private fun getViewCustomID(view: View): String {
        val index = if (mapOfViews.containsKey(view)) {
            mapOfViews[view]!!
        } else {
            val index = mapOfViews.keys.size
            mapOfViews[view] = index
            index
        }

        return "$customIdPrefix$index"
    }
}

fun interface PrintCondition {

    fun shouldPrint(view: View): Boolean
}