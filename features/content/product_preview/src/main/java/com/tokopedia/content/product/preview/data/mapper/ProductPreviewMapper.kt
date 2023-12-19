package com.tokopedia.content.product.preview.data.mapper

import com.tokopedia.content.product.preview.data.MediaReviewResponse
import com.tokopedia.content.product.preview.view.uimodel.AuthorUiModel
import com.tokopedia.content.product.preview.view.uimodel.DescriptionUiModel
import com.tokopedia.content.product.preview.view.uimodel.LikeUiState
import com.tokopedia.content.product.preview.view.uimodel.MenuStatus
import com.tokopedia.content.product.preview.view.uimodel.ReviewUiModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by astidhiyaa on 06/12/23
 */
class ProductPreviewMapper @Inject constructor(private val userSession: UserSessionInterface) {
    fun mapReviews(response: MediaReviewResponse): List<ReviewUiModel> {
        return response.data.review.map {
            ReviewUiModel(
                reviewId = it.feedbackId,
                medias = emptyList(), //TODO: map and sew it later,
                menus = MenuStatus(isReportable = it.isReportable && !isOwner(it.user)),
                likeState = LikeUiState(
                    count = it.likeStats.totalLike,
                    state = LikeUiState.LikeStatus.getByValue(it.likeStats.likeStatus),
                ),
                author = AuthorUiModel(
                    name = it.user.fullName,
                    type = it.user.label,
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

    private fun isOwner(author: MediaReviewResponse.ReviewerUserInfo): Boolean =
        author.userId == userSession.userId
}
