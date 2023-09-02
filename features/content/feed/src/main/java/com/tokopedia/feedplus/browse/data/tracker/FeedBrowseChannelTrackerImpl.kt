package com.tokopedia.feedplus.browse.data.tracker

import android.util.Log
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * Created by meyta.taliti on 01/09/23.
 */
class FeedBrowseChannelTrackerImpl @AssistedInject constructor(
    @Assisted private val prefix: String,
    private val userSession: UserSessionInterface,
    private val trackingQueue: TrackingQueue,
) : FeedBrowseChannelTracker {

    @AssistedFactory
    interface Factory : FeedBrowseChannelTracker.Factory {
        override fun create(prefix: String): FeedBrowseChannelTrackerImpl
    }

    override fun sendViewChannelCardEvent() {
        Log.e("FEED Tracker", "sendViewChannelCardEvent")
    }

    override fun sendViewChipsWidgetEvent() {
        Log.e("FEED Tracker", "sendViewChipsWidgetEvent")
    }

    override fun sendClickChannelCardEvent() {
        Log.e("FEED Tracker", "sendClickChannelCardEvent")
    }

    override fun sendClickChipsWidgetEvent() {
        Log.e("FEED Tracker", "sendClickChipsWidgetEvent")
    }

}
