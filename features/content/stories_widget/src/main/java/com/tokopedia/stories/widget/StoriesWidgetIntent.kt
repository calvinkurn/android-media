package com.tokopedia.stories.widget

/**
 * Created by kenny.hadisaputra on 25/07/23
 */
internal sealed interface StoriesWidgetIntent {

    data class GetStoriesStatus(val shopIds: List<String>) : StoriesWidgetIntent

    data class SetAllStoriesSeen(val shopId: String) : StoriesWidgetIntent

    object ShowCoachMark : StoriesWidgetIntent

    object HasSeenCoachMark : StoriesWidgetIntent
}
