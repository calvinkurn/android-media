package com.tokopedia.media.editor.utils

import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.*
import android.net.Uri
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.fragment.app.Fragment
import com.tokopedia.kotlin.extensions.view.toBitmap
import com.tokopedia.media.editor.R
import com.tokopedia.media.editor.ui.uimodel.EditorCropRotateUiModel
import com.tokopedia.picker.common.ImageRatioType
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.file.FileUtil
import com.tokopedia.utils.image.ImageProcessingUtil
import timber.log.Timber
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

fun cropCenterImage(
    sourceBitmap: Bitmap?,
    cropRaw: ImageRatioType
): EditorCropRotateUiModel? {
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

        return cropDetail
    }
    return null
}

// validation for each delay to make sure fragment still exist
fun Fragment.getRunnable(action: () -> Unit): Runnable {
    return Runnable {
        if (isAdded) {
            action()
        }
    }
}

fun Fragment.delay(action: () -> Unit, delayTime: Long) {
    Handler().postDelayed(getRunnable {
        action()
    }, delayTime)
}

private fun checkBitmapSizeOverflow(width: Float, height: Float): Boolean {
    val imagePxDrawThreshold = 25_000_000 // 25 million pixel
    return (width * height) >= imagePxDrawThreshold
}

// get image size without load the image
fun getImageSize(path: String): Pair<Int, Int> {
    return try {
        val option = BitmapFactory.Options()
        option.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, option)
        return Pair(option.outWidth, option.outHeight)
    } catch (e: Exception) {
        Timber.d("get image size bound error, ${e.message}")
        Pair(0, 0)
    }
}

// scale down image size (if needed) until canvas draw limit size (25 million pixel)
// used only when implement image to imageView
fun validateImageSize(source: Bitmap): Bitmap {
    // used to decide scaled result value for each scaled down iteration
    // each iteration will reduce image size 10% (100px -> 90px -> 81px -> etc)
    val scaledFactor = 0.9f
    return if (checkBitmapSizeOverflow(source.width.toFloat(), source.height.toFloat())) {
        var newImageHeight = 0f
        var newImageWidth = source.width.toFloat()
        val sourceWidth = source.width
        val sourceHeight = source.height
        do {
            newImageWidth *= scaledFactor
            newImageHeight = (newImageWidth / sourceWidth) * sourceHeight
        } while (checkBitmapSizeOverflow(newImageWidth, newImageHeight))

        return Bitmap.createScaledBitmap(
            source,
            newImageWidth.toInt(),
            newImageHeight.toInt(),
            true
        )
    } else {
        source
    }
}

private fun Context.getFreeMemory(): Long {
    val mi = ActivityManager.MemoryInfo()
    val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    activityManager.getMemoryInfo(mi)
    return mi.availMem
}

fun Context.checkMemoryOverflow(memoryUsage: Int): Boolean {
    return getFreeMemory() < memoryUsage
}

fun Context.showMemoryLimitToast(imageSize: Pair<Int, Int>, msg: String? = null, multiplier: Float) {
    // format = image width || image height || avail memory
    newRelicLog(
        mapOf(
            "Error" to (msg ?: ""),
            MEMORY_LIMIT_FIELD to "width: ${imageSize.first} || height: ${imageSize.second} || " +
                "avail memory: ${this.getFreeMemory()} -- multiplier $multiplier"
        )
    )

    Toast.makeText(
        this,
        R.string.editor_activity_memory_limit,
        Toast.LENGTH_LONG
    ).show()
}

fun showErrorLoadToaster(view: View, msg: String?) {
    msg?.let {
        newRelicLog(
            mapOf(
                LOAD_IMAGE_FAILED to it
            )
        )
    }

    if (view.isAttachedToWindow) {
        Toaster.build(
            view,
            view.context.resources.getString(R.string.editor_activity_failed_load_image),
            Toaster.LENGTH_LONG,
            Toaster.TYPE_ERROR
        ).show()
    }
}

fun showErrorGeneralToaster(context: Context?, msg: String? = null) {
    if (context == null) return
    Toast.makeText(
        context,
        context.resources.getString(R.string.editor_activity_general_error),
        Toast.LENGTH_LONG
    ).show()

    if (msg != null) {
        newRelicLog(
            mapOf(
                GENERAL_ERROR to msg
            )
        )
    }
}

fun roundedBitmap(
    context: Context,
    source: Bitmap,
    cornerRadius: Float = 0f,
    isCircular: Boolean = false
): Bitmap {
    val roundedBitmap = RoundedBitmapDrawableFactory.create(context.resources, source)
    roundedBitmap.cornerRadius = if (isCircular) (source.width.toFloat() / 2) else cornerRadius

    return roundedBitmap.toBitmap()
}
