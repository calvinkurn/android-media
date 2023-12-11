package com.tokopedia.linter.detectors

import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.LayoutDetector
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.XmlContext
import com.android.tools.lint.detector.api.XmlScanner
import org.w3c.dom.Element
import java.util.regex.Pattern

class TkpdDesignComponentDetector: LayoutDetector(), XmlScanner {

    private val pattern = Pattern.compile("^com\\.tokopedia\\.design\\..*")

    override fun getApplicableElements(): Collection<String>? {
        return ALL
    }

    override fun visitElement(context: XmlContext, element: Element) {
        val elementName = element.tagName
        if (pattern.matcher(elementName).matches()) {
            context.report(
                ISSUE,
                element,
                context.getLocation(element),
                "Using component from the tkpddesign package in XML is not allowed. Because TkpdDesign will be deleted soon."
            )
        }
    }

    companion object {
        val ISSUE = Issue.create(
            "TkpdDesignComponentUsage",
            "Avoid using tkpddesign component",
            "Using component from a tkpddesign module is not allowed.Because TkpdDesign will be deleted soon.",
            Category.CORRECTNESS, 6, Severity.ERROR,
            Implementation(TkpdDesignComponentDetector::class.java, Scope.RESOURCE_FILE_SCOPE)
        )
    }
}
