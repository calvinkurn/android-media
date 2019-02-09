package com.tokopedia.product.detail.data.model.talk

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductTalkQuery(
        @SerializedName("talk")
        @Expose
        val talk: TalkStats = TalkStats(),

        @SerializedName("talk_response")
        @Expose
        val talkResponse: TalkResponse = TalkResponse()
){
    data class TalkStats(
            @SerializedName("count")
            @Expose
            val count: Int = 0,

            @SerializedName("talks")
            @Expose
            val talks: List<Talk> = listOf()
    )

    data class Response(
            @SerializedName("ProductTalkQuery")
            @Expose
            val productTalkQuery: ProductTalkQuery = ProductTalkQuery()
    )
}