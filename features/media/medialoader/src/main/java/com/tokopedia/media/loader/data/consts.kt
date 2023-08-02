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
internal const val PARAM_ECT = "ect"

// url parameter of blur hashing
internal const val PARAM_BLURHASH = "b"

/*
* determine object classification in every network capability segmentation
* only supported for 2 segmentation are slow for 2g / 3g, and fast for 4g and wifi.
* */
internal const val LOW_QUALITY = "3g"
internal const val HIGH_QUALITY = "4g"

// leave as high quality if users networkState have unexpected state.
internal const val UNDEFINED = "4g"

// the key of connection type
internal const val LOW_QUALITY_SETTINGS = 1 // 2g / 3g
internal const val HIGH_QUALITY_SETTINGS = 2 // 4g / wifi

// cache pref name
internal const val MEDIA_QUALITY_PREF = "media_image_quality"

// wrapper of GlideException
typealias MediaException = GlideException

const val DEFAULT_ROUNDED = 5.0f
const val DEFAULT_ICON_SIZE = 300

// uses for secure image loader
internal const val HEADER_KEY_AUTH = "Accounts-Authorization"
internal const val HEADER_USER_ID = "Tkpd-UserId"
internal const val HEADER_X_DEVICE = "X-Device"
internal const val PREFIX_BEARER = "Bearer"

internal const val DEBUG_TIMBER_TAG = "media-loader"
