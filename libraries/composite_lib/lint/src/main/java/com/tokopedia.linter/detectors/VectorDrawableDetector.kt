package com.tokopedia.linter.detectors

import com.android.SdkConstants.*
import com.android.tools.lint.detector.api.*
import com.android.utils.XmlUtils
import com.google.common.collect.Sets
import org.w3c.dom.Attr
import java.io.File

class VectorDrawableDetector : Detector(), XmlScanner {

    companion object {
        val ISSUE = Issue.create(
            id = "VectorDrawable",
            briefDescription = "Unsafe vector drawable usage.",
            explanation = "Vector drawable can lead to crash on pre-lollipop device. " +
                "Change drawable to non-vector or follow suggestion.",
            category = Category.CORRECTNESS,
            priority = 5,
            severity = Severity.FATAL,
            implementation = Implementation(VectorDrawableDetector::class.java, Scope.RESOURCE_FILE_SCOPE)
        )

        private const val ATTR_ANDROID_BACKGROUND = "${PREFIX_ANDROID}${ATTR_BACKGROUND}"
        private const val ATTR_ANDROID_DRAWABLE_LEFT = "${PREFIX_ANDROID}${ATTR_DRAWABLE_LEFT}"
        private const val ATTR_ANDROID_DRAWABLE_RIGHT = "${PREFIX_ANDROID}${ATTR_DRAWABLE_RIGHT}"
        private const val ATTR_ANDROID_DRAWABLE_TOP = "${PREFIX_ANDROID}${ATTR_DRAWABLE_TOP}"
        private const val ATTR_ANDROID_DRAWABLE_BOTTOM = "${PREFIX_ANDROID}${ATTR_DRAWABLE_BOTTOM}"
        private const val ATTR_ANDROID_DRAWABLE_START = "${PREFIX_ANDROID}${ATTR_DRAWABLE_START}"
        private const val ATTR_ANDROID_DRAWABLE_END = "${PREFIX_ANDROID}${ATTR_DRAWABLE_END}"
    }

    private val vectorResources = Sets.newHashSet<String>()

    override fun getApplicableAttributes(): Collection<String>? {
        return listOf(
            ATTR_BACKGROUND,
            ATTR_DRAWABLE_LEFT,
            ATTR_DRAWABLE_RIGHT,
            ATTR_DRAWABLE_TOP,
            ATTR_DRAWABLE_BOTTOM,
            ATTR_DRAWABLE_START,
            ATTR_DRAWABLE_END
        )
    }

    override fun visitAttribute(context: XmlContext, attribute: Attr) {
        val libFolders = context.project.directLibraries.filter { it.dir.isDirectory }
        val resourceFolders = context.project.resourceFolders
        val fileNames = resourceFolders.findFileNames()

        libFolders.forEach { project ->
            val libFileNames = project.resourceFolders.findFileNames()
            vectorResources.addAll(libFileNames)
        }

        vectorResources.addAll(fileNames)

        if (attribute.hasVector()) {
            when (attribute.name) {
                ATTR_ANDROID_DRAWABLE_LEFT,
                ATTR_ANDROID_DRAWABLE_RIGHT,
                ATTR_ANDROID_DRAWABLE_TOP,
                ATTR_ANDROID_DRAWABLE_BOTTOM,
                ATTR_ANDROID_DRAWABLE_START,
                ATTR_ANDROID_DRAWABLE_END -> reportDrawableError(context, attribute)
                ATTR_ANDROID_BACKGROUND -> reportBackgroundError(context, attribute)
            }
        }
    }

    private fun reportDrawableError(context: XmlContext, attribute: Attr) {
        val attrName = attribute.name
        val message = "Unsafe vector as $attrName. " +
            "Consider using setCompoundDrawablesWithIntrinsicBounds() programmatically."

        val lintFix = LintFix.create()
            .unset()
            .attribute(attrName)
            .build()

        reportError(context, attribute, message, lintFix)
    }

    private fun reportBackgroundError(context: XmlContext, attribute: Attr) {
        val attrName = attribute.name
        val message = "Unsafe vector as $attrName. " +
            "Consider using setBackgroundResource() programmatically."

        val lintFix = LintFix.create()
            .unset()
            .attribute(attrName)
            .build()

        reportError(context, attribute, message, lintFix)
    }

    private fun reportError(
        context: XmlContext,
        attribute: Attr,
        message: String,
        quickFix: LintFix? = null
    ) {
        context.report(
            ISSUE,
            attribute,
            context.getLocation(attribute),
            message,
            quickFix
        )
    }

    private fun List<File>.findFileNames(): List<String> {
        val fileNames = mutableListOf<String>()
        val resDir = firstOrNull { it.name.contains(RES_FOLDER) }
        val drawableDirs = resDir?.listFiles()?.filter { it.name.contains(DRAWABLE_FOLDER) }

        drawableDirs?.forEach { file ->
            val files = file.listFiles()?.filter { it.isVector() }.orEmpty()
            val vectorNames = files.map { getBaseName(it.name) }
            fileNames.addAll(vectorNames)
        }

        return fileNames
    }

    private fun File.isVector(): Boolean {
        val rootTag = XmlUtils.getRootTagName(this)
        return rootTag == TAG_VECTOR || rootTag == TAG_ANIMATED_VECTOR
    }

    private fun Attr.hasVector(): Boolean {
        val value = value.substringAfter("/")
        return vectorResources.contains(value)
    }
}