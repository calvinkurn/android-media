package com.tokopedia.linter.detectors

import com.android.SdkConstants.DRAWABLE_FOLDER
import com.android.SdkConstants.RES_FOLDER
import com.android.SdkConstants.TAG_ANIMATED_VECTOR
import com.android.resources.ResourceFolderType
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.SourceCodeScanner
import com.android.tools.lint.detector.api.XmlContext
import com.android.tools.lint.detector.api.XmlScanner
import com.android.tools.lint.detector.api.getBaseName
import com.android.utils.XmlUtils
import com.google.common.collect.Sets
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UExpression
import org.w3c.dom.Element
import java.io.File

class AnimatedVectorDetector: Detector(), SourceCodeScanner, XmlScanner {

    companion object {
        val JAVA_ISSUE = Issue.create(
            id = "AnimatedVectorDrawable",
            briefDescription = "Unsafe animated vector usage.",
            explanation = "Animated vector drawable without <target> tag can lead to crash on lollipop device.",
            category = Category.CORRECTNESS,
            priority = 5,
            severity = Severity.FATAL,
            implementation = Implementation(AnimatedVectorDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )

        val XML_ISSUE = Issue.create(
            id = "AnimatedVectorDrawable",
            briefDescription = "Unsafe animated vector usage.",
            explanation = "Animated vector drawable without <target> tag can lead to crash on lollipop device.",
            category = Category.CORRECTNESS,
            priority = 5,
            severity = Severity.FATAL,
            implementation = Implementation(AnimatedVectorDetector::class.java, Scope.RESOURCE_FILE_SCOPE)
        )

        private const val TAG_TARGET = "<target"
        private const val METHOD_CREATE = "create"
        private const val ANIMATED_VECTOR_PACKAGE = "androidx.vectordrawable.graphics.drawable"
        private const val ANIMATED_VECTOR_CLASS = "$ANIMATED_VECTOR_PACKAGE.AnimatedVectorDrawableCompat"
        private const val ERROR_MESSAGE = "Unsafe animated vector usage. Animated vector should have <target> tag."
    }

    private val animatedVectorResources = Sets.newHashSet<String>()

    override fun appliesTo(folderType: ResourceFolderType): Boolean {
        return folderType == ResourceFolderType.DRAWABLE
    }

    override fun getApplicableElements(): Collection<String>? {
        return listOf(TAG_ANIMATED_VECTOR)
    }

    override fun visitElement(context: XmlContext, element: Element) {
        if(context.file.isNotSafeVector()) {
            reportXmlError(context, element)
        }
    }

    override fun getApplicableMethodNames(): List<String>? {
        return listOf(METHOD_CREATE)
    }

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        val evaluator = context.evaluator

        if(evaluator.isMemberInClass(method, ANIMATED_VECTOR_CLASS)) {
            scanAnimatedVector(context)
            scanJavaError(context, node)
        }
    }

    private fun scanAnimatedVector(context: JavaContext) {
        val libFolders = context.project.directLibraries.filter { it.dir.isDirectory }
        val resourceFolders = context.project.resourceFolders
        val fileNames = resourceFolders.findAnimatedVector()

        libFolders.forEach { project ->
            val libFileNames = project.resourceFolders.findAnimatedVector()
            animatedVectorResources.addAll(libFileNames)
        }

        animatedVectorResources.addAll(fileNames)
    }

    private fun scanJavaError(context: JavaContext, node: UCallExpression) {
        node.valueArguments.lastOrNull()?.let { arg ->
            val drawableName = arg.asSourceString()
                .substringAfterLast(".")

            if(animatedVectorResources.contains(drawableName)) {
                reportJavaError(context, arg)
            }
        }
    }

    private fun reportJavaError(context: JavaContext, node: UExpression) {
        context.report(
            JAVA_ISSUE,
            node,
            context.getNameLocation(node),
            ERROR_MESSAGE
        )
    }

    private fun reportXmlError(context: XmlContext, node: Element) {
        context.report(
            XML_ISSUE,
            node,
            context.getNameLocation(node),
            ERROR_MESSAGE
        )
    }

    private fun List<File>.findAnimatedVector(): List<String> {
        val fileNames = mutableListOf<String>()
        val resDir = firstOrNull { it.name.contains(RES_FOLDER) }
        val drawableDirs = resDir?.listFiles()?.filter { it.name.contains(DRAWABLE_FOLDER) }

        drawableDirs?.forEach { file ->
            val files = file.listFiles()?.filter { it.isNotSafeVector() }.orEmpty()
            val vectorNames = files.map { getBaseName(it.name) }
            fileNames.addAll(vectorNames)
        }

        return fileNames
    }

    private fun File.isNotSafeVector(): Boolean {
        val rootTag = XmlUtils.getRootTagName(this)
        val containsTargetTag = readText().contains(TAG_TARGET)
        return rootTag == TAG_ANIMATED_VECTOR && !containsTargetTag
    }
}