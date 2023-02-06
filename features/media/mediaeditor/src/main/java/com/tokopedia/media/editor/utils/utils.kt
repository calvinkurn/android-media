package com.tokopedia.media.editor.utils

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import androidx.core.content.ContextCompat
import com.tokopedia.media.editor.R
import com.tokopedia.media.editor.analytics.REMOVE_BG_TYPE_GREY
import com.tokopedia.media.editor.analytics.REMOVE_BG_TYPE_ORI
import com.tokopedia.media.editor.analytics.REMOVE_BG_TYPE_WHITE
import com.tokopedia.media.editor.analytics.WATERMARK_TYPE_CENTER
import com.tokopedia.media.editor.analytics.WATERMARK_TYPE_DIAGONAL
import com.tokopedia.media.editor.data.repository.WatermarkType
import com.tokopedia.media.editor.ui.uimodel.EditorCropRotateUiModel
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel
import com.tokopedia.picker.common.ImageRatioType
import com.tokopedia.picker.common.types.EditorToolType
import com.tokopedia.utils.file.FileUtil
import com.tokopedia.utils.image.ImageProcessingUtil
import java.io.File

private const val MEDIA_EDITOR_CACHE_DIR = "Editor-Cache"

@Suppress("SpellCheckingInspection")
fun getTokopediaCacheDir(): String {
    return FileUtil.getTokopediaInternalDirectory(ImageProcessingUtil.DEFAULT_DIRECTORY).absolutePath
}

fun getEditorSaveFolderPath(): String {
    return ImageProcessingUtil.DEFAULT_DIRECTORY + MEDIA_EDITOR_CACHE_DIR
}

fun getUCropTempResultPath(): Uri {
    val folderPath = FileUtil.getTokopediaInternalDirectory(getEditorSaveFolderPath()).path
    val dir = File(folderPath)
    if (!dir.exists()) dir.mkdir()

    return Uri.fromFile(File("${folderPath}/uCrop_temp_result.png"))
}

fun isGranted(context: Context, permission: String): Boolean {
    return ContextCompat.checkSelfPermission(context, permission) ==
        PackageManager.PERMISSION_GRANTED
}

//formula to determine brightness 0.299 * r + 0.0f + 0.587 * g + 0.0f + 0.114 * b + 0.0f
// if total of dark pixel > total of pixel * 0.45 count that as dark image
fun Bitmap.isDark(): Boolean {
    try {
        val ratio = this.width.toFloat() / this.height
        val widthBitmapChecker = 50
        val heightBitmapChecker = widthBitmapChecker * ratio
        val bitmapChecker =
            Bitmap.createScaledBitmap(this, widthBitmapChecker, heightBitmapChecker.toInt(), false)
        val darkThreshold = bitmapChecker.width * bitmapChecker.height * 0.45f
        var darkPixels = 0
        val pixels = IntArray(bitmapChecker.width * bitmapChecker.height)
        bitmapChecker.getPixels(
            pixels,
            0,
            bitmapChecker.width,
            0,
            0,
            bitmapChecker.width,
            bitmapChecker.height
        )
        val luminanceThreshold = 150
        for (i in pixels.indices) {
            val color = pixels[i]
            val r = Color.red(color)
            val g = Color.green(color)
            val b = Color.blue(color)
            val luminance = 0.299 * r + 0.0f + 0.587 * g + 0.0f + 0.114 * b + 0.0f
            if (luminance < luminanceThreshold) {
                darkPixels++
            }
        }
        bitmapChecker.recycle()
        return darkPixels >= darkThreshold
    } catch (t: Throwable) {
        return false
    }
}

fun getToolEditorText(editorToolType: Int): Int {
    return when (editorToolType) {
        EditorToolType.BRIGHTNESS -> R.string.editor_tool_brightness
        EditorToolType.CONTRAST -> R.string.editor_tool_contrast
        EditorToolType.WATERMARK -> R.string.editor_tool_watermark
        EditorToolType.ROTATE -> R.string.editor_tool_rotate
        EditorToolType.CROP -> R.string.editor_tool_crop
        else -> R.string.editor_tool_remove_background
    }
}

fun cropCenterImage(
    sourceBitmap: Bitmap?,
    cropRaw: ImageRatioType
): Pair<Bitmap, EditorCropRotateUiModel>? {
    sourceBitmap?.let { bitmap ->
        // ratio height to width (to get height value)
        val autoCropRatio = cropRaw.getRatioY().toFloat() / cropRaw.getRatioX()

        val bitmapWidth = bitmap.width
        val bitmapHeight = bitmap.height

        // if source image is already have ratio target then skip crop
        if (bitmapWidth.toFloat() / bitmapHeight == cropRaw.getRatio()) {
            return null
        }

        var newWidth = bitmapWidth
        var newHeight = (bitmapWidth * autoCropRatio).toInt()

        var topMargin = 0
        var leftMargin = 0

        if (newHeight <= bitmapHeight && newWidth <= bitmapWidth) {
            leftMargin = (bitmapWidth - newWidth) / 2
            topMargin = (bitmapHeight - newHeight) / 2
        } else if (newHeight > bitmapHeight) {
            val scaledTarget = bitmapHeight.toFloat() / newHeight

            // new value after rescale small
            newWidth = (newWidth * scaledTarget).toInt()
            newHeight = (newHeight * scaledTarget).toInt()

            leftMargin = (bitmapWidth - newWidth) / 2
            topMargin = (bitmapHeight - newHeight) / 2
        }

        val bitmapResult = Bitmap.createBitmap(bitmap, leftMargin, topMargin, newWidth, newHeight)

        val cropDetail = EditorCropRotateUiModel().apply {
            offsetX = leftMargin
            offsetY = topMargin
            imageWidth = newWidth
            imageHeight = newHeight
            scaleX = 1f
            scaleY = 1f
            isCrop = true
            this.isAutoCrop = true
            croppedSourceWidth = bitmap.width
            cropRatio = cropRaw.ratio
        }

        return Pair(bitmapResult, cropDetail)
    }
    return null
}

// for analytics purpose
fun cropRatioToText(ratio: Pair<Int, Int>): String {
    return if (ratio.first != 0) {
        "${ratio.first}:${ratio.second}"
    } else {
        ""
    }
}

fun removeBackgroundToText(removeBackgroundType: Int?): String {
    return when (removeBackgroundType) {
        EditorDetailUiModel.REMOVE_BG_TYPE_GRAY -> REMOVE_BG_TYPE_GREY
        EditorDetailUiModel.REMOVE_BG_TYPE_WHITE -> REMOVE_BG_TYPE_WHITE
        else -> REMOVE_BG_TYPE_ORI
    }
}

fun watermarkToText(watermarkType: Int?): String {
    return when (watermarkType) {
        WatermarkType.Center.value -> WATERMARK_TYPE_CENTER
        WatermarkType.Diagonal.value -> WATERMARK_TYPE_DIAGONAL
        else -> ""
    }
}
