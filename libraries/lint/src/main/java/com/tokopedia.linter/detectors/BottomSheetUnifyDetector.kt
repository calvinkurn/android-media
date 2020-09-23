package com.tokopedia.linter.detectors

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import com.intellij.psi.PsiMethod
import com.tokopedia.linter.*
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UImportStatement

class BottomSheetUnifyDetector : Detector(), SourceCodeScanner {

    companion object {

        private val disallowedImports = listOf(BOTTOM_SHEET_DIALOG, BOTTOM_SHEET_DIALOG_FRAGMENT)
        private val applicableMethodsList = listOf("findViewById", "setCancelable")
        private val implementation = Implementation(
                BottomSheetUnifyDetector::class.java,
                Scope.JAVA_FILE_SCOPE
        )
        val ISSUE: Issue = Issue.create(
                id = BOTTOM_SHEET_ISSUE_ID,
                briefDescription = WARNING_DESCRIPTION_FORMAT.format(BOTTOM_SHEET_OLD_NAME, BOTTOM_SHEET_NEW_NAME),
                explanation = WARNING_EXPLANATION_FORMAT.format(BOTTOM_SHEET_OLD_NAME, BOTTOM_SHEET_NEW_NAME).trimIndent(),
                category = Category.CORRECTNESS,
                priority = 3,
                severity = Severity.WARNING,
                implementation = implementation
        ).setAndroidSpecific(true)
    }

    override fun visitMethodCall(
            context: JavaContext,
            node: UCallExpression,
            method: PsiMethod
    ) {
        val evaluator = context.evaluator
        if (evaluator.isMemberInClass(method, BOTTOM_SHEET_DIALOG_FRAGMENT)) {
            context.report(
                    issue = ISSUE,
                    scope = node,
                    location = context.getCallLocation(
                            call = node,
                            includeReceiver = false,
                            includeArguments = false
                    ),
                    message = USAGE_ERROR_MESSAGE_FORMAT.format(BOTTOM_SHEET_NEW_NAME)
            )
        }
    }

    override fun getApplicableMethodNames(): List<String>? {
        return applicableMethodsList
    }

    override fun getApplicableUastTypes() = listOf(UImportStatement::class.java)

    override fun createUastHandler(context: JavaContext) = InvalidImportHandler(context)

    class InvalidImportHandler(private val context: JavaContext) : UElementHandler() {
        override fun visitImportStatement(node: UImportStatement) {
            node.importReference?.let { importReference ->
                if (disallowedImports.any { importReference.asSourceString().contains(it) }) {
                    context.report(DialogUnifyDetector.ISSUE, node,
                            context.getLocation(importReference), FORBIDDEN_IMPORT)
                }
            }
        }
    }
}