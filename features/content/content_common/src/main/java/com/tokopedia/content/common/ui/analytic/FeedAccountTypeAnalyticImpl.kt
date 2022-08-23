package com.tokopedia.content.common.ui.analytic

import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on May 23, 2022
 */
class FeedAccountTypeAnalyticImpl @Inject constructor(
    private val userSession: UserSessionInterface
) : FeedAccountTypeAnalytic {

    override fun clickAccountTypeItem(item: ContentAccountUiModel) {
        val type = when {
            item.isShop -> "shop"
            item.isUser -> "user"
            else -> ""
        }
        sendClickEvent("click - account types selection", type)
    }

    override fun clickAccountInfo() {
        sendClickEvent("click - available account types")
    }

    override fun clickChangeAccountToSeller() {
        sendClickEvent("click - change account to seller")
    }

    private fun sendClickEvent(action: String, label: String = "") {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to "clickPG",
                KEY_EVENT_ACTION to action,
                KEY_EVENT_CATEGORY to "content feed post creation",
                KEY_EVENT_LABEL to label,
                KEY_BUSINESS_UNIT to VAL_CONTENT,
                KEY_CURRENT_SITE to VAL_CURRENT_SITE,
                KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                KEY_USER_ID to userSession.userId,
            )
        )
    }
}