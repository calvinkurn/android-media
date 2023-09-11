package com.tokopedia.feedplus.browse.data.tracker

import com.tokopedia.content.analytic.BusinessUnit
import com.tokopedia.content.analytic.CurrentSite
import com.tokopedia.content.analytic.Event
import com.tokopedia.content.analytic.Key
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by meyta.taliti on 01/09/23.
 * https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4134
 */
class FeedBrowseTracker @Inject constructor(
    channelTracker: FeedBrowseChannelTracker.Factory,
    private val userSession: UserSessionInterface
) : FeedBrowseChannelTracker by channelTracker.create(PREFIX_VALUE) {

    companion object {
        private const val PREFIX_VALUE = "BROWSE_PAGE_FEED"
    }

    fun sendClickBackExitEvent() {
        Tracker.Builder()
            .setEvent(Event.clickHomepage)
            .setEventAction("click - back exit browse")
            .setEventCategory("feed browse page")
            .setEventLabel(PREFIX_VALUE)
            .setCustomProperty(Key.trackerId, "45745")
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .setUserId(userSession.userId)
            .build()
            .send()
    }
}
