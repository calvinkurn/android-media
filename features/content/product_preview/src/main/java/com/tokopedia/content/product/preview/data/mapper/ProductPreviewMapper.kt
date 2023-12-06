package com.tokopedia.content.product.preview.data.mapper

import com.tokopedia.content.product.preview.data.MediaReviewResponse
import com.tokopedia.content.product.preview.view.uimodel.AuthorUiModel
import com.tokopedia.content.product.preview.view.uimodel.DescriptionUiModel
import com.tokopedia.content.product.preview.view.uimodel.LikeUiState
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
                likeState = LikeUiState(
                    count = it.totalLike,
                    state = if (it.isLiked) LikeUiState.LikeStatus.Like else LikeUiState.LikeStatus.Dislike
                ),
                author = AuthorUiModel(
                    name = it.user.fullName,
                    type = AuthorUiModel.ReviewerType.Unknown, //TODO: map it later
                    id = it.user.userId,
                    avatarUrl = it.user.image,
                    appLink = it.user.url,
                ),
                description = DescriptionUiModel(
                    stars = it.rating,
                    productType = it.variantName,
                    timestamp = it.createTimestamp,
                    description = it.review,
                )
            )
        }
    }
}
