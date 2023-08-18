package com.tokopedia.stories.widget

/**
 * Created by kenny.hadisaputra on 25/07/23
 */
internal sealed interface StoriesAvatarIntent {

    data class GetStoriesStatus(val shopIds: List<String>) : StoriesAvatarIntent

    object ShowCoachMark : StoriesAvatarIntent

    object HasSeenCoachMark : StoriesAvatarIntent
}
