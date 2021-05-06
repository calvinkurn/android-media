package com.tokopedia.topads.common.data.response

import com.google.gson.annotations.SerializedName

/**
 * Created by Pika on 15/4/20.
 */
data class KeywordEditInput(
        @field:SerializedName("action")
        var action: String = "",

        @field:SerializedName("keyword")
        var keyword: Keyword = Keyword()
) {

    data class Keyword(
            @field:SerializedName("id")
            var id: String = "",
            @field:SerializedName("type")
            var type: String? = "",
            @field:SerializedName("status")
            var status: String? = "",
            @field:SerializedName("tag")
            var tag: String? = "",
            @field:SerializedName("priceBid")
            var price_bid: Double? = 0.0,
            @field:SerializedName("source")
            var source: String? = ""

    )
}