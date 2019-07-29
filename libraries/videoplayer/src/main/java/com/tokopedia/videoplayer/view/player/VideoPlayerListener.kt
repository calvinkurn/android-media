package com.tokopedia.videoplayer.view.player

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.videoplayer.state.PlayerException
import java.io.Serializable

interface VideoPlayerListener: Parcelable {
    /**
     * Handle state changed on ExoPlayer
     * you can see any states of playback here: `com.google.android.exoplayer2.Player`
     */
    fun onPlayerStateChanged(playbackState: Int)

    /**
     * Catch a error on player level
     * @param: PlayerException
     */
    fun onPlayerError(error: PlayerException)

    override fun writeToParcel(dest: Parcel?, flags: Int) {}
    override fun describeContents(): Int = 0
}