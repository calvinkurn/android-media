package com.tokopedia.stories.common.domain

/**
 * Created by kenny.hadisaputra on 27/07/23
 */
data class ShopStoriesState(
    val shopId: String,
    val anyStoryExisted: Boolean,
    val hasUnseenStories: Boolean,
    val appLink: String
)
