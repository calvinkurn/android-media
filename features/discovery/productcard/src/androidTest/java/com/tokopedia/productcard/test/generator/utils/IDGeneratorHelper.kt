package com.tokopedia.productcard.test.generator.utils

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.productcard.BuildConfig
import com.tokopedia.test.application.id_generator.FileWriter
import com.tokopedia.test.application.id_generator.PrintCondition
import com.tokopedia.test.application.id_generator.ViewHierarchyPrinter
import com.tokopedia.test.application.id_generator.writeGeneratedViewIds

object IDGeneratorHelper {
    private val printConditions = listOf(
        PrintCondition { view ->
            view.id != View.NO_ID || view is ViewGroup
        }
    )

    private val viewPrinter = ViewHierarchyPrinter(
        printConditions,
        packageName = BuildConfig.LIBRARY_PACKAGE_NAME,
    )
    private val fileWriter = FileWriter()

    fun printView(view: RecyclerView.ViewHolder, fileName: String) {
        val text = viewPrinter.printAsCSV(
            view.itemView
        )
        fileWriter.writeGeneratedViewIds(
            "product_card_${fileName}.csv",
            text
        )
    }
}
