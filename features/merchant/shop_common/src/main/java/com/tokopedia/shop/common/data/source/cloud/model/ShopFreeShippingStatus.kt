package com.tokopedia.shop.common.data.source.cloud.model

import com.google.gson.annotations.SerializedName

class ShopFreeShippingStatus(
    @SerializedName("EligibleServiceShop")
    val response: Response
) {

    data class Response(
        @SerializedName("shops")
        val shops: List<Shop>
    )

    data class Shop(
        @SerializedName("shopID")
        val shopId:  Int,
        @SerializedName("status")
        val status: Boolean,
        @SerializedName("statusEligible")
        val statusEligible: Boolean,
        @SerializedName("statusSellerApproval")
        val statusSellerApproval: Int
    ) {
        object SellerApproval {
            const val APPROVED = 3
        }

        fun isEligible(): Boolean {
            return statusEligible && statusSellerApproval == SellerApproval.APPROVED
        }
    }
}