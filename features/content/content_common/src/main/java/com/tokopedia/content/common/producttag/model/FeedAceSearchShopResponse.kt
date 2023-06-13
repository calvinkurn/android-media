package com.tokopedia.content.common.producttag.model

import com.google.gson.annotations.SerializedName

/**
 * Created By : Jonathan Darwin on May 11, 2022
 */
data class FeedAceSearchShopResponse(
    @SerializedName("aceSearchShop")
    val wrapper: Data = Data(),
) {
    data class Data(
        @SerializedName("total_shop")
        val totalShop: Int = 0,

        @SerializedName("header")
        val header: Header = Header(),

        @SerializedName("shops")
        val shops: List<Shop> = emptyList(),
    )

    data class Header(
        @SerializedName("total_data")
        val totalData: Int = 0,

        @SerializedName("total_data_text")
        val totalDataText: String = "",

        @SerializedName("response_code")
        val responseCode: Int = 0,

        @SerializedName("keyword_process")
        val keywordProcess: String = "",
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