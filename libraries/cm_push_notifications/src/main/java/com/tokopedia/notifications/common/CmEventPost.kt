package com.tokopedia.notifications.common

import android.content.Context

import com.tokopedia.abstraction.common.utils.view.CommonUtils
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

/**
 * @author lalit.singh
 */
object CmEventPost {

    private val TAG = CmEventPost::class.java.simpleName

    fun postEvent(event: String, category: String, action: String, label: String) {
        CommonUtils.dumper("$TAG-$event&$category&$action&$label")
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                event, category, action, label))

    }

}
