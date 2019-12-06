package com.tokopedia.play_common.type

import android.net.Uri
import java.util.*

/**
 * Created by jegul on 29/11/19
 */
enum class Scheme(
        private val schemeString: String
) {
    Http("http"),
    Https("https"),
    Rtmp("rtmp"),

    Unsupported("");

    companion object {
        private val values = EnumSet.allOf(Scheme::class.java).apply {
            remove(Unsupported)
        }

        fun getSchemeFromUri(uri: Uri): Scheme {
            values.forEach {
                if (uri.scheme == it.schemeString) return it
            }
            return Unsupported
        }
    }
}