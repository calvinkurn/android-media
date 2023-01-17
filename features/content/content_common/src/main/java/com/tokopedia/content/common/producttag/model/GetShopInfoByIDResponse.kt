package com.tokopedia.content.common.producttag.model

import com.google.gson.annotations.SerializedName

/**
 * Created By : Jonathan Darwin on May 20, 2022
 */
data class GetShopInfoByIDResponse(
    @SerializedName("shopInfoByID")
    val wrapper: Wrapper = Wrapper(),
) {

    data class Wrapper(
        @SerializedName("result")
        val result: List<Result> = emptyList(),
    )

    data class Result(
        @SerializedName("shopCore")
        val shopCore: ShopCore = ShopCore(),

        @SerializedName("goldOS")
        val goldOS: GoldOS = GoldOS(),
    )

    data class ShopCore(
        @SerializedName("shopID")
        val shopID: String = "",

        @SerializedName("name")
        val name: String = "",
    )

    data class GoldOS(
        @SerializedName("badge")
        val badge: String = "",

        @SerializedName("isGold")
        val isGold: Int = 0,

        @SerializedName("isGoldBadge")
        val isGoldBadge: Int = 0,

        @SerializedName("isOfficial")
        val isOfficial: Int = 0,
    )
}