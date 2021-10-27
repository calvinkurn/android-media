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
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UField
import org.jetbrains.uast.ULocalVariable
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
            priority = 5,
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
            SdkConstants.TAG_STRING
        )
    }

    private val resourceIds = arrayListOf<String>()

    override fun beforeCheckEachProject(context: Context) {
        findResourceIds(context)
    }

    override fun getApplicableUastTypes(): List<Class<out UElement>> {
        return listOf(
            UField::class.java,
            ULocalVariable::class.java,
            UCallExpression::class.java
        )
    }

    override fun createUastHandler(context: JavaContext): UElementHandler {
        return object : UElementHandler() {
            override fun visitField(node: UField) {
                val resource = node.text.substringAfter("= ")

                if(shouldScanResource(resource)) {
                    scanResource(context, node, resource)
                }
            }

            override fun visitLocalVariable(node: ULocalVariable) {
                val resource = node.text.substringAfter("= ")

                if(shouldScanResource(resource)) {
                    scanResource(context, node, resource)
                }
            }

            override fun visitCallExpression(node: UCallExpression) {
                node.valueArguments.firstOrNull {
                    val resource = it.asSourceString()
                    shouldScanResource(resource)
                }?.let {
                    val resource = it.asSourceString()
                    scanResource(context, node, resource)
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
        node: UElement,
        value: String
    ) {
        val resourceName = value.substringAfterLast(".")
        if(!resourceIds.contains(resourceName)) {
            reportError(context, node)
        }
    }

    private fun reportError(context: JavaContext, node: UElement) {
        context.report(
            JAVA_ISSUE,
            node,
            context.getLocation(node),
            ERROR_MESSAGE
        )
    }

    private fun findResourceIds(context: Context) {
        val resFolders = context.project.resourceFolders.firstOrNull()

        val drawableDirs = resFolders?.listFiles()
            ?.filter { it.name.contains(SdkConstants.FD_RES_DRAWABLE) }

        val valueDirs = resFolders?.listFiles()
            ?.filter { it.name.contains(SdkConstants.FD_RES_VALUES) }

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

    private fun findValueResources(files: Array<File>?) {
        files?.forEach { file ->
            val valueIds = file.readLines().filter {
                it.contains(SdkConstants.ATTR_NAME) && !it.contains(SdkConstants.TAG_ITEM)
            }.map {
                it.substringAfter("=\"").substringBefore("\"")
            }.filter { it.isNotEmpty() }
            resourceIds.addAll(valueIds)
        }
    }
}
