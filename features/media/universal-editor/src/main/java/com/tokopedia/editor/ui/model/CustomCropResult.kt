package com.tokopedia.editor.ui.model

import android.graphics.Bitmap

data class CustomCropResult(
    val bitmap: Bitmap,
    val matrix: FloatArray,
    val outputPath: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CustomCropResult

        if (bitmap != other.bitmap) return false
        if (!matrix.contentEquals(other.matrix)) return false
        if (outputPath != other.outputPath) return false

        return true
    }

    override fun hashCode(): Int {
        var result = bitmap.hashCode()
        result = 31 * result + matrix.contentHashCode()
        result = 31 * result + outputPath.hashCode()
        return result
    }
}
