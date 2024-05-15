package com.tokopedia.media.loaderfresco

import android.widget.ImageView
import com.tokopedia.media.loaderfresco.data.Properties

inline fun ImageView.loadImageFresco(url: String?,
                        crossinline properties: Properties.() -> Unit = {}
) = call(url, Properties()
    .apply(properties))

//TODO IMAGE Rounded
