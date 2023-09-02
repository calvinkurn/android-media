package com.tokopedia.feedplus.browse.data.tracker

import com.tokopedia.content.analytic.BusinessUnit
import com.tokopedia.content.analytic.CurrentSite
import com.tokopedia.content.analytic.Event
import com.tokopedia.content.analytic.Key
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * Created by meyta.taliti on 01/09/23.
 * https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4134
 */
class FeedBrowseTracker @AssistedInject constructor(
    @Assisted private val prefix: String, // can only be filled when opening page from browse icon in unified feed
    channelTracker: FeedBrowseChannelTracker.Factory,
    private val userSession: UserSessionInterface
): FeedBrowseChannelTracker by channelTracker.create(prefix) {

    @AssistedFactory
    interface Factory {
        fun create(prefix: String) : FeedBrowseTracker
    }

    fun sendClickBackExitEvent() {
        Tracker.Builder()
            .setEvent(Event.clickHomepage)
            .setEventAction("click - back exit browse")
            .setEventCategory("feed browse page")
            .setEventLabel(prefix)
            .setCustomProperty(Key.trackerId, "45745")
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .setUserId(userSession.userId)
            .build()
            .send()
    }
}
