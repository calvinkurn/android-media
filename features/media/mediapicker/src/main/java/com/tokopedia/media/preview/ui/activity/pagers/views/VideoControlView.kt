package com.tokopedia.media.preview.ui.activity.pagers.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.google.android.exoplayer2.ui.DefaultTimeBar
import com.google.android.exoplayer2.ui.PlayerControlView
import com.google.android.exoplayer2.ui.TimeBar
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.R
import com.tokopedia.media.R.color as mediaColorR
import com.tokopedia.unifycomponents.ContainerUnify

class VideoControlView(context: Context, attributeSet: AttributeSet) :
    PlayerControlView(context, attributeSet) {

    private val centerControlButton: ImageView = findViewById(R.id.video_center_player_button)
    private val scrubber: DefaultTimeBar = findViewById<DefaultTimeBar>(R.id.exo_progress)

    private var playIconDrawable: Drawable? = null
    private var pauseIconDrawable: Drawable? = null

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
        centerControlButton.setImageDrawable(if (isPlaying) {
            centerControlButton.show()
            playIconDrawable ?: loadDrawableAsset(R.drawable.picker_ic_video_play_bg).also {
                playIconDrawable = it
            }
        } else {
            centerControlButton.hide()
            pauseIconDrawable ?: loadDrawableAsset(R.drawable.picker_ic_video_pause_bg).also {
                pauseIconDrawable = it
            }
        })
    }

    fun setListener(listener: Listener) {
        centerControlButton.setOnClickListener {
            listener.onCenterControlButtonClicked()
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

    private fun loadDrawableAsset(resId: Int): Drawable? {
        return MethodChecker.getDrawable(context, resId)
    }

    interface Listener {
        fun onCenterControlButtonClicked()
        fun onScrubStart()
        fun onScrubStop()
        fun onScrubMove(position: Long)
    }
}