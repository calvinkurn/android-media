package com.tokopedia.play.view.viewcomponent

import android.animation.Animator
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.IdRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.play.R
import com.tokopedia.play.animation.PlayFadeInFadeOutAnimation
import com.tokopedia.play.animation.PlayFadeOutAnimation
import com.tokopedia.play.extensions.isFullSolid
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by jegul on 03/08/20
 */
class VideoSettingsViewComponent(
        container: ViewGroup,
        @IdRes idRes: Int,
        private val listener: Listener
) : ViewComponent(container, idRes) {

    companion object {
        private const val FADE_DURATION = 200L
        private const val FADE_TRANSITION_DELAY = 3000L
    }

    private val ivFullscreenControl: ImageView = findViewById(R.id.iv_fullscreen_control)

    private val fadeInListener = object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator?) {
        }

        override fun onAnimationEnd(animation: Animator?) {
            animation?.removeAllListeners()
            rootView.isClickable = true
        }

        override fun onAnimationCancel(animation: Animator?) {
            animation?.removeAllListeners()
            rootView.isClickable = rootView.isFullSolid
        }

        override fun onAnimationStart(animation: Animator?) {
            rootView.isClickable = false
        }
    }

    private val fadeOutListener = object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator?) {
        }

        override fun onAnimationEnd(animation: Animator?) {
            animation?.removeAllListeners()
            rootView.isClickable = false
        }

        override fun onAnimationCancel(animation: Animator?) {
            animation?.removeAllListeners()
            rootView.isClickable = rootView.isFullSolid
        }

        override fun onAnimationStart(animation: Animator?) {
            rootView.isClickable = false
        }
    }

    private val fadeOutAnimation = PlayFadeOutAnimation(FADE_DURATION, fadeOutListener)
    private val fadeInFadeOutAnimation = PlayFadeInFadeOutAnimation(FADE_DURATION, FADE_TRANSITION_DELAY, fadeInListener, fadeOutListener)

    internal fun setFullscreen(isFullscreen: Boolean) {
        ivFullscreenControl.setImageResource(
                if (isFullscreen) R.drawable.ic_play_exit_fullscreen
                else R.drawable.ic_play_enter_fullscreen
        )

        rootView.setOnClickListener {
            if (isFullscreen) listener.onExitFullscreen(this)
            else listener.onEnterFullscreen(this)
        }
    }

    fun fadeOut() {
        cancelAllAnimation()

        fadeOutAnimation.start(rootView)
    }

    fun fadeIn() {
        cancelAllAnimation()

        fadeInFadeOutAnimation.start(rootView)
    }

    private fun cancelAllAnimation() {
        fadeInFadeOutAnimation.cancel()
        fadeOutAnimation.cancel()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        cancelAllAnimation()
    }

    interface Listener {

        fun onEnterFullscreen(view: VideoSettingsViewComponent)
        fun onExitFullscreen(view: VideoSettingsViewComponent)
    }
}