package com.tokopedia.stories.common

/**
 * Created by kenny.hadisaputra on 25/07/23
 */
internal data class StoriesAvatarState(
    val shopId: String,
    val status: StoriesStatus,
    val appLink: String,
) {
    companion object {
        fun create(shopId: String) = StoriesAvatarState(
            shopId = shopId,
            status = StoriesStatus.NoStories,
            appLink = "",
        )
    }
}
