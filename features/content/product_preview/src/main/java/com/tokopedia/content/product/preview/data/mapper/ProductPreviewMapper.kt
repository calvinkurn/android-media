package com.tokopedia.content.product.preview.data.mapper

import android.net.Uri
import com.tokopedia.content.product.preview.data.response.AddWishlistResponse
import com.tokopedia.content.product.preview.data.response.GetMiniProductInfoResponse
import com.tokopedia.content.product.preview.data.response.LikeReviewResponse
import com.tokopedia.content.product.preview.data.response.MediaReviewResponse
import com.tokopedia.content.product.preview.data.response.ReviewByIdsResponse
import com.tokopedia.content.product.preview.view.uimodel.BottomNavUiModel
import com.tokopedia.content.product.preview.view.uimodel.MediaType
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewAuthorUiModel
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewContentUiModel
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewDescriptionUiModel
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewLikeUiState
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewMediaUiModel
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewMenuStatus
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewPaging
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewUiModel
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.currency.CurrencyFormatUtil
import javax.inject.Inject

/**
 * @author by astidhiyaa on 06/12/23
 */
class ProductPreviewMapper @Inject constructor(private val userSession: UserSessionInterface) {

    fun mapReviewsByIds(response: ReviewByIdsResponse): ReviewUiModel {
        val mapped = response.data.review.map {
            ReviewContentUiModel(
                reviewId = it.feedbackId,
                medias = it.videos.mapIndexed { index, videos ->
                    ReviewMediaUiModel(
                        mediaId = videos.attachmentId,
                        type = MediaType.Video,
                        url = videos.url
                    )
                } + it.images.mapIndexed { index, images ->
                    ReviewMediaUiModel(
                        mediaId = images.attachmentId,
                        type = MediaType.Image,
                        url = images.fullSizeUrl
                    )
                },
                menus = ReviewMenuStatus(isReportable = it.isReportable && !isOwner(it.user.userId)),
                likeState = ReviewLikeUiState(
                    count = it.likeStats.totalLike,
                    state = ReviewLikeUiState.ReviewLikeStatus.getByValue(it.likeStats.likeStatus),
                    withAnimation = false
                ),
                author = ReviewAuthorUiModel(
                    name = it.user.fullName,
                    type = it.user.label,
                    id = it.user.userId,
                    avatarUrl = it.user.image,
                    appLink = it.user.url
                ),
                description = ReviewDescriptionUiModel(
                    stars = it.rating,
                    productType = Uri.decode(it.variantName),
                    timestamp = it.createTimestamp,
                    description = Uri.decode(it.review)
                ),
                mediaSelectedPosition = 0,
                isWatchMode = false,
                isScrolling = false,
            )
        }
        return ReviewUiModel(
            reviewPaging = ReviewPaging.Success(0, true),
            reviewContent = mapped
        )
    }

    fun mapReviews(response: MediaReviewResponse, page: Int): ReviewUiModel {
        val mapped = response.data.review.map {
            ReviewContentUiModel(
                reviewId = it.feedbackId,
                medias = it.videos.mapIndexed { index, videos ->
                    ReviewMediaUiModel(
                        mediaId = videos.attachmentId,
                        type = MediaType.Video,
                        url = videos.url,
                        selected = index == 0
                    )
                } + it.images.mapIndexed { index, images ->
                    ReviewMediaUiModel(
                        mediaId = images.attachmentId,
                        type = MediaType.Image,
                        url = images.fullSizeUrl,
                        selected = if (it.videos.isEmpty()) index == 0 else false
                    )
                },
                menus = ReviewMenuStatus(isReportable = it.isReportable && !isOwner(it.user.userId)),
                likeState = ReviewLikeUiState(
                    count = it.likeStats.totalLike,
                    state = ReviewLikeUiState.ReviewLikeStatus.getByValue(it.likeStats.likeStatus),
                    withAnimation = false
                ),
                author = ReviewAuthorUiModel(
                    name = it.user.fullName,
                    type = it.user.label,
                    id = it.user.userId,
                    avatarUrl = it.user.image,
                    appLink = it.user.url
                ),
                description = ReviewDescriptionUiModel(
                    stars = it.rating,
                    productType = Uri.decode(it.variantName),
                    timestamp = it.createTimestamp,
                    description = Uri.decode(it.review)
                ),
                mediaSelectedPosition = 0,
                isWatchMode = false,
                isScrolling = false,
            )
        }
        return ReviewUiModel(
            reviewPaging = ReviewPaging.Success(page, response.data.hasNext),
            reviewContent = mapped
        )
    }

    private fun isOwner(authorId: String): Boolean = authorId == userSession.userId

    fun mapMiniInfo(response: GetMiniProductInfoResponse): BottomNavUiModel {
        val ogPrice = CurrencyFormatUtil.convertPriceValueToIdrFormat(
            price = response.data.product.price,
            hasSpace = false
        )
        return BottomNavUiModel(
            title = response.data.product.name,
            price = if (response.data.promo.isActive) {
                BottomNavUiModel.Price.NettPrice(
                    ogPriceFmt = ogPrice,
                    nettPriceFmt = response.data.promo.nettPriceFmt,
                )
            } else if (response.data.campaign.isActive) {
                BottomNavUiModel.Price.DiscountedPrice(
                    discountedPrice = CurrencyFormatUtil.convertPriceValueToIdrFormat(
                        price = response.data.campaign.discountedPrice,
                        hasSpace = false
                    ),
                    ogPriceFmt = ogPrice,
                    discountPercentage = "${response.data.campaign.discountPercentage}%"
                )
            } else {
                BottomNavUiModel.Price.NormalPrice(ogPriceFmt = ogPrice)
            },
            stock = response.data.product.stock,
            shop = BottomNavUiModel.Shop(
                id = response.data.shop.id,
                name = response.data.shop.name
            ),
            hasVariant = response.data.hasVariant,
            buttonState = if (response.data.hasVariant) {
                BottomNavUiModel.ButtonState.Active
            } else {
                BottomNavUiModel.ButtonState.getByValue(
                    response.data.buttonState
                ) // Variant product always active, to open GVBS.
            },
            categoryTree = response.data.categoryTree.map { categoryTree ->
                BottomNavUiModel.CategoryTree(
                    id = categoryTree.id,
                    name = categoryTree.name,
                    title = categoryTree.title
                )
            }
        )
    }

    fun mapRemindMe(response: AddWishlistResponse): BottomNavUiModel.RemindMeUiModel =
        BottomNavUiModel.RemindMeUiModel(
            isSuccess = response.wishlistAdd.success,
            message = response.wishlistAdd.message
        )

    fun mapLike(response: LikeReviewResponse): ReviewLikeUiState = ReviewLikeUiState(
        count = response.data.totalLike,
        state = ReviewLikeUiState.ReviewLikeStatus.getByValue(response.data.likeStatus)
    )
}
