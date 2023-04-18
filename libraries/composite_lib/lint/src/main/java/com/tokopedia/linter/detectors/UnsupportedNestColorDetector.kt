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
import com.android.tools.lint.detector.api.SourceCodeScanner
import com.android.tools.lint.detector.api.XmlContext
import com.android.tools.lint.detector.api.XmlScanner
import com.intellij.psi.PsiElement
import com.tokopedia.linter.Priority
import com.tokopedia.linter.unify.UnifyComponentsList
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UField
import org.jetbrains.uast.ULocalVariable
import org.jetbrains.uast.UMethod
import org.jetbrains.uast.UReturnExpression
import org.jetbrains.uast.UVariable
import org.w3c.dom.Attr

class UnsupportedNestColorDetector : Detector(), XmlScanner, SourceCodeScanner {
    companion object {
        private const val ISSUE_ID = "UnsupportedNestColor"
        private const val BRIEF_DESC = "Deprecated color should not be used."
        private const val ERROR_MESSAGE =
            "Please use color with token contains 'N' before color number, ex: " +
                "❌ : Unify_N100 → ✅ : Unify_NN100, or you can consultation with your designer."
        private const val QUICK_FIX_MESSAGE = "\n⚒️ ️Quick Fix:\n@s"
        private val ISSUE_PRIORITY = Priority.Medium
        private val ISSUE_SEVERITY = Severity.WARNING
        private val ISSUE_CATEGORY = Category.CORRECTNESS
        private const val NEST_INDEX = 7
        private const val NEST_CHARACTER = "N"
        val REGEX_OLD_COLOR = "(Unify_[A-Z]\\d{1,4}_\\d{1,2})|(Unify_[A-Z]\\d{1,4})".toRegex()
        val JAVA_REGEX_OLD_COLOR =
            ".(R.color.Unify_[A-Z]\\d{1,4}_\\d{1,2})|(R.color.Unify_[A-Z]\\d{1,4})".toRegex()

        val JAVA_ISSUE = Issue.create(
            id = ISSUE_ID,
            briefDescription = BRIEF_DESC,
            explanation = ERROR_MESSAGE,
            category = ISSUE_CATEGORY,
            priority = ISSUE_PRIORITY.value,
            severity = ISSUE_SEVERITY,
            androidSpecific = true,
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
        val message = ERROR_MESSAGE + QUICK_FIX_MESSAGE.replace("@s", quickFixDesc)

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
        return listOf(
            UField::class.java,
            ULocalVariable::class.java,
            UCallExpression::class.java,
            UReturnExpression::class.java,
            UMethod::class.java
        )
    }

    override fun createUastHandler(context: JavaContext): UElementHandler {
        return object : UElementHandler() {
            override fun visitField(node: UField) {
                val value = node.text
                validate(node = node, value = value)
            }

            override fun visitLocalVariable(node: ULocalVariable) {
                val value = node.text
                validate(node = node, value = value)
            }

            override fun visitCallExpression(node: UCallExpression) {
                node.valueArguments.firstOrNull {
                    val resource = it.asSourceString()
                    shouldScanResource(value = resource)
                }?.let {
                    reportJavaError(context, it)
                }
            }

            override fun visitReturnExpression(node: UReturnExpression) {
                val element = node.sourcePsi?.lastChild
                val resource = element?.text.orEmpty()

                if (shouldScanResource(resource)) {
                    reportJavaError(
                        context = context,
                        psi = element
                    )
                }
            }

            override fun visitMethod(node: UMethod) {
                if (node.parameters.isNotEmpty()) {
                    node.uastParameters.forEach { param ->
                        val element = param.sourcePsi?.lastChild
                        val resource = element?.text.orEmpty()

                        if (shouldScanResource(resource)) {
                            reportJavaError(
                                context = context,
                                psi = param
                            )
                        }
                    }
                }
            }

            private fun validate(node: UVariable, value: String) {
                if (shouldScanResource(value = value)) {
                    reportJavaError(context = context, node = node)
                }
            }

            private fun shouldScanResource(value: String) =
                value.contains(JAVA_REGEX_OLD_COLOR) && !value.contains("getColor")
        }
    }

    private fun reportJavaError(
        context: JavaContext,
        node: UElement? = null,
        psi: PsiElement? = null
    ) {
        if (psi != null) {
            val source = psi.text.ifBlank { return }
            val component = getJavaMessageAndQuickFix(source = source)
            val location = context.getLocation(psi)

            context.report(
                JAVA_ISSUE,
                psi,
                location,
                component.first,
                component.second
            )
        } else {
            val source = (node?.asSourceString() ?: return).ifBlank { return }
            val component = getJavaMessageAndQuickFix(source = source)
            val location = context.getLocation(node)

            context.report(
                JAVA_ISSUE,
                node,
                location,
                component.first,
                component.second
            )
        }
    }

    private fun getJavaMessageAndQuickFix(source: String): Pair<String, LintFix> {
        val attrValue = StringBuilder(source)
        val sAttrValue = attrValue.toString()
        val token = sAttrValue.substringAfter("Unify_").substringBefore(" ")
        val suggestion = getJavaSuggestion(token, sAttrValue, attrValue)
        val quickFix = LintFix.create()
            .replace()
            .text(sAttrValue)
            .with(suggestion)
            .build()
        val message = getQuickFixDesc(suggestion, sAttrValue)

        return message to quickFix
    }

    private fun getQuickFixDesc(suggestion: String, sAttrValue: String) =
        if (suggestion.isBlank()) {
            ERROR_MESSAGE
        } else {
            ERROR_MESSAGE + QUICK_FIX_MESSAGE.replace(
                "@s",
                "❌: $sAttrValue → ✅: $suggestion"
            )
        }

    private fun getJavaSuggestion(
        token: String,
        sAttrValue: String,
        attrValue: StringBuilder
    ): String = UnifyComponentsList.unifyToNestColor["Unify_$token"]
        ?: run {
            try {
                val index = sAttrValue.indexOf("Unify_", 0, false)
                attrValue.insert(index + NEST_INDEX, NEST_CHARACTER).toString()
            } catch (_: Throwable) {
                ""
            }
        }
    // endregion JAVA detector
}
