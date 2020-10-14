package com.tokopedia.linter.detectors

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import com.intellij.psi.PsiMethod
import com.tokopedia.linter.*
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UImportStatement

class DatePickerUnifyDetector : Detector(), SourceCodeScanner {


    companion object {

        private val disallowedImports = listOf(DATE_PICKER, DATE_PICKER_DIALOG)
        private val IMPLEMENTATION = Implementation(DatePickerUnifyDetector::class.java, Scope.JAVA_FILE_SCOPE)
        private val APPLICABLE_METHOD_NAME = listOf("init", "setOnDateChangeListener", "updateDate",
                "setDayOfMonth", "setYear", "setMonth", "setFirstDayOfWeek", "setMinDate", "setMaxDate",
                "getDayOfMonth", "getYear", "getMonth", "getFirstDayOfWeek", "getMinDate", "getMaxDate")

        val ISSUE: Issue = Issue.create(
                id = DATE_PICKER_ISSUE_ID,
                briefDescription = WARNING_DESCRIPTION_FORMAT.format(DATE_PICKER_OLD_NAME, DATE_PICKER_NEW_NAME),
                explanation = WARNING_EXPLANATION_FORMAT.format(DATE_PICKER_OLD_NAME, DATE_PICKER_NEW_NAME).trimIndent(),
                category = Category.CORRECTNESS,
                priority = 3,
                severity = Severity.WARNING,
                implementation = IMPLEMENTATION
        ).setAndroidSpecific(true)
    }


    override fun getApplicableUastTypes() = listOf(UImportStatement::class.java)

    override fun createUastHandler(context: JavaContext) = InvalidImportHandler(context)

    class InvalidImportHandler(private val context: JavaContext) : UElementHandler() {
        override fun visitImportStatement(node: UImportStatement) {
            node.importReference?.let { importReference ->
                if (disallowedImports.any { importReference.asSourceString().contains(it) }) {
                    context.report(ISSUE, node,
                            context.getLocation(importReference), FORBIDDEN_IMPORT)
                }
            }
        }
    }

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        super.visitMethodCall(context, node, method)
        val evaluator = context.evaluator
        if (evaluator.isMemberInClass(method, DATE_PICKER))
            context.report(ISSUE, context.getLocation(node), USAGE_ERROR_MESSAGE_FORMAT.format(DATE_PICKER_NEW_NAME))
    }

    override fun getApplicableMethodNames(): List<String>? {
        return APPLICABLE_METHOD_NAME
    }

}
