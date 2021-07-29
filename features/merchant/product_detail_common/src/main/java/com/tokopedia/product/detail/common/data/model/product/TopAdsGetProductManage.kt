package com.tokopedia.product.detail.common.data.model.product


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TopAdsGetProductManageResponse(
        @SerializedName("topAdsGetProductManageV2")
        @Expose
        val topAdsGetProductManage: TopAdsGetProductManage? = null
)

data class TopAdsGetProductManage(
        @SerializedName("data")
        val `data`: Data = Data()
) {
    data class Data(
            @SerializedName("ad_id")
            val adId: String = "",
            @SerializedName("ad_type")
            val adType: String = "1",
            @SerializedName("is_enable_ad")
            val isEnableAd: String = "0",
            @SerializedName("item_id")
            val itemId: String = "",
            @SerializedName("item_image")
            val itemImage: String = "",
            @SerializedName("item_name")
            val itemName: String = "",
            @SerializedName("manage_link")
            val manageLink: String = "",
            @SerializedName("shop_id")
            val shopId: String = ""
    )
}