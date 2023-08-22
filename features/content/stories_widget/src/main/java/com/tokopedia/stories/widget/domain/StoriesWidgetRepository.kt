package com.tokopedia.stories.widget.domain

import com.tokopedia.stories.widget.StoriesWidgetState
import com.tokopedia.stories.widget.TimeMillis

/**
 * Created by kenny.hadisaputra on 27/07/23
 */
interface StoriesWidgetRepository {

    suspend fun setHasSeenCoachMark()

    suspend fun hasSeenCoachMark(): Boolean

    suspend fun getUpdatedSeenStatus(shopId: String, lastUpdated: TimeMillis): Boolean

    suspend fun getStoriesWidgetState(
        entryPoint: StoriesEntryPoint,
        shopIds: List<String>
    ): List<StoriesWidgetState>
}
