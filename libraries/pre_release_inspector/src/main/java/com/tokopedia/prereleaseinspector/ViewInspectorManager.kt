package com.tokopedia.prereleaseinspector

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.media.AudioManager
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.media.VolumeProviderCompat
import java.util.*


@SuppressLint("StaticFieldLeak")
object ViewInspectorManager {

    const val TAG_OPTION_PICKER_DIALOG = "OPTION_PICKER_DIALOG"

    const val VOLUME_UP = 1
    const val VOLUME_DOWN = -1
    const val VOLUME_STAY = 0


    val viewList = ArrayList<View>()

    val VIEW_INSPECTOR_TRIGGER_SEQUENCE = intArrayOf(VOLUME_UP, VOLUME_DOWN, VOLUME_UP, VOLUME_DOWN)
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
                }
                else if (direction == VOLUME_DOWN) {
                    audio?.adjustStreamVolume(STREAM_TYPE,
                            AudioManager.ADJUST_LOWER, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                }
                setCurrentVolume(audio?.getStreamVolume(STREAM_TYPE) ?: 0);

                if (VIEW_INSPECTOR_TRIGGER_SEQUENCE[triggerSequenceStep] == direction) {
                    triggerSequenceStep++
                } else if (direction != VOLUME_STAY) {
                    triggerSequenceStep = 0
                }

                if (triggerSequenceStep == VIEW_INSPECTOR_TRIGGER_SEQUENCE.size) {
                    triggerSequenceStep = 0
                    showPopupDialog(activity)
                }
            }
        }
        mediaSession = MediaSessionCompat(activity, "AAA")
        mediaSession?.setPlaybackState(PlaybackStateCompat.Builder()
                .setState(PlaybackStateCompat.STATE_PLAYING, 0, 0f)
                .build())
        mediaSession?.setPlaybackToRemote(myVolumeProvider);
        mediaSession?.setActive(true)
    }

    private fun showPopupDialog(activity : Activity) {
        (activity as? AppCompatActivity)?.let {
            val viewInspectorDialog = ViewInspectorDialogFragment()
            viewInspectorDialog.setViewList(gatherView(it))
            viewInspectorDialog.show(it.supportFragmentManager, TAG_OPTION_PICKER_DIALOG)
        }
    }

    private fun gatherView(activity: Activity) : List<View> {
        viewList.clear()
        show_children(activity, activity.window.decorView)
        return viewList
    }

    private fun show_children(activity: Activity, v: View) {
        val viewgroup = v as ViewGroup
        for (i in 0 until viewgroup.childCount) {
            val v1 = viewgroup.getChildAt(i)
            viewList.add(v1)
            if (v1 is ViewGroup) {
                show_children(activity, v1)
            }
        }
    }

    fun unregister() {
        mediaSession?.setActive(false)
        mediaSession?.release()
        mediaSession = null
    }
}
