package com.tokopedia.media.loader.data

import com.bumptech.glide.load.engine.GlideException

// unify resources
val ERROR_RES_UNIFY = com.tokopedia.media.loader.R.drawable.medialoader_image_state_error
val PLACEHOLDER_RES_UNIFY = com.tokopedia.media.loader.R.drawable.medialoader_image_state_placeholder

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

// cache pref name
const val MEDIA_QUALITY_PREF = "media_image_quality"

// wrapper of GlideException
typealias MediaException = GlideException

// default rounded for loadImageRounded()
const val DEFAULT_ROUNDED = 5.0f

// uses for secure image loader
const val HEADER_KEY_AUTH = "Accounts-Authorization"
const val HEADER_USER_ID = "Tkpd-UserId"
const val HEADER_X_DEVICE = "X-Device"
const val PREFIX_BEARER = "Bearer"

const val DEBUG_TIMBER_TAG = "media-loader"
