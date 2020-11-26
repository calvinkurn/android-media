package com.tokopedia.play_common.util

import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.source.BehindLiveWindowException
import com.google.android.exoplayer2.upstream.HttpDataSource
import com.google.android.exoplayer2.upstream.Loader
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.UnknownHostException

/**
 * Created by jegul on 03/02/20
 */
class ExoPlaybackExceptionParser {

    private val knownCauses = arrayOf(
            BehindLiveWindowException::class.java,
            HttpDataSource.InvalidResponseCodeException::class.java,
            ConnectException::class.java,
            UnknownHostException::class.java,
            Loader.UnexpectedLoaderException::class.java
    )

    fun parse(e: ExoPlaybackException): ExceptionWrapper {
        if (e.type != ExoPlaybackException.TYPE_SOURCE) return ExceptionWrapper.UnknownException

        var cause: Throwable? = e.sourceException
        while (cause != null) {
            if (cause::class.java in knownCauses) return ExceptionWrapper.KnownException(cause)
            cause = cause.cause
        }
        return ExceptionWrapper.UnknownException
    }

    sealed class ExceptionWrapper {
        data class KnownException(val e: Throwable) : ExceptionWrapper()
        object UnknownException : ExceptionWrapper()

        companion object {
            private val blackListExceptionList = intArrayOf(HttpURLConnection.HTTP_NOT_FOUND, HttpURLConnection.HTTP_GONE, 416)
        }

        val isBehindLiveWindowException
            get() = this is KnownException && e is BehindLiveWindowException
        val isInvalidResponseCodeException
            get() = this is KnownException && e is HttpDataSource.InvalidResponseCodeException
        val isBlackListedException
            get() = this is KnownException && e is HttpDataSource.InvalidResponseCodeException && e.responseCode in blackListExceptionList
        val isConnectException
            get() = this is KnownException && e is ConnectException
        val isUnknownHostException
            get() = this is KnownException && e is UnknownHostException
        val isUnexpectedLoaderException
            get() = this is KnownException && e is Loader.UnexpectedLoaderException
    }

}