package com.tokopedia.media.loader

import android.widget.ImageView
import com.tokopedia.media.loader.common.Properties

inline fun ImageView.loadSecureImage(
    url: String,
    accessToken: String,
    crossinline properties: Properties.() -> Unit = {}
) = call(
    url,
    Properties()
        .apply(properties)
        .userSessionAccessToken(accessToken),
    isSecure = true
)