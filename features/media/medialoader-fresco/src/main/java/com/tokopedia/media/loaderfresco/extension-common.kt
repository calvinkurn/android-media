package com.tokopedia.media.loaderfresco

import android.widget.ImageView
import com.tokopedia.media.loaderfresco.data.Properties

inline fun ImageView.loadImage(url: String?,
                        crossinline properties: Properties.() -> Unit = {}
) = call(url, Properties()
    .apply(properties))

//TODO IMAGE Rounded
