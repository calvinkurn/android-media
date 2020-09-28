package com.tokopedia.media.loader.common

import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.signature.ObjectKey
import com.tokopedia.media.loader.R
import com.tokopedia.media.loader.utils.DEFAULT_ROUNDED

open class Properties(
        var isRounded: Boolean = false,
        var isAnimate: Boolean = false,
        var isCircular: Boolean = false,
        var roundedRadius: Float = DEFAULT_ROUNDED,
        var signature: Key? = ObjectKey(System.currentTimeMillis().toString()),
        var error: Int = R.drawable.ic_media_default_error,
        var placeHolder: Int = R.drawable.ic_media_default_placeholder,
        var cacheStrategy: DiskCacheStrategy? = DiskCacheStrategy.DATA
)