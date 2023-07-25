package com.tokopedia.stories.common

/**
 * Created by kenny.hadisaputra on 25/07/23
 */
internal data class StoriesState(
    val shopId: String,
    val status: StoriesStatus,
) {
    companion object {
        fun create(shopId: String) = StoriesState(
            shopId = shopId,
            status = StoriesStatus.NoStories,
        )
    }
}
