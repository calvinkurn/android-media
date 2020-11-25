package com.tokopedia.topads.dashboard.data.model

import com.google.gson.annotations.SerializedName

data class KeywordsResponse(

        @field:SerializedName("GetTopadsDashboardKeywords")
        val getTopadsDashboardKeywords: GetTopadsDashboardKeywords = GetTopadsDashboardKeywords()
) {
    data class GetTopadsDashboardKeywords(

            @field:SerializedName("data")
            val data: List<DataItem> = listOf(),

            @field:SerializedName("meta")
            val meta: Meta = Meta()
    ) {
        data class DataItem(

                @field:SerializedName("stat_total_conversion")
                val statTotalConversion: String = "",

                @field:SerializedName("keyword_status")
                val keywordStatus: Int = 0,

                @field:SerializedName("keyword_type_desc")
                val keywordTypeDesc: String = "",

                @field:SerializedName("keyword_price_bid_fmt")
                val keywordPriceBidFmt: String = "",

                @field:SerializedName("stat_total_impression")
                val statTotalImpression: String = "",

                @field:SerializedName("stat_total_spent")
                val statTotalSpent: String = "",

                @field:SerializedName("keyword_id")
                val keywordId: Int = 0,

                @field:SerializedName("stat_total_ctr")
                val statTotalCtr: String = "",

                @field:SerializedName("keyword_tag")
                val keywordTag: String = "",

                @field:SerializedName("stat_total_click")
                val statTotalClick: String = "",

                @field:SerializedName("keyword_price_bid")
                val keywordPriceBid: Int = 0,

                @field:SerializedName("stat_total_sold")
                val statTotalSold: String = "",

                @field:SerializedName("stat_avg_click")
                val statAvgClick: String = ""
        )
    }

    data class Meta(

            @field:SerializedName("page")
            val page: Page = Page()
    )

    data class Page(

            @field:SerializedName("per_page")
            val perPage: Int = 0,

            @field:SerializedName("current")
            val current: Int = 0,

            @field:SerializedName("max")
            val max:Int = 0
    )
}