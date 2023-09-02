package com.tokopedia.feedplus.browse.data.tracker

import android.util.Log
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by meyta.taliti on 01/09/23.
 */
class FeedBrowseChannelTrackerImpl @Inject constructor(
    private val userSession: UserSessionInterface,
    private val trackingQueue: TrackingQueue,
) : FeedBrowseChannelTracker {

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
