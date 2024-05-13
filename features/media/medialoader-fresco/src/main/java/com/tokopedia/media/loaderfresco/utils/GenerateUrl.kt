package com.tokopedia.media.loaderfresco.utils

import android.net.Uri


fun String?.generateFrescoUri(): Uri {
    // get url that fresco needed to load

    //TODO from internal cdn
    //TODO valid url
    //TODO webp
    //TODO generate secure url
    return Uri.parse(this)
}

internal fun String.isFromInternalCdnImageUrl(): Boolean {
    return this.startsWith("https://images.tokopedia.net/")
}

internal fun String.isValidUrl(): Boolean {
    return this.isNotEmpty() && (this.contains("https") || this.contains("http"))
}
