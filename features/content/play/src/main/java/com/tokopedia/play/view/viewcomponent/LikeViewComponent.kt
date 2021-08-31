package com.tokopedia.play.view.viewcomponent

import android.animation.Animator
import android.os.CountDownTimer
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

    private var isMultipleLike: Boolean = false
    private var timer: CountDownTimer? = null
    private var currentLikeStatus = false

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
            vLikeClickArea.setOnClickListener { }
        }
    }

    fun setTotalLikes(totalLikes: PlayLikeStatusInfoUiModel) {
        /**
         * TODO: backend is not yet setup for multiplelike, so spamming like right now will counted 1 like
         * to prevent unconsistency view, we take the largest likes
         */
        tvTotalLikes.text = if(totalLikes.totalLike > totalLikes.previousLike || !isMultipleLike) totalLikes.totalLikeFormatted
                            else totalLikes.previousLikeFormatted
    }

    fun playLikeAnimation(shouldLike: Boolean, animate: Boolean) {
        if (!shouldLike) animationLike.progress = START_ANIMATED_PROGRESS
        else {
            if (animate) {
                playAnimation(
                    if(isMultipleLike) AnimationType.Spam
                    else AnimationType.Default
                )
            }
            else animationLike.progress = END_ANIMATED_PROGRESS
        }
        currentLikeStatus = shouldLike
    }

    private fun playAnimation(animationType: AnimationType) {
        animationLike.setAnimation(
            when(animationType) {
                AnimationType.Default -> R.raw.anim_play_like
                AnimationType.Spam -> R.raw.anim_spam_like
                AnimationType.Reminder -> R.raw.anim_shaking_thumb
            }
        )
        animationLike.playAnimation()
    }

    fun setIsMultipleLike(isMultipleLike: Boolean) {
        this.isMultipleLike = isMultipleLike
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        if(allowReminderLike())
            setReminderLike(INITIAL_REMINDER_DURATION)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        timer?.cancel()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        animationLike.removeAnimatorListener(likeAnimatorListener)
    }

    private fun setReminderLike(duration: Long) {
        timer = object: CountDownTimer(duration, COUNTDOWN_INTERVAL) {
            override fun onTick(p0: Long) {}

            override fun onFinish() {
                if(allowReminderLike()) {
                    playAnimation(AnimationType.Reminder)
                    setReminderLike(REMINDER_DURATION)
                }
            }
        }.start()
    }

    private fun allowReminderLike(): Boolean = isMultipleLike && !currentLikeStatus

    private companion object {

        const val START_ANIMATED_PROGRESS = 0f
        const val END_ANIMATED_PROGRESS = 1f

        const val COUNTDOWN_INTERVAL = 1000L
        const val INITIAL_REMINDER_DURATION = 300000L
        const val REMINDER_DURATION = 60000L

        // Mock
//        const val INITIAL_REMINDER_DURATION = 3000L
//        const val REMINDER_DURATION = 5000L
    }

    sealed class AnimationType {
        object Default: AnimationType()
        object Spam: AnimationType()
        object Reminder: AnimationType()
    }

    interface Listener {

        fun onLikeClicked(view: LikeViewComponent, shouldLike: Boolean)
    }
}