package com.tokopedia.media.loader.utils

import android.net.Uri
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.tokopedia.config.GlobalConfig
import com.tokopedia.media.loader.data.*

// convert String to Uri
fun String.toUri(): Uri? {
    return Uri.parse(this)
}

fun String.isValidUrl(): Boolean {
    return this.isNotEmpty() && (this.contains("https") || this.contains("http"))
}

fun Properties.generateUrl(): Any {
    val data = data.toString()

    if (isSecure.not()) {
        return data
    }

    return GlideUrl(
        data,
        LazyHeaders.Builder()
            .apply {
                addHeader(HEADER_KEY_AUTH, "$PREFIX_BEARER %s".format(accessToken))
                addHeader(HEADER_X_DEVICE, "android-${GlobalConfig.VERSION_NAME}")
                addHeader(HEADER_USER_ID, userId)
            }
            .build()
    )
}
