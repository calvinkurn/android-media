package com.tokopedia.linter.detectors

import com.android.SdkConstants
import com.android.resources.ResourceFolderType
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Context
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.LintFix
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.XmlContext
import com.android.tools.lint.detector.api.XmlScanner
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.io.File

class DimenResourceValueDetector : Detector(), XmlScanner {
    companion object {
        val ISSUE = Issue.create(
            id = "DimenResourceValue",
            briefDescription = "Please use actual value instead of dimen for better performance.",
            explanation = "Using actual value is more readable and increase performance.",
            category = Category.CORRECTNESS,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                DimenResourceValueDetector::class.java,
                Scope.RESOURCE_FILE_SCOPE
            )
        )

        private const val ERROR_MESSAGE = "Please use actual value instead of dimen for better performance."
    }

    private var resourceIds = arrayListOf<String>()

    override fun beforeCheckEachProject(context: Context) {
        findResourceIds(context)
    }

    override fun appliesTo(folderType: ResourceFolderType): Boolean {
        return folderType == ResourceFolderType.VALUES
    }

    override fun getApplicableElements(): Collection<String> {
        return listOf(SdkConstants.TAG_DIMEN)
    }

    override fun visitElement(context: XmlContext, element: Element) {
        val value = element.firstChild.textContent
        val resName = value.substringAfter(SdkConstants.DIMEN_PREFIX)

        if (value.startsWith(SdkConstants.DIMEN_PREFIX) && !resourceIds.contains(resName)) {
            reportError(context, element.firstChild, resName)
        }
    }

    private fun reportError(context: XmlContext, node: Node, resName: String) {
        val dimen = "${SdkConstants.DIMEN_PREFIX}$resName"
        val hint = "Change \"$dimen\" with actual value."

        val quickFix = LintFix.create()
            .replace()
            .text(dimen)
            .build()

        context.report(
            ISSUE,
            node,
            context.getLocation(node),
            "$ERROR_MESSAGE $hint",
            quickFix
        )
    }

    private fun findResourceIds(context: Context) {
        context.project.resourceFolders.forEach { resFolder ->
            val valueDirs = resFolder.listFiles()
                ?.filter { it.name.contains(SdkConstants.FD_RES_VALUES) }

            valueDirs?.forEach {
                val files = it.listFiles()
                findValueResources(files)
            }
        }
    }

    private fun findValueResources(files: Array<File>?) {
        files?.forEach { file ->
            val ids = file.readLines().filter {
                val nameAttr = "${SdkConstants.ATTR_NAME}="
                val itemTag = "<${SdkConstants.TAG_ITEM}"
                it.contains(nameAttr) && !it.contains(itemTag)
            }.map {
                it.substringAfter("=\"").substringBefore("\"")
            }.filter { it.isNotEmpty() }

            resourceIds.addAll(ids)
        }
    }
}
