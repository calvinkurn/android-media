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

        @SerializedName("shop_domain")
        val shopDomain: String = "",

        @SerializedName("shop_url")
        val shopUrl: String = "",

        @SerializedName("shop_image")
        val shopImage: String = "",

        @SerializedName("shop_image_300")
        val shopImage300: String = "",

        @SerializedName("shop_description")
        val shopDescription: String = "",

        @SerializedName("shop_tag_line")
        val shopTagline: String = "",

        @SerializedName("shop_location")
        val shopLocation: String = "",

        @SerializedName("shop_total_transaction")
        val shopTotalTransaction: String = "",

        @SerializedName("shop_total_favorite")
        val shopTotalFavorite: String = "",

        @SerializedName("shop_gold_shop")
        val shopGoldShop: Int = 0,

        @SerializedName("shop_is_owner")
        val shopIsOwner: Int = 0,

        @SerializedName("shop_rate_speed")
        val shopRateSpeed: Int = 0,

        @SerializedName("shop_rate_accuracy")
        val shopRateAccuracy: Int = 0,

        @SerializedName("shop_rate_service")
        val shopRateService: Int = 0,

        @SerializedName("shop_status")
        val shopStatus: Int = 0,

        @SerializedName("reputation_image_uri")
        val reputationImageUri: String = "",

        @SerializedName("is_official")
        val isOfficial: Boolean = false,
    )
}