package com.tokopedia.media.loader.utils

import android.net.Uri
import com.bumptech.glide.load.engine.GlideException

// wrapper of GlideException
typealias MediaException = GlideException

// url parameter of blur hashing
const val BLUR_HASH_QUERY = "b"

// default rounded
const val DEFAULT_ROUNDED = 5.0f

fun String.toUri(): Uri? {
    return Uri.parse(this)
}