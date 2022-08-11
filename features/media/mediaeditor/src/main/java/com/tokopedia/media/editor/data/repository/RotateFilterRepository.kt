package com.tokopedia.media.editor.data.repository

import android.os.Handler
import com.tokopedia.media.editor.ui.component.EditorDetailPreviewImage
import com.tokopedia.media.editor.ui.component.RotateToolUiComponent
import com.yalantis.ucrop.view.CropImageView
import javax.inject.Inject
import kotlin.math.abs

interface RotateFilterRepository {
    fun rotate(editorDetailPreview: EditorDetailPreviewImage?, degree: Float, isRotateRatio: Boolean)
    fun mirror(editorDetailPreview: EditorDetailPreviewImage?)
}

class RotateFilterRepositoryImpl @Inject constructor(): RotateFilterRepository {
    var previousDegree = 0f
    var rotateNumber = 0
    private var isRatioRotated = false
    var sliderValue = 0f

    override fun rotate(editorDetailPreview: EditorDetailPreviewImage?, degree: Float, isRotateRatio: Boolean) {
        if(editorDetailPreview == null) return

        val cropImageView = editorDetailPreview.cropImageView
        cropImageView.cancelAllAnimations()

        val normalizeDegree = degree * editorDetailPreview.scaleNormalizeValue

        // rotate logic when rotation is triggered by rotate button instead on slider
        if(isRotateRatio){
            val cropOverlay = editorDetailPreview.overlayView
            val originalWidth = cropImageView.drawable?.intrinsicWidth ?: 0
            val originalHeight = cropImageView.drawable?.intrinsicHeight ?: 0

            cropImageView.postRotate(normalizeDegree)

            if (isRatioRotated) {
                cropOverlay.setTargetAspectRatio(originalWidth/originalHeight.toFloat())
            } else {
                cropOverlay.setTargetAspectRatio(originalHeight/originalWidth.toFloat())
            }

            isRatioRotated = !isRatioRotated
            rotateNumber++
        } else {
            cropImageView.postRotate(normalizeDegree - previousDegree)

            previousDegree = normalizeDegree
            sliderValue = degree
        }

//        if (cropImageView.minScale > 0) {
//            cropImageView.zoomOutImage(cropImageView.minScale + 0.01f)
//        }

        cropImageView.setImageToWrapCropBounds(false)
    }


    override fun mirror(editorDetailPreview: EditorDetailPreviewImage?) {
        editorDetailPreview?.let {
            val originalDegree = it.cropImageView.currentAngle
            it.cropImageView.postRotate(-originalDegree * 2)

            if(!isRatioRotated){
                it.cropImageView.scaleX = -it.cropImageView.scaleX
            } else {
                it.cropImageView.scaleY = -it.cropImageView.scaleY
            }

            previousDegree = -previousDegree
        }
    }

    // get total degree from clicked rotate button & slider value
    fun getFinalRotationDegree(): Float{
        return ((rotateNumber * RotateToolUiComponent.ROTATE_BTN_DEGREE) + sliderValue)
    }
}