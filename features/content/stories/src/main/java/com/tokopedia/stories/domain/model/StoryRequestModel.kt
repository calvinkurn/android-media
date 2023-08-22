package com.tokopedia.stories.domain.model

data class StoryRequestModel(
    val authorID: String,
    val authorType: String,
    val source: String,
    val sourceID: String,
)

enum class StoryAuthorType(val value: String) {
    SHOP("shop"),
}

enum class StorySource(val value: String) {
    SHOP_ENTRY_POINT("shop-entrypoint"),
    STORY_GROUP("story-group"),
    SHARE_LINK("sharelink"),
}
