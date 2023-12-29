package com.tokopedia.people.model.review

import com.tokopedia.people.views.uimodel.ProfileSettingsUiModel
import com.tokopedia.people.views.uimodel.UserReviewUiModel
import com.tokopedia.people.views.uimodel.mapper.UserProfileLikeStatusMapper

/**
 * Created By : Jonathan Darwin on June 05, 2023
 */
class UserReviewModelBuilder {

    fun buildReviewSetting(
        isEnabled: Boolean = true,
    ): List<ProfileSettingsUiModel> = listOf(
        ProfileSettingsUiModel(
            settingID = ProfileSettingsUiModel.SETTING_ID_REVIEW,
            title = "Show / hide review",
            isEnabled = isEnabled
        )
    )

    fun buildReviewList(
        size: Int = 5,
        sizeAttachment: Int = 5,
        page: Int = 2,
        hasNext: Boolean = true,
        isLike: Boolean = false,
        status: UserReviewUiModel.Status = UserReviewUiModel.Status.Success,
    ): UserReviewUiModel = UserReviewUiModel(
        reviewList = List(size) { idx ->
            UserReviewUiModel.Review(
                feedbackID = idx.toString(),
                product = UserReviewUiModel.Product(
                    productID = idx.toString(),
                    productName = "Product $idx",
                    productImageURL = "product_url_$idx",
                    productPageURL = "product_page_url_$idx",
                    productStatus = 0,
                    productVariant = UserReviewUiModel.ProductVariant(
                        variantID =  idx.toString(),
                        variantName = "Variant $idx",
                    )
                ),
                rating = idx.coerceAtMost(5),
                reviewText = "Waw",
                reviewTime = "time",
                attachments = List(sizeAttachment) {
                    if (it % 2 == 0) {
                        UserReviewUiModel.Attachment.Image(
                            attachmentID = it.toString(),
                            thumbnailUrl = "thumbnail_$it",
                            fullSizeUrl = "fullsizeUrl_$it",
                        )
                    } else {
                        UserReviewUiModel.Attachment.Video(
                            attachmentID = it.toString(),
                            mediaUrl = "mediaUrl_$it",
                        )
                    }
                },
                likeDislike = buildLikeDislike(
                    totalLike = idx,
                    isLike = isLike,
                ),
                isReviewTextExpanded = false
            )
        },
        page = page,
        hasNext = hasNext,
        status = status,
    )

    fun buildLikeDislike(
        totalLike: Int = 123,
        isLike: Boolean = false,
    ) = UserReviewUiModel.LikeDislike(
        totalLike = totalLike,
        isLike = isLike,
    )
}
