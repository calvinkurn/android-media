package com.tokopedia.media.loaderfresco.utils

import android.net.Uri

//Need to check internal cdn later
//Need to check generate webp
//Need to check generate secure url
fun String.generateFrescoUri(): Uri {
    return Uri.parse(this)
}

internal fun String.isFromInternalCdnImageUrl(): Boolean {
    return this.startsWith("https://images.tokopedia.net/")
}
