package com.tokopedia.media.loader.wrapper

import com.bumptech.glide.load.DecodeFormat

enum class MediaDecodeFormat {
    PREFER_ARGB_8888,
    PREFER_RGB_565,
    DEFAULT;

    companion object {
        fun mapTo(decodeFormat: MediaDecodeFormat): DecodeFormat {
            return when(decodeFormat) {
                PREFER_ARGB_8888 -> DecodeFormat.PREFER_ARGB_8888
                PREFER_RGB_565 -> DecodeFormat.PREFER_RGB_565
                DEFAULT -> DecodeFormat.DEFAULT
            }
        }
    }
}