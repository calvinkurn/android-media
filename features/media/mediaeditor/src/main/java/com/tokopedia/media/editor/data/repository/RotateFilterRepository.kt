package com.tokopedia.media.editor.data.repository

import com.tokopedia.media.editor.ui.widget.EditorDetailPreviewWidget
import com.tokopedia.media.editor.ui.component.RotateToolUiComponent
import com.tokopedia.unifycomponents.HtmlLinkHelper
import javax.inject.Inject

interface RotateFilterRepository {
    fun rotate(
        editorDetailPreview: EditorDetailPreviewWidget?,
        degree: Float,
        isRotateRatio: Boolean,
        imageRatio: Pair<Float, Float>?
    )

    fun mirror(editorDetailPreview: EditorDetailPreviewWidget?)
    fun getFinalRotationDegree(): Float

    var previousDegree: Float
    var rotateNumber: Int
    var sliderValue: Float
}

class RotateFilterRepositoryImpl @Inject constructor() : RotateFilterRepository {
    override var previousDegree = 0f
    override var rotateNumber = 0
    override var sliderValue = 0f

    private var isRatioRotated = false

    private var initialRatioZoom = 0f
    private var rotatedRatioZoom = 0f

    override fun rotate(
        editorDetailPreview: EditorDetailPreviewWidget?,
        degree: Float,
        isRotateRatio: Boolean,
        imageRatio: Pair<Float, Float>?
    ) {
        if (editorDetailPreview == null) return

        val cropImageView = editorDetailPreview.cropImageView
        cropImageView.cancelAllAnimations()

        val normalizeDegree = degree * editorDetailPreview.scaleNormalizeValue

        // rotate logic when rotation is triggered by rotate button instead on slider
        if (isRotateRatio) {
            val cropOverlay = editorDetailPreview.overlayView

            cropImageView.postRotate(normalizeDegree)

            // isRatioRotated = false mean initial ratio going to rotate 90 degree
            if (isRatioRotated) {
                if (rotatedRatioZoom == 0f) rotatedRatioZoom = cropImageView.currentScale
                cropOverlay.setTargetAspectRatio(imageRatio?.first ?: 1f)
                cropImageView.zoomOutImage(initialRatioZoom)
            } else {
                if (initialRatioZoom == 0f) initialRatioZoom = cropImageView.currentScale
                cropOverlay.setTargetAspectRatio(imageRatio?.second ?: 1f)
                cropImageView.zoomOutImage(rotatedRatioZoom)
            }

            isRatioRotated = !isRatioRotated
            rotateNumber++
        } else {
            cropImageView.postRotate(normalizeDegree - previousDegree)

            previousDegree = normalizeDegree
            sliderValue = degree
            cropImageView.zoomOutImage(cropImageView.minScale + 0.01f)
        }

        cropImageView.setImageToWrapCropBounds(false)
    }


    override fun mirror(editorDetailPreview: EditorDetailPreviewWidget?) {
        editorDetailPreview?.let {
            val originalDegree = it.cropImageView.currentAngle
            it.cropImageView.postRotate(-originalDegree * 2)

            if (!isRatioRotated) {
                it.cropImageView.scaleX = -it.cropImageView.scaleX
            } else {
                it.cropImageView.scaleY = -it.cropImageView.scaleY
            }

            previousDegree = -previousDegree
        }
    }

    // get total degree from clicked rotate button & slider value
    override fun getFinalRotationDegree(): Float {
        return ((rotateNumber * RotateToolUiComponent.ROTATE_BTN_DEGREE) + sliderValue)
    }
}