package com.tokopedia.play.broadcaster.shorts.analytic.sender

import com.tokopedia.content.common.ui.model.ContentAccountUiModel

/**
 * Created By : Jonathan Darwin on November 24, 2022
 */
interface PlayShortsAnalyticSender {

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

    fun sendGeneralViewEventContent(
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

    fun sendGeneralClickEventContent(
        eventAction: String,
        eventLabel: String,
        trackerId: String
    )
}
