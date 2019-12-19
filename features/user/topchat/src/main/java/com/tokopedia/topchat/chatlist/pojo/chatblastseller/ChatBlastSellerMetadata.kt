package com.tokopedia.topchat.chatlist.pojo.chatblastseller


import com.google.gson.annotations.SerializedName

data class ChatBlastSellerMetadata(
        @SerializedName("expireAt")
        val expireAt: String = "",
        @SerializedName("expirePromo")
        val expirePromo: String = "",
        @SerializedName("url")
        val url: String = "",
        @SerializedName("price")
        val price: Int = 0,
        @SerializedName("priceAB")
        val priceAB: Int = 0,
        @SerializedName("pricePB")
        val pricePB: Int = 0,
        @SerializedName("promo")
        val promo: Int = 0,
        @SerializedName("quota")
        val quota: Int = 0,
        @SerializedName("quotaGroup")
        val quotaGroup: QuotaGroup = QuotaGroup(),
        @SerializedName("refreshTime")
        val refreshTime: String = "",
        @SerializedName("shopId")
        val shopId: Int = -1,
        @SerializedName("status")
        val status: Int = -1,
        @SerializedName("unused")
        val unused: Boolean = false
) {
    fun isWhiteListed(): Boolean {
        return status == 1
    }
}