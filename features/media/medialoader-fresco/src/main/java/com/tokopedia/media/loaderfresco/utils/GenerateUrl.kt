package com.tokopedia.media.loaderfresco.utils

import android.net.Uri
import android.webkit.MimeTypeMap
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.tokopedia.config.GlobalConfig
import com.tokopedia.media.loaderfresco.data.HEADER_KEY_AUTH
import com.tokopedia.media.loaderfresco.data.HEADER_USER_ID
import com.tokopedia.media.loaderfresco.data.HEADER_X_DEVICE
import com.tokopedia.media.loaderfresco.data.PREFIX_BEARER


fun String.generateFrescoUri(): Uri {
    var url = this
    val extension = MimeTypeMap.getFileExtensionFromUrl(url)
    // get url that fresco needed to load

    val isNotWebpImageUrl = extension != "webp"


    //TODO from internal cdn
    //TODO valid url
    //TODO webp
    //TODO generate secure url
    return Uri.parse(url)
}

internal fun String.isFromInternalCdnImageUrl(): Boolean {
    return this.startsWith("https://images.tokopedia.net/")
}

internal fun String.isValidUrl(): Boolean {
    return this.isNotEmpty() && (this.contains("https") || this.contains("http"))
}

//private fun String.generateSecureUrl(
//    userId: String,
//    accessToken: String
//): GlideUrl {
//    return GlideUrl(
//        this,
//        LazyHeaders.Builder()
//            .apply {
//                addHeader(HEADER_KEY_AUTH, "$PREFIX_BEARER %s".format(accessToken))
//                addHeader(HEADER_X_DEVICE, "android-${GlobalConfig.VERSION_NAME}")
//                addHeader(HEADER_USER_ID, userId)
//            }
//            .build()
//    )
//}
