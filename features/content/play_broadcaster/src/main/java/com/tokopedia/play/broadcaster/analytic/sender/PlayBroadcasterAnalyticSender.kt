package com.tokopedia.play.broadcaster.analytic.sender

import com.tokopedia.content.common.ui.model.ContentAccountUiModel

/**
 * Created By : Jonathan Darwin on March 17, 2023
 */
interface PlayBroadcasterAnalyticSender {

    fun sendGeneralOpenScreen(
        screenName: String,
        trackerId: String,
    )

    fun sendGeneralViewEvent(
        eventAction: String,
        account: ContentAccountUiModel,
        trackerId: String
    )

    fun sendGeneralViewEvent(
        eventAction: String,
        eventLabel: String,
        trackerId: String
    )

    fun sendGeneralClickEvent(
        eventAction: String,
        account: ContentAccountUiModel,
        trackerId: String
    )

    fun sendGeneralClickEvent(
        eventAction: String,
        eventLabel: String,
        trackerId: String
    )
}
