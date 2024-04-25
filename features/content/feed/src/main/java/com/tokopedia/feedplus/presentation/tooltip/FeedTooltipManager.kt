package com.tokopedia.feedplus.presentation.tooltip

import com.tokopedia.feedplus.presentation.model.FeedTooltipEvent
import kotlinx.coroutines.flow.Flow

/**
 * Created by Jonathan Darwin on 24 April 2024
 */
interface FeedTooltipManager {

    val tooltipEvent: Flow<FeedTooltipEvent?>

    fun isShowTooltip(contentPosition: Int): Boolean

    suspend fun emitShowTooltipEvent()

    fun setHasShownTooltip()

    suspend fun clearTooltipEvent(id: Long)
}
