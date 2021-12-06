package com.tokopedia.linter.detectors

import com.android.SdkConstants.*
import com.android.tools.lint.detector.api.*
import com.android.utils.XmlUtils.getFirstSubTagByName
import com.android.utils.XmlUtils.getSubTagsAsList
import org.w3c.dom.Element

class AndroidExportedDetector: XmlScanner, Detector() {

    companion object {
        val ISSUE = Issue.create(
            id = "AndroidExported",
            briefDescription = "android:exported value should be false",
            explanation = "android:exported value should be false",
            category = Category.CORRECTNESS,
            priority = 5,
            severity = Severity.FATAL,
            implementation = Implementation(
                AndroidExportedDetector::class.java,
                Scope.MANIFEST_SCOPE
            )
        )

        /* whitelist */
        const val TAG_ACTION_ATTR_NAME_MAIN = "android.intent.action.MAIN"
        const val TAG_ACTION_ATTR_NAME_VIEW = "android.intent.action.VIEW"
        const val TAG_CATEGORY_ATTR_NAME_DEFAULT = "android.intent.category.DEFAULT"
        const val TAG_CATEGORY_ATTR_NAME_LAUNCHER = "android.intent.category.LAUNCHER"

        /* attr value */
        const val VALUE_EXPORTED_FALSE = "false"
    }

    override fun getApplicableElements(): Collection<String>? {
        return listOf(TAG_ACTIVITY)
    }

    override fun visitElement(context: XmlContext, element: Element) {
        val tag = element.tagName

        if (tag.equals(TAG_ACTIVITY)) {
            val childT1 = getFirstSubTagByName(element, TAG_INTENT_FILTER)

            // if there's <intent-filter>
            if (childT1 != null) {
                val childsT2 = getSubTagsAsList(childT1)
                var isContainWhitelistSignature = false

                // check if contain whitelist
                for (childT2 in childsT2) {
                    val childT2Attr = childT2.getAttributeNS(ANDROID_URI, ATTR_NAME)

                    when (childT2.tagName) {
                        TAG_ACTION -> {
                            if (childT2Attr == TAG_ACTION_ATTR_NAME_MAIN ||
                                childT2Attr == TAG_ACTION_ATTR_NAME_VIEW) {
                                isContainWhitelistSignature = true
                            }
                        }
                        TAG_CATEGORY -> {
                            if (childT2Attr == TAG_CATEGORY_ATTR_NAME_DEFAULT ||
                                childT2Attr == TAG_CATEGORY_ATTR_NAME_LAUNCHER
                            ) {
                                isContainWhitelistSignature = true
                            }
                        }
                        else -> {}
                    }
                }
                // if does not contain whitelist, check android:exported in activity bracket
                if (!isContainWhitelistSignature) {
                    val node = element.getAttributeNodeNS(ANDROID_URI, ATTR_EXPORTED)
                    if (node != null) {
                        val lintFix = LintFix.create()
                            .set()
                            .attribute(node.name)
                            .value(VALUE_EXPORTED_FALSE)
                            .build()

                        context.report(
                            ISSUE,
                            node,
                            context.getNameLocation(node),
                            "${node.name} value should be false",
                            lintFix
                        )
                    }
                }
            }
        }
    }
}