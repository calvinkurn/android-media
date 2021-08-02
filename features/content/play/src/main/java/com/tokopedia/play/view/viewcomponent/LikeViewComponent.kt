package com.tokopedia.play.view.viewcomponent

import android.animation.Animator
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.airbnb.lottie.LottieAnimationView
import com.tokopedia.play.R
import com.tokopedia.play.view.uimodel.recom.PlayLikeStatusInfoUiModel
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

    val clickAreaView: View
        get() = vLikeClickArea

    private val animationLike = findViewById<LottieAnimationView>(R.id.animation_like)
    private val vLikeClickArea = findViewById<View>(R.id.v_like_click_area)
    private val tvTotalLikes = findViewById<Typography>(R.id.tv_total_likes)

    private val likeAnimatorListener = object : Animator.AnimatorListener {

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
    }

    init {
        animationLike.addAnimatorListener(likeAnimatorListener)
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

    fun setTotalLikes(totalLikes: PlayLikeStatusInfoUiModel) {
        tvTotalLikes.text = totalLikes.totalLikeFormatted
    }

    fun playLikeAnimation(shouldLike: Boolean, animate: Boolean) {
        if (!shouldLike) animationLike.progress = START_ANIMATED_PROGRESS
        else {
            if (animate) animationLike.playAnimation()
            else animationLike.progress = END_ANIMATED_PROGRESS
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        animationLike.removeAnimatorListener(likeAnimatorListener)
    }

    private companion object {

        const val START_ANIMATED_PROGRESS = 0f
        const val END_ANIMATED_PROGRESS = 1f

    }

    interface Listener {

        fun onLikeClicked(view: LikeViewComponent, shouldLike: Boolean)
    }
}