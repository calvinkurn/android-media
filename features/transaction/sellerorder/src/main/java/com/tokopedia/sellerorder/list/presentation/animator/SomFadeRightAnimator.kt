package com.tokopedia.sellerorder.list.presentation.animator

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.view.animation.BounceInterpolator
import android.view.animation.Interpolator
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ZERO

class SomFadeRightAnimator : DefaultItemAnimator() {

    companion object {
        private const val DEFAULT_REMOVE_DURATION = 200L
        private const val ALPHA_MIN = 0f
        private const val ALPHA_MAX = 1f
        private const val REMOVAL_TARGET_TRANSITION_X = 1000f
        private const val REMOVAL_INITIAL_TRANSITION_X = 0f
    }

    private val interpolator: Interpolator = BounceInterpolator()
    private val pendingRemoval: ArrayList<RecyclerView.ViewHolder> = arrayListOf()
    private val removeAnimations: ArrayList<RecyclerView.ViewHolder> = arrayListOf()

    init {
        removeDuration = DEFAULT_REMOVE_DURATION
    }

    override fun runPendingAnimations() {
        for (holder in pendingRemoval) {
            animateRemoveImpl(holder)
        }
        pendingRemoval.clear()
        super.runPendingAnimations()
    }

    override fun animateRemove(holder: RecyclerView.ViewHolder?): Boolean {
        if (holder != null) {
            resetAnimation(holder)
            pendingRemoval.add(holder)
        }
        return true
    }

    override fun isRunning(): Boolean {
        return pendingRemoval.isNotEmpty() || removeAnimations.isNotEmpty() || super.isRunning()
    }

    override fun endAnimations() {
        super.endAnimations()
        for (i in pendingRemoval.size.dec() downTo Int.ZERO) {
            val item = pendingRemoval[i]
            dispatchRemoveFinished(item)
            pendingRemoval.removeAt(i)
        }

        if (!isRunning) return

        cancelAll(removeAnimations)

        dispatchFinishedWhenDone()
    }

    override fun endAnimation(item: RecyclerView.ViewHolder) {
        super.endAnimation(item)
        if (pendingRemoval.remove(item)) {
            item.itemView.resetStateAfterRemoval()
            dispatchRemoveFinished(item)
        }
        dispatchFinishedWhenDone()
    }

    private fun dispatchFinishedWhenDone() {
        if (!isRunning) {
            dispatchAnimationsFinished()
        }
    }

    private fun resetAnimation(holder: RecyclerView.ViewHolder) {
        endAnimation(holder)
    }

    private fun animateRemoveImpl(holder: RecyclerView.ViewHolder) {
        val view = holder.itemView
        val animation = view.animate()
        removeAnimations.add(holder)
        animation
            .alpha(ALPHA_MIN)
            .translationX(REMOVAL_TARGET_TRANSITION_X)
            .setDuration(removeDuration)
            .setInterpolator(interpolator)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animator: Animator) {
                    dispatchRemoveStarting(holder)
                }

                override fun onAnimationEnd(animator: Animator) {
                    animation.setListener(null)
                    view.resetStateAfterRemoval()
                    dispatchRemoveFinished(holder)
                    removeAnimations.remove(holder)
                    dispatchFinishedWhenDone()
                }
            }).start()
    }

    private fun cancelAll(viewHolders: List<RecyclerView.ViewHolder>) {
        for (i in viewHolders.indices.reversed()) {
            viewHolders[i].itemView.animate().cancel()
        }
    }

    private fun View.resetStateAfterRemoval() {
        alpha = ALPHA_MAX
        translationX = REMOVAL_INITIAL_TRANSITION_X
    }
}