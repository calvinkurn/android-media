package com.tokopedia.tokomart.common.base.viewholder

import android.view.View
import android.view.animation.Animation
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.tokomart.R
import com.tokopedia.tokomart.common.base.uimodel.BaseExpandableUiModel
import com.tokopedia.tokomart.common.animator.ExpandCollapseAnimator

abstract class BaseExpandableViewHolder<T : BaseExpandableUiModel<*>>(
    itemView: View
) : AbstractViewHolder<T>(itemView) {

    private val context by lazy { itemView.context }
    private val expandCollapseLayout by lazy { itemView.findViewById<View>(expandCollapseLayoutId()) }
    private val expandCollapseChevron by lazy { itemView.findViewById<ImageView?>(expandCollapseChevronId()) }

    private val animator by lazy {
        ExpandCollapseAnimator(
            expandCollapseLayout,
            expandAnimationListener(),
            collapseAnimationListener()
        )
    }

    override fun bind(data: T) {
        data.run {
            expandCollapseLayout.showWithCondition(isExpanded)

            itemView.setOnClickListener {
                if (isExpanded) {
                    isExpanded = false
                    animator.collapse()
                    setChevronIcon(com.tokopedia.iconunify.R.drawable.iconunify_chevron_down)
                } else {
                    isExpanded = true
                    animator.expand()
                    setChevronIcon(com.tokopedia.iconunify.R.drawable.iconunify_chevron_up)
                }
            }
        }
        setChevronIcon(com.tokopedia.iconunify.R.drawable.iconunify_chevron_down)
    }

    open fun expandCollapseLayoutId(): Int = R.id.expandCollapseLayout

    open fun expandCollapseChevronId(): Int = R.id.expandCollapseChevron

    open fun expandAnimationListener(): Animation.AnimationListener? = null

    open fun collapseAnimationListener(): Animation.AnimationListener? = null

    private fun setChevronIcon(@DrawableRes resId: Int) {
        expandCollapseChevron?.run {
            val drawable = ContextCompat.getDrawable(context, resId)
            drawable?.setTint(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700))
            setImageDrawable(ContextCompat.getDrawable(context, resId))
        }
    }
}