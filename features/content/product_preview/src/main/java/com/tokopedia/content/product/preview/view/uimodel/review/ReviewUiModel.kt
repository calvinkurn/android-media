package com.tokopedia.content.product.preview.view.uimodel.review

data class ReviewUiModel(
    val reviewContent: List<ReviewContentUiModel>,
    val reviewPaging: ReviewPaging
) {
    companion object {
        val Empty
            get() = ReviewUiModel(
                reviewContent = emptyList(),
                reviewPaging = ReviewPaging.Unknown
            )
    }
}
