package com.tokopedia.linter.detectors.sourcescanner.elements.annotation

import com.android.tools.lint.detector.api.JavaContext
import org.jetbrains.uast.UAnnotation

object AnnotationDetector {
    const val SERIALIZED_NAME = "com.google.gson.annotations.SerializedName"
    fun checkAnnotation(context:JavaContext,node:UAnnotation) {
        val type: String? = node.qualifiedName
        when(type){
            SERIALIZED_NAME -> {
                ServerResponseDataTypeDetector.checkAndCreateError(context,node)
            }
        }
    }
}