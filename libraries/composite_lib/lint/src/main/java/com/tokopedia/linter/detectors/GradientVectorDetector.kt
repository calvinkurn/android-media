package com.tokopedia.linter.detectors

import com.android.SdkConstants
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
import com.android.utils.XmlUtils
import com.google.common.collect.Sets
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression
import java.io.File

class GradientVectorDetector: Detector(), SourceCodeScanner {

    companion object {
        val ISSUE = Issue.create(
            id = "GradientVectorDrawable",
            briefDescription = "Unsafe gradient vector usage.",
            explanation = "Vector drawable with gradient tag can cause crash on devices below API 24.",
            category = Category.CORRECTNESS,
            priority = 5,
            severity = Severity.ERROR,
            implementation = Implementation(GradientVectorDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )

        private const val TAG_GRADIENT = "<gradient"
        private const val METHOD_GET_DRAWABLE = "getDrawable"
        private const val ERROR_MESSAGE = "Vector drawable with gradient tag can cause crash on devices below API 24. Consider using VectorDrawableCompat.create()."
    }

    private val gradientVectorResources = Sets.newHashSet<String>()

    override fun beforeCheckEachProject(context: Context) {
        scanGradientVector(context)
    }

    override fun getApplicableMethodNames(): List<String>? {
        return listOf(METHOD_GET_DRAWABLE)
    }

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        scanJavaError(context, node)
    }

    private fun scanJavaError(context: JavaContext, node: UCallExpression) {
        node.valueArguments.lastOrNull()?.let { arg ->
            val drawableName = arg.asSourceString()
                .substringAfterLast(".")

            if(gradientVectorResources.contains(drawableName)) {
                reportJavaError(context, node)
            }
        }
    }

    private fun reportJavaError(context: JavaContext, node: UCallExpression) {
        context.report(
            ISSUE,
            node,
            context.getNameLocation(node),
            ERROR_MESSAGE
        )
    }

    private fun List<File>.findGradientVector(): List<String> {
        val fileNames = mutableListOf<String>()
        val resDir = firstOrNull { it.name.contains(SdkConstants.RES_FOLDER) }
        val drawableDirs = resDir?.listFiles()?.filter { it.name.contains(SdkConstants.DRAWABLE_FOLDER) }

        drawableDirs?.forEach { file ->
            val files = file.listFiles()?.filter { it.isNotSafeVector() }.orEmpty()
            val vectorNames = files.map { getBaseName(it.name) }
            fileNames.addAll(vectorNames)
        }

        return fileNames
    }

    private fun File.isNotSafeVector(): Boolean {
        val rootTag = XmlUtils.getRootTagName(this)
        val containsGradientTag = readText().contains(TAG_GRADIENT)
        return rootTag == SdkConstants.TAG_VECTOR && containsGradientTag
    }

    private fun scanGradientVector(context: Context) {
        val libFolders = context.project.directLibraries.filter { it.dir.isDirectory }
        val resourceFolders = context.project.resourceFolders
        val fileNames = resourceFolders.findGradientVector()

        libFolders.forEach { project ->
            val libFileNames = project.resourceFolders.findGradientVector()
            gradientVectorResources.addAll(libFileNames)
        }

        gradientVectorResources.addAll(fileNames)
    }
}