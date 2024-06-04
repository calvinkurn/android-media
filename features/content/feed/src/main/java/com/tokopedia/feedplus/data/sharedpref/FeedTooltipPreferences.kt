package com.tokopedia.feedplus.data.sharedpref

import com.tokopedia.feedplus.presentation.model.FeedDate

/**
 * Created by Jonathan Darwin on 24 April 2024
 */
interface FeedTooltipPreferences {

    fun getLastTimeSearchTooltipShown(): FeedDate

    fun setLastTimeSearchTooltipShown(date: FeedDate)
}
