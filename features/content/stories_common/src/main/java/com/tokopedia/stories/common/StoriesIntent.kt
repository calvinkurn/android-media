package com.tokopedia.stories.common

/**
 * Created by kenny.hadisaputra on 25/07/23
 */
internal sealed interface StoriesIntent {

    data class getStoriesStatus(val shopId: String) : StoriesIntent
}
