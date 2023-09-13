package com.tokopedia.stories.widget.domain

/**
 * Created by kenny.hadisaputra on 27/07/23
 */
interface StoriesWidgetRepository {

    suspend fun setHasSeenCoachMark()

    suspend fun hasSeenCoachMark(): Boolean

    suspend fun getUpdatedSeenStatus(shopId: String, lastUpdated: TimeMillis): Boolean

    suspend fun getStoriesWidgetInfo(
        entryPoint: StoriesEntryPoint,
        shopIds: List<String>
    ): StoriesWidgetInfo
}
