package com.tokopedia.topads.edit.data.param

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
//            @field:SerializedName("shop_id")
//            var shop_id: Int = 0,
//            @field:SerializedName("group_id")
//            var group_id: Int? = 0,
            @field:SerializedName("type")
            var type: String? = "",
            @field:SerializedName("status")
            var status: String? = "",
            @field:SerializedName("tag")
            var tag: String? = "",
            @field:SerializedName("priceBid")
            var price_bid: Int? = 0,
            @field:SerializedName("source")
            var source: String? = ""

    )
}