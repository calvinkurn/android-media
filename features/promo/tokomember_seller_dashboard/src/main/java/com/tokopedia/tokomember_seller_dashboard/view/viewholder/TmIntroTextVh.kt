package com.tokopedia.tokomember_seller_dashboard.view.viewholder

import android.animation.Animator
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberIntroTextItem
import com.tokopedia.tokomember_seller_dashboard.view.animation.TmIntroItemAnimator
import kotlinx.android.synthetic.main.tm_dash_intro_text_item.view.*

class TmIntroTextVh(view: View) :
    AbstractViewHolder<TokomemberIntroTextItem>(view) {

    private val tvSectionText = itemView.tvSection
    private var holderElement:TokomemberIntroTextItem?=null

    override fun bind(element: TokomemberIntroTextItem?) {
        holderElement = element
        holderElement?.apply {
            tvSectionText.text = element?.text
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
        val LAYOUT_ID = R.layout.tm_dash_intro_text_item
    }

}
