package com.tokopedia.product.detail.common.data.model.re

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Yehezkiel on 06/10/20
 */
data class RestrictionInfoResponse(
        @SerializedName("message")
        @Expose
        val message: String = "",

        @SerializedName("restrictionData")
        @Expose
        val restrictionData: List<RestrictionData> = listOf()
) {
    companion object {
        const val SHOP_FOLLOWERS_TYPE = "shop_follower"
        const val SHOP_EXCLUSIVE_TYPE = "exclusive_discount"
        const val GAMIFICATION_TYPE = "gamification_eligible_rp0"
        const val LOCATION_TYPE = "campaign_location"

        const val CATEGORIES_KYC_STATUS_TYPE = "category_user_kyc_status"
        const val CATEGORIES_AGE_TYPE = "category_user_kyc_age"
    }

    fun getReByProductId(productId: String): RestrictionData? {
        return restrictionData.firstOrNull { it.productId == productId }
    }
}

data class RestrictionData(
        @SerializedName("productID")
        @Expose
        val productId: String = "",

        //If true the button follow npl will not be shown
        @SerializedName("isEligible")
        @Expose
        val isEligible: Boolean = true,

        @SerializedName("action")
        @Expose
        val action: List<RestrictionAction> = listOf()
) {
    fun isNotEligibleExclusive(): Boolean {
        return restrictionExclusiveType() && !isEligible
    }

    fun restrictionCategoriesType(): Boolean {
        return action.firstOrNull()?.attributeName == RestrictionInfoResponse.CATEGORIES_KYC_STATUS_TYPE ||
                action.firstOrNull()?.attributeName == RestrictionInfoResponse.CATEGORIES_AGE_TYPE
    }

    fun restrictionShopFollowersType(): Boolean {
        return action.firstOrNull()?.attributeName == RestrictionInfoResponse.SHOP_FOLLOWERS_TYPE
    }

    fun restrictionExclusiveType(): Boolean {
        return action.firstOrNull()?.attributeName == RestrictionInfoResponse.SHOP_EXCLUSIVE_TYPE
    }

    fun restrictionGamificationType(): Boolean {
        return action.firstOrNull()?.attributeName == RestrictionInfoResponse.GAMIFICATION_TYPE
    }

    fun restrictionLocationType(): Boolean {
        return action.firstOrNull()?.attributeName == RestrictionInfoResponse.LOCATION_TYPE
    }
}

data class RestrictionAction(
        @SerializedName("actionType")
        @Expose
        val actionType: String = "",

        @SerializedName("title")
        @Expose
        val title: String = "",

        @SerializedName("description")
        @Expose
        val description: String = "",

        @SerializedName("badgeURL")
        @Expose
        val badgeURL: String = "",

        @SerializedName("attributeName")
        @Expose
        val attributeName: String = "",

        @SerializedName("buttonLink")
        @Expose
        val buttonLink: String = "",

        @SerializedName("buttonText")
        @Expose
        val buttonText: String = ""
)