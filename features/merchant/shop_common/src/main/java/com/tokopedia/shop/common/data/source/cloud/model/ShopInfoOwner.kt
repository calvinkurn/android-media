package com.tokopedia.shop.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ShopInfoOwner {
    @SerializedName("is_gold_merchant")
    @Expose
    var isIsGoldMerchant = false
        private set

    @SerializedName("is_seller")
    @Expose
    var isIsSeller = false
        private set

    @SerializedName("owner_email")
    @Expose
    var ownerEmail: Long = 0

    @SerializedName("owner_image")
    @Expose
    var ownerImage: String? = null

    @SerializedName("owner_messenger")
    @Expose
    var ownerMessenger: Long = 0

    @SerializedName("owner_name")
    @Expose
    var ownerName: String? = null

    @SerializedName("owner_phone")
    @Expose
    var ownerPhone: Long = 0
    fun setIsGoldMerchant(isGoldMerchant: Boolean) {
        isIsGoldMerchant = isGoldMerchant
    }

    fun setIsSeller(isSeller: Boolean) {
        isIsSeller = isSeller
    }
}