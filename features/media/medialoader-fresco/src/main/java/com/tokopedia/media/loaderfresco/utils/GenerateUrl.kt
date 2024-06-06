package com.tokopedia.media.loaderfresco.utils

import android.net.Uri
import com.tokopedia.media.loaderfresco.data.Properties

//Need to check internal cdn later
//Need to check generate webp
//Need to check generate secure url
fun Properties.generateFrescoUrl(): String {
    return data.toString()
}

fun String.generateFrescoUri(): Uri {
    return Uri.parse(this)
}

internal fun String.isFromInternalCdnImageUrl(): Boolean {
    return this.startsWith("https://images.tokopedia.net/")
}
