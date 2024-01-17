package com.tokopedia.content.product.preview.data.mapper

import com.tokopedia.content.product.preview.data.AddWishlistResponse
import com.tokopedia.content.product.preview.data.GetMiniProductInfoResponse
import com.tokopedia.content.product.preview.data.LikeReviewResponse
import com.tokopedia.content.product.preview.data.MediaReviewResponse
import com.tokopedia.content.product.preview.view.uimodel.AuthorUiModel
import com.tokopedia.content.product.preview.view.uimodel.BottomNavUiModel
import com.tokopedia.content.product.preview.view.uimodel.DescriptionUiModel
import com.tokopedia.content.product.preview.view.uimodel.LikeUiState
import com.tokopedia.content.product.preview.view.uimodel.MenuStatus
import com.tokopedia.content.product.preview.view.uimodel.ReviewUiModel
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.currency.CurrencyFormatUtil
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
                likeState = LikeUiState (
                    count = it.likeStats.totalLike,
                    state = LikeUiState.LikeStatus.getByValue(it.likeStats.likeStatus),
                    withAnimation = false,
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

    fun mapMiniInfo(response: GetMiniProductInfoResponse): BottomNavUiModel =
        BottomNavUiModel(
            title = response.data.product.name,
            price = if (response.data.campaign.isActive) {
                BottomNavUiModel.DiscountedPrice(
                    discountedPrice = CurrencyFormatUtil.convertPriceValueToIdrFormat(
                        price = response.data.campaign.discountedPrice,
                        hasSpace = false
                    ),
                    ogPriceFmt = response.data.product.priceFmt,
                    discountPercentage = "${response.data.campaign.discountPercentage}%",
                )
            } else BottomNavUiModel.NormalPrice(priceFmt = response.data.product.priceFmt),
            stock = response.data.product.stock,
            shop = BottomNavUiModel.Shop(
                id = response.data.shop.id,
                name = response.data.shop.name
            ),
            hasVariant = response.data.hasVariant,
            buttonState = if (response.data.hasVariant) BottomNavUiModel.ButtonState.Active else BottomNavUiModel.ButtonState.getByValue(
                response.data.buttonState
            ) //Variant product always active, to open GVBS.
        )
    fun mapRemindMe(response: AddWishlistResponse): BottomNavUiModel.RemindMeUiModel =
        BottomNavUiModel.RemindMeUiModel(
            isSuccess = response.wishlistAdd.success,
            message = response.wishlistAdd.message
        )
    fun mapLike(response: LikeReviewResponse): LikeUiState = LikeUiState(
        count = response.data.totalLike,
        state = LikeUiState.LikeStatus.getByValue(response.data.likeStatus),
    )
}
