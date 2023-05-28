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
 * Created by @ilhamsuaib on 28/05/23.
 */

@Suppress("UnstableAPIUsage")
class ImageUrlDeclarationDetector : Detector(), SourceCodeScanner {

    companion object {
        val JAVA_ISSUE = Issue.create(
            id = "ImageUrlDeclarationDetector",
            briefDescription = "Image URL declaration outside of `libraries/images_assets` module is not allowed",
            explanation = "Please create constant variable for this in `libraries/images_assets` module instead",
            category = Category.CORRECTNESS,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                ImageUrlDeclarationDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )

        private const val ERROR_MESSAGE =
            "You are not allowed to create image url declaration here. " +
                    "Should be moved to `libraries/images_assets` module."
        private const val VARIABLE_VALUE_DELIMITER = "="
        private const val EXCLUDED_DIR = "libraries/image_assets/"
        private const val IMAGE_URL_PATTERN = "/(http.*?(gif|jpeg|png|jpg|bmp))/g"
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
                val variableValue = node.text.substringAfter(VARIABLE_VALUE_DELIMITER)
                checkImageUrl(context, node, variableValue)
            }

            override fun visitLocalVariable(node: ULocalVariable) {
                val variableValue = node.text.substringAfter(VARIABLE_VALUE_DELIMITER)
                checkImageUrl(context, node, variableValue)
            }
        }
    }

    private fun checkImageUrl(context: JavaContext, node: UElement, variableValue: String) {
        val isImageUrl = getIsImageUrl(variableValue)
        val isExcludedDir = context.getNameLocation(node).file.path.contains(EXCLUDED_DIR)
        val shouldReportIssue = isImageUrl && !isExcludedDir
        if (shouldReportIssue) {
            reportIssue(context, node)
        }
    }

    private fun getIsImageUrl(variableValue: String): Boolean {
        val regex = IMAGE_URL_PATTERN.toRegex()
        return regex.matches(variableValue)
    }

    private fun reportIssue(context: JavaContext, node: UElement) {
        context.report(
            ResourcePackageDetector.JAVA_ISSUE,
            node,
            context.getLocation(node),
            ERROR_MESSAGE
        )
    }
}