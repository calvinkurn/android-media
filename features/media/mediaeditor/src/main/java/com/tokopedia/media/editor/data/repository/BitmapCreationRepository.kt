package com.tokopedia.media.editor.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.media.editor.ui.uimodel.*
import com.tokopedia.media.editor.utils.*
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import javax.inject.Inject
import kotlin.math.abs

interface BitmapCreationRepository {
    fun createBitmap(bitmapCreationModel: BitmapCreationModel): Bitmap?
    fun isBitmapOverflow(width: Int, height: Int): Boolean
    fun getProcessedBitmap(processedBitmapData: ProcessedBitmapModel): Bitmap?
}

class BitmapCreationRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : BitmapCreationRepository {
    private val refFirebase: FirebaseRemoteConfigImpl = FirebaseRemoteConfigImpl(context)

    override fun isBitmapOverflow(width: Int, height: Int): Boolean {
        var multiplier = DEFAULT_MULTIPLIER
        try {
            multiplier = refFirebase.getString(REMOTE_CONFIG_MULTIPLIER_KEY).toFloat()
        } catch (_: Exception) {}

        val memoryUsage = (width * height * BITMAP_PIXEL_SIZE) * multiplier
        val sourceSize = Pair(width, height)

        // check memory limit when create new bitmap
        return if (context.checkMemoryOverflow(memoryUsage.toInt())) {
             context.showMemoryLimitToast(sourceSize, multiplier = multiplier)
            true
        } else {
            false
        }
    }

    override fun createBitmap(bitmapCreationModel: BitmapCreationModel): Bitmap? {
        return when(bitmapCreationModel.modelType) {
            BitmapCreation.CREATION_SCALED -> mediaCreateScaledBitmap(bitmapCreationModel)
            BitmapCreation.CREATION_EMPTY -> mediaCreateEmptyBitmap(bitmapCreationModel)
            BitmapCreation.CREATION_CROP -> mediaCreateCropBitmap(bitmapCreationModel)
            else -> null
        }
    }

    override fun getProcessedBitmap(processedBitmapData: ProcessedBitmapModel): Bitmap? {
        processedBitmapData.apply {
            val originalWidth = originalBitmap.width
            val originalHeight = originalBitmap.height
            val matrix = Matrix()

            matrix.preScale(scaleX, scaleY)
            matrix.postRotate(
                finalRotationDegree,
                (offsetX + (imageWidth / HALF_DIVIDER)),
                (offsetY + (imageHeight / HALF_DIVIDER))
            )

            val rotatedBitmap = mediaCreateCropBitmap(
                BitmapCreation.cropBitmap(
                    originalBitmap,
                    0,
                    0,
                    originalWidth,
                    originalHeight,
                    matrix,
                    true
                )
            ) ?: return null

            var normalizeX = offsetX
            var normalizeY = offsetY

            if (scaleX == SCALE_MIRROR_VALUE) {
                normalizeX = rotatedBitmap.width - (offsetX + imageWidth)
            }

            // used only on ucrop result re-cropped for get mirror effect
            if (scaleY == SCALE_MIRROR_VALUE && isNormalizeY) {
                normalizeY = rotatedBitmap.height - (offsetY + imageHeight)
            }

            // set crop area on data that will be pass to landing pass for state
            val imageRatio = data?.let {
                if (it.cropRotateValue.cropRatio == EditorCropRotateUiModel.EMPTY_RATIO) {
                    Pair(imageWidth, imageHeight)
                } else {
                    it.cropRotateValue.cropRatio
                }
            }
            data?.cropRotateValue = EditorCropRotateUiModel(
                normalizeX,
                normalizeY,
                imageWidth,
                imageHeight,
                imageScale,
                translateX,
                translateY,
                scaleX,
                scaleY,
                sliderValue,
                rotateNumber,
                isRotate = isRotate,
                isCrop = isCrop,
                croppedSourceWidth = originalWidth,
                cropRatio = imageRatio ?: EditorCropRotateUiModel.EMPTY_RATIO
            )

            return mediaCreateCropBitmap(
                BitmapCreation.cropBitmap(
                    rotatedBitmap,
                    normalizeX,
                    normalizeY,
                    imageWidth,
                    imageHeight
                )
            )
        }
    }

    private fun mediaCreateEmptyBitmap(bitmapCreationModel: BitmapCreationModel): Bitmap? {
        if (isMemoryOverflow(bitmapCreationModel)) return null

        bitmapCreationModel.apply {
            try {
                return config?.let { Bitmap.createBitmap(width, height, it) }
            } catch (e: Exception) {
                val targetSize = Pair(width, height)
                newRelicLog(
                    mapOf(
                        FAILED_CREATE_BITMAP to "{$targetSize} /n ${e.message}"
                    )
                )
            }
        }
        return null
    }

    private fun mediaCreateCropBitmap(bitmapCreationModel: BitmapCreationModel): Bitmap? {
        if (isMemoryOverflow(bitmapCreationModel)) return null

        bitmapCreationModel.apply {
            source?.let { bitmap ->
                position?.let { (x, y) ->
                    try {
                        return validateCroppedSize(bitmap, width, height, x, y, this)
                    } catch (e: Exception) {
                        val sourceSize = Pair(bitmap.width, bitmap.height)
                        val targetSize = Pair(width, height)
                        val targetCor = Pair(x, y)
                        newRelicLog(
                            mapOf(
                                FAILED_CREATE_BITMAP to "{$sourceSize} | {$targetSize} | {$targetCor} /n ${e.message}"
                            )
                        )
                    }
                }
            }
        }

        return null
    }

    private fun mediaCreateScaledBitmap(bitmapCreationModel: BitmapCreationModel): Bitmap? {
        if (isMemoryOverflow(bitmapCreationModel)) return null

        bitmapCreationModel.apply {
            source?.let { bitmap ->
                filter?.let { filter ->
                    try {
                        return Bitmap.createScaledBitmap(bitmap, width, height, filter)
                    } catch (e: Exception) {
                        val targetSize = Pair(width, height)
                        newRelicLog(
                            mapOf(
                                FAILED_CREATE_BITMAP to "scaled => {$targetSize} /n ${e.message}"
                            )
                        )
                    }
                }
            }
        }

        return null
    }

    // helper function to spread width & height
    private fun isMemoryOverflow(bitmapCreationModel: BitmapCreationModel): Boolean {
        return isBitmapOverflow(bitmapCreationModel.width, bitmapCreationModel.height)
    }

    private fun validateCroppedSize(
        bitmap: Bitmap,
        width: Int,
        height: Int,
        x: Int,
        y: Int,
        bitmapCreationModel: BitmapCreationModel
    ): Bitmap {
        var finalWidth = width
        var finalX = x

        var finalHeight = height
        var finalY = y

        val widthRatio = width/height.toFloat()
        val heightRatio = height/width.toFloat()

        //===========

        // if width target is more than source but still on tolerance size
        // then reduce the width target
        val tempWidth = bitmap.width - finalWidth
        if (tempWidth in BITMAP_SIZE_TOLERANCE_VALUE..0) {
            finalWidth -= abs(tempWidth)
            finalHeight = (finalWidth * heightRatio).toInt()
        }

        // if width target + x pos is more than source but still on tolerance size
        // then reduce the x pos
        val tempX = (bitmap.width - finalWidth) - x
        if (tempX in BITMAP_SIZE_TOLERANCE_VALUE..0) {
            finalX = x - abs(tempX)
        }

        // if height target is more than source but still on tolerance size
        // then reduce the height target
        val tempHeight = bitmap.height - finalHeight
        if (tempHeight in BITMAP_SIZE_TOLERANCE_VALUE..0) {
            finalHeight -= abs(tempHeight)
            finalWidth = (finalHeight * widthRatio).toInt()
        }

        // if height target + y pos is more than source but still on tolerance size
        // then reduce the y pos
        val tempY = (bitmap.height - finalHeight) - y
        if (tempY in BITMAP_SIZE_TOLERANCE_VALUE..0) {
            finalY = y - abs(tempY)
        }

        bitmapCreationModel.apply {
            return if (matrix != null && filter != null) {
                Bitmap.createBitmap(bitmap, finalX, finalY, finalWidth, finalHeight, matrix, filter)
            } else {
                Bitmap.createBitmap(bitmap, finalX, finalY, finalWidth, finalHeight)
            }
        }
    }

    companion object {
        private const val BITMAP_PIXEL_SIZE = 4
        private const val HALF_DIVIDER = 2f
        private const val SCALE_MIRROR_VALUE = -1f
        private const val BITMAP_SIZE_TOLERANCE_VALUE = -3

        private const val REMOTE_CONFIG_MULTIPLIER_KEY = "android_media_editor_memory_multiplier"
        private const val DEFAULT_MULTIPLIER = 1.25f
    }
}
