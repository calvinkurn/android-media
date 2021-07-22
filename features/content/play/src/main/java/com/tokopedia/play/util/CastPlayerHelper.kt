package com.tokopedia.play.util

import android.net.Uri
import android.util.Log
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.cast.CastPlayer
import com.google.android.exoplayer2.ext.cast.SessionAvailabilityListener
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.gms.cast.MediaInfo
import com.google.android.gms.cast.MediaMetadata
import com.google.android.gms.cast.MediaQueueItem
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastState
import com.google.android.gms.common.images.WebImage
import com.tokopedia.play.di.PlayScope
import com.tokopedia.play.view.uimodel.PlayCastState
import javax.inject.Inject

/**
 * Created by jegul on 15/06/21
 */
@PlayScope
class CastPlayerHelper @Inject constructor(
        private val castContext: CastContext,
        val player: CastPlayer
) {

    val hasAvailableSession: Boolean
        get() = player.isCastSessionAvailable

    fun getCurrentMediaMetadata(): MediaMetadata? {
        return castContext.sessionManager
                .currentCastSession
                .remoteMediaClient
                ?.mediaInfo
                ?.metadata
    }

    fun getCurrentMediaChannelId(): String {
        return getCurrentMediaMetadata()?.getString(KEY_CHANNEL_ID).orEmpty()
    }

    fun setSessionAvailabilityListener(listener: SessionAvailabilityListener?) {
        player.setSessionAvailabilityListener(listener)
    }

    fun castPlay(
            channelId: String,
            title: String,
            partnerName: String,
            coverUrl: String,
            videoUrl: String,
            currentPosition: Long
    ) {
        val mediaItem = getCastMediaItem(channelId, title, partnerName, coverUrl, videoUrl)
        player.loadItem(mediaItem, currentPosition)
    }

    private fun getCastMediaItem(
            channelId: String,
            title: String,
            partnerName: String,
            coverUrl: String,
            videoUrl: String,
    ): MediaQueueItem {
        val movieMetadata = MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE)
        movieMetadata.putString(KEY_CHANNEL_ID, channelId)
        movieMetadata.putString(MediaMetadata.KEY_TITLE, title)
        movieMetadata.putString(MediaMetadata.KEY_ALBUM_ARTIST, partnerName)
        movieMetadata.addImage(WebImage(Uri.parse(coverUrl)))
        val mediaInfo = MediaInfo.Builder(videoUrl)
                .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                .setContentType(MimeTypes.VIDEO_UNKNOWN)
                .setMetadata(movieMetadata).build()

        return MediaQueueItem.Builder(mediaInfo).build()
    }

    fun mapCastState(castState: Int): PlayCastState {
        return when(castState) {
            CastState.CONNECTING -> PlayCastState.CONNECTING
            CastState.CONNECTED -> PlayCastState.CONNECTED
            CastState.NOT_CONNECTED -> PlayCastState.NOT_CONNECTED
            CastState.NO_DEVICES_AVAILABLE -> PlayCastState.NO_DEVICE_AVAILABLE
            else -> PlayCastState.NO_DEVICE_AVAILABLE
        }
    }

    companion object {
        private const val KEY_CHANNEL_ID = "channel_id"
    }
}