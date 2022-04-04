package com.tokopedia.review.feature.createreputation.presentation.bottomsheet

import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable.BaseCreateReviewVisitable
import com.tokopedia.review.feature.createreputation.presentation.viewholder.BaseCreateReviewViewHolder

class CreateReviewItemAnimator: DefaultItemAnimator() {

    companion object {
        const val ANIM_TYPE_SHOWING = "showing"
        const val ANIM_TYPE_HIDING = "hiding"
        const val ANIM_TYPE_ADDING = "adding"
    }

    init {
        supportsChangeAnimations = supportsChangeAnimations
        removeDuration = 300L
        moveDuration = 300L
        changeDuration = 300L
        addDuration = 300L
    }

    private val pendingAnim = arrayListOf<AnimInfo>()

    private fun onItemShowing(): ItemHolderInfo {
        return BaseCreateReviewVisitable.BaseCreateReviewHolderInfo(shouldShow = true)
    }

    private fun onItemHiding(): ItemHolderInfo {
        return BaseCreateReviewVisitable.BaseCreateReviewHolderInfo(shouldHide = true)
    }

    override fun isRunning(): Boolean {
        return super.isRunning() || pendingAnim.isNotEmpty()
    }

    override fun setSupportsChangeAnimations(supportsChangeAnimations: Boolean) {
        super.setSupportsChangeAnimations(this.supportsChangeAnimations)
    }

    override fun getSupportsChangeAnimations(): Boolean {
        return true
    }

    override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder): Boolean {
        return true
    }

    override fun canReuseUpdatedViewHolder(
        viewHolder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ): Boolean {
        return true
    }

    override fun recordPreLayoutInformation(
        state: RecyclerView.State,
        viewHolder: RecyclerView.ViewHolder,
        changeFlags: Int,
        payloads: MutableList<Any>
    ): ItemHolderInfo {
        val baseCreateReviewPayloads = payloads.firstOrNull()
        if (baseCreateReviewPayloads is List<*> && baseCreateReviewPayloads.contains(BaseCreateReviewVisitable.PAYLOAD_SHOWING)) {
            return onItemShowing()
        } else if (baseCreateReviewPayloads is List<*> && baseCreateReviewPayloads.contains(BaseCreateReviewVisitable.PAYLOAD_HIDING)) {
            return onItemHiding()
        }
        return super.recordPreLayoutInformation(state, viewHolder, changeFlags, payloads)
    }

    override fun animateChange(
        oldHolder: RecyclerView.ViewHolder,
        newHolder: RecyclerView.ViewHolder,
        preInfo: ItemHolderInfo,
        postInfo: ItemHolderInfo
    ): Boolean {
        if (newHolder is BaseCreateReviewViewHolder<*, *> && preInfo is BaseCreateReviewVisitable.BaseCreateReviewHolderInfo) {
            if (preInfo.shouldShow) {
                pendingAnim.add(AnimInfo(newHolder, ANIM_TYPE_SHOWING))
                dispatchAnimationStarted(newHolder)
                return true
            } else if (preInfo.shouldHide) {
                pendingAnim.add(AnimInfo(newHolder, ANIM_TYPE_HIDING))
                dispatchAnimationStarted(newHolder)
                return true
            }
        }

        return super.animateChange(oldHolder, newHolder, preInfo, postInfo)
    }

    override fun animateAdd(holder: RecyclerView.ViewHolder): Boolean {
        if (holder is BaseCreateReviewViewHolder<*, *>) {
            holder.binding.root.apply {
                val layoutParamsCopy = layoutParams
                layoutParamsCopy.height = Int.ZERO
                layoutParams = layoutParamsCopy
            }
            holder.binding.root.alpha = 0f
            pendingAnim.add(AnimInfo(holder, ANIM_TYPE_ADDING))
            dispatchAddStarting(holder)
            return true
        }

        return super.animateAdd(holder)
    }

    override fun runPendingAnimations() {
        super.runPendingAnimations()
        pendingAnim.forEach {
            it.start()
        }
        pendingAnim.clear()
    }

    private inner class AnimInfo(
        private val holder: BaseCreateReviewViewHolder<*, *>,
        private val animType: String
    ) {
        fun start() {
            when (animType) {
                ANIM_TYPE_ADDING -> {
                    holder.animateAdd(duration = addDuration, onStart = {

                    }, onComplete = {
                        dispatchAddFinished(holder)
                        if (!isRunning) dispatchAnimationsFinished()
                    })
                }
                ANIM_TYPE_SHOWING -> {
                    holder.animateShow(duration = changeDuration, onStart = {

                    }, onComplete = {
                        dispatchAnimationFinished(holder)
                        if (!isRunning) dispatchAnimationsFinished()
                    })
                }
                ANIM_TYPE_HIDING -> {
                    holder.animateHide(duration = changeDuration, onStart = {

                    }, onComplete = {
                        dispatchAnimationFinished(holder)
                        if (!isRunning) dispatchAnimationsFinished()
                    })
                }
            }
        }
    }
}