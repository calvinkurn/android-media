package com.tokopedia.media.picker.helper

import android.view.View
import com.tokopedia.instrumentation.test.BuildConfig
import com.tokopedia.test.application.id_generator.FileWriter
import com.tokopedia.test.application.id_generator.PrintCondition
import com.tokopedia.test.application.id_generator.ViewHierarchyPrinter
import com.tokopedia.test.application.id_generator.writeGeneratedViewIds

object ViewIdGenerator {
    private val writer = FileWriter()

    private val printConditions = listOf(
        PrintCondition {
            return@PrintCondition true
        }
    )

    private val views = ViewHierarchyPrinter(
        printConditions = printConditions,
        packageName = BuildConfig.LIBRARY_PACKAGE_NAME
    )

    fun create(view: View, fileName: String) {
        val info = views.printAsCSV(view)

        writer.writeGeneratedViewIds(
            fileName = fileName,
            text = info
        )
    }
}
