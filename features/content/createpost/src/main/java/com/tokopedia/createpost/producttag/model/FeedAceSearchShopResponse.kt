package com.tokopedia.createpost.producttag.model

import com.google.gson.annotations.SerializedName

/**
 * Created By : Jonathan Darwin on May 11, 2022
 */
data class FeedAceSearchShopResponse(
    @SerializedName("aceSearchShop")
    val wrapper: Data = Data(),
) {
    data class Data(
        @SerializedName("shops")
        val shops: List<Shop> = emptyList(),
    )

    data class Shop(
        @SerializedName("shop_id")
        val shopId: String = "",

        @SerializedName("shop_name")
        val shopName: String = "",

        @SerializedName("shop_image")
        val shopImage: String = "",

        @SerializedName("shop_location")
        val shopLocation: String = "",

        @SerializedName("shop_gold_shop")
        val shopGoldShop: Int = 0,

        @SerializedName("shop_status")
        val shopStatus: Int = 0,

        @SerializedName("is_official")
        val isOfficial: Boolean = false,

        @SerializedName("is_pm_pro")
        val isPMPro: Boolean = false,
    )
}