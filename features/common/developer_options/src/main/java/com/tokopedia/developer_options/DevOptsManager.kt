package com.tokopedia.developer_options

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.media.VolumeProviderCompat
import com.tokopedia.developer_options.presentation.activity.DeveloperOptionActivity


object DevOptsManager {

    const val VOLUME_UP = 1
    const val VOLUME_DOWN = -1
    const val VOLUME_STAY = 0

    val TRIGGER_SEQUENCE = intArrayOf(VOLUME_UP, VOLUME_UP, VOLUME_DOWN, VOLUME_DOWN)
    var triggerSequenceStep = 0

    var mediaSession: MediaSessionCompat? = null

    fun register(activity: Activity) {

        val audio = activity.getSystemService(Context.AUDIO_SERVICE) as? AudioManager

        val STREAM_TYPE = AudioManager.STREAM_MUSIC
        val currentVolume = audio?.getStreamVolume(STREAM_TYPE) ?: 50
        val maxVolume = audio?.getStreamMaxVolume(STREAM_TYPE) ?: 100

        var myVolumeProvider = object : VolumeProviderCompat(VOLUME_CONTROL_RELATIVE, maxVolume, currentVolume) {
            override fun onAdjustVolume(direction: Int) {

                if (direction == VOLUME_UP) {
                    audio?.adjustStreamVolume(STREAM_TYPE,
                            AudioManager.ADJUST_RAISE, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                } else if (direction == VOLUME_DOWN) {
                    audio?.adjustStreamVolume(STREAM_TYPE,
                            AudioManager.ADJUST_LOWER, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                }
                setCurrentVolume(audio?.getStreamVolume(STREAM_TYPE) ?: 0);

                if (TRIGGER_SEQUENCE[triggerSequenceStep] == direction) {
                    triggerSequenceStep++
                } else if (direction != VOLUME_STAY) {
                    triggerSequenceStep = 0
                }

                if (triggerSequenceStep == TRIGGER_SEQUENCE.size) {
                    triggerSequenceStep = 0
                    activity.startActivity(Intent(activity, DeveloperOptionActivity::class.java))
                }
            }
        }
        mediaSession = MediaSessionCompat(activity, "BBB")
        mediaSession?.setPlaybackState(PlaybackStateCompat.Builder()
                .setState(PlaybackStateCompat.STATE_PLAYING, 0, 0f)
                .build())
        mediaSession?.setPlaybackToRemote(myVolumeProvider);
        mediaSession?.setActive(true)
    }

    fun unregister() {
        mediaSession?.setActive(false)
        mediaSession?.release()
        mediaSession = null
    }
}
