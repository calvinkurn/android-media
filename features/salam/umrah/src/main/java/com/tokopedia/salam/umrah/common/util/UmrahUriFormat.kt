package com.tokopedia.salam.umrah.common.util

import android.net.Uri

object UmrahUriFormat{
    fun getLastPathUrl(url: String): String {
        val uri = Uri.parse(url).lastPathSegment
        return if (uri.isNullOrEmpty()) ""
        else uri
    }
}