package com.tokopedia.product.detail.data.model.restrictioninfo

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
    fun isNotEligibleExclusive():Boolean {
        return restrictionExclusiveType() && !isEligible
    }

    fun restrictionShopFollowersType(): Boolean {
        return action.firstOrNull()?.attributeName ?: "" == RestrictionInfoResponse.SHOP_FOLLOWERS_TYPE
    }

    fun restrictionExclusiveType(): Boolean {
        return action.firstOrNull()?.attributeName ?: "" == RestrictionInfoResponse.SHOP_EXCLUSIVE_TYPE
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
        val attributeName: String = ""
)