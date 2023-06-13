package com.tokopedia.content.common.producttag.analytic.autocomplete

import com.tokopedia.content.common.producttag.analytic.KEY_BUSINESS_UNIT
import com.tokopedia.content.common.producttag.analytic.KEY_CURRENT_SITE
import com.tokopedia.content.common.producttag.analytic.KEY_EVENT
import com.tokopedia.content.common.producttag.analytic.KEY_EVENT_ACTION
import com.tokopedia.content.common.producttag.analytic.KEY_EVENT_CATEGORY
import com.tokopedia.content.common.producttag.analytic.KEY_EVENT_LABEL
import com.tokopedia.content.common.producttag.analytic.KEY_SESSION_IRIS
import com.tokopedia.content.common.producttag.analytic.VAL_EVENT_CATEGORY
import com.tokopedia.content.common.producttag.analytic.KEY_USER_ID
import com.tokopedia.content.common.producttag.analytic.VAL_CONTENT
import com.tokopedia.content.common.producttag.analytic.VAL_CURRENT_SITE
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on May 24, 2022
 */
class FeedAutoCompleteAnalyticImpl @Inject constructor(
    private val userSession: UserSessionInterface
) : FeedAutoCompleteAnalytic {

    override fun clickRecentSearch() {
        sendClickEvent("click - search terakhir dicari")
    }

    override fun clickSuggestionShop(shopId: String) {
        sendClickEvent("click - toko section search result", shopId)
    }

    override fun impressSuggestionShop(shopId: String) {
        sendImpressEvent("impression - toko section search result", shopId)
    }

    override fun clickSuggestionKeyword() {
        sendClickEvent("click - recommendation search result")
    }

    private fun sendClickEvent(action: String, label: String = "") {
        sendGeneralEvent("clickPG", action, label)
    }

    private fun sendImpressEvent(action: String, label: String = "") {
        sendGeneralEvent("viewPGIris", action, label)
    }

    private fun sendGeneralEvent(event: String, action: String, label: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to event,
                KEY_EVENT_ACTION to action,
                KEY_EVENT_CATEGORY to VAL_EVENT_CATEGORY,
                KEY_EVENT_LABEL to label,
                KEY_BUSINESS_UNIT to VAL_CONTENT,
                KEY_CURRENT_SITE to VAL_CURRENT_SITE,
                KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                KEY_USER_ID to userSession.userId,
            )
        )
    }
}