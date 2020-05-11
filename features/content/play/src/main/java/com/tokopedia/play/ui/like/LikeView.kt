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
import com.tokopedia.play.view.uimodel.TotalLikeUiModel
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by jegul on 02/12/19
 */
class LikeView(container: ViewGroup, listener: Listener) : UIView(container) {

    private companion object {

        const val START_ANIMATED_PROGRESS = 0f
        const val END_ANIMATED_PROGRESS = 1f

    }

    private val view: View =
            LayoutInflater.from(container.context).inflate(R.layout.view_like, container, true)
                    .findViewById(R.id.cl_like)

    private val animationLike = view.findViewById<LottieAnimationView>(R.id.animation_like)
    private val vLikeClickArea = view.findViewById<View>(R.id.v_like_click_area)
    private val tvTotalLikes = view.findViewById<Typography>(R.id.tv_total_likes)

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

    internal fun setTotalLikes(totalLikes: TotalLikeUiModel) {
        tvTotalLikes.text = totalLikes.totalLikeFormatted
    }

    internal fun playLikeAnimation(shouldLike: Boolean, animate: Boolean) {
        if (!shouldLike) animationLike.progress = START_ANIMATED_PROGRESS
        else {
            if (animate) animationLike.playAnimation()
            else animationLike.progress = END_ANIMATED_PROGRESS
        }
    }

    interface Listener {

        fun onLikeClicked(view: LikeView, shouldLike: Boolean)
    }
}