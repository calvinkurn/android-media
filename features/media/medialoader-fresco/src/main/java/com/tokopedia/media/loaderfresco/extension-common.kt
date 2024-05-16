package com.tokopedia.media.loaderfresco

import android.widget.ImageView
import com.tokopedia.media.loaderfresco.data.DEFAULT_ROUNDED
import com.tokopedia.media.loaderfresco.data.Properties

inline fun ImageView.loadImageFresco(
    url: String?,
    crossinline properties: Properties.() -> Unit = {}
) = call(url, Properties()
    .apply(properties))

inline fun ImageView.loadImageRoundedFresco(
    url: String?,
    rounded: Float = DEFAULT_ROUNDED,
    crossinline properties: Properties.() -> Unit = {}
) = call(url, Properties()
    .apply(properties)
    .setRoundedRadius(rounded))
