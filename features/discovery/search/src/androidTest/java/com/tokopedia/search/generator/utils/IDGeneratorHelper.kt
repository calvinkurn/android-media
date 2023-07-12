package com.tokopedia.search.generator.utils

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.contrib.RecyclerViewActions
import com.tokopedia.search.BuildConfig
import com.tokopedia.search.getProductListAdapter
import com.tokopedia.search.perform
import com.tokopedia.test.application.id_generator.FileWriter
import com.tokopedia.test.application.id_generator.PrintCondition
import com.tokopedia.test.application.id_generator.ViewHierarchyPrinter
import com.tokopedia.test.application.id_generator.writeGeneratedViewIds

object IDGeneratorHelper {
    private val printConditions = listOf(
        PrintCondition { view ->
            val parent = (view.parent as? ViewGroup) ?: return@PrintCondition true
            val packageName = parent::class.java.`package`?.name.orEmpty()
            val className = parent::class.java.name
            !packageName.startsWith("com.tokopedia") || !className.contains(
                "unify",
                ignoreCase = true
            )
        },
        PrintCondition { view ->
            view.id != View.NO_ID || view is ViewGroup
        }
    )

    private val rootPrintCondition = listOf(
        PrintCondition { view ->
            view.parent !is RecyclerView
        }
    ) + printConditions

    private val viewPrinter = ViewHierarchyPrinter(
        printConditions,
        packageName = BuildConfig.LIBRARY_PACKAGE_NAME
    )
    private val rootViewPrinter = ViewHierarchyPrinter(
        rootPrintCondition,
        packageName = BuildConfig.LIBRARY_PACKAGE_NAME
    )
    private val fileWriter = FileWriter()
    private val savedFileName = mutableListOf<String>()

    fun printView(view: View, fileName: String) {
        val text = viewPrinter.printAsCSV(view)
        saveFile(text, fileName)
    }

    fun printRootView(view: View) {
        val text = rootViewPrinter.printAsCSV(view)
        saveFile(text, "rootView")
    }

    fun scrollAndPrintView(recyclerView: RecyclerView?) {
        Thread.sleep(1000)

        val visitableList = recyclerView.getProductListAdapter().itemList

        visitableList.forEachIndexed { index, visitable ->
            recyclerView.perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(index))

            recyclerView?.findViewHolderForAdapterPosition(index)?.let {
                printView(it.itemView, it.javaClass.simpleName)
            }
        }
    }

    private fun saveFile(text: String, fileName: String) {
        if (!savedFileName.contains(fileName)) {
            fileWriter.writeGeneratedViewIds(
                "search_$fileName.csv",
                text
            )
            savedFileName.add(fileName)
        }
    }
}
