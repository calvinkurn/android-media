package com.tokopedia.linter.detectors

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.SourceCodeScanner
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UField
import org.jetbrains.uast.ULocalVariable

/**
 * Created by @ilhamsuaib on 25/05/23.
 */

@Suppress("UnstableApiUsage")
class ImageUrlDeclarationDetector : Detector(), SourceCodeScanner {

    companion object {

        @JvmField
        val JAVA_ISSUE = Issue.create(
            id = "ImageUrlDeclarationDetector",
            briefDescription = "Image resource declaration disallowed here.",
            explanation = "Please create a variable in common module instead.",
            category = Category.CORRECTNESS,
            priority = 10,
            severity = Severity.FATAL,
            implementation = Implementation(
                ImageUrlDeclarationDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )

        private const val ERROR_MESSAGE =
            "You can't put image url in this class. Please put in common module instead"
    }

    override fun getApplicableUastTypes(): List<Class<out UElement>> {
        return listOf(
            UField::class.java,
            ULocalVariable::class.java
        )
    }

    override fun createUastHandler(context: JavaContext): UElementHandler {
        return object : UElementHandler() {
            override fun visitField(node: UField) {
                val variableValue = node.text.substringAfter("=")
                reportIssue(context, node, variableValue)
            }

            override fun visitLocalVariable(node: ULocalVariable) {
                val variableValue = node.text.substringAfter("=")
                reportIssue(context, node, variableValue)
            }
        }
    }

    private fun reportIssue(context: JavaContext, node: UElement, variableValue: String) {
        val shouldReport = variableValue.endsWith(".png")
        if (shouldReport) {
            context.report(
                ResourcePackageDetector.JAVA_ISSUE,
                node,
                context.getLocation(node),
                ERROR_MESSAGE
            )
        }
    }
}