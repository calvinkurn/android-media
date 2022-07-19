package com.tokopedia.media.editor.data.repository

import com.yalantis.ucrop.view.CropImageView
import javax.inject.Inject

interface RotateFilterRepository {
    fun rotate(cropImageView: CropImageView, degree: Float)
}

class RotateFilterRepositoryImpl @Inject constructor(): RotateFilterRepository {
    var previousDegree = 0f
    override fun rotate(cropImageView: CropImageView, degree: Float) {
        cropImageView.cancelAllAnimations()

        cropImageView.postRotate(degree - previousDegree)
        previousDegree = degree

        if (cropImageView.minScale > 0) {
            cropImageView.zoomOutImage(cropImageView.minScale + 0.01f)
        }
        cropImageView.setImageToWrapCropBounds(false)
    }
}