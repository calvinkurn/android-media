package com.tokopedia.content.product.preview.data.mapper

import com.tokopedia.content.product.preview.data.MediaReviewResponse
import com.tokopedia.content.product.preview.view.uimodel.ReviewUiModel
import javax.inject.Inject

/**
 * @author by astidhiyaa on 06/12/23
 */
class ProductPreviewMapper @Inject constructor() {
    fun map(response: MediaReviewResponse): List<ReviewUiModel> {
        return response.data.detail.reviewDetail.map {
            ReviewUiModel(
                id = it.feedbackId,
                medias = emptyList(), //TODO: map and sew it later,
                menus = emptyList(), //TODO: map data
                likeState = ReviewUiModel.LikeUiState(
                    count = it.totalLike,
                    state = if (it.isLiked) ReviewUiModel.LikeUiState.LikeStatus.Like else ReviewUiModel.LikeUiState.LikeStatus.Dislike
                ),
                author = ReviewUiModel.AuthorUiModel(
                    name = it.user.fullName,
                    type = ReviewUiModel.AuthorUiModel.ReviewerType.Unknown, //TODO: map it later
                    id = it.user.userId
                ),
                description = ReviewUiModel.DescriptionUiModel(
                    stars = it.rating,
                    productType = it.variantName,
                    timestamp = it.createTimestamp,
                    description = it.description,
                )
            )
        }
    }
}
