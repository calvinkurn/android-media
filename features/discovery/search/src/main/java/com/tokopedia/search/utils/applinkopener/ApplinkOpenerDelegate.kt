package com.tokopedia.search.utils.applinkopener

import android.content.Context
import com.tokopedia.applink.RouteManager

object ApplinkOpenerDelegate : ApplinkOpener {
    override fun openApplink(context: Context?, applink: String) {
        val currentContext = context ?: return

        RouteManager.route(currentContext, applink)
    }
}