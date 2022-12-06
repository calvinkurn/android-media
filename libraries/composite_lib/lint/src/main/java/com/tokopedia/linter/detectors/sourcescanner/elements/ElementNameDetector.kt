package com.tokopedia.linter.detectors.sourcescanner.elements

import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Severity
import com.tokopedia.linter.Priority
import com.tokopedia.linter.detectors.sourcescanner.SourceCodeDetector
import org.jetbrains.uast.*

object ElementNameDetector {

    val PII_DATA_EXPOSURE_ISSUE_ID = "PII Data Exposure"
    val BRIEF_DESCRIPTION = "Please make sure PII data is not exposed publicly"
    val EXPLAINATION = "Exposing PII data may introduce data leak"

    val CLASS_PII_DATA_EXPOSURE: Issue = Issue.create(
        id = PII_DATA_EXPOSURE_ISSUE_ID,
        briefDescription = BRIEF_DESCRIPTION,
        explanation = EXPLAINATION,
        category = Category.CORRECTNESS,
        priority = Priority.Low.value,
        severity = Severity.WARNING,
        implementation = SourceCodeDetector.IMPLEMENTATION

    ).setAndroidSpecific(true)

    fun checkElementName(context: JavaContext, node: USimpleNameReferenceExpression) {
        val string = node.resolvedName.toString()
        if (checkPIIRegex(string)) {
            context.report(
                CLASS_PII_DATA_EXPOSURE,
                node,
                context.getLocation(node),
                messageString()
            )
        }
    }

    private val regexList = listOf(
        "(?i)(card*cvv|(\bcard.*cvv|card.*cvv\b))|(card.*cvv)|(\bcard.*cvv|card.*cvv\b\\S+)|(card.*cvv\b)|(creditcard*cvv|(\bcreditcard.*cvv|creditcard.*cvv\b))|(creditcard.*cvv)|(\bcreditcard.*cvv|creditcard.*cvv\b\\S+)|(creditcard.*cvv\b)|(cc*cvv|(\bcc.*cvv|cc.*cvv\b))|(cc.*cvv)|(\bcc.*cvv|cc.*cvv\b\\S+)|(cc.*cvv\\b)|(cvv*code|(\bcvv.*code|cvv.*code\b))|(cvv.*code)|(\bcvv.*code|cvv.*code\b\\S+)|(cvv.*code\b)|(cvvault*code|(\bcvvault.*code|cvvault.*code\b))|(cvvault.*code)|(\bcvvault.*code|cvvault.*code\b\\S+)|(cvvault.*code\b)|(\b*CVV)|(\b*cvv)",
        "(?i)(mail*address|(\bmail.*address|mail.*address\b))|(mail.*address)|(mail.*address\b.*)|(email*address|(\bemail.*address|email.*address\b))|(email.*address)|(email.*address\b.*)|(mail.*|\bmail.*)|(email.*|\bemail.*)",
        "name|Name|NAME|user_name|userName",
        "full_name|FULL_NAME|fullName",
        "address|ADDRESS|Address|home_addr|shop_addr",
        "ktp|KTP|kTp|Ktp|KTp|kTP",
        "(?i)(phone*number|(\bphone.*number|phone.*number\b))|(phone.*number)|(\bphone.*number|phone.*number\b\\S+)|(phone.*number\b)|(phone*no|(\bphone.*no|phone.*no\b))|(phone.*no)|(\bphone.*no|phone.*no\b\\S+)|(phone.*no\b)|(\b*phone)|(\b*Phone)|(\b*PHONE)"
    )

    fun checkPIIRegex(assignmentString: String): Boolean {
        for (regEx in regexList) {
            val pattern = Regex(regEx)
            if (pattern.containsMatchIn(assignmentString)) {
                return true
            }
        }
        return false
    }

    fun messageString(
    ): String {
        return "This code assigns or uses `PII data`: **Please make sure that it is not exposed publicly for attackers to exploit**"
    }
}
