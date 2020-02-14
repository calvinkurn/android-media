package com.tokopedia.play_common.util

import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.source.BehindLiveWindowException
import com.google.android.exoplayer2.upstream.HttpDataSource
import java.net.ConnectException

/**
 * Created by jegul on 03/02/20
 */
object ExoPlaybackExceptionHelper {

    fun isBehindLiveWindow(e: ExoPlaybackException) = isExceptionOfClass(e, BehindLiveWindowException::class.java)
    fun isInvalidResponseCode(e: ExoPlaybackException) = isExceptionOfClass(e, HttpDataSource.InvalidResponseCodeException::class.java)
    fun isConnectException(e: ExoPlaybackException) = isExceptionOfClass(e, ConnectException::class.java)

    private fun <T>isExceptionOfClass(e: ExoPlaybackException, clazz: Class<T>): Boolean {
        if (e.type != ExoPlaybackException.TYPE_SOURCE) return false

        var cause: Throwable? = e.sourceException
        while (cause != null) {
            if (cause::class.java == clazz) return true
            cause = cause.cause
        }
        return false
    }
}