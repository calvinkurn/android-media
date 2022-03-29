package com.tokopedia.play.view.type

/**
 * @author by astidhiyaa on 18/02/22
 */
sealed class PlayUpcomingBellStatus {
    data class On(val campaignId: Long) : PlayUpcomingBellStatus()
    data class Off(val campaignId: Long) : PlayUpcomingBellStatus()
    object Unknown : PlayUpcomingBellStatus()
}

fun PlayUpcomingBellStatus.reversed(campaignId: Long): PlayUpcomingBellStatus =
    if (this is PlayUpcomingBellStatus.Off) PlayUpcomingBellStatus.On(campaignId) else PlayUpcomingBellStatus.Off(
        campaignId
    )