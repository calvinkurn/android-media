package com.tokopedia.people.model.review

import com.tokopedia.people.views.uimodel.ProfileSettingsUiModel
import com.tokopedia.people.views.uimodel.UserReviewUiModel

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
        page: Int = 2,
        hasNext: Boolean = true,
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
                attachments = listOf(),
                likeDislike = UserReviewUiModel.LikeDislike(
                    totalLike = idx,
                    likeStatus = 3,
                ),
                isReviewTextExpanded = false
            )
        },
        page = page,
        hasNext = hasNext,
        status = status,
    )
}
