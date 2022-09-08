package com.tokopedia.review.feature.createreputation.presentation.mapper

import com.tokopedia.review.feature.createreputation.model.BaseImageReviewUiModel
import com.tokopedia.review.feature.createreputation.model.DefaultImageReviewUiModel
import com.tokopedia.review.feature.createreputation.model.ImageReviewUiModel

object CreateReviewImageMapper {

    fun getImageUrl(imageReviewUiModel: BaseImageReviewUiModel): String {
        return if ((imageReviewUiModel as? ImageReviewUiModel)?.fullImageUrl?.isNotBlank() == true) {
            (imageReviewUiModel as? ImageReviewUiModel)?.fullImageUrl ?: ""
        } else {
            (imageReviewUiModel as? ImageReviewUiModel)?.imageUrl ?: ""
        }
    }

    fun removeImageFromList(image: BaseImageReviewUiModel, originalImages: MutableList<String>): MutableList<String> {
        val imageToRemove = image as? ImageReviewUiModel
        imageToRemove?.let {
            return originalImages.filter { originalImage ->
                (originalImage != it.imageUrl && originalImage != it.fullImageUrl)
            }.toMutableList()
        }
        return originalImages
    }

    fun addDefaultModelIfLessThan(imageData: MutableList<Any>, min: Int): MutableList<Any> {
        if (imageData.size < min && !imageData.contains(DefaultImageReviewUiModel())) {
            imageData.add(DefaultImageReviewUiModel())
        }
        return imageData
    }
}