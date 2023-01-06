package com.tokopedia.topads.debit.autotopup.data.model


import com.google.gson.annotations.SerializedName

data class TopAdsShopTierShopGradeData(
    @SerializedName("shopInfoByID")
    val shopInfoByID: ShopInfoByID? = null
) {
    data class ShopInfoByID(
        @SerializedName("result")
        val result: List<Result>
    ) {
        data class Result(
            @SerializedName("goldOS")
            val goldOS: GoldOS? = null
        ) {
            data class GoldOS(
                @SerializedName("shopGrade")
                val shopGrade: Int,
                @SerializedName("shopTier")
                val shopTier: Int
            )
        }
    }
}
