package com.tokopedia.applink

import android.content.Context
import android.net.Uri

internal interface CustomAppLinkMapping {
    fun customDest(ctx: Context, uri: Uri, deeplink: String, ids: List<String>?): String
}
