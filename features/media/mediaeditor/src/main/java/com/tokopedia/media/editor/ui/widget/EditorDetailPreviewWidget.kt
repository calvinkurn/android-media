package com.tokopedia.media.editor.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.net.Uri
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import androidx.core.graphics.values
import com.tokopedia.kotlin.extensions.view.toBitmap
import com.tokopedia.media.editor.ui.uimodel.EditorCropRotateUiModel
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel
import com.tokopedia.media.editor.utils.getUCropTempResultPath
import com.yalantis.ucrop.callback.BitmapCropCallback
import com.yalantis.ucrop.view.TransformImageView
import com.yalantis.ucrop.view.UCropView
import com.tokopedia.unifyprinciples.R as principleR

class EditorDetailPreviewWidget(context: Context, attributeSet: AttributeSet) :
    UCropView(context, attributeSet) {

    val scaleNormalizeValue get() = cropImageView.scaleX * cropImageView.scaleY

    fun initializeRotate(uriSource: Uri, listener: Listener, data: EditorDetailUiModel) {
        val resultDestination = getUCropTempResultPath()
        cropImageView.setImageUri(uriSource, resultDestination)
        disabledTouchEvent()
        initListener(listener)
    }

    fun initializeCrop(uriSource: Uri, listener: Listener) {
        val resultDestination = getUCropTempResultPath()
        cropImageView.setImageUri(uriSource, resultDestination)
        disableRotate()
        initListener(listener)
    }

    fun setOverlayRotate() {
        overlayView.apply {
            setDimmedColor(
                ContextCompat.getColor(
                    context,
                    principleR.color.Unify_NN50
                )
            )

            setCropGridColor(Color.TRANSPARENT)
        }
    }

    // conditional crop function for rotate feature
    fun cropRotate(
        finalRotationDegree: Float,
        sliderValue: Float,
        rotateNumber: Int,
        initialRotateNumber: Int,
        data: EditorDetailUiModel,
        onCropFinish: (cropResult: Bitmap) -> Unit,
    ) {
        val bitmap = cropImageView.drawable.toBitmap()

        val totalRotateNumber = rotateNumber + initialRotateNumber

        val matrixImage = cropImageView.imageMatrix?.values()
        val translateX = matrixImage?.get(2) ?: 0f
        val translateY = matrixImage?.get(5) ?: 0f

        val imageScale = cropImageView.currentScale

        // if previous value is true then use it, otherwise use current editor state for indicator
        val isRotate =
            if (data.cropRotateValue.isRotate) true else data.isToolRotate()
        val isCrop =
            if (data.cropRotateValue.isCrop) true else data.isToolCrop()

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
                            offsetX,
                            offsetY,
                            imageWidth, imageHeight,
                            finalRotationDegree = finalRotationDegree,
                            sliderValue = sliderValue,
                            rotateNumber = totalRotateNumber,
                            data = data,
                            translateX,
                            translateY,
                            imageScale,
                            isRotate = isRotate,
                            isCrop = isCrop,
                            scaleX,
                            scaleY,
                            isNormalizeY = true
                        )
                    )
                }

                override fun onCropFailure(t: Throwable) {}
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
        scaleY: Float,
        isNormalizeY: Boolean = false
    ): Bitmap {
        val originalWidth = originalBitmap.width
        val originalHeight = originalBitmap.height

        val matrix = Matrix()

        matrix.preScale(scaleX, scaleY)
        matrix.postRotate(
            finalRotationDegree,
            (offsetX + (imageWidth / 2f)),
            (offsetY + (imageHeight / 2f))
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
            cropRatio = data?.cropRotateValue?.cropRatio ?: Pair(0, 0)
        )

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