package com.tokopedia.feedplus.presentation.tooltip

import com.tokopedia.feedplus.presentation.model.FeedTooltipEvent
import kotlinx.coroutines.flow.Flow

/**
 * Created by Jonathan Darwin on 24 April 2024
 */
interface FeedTooltipManager {

    val tooltipEvent: Flow<FeedTooltipEvent?>

    val currentCategory: FeedSearchTooltipCategory

    fun isShowTooltip(contentPosition: Int): Boolean

    suspend fun showTooltipEvent()

    fun setHasShownTooltip()

    suspend fun clearTooltipEvent(id: Long)
}
