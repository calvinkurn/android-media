package com.tokopedia.linter.detectors

import com.android.tools.lint.detector.api.*
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression

@Suppress("UnstableApiUsage")
class SetColourDetector : Detector(), SourceCodeScanner {

    override fun getApplicableMethodNames(): List<String>? =
            listOf(
                    "getColor"
            )

    override fun visitMethodCall(
            context: JavaContext,
            node: UCallExpression,
            method: PsiMethod
    ) {
        val evaluator = context.evaluator
        if (evaluator.isMemberInClass(method, "androidx.core.content.ContextCompat")) {
            reportUsage(context, node)
        }
        if (evaluator.isMemberInClass(method, "android.content.res.Resources")) {
            reportUsage(context, node)
        }
    }

    private fun reportUsage(
            context: JavaContext,
            node: UCallExpression
    ) {
        context.report(
                issue = ISSUE,
                scope = node,
                location = context.getCallLocation(
                        call = node,
                        includeReceiver = false,
                        includeArguments = false
                ),
                message = "Usage of `getColour()` is prohibited, Use MethodChecker.getColour()"
        )
    }

    companion object {
        private val IMPLEMENTATION = Implementation(
                SetColourDetector::class.java,
                Scope.JAVA_FILE_SCOPE
        )

        val ISSUE: Issue = Issue.create(
                id = "SetColourUsageError",
                briefDescription = "Use Method Checker instead",
                explanation = """
                    This lint check prevents usage of `ContextCompact.getColour() & resources.getColour()`.
                """.trimIndent(),
                category = Category.CORRECTNESS,
                priority = 3,
                severity = Severity.WARNING,
                implementation = IMPLEMENTATION
        ).setAndroidSpecific(true)
    }
}