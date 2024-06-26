package com.tokopedia.shop.common.graphql.data.shopinfo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Broadcaster (
        @Expose
        @SerializedName("broadcasterGetAuthorConfig")
        val config: Config = Config()
){
    data class Config(
        @SerializedName("streamAllowed")
        val streamAllowed: Boolean = false,

        @SerializedName("shortVideoAllowed")
        val shortVideoAllowed: Boolean = false,

        @SerializedName("hasContent")
        val hasContent: Boolean = false,
    )
}

