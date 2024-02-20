package com.tokopedia.search.utils.applinkopener

import android.content.Context
import android.content.Intent

interface ApplinkOpener {
    fun openApplink(context: Context?, applink: String) : Boolean

    fun openApplinkWithExtras(context: Context?, applink: String, extras: Intent.() -> Unit) : Boolean
}
