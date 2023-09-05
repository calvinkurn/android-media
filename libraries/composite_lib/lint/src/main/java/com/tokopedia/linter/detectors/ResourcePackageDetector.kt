package com.tokopedia.linter.detectors

import com.android.SdkConstants
import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Context
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.SourceCodeScanner
import com.android.tools.lint.detector.api.getBaseName
import com.intellij.psi.PsiElement
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UField
import org.jetbrains.uast.UFieldEx
import org.jetbrains.uast.ULocalVariable
import org.jetbrains.uast.UReturnExpression
import java.io.File

class ResourcePackageDetector : Detector(), SourceCodeScanner {

    companion object {
        val JAVA_ISSUE = Issue.create(
            id = "ResourcePackage",
            briefDescription = "Resource location in different module can lead to crash.",
            explanation = "Resource location is in different module. " +
                "Please use resource with its package name. " +
                "Example: com.tokopedia.unifyprinciples.R.dimen.spacing_lvl1",
            category = Category.CORRECTNESS,
            priority = 10,
            severity = Severity.FATAL,
            implementation = Implementation(
                ResourcePackageDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )

        private const val ERROR_MESSAGE = "Resource location is in different module. " +
            "Please use resource with its package name. " +
            "Example: com.tokopedia.unifyprinciples.R.dimen.spacing_lvl1"

        private val RESOURCE_TAG = listOf(
            SdkConstants.TAG_DRAWABLE,
            SdkConstants.TAG_DIMEN,
            SdkConstants.TAG_COLOR,
            SdkConstants.TAG_STYLE,
            SdkConstants.TAG_STRING,
            SdkConstants.TAG_LAYOUT
        )
    }

    private val resourceIds = arrayListOf<String>()

    override fun beforeCheckEachProject(context: Context) {
        findResourceIds(context)
    }

    override fun getApplicableUastTypes(): List<Class<out UElement>> {
        return listOf(
            UFieldEx::class.java,
            ULocalVariable::class.java,
            UCallExpression::class.java,
            UReturnExpression::class.java
        )
    }

    override fun createUastHandler(context: JavaContext): UElementHandler {
        return object : UElementHandler() {
            override fun visitField(node: UField) {
                val resource = node.text.substringAfter("= ")

                if (shouldScanResource(resource)) {
                    scanResource(
                        context = context,
                        node = node,
                        value = resource
                    )
                }
            }

            override fun visitLocalVariable(node: ULocalVariable) {
                val resource = node.text.substringAfter("= ")

                if (shouldScanResource(resource)) {
                    scanResource(
                        context = context,
                        node = node,
                        value = resource
                    )
                }
            }

            override fun visitCallExpression(node: UCallExpression) {
                node.valueArguments.firstOrNull {
                    val resource = it.asSourceString()
                    shouldScanResource(resource)
                }?.let {
                    scanResource(
                        context = context,
                        node = node,
                        value = it.asSourceString()
                    )
                }
            }

            override fun visitReturnExpression(node: UReturnExpression) {
                val element = node.sourcePsi?.lastChild
                val resource = element?.text.orEmpty()

                if (shouldScanResource(resource)) {
                    scanResource(
                        context = context,
                        psi = element,
                        value = resource
                    )
                }
            }
        }
    }

    private fun shouldScanResource(resource: String): Boolean {
        val regex = Regex("^[R.]{2}.*.[.].*")
        return resource.matches(regex) && checkResourceType(resource)
    }

    private fun checkResourceType(resource: String): Boolean {
        val resourceType = resource.substringAfter(".")
            .substringBefore(".")
        return RESOURCE_TAG.contains(resourceType)
    }

    private fun scanResource(
        context: JavaContext,
        node: UElement? = null,
        psi: PsiElement? = null,
        value: String
    ) {
        val resourceName = value.substringAfterLast(".")
        if (!resourceIds.contains(resourceName)) {
            reportError(context, node, psi)
        }
    }

    private fun reportError(context: JavaContext, node: UElement?, psi: PsiElement?) {
        if (psi != null) {
            context.report(
                JAVA_ISSUE,
                psi,
                context.getLocation(psi),
                ERROR_MESSAGE
            )
        } else {
            node?.let {
                context.report(
                    JAVA_ISSUE,
                    node,
                    context.getLocation(it),
                    ERROR_MESSAGE
                )
            }
        }
    }

    private fun findResourceIds(context: Context) {
        context.project.resourceFolders.forEach { resFolder ->
            val layoutDirs = resFolder.listFiles()
                ?.filter { it.name.contains(SdkConstants.FD_RES_LAYOUT) }

            val drawableDirs = resFolder.listFiles()
                ?.filter { it.name.contains(SdkConstants.FD_RES_DRAWABLE) }

            val valueDirs = resFolder.listFiles()
                ?.filter { it.name.contains(SdkConstants.FD_RES_VALUES) }

            layoutDirs?.forEach { file ->
                val files = file.listFiles().orEmpty()
                val drawable = files.map { getBaseName(it.name) }
                resourceIds.addAll(drawable)
            }

            drawableDirs?.forEach { file ->
                val files = file.listFiles().orEmpty()
                val drawable = files.map { getBaseName(it.name) }
                resourceIds.addAll(drawable)
            }

            valueDirs?.forEach {
                val files = it.listFiles()
                findValueResources(files)
            }
        }
    }

    private fun findValueResources(files: Array<File>?) {
        files?.forEach { file ->
            val valueIds = file.readLines().filter {
                val nameAttr = "${SdkConstants.ATTR_NAME}="
                val itemTag = "<${SdkConstants.TAG_ITEM}"
                it.contains(nameAttr) && !it.contains(itemTag)
            }.map {
                it.substringAfter("=\"").substringBefore("\"")
            }.filter { it.isNotEmpty() }
            resourceIds.addAll(valueIds)
        }
    }
}
