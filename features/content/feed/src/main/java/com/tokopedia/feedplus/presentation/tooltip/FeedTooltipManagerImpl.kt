package com.tokopedia.feedplus.presentation.tooltip

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.content.common.util.UiEventManager
import com.tokopedia.feedplus.data.sharedpref.FeedTooltipPreferences
import com.tokopedia.feedplus.di.FeedMainScope
import com.tokopedia.feedplus.presentation.model.FeedTooltipEvent
import com.tokopedia.kotlin.extensions.view.toDate
import kotlinx.coroutines.flow.Flow
import java.util.*
import javax.inject.Inject

/**
 * Created by Jonathan Darwin on 24 April 2024
 */
class FeedTooltipManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val tooltipPreferences: FeedTooltipPreferences,
    private val uiEventManager: UiEventManager<FeedTooltipEvent>,
) : FeedTooltipManager {

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
        if (contentPosition != 3) return false

        val lastTimeShown = tooltipPreferences.getLastTimeSearchTooltipShown()

        if (lastTimeShown == 0L) return true

        val lastTimeShownCalendar = getCalendar(lastTimeShown.toDate())
        val currentCalendar = getCalendar(System.currentTimeMillis().toDate())

        val lastTimeShownCategory = FeedSearchTooltipCategory.getByDay(lastTimeShownCalendar.getDayOfMonth())
        val currentCategory = FeedSearchTooltipCategory.getByDay(currentCalendar.getDayOfMonth())

        if (lastTimeShownCategory != currentCategory) return true

        return currentCalendar.getMonth() != lastTimeShownCalendar.getMonth() || currentCalendar.getYear() != lastTimeShownCalendar.getYear()
    }

    override suspend fun emitShowTooltipEvent() {
        val currentCalendar = getCalendar(System.currentTimeMillis().toDate())
        val currentCategory = FeedSearchTooltipCategory.getByDay(currentCalendar.getDayOfMonth())

        uiEventManager.emitEvent(FeedTooltipEvent.ShowTooltip(context.getString(currentCategory.text)))
    }

    override fun setHasShownTooltip() {
        tooltipPreferences.setLastTimeSearchTooltipShown(System.currentTimeMillis())
    }

    override suspend fun clearTooltipEvent(id: Long) {
        uiEventManager.clearEvent(id)
    }

    private fun getCalendar(date: Date): Calendar {
        return Calendar.getInstance().apply {
            time = date
        }
    }

    private fun Calendar.getDayOfMonth(): Int {
        return get(Calendar.DAY_OF_MONTH)
    }

    private fun Calendar.getMonth(): Int {
        return get(Calendar.MONTH)
    }

    private fun Calendar.getYear(): Int {
        return get(Calendar.YEAR)
    }
}
