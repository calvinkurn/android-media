package com.tokopedia.play_common.type

import android.net.Uri
import com.tokopedia.play_common.exception.UnsupportedVideoTypeException

/**
 * Created by jegul on 29/11/19
 */
sealed class PlayVideoProtocol {

    abstract val uri: Uri

    companion object
}

data class Http(override val uri: Uri) : PlayVideoProtocol()
data class Rtmp(override val uri: Uri) : PlayVideoProtocol()

fun PlayVideoProtocol.Companion.getVideoTypeByUri(uri: Uri): PlayVideoProtocol {
    return when (Scheme.getSchemeFromUri(uri)) {
        Scheme.Http, Scheme.Https -> Http(uri)
        Scheme.Rtmp -> Rtmp(uri)
        else -> throw UnsupportedVideoTypeException()
    }
}