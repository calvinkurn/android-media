package com.tokopedia.search.utils.applinkopener

import android.content.Context
import android.content.Intent
import com.tokopedia.applink.RouteManager

object ApplinkOpenerDelegate : ApplinkOpener {
    override fun openApplink(context: Context?, applink: String): Boolean {
        context ?: return false
        return openApplinkWithExtras(context, applink) { }
    }

    override fun openApplinkWithExtras(
        context: Context?,
        applink: String,
        extras: Intent.() -> Unit
    ): Boolean {
        context ?: return false

        val intent = RouteManager.getIntent(context, applink).apply(extras)
        context.startActivity(intent)

        return true
    }
}
