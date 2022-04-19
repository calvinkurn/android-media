package com.tokopedia.media.preview.ui.activity.pagers.views

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.google.android.exoplayer2.ui.DefaultTimeBar
import com.google.android.exoplayer2.ui.PlayerControlView
import com.google.android.exoplayer2.ui.TimeBar
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.R
import com.tokopedia.media.R.color as mediaColorR
import com.tokopedia.unifycomponents.ContainerUnify

class VideoControlView(context: Context, attributeSet: AttributeSet) :
    PlayerControlView(context, attributeSet) {

    private val scrubber: DefaultTimeBar = findViewById(R.id.exo_progress)

    private val centerPlayButton: ImageView = findViewById(R.id.video_center_play_button)
    private val centerPauseButton: ImageView = findViewById(R.id.video_center_pause_button)

    init {
        val controllerContainer = findViewById<ContainerUnify>(R.id.nav_container)

        // lint force to use full path resource ref
        val controllerEndColor =
            ContextCompat.getColor(context, mediaColorR.dms_bg_gradient_video_player_end)
        val controllerStartColor =
            ContextCompat.getColor(context, mediaColorR.dms_bg_gradient_video_player_start)
        controllerContainer.setCustomContainerColor(Pair(controllerStartColor, controllerEndColor))
    }

    fun updateCenterButton(isPlaying: Boolean) {
        centerPlayButton.showWithCondition(!isPlaying)
//        centerPauseButton.showWithCondition(isPlaying)
    }

    fun setListener(listener: Listener) {
        centerPlayButton.setOnClickListener {
            listener.onCenterPlayButtonClicked()
        }

        centerPauseButton.setOnClickListener {
            listener.onCenterPauseButtonClicked()
        }

        scrubber.addListener(object : TimeBar.OnScrubListener {
            override fun onScrubMove(timeBar: TimeBar, position: Long) {
                listener.onScrubMove(position)
            }

            override fun onScrubStart(timeBar: TimeBar, position: Long) {
                listener.onScrubStart()
            }

            override fun onScrubStop(timeBar: TimeBar, position: Long, canceled: Boolean) {
                listener.onScrubStop()
            }
        })
    }

    interface Listener {
        fun onCenterPlayButtonClicked()
        fun onCenterPauseButtonClicked()
        fun onScrubStart()
        fun onScrubStop()
        fun onScrubMove(position: Long)
    }
}