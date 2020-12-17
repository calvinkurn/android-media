package com.tokopedia.play_common.player.errorhandlingpolicy

import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ParserException
import com.google.android.exoplayer2.upstream.DefaultLoadErrorHandlingPolicy
import com.google.android.exoplayer2.upstream.HttpDataSource.InvalidResponseCodeException
import com.google.android.exoplayer2.upstream.Loader
import java.io.FileNotFoundException
import java.io.IOException
import java.net.HttpURLConnection

/**
 * Created by jegul on 08/09/20
 */
class PlayLoadErrorHandlingPolicy : DefaultLoadErrorHandlingPolicy() {
    override fun getRetryDelayMsFor(dataType: Int, loadDurationMs: Long, exception: IOException?, errorCount: Int): Long {
        return if (exception is ParserException || exception is FileNotFoundException || exception is Loader.UnexpectedLoaderException) C.TIME_UNSET
        else errorCount * RETRY_DELAY
    }

    override fun getMinimumLoadableRetryCount(dataType: Int): Int {
        return when (dataType) {
            C.DATA_TYPE_MEDIA_PROGRESSIVE_LIVE -> RETRY_COUNT_LIVE
            else -> RETRY_COUNT_DEFAULT
        }
    }

    override fun getBlacklistDurationMsFor(dataType: Int, loadDurationMs: Long, exception: IOException?, errorCount: Int): Long {
        if (exception is InvalidResponseCodeException) {
            val responseCode = exception.responseCode
            return if (responseCode == HttpURLConnection.HTTP_NOT_FOUND
                    || responseCode == HttpURLConnection.HTTP_GONE
                    || responseCode == 416 // HTTP 416 Range Not Satisfiable.
            ) BLACKLIST_MS else C.TIME_UNSET
        }
        return C.TIME_UNSET
    }

    companion object {
        private const val RETRY_COUNT_LIVE = 1
        private const val RETRY_COUNT_DEFAULT = 2
        private const val RETRY_DELAY = 1000L
        private const val BLACKLIST_MS = 4000L
    }
}