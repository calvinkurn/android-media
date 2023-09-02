package com.tokopedia.feedplus.browse.data.tracker

import android.util.Log
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by meyta.taliti on 01/09/23.
 * https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4134
 */
class FeedBrowseTracker @Inject constructor(
    channelTracker: FeedBrowseChannelTracker,
    private val userSession: UserSessionInterface
): FeedBrowseChannelTracker by channelTracker {

    fun sendClickBackExitEvent() {
        Log.e("FEED Tracker", "sendClickBackExitEvent")
    }
}
