package com.tokopedia.media.loader.common

import android.graphics.Bitmap
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.signature.ObjectKey
import com.tokopedia.media.loader.R
import com.tokopedia.media.loader.data.Resize

open class Properties(
        var thumbnailUrl: String = "",
        var isAnimate: Boolean = false,
        var isCircular: Boolean = false,
        var roundedRadius: Float = 0f,
        var signature: Key? = null,
        var error: Int = R.drawable.ic_media_default_error,
        var placeHolder: Int = R.drawable.ic_media_default_placeholder,
        var cacheStrategy: DiskCacheStrategy? = DiskCacheStrategy.DATA,
        var overrideSize: Resize? = null,
        var decodeFormat: DecodeFormat? = DecodeFormat.DEFAULT,
        var loaderListener: LoaderStateListener? = null,
        var transform: Transformation<Bitmap>? = null,
        var transforms: List<Transformation<Bitmap>>? = null
)