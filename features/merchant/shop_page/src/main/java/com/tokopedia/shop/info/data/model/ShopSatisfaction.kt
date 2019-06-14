package com.tokopedia.shop.info.data.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopSatisfaction(
        @SerializedName("recentOneMonth")
        @Expose
        val recentOneMonth: SatisfactionItem = SatisfactionItem(),

        @SerializedName("recentOneYear")
        @Expose
        val recentOneYear: SatisfactionItem = SatisfactionItem(),

        @SerializedName("recentSixMonth")
        @Expose
        val recentSixMonth: SatisfactionItem = SatisfactionItem()
){

        data class Response(
                @SerializedName("ShopSatisfaction")
                @Expose
                val shopSatisfaction: ShopSatisfaction = ShopSatisfaction()
        )

        data class SatisfactionItem(
                @SerializedName("bad")
                @Expose
                val bad: Int = 0,

                @SerializedName("good")
                @Expose
                val good: Int = 0,

                @SerializedName("neutral")
                @Expose
                val neutral: Int = 0
        )
}