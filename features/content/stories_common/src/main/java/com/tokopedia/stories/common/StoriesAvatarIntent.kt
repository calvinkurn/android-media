package com.tokopedia.stories.common

/**
 * Created by kenny.hadisaputra on 25/07/23
 */
internal sealed interface StoriesAvatarIntent {

    data class GetStoriesStatus(val shopIds: List<String>) : StoriesAvatarIntent
    data class OpenStoriesDetail(val shopId: String) : StoriesAvatarIntent
}
