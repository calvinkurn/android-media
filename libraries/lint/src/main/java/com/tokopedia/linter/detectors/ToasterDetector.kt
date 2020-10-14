package com.tokopedia.linter.detectors

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import com.intellij.psi.PsiMethod
import com.tokopedia.linter.*
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UImportStatement

class ToasterDetector : Detector(), SourceCodeScanner {
    override fun getApplicableUastTypes() = listOf(UImportStatement::class.java)

    override fun createUastHandler(context: JavaContext) = InvalidImportHandler(context)

    override fun visitMethodCall(
            context: JavaContext,
            node: UCallExpression,
            method: PsiMethod
    ) {
        val evaluator = context.evaluator
        if (evaluator.isMemberInClass(method, SNACKBAR_OLD_NAME)) {
            reportUsage(context, node)
        }
    }

    override fun getApplicableMethodNames(): List<String>? {
        return listOf("make", "show")
    }

    private fun reportUsage(
            context: JavaContext,
            node: UCallExpression
    ) {
        context.report(
                issue = SetColourDetector.ISSUE,
                scope = node,
                location = context.getCallLocation(
                        call = node,
                        includeReceiver = false,
                        includeArguments = false
                ),
                message = USAGE_ERROR_MESSAGE_FORMAT.format(SNACKBAR_NEW_NAME)
        )
    }

    class InvalidImportHandler(private val context: JavaContext) : UElementHandler() {
        override fun visitImportStatement(node: UImportStatement) {
            node.importReference?.let { importReference ->
                if (disallowedImports.any { importReference.asSourceString().contains(it) }) {
                    context.report(ISSUE, node, context.getLocation(importReference), FORBIDDEN_IMPORT)
                }
            }
        }
    }

    companion object {
        private val disallowedImports = listOf(SNACKBAR_OLD_NAME)
        private val IMPLEMENTATION = Implementation(
                ToasterDetector::class.java,
                Scope.JAVA_FILE_SCOPE
        )

        val ISSUE: Issue = Issue.create(
                id = SNACKBAR_ISSUE_ID,
                briefDescription = WARNING_DESCRIPTION_FORMAT.format(SEARCHBAR_OLD_NAME, SEARCHBAR_NEW_NAME),
                explanation = WARNING_EXPLANATION_FORMAT.format(SEARCHBAR_OLD_NAME, SEARCHBAR_NEW_NAME),
                category = Category.CORRECTNESS,
                priority = 3,
                severity = Severity.WARNING,
                implementation = IMPLEMENTATION
        ).setAndroidSpecific(true)
    }
}
