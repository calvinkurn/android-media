package com.tokopedia.linter.detectors.resources
import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Context
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.LintFix
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.SourceCodeScanner
import com.android.tools.lint.detector.api.isKotlin
import com.tokopedia.linter.util.sourceImplementation
import com.tokopedia.linter.util.toAlias
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtImportDirective
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UQualifiedReferenceExpression

private const val FQN_ANDROID_R = "android.R"

/** Reports an error when an R class is referenced using its fully qualified name. */
class FullyQualifiedResourceDetector : Detector(), SourceCodeScanner {
    private lateinit var importAliases: Map<String, String>

    override fun beforeCheckRootProject(context: Context) {
        super.beforeCheckRootProject(context)

        importAliases = ImportAliasesLoader.loadImportAliases(context)
    }

    override fun getApplicableUastTypes(): List<Class<out UElement>> =
        listOf(UQualifiedReferenceExpression::class.java)

    override fun createUastHandler(context: JavaContext): UElementHandler {
        return object : UElementHandler() {

            override fun visitQualifiedReferenceExpression(node: UQualifiedReferenceExpression) {
                // Import alias is a Kotlin feature.
                if (!isKotlin(node.lang)) return

                val qualifier = node.receiver.asSourceString()
                if (qualifier.endsWith(".R") && qualifier != FQN_ANDROID_R) {
                    val alias = qualifier.toAlias()

                    context.report(
                        ISSUE,
                        context.getNameLocation(node.receiver),
                        if (alias != null) {
                            "Use $alias as an import alias instead"
                        } else {
                            "Use an import alias instead"
                        },
                        quickfixData = createLintFix(alias, node, qualifier)
                    )
                }
            }

            private fun createLintFix(
                alias: String?,
                node: UQualifiedReferenceExpression,
                qualifier: String
            ): LintFix? {
                return if (alias != null) {
                    val fixes =
                        mutableListOf(
                            fix().replace().range(context.getLocation(node.receiver)).with(alias).build()
                        )

                    // Alternative to ReplaceStringBuilder#imports since that one didn't work here.
                    addImportIfMissing(qualifier, alias, fixes)

                    fix()
                        .name("Replace with import alias")
                        .composite(*fixes.reversed().toTypedArray())
                        .autoFix()
                } else {
                    null
                }
            }

            private fun addImportIfMissing(
                qualifier: String,
                alias: String,
                issues: MutableList<LintFix>
            ) {
                context.uastFile?.imports?.let { imports ->
                    val importIsMissing =
                        !imports.any {
                            val importDirective = it.sourcePsi as? KtImportDirective

                            val importedFqNameString = importDirective?.importedFqName?.asString()
                            qualifier == importedFqNameString && alias == importDirective.aliasName
                        }

                    if (importIsMissing) {
                        if (imports.isNotEmpty()) {
                            val lastImport = imports.last().sourcePsi as? KtImportDirective
                            addImportAfterLastImport(lastImport, qualifier, alias, issues)
                        } else {
                            addImportAfterPackageName(qualifier, alias, issues)
                        }
                    }
                }
            }

            private fun addImportAfterLastImport(
                lastImport: KtImportDirective?,
                qualifier: String,
                alias: String,
                issues: MutableList<LintFix>
            ) {
                if (lastImport != null) {
                    issues.add(
                        fix()
                            .replace()
                            .range(context.getLocation(lastImport))
                            .with(lastImport.text + System.lineSeparator() + "import $qualifier as $alias")
                            .build()
                    )
                }
            }

            private fun addImportAfterPackageName(
                qualifier: String,
                alias: String,
                issues: MutableList<LintFix>
            ) {
                (context.psiFile as? KtFile)?.packageDirective?.let { packageDirective ->
                    issues.add(
                        fix()
                            .replace()
                            .range(context.getLocation(packageDirective))
                            .with(
                                packageDirective.text +
                                    System.lineSeparator() +
                                    System.lineSeparator() +
                                    "import $qualifier as $alias"
                            )
                            .build()
                    )
                }
            }
        }
    }

    companion object {
        val ISSUE: Issue =
            Issue.create(
                "FullyQualifiedResource",
                "Resources should use an import alias instead of being fully qualified.",
                "Resources should use an import alias instead of being fully qualified. For example: \n" +
                    "com.tokopedia.resources.common.R as commonR\n" +
                    "...\n" +
                    "...getString(commonR.string.app_name)",
                Category.CORRECTNESS,
                6,
                Severity.ERROR,
                sourceImplementation<FullyQualifiedResourceDetector>()
            )
                .setOptions(listOf(ImportAliasesLoader.IMPORT_ALIASES))
    }
}
