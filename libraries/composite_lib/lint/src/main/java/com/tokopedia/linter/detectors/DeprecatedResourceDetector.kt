package com.tokopedia.linter.detectors

import com.android.SdkConstants
import com.android.SdkConstants.ATTR_CORNER_RADIUS
import com.android.SdkConstants.ATTR_LAYOUT_HEIGHT
import com.android.SdkConstants.ATTR_LAYOUT_MARGIN
import com.android.SdkConstants.ATTR_LAYOUT_MARGIN_BOTTOM
import com.android.SdkConstants.ATTR_LAYOUT_MARGIN_LEFT
import com.android.SdkConstants.ATTR_LAYOUT_MARGIN_RIGHT
import com.android.SdkConstants.ATTR_LAYOUT_MARGIN_TOP
import com.android.SdkConstants.ATTR_LAYOUT_WIDTH
import com.android.SdkConstants.ATTR_MAX_HEIGHT
import com.android.SdkConstants.ATTR_MAX_WIDTH
import com.android.SdkConstants.ATTR_MIN_HEIGHT
import com.android.SdkConstants.ATTR_MIN_WIDTH
import com.android.SdkConstants.ATTR_PADDING
import com.android.SdkConstants.ATTR_PADDING_BOTTOM
import com.android.SdkConstants.ATTR_PADDING_LEFT
import com.android.SdkConstants.ATTR_PADDING_RIGHT
import com.android.SdkConstants.ATTR_PADDING_TOP
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Context
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.XmlContext
import com.android.tools.lint.detector.api.XmlScanner
import com.android.tools.lint.detector.api.getBaseName
import com.google.common.collect.Sets
import org.w3c.dom.Attr
import java.io.File


class DeprecatedResourceDetector : Detector(), XmlScanner {

    companion object {
        val ISSUE = Issue.create(
            id = "DeprecatedResource",
            briefDescription = "Do not use resource from tkpddesign.",
            explanation = "Do not use resource from tkpddesign. " +
                "Please use resource from unify or use actual value instead.",
            category = Category.CORRECTNESS,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                DeprecatedResourceDetector::class.java,
                Scope.RESOURCE_FILE_SCOPE
            )
        )

        private const val MESSAGE = "Do not use resource from tkpddesign. " +
            "Please use resource from unify or use actual value instead."

        private const val MODULE_NAME = "tkpddesign"
        private const val RES_DIR = "/src/main/res"
        private const val CODEOWNERS_FILE = "CODEOWNERS"
    }

    private val deprecatedResourceIds = Sets.newHashSet<String>()

    override fun beforeCheckEachProject(context: Context) {
        val rootDir = findRootProject(context.project.dir)
        val resFolder = File("$rootDir/$MODULE_NAME$RES_DIR")
        findDeprecatedResourceIds(resFolder)
    }

    override fun getApplicableAttributes(): Collection<String>? {
        return listOf(
            ATTR_MIN_HEIGHT,
            ATTR_MAX_HEIGHT,
            ATTR_MIN_WIDTH,
            ATTR_MAX_WIDTH,
            ATTR_LAYOUT_HEIGHT,
            ATTR_LAYOUT_WIDTH,
            ATTR_LAYOUT_MARGIN,
            ATTR_LAYOUT_MARGIN_LEFT,
            ATTR_LAYOUT_MARGIN_TOP,
            ATTR_LAYOUT_MARGIN_RIGHT,
            ATTR_LAYOUT_MARGIN_BOTTOM,
            ATTR_PADDING,
            ATTR_PADDING_LEFT,
            ATTR_PADDING_TOP,
            ATTR_PADDING_RIGHT,
            ATTR_PADDING_BOTTOM,
            ATTR_CORNER_RADIUS
        )
    }

    override fun visitAttribute(context: XmlContext, attribute: Attr) {
        val resId = attribute.value.substringAfter("/")
        if(deprecatedResourceIds.contains(resId)) {
            reportIssue(context, attribute)
        }
    }

    private fun reportIssue(context: XmlContext, attribute: Attr) {
        context.report(
            ISSUE,
            attribute,
            context.getValueLocation(attribute),
            MESSAGE
        )
    }

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

    private fun findRootProject(dir: File?): String? {
        var root = dir
        var listFiles = listOf<String>()

        while(!listFiles.contains(CODEOWNERS_FILE)) {
            root = root?.parentFile
            listFiles = root?.list()?.asList().orEmpty()

            if(root?.path == "/") {
                return null
            }
        }

        return root?.path
    }
}