package com.tokopedia.media.editor.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.media.editor.ui.uimodel.*
import com.tokopedia.media.editor.utils.*
import javax.inject.Inject

interface BitmapCreationRepository {
    fun createBitmap(bitmapCreationModel: BitmapCreationModel): Bitmap?
    fun isBitmapOverflow(width: Int, height: Int): Boolean
    fun getProcessedBitmap(processedBitmapData: ProcessedBitmapModel): Bitmap?
}

class BitmapCreationRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : BitmapCreationRepository {
    override fun isBitmapOverflow(width: Int, height: Int): Boolean {
        val memoryUsage = width * height * BITMAP_PIXEL_SIZE
        val sourceSize = Pair(width, height)

        // check memory limit when create new bitmap
        return if (context.checkMemoryOverflow(memoryUsage)) {
             context.showMemoryLimitToast(sourceSize)
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
                (offsetX + (imageWidth / 2f)),
                (offsetY + (imageHeight / 2f))
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

            if (scaleX == -1f) {
                normalizeX = rotatedBitmap.width - (offsetX + imageWidth)
            }

            // used only on ucrop result re-cropped for get mirror effect
            if (scaleY == -1f && isNormalizeY) {
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
                position?.let { pos -> // first => xPos || second => yPos
                    try {
                        return if (matrix != null && filter != null) {
                            Bitmap.createBitmap(bitmap, pos.first, pos.second, width, height, matrix, filter)
                        } else {
                            Bitmap.createBitmap(bitmap, pos.first, pos.second, width, height)
                        }
                    } catch (e: Exception) {
                        val sourceSize = Pair(bitmap.width, bitmap.height)
                        val targetSize = Pair(width, height)
                        val targetCor = Pair(pos.first, pos.second)
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

    private

    companion object {
        private val BITMAP_PIXEL_SIZE = 4
    }
}
