package com.tokopedia.tokomember_seller_dashboard.view.viewholder

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.view.adapter.TokomemberIntroAdapterListener
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberIntroTextItem
import kotlinx.android.synthetic.main.tm_dash_intro_text_item.view.*

class TokomemberIntroTextVh(val listener: TokomemberIntroAdapterListener, view: View)
    : AbstractViewHolder<TokomemberIntroTextItem>(view) {
    private var  lastPosition = -1

    private val tvSectionText = itemView.tvSection

    override fun bind(element: TokomemberIntroTextItem?) {
        element?.apply {
            tvSectionText.text = element.text
        }
        if (element != null) {
            listener.onItemDisplayed(element,adapterPosition)
        }
    }

     fun setAnimation() {
            val animType = getAnimationLeftOrRight()
            val animation: Animation =
                AnimationUtils.loadAnimation(itemView.context, animType)
            this@TokomemberIntroTextVh.itemView.startAnimation(animation)
    }

    private fun getAnimationLeftOrRight(): Int {
        return if (adapterPosition % 2 == 0) {
            R.anim.tm_dash_intro_benefit_right
        } else {
            R.anim.tm_dash_intro_benefit_left
        }
    }

    companion object {
        val LAYOUT_ID = R.layout.tm_dash_intro_text_item
    }

}
