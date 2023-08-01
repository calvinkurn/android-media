package com.tokopedia.media.editor.data.repository

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.media.editor.ui.widget.EditorDetailPreviewWidget
import com.tokopedia.media.editor.ui.component.RotateToolUiComponent
import com.tokopedia.media.editor.utils.showErrorGeneralToaster
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

class RotateFilterRepositoryImpl @Inject constructor(
    @ApplicationContext val context: Context
) : RotateFilterRepository {
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
        try {
            cropImageView.postRotate(normalizeDegree)
        } catch (e: Exception) {
            showErrorGeneralToaster(context, e.message)
            return
        }

        var prevImageWidth: Float
        var prevImageHeight: Float
        cropOverlay.cropViewRect.let {
            prevImageWidth = it.width()
            prevImageHeight = it.height()
        }

        val currentScale = cropImageView.currentScale
        var newScale = initialRatioZoom

        // isRatioRotated = false mean initial ratio going to rotate 90 degree
        if (isRatioRotated) {
            cropOverlay.setTargetAspectRatio(imageRatio?.first ?: 1f)
        } else {
            cropOverlay.setTargetAspectRatio(imageRatio?.second ?: 1f)

            if (rotatedRatioZoom == 0f) {
                val newTargetWidth = cropOverlay.cropViewRect

                // cannot compare directly, need tolerance value since Ucrop overlay size can return diff rect value after each init
                // cropOverlay.setTargetAspectRatio => will re-init the overlay size
                val diffWidth = abs(newTargetWidth.width() - prevImageWidth)
                val diffHeight = abs(newTargetWidth.height() - prevImageHeight)

                rotatedRatioZoom =
                    if (diffWidth <= TOLERANCE_SIZE_VALUE && diffHeight <= TOLERANCE_SIZE_VALUE) {
                        initialScale
                    } else {
                        (newTargetWidth.height() / prevImageWidth) * currentScale
                    }
            }

            newScale = rotatedRatioZoom

            if (initialRatioZoom == 0f) {
                initialRatioZoom = currentScale
            }
        }

        if (newScale > currentScale) {
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
        private const val TOLERANCE_SIZE_VALUE = 5 // in pixel
    }
}
