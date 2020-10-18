package com.tokopedia.media.loader.utils

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

/*
* determine object classification in every network capability segmentation
* only supported for 2 segmentation are slow for 2g / 3g, and fast for 4g and wifi.
* */
const val LOW_QUALITY = "2g"
const val HIGH_QUALITY = "4g"

// default rounded
const val DEFAULT_ROUNDED = 5.0f

// custom signature
fun Key?.mediaSignature(glideUrl: GlideUrl): Key {
    if (this != null) return this
    return ObjectKey("${glideUrl.toStringUrl()}&connection=${glideUrl.headers[HEADER_ECT].toString()}")
}