package com.tokopedia.play.ui.immersivebox

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.animation.PlayFadeInFadeOutAnimation
import com.tokopedia.play.animation.PlayFadeOutAnimation
import com.tokopedia.play.component.UIView

/**
 * Created by jegul on 13/12/19
 */
class ImmersiveBoxView(
        container: ViewGroup,
        listener: Listener
) : UIView(container) {

    companion object {
        private const val FADE_DURATION = 200L
        private const val FADE_TRANSITION_DELAY = 3000L
    }

    private val view: View =
            LayoutInflater.from(container.context).inflate(R.layout.view_immersive_box, container, true)
                    .findViewById(R.id.v_immersive_box)

    init {
        view.setOnClickListener {
            listener.onImmersiveBoxClicked(this, view.alpha)
        }
    }

    override val containerId: Int = view.id

    private val fadeOutAnimation = PlayFadeOutAnimation(FADE_DURATION)
    private val fadeInFadeOutAnimation = PlayFadeInFadeOutAnimation(FADE_DURATION, FADE_TRANSITION_DELAY)

    override fun show() {
        view.show()
    }

    override fun hide() {
        view.hide()
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

        fun onImmersiveBoxClicked(view: ImmersiveBoxView, currentAlpha: Float)
    }
}