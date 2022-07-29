package com.tokopedia.media.editor.data.repository

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

    override fun rotate(editorDetailPreview: EditorDetailPreviewImage?, degree: Float, isRotateRatio: Boolean) {
        if(editorDetailPreview == null) return

        val cropImageView = editorDetailPreview.cropImageView
        cropImageView.cancelAllAnimations()

        val absoluteDegree = abs(degree)

        if(isRotateRatio){
            val cropOverlay = editorDetailPreview.overlayView
            val originalWidth = cropImageView.drawable?.intrinsicWidth ?: 0
            val originalHeight = cropImageView.drawable?.intrinsicHeight ?: 0

            cropImageView.postRotate(absoluteDegree)

            if (isRatioRotated) {
                cropOverlay.setTargetAspectRatio(originalWidth/originalHeight.toFloat())
            } else {
                cropOverlay.setTargetAspectRatio(originalHeight/originalWidth.toFloat())
            }

            isRatioRotated = !isRatioRotated
            rotateNumber++
        } else {
            cropImageView.postRotate(absoluteDegree - previousDegree)

            previousDegree = absoluteDegree
        }

        if (cropImageView.minScale > 0) {
            cropImageView.zoomOutImage(cropImageView.minScale + 0.01f)
        }

        cropImageView.setImageToWrapCropBounds(false)
    }

    override fun mirror(editorDetailPreview: EditorDetailPreviewImage?) {
        editorDetailPreview?.mirrorImage(isMirrorXAxis = true, isMirrorYAxis = false)
    }
}