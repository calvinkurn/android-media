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
import com.tokopedia.media.editor.ui.uimodel.EditorCropRotateUiModel.Companion.EMPTY_RATIO
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel
import com.tokopedia.media.editor.ui.uimodel.ProcessedBitmapModel
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
        onCropFinish: (cropData: ProcessedBitmapModel) -> Unit,
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
                        ProcessedBitmapModel(
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
