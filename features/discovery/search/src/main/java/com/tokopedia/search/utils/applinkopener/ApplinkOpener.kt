package com.tokopedia.search.utils.applinkopener

import android.content.Context

interface ApplinkOpener {
    fun openApplink(context: Context?, applink: String) : Boolean
}