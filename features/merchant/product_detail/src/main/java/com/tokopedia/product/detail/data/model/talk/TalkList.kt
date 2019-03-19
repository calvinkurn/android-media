package com.tokopedia.product.detail.data.model.talk

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TalkList(
        @SerializedName("list")
        @Expose
        val talks: List<Talk> = listOf()
){

    data class Result(
            @SerializedName("data")
            @Expose
            val data: TalkList = TalkList()
    )

    data class Response(
            @SerializedName("talkProductList")
            @Expose
            val result: Result = Result()
    )
}