package com.tokopedia.feedplus.data.sharedpref

import android.content.Context
import androidx.core.content.edit
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import javax.inject.Inject

/**
 * Created by Jonathan Darwin on 24 April 2024
 */
class FeedTooltipPreferencesImpl @Inject constructor(
    @ApplicationContext context: Context,
) : FeedTooltipPreferences {

    private val sharedPref = context.getSharedPreferences(FEED_TOOLTIP_PREFERENCES, Context.MODE_PRIVATE)

    override fun getLastTimeSearchTooltipShown(): Long {
        return sharedPref.getLong(KEY_LAST_TIME_SEARCH_TOOLTIP_SHOWN, 0L)
    }

    override fun setLastTimeSearchTooltipShown(timeMillis: Long) {
        sharedPref.edit(true) {
            putLong(KEY_LAST_TIME_SEARCH_TOOLTIP_SHOWN, timeMillis)
        }
    }

    companion object {
        private const val FEED_TOOLTIP_PREFERENCES = "feed_tooltip_pref"

        private const val KEY_LAST_TIME_SEARCH_TOOLTIP_SHOWN = "last_time_search_tooltip_shown"
    }
}
