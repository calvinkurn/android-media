package com.tokopedia.linter.detectors

import com.android.SdkConstants
import com.android.resources.ResourceFolderType
import com.android.tools.lint.detector.api.*
import com.tokopedia.linter.LinterConstants
import org.w3c.dom.Attr

/**
 * Created by @ilhamsuaib on 15/12/21.
 */

class DimenUsageDetector : Detector(), XmlScanner {

    companion object {

        @JvmField
        val XML_ISSUE = Issue.create(
            id = "DimageUsage",
            briefDescription = "Avoid using dimen if possible, especially dimen from external module. " +
                    "Must be replaced with actual value for more readable and better performance.",
            explanation = "Avoid use dimens from other modules to minimize dependencies, please use dimens from own module or actual value. " +
                    "Don't put your values in dimens.xml if it is going to make them more difficult to maintain. Example : " +
                    "\n❌ : android:padding=\"@dimen/layout_lvl0\" " +
                    "\n✅ : android:padding=\"0dp\"",
            category = Category.CORRECTNESS,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                DimenUsageDetector::class.java,
                Scope.RESOURCE_FILE_SCOPE
            )
        )

        fun matchAttributeValue(attributeValue: String): MatchResult? {
            return "(@dimen/.*)".toRegex().matchEntire(attributeValue)
        }
    }

    private val dimenMap = mutableMapOf<String, String>()

    override fun beforeCheckEachProject(context: Context) {
        context.project.resourceFolders.forEach { resFolder ->
            val valueDirs = resFolder.listFiles()
                ?.filter { it.name.contains(SdkConstants.FD_RES_VALUES) }
                .orEmpty()

            valueDirs.forEach { dir ->
                val files = dir.listFiles()
                files?.forEach { file ->
                    file.readLines().forEach {
                        val match = ".*<dimen.*".toRegex().matchEntire(it)
                        if (match != null) {
                            val key = "\".*\"".toRegex().find(it)?.groupValues?.firstOrNull()
                                ?.replace("\"", "").orEmpty()
                            val value = it.substringAfter("\">").substringBefore("</")

                            if (key.isNotBlank() && value.isNotBlank()) {
                                dimenMap[key] = value
                            }
                        }
                    }
                }
            }
        }
    }

    override fun appliesTo(folderType: ResourceFolderType): Boolean {
        return folderType == ResourceFolderType.LAYOUT || folderType == ResourceFolderType.DRAWABLE
    }

    override fun getApplicableAttributes(): Collection<String> {
        return listOf(
            SdkConstants.ATTR_LAYOUT_WIDTH,
            SdkConstants.ATTR_LAYOUT_HEIGHT,
            SdkConstants.ATTR_LAYOUT_MARGIN,
            SdkConstants.ATTR_LAYOUT_MARGIN_START,
            SdkConstants.ATTR_LAYOUT_MARGIN_LEFT,
            SdkConstants.ATTR_LAYOUT_MARGIN_TOP,
            SdkConstants.ATTR_LAYOUT_MARGIN_END,
            SdkConstants.ATTR_LAYOUT_MARGIN_RIGHT,
            SdkConstants.ATTR_LAYOUT_MARGIN_BOTTOM,
            SdkConstants.ATTR_PADDING,
            SdkConstants.ATTR_PADDING_START,
            SdkConstants.ATTR_PADDING_LEFT,
            SdkConstants.ATTR_PADDING_TOP,
            SdkConstants.ATTR_PADDING_END,
            SdkConstants.ATTR_PADDING_RIGHT,
            SdkConstants.ATTR_PADDING_BOTTOM,
            SdkConstants.ATTR_TEXT_SIZE,
            SdkConstants.ATTR_WIDTH,
            SdkConstants.ATTR_HEIGHT,
            SdkConstants.ATTR_STROKE_WIDTH,
            SdkConstants.ATTR_CORNER_RADIUS,
            SdkConstants.ATTR_GRADIENT_RADIUS,
            LinterConstants.Attrs.RADIUS,
            LinterConstants.Attrs.BOTTOM_LEFT_RADIUS,
            LinterConstants.Attrs.BOTTOM_RIGHT_RADIUS,
            LinterConstants.Attrs.TOP_LEFT_RADIUS,
            LinterConstants.Attrs.TOP_RIGHT_RADIUS,
        )
    }

    override fun visitAttribute(context: XmlContext, attribute: Attr) {
        val matchResult = matchAttributeValue(attribute.value)
        if (matchResult != null) {
            reportIssue(context, attribute)
        }
    }

    /**
     * will show warning for external dimen usage
     * */
    private fun reportIssue(context: XmlContext, attr: Attr) {
        val dimenName = attr.value.replace("@dimen/", "")
        val isDimenNotExistOnLocalModule = dimenMap[dimenName].isNullOrBlank()

        if (isDimenNotExistOnLocalModule) {
            context.report(
                XML_ISSUE,
                context.getLocation(attr),
                XML_ISSUE.getExplanation(TextFormat.TEXT)
            )
        }
    }
}
