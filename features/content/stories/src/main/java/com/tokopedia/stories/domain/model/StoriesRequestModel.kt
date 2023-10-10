package com.tokopedia.stories.domain.model

import com.google.gson.annotations.SerializedName

data class StoriesRequestModel(
    @SerializedName("authorID")
    val authorID: String,
    @SerializedName("authorType")
    val authorType: String,
    @SerializedName("source")
    val source: String,
    @SerializedName("sourceID")
    val sourceID: String,
    @SerializedName("entrypoint")
    val entryPoint: String,
)

enum class StoriesSource(val value: String) {
    SHOP_ENTRY_POINT("shop-entrypoint"),
    STORY_GROUP("story-group"),
    SHARE_LINK("sharelink")
}
