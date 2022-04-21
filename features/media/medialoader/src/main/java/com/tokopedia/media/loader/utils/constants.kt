package com.tokopedia.media.loader.utils

import com.bumptech.glide.load.engine.GlideException

// wrapper of GlideException
typealias MediaException = GlideException

// default rounded for loadImageRounded()
const val DEFAULT_ROUNDED = 5.0f

// uses for secure image loader
const val HEADER_KEY_AUTH = "Accounts-Authorization"
const val PREFIX_BEARER = "Bearer"