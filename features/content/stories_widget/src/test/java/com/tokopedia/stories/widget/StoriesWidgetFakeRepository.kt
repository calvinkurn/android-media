package com.tokopedia.stories.widget

import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.stories.widget.domain.StoriesEntryPoint
import com.tokopedia.stories.widget.domain.StoriesWidgetInfo
import com.tokopedia.stories.widget.domain.StoriesWidgetRepository
import com.tokopedia.stories.widget.domain.StoriesWidgetState
import com.tokopedia.stories.widget.domain.TimeMillis

/**
 * Created by kenny.hadisaputra on 24/08/23
 */
class StoriesWidgetFakeRepository(
    private val forbiddenEntryPoints: List<StoriesEntryPoint> = emptyList(),
    initialHasSeenCoachMark: Boolean = false,
    private val coachMarkText: String = ""
) : StoriesWidgetRepository {

    private var mHasSeenCoachMark = initialHasSeenCoachMark

    private val stateMap = mutableMapOf<String, StoriesWidgetState>()
    private val seenStatusMap = mutableMapOf<String, Boolean>()

    override suspend fun setHasSeenCoachMark() {
        mHasSeenCoachMark = true
    }

    override suspend fun hasSeenCoachMark(): Boolean {
        return mHasSeenCoachMark
    }

    override suspend fun getUpdatedSeenStatus(shopId: String, lastUpdated: TimeMillis): Boolean {
        return seenStatusMap[shopId].orFalse()
    }

    override suspend fun getStoriesWidgetInfo(
        entryPoint: StoriesEntryPoint,
        shopIds: List<String>
    ): StoriesWidgetInfo {
        if (forbiddenEntryPoints.contains(entryPoint)) return StoriesWidgetInfo.Default
        return StoriesWidgetInfo(stateMap, coachMarkText)
    }

    fun setSeenStatus(shopId: String, hasBeenSeen: Boolean) {
        seenStatusMap[shopId] = hasBeenSeen
    }

    fun addStoriesWidgetState(state: StoriesWidgetState) {
        stateMap[state.shopId] = state
    }

    fun addAllStoriesWidgetState(states: List<StoriesWidgetState>) {
        states.associateByTo(stateMap) { it.shopId }
    }

    fun setStoriesWidgetState(states: List<StoriesWidgetState>) {
        stateMap.clear()
        addAllStoriesWidgetState(states)
    }
}
