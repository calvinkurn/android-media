package com.tokopedia.talk.feature.reporttalk.analytics

import android.app.Activity
import com.tokopedia.track.TrackApp
import javax.inject.Inject

/**
 * @author by nisie on 8/28/18.
 */
class TalkAnalytics @Inject constructor() {

    fun sendScreen(activity: Activity, screenName: String) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName)
    }

    companion object {
        val SCREEN_NAME_REPORT_TALK: String = "Report Talk"
    }
}