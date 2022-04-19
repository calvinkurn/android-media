package com.tokopedia.linter.detectors

import com.android.SdkConstants
import com.android.resources.ResourceFolderType
import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.LintFix
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.SourceCodeScanner
import com.android.tools.lint.detector.api.XmlContext
import com.android.tools.lint.detector.api.XmlScanner
import org.jetbrains.uast.UElement
import org.jetbrains.uast.ULiteralExpression
import org.w3c.dom.Attr
import org.w3c.dom.Element

class UnsupportedColorDetector : Detector(), SourceCodeScanner, XmlScanner {
    companion object {
        val JAVA_ISSUE = Issue.create(
            id = "UnsupportedDarkModeColor",
            briefDescription = "Color not supported for dark mode.",
            explanation = "Do not hardcode color, use unify color or " +
                "add color resource with dms prefix.",
            category = Category.CORRECTNESS,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                UnsupportedColorDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )

        val XML_ISSUE = Issue.create(
            id = "UnsupportedDarkModeColor",
            briefDescription = "Color not supported for dark mode.",
            explanation = "Color not supported for dark mode. Use unify color or add dms prefix.",
            category = Category.CORRECTNESS,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                UnsupportedColorDetector::class.java,
                Scope.RESOURCE_FILE_SCOPE
            )
        )

        private const val UNDERSCORE = "_"
        private const val DMS_PREFIX = "dms"
        private const val XML_ERROR_MESSAGE = "Color not supported for dark mode. " +
            "Use unify color or add dms prefix."
        private const val JAVA_ERROR_MESSAGE = "Do not hardcode color, use unify color or " +
            "add color resource with dms prefix."
    }

    override fun appliesTo(folderType: ResourceFolderType): Boolean {
        return folderType == ResourceFolderType.VALUES
    }

    override fun getApplicableElements(): Collection<String> {
        return listOf(SdkConstants.TAG_COLOR)
    }

    override fun visitElement(context: XmlContext, element: Element) {
        val attribute = element.getAttributeNode("name")
        if (!attribute.value.contains(DMS_PREFIX)) {
            reportXmlError(context, element, attribute)
        }
    }

    override fun getApplicableUastTypes(): List<Class<out UElement>> {
        return listOf(ULiteralExpression::class.java)
    }

    override fun createUastHandler(context: JavaContext): UElementHandler {
        return object : UElementHandler() {
            override fun visitLiteralExpression(node: ULiteralExpression) {
                val value = node.value.toString()
                if(value.matches(Regex("^#(?:[0-9a-fA-F]{3}){1,2}\$"))) {
                    reportJavaError(context, node)
                }
            }
        }
    }

    private fun reportJavaError(context: JavaContext, node: UElement) {
        context.report(
            JAVA_ISSUE,
            node,
            context.getLocation(node),
            JAVA_ERROR_MESSAGE
        )
    }

    private fun reportXmlError(context: XmlContext, node: Element, attribute: Attr) {
        val attrValue = attribute.value
        val quickFix = LintFix.create()
            .replace()
            .text(attrValue)
            .with(DMS_PREFIX + UNDERSCORE + attrValue)
            .build()

        context.report(
            XML_ISSUE,
            node,
            context.getValueLocation(attribute),
            XML_ERROR_MESSAGE,
            quickFix
        )
    }
}