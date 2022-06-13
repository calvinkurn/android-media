package com.tokopedia.media.preview.ui.player

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.google.android.exoplayer2.ui.DefaultTimeBar
import com.google.android.exoplayer2.ui.PlayerControlView
import com.google.android.exoplayer2.ui.TimeBar
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.R
import com.tokopedia.media.R.color as mediaColorR
import com.tokopedia.unifycomponents.ContainerUnify
import android.view.animation.AlphaAnimation
import android.view.animation.Animation

class VideoControlView(context: Context, attributeSet: AttributeSet) :
    PlayerControlView(context, attributeSet) {

    private val scrubber: DefaultTimeBar =
        findViewById(com.google.android.exoplayer2.R.id.exo_progress)

    private val centerPlayButton: ImageView = findViewById(R.id.video_center_play_button)
    private val centerPauseButton: ImageView = findViewById(R.id.video_center_pause_button)

    private val videoControlContainer: LinearLayout = findViewById(R.id.nav_container)

    private val videoControlHandler = Handler(Looper.getMainLooper())
    private val videoPauseButtonHandler = Handler(Looper.getMainLooper())

    private val fadeOutAlphaAnimation = AlphaAnimation(1f, 0f)
    private val fadeInAlphaAnimation = AlphaAnimation(0f, 1f)

    var listener: Listener? = null
        set(value) {
            field = value
            setupListener()
        }

    init {
        setupGradientBackground()
        setupAnimation()

        this.setOnClickListener {
            if (videoControlContainer.isVisible) {
                hideController()
            } else {
                showController(!centerPlayButton.isVisible)
            }
        }
    }

    fun updateCenterButtonState(isPlaying: Boolean) {
        centerPlayButtonConditionalShow(!isPlaying)
        centerPauseButtonConditionalShow(isPlaying)
    }

    private fun setupListener() {
        centerPlayButton.setOnClickListener {
            listener?.onCenterPlayButtonClicked()
            if (!videoControlContainer.isVisible) {
                showController()
            }
        }

        centerPauseButton.setOnClickListener {
            it.hide()
            centerPlayButton.show()
            listener?.onCenterPauseButtonClicked()
        }

        scrubber.addListener(object : TimeBar.OnScrubListener {
            override fun onScrubMove(timeBar: TimeBar, position: Long) {
                listener?.onScrubMove(position)
            }

            override fun onScrubStart(timeBar: TimeBar, position: Long) {
                listener?.onScrubStart()
            }

            override fun onScrubStop(timeBar: TimeBar, position: Long, canceled: Boolean) {
                listener?.onScrubStop()
            }
        })
    }

    fun cleanHideJob() {
        videoControlHandler.removeCallbacksAndMessages(null)
    }

    fun showController(isAutoHide: Boolean = true) {
        cleanHideJob()
        if (!videoControlContainer.isVisible) {
            videoControlContainer.startAnimation(fadeInAlphaAnimation)
        }
        if (isAutoHide) hideControllerJob()
    }

    private fun hideController() {
        videoControlContainer.startAnimation(fadeOutAlphaAnimation)
        cleanHideJob()
    }

    private fun centerPlayButtonConditionalShow(isShowing: Boolean) {
        cleanHideJob()
        centerPlayButton.showWithCondition(isShowing)
        hideControllerJob()
    }

    private fun centerPauseButtonConditionalShow(isShowing: Boolean) {
        centerPauseButton.showWithCondition(isShowing)
        if (isShowing) hideCenterPauseJob()
    }

    private fun hideCenterPauseJob() {
        videoPauseButtonHandler.postDelayed({
            centerPauseButtonConditionalShow(false)
        }, PLAY_DELAY_SCRUBBER)
    }

    private fun hideControllerJob() {
        videoControlHandler.postDelayed({
            hideController()
        }, HIDE_DELAY_SCRUBBER)
    }

    private fun setupAnimation() {
        fadeInAlphaAnimation.duration = ANIMATION_DURATION

        fadeOutAlphaAnimation.duration = ANIMATION_DURATION

        fadeOutAlphaAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationEnd(animation: Animation?) {
                videoControlContainer.hide()
            }

            override fun onAnimationRepeat(animation: Animation?) {}
            override fun onAnimationStart(animation: Animation?) {}
        })

        fadeInAlphaAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationEnd(animation: Animation?) {
                videoControlContainer.show()
            }

            override fun onAnimationRepeat(animation: Animation?) {}
            override fun onAnimationStart(animation: Animation?) {}
        })
    }

    private fun setupGradientBackground() {
        val controllerContainer = findViewById<ContainerUnify>(R.id.nav_container)

        val controllerEndColor =
            ContextCompat.getColor(context, mediaColorR.dms_bg_gradient_video_player_end)
        val controllerStartColor =
            ContextCompat.getColor(context, mediaColorR.dms_bg_gradient_video_player_start)
        controllerContainer.setCustomContainerColor(Pair(controllerStartColor, controllerEndColor))
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        videoControlHandler.removeCallbacksAndMessages(null)
        videoPauseButtonHandler.removeCallbacksAndMessages(null)
    }

    interface Listener {
        fun onCenterPlayButtonClicked()
        fun onCenterPauseButtonClicked()
        fun onScrubStart()
        fun onScrubStop()
        fun onScrubMove(position: Long)
    }

    companion object {
        const val HIDE_DELAY_SCRUBBER = 3000L
        const val PLAY_DELAY_SCRUBBER = 1000L
        const val ANIMATION_DURATION = 200L
    }
}