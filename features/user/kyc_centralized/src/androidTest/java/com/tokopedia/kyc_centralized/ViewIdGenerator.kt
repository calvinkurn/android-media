package com.tokopedia.kyc_centralized

import android.view.View
import com.tokopedia.instrumentation.test.BuildConfig
import com.tokopedia.test.application.id_generator.FileWriter
import com.tokopedia.test.application.id_generator.PrintCondition
import com.tokopedia.test.application.id_generator.ViewHierarchyPrinter
import com.tokopedia.test.application.id_generator.writeGeneratedViewIds

object ViewIdGenerator {
    private val fileWriter = FileWriter()

    private val printConditions = listOf(
        PrintCondition {
            return@PrintCondition true
        }
    )

    private val viewPrinter = ViewHierarchyPrinter(
        printConditions = printConditions,
        packageName = BuildConfig.LIBRARY_PACKAGE_NAME
    )

    fun createViewIdFile(view: View, fileNameWithExtension: String) {
        val hierarchyInfo = viewPrinter.printAsCSV(view = view)

        fileWriter.writeGeneratedViewIds(
            fileName = fileNameWithExtension,
            text = hierarchyInfo
        )
    }
}
