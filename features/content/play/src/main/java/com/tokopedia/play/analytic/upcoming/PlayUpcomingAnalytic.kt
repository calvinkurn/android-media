package com.tokopedia.play.analytic.upcoming

/**
 * Created By : Jonathan Darwin on September 14, 2021
 */
interface PlayUpcomingAnalytic {
    fun impressUpcomingPage(channelId: String)

    fun clickRemindMe(channelId: String)

    fun clickWatchNow(channelId: String)
}