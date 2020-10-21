package com.tokopedia.media.loader.utils

import android.net.Uri
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.signature.ObjectKey

// wrapper of GlideException
typealias MediaException = GlideException

/*
* Client hints: ECT
* The first client hints that will be used for adaptive is Effective Connection Types (ECT).
* Beside ECT, there are also other hints that we may take to consideration: Downlink and RTT.
* But for the first phase, ECT is giving us enough information regarding the client’s
* network capabilities. See this article to read the details.
* */
const val HEADER_ECT = "ECT"

// url parameter of blur hashing
const val BLUR_HASH_QUERY = "b"

// default rounded
const val DEFAULT_ROUNDED = 5.0f

// custom signature
fun Key?.mediaSignature(glideUrl: GlideUrl): Key {
    if (this != null) return this
    return ObjectKey("${glideUrl.toStringUrl()}&connection=${glideUrl.headers[HEADER_ECT].toString()}")
}

fun String.toUri(): Uri? {
    return Uri.parse(this)
}