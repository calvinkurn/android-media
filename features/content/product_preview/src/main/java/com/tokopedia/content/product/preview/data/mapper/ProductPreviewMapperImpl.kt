package com.tokopedia.content.product.preview.data.mapper

import com.tokopedia.content.common.report_content.model.ContentMenuIdentifier
import com.tokopedia.content.common.report_content.model.ContentMenuItem
import com.tokopedia.content.product.preview.R
import com.tokopedia.content.product.preview.data.MediaReviewResponse
import com.tokopedia.content.product.preview.view.uimodel.AuthorUiModel
import com.tokopedia.content.product.preview.view.uimodel.DescriptionUiModel
import com.tokopedia.content.product.preview.view.uimodel.LikeUiState
import com.tokopedia.content.product.preview.view.uimodel.ReviewUiModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import com.tokopedia.content.common.R as contentcommonR

/**
 * @author by astidhiyaa on 06/12/23
 */
class ProductPreviewMapperImpl @Inject constructor(private val userSession: UserSessionInterface) :
    ProductPreviewMapper {
    override fun mapReviews(response: MediaReviewResponse): List<ReviewUiModel> {
        return response.data.review.map{
            ReviewUiModel(
                reviewId = it.feedbackId,
                medias = emptyList(), //TODO: map and sew it later,
                menus = buildMenu(it),
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

    private fun buildMenu(detail: MediaReviewResponse.ReviewDetail) =
        buildList {
            add(
                ContentMenuItem(
                    iconUnify = IconUnify.VISIBILITY,
                    name = R.string.product_prev_menu_opt_watch,
                    type = ContentMenuIdentifier.WatchMode
                )
            )
            if (detail.isReportable && !isOwner(detail.user))
                add(
                    ContentMenuItem(
                        iconUnify = IconUnify.WARNING,
                        name = contentcommonR.string.content_common_menu_report,
                        type = ContentMenuIdentifier.Report
                    )
                )
        }

    private fun isOwner(author: MediaReviewResponse.ReviewerUserInfo): Boolean =
        author.userId == userSession.userId

}
