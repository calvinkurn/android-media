package com.tokopedia.topads.common.data.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.topads.common.data.response.Error

data class ResponseCreateGroup(

        @field:SerializedName("topadsCreateGroupAds")
        var topadsCreateGroupAds: TopadsCreateGroupAds = TopadsCreateGroupAds()
) {

    data class TopadsCreateGroupAds(
            @field:SerializedName("meta")
            var meta: Meta = Meta(),

            @field:SerializedName("errors")
            var errors: List<Error> = listOf()
    )

    data class Meta(

            @field:SerializedName("messages")
            var messages: List<MessagesItem> = listOf()
    )

    data class MessagesItem(

            @field:SerializedName("code")
            var code: String = "",

            @field:SerializedName("detail")
            var detail: String = "",

            @field:SerializedName("title")
            var title: String = ""
    )


}
