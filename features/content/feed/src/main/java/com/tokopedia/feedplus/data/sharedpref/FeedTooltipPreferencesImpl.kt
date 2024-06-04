package com.tokopedia.feedplus.data.sharedpref

import android.content.Context
import androidx.core.content.edit
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.feedplus.presentation.model.FeedDate
import javax.inject.Inject

/**
 * Created by Jonathan Darwin on 24 April 2024
 */
class FeedTooltipPreferencesImpl @Inject constructor(
    @ApplicationContext context: Context,
) : FeedTooltipPreferences {

    private val sharedPref = context.getSharedPreferences(FEED_TOOLTIP_PREFERENCES, Context.MODE_PRIVATE)

    override fun getLastTimeSearchTooltipShown(): FeedDate {
        return FeedDate(sharedPref.getString(KEY_LAST_TIME_SEARCH_TOOLTIP_SHOWN, "").orEmpty())
    }

    override fun setLastTimeSearchTooltipShown(date: FeedDate) {
        sharedPref.edit(true) {
            putString(KEY_LAST_TIME_SEARCH_TOOLTIP_SHOWN, date.date)
        }
    }

    companion object {
        private const val FEED_TOOLTIP_PREFERENCES = "feed_tooltip_pref"

        private const val KEY_LAST_TIME_SEARCH_TOOLTIP_SHOWN = "last_time_search_tooltip_shown"
    }
}
