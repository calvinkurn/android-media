@file:Suppress("SameParameterValue")
package com.tokopedia.picker.common.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import com.tokopedia.utils.image.ImageProcessingUtil.getCompressFormat
import com.tokopedia.utils.image.ImageProcessingUtil.writeImageToTkpdPath
import java.io.File
import java.io.InputStream
import kotlin.math.max
import kotlin.math.min

object ImageCompressor {

    private const val MAX_WIDTH = 2000f
    private const val MAX_HEIGHT = 2000f

    private const val MIN_WIDTH = 300
    private const val MIN_HEIGHT = 300

    private const val QUALITY = 80 // in percent

    fun compress(context: Context, imagePath: String): Uri? {
        val file = File(imagePath)
        val fileAsUri = Uri.fromFile(file)
        val compressFormat = file.absolutePath.getCompressFormat()
        val useMaxScale = true

        return compress(
            context,
            fileAsUri,
            compressFormat,
            MAX_WIDTH,
            MAX_HEIGHT,
            useMaxScale,
            QUALITY,
            MIN_WIDTH,
            MIN_HEIGHT
        )
    }

    private fun compress(
        context: Context,
        imageUri: Uri,
        compressFormat: Bitmap.CompressFormat,
        maxWidth: Float,
        maxHeight: Float,
        useMaxScale: Boolean,
        quality: Int,
        minWidth: Int,
        minHeight: Int
    ): Uri? {
        val bmOptions = decodeBitmapFromUri(context, imageUri)

        val scaleDownFactor = calculateScaleDownFactor(
            bmOptions, useMaxScale, maxWidth, maxHeight
        )

        setNearestInSampleSize(bmOptions, scaleDownFactor)

        val matrix = calculateImageMatrix(
            context, imageUri, scaleDownFactor, bmOptions
        ) ?: return null

        val newBitmap = generateNewBitmap(
            context, imageUri, bmOptions, matrix
        ) ?: return null

        val newBitmapWidth = newBitmap.width
        val newBitmapHeight = newBitmap.height

        val shouldScaleUp = shouldScaleUp(
            newBitmapWidth, newBitmapHeight, minWidth, minHeight
        )

        val scaleUpFactor = calculateScaleUpFactor(
            newBitmapWidth.toFloat(), newBitmapHeight.toFloat(), maxWidth, maxHeight,
            minWidth, minHeight, shouldScaleUp
        )

        val finalWidth = finalWidth(newBitmapWidth.toFloat(), scaleUpFactor)
        val finalHeight = finalHeight(newBitmapHeight.toFloat(), scaleUpFactor)

        val finalBitmap = scaleUpBitmapIfNeeded(newBitmap, finalWidth, finalHeight, scaleUpFactor, shouldScaleUp)

        val imageFilePath = writeImageToTkpdPath(
            bitmap = finalBitmap,
            compressFormat = compressFormat,
            quality = quality
        ) ?: return null

        return Uri.fromFile(imageFilePath)
    }

    private fun decodeBitmapFromUri(
        context: Context,
        imageUri: Uri
    ): BitmapFactory.Options {
        val bmOptions = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }

        val input = context.contentResolver.openInputStream(imageUri)
        BitmapFactory.decodeStream(input, null, bmOptions)
        input?.close()
        return bmOptions
    }

    private fun calculateScaleDownFactor(
        bmOptions: BitmapFactory.Options,
        useMaxScale: Boolean,
        maxWidth: Float,
        maxHeight: Float
    ): Float {
        val photoW = bmOptions.outWidth.toFloat()
        val photoH = bmOptions.outHeight.toFloat()

        val widthRatio = photoW / maxWidth
        val heightRatio = photoH / maxHeight

        var scaleFactor = if (useMaxScale) {
            max(widthRatio, heightRatio)
        } else {
            min(widthRatio, heightRatio)
        }

        if (scaleFactor < 1) {
            scaleFactor = 1f
        }

        return scaleFactor
    }

    private fun setNearestInSampleSize(
        bmOptions: BitmapFactory.Options,
        scaleFactor: Float
    ) {
        bmOptions.inJustDecodeBounds = false
        bmOptions.inSampleSize = scaleFactor.toInt()

        if (bmOptions.inSampleSize % 2 != 0) {
            var sample = 1
            while (sample * 2 < bmOptions.inSampleSize) {
                sample *= 2
            }
            bmOptions.inSampleSize = sample
        }
    }

    private fun calculateImageMatrix(
        context: Context,
        imageUri: Uri,
        scaleFactor: Float,
        bmOptions: BitmapFactory.Options
    ): Matrix? {
        val input = context.contentResolver.openInputStream(imageUri) ?: return null
        val exif = ExifInterface(input)
        val matrix = Matrix()

        val orientation = exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )

        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(
                90f
            )
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(
                180f
            )
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(
                270f
            )
        }

        val remainingScaleFactor = scaleFactor / bmOptions.inSampleSize.toFloat()

        if (remainingScaleFactor > 1) {
            matrix.postScale(1.0f / remainingScaleFactor, 1.0f / remainingScaleFactor)
        }

        input.close()

        return matrix
    }

    private fun generateNewBitmap(
        context: Context,
        imageUri: Uri,
        bmOptions: BitmapFactory.Options,
        matrix: Matrix
    ): Bitmap? {
        var bitmap: Bitmap? = null
        val inputStream = context.contentResolver.openInputStream(imageUri)

        try {
            bitmap = BitmapFactory.decodeStream(inputStream, null, bmOptions)
            if (bitmap != null) {
                val matrixScaledBitmap: Bitmap = Bitmap.createBitmap(
                    bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true
                )
                if (matrixScaledBitmap != bitmap) {
                    bitmap.recycle()
                    bitmap = matrixScaledBitmap
                }
            }
            inputStream?.close()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return bitmap
    }

    private fun shouldScaleUp(
        photoW: Int,
        photoH: Int,
        minWidth: Int,
        minHeight: Int
    ): Boolean {
        return (minWidth != 0 && minHeight != 0 && (photoW < minWidth || photoH < minHeight))
    }

    private fun calculateScaleUpFactor(
        photoW: Float,
        photoH: Float,
        maxWidth: Float,
        maxHeight: Float,
        minWidth: Int,
        minHeight: Int,
        shouldScaleUp: Boolean
    ): Float {
        var scaleUpFactor: Float = max(photoW / maxWidth, photoH / maxHeight)

        if (shouldScaleUp) {
            scaleUpFactor = if (photoW < minWidth && photoH > minHeight) {
                photoW / minWidth
            } else if (photoW > minWidth && photoH < minHeight) {
                photoH / minHeight
            } else {
                max(photoW / minWidth, photoH / minHeight)
            }
        }

        return scaleUpFactor
    }

    private fun finalWidth(photoW: Float, scaleUpFactor: Float): Int {
        return (photoW / scaleUpFactor).toInt()
    }

    private fun finalHeight(photoH: Float, scaleUpFactor: Float): Int {
        return (photoH / scaleUpFactor).toInt()
    }

    private fun scaleUpBitmapIfNeeded(
        bitmap: Bitmap,
        finalWidth: Int,
        finalHeight: Int,
        scaleUpFactor: Float,
        shouldScaleUp: Boolean
    ): Bitmap {
        val scaledBitmap = if (scaleUpFactor > 1 || shouldScaleUp) {
            Bitmap.createScaledBitmap(bitmap, finalWidth, finalHeight, true)
        } else {
            bitmap
        }

        if (scaledBitmap != bitmap) {
            bitmap.recycle()
        }

        return scaledBitmap
    }

}