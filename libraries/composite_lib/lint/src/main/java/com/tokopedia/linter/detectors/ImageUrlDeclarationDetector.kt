package com.tokopedia.linter.detectors

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.intellij.psi.PsiModifier
import com.intellij.psi.PsiNamedElement
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UField
import org.jetbrains.uast.ULocalVariable

/**
 * Created by @ilhamsuaib on 28/05/23.
 */

@Suppress("UnstableAPIUsage")
class ImageUrlDeclarationDetector : Detector(), Detector.UastScanner {

    companion object {
        val JAVA_ISSUE = Issue.create(
            id = "ImageUrlDeclarationDetector",
            briefDescription = "Image URL declaration outside of `libraries/images_assets` module is not allowed",
            explanation = "In order to make a centralized remote resources. So, you have to create the constant variable " +
                "for this in `libraries/images_assets` module instead.",
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
        private const val EXCLUDED_DIR = "src/main/java/com/tokopedia/imageassets"
        private const val IMAGE_URL_PATTERN = "(http.*?(gif|jpeg|png|jpg|bmp))"
        private val VARIABLES_MODIFIER = arrayOf("constval", "val", "constvar", "var")
    }

    override fun getApplicableUastTypes() = listOf<Class<out UElement>>(
        UField::class.java,
        ULocalVariable::class.java
    )

    override fun createUastHandler(context: JavaContext): UElementHandler {
        return ElementHandler(context)
    }

    inner class ElementHandler(private val context: JavaContext) : UElementHandler() {

        override fun visitField(node: UField) {
            try {
                if (isVariable(node.text)) {
                    checkImageUrl(context, node)
                }
            } catch (ignore: Exception) {
            }
        }

        override fun visitLocalVariable(node: ULocalVariable) {
            checkImageUrl(context, node)
        }

        private fun isVariable(text: String?): Boolean {
            if (text.isNullOrBlank()) return false
            var textFmt = text.replace(" ", "")
            PsiModifier.MODIFIERS.forEach {
                textFmt = textFmt.replace(it, "")
            }
            return VARIABLES_MODIFIER.any { textFmt.startsWith(it) }
        }

        private fun checkImageUrl(
            context: JavaContext,
            element: PsiNamedElement
        ) {
            val variableValue = element.text
            val isImageUrl = getIsImageUrl(variableValue)
            val filePath = context.getNameLocation(element).file.path
            val isExcludedDir = filePath.contains(EXCLUDED_DIR)
            val shouldReportIssue = isImageUrl && !isExcludedDir
            if (shouldReportIssue) {
                reportIssue(context, element)
            }
        }

        private fun getIsImageUrl(variableValue: String): Boolean {
            val regex = IMAGE_URL_PATTERN.toRegex()
            return variableValue.contains(regex)
        }

        private fun reportIssue(context: JavaContext, element: PsiNamedElement) {
            context.report(
                JAVA_ISSUE,
                element,
                context.getLocation(element),
                ERROR_MESSAGE
            )
        }
    }
}
