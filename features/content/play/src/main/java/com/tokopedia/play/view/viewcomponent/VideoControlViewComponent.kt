package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.DefaultTimeBar
import com.google.android.exoplayer2.ui.PlayerControlView
import com.google.android.exoplayer2.ui.TimeBar
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by jegul on 03/08/20
 */
class VideoControlViewComponent(
        container: ViewGroup,
        @IdRes idRes: Int,
        listener: Listener
) : ViewComponent(container, idRes) {

    private val pcvVideo = rootView as PlayerControlView
    private val timeBar = pcvVideo.findViewById<DefaultTimeBar>(com.google.android.exoplayer2.ui.R.id.exo_progress)

    private val scrubListener = object : TimeBar.OnScrubListener {
        override fun onScrubMove(timeBar: TimeBar, position: Long) {
        }

        override fun onScrubStart(timeBar: TimeBar, position: Long) {
            listener.onStartSeeking(this@VideoControlViewComponent)
        }

        override fun onScrubStop(timeBar: TimeBar, position: Long, canceled: Boolean) {
            listener.onEndSeeking(this@VideoControlViewComponent)
        }
    }

    init {
        timeBar.addListener(scrubListener)
    }

    override fun show() {
        pcvVideo.show()
    }

    override fun hide() {
        pcvVideo.hide()
    }

    fun setPlayer(exoPlayer: ExoPlayer?) {
        pcvVideo.player = exoPlayer
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        pcvVideo.player = null
        timeBar.removeListener(scrubListener)
    }

    interface Listener {

        fun onStartSeeking(view: VideoControlViewComponent)
        fun onEndSeeking(view: VideoControlViewComponent)
    }
}