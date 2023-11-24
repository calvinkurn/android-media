package com.tokopedia.linter.detectors.resources

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope.Companion.JAVA_FILE_SCOPE
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.SourceCodeScanner
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UFile

/** Reports an error when an R class is imported using the wrong import alias. */
class TkpdDesignResourceImportAliasDetector : Detector(), SourceCodeScanner {

    override fun getApplicableUastTypes(): List<Class<out UElement>>? {
        return listOf(UFile::class.java)
    }

    override fun createUastHandler(context: JavaContext): UElementHandler {
        return object : UElementHandler() {
            override fun visitFile(node: UFile) {
                node.imports.forEach { importStatement ->
                    val importedClassName = importStatement.importReference?.asRenderString()
                    if (importedClassName == "com.tokopedia.design.*" || importedClassName?.startsWith(
                            "com.tokopedia.design."
                        ) == true
                    ) {
                        // Report an issue
                        context.report(
                            ISSUE,
                            importStatement,
                            context.getLocation(importStatement),
                            "Avoid importing classes from com.tokopedia.design module. TkpdDesign will be deleted soon",
                            quickfixData = fix().replace().range(context.getLocation(importStatement)).with("").build()
                        )
                    }
                }
            }
        }
    }

    companion object {

        val ISSUE: Issue =
            Issue.create(
                "TkpdDesignModuleUsage",
                "Avoid importing classes from com.tokopedia.design module",
                "Importing classes from com.tokopedia.design module is not allowed in this project.TkpdDesign will be deleted soon",
                CORRECTNESS, 6, Severity.ERROR,
                Implementation(TkpdDesignResourceImportAliasDetector::class.java, JAVA_FILE_SCOPE)
            )
    }
}
