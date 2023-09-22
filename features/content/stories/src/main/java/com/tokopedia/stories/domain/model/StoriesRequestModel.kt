package com.tokopedia.stories.domain.model

data class StoriesRequestModel(
    val authorID: String,
    val authorType: String,
    val source: String,
    val sourceID: String,
    val entryPoint: String,
)

enum class StoriesSource(val value: String) {
    SHOP_ENTRY_POINT("shop-entrypoint"),
    STORY_GROUP("story-group"),
    SHARE_LINK("sharelink"),
}
