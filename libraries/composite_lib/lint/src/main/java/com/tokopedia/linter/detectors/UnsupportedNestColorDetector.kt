package com.tokopedia.linter.detectors

import com.android.SdkConstants
import com.android.resources.ResourceFolderType
import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Context
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
import com.android.tools.lint.detector.api.getBaseName
import com.google.common.collect.Sets
import com.tokopedia.linter.Priority
import com.tokopedia.linter.unify.UnifyComponentsList
import org.jetbrains.uast.UElement
import org.jetbrains.uast.ULiteralExpression
import org.w3c.dom.Attr
import java.io.File

class UnsupportedNestColorDetector : Detector(), SourceCodeScanner, XmlScanner {
    companion object {
        private const val ISSUE_ID = "UnsupportedNestColor"
        private const val ERROR_MESSAGE = "Color has deprecated. " +
            "Please use color with token contain 'N' before color number. Example:" +
            "\n❌ : Unify_N100 " +
            "\n✅ : Unify_NN100" +
            "\n❌ : Unify_G500 " +
            "\n✅ : Unify_GN500"
        private val ISSUE_PRIORITY = Priority.Medium
        private val ISSUE_SEVERITY = Severity.WARNING
        private val ISSUE_CATEGORY = Category.CORRECTNESS
        private const val NEST_INDEX = 7
        private const val NEST_CHARACTER = "N"
        private const val REGEX_OLD_COLOR = "Unify_[A-Z]\\d{1,4}"

        val JAVA_ISSUE = Issue.create(
            id = ISSUE_ID,
            briefDescription = "Deprecated color should not be used.",
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
            briefDescription = "Deprecated color should not be used.",
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

    private val deprecatedResourceIds = Sets.newHashSet<String>()

    override fun appliesTo(folderType: ResourceFolderType): Boolean {
        return folderType == ResourceFolderType.LAYOUT ||
            folderType == ResourceFolderType.VALUES ||
            folderType == ResourceFolderType.DRAWABLE ||
            folderType == ResourceFolderType.COLOR
    }

    /*override fun beforeCheckEachProject(context: Context) {
        context.project.resourceFolders.forEach { resFolder ->
            findDeprecatedResourceIds(resFolder = resFolder)
        }
    }*/

    private fun findDeprecatedResourceIds(resFolder: File?) {
        val layoutDirs = resFolder?.listFiles()
            ?.filter { it.name.contains(SdkConstants.FD_RES_LAYOUT) }

        val drawableDirs = resFolder?.listFiles()
            ?.filter { it.name.contains(SdkConstants.FD_RES_DRAWABLE) }

        val valueDirs = resFolder?.listFiles()
            ?.filter { it.name.contains(SdkConstants.FD_RES_VALUES) }

        layoutDirs?.forEach { file ->
            val files = file.listFiles().orEmpty()
            val drawable = files.map { getBaseName(it.name) }
            deprecatedResourceIds.addAll(drawable)
        }

        drawableDirs?.forEach { file ->
            val files = file.listFiles().orEmpty()
            val drawable = files.map { getBaseName(it.name) }
            deprecatedResourceIds.addAll(drawable)
        }

        valueDirs?.forEach {
            val files = it.listFiles()
            findValueResources(files)
        }
    }

    private fun findValueResources(files: Array<File>?) {
        files?.forEach { file ->
            val valueIds = file.readLines().filter {
                it.contains(SdkConstants.ATTR_NAME) && !it.contains(SdkConstants.TAG_ITEM)
            }.map {
                it.substringAfter("=\"").substringBefore("\"")
            }.filter { it.isNotEmpty() }
            deprecatedResourceIds.addAll(valueIds)
        }
    }

    override fun getApplicableElements(): Collection<String> {
        return listOf(
            SdkConstants.COLOR_RESOURCE_PREFIX,
            SdkConstants.ATTR_COLOR,
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
            SdkConstants.FD_RES_COLOR,
            SdkConstants.RESOURCE_CLZ_COLOR,
            SdkConstants.TAG_COLOR,
            SdkConstants.TRANSPARENT_COLOR,
            SdkConstants.ATTR_TEXT_COLOR_LINK,
            SdkConstants.ANDROID_COLOR_RESOURCE_PREFIX,
            SdkConstants.ATTR_CACHE_COLOR_HINT,
            SdkConstants.ATTR_TEXT_COLOR_HIGHLIGHT,
            SdkConstants.ATTR_TEXT_COLOR_HINT
        )
    }

    override fun visitAttribute(context: XmlContext, attribute: Attr) {
        runCatching {
            if (attribute.value.contains(Regex(REGEX_OLD_COLOR))) {
                reportXmlError(context, attribute)
            }
        }
    }

    override fun getApplicableUastTypes(): List<Class<out UElement>> {
        return listOf(ULiteralExpression::class.java)
    }

    override fun createUastHandler(context: JavaContext): UElementHandler {
        return object : UElementHandler() {
            override fun visitLiteralExpression(node: ULiteralExpression) {
                val value = node.value.toString()
                if (value.matches(Regex(REGEX_OLD_COLOR))) {
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

    private fun reportXmlError(context: XmlContext, attribute: Attr) {
        val attrValue = StringBuilder(attribute.value)
        val sAttrValue = attrValue.toString()
        val suggestion = UnifyComponentsList.unifyToNestColor[sAttrValue]
            ?: sAttrValue
        val quickFix = LintFix.create()
            .replace()
            .text(sAttrValue)
            .with(suggestion)
            .build()

        context.report(
            XML_ISSUE,
            attribute,
            context.getValueLocation(attribute),
            ERROR_MESSAGE,
            quickFix
        )
    }
}
