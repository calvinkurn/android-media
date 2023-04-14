@file:Suppress("UnstableApiUsage")

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
import com.android.tools.lint.detector.api.XmlContext
import com.android.tools.lint.detector.api.XmlScanner
import com.tokopedia.linter.Priority
import com.tokopedia.linter.unify.UnifyComponentsList
import org.jetbrains.uast.UElement
import org.jetbrains.uast.ULiteralExpression
import org.w3c.dom.Attr

class UnsupportedNestColorDetector : Detector(), XmlScanner {
    companion object {
        private const val ISSUE_ID = "UnsupportedNestColor"
        private const val BRIEF_DESC = "Deprecated color should not be used."
        private const val ERROR_MESSAGE = "Please use color with token contains 'N' before color number, ex: " +
            "❌ : Unify_N100 → ✅ : Unify_NN100, or you can consultation with your designer." +
            "\n⚒️ ️Quick Fix:\n@s"
        private val ISSUE_PRIORITY = Priority.Medium
        private val ISSUE_SEVERITY = Severity.WARNING
        private val ISSUE_CATEGORY = Category.CORRECTNESS
        private const val NEST_INDEX = 14
        private const val NEST_CHARACTER = "N"
        val REGEX_OLD_COLOR = "(Unify_[A-Z]\\d{1,4}_\\d{1,2})|(Unify_[A-Z]\\d{1,4})".toRegex()

        val JAVA_ISSUE = Issue.create(
            id = ISSUE_ID,
            briefDescription = BRIEF_DESC,
            explanation = ERROR_MESSAGE,
            category = ISSUE_CATEGORY,
            priority = ISSUE_PRIORITY.value,
            severity = ISSUE_SEVERITY,
            implementation = Implementation(
                UnsupportedNestColorDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
        val XML_ISSUE = Issue.create(
            id = ISSUE_ID,
            briefDescription = BRIEF_DESC,
            explanation = ERROR_MESSAGE,
            category = ISSUE_CATEGORY,
            priority = ISSUE_PRIORITY.value,
            severity = ISSUE_SEVERITY,
            implementation = Implementation(
                UnsupportedNestColorDetector::class.java,
                Scope.RESOURCE_FILE_SCOPE
            )
        )
    }

    // region XML detector
    override fun appliesTo(folderType: ResourceFolderType): Boolean {
        return folderType == ResourceFolderType.LAYOUT ||
            folderType == ResourceFolderType.VALUES ||
            folderType == ResourceFolderType.COLOR ||
            folderType == ResourceFolderType.DRAWABLE
    }

    override fun getApplicableAttributes(): Collection<String> {
        return listOf(
            SdkConstants.ATTR_COLOR,
            SdkConstants.ATTR_BACKGROUND,
            SdkConstants.ATTR_TEXT_COLOR,
            SdkConstants.ATTR_AM_PM_BACKGROUND_COLOR,
            SdkConstants.ATTR_AM_PM_TEXT_COLOR,
            SdkConstants.ATTR_CHIP_SURFACE_COLOR,
            SdkConstants.ATTR_CHIP_STROKE_COLOR,
            SdkConstants.ATTR_BOX_STROKE_COLOR,
            SdkConstants.ATTR_BOX_BACKGROUND_COLOR,
            SdkConstants.ATTR_CALENDAR_TEXT_COLOR,
            SdkConstants.ATTR_CARD_BACKGROUND_COLOR,
            SdkConstants.ATTR_CHIP_BACKGROUND_COLOR,
            SdkConstants.ATTR_ERROR_TEXT_COLOR,
            SdkConstants.ATTR_FILL_COLOR,
            SdkConstants.ATTR_HELPER_TEXT_TEXT_COLOR,
            SdkConstants.ATTR_HINT_TEXT_COLOR,
            SdkConstants.ATTR_ITEM_RIPPLE_COLOR,
            SdkConstants.ATTR_ITEM_SHAPE_FILL_COLOR,
            SdkConstants.ATTR_ITEM_TEXT_COLOR,
            SdkConstants.ATTR_NUMBERS_BACKGROUND_COLOR,
            SdkConstants.ATTR_NUMBERS_INNER_TEXT_COLOR,
            SdkConstants.ATTR_NUMBERS_SELECTOR_COLOR,
            SdkConstants.ATTR_NUMBERS_TEXT_COLOR,
            SdkConstants.ATTR_RIPPLE_COLOR,
            SdkConstants.ATTR_SHADOW_COLOR,
            SdkConstants.ATTR_STOP_COLOR,
            SdkConstants.ATTR_STROKE_COLOR,
            SdkConstants.ATTR_SUBTITLE_TEXT_COLOR,
            SdkConstants.ATTR_TAB_INDICATOR_COLOR,
            SdkConstants.ATTR_TAB_RIPPLE_COLOR,
            SdkConstants.ATTR_TAB_SELECTED_TEXT_COLOR,
            SdkConstants.ATTR_TAB_TEXT_COLOR,
            SdkConstants.ATTR_TITLE_TEXT_COLOR,
            SdkConstants.ATTR_YEAR_LIST_SELECTOR_COLOR,
            SdkConstants.ATTR_TEXT_COLOR_LINK,
            SdkConstants.ATTR_CACHE_COLOR_HINT,
            SdkConstants.ATTR_TEXT_COLOR_HIGHLIGHT,
            SdkConstants.ATTR_TEXT_COLOR_HINT

        )
    }

    override fun visitAttribute(context: XmlContext, attribute: Attr) {
        if (attribute.value.contains(REGEX_OLD_COLOR)) {
            reportXmlError(context, attribute)
        }
    }

    private fun reportXmlError(context: XmlContext, attribute: Attr) {
        val attrValue = StringBuilder(attribute.value)
        val sAttrValue = attrValue.toString()
        val token = sAttrValue.substringAfter("/")
        val suggestion = UnifyComponentsList.unifyToNestColor[token]
            ?: attrValue.insert(NEST_INDEX, NEST_CHARACTER).toString()
        val quickFix = LintFix.create()
            .replace()
            .text(sAttrValue)
            .with(suggestion)
            .build()
        val quickFixDesc = " ❌: $sAttrValue → ✅: $suggestion"
        val message = ERROR_MESSAGE.replace("@s", quickFixDesc)

        context.report(
            XML_ISSUE,
            attribute,
            context.getValueLocation(attribute),
            message,
            quickFix
        )
    }
    // endregion XML detector

    // region JAVA detector
    override fun getApplicableUastTypes(): List<Class<out UElement>> {
        return listOf(ULiteralExpression::class.java)
    }

    override fun createUastHandler(context: JavaContext): UElementHandler {
        return object : UElementHandler() {
            override fun visitLiteralExpression(node: ULiteralExpression) {
                val value = node.value.toString()
                if (value.contains(REGEX_OLD_COLOR)) {
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

    // endregion JAVA detector
}
