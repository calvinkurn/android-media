package com.tokopedia.notifcenter

import android.app.Activity

/**
 * @author by milhamj on 31/08/18.
 */
interface NotifCenterRouter {
    fun openRedirectUrl(activity: Activity, url: String)
}