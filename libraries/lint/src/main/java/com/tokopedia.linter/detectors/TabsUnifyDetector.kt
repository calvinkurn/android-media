package com.tokopedia.linter.detectors

import com.android.tools.lint.detector.api.*
import com.tokopedia.linter.*
import org.w3c.dom.Element

class TabsUnifyDetector : Detector(), XmlScanner {


    companion object {
        private val IMPLEMENTATION = Implementation(
                TabsUnifyDetector::class.java,
                Scope.RESOURCE_FILE_SCOPE
        )
        private val APPLICABLE_LIST = listOf(
                TABS_OLD_NAME, TAB_LAYOUT_OLD_NAME)

        val ISSUE: Issue = Issue.create(
                TABS_ISSUE_ID,
                WARNING_DESCRIPTION_FORMAT.format(TABS_ACTUAL_SHORT_NAME, TABS_FIX_SHORT_NAME),
                WARNING_EXPLANATION_FORMAT.format(TABS_ACTUAL_SHORT_NAME, TABS_FIX_SHORT_NAME),
                Category.CORRECTNESS,
                3,
                Severity.WARNING,
                IMPLEMENTATION)

        val TAB_LAYOUT_ISSUE: Issue = Issue.create(
                TABS_ISSUE_ID,
                WARNING_DESCRIPTION_FORMAT.format(TAB_LAYOUT_ACTUAL_SHORT_NAME, TABS_FIX_SHORT_NAME),
                WARNING_EXPLANATION_FORMAT.format(TAB_LAYOUT_ACTUAL_SHORT_NAME, TABS_FIX_SHORT_NAME),
                Category.CORRECTNESS,
                3,
                Severity.WARNING,
                IMPLEMENTATION)
    }


    override fun getApplicableElements(): Collection<String>? {
        return APPLICABLE_LIST
    }


    override fun visitElement(context: XmlContext, element: Element) {
        val tabsFix = LintFix.create().name(FIX_NAME_FORMAT.format(TAB_NEW_NAME))
                .replace()
                .text(TABS_OLD_NAME)
                .with(TAB_NEW_NAME)
                .robot(true)
                .independent(true)
                .build()

        context.report(ISSUE, location = context.getLocation(element),
                message = USAGE_ERROR_MESSAGE_FORMAT.format(TABS_FIX_SHORT_NAME), quickfixData = tabsFix)

        val tabsLayoutFix = LintFix.create().name(FIX_NAME_FORMAT.format(TABS_FIX_SHORT_NAME))
                .replace()
                .text(TAB_LAYOUT_OLD_NAME)
                .with(TAB_NEW_NAME)
                .robot(true)
                .independent(true)
                .build()

        context.report(TAB_LAYOUT_ISSUE, location = context.getLocation(element),
                message = USAGE_ERROR_MESSAGE_FORMAT.format(TABS_FIX_SHORT_NAME), quickfixData = tabsLayoutFix)

    }
}