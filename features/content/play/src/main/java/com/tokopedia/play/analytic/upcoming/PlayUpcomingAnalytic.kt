package com.tokopedia.play.analytic.upcoming

/**
 * Created By : Jonathan Darwin on September 14, 2021
 */
interface PlayUpcomingAnalytic {
    fun impressUpcomingPage(channelId: String)

    fun clickRemindMe(channelId: String)

    fun clickCancelRemindMe(channelId: String)

    fun clickWatchNow(channelId: String)

    fun impressDescription(channelId: String)

    fun clickSeeAllDescription(channelId: String)

    fun clickSeeLessDescription(channelId: String)

    fun clickCover(channelId: String)

    fun impressCoverWithoutComponent(channelId: String)

    fun impressShare(channelId: String)
}