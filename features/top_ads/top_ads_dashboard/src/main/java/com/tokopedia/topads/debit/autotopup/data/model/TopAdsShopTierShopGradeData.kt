package com.tokopedia.topads.debit.autotopup.data.model


import com.google.gson.annotations.SerializedName

data class TopAdsShopTierShopGradeData(
    @SerializedName("shopInfoByID")
    val shopInfoByID: ShopInfoByID = ShopInfoByID()
) {
    data class ShopInfoByID(
        @SerializedName("result")
        val result: List<Result> = listOf()
    ) {
        data class Result(
            @SerializedName("goldOS")
            val goldOS: GoldOS = GoldOS()
        ) {
            data class GoldOS(
                @SerializedName("shopGrade")
                val shopGrade: Int = 0,
                @SerializedName("shopTier")
                val shopTier: Int = 0
            )
        }
    }
}
