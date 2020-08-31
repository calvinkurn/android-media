package com.tokopedia.linter.detectors

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import org.jetbrains.uast.UImportStatement

@Suppress("UnstableApiUsage")
class CoreResourcesDetector : Detector(), Detector.UastScanner  {
    override fun getApplicableUastTypes() = listOf(UImportStatement::class.java)

    override fun createUastHandler(context: JavaContext) = InvalidImportHandler(context)

    class InvalidImportHandler(private val context: JavaContext) : UElementHandler() {
        override fun visitImportStatement(node: UImportStatement) {
            node.importReference?.let { importReference ->
                if (disallowedImports.any { importReference.asSourceString().contains(it) }) {
                    context.report(ISSUE, node, context.getLocation(importReference), "Forbidden import")
                }
            }
        }
    }

    companion object {
        private val disallowedImports = listOf("com.tokopedia.core.R.","com.tokopedia.core.R")
        private val IMPLEMENTATION = Implementation(
                CoreResourcesDetector::class.java,
                Scope.JAVA_FILE_SCOPE
        )

        val ISSUE: Issue = Issue.create(
                id = "CoreResourcesUsageError",
                briefDescription = "Do not use core resources",
                explanation = """
                    This lint check prevents usage of `com.tokopedia.core.R`.
                """.trimIndent(),
                category = Category.CORRECTNESS,
                priority = 3,
                severity = Severity.WARNING,
                implementation = IMPLEMENTATION
        ).setAndroidSpecific(true)
    }
}