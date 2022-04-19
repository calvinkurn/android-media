package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.play.animation.PlayFadeInFadeOutAnimation
import com.tokopedia.play.animation.PlayFadeOutAnimation
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by jegul on 03/08/20
 */
class ImmersiveBoxViewComponent(
        container: ViewGroup,
        @IdRes idRes: Int,
        private val listener: Listener
) : ViewComponent(container, idRes) {

    private val fadeOutAnimation = PlayFadeOutAnimation(FADE_DURATION)
    private val fadeInFadeOutAnimation = PlayFadeInFadeOutAnimation(FADE_DURATION, FADE_TRANSITION_DELAY)

    init {
        rootView.setOnClickListener {
            listener.onImmersiveBoxClicked(this, rootView.alpha)
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

    companion object {
        private const val FADE_DURATION = 200L
        private const val FADE_TRANSITION_DELAY = 3000L
    }

    interface Listener {

        fun onImmersiveBoxClicked(view: ImmersiveBoxViewComponent, currentAlpha: Float)
    }
}