package com.tokopedia.play.model

import com.tokopedia.play.view.uimodel.PlayUpcomingUiModel

/**
 * Created By : Jonathan Darwin on September 09, 2021
 */
class PlayUpcomingInfoModelBuilder {

    fun buildUpcomingInfo(
        title: String = "Test Upcoming",
        isUpcoming: Boolean = false,
        isReminderSet: Boolean = false,
        coverUrl: String = "cover_url.com",
        startTime: String = "2021-12-12'T'12:23:45",
        isAlreadyLive: Boolean = false,
        description: String = "Ini description"
    ): PlayUpcomingUiModel =
        PlayUpcomingUiModel(
            title = title,
            isUpcoming = isUpcoming,
            isReminderSet = isReminderSet,
            coverUrl = coverUrl,
            startTime = startTime,
            isAlreadyLive = isAlreadyLive,
            description = description,
        )
}