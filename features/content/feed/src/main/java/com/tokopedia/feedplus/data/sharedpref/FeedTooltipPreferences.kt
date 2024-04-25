package com.tokopedia.feedplus.data.sharedpref

/**
 * Created by Jonathan Darwin on 24 April 2024
 */
interface FeedTooltipPreferences {

    fun getLastTimeSearchTooltipShown(): Long

    fun setLastTimeSearchTooltipShown(timeMillis: Long)
}
