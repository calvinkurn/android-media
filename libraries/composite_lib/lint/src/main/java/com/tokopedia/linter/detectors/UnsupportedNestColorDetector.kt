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
import com.tokopedia.linter.Priority
import org.jetbrains.uast.UElement
import org.jetbrains.uast.ULiteralExpression
import org.w3c.dom.Attr
import org.w3c.dom.Element

class UnsupportedNestColorDetector : Detector(), SourceCodeScanner, XmlScanner {
    companion object {
        val JAVA_ISSUE = Issue.create(
            id = "UnsupportedNestModeColor",
            briefDescription = "Deprecated color should not be used.",
            explanation = "This color has deprecated, please use color with token contain 'N' before color number. Example:" +
                "\n❌ : Unify_N100 " +
                "\n✅ : Unify_NN100" +
                "\n❌ : Unify_G500 " +
                "\n✅ : Unify_GN500",
            category = Category.CORRECTNESS,
            priority = Priority.Medium.value,
            severity = Severity.WARNING,
            implementation = Implementation(
                UnsupportedNestColorDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )

        val XML_ISSUE = Issue.create(
            id = "UnsupportedNestModeColor",
            briefDescription = "Deprecated color should not be used.",
            explanation = "This color has deprecated, please use color with token contain 'N' before color number. Example:" +
                "\n❌ : Unify_N100 " +
                "\n✅ : Unify_NN100" +
                "\n❌ : Unify_G500 " +
                "\n✅ : Unify_GN500",
            category = Category.CORRECTNESS,
            priority = Priority.Medium.value,
            severity = Severity.WARNING,
            implementation = Implementation(
                UnsupportedNestColorDetector::class.java,
                Scope.RESOURCE_FILE_SCOPE
            )
        )

        private const val NEST_INDEX = 7
        private const val NEST_CHARACTER = "N"
        private const val REGEX_OLD_COLOR = "^Unify_(?:[A-Z]){1}(?:[0-9]){2,4}"
        private const val ERROR_MESSAGE = "Color has deprecated. " +
            "Please use color with token contain 'N' before color number. Example:" +
            "\n❌ : Unify_N100 " +
            "\n✅ : Unify_NN100" +
            "\n❌ : Unify_G500 " +
            "\n✅ : Unify_GN500"
    }

    override fun appliesTo(folderType: ResourceFolderType): Boolean {
        return folderType == ResourceFolderType.VALUES
    }

    override fun getApplicableElements(): Collection<String> {
        return listOf(SdkConstants.TAG_COLOR)
    }

    override fun visitElement(context: XmlContext, element: Element) {
        val attribute = element.getAttributeNode("name")
        if (attribute.value.matches(Regex(REGEX_OLD_COLOR))) {
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
                if(value.matches(Regex(REGEX_OLD_COLOR))) {
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
            ERROR_MESSAGE
        )
    }

    private fun reportXmlError(context: XmlContext, node: Element, attribute: Attr) {
        val attrValue = StringBuilder(attribute.value)
        val suggestion = attrValue.insert(NEST_INDEX, NEST_CHARACTER).toString()
        val quickFix = LintFix.create()
            .replace()
            .text(attrValue.toString())
            .with(suggestion)
            .build()

        context.report(
            XML_ISSUE,
            node,
            context.getValueLocation(attribute),
            ERROR_MESSAGE,
            quickFix
        )
    }
}
