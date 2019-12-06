package com.tokopedia.play_common.type

import android.net.Uri
import com.tokopedia.play_common.exception.UnsupportedVideoTypeException

/**
 * Created by jegul on 29/11/19
 */
sealed class TokopediaPlayVideoType {

    abstract val uri: Uri

    companion object
}

data class Http(override val uri: Uri) : TokopediaPlayVideoType()
data class Rtmp(override val uri: Uri) : TokopediaPlayVideoType()

fun TokopediaPlayVideoType.Companion.getVideoTypeByUri(uri: Uri): TokopediaPlayVideoType {
    return when (Scheme.getSchemeFromUri(uri)) {
        Scheme.Http, Scheme.Https -> Http(uri)
        Scheme.Rtmp -> Rtmp(uri)
        else -> throw UnsupportedVideoTypeException()
    }
}