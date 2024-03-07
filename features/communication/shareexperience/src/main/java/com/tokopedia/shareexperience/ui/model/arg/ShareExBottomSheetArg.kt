package com.tokopedia.shareexperience.ui.model.arg

import android.os.Parcelable
import com.tokopedia.shareexperience.domain.model.ShareExPageTypeEnum
import kotlinx.parcelize.Parcelize

/**
 * How to use
 * ShareExBottomSheetArg.Builder(pageTypeEnum, defaultUrl, trackerArg)
 *      .withProductId(id) // only put value to the necessary field
 *      .withReviewId(id)
 *      .build()
 */
@Parcelize
class ShareExBottomSheetArg private constructor(
    /**
     * Feature related id
     */
    val productId: String,
    val reviewId: String,
    val attachmentId: String,
    val shopId: String,
    val campaignId: String,
    val generalId: String,

    /**
     * Mandatory field
     */
    val pageTypeEnum: ShareExPageTypeEnum,
    val defaultUrl: String,
    val trackerArg: ShareExTrackerArg,
    /**
     * Optional
     */
    val selectedChip: String
) : Parcelable {
    class Builder(
        val pageTypeEnum: ShareExPageTypeEnum,
        val defaultUrl: String,
        val trackerArg: ShareExTrackerArg
    ) {
        private var productId: String? = null
        private var reviewId: String? = null
        private var attachmentId: String? = null
        private var shopId: String? = null
        private var campaignId: String? = null
        private var generalId: String? = null
        private var selectedChip: String? = null

        fun withProductId(productId: String) = apply {
            this.productId = productId
        }

        fun withReviewId(reviewId: String) = apply {
            this.reviewId = reviewId
        }

        fun withAttachmentId(attachmentId: String) = apply {
            this.attachmentId = attachmentId
        }

        fun withShopId(shopId: String) = apply {
            this.shopId = shopId
        }

        fun withCampaignId(campaignId: String) = apply {
            this.campaignId = campaignId
        }

        fun withGeneralId(generalId: String) = apply {
            this.generalId = generalId
        }

        fun withSelectedChip(selectedChip: String) = apply {
            this.selectedChip = selectedChip
        }

        fun build(): ShareExBottomSheetArg {
            return ShareExBottomSheetArg(
                pageTypeEnum = pageTypeEnum,
                defaultUrl = defaultUrl,
                trackerArg = trackerArg,

                productId = productId.orEmpty(),
                reviewId = reviewId.orEmpty(),
                attachmentId = attachmentId.orEmpty(),
                shopId = shopId.orEmpty(),
                campaignId = campaignId.orEmpty(),
                generalId = generalId.orEmpty(),

                selectedChip = selectedChip.orEmpty()
            )
        }
    }

    fun getIdentifier(): String {
        return when (pageTypeEnum) {
            ShareExPageTypeEnum.PDP -> productId
            ShareExPageTypeEnum.REVIEW -> reviewId
            ShareExPageTypeEnum.SHOP -> shopId
            ShareExPageTypeEnum.DISCOVERY -> campaignId
            else -> generalId
        }
    }
}
