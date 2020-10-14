@file:Suppress("UnstableApiUsage")

package com.tokopedia.linter.detectors.sourcescanner.elements

import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Severity
import com.tokopedia.linter.detectors.sourcescanner.SourceCodeDetector
import org.jetbrains.uast.UImportStatement

object ImportDetector {
    private val disAllowedImportsClasses = listOf("com.tokopedia.core.R.")

    const val CLASS_IMPORT_ID = "Invalid_Import_Class"
    const val BRIEF_DESCRIPTION = "This Import is deprecated or not Allowed for this class"
    const val EXPLAINATION = "Usage of this class is not allowed"

    val CLASS_IMPORT: Issue = Issue.create(
            CLASS_IMPORT_ID,
            briefDescription = BRIEF_DESCRIPTION,
            explanation = EXPLAINATION,
            category = Category.CORRECTNESS,
            priority = 3,
            severity = Severity.WARNING,
            implementation = SourceCodeDetector.IMPLEMENTATION
    ).setAndroidSpecific(true)


    fun checkImport(context: JavaContext, import: UImportStatement) {
        checkInvalidImport(context, import)
    }

    private fun checkInvalidImport(context: JavaContext, import: UImportStatement) {
        import.importReference?.let { importReference ->
            if (disAllowedImportsClasses.any { importReference.asSourceString().contains(it) })
            {
                context.report(CLASS_IMPORT, import, context.getLocation(importReference), "Forbidden import")
            }
        }
    }
}
