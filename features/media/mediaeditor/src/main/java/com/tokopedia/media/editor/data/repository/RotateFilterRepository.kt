package com.tokopedia.media.editor.data.repository

import android.graphics.RectF
import com.tokopedia.media.editor.ui.widget.EditorDetailPreviewWidget
import com.tokopedia.media.editor.ui.component.RotateToolUiComponent
import com.yalantis.ucrop.view.CropImageView
import com.yalantis.ucrop.view.OverlayView
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

    private var isMirrorY = false

    // used by rotated via slider & be used as source of truth anchor of zooming 
    override var initialScale = 0f
        set(value) {
            field = value
            if (latestZoomPoint == 0f) latestZoomPoint = value
        }
    private var latestZoomPoint = 0f

    private var originalTargetWidth: RectF = RectF()

    override fun rotate(
        editorDetailPreview: EditorDetailPreviewWidget?,
        degree: Float,
        isRotateRatio: Boolean,
        imageRatio: Pair<Float, Float>?,
        isPreviousState: Boolean
    ) {
        editorDetailPreview?.let { previewWidget ->
            val cropImageView = previewWidget.cropImageView
            cropImageView.cancelAllAnimations()

            val normalizeDegree = degree * previewWidget.scaleNormalizeValue

            if (initialScale == 0f) initialScale = cropImageView.currentScale
            if (originalTargetWidth.width() == 0f) {
                originalTargetWidth.set(previewWidget.overlayView.cropViewRect)
            }

            // if set rotate is triggered by implemented previous state, then ignore all set
            if (isPreviousState) {
                implementPreviousRotateState(cropImageView, normalizeDegree, isRotateRatio)
                return
            }

            latestZoomPoint = cropImageView.currentScale

            // rotate logic when rotation is triggered by rotate button instead on slider
            if (isRotateRatio && !isPreviousState) {
                rotateWithRatio(
                    previewWidget.overlayView,
                    cropImageView, normalizeDegree, imageRatio
                )
            } else {
                rotateWithoutRotation(
                    cropImageView, normalizeDegree, degree
                )
            }

            cropImageView.setImageToWrapCropBounds(false)
        }
    }

    override fun mirror(editorDetailPreview: EditorDetailPreviewWidget?) {
        editorDetailPreview?.let {
            val originalDegree = it.cropImageView.currentAngle
            it.cropImageView.postRotate(-originalDegree * 2)

            if (!isMirrorY) {
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

    private fun implementPreviousRotateState(
        cropImageView: CropImageView,
        normalizeDegree: Float,
        isRotateRatio: Boolean
    ) {
        cropImageView.postRotate(normalizeDegree)
        isMirrorY = isRotateRatio
    }

    private fun rotateWithRatio(
        cropOverlay: OverlayView,
        cropImageView: CropImageView,
        normalizeDegree: Float,
        imageRatio: Pair<Float, Float>?
    ) {
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
                rotatedRatioZoom = if (newTargetWidth == originalTargetWidth) {
                    initialScale
                } else {
                    (newTargetWidth.height() / originalTargetWidth.width()) * initialScale
                }
            }
            newScale = rotatedRatioZoom
        }

        if (newScale > initialScale) {
            cropImageView.zoomInImage(newScale)
        } else {
            cropImageView.zoomOutImage(newScale)
        }

        /**
         * need delay to provide uCrop process the zoom & rotate
         * it will break zoom & crop position after several rotate crop state if process is not delayed
         */
        Thread.sleep(CROP_VIEW_ZOOM_DELAY)

        initialScale = cropImageView.currentScale
        isRatioRotated = !isRatioRotated
        isMirrorY = !isMirrorY
        rotateNumber++
    }

    private fun rotateWithoutRotation(
        cropImageView: CropImageView,
        normalizeDegree: Float,
        degree: Float
    ) {
        val rotateDegree = normalizeDegree - previousDegree

        val absPrev = abs(previousDegree)
        val absNormalize = abs(normalizeDegree)
        var zoomPointDiff = 0f
        if (absPrev > absNormalize) {
            zoomPointDiff = abs((latestZoomPoint - initialScale) / normalizeDegree)
        }

        cropImageView.postRotate(rotateDegree)

        previousDegree = normalizeDegree
        sliderValue = degree

        cropImageView.zoomOutImage(cropImageView.currentScale - zoomPointDiff)
    }

    companion object {
        private const val CROP_VIEW_ZOOM_DELAY = 500L
    }
}