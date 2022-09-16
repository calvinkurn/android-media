package com.tokopedia.media.editor.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.net.Uri
import android.util.AttributeSet
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.values
import com.tokopedia.kotlin.extensions.view.toBitmap
import com.tokopedia.media.editor.ui.uimodel.EditorCropRotateModel
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel
import com.tokopedia.media.editor.utils.getUCropTempResultPath
import com.yalantis.ucrop.callback.BitmapCropCallback
import com.yalantis.ucrop.view.TransformImageView
import com.yalantis.ucrop.view.UCropView
import kotlin.math.abs
import com.tokopedia.unifyprinciples.R as principleR

class EditorDetailPreviewWidget(context: Context, attributeSet: AttributeSet) :
    UCropView(context, attributeSet) {

    val scaleNormalizeValue get() = cropImageView.scaleX * cropImageView.scaleY

    fun initializeRotate(uriSource: Uri, listener: Listener, data: EditorDetailUiModel) {
        val resultDestination = getUCropTempResultPath()

        cropImageView.setImageUri(uriSource, resultDestination)

        overlayView.apply {
            setDimmedColor(
                ContextCompat.getColor(
                    context,
                    principleR.color.Unify_NN50
                )
            )

            setCropGridColor(Color.TRANSPARENT)
        }

        disabledTouchEvent()
        initListener(listener)
    }

    fun initializeCrop(uriSource: Uri, listener: Listener) {
        val resultDestination = getUCropTempResultPath()
        cropImageView.setImageUri(uriSource, resultDestination)
        disableRotate()
        initListener(listener)
    }

    fun getBitmap(): Bitmap {
        return cropImageView.drawable.toBitmap()
    }

    // conditional crop function for rotate feature
    fun cropRotate(
        finalRotationDegree: Float,
        sliderValue: Float,
        rotateNumber: Int,
        data: EditorDetailUiModel,
        onCropFinish: (cropResult: Bitmap) -> Unit,
    ) {
        val bitmap = cropImageView.drawable.toBitmap()
        val cropViewScale = getScale()
        val cropViewScaleX = cropViewScale.first
        val cropViewScaleY = cropViewScale.second

        val matrixImage = cropImageView.imageMatrix?.values()
        val translateX = matrixImage?.get(2) ?: 0f
        val translateY = matrixImage?.get(5) ?: 0f

        val imageScale = cropImageView.currentScale

        // if previous value is true then use it, otherwise use current editor state for indicator
        val isRotate =
            if (data.cropRotateValue.isRotate) true else data.isToolRotate() ?: false
        val isCrop =
            if (data.cropRotateValue.isCrop) true else data.isToolCrop() ?: false

        // if rotated image is same with original ratio without overflow, ucrop will skip it
        // need to manually crop & save
        if (cropImageView.currentAngle % 90f == 0f && rotateNumber != 0 && isRotate) {
            val cropRotateData = data.cropRotateValue
            val scalingSize = bitmap.width.toFloat() / cropRotateData.croppedSourceWidth

            val offsetX = (cropRotateData.offsetX * scalingSize).toInt()
            val imageWidth = if(cropRotateData.imageWidth != 0) {
                (cropRotateData.imageWidth * scalingSize).toInt()
            } else bitmap.width

            val offsetY = (cropRotateData.offsetY * scalingSize).toInt()
            val imageHeight = if(cropRotateData.imageHeight != 0) {
                (cropRotateData.imageHeight * scalingSize).toInt()
            } else bitmap.height

            val matrix = Matrix()
            matrix.preScale(
                cropViewScaleX,
                cropViewScaleY
            )

            matrix.postRotate(abs(finalRotationDegree))

            val isRatioChange = rotateNumber % 2 != 0
            var finalWidth = imageWidth
            var finalHeight = imageHeight
            var finalOffsetX = offsetX
            var finalOffsetY = offsetY

            // need to swap the detail if image is rotated
            if(isRatioChange){
                finalWidth = imageHeight
                finalHeight = imageWidth
                finalOffsetX = offsetY
                finalOffsetY = offsetX
            }

            // set crop area on data that will be pass to landing pass for state
            data.cropRotateValue = EditorCropRotateModel(
                finalOffsetX,
                finalOffsetY,
                finalWidth,
                finalHeight,
                imageScale,
                translateX,
                translateY,
                cropViewScaleX * scalingSize,
                cropViewScaleY * scalingSize,
                sliderValue,
                rotateNumber,
                isRotate = isRotate,
                isCrop = isCrop,
                croppedSourceWidth = bitmap.width
            )

            onCropFinish(
                Bitmap.createBitmap(
                    bitmap,
                    offsetX,
                    offsetY,
                    imageWidth,
                    imageHeight,
                    matrix,
                    true
                )
            )
            return
        }

        // if rotated image is overflow from the original ratio then we can use ucrop crop feature
        cropImageView.cropAndSaveImage(
            Bitmap.CompressFormat.JPEG,
            100,
            object : BitmapCropCallback {
                override fun onBitmapCropped(
                    resultUri: Uri,
                    offsetX: Int,
                    offsetY: Int,
                    imageWidth: Int,
                    imageHeight: Int
                ) {
                    val scale = getScale()
                    val scaleX = scale.first
                    val scaleY = scale.second

                    onCropFinish(
                        getProcessedBitmap(
                            bitmap,
                            offsetX, offsetY,
                            imageWidth, imageHeight,
                            finalRotationDegree = finalRotationDegree,
                            sliderValue = sliderValue,
                            rotateNumber = rotateNumber,
                            data = data,
                            translateX,
                            translateY,
                            imageScale,
                            isRotate = isRotate,
                            isCrop = isCrop,
                            scaleX,
                            scaleY
                        )
                    )
                }

                override fun onCropFailure(t: Throwable) {
                    Toast.makeText(context, "Crop Error - ${t.message}", Toast.LENGTH_LONG).show()
                }
            }
        )
    }

    // crop function that support mirroring feature
    fun getProcessedBitmap(
        originalBitmap: Bitmap,
        offsetX: Int,
        offsetY: Int,
        imageWidth: Int,
        imageHeight: Int,
        finalRotationDegree: Float,
        sliderValue: Float,
        rotateNumber: Int,
        data: EditorDetailUiModel?,
        translateX: Float,
        translateY: Float,
        imageScale: Float,
        isRotate: Boolean,
        isCrop: Boolean,
        scaleX: Float,
        scaleY: Float
    ): Bitmap {
        var originalWidth = originalBitmap.width
        var originalHeight = originalBitmap.height

        val matrix = Matrix()

        matrix.preScale(scaleX, scaleY)
        matrix.postRotate(
            finalRotationDegree,
            (originalWidth / 2).toFloat(),
            (originalHeight / 2).toFloat()
        )

        val rotatedBitmap = Bitmap.createBitmap(
            originalBitmap,
            0,
            0,
            originalWidth,
            originalHeight,
            matrix,
            true
        )

        // set crop area on data that will be pass to landing pass for state
        data?.cropRotateValue = EditorCropRotateModel(
            offsetX,
            offsetY,
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
            croppedSourceWidth = originalWidth
        )

        val normalizeX =
            if (scaleX == -1f) rotatedBitmap.width - (offsetX + imageWidth) else offsetX
        val normalizeY =
            if (scaleY == -1f) rotatedBitmap.height - (offsetY + imageHeight) else offsetY

        return Bitmap.createBitmap(rotatedBitmap, normalizeX, normalizeY, imageWidth, imageHeight)
    }

    @SuppressLint("ClickableViewAccessibility")
    /**
     * Component will lose the ability to interact with user input via touch
     * if you want to enable the interaction again please re-init the component
     */
    private fun disabledTouchEvent() {
        cropImageView.setOnTouchListener { _, _ ->
            true
        }
    }

    private fun disableRotate() {
        cropImageView.isRotateEnabled = false
    }

    private fun getScale(): Pair<Float, Float> {
        return Pair(cropImageView.scaleX, cropImageView.scaleY)
    }

    private fun initListener(listener: Listener) {
        cropImageView.setTransformImageListener(object : TransformImageView.TransformImageListener {
            override fun onLoadFailure(e: java.lang.Exception) {}
            override fun onRotate(currentAngle: Float) {}
            override fun onScale(currentScale: Float) {}

            override fun onLoadComplete() {
                listener.onLoadComplete()
            }
        })
    }

    interface Listener {
        fun onLoadComplete()
    }
}