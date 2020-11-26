package com.tokopedia.play.view.viewcomponent

import android.animation.Animator
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import com.airbnb.lottie.LottieAnimationView
import com.tokopedia.play.R
import com.tokopedia.play.view.uimodel.TotalLikeUiModel
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by jegul on 03/08/20
 */
class LikeViewComponent(
        container: ViewGroup,
        @IdRes idRes: Int,
        private val listener: Listener
) : ViewComponent(container, idRes) {

    private val animationLike = findViewById<LottieAnimationView>(R.id.animation_like)
    private val vLikeClickArea = findViewById<View>(R.id.v_like_click_area)
    private val tvTotalLikes = findViewById<Typography>(R.id.tv_total_likes)

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
    }

    fun setEnabled(isEnabled: Boolean) {
        if (isEnabled) {
            vLikeClickArea.setOnClickListener {
                val shouldLike = animationLike.progress == START_ANIMATED_PROGRESS
                listener.onLikeClicked(this, shouldLike)
            }
        } else {
            vLikeClickArea.setOnClickListener {  }
        }
    }

    fun setTotalLikes(totalLikes: TotalLikeUiModel) {
        tvTotalLikes.text = totalLikes.totalLikeFormatted
    }

    fun playLikeAnimation(shouldLike: Boolean, animate: Boolean) {
        if (!shouldLike) animationLike.progress = START_ANIMATED_PROGRESS
        else {
            if (animate) animationLike.playAnimation()
            else animationLike.progress = END_ANIMATED_PROGRESS
        }
    }

    private companion object {

        const val START_ANIMATED_PROGRESS = 0f
        const val END_ANIMATED_PROGRESS = 1f

    }

    interface Listener {

        fun onLikeClicked(view: LikeViewComponent, shouldLike: Boolean)
    }
}