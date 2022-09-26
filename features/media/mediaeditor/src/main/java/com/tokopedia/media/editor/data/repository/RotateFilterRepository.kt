package com.tokopedia.media.editor.data.repository

import android.graphics.RectF
import com.tokopedia.media.editor.ui.widget.EditorDetailPreviewWidget
import com.tokopedia.media.editor.ui.component.RotateToolUiComponent
import javax.inject.Inject
import kotlin.math.abs

interface RotateFilterRepository {
    fun rotate(
        editorDetailPreview: EditorDetailPreviewWidget?,
        degree: Float,
        isRotateRatio: Boolean,
        imageRatio: Pair<Float, Float>?,
        isPreviousState: Boolean,
    )

    fun mirror(editorDetailPreview: EditorDetailPreviewWidget?)
    fun getFinalRotationDegree(): Float

    var previousDegree: Float
    var rotateNumber: Int
    var sliderValue: Float
    var initialScale: Float
}

class RotateFilterRepositoryImpl @Inject constructor() : RotateFilterRepository {
    override var previousDegree = 0f
    override var sliderValue = 0f
    override var rotateNumber = 0

    private var isRatioRotated = false

    // used by rotated via rotate button
    private var initialRatioZoom = 0f
    private var rotatedRatioZoom = 0f

    // used by rotated via slider & be used as source of truth anchor of zooming 
    override var initialScale = 0f
    private var latestZoomPoint = 0f

    private var originalTargetWidth: RectF = RectF()

    override fun rotate(
        editorDetailPreview: EditorDetailPreviewWidget?,
        degree: Float,
        isRotateRatio: Boolean,
        imageRatio: Pair<Float, Float>?,
        isPreviousState: Boolean
    ) {
        if (editorDetailPreview == null) return

        val cropImageView = editorDetailPreview.cropImageView
        cropImageView.cancelAllAnimations()

        val normalizeDegree = degree * editorDetailPreview.scaleNormalizeValue

        if(initialScale == 0f) initialScale = cropImageView.currentScale
        if(originalTargetWidth.width() == 0f) {
            originalTargetWidth.set(editorDetailPreview.overlayView.cropViewRect)
        }

        // rotate logic when rotation is triggered by rotate button instead on slider
        if (isRotateRatio) {
            val cropOverlay = editorDetailPreview.overlayView

            cropImageView.postRotate(normalizeDegree)

            // isRatioRotated = false mean initial ratio going to rotate 90 degree
            var newScale = initialRatioZoom
            if (isRatioRotated) {
                cropOverlay.setTargetAspectRatio(imageRatio?.first ?: 1f)
            } else {
                if (initialRatioZoom == 0f) initialRatioZoom = cropImageView.currentScale
                cropOverlay.setTargetAspectRatio(imageRatio?.second ?: 1f)

                if (rotatedRatioZoom == 0f) {
                    val newTargetWidth = cropOverlay.cropViewRect
                    rotatedRatioZoom = (newTargetWidth.height() / originalTargetWidth.width()) * initialScale
                }
                newScale = rotatedRatioZoom
            }

            if(newScale > initialScale){
                cropImageView.zoomInImage(newScale)
            } else {
                cropImageView.zoomOutImage(newScale)
            }

            initialScale = cropImageView.currentScale
            if (!isPreviousState) isRatioRotated = !isRatioRotated
            rotateNumber++
        } else {
            val rotateDegree = normalizeDegree - previousDegree

            val absPrev = abs(previousDegree)
            val absNormalize = abs(normalizeDegree)
            var zoomPointDiff = 0f
            if(absPrev > absNormalize){
                zoomPointDiff = abs((latestZoomPoint - initialScale) / normalizeDegree)
            }

            cropImageView.postRotate(rotateDegree)

            previousDegree = normalizeDegree
            sliderValue = degree

            cropImageView.zoomOutImage(cropImageView.currentScale - zoomPointDiff)
        }

        cropImageView.setImageToWrapCropBounds(false)
        latestZoomPoint = cropImageView.currentScale
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