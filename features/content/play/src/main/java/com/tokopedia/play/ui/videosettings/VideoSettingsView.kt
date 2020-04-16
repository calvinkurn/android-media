package com.tokopedia.play.ui.videosettings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.animation.PlayFadeInFadeOutAnimation
import com.tokopedia.play.animation.PlayFadeOutAnimation
import com.tokopedia.play.component.UIView

/**
 * Created by jegul on 14/04/20
 */
class VideoSettingsView(
        container: ViewGroup,
        private val listener: Listener
) : UIView(container) {

    companion object {
        private const val FADE_DURATION = 200L
        private const val FADE_TRANSITION_DELAY = 3000L
    }

    private val view: View =
            LayoutInflater.from(container.context).inflate(R.layout.view_video_settings, container, true)
                    .findViewById(R.id.iv_fullscreen_control)

    private val ivFullscreenControl: ImageView = view as ImageView

    override val containerId: Int = view.id

    private val fadeOutAnimation = PlayFadeOutAnimation(FADE_DURATION)
    private val fadeInFadeOutAnimation = PlayFadeInFadeOutAnimation(FADE_DURATION, FADE_TRANSITION_DELAY)

    override fun show() {
        view.show()
    }

    override fun hide() {
        view.hide()
    }

    internal fun setFullscreen(isFullscreen: Boolean) {
        ivFullscreenControl.setImageResource(
                if (isFullscreen) R.drawable.ic_play_exit_fullscreen
                else R.drawable.ic_play_enter_fullscreen
        )

        ivFullscreenControl.setOnClickListener {
            if (isFullscreen) listener.onExitFullscreen(this)
            else listener.onEnterFullscreen(this)
        }
    }

    internal fun fadeOut() {
        cancelAllAnimation()

        fadeOutAnimation.start(view)
    }

    internal fun fadeIn() {
        cancelAllAnimation()

        fadeInFadeOutAnimation.start(view)
    }

    private fun cancelAllAnimation() {
        fadeInFadeOutAnimation.cancel()
        fadeOutAnimation.cancel()
    }

    interface Listener {

        fun onEnterFullscreen(view: VideoSettingsView)
        fun onExitFullscreen(view: VideoSettingsView)
    }
}