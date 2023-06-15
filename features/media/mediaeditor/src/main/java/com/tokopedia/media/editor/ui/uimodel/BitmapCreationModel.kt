package com.tokopedia.media.editor.ui.uimodel

import android.graphics.Bitmap
import android.graphics.Bitmap.Config
import android.graphics.Matrix

data class BitmapCreationModel(
    val source: Bitmap? = null,
    val width: Int,
    val height: Int,
    val filter: Boolean? = null,
    val config: Bitmap.Config? = null,
    val position: Pair<Int, Int>? = null,
    val matrix: Matrix? = null,
    val modelType: Int
)

class BitmapCreation {
    companion object {
        const val CREATION_SCALED = 0
        const val CREATION_CROP = 1
        const val CREATION_EMPTY = 2

        fun scaledBitmap(
            src: Bitmap,
            dstWidth: Int,
            dstHeight: Int,
            filter: Boolean
        ): BitmapCreationModel {
            return BitmapCreationModel(
                source = src,
                width = dstWidth,
                height = dstHeight,
                filter = filter,
                modelType = CREATION_SCALED
            )
        }

        fun emptyBitmap(
            width: Int,
            height: Int,
            config: Config
        ): BitmapCreationModel {
            return BitmapCreationModel(
                width = width,
                height = height,
                config = config,
                modelType = CREATION_EMPTY
            )
        }

        fun cropBitmap(
            source: Bitmap,
            x: Int,
            y: Int,
            width: Int,
            height: Int,
            matrix: Matrix? = null,
            filter: Boolean? = null
        ): BitmapCreationModel {
            return BitmapCreationModel(
                source = source,
                position = Pair(x, y),
                width = width,
                height = height,
                matrix = matrix,
                filter = filter,
                modelType = CREATION_CROP
            )
        }
    }
}
