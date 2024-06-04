package com.tokopedia.feedplus.presentation.tooltip

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.util.UiEventManager
import com.tokopedia.feedplus.data.sharedpref.FeedTooltipPreferences
import com.tokopedia.feedplus.di.FeedMainScope
import com.tokopedia.feedplus.presentation.model.FeedDate
import com.tokopedia.feedplus.presentation.model.FeedTooltipEvent
import com.tokopedia.kotlin.extensions.view.toDate
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

/**
 * Created by Jonathan Darwin on 24 April 2024
 */
class FeedTooltipManagerImpl @Inject constructor(
    private val tooltipPreferences: FeedTooltipPreferences,
    private val uiEventManager: UiEventManager<FeedTooltipEvent>,
    private val dispatchers: CoroutineDispatchers,
) : FeedTooltipManager {

    private val currentDate = FeedDate.getCurrentDate()

    override val currentCategory = FeedSearchTooltipCategory.getByDay(currentDate.day)

    override val tooltipEvent: Flow<FeedTooltipEvent?>
        get() = uiEventManager.event

    override fun isShowTooltip(contentPosition: Int): Boolean {
        /**
         * Based on PRD, tooltip will be shown when app displays the 4th content.
         * Since the position index is starting from 0, the 4th index is 3.
         *
         * Only show tooltip if:
         * 1. app displays the 4th content
         * 2. user hasn't seen the tooltip for a specific period in this month.
         */
        try {
            if (contentPosition != 3) return false
            if (currentDate.isEmpty) return false

            val lastTimeShownDate = tooltipPreferences.getLastTimeSearchTooltipShown()

            if (lastTimeShownDate.isEmpty) return true
            if (lastTimeShownDate == currentDate) return false

            val lastTimeShownCategory = FeedSearchTooltipCategory.getByDay(lastTimeShownDate.day)

            if (lastTimeShownCategory != currentCategory) return true

            return lastTimeShownDate.month != currentDate.month || lastTimeShownDate.year != currentDate.year
        } catch (_: Throwable) {
            return false
        }
    }

    override suspend fun showTooltipEvent() {
        uiEventManager.emitEvent(FeedTooltipEvent.ShowTooltip(currentCategory))

        withContext(dispatchers.default) { delay(SHOW_TOOLTIP_DURATION) }

        uiEventManager.emitEvent(FeedTooltipEvent.DismissTooltip)
    }

    override fun setHasShownTooltip() {
        tooltipPreferences.setLastTimeSearchTooltipShown(currentDate)
    }

    override suspend fun clearTooltipEvent(id: Long) {
        uiEventManager.clearEvent(id)
    }

    companion object {
        private const val SHOW_TOOLTIP_DURATION = 4000L
    }
}
