package com.tokopedia.search.generator.utils

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.contrib.RecyclerViewActions
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

    private val viewPrinter = ViewHierarchyPrinter(printConditions)
    private val fileWriter = FileWriter()
    private val savedFileName = mutableListOf<String>()

    private fun printView(view: RecyclerView.ViewHolder, fileName: String) {
        val text = viewPrinter.printAsCSV(
            view.itemView
        )
        if (!savedFileName.contains(fileName)) {
            fileWriter.writeGeneratedViewIds(
                "search_${fileName}.csv",
                text
            )
            savedFileName.add(fileName)
        }
    }

    fun scrollAndPrintView(recyclerView: RecyclerView?) {
        val visitableList = recyclerView.getProductListAdapter().itemList

        visitableList.forEachIndexed { index, visitable ->
            recyclerView.perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(index))

            recyclerView?.findViewHolderForAdapterPosition(index)?.let {
               printView(it, it.javaClass.simpleName)
            }
        }
    }
}
