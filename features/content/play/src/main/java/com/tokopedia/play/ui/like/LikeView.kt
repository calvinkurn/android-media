package com.tokopedia.play.ui.like

import android.animation.Animator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.lottie.LottieAnimationView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.component.UIView

/**
 * Created by jegul on 02/12/19
 */
class LikeView(container: ViewGroup, listener: Listener) : UIView(container) {

    private companion object {

        const val START_ANIMATED_PROGRESS = 0f

    }

    private val view: View =
            LayoutInflater.from(container.context).inflate(R.layout.view_like, container, true)
                    .findViewById(R.id.cl_like)

    private val animationLike = view.findViewById<LottieAnimationView>(R.id.animation_like)
    private val vLikeClickArea = view.findViewById<View>(R.id.v_like_click_area)

    init {
        animationLike.addAnimatorListener(object : Animator.AnimatorListener {

            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                vLikeClickArea.isClickable = true
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
                vLikeClickArea.isClickable = false
            }
        })

        vLikeClickArea.setOnClickListener {
            val shouldLike = animationLike.progress == START_ANIMATED_PROGRESS
            playLikeAnimation(shouldLike)
            listener.onLikeClicked(this, shouldLike)
        }
    }

    override val containerId: Int = view.id

    override fun show() {
        view.show()
    }

    override fun hide() {
        view.hide()
    }

    private fun playLikeAnimation(shouldLike: Boolean) {
        if (!shouldLike) animationLike.progress = 0f
        else animationLike.playAnimation()
    }

    interface Listener {

        fun onLikeClicked(view: LikeView, shouldLike: Boolean)
    }
}