package com.tokopedia.tkpd.tkpdreputation.createreputation.model

data class ImageReviewUiModel (
        val imageUrl:String = "",
        val otherImageCount : Int = DEFAULT_OTHER_COUNT,
        val shouldDisplayOverlay: Boolean = false
):BaseImageReviewUiModel {
    companion object {
        const val DEFAULT_OTHER_COUNT = 1
    }
}