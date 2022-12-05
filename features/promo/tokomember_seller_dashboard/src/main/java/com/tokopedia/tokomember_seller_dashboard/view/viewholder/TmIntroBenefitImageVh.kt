package com.tokopedia.tokomember_seller_dashboard.view.viewholder

import android.animation.Animator
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberIntroBenefitImageItem
import com.tokopedia.tokomember_seller_dashboard.view.animation.TmIntroItemAnimator
import kotlinx.android.synthetic.main.tm_dash_intro_benefit_image_item.view.*

class TmIntroBenefitImageVh(val view: View)
    : AbstractViewHolder<TokomemberIntroBenefitImageItem>(view) {

    private val tmIntroVideoView = itemView.ivSection
    private var holderElement: TokomemberIntroBenefitImageItem?=null

    override fun bind(element: TokomemberIntroBenefitImageItem?) {
        holderElement = element
        holderElement?.apply {
            tmIntroVideoView?.loadImage(element?.imgUrl)
        }
    }

    fun setAnimationLeftToRight(itemView: View, adapterPosition: Int, ON_ATTACH: Boolean) {
        if (holderElement?.isAnimationFinished==false) {
            TmIntroItemAnimator.fromLeftToRight(
                itemView,
                adapterPosition,
                ON_ATTACH,
                animListener
            )
        }
    }

    fun setAnimationRightToLeft(itemView: View, adapterPosition: Int, ON_ATTACH: Boolean) {
        if (holderElement?.isAnimationFinished==false) {
            TmIntroItemAnimator.fromRightToLeft(
                itemView,
                adapterPosition,
                ON_ATTACH,
                animListener
            )
        }
    }

    private val animListener = object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) {
        }

        override fun onAnimationEnd(animation: Animator) {
            animation?.removeAllListeners()
            holderElement?.isAnimationFinished = true
        }

        override fun onAnimationCancel(animation: Animator) {
            animation?.removeAllListeners()
        }

        override fun onAnimationRepeat(animation: Animator) {
        }
    }

    companion object {
        val LAYOUT_ID = R.layout.tm_dash_intro_benefit_image_item
    }
}
