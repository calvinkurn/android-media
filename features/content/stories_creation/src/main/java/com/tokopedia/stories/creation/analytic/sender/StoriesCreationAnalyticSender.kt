package com.tokopedia.stories.creation.analytic.sender

import com.tokopedia.content.common.ui.model.ContentAccountUiModel

/**
 * Created By : Jonathan Darwin on October 17, 2023
 */
interface StoriesCreationAnalyticSender {

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
