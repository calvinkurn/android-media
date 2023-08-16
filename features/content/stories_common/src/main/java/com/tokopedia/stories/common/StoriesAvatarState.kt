package com.tokopedia.stories.common

/**
 * Created by kenny.hadisaputra on 25/07/23
 */
data class StoriesAvatarState(
    val shopId: String,
    val status: StoriesStatus,
    val appLink: String
) {
    companion object {
        val Default: StoriesAvatarState
            get() = StoriesAvatarState(
                shopId = "",
                status = StoriesStatus.NoStories,
                appLink = ""
            )
    }
}
