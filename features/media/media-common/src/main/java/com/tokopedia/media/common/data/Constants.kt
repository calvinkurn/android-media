package com.tokopedia.media.common.data

import android.net.Uri

/*
* Client hints: ECT
* The first client hints that will be used for adaptive is Effective Connection Types (ECT).
* Beside ECT, there are also other hints that we may take to consideration: Downlink and RTT.
* But for the first phase, ECT is giving us enough information regarding the client’s
* network capabilities. See this article to read the details.
* */
const val PARAM_ECT = "ect"

// url parameter of blur hashing
const val PARAM_BLURHASH = "b"

// a new main URL using CDN
const val CDN_IMAGE_URL = "images.tokopedia.net"

/*
* determine object classification in every network capability segmentation
* only supported for 2 segmentation are slow for 2g / 3g, and fast for 4g and wifi.
* */
const val LOW_QUALITY = "3g"
const val HIGH_QUALITY = "4g"

// leave as high quality if users networkState have unexpected state.
const val UNDEFINED = "4g"

// the key of connection type
const val LOW_QUALITY_SETTINGS = 1 // 2g / 3g
const val HIGH_QUALITY_SETTINGS = 2 // 4g / wifi

// convert String to Uri
fun String.toUri(): Uri? {
    return Uri.parse(this)
}