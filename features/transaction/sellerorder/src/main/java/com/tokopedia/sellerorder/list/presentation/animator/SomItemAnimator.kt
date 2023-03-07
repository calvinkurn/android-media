package com.tokopedia.sellerorder.list.presentation.animator

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.view.animation.Interpolator
import androidx.core.view.ViewCompat
import androidx.core.view.animation.PathInterpolatorCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ZERO

class SomItemAnimator : DefaultItemAnimator() {

    companion object {
        private const val DEFAULT_REMOVE_DURATION = 200L
        private const val DEFAULT_ADD_DURATION = 200L
        private const val ALPHA_MIN = 0f
        private const val ALPHA_MAX = 1f
        private const val REMOVAL_TARGET_TRANSITION_X = 1000f
        private const val ADDITION_TARGET_TRANSITION_Y = 0f
        private const val ADDITION_INITIAL_TRANSITION_Y = 50f
        private const val CUBIC_BEZIER_X1 = 0.63f
        private const val CUBIC_BEZIER_X2 = 0.29f
        private const val CUBIC_BEZIER_Y1 = 0.01f
        private const val CUBIC_BEZIER_Y2 = 1f
    }

    private val interpolator: Interpolator = PathInterpolatorCompat.create(
        CUBIC_BEZIER_X1, CUBIC_BEZIER_Y1, CUBIC_BEZIER_X2, CUBIC_BEZIER_Y2
    )
    private val pendingRemoval: ArrayList<RecyclerView.ViewHolder> = arrayListOf()
    private val pendingAdditions: ArrayList<RecyclerView.ViewHolder> = arrayListOf()
    private val removeAnimations: ArrayList<RecyclerView.ViewHolder> = arrayListOf()
    private val addAnimations: ArrayList<RecyclerView.ViewHolder> = arrayListOf()
    private val additionsList: ArrayList<ArrayList<RecyclerView.ViewHolder>> = arrayListOf()

    init {
        removeDuration = DEFAULT_REMOVE_DURATION
        addDuration = DEFAULT_ADD_DURATION
    }

    override fun runPendingAnimations() {
        val hasPendingAdditions = pendingAdditions.isNotEmpty()
        val hasPendingRemovals = pendingRemoval.isNotEmpty()
        if (hasPendingRemovals) {
            for (holder in pendingRemoval) {
                animateRemoveImpl(holder)
            }
            pendingRemoval.clear()
        }
        if (hasPendingAdditions) {
            val additions = arrayListOf<RecyclerView.ViewHolder>()
            additions.addAll(pendingAdditions)
            additionsList.add(additions)
            pendingAdditions.clear()
            val adder = Runnable {
                for (holder in additions) {
                    animateAddImpl(holder)
                }
                additions.clear()
                additionsList.remove(additions)
            }
            if (hasPendingRemovals) {
                val totalDelay = removeDuration
                additions.firstOrNull()?.itemView?.run {
                    ViewCompat.postOnAnimationDelayed(this, adder, totalDelay)
                }
            } else {
                adder.run()
            }
        }
        super.runPendingAnimations()
    }

    override fun animateRemove(holder: RecyclerView.ViewHolder?): Boolean {
        if (holder != null) {
            resetAnimation(holder)
            pendingRemoval.add(holder)
        }
        return true
    }

    override fun animateAdd(holder: RecyclerView.ViewHolder?): Boolean {
        if (holder != null) {
            resetAnimation(holder)
            holder.itemView.setupStateBeforeAddition()
            pendingAdditions.add(holder)
        }
        return true
    }

    override fun isRunning(): Boolean {
        return pendingRemoval.isNotEmpty() ||
                removeAnimations.isNotEmpty() ||
                pendingAdditions.isNotEmpty() ||
                additionsList.isNotEmpty() ||
                addAnimations.isNotEmpty() ||
                super.isRunning()
    }

    override fun endAnimations() {
        super.endAnimations()
        for (i in pendingRemoval.size.dec() downTo Int.ZERO) {
            pendingRemoval.getOrNull(i)?.let { item ->
                dispatchRemoveFinished(item)
                pendingRemoval.removeAt(i)
            }
        }
        for (i in pendingAdditions.size.dec() downTo Int.ZERO) {
            pendingAdditions.getOrNull(i)?.let { item ->
                item.itemView.resetStateAfterAddition()
                dispatchAddFinished(item)
                pendingAdditions.removeAt(i)
            }
        }

        if (!isRunning) return

        for (i in additionsList.size.dec() downTo Int.ZERO) {
            additionsList.getOrNull(i)?.let { additions ->
                for (j in additions.size.dec() downTo Int.ZERO) {
                    additions.getOrNull(j)?.let { item ->
                        val view = item.itemView
                        view.resetStateAfterAddition()
                        dispatchAddFinished(item)
                        additions.removeAt(j)
                        if (additions.isEmpty()) {
                            additionsList.remove(additions)
                        }
                    }
                }
            }
        }

        cancelAll(removeAnimations)
        cancelAll(addAnimations)

        dispatchFinishedWhenDone()
    }

    override fun endAnimation(item: RecyclerView.ViewHolder) {
        super.endAnimation(item)
        if (pendingRemoval.remove(item)) {
            item.itemView.resetStateAfterRemoval()
            dispatchRemoveFinished(item)
        }
        if (pendingAdditions.remove(item)) {
            item.itemView.resetStateAfterAddition()
            dispatchAddFinished(item)
        }

        for (i in additionsList.indices.reversed()) {
            additionsList.getOrNull(i)?.let { additions ->
                if (additions.remove(item)) {
                    item.itemView.resetStateAfterAddition()
                    dispatchAddFinished(item)
                    if (additions.isEmpty()) {
                        additionsList.removeAt(i)
                    }
                }
            }
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

    private fun animateAddImpl(holder: RecyclerView.ViewHolder) {
        val view = holder.itemView
        val animation = view.animate()
        addAnimations.add(holder)
        animation
            .alpha(ALPHA_MAX)
            .translationY(ADDITION_TARGET_TRANSITION_Y)
            .setDuration(addDuration)
            .setInterpolator(interpolator)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animator: Animator) {
                    dispatchAddStarting(holder)
                }

                override fun onAnimationCancel(animator: Animator) {
                    view.resetStateAfterAddition()
                }

                override fun onAnimationEnd(animator: Animator) {
                    animation.setListener(null)
                    dispatchAddFinished(holder)
                    addAnimations.remove(holder)
                    dispatchFinishedWhenDone()
                }
            }).start()
    }

    private fun cancelAll(viewHolders: List<RecyclerView.ViewHolder>) {
        for (i in viewHolders.indices.reversed()) {
            viewHolders.getOrNull(i)?.itemView?.animate()?.cancel()
        }
    }

    private fun View.resetStateAfterRemoval() {
        alpha = ALPHA_MAX
        translationX = Float.ZERO
    }

    private fun View.resetStateAfterAddition() {
        alpha = ALPHA_MAX
        translationY = ADDITION_TARGET_TRANSITION_Y
    }

    private fun View.setupStateBeforeAddition() {
        alpha = ALPHA_MIN
        translationY = ADDITION_INITIAL_TRANSITION_Y
    }
}