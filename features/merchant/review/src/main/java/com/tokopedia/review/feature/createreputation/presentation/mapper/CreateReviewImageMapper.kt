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
            if (originalImages.contains(it.fullImageUrl)) {
                originalImages.remove(it.fullImageUrl)
            }
        }
        return originalImages
    }

    fun getEditedImages(originalImageUrl: MutableList<String>, originalImages: MutableList<String>, edited: MutableList<Boolean>): List<String> {
        return originalImages.filter {
            originalImageUrl.contains(it)
        }.filterIndexed { index, _ -> !edited[index] }
    }

    fun getImageUrlList(imagePickerResult: MutableList<String>, edited: MutableList<Boolean>, pictureList: List<String>, originalImageUrl: MutableList<String>): MutableList<String> {
        return imagePickerResult.mapIndexed { index, urlOrPath ->
            if (edited[index]) urlOrPath else pictureList.find { it == originalImageUrl[index] }
                    ?: urlOrPath
        }.toMutableList()
    }

    fun addDefaultModelIfLessThanFive(imageData: MutableList<BaseImageReviewUiModel>): MutableList<BaseImageReviewUiModel> {
        if (imageData.size < 5 && !imageData.contains(DefaultImageReviewUiModel())) {
            imageData.add(DefaultImageReviewUiModel())
        }
        return imageData
    }
}