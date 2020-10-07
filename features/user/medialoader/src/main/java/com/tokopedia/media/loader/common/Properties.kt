package com.tokopedia.media.loader.common

import android.graphics.Bitmap
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.Transformation
import com.tokopedia.media.loader.R
import com.tokopedia.media.loader.data.Resize
import com.tokopedia.media.loader.wrapper.MediaCacheStrategy
import com.tokopedia.media.loader.wrapper.MediaDecodeFormat

open class Properties(
        var thumbnailUrl: String = "",
        var isAnimate: Boolean = false,
        var isCircular: Boolean = false,
        var roundedRadius: Float = 0f,
        var signature: Key? = null,
        var error: Int = R.drawable.ic_media_default_error,
        var placeHolder: Int = R.drawable.ic_media_default_placeholder,
        var cacheStrategy: MediaCacheStrategy? = MediaCacheStrategy.ALL,
        var overrideSize: Resize? = null,
        var decodeFormat: MediaDecodeFormat? = MediaDecodeFormat.PREFER_RGB_565,
        var loaderListener: LoaderStateListener? = null,
        var transform: Transformation<Bitmap>? = null,
        var transforms: List<Transformation<Bitmap>>? = null
)