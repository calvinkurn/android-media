package com.tokopedia.tokomember_seller_dashboard.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.view.adapter.TokomemberIntroAdapterListener
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberIntroTextItem
import kotlinx.android.synthetic.main.tm_dash_intro_text_item.view.*

class TokomemberIntroTextVh(val listener: TokomemberIntroAdapterListener, view: View)
    : AbstractViewHolder<TokomemberIntroTextItem>(view) {

    private val tvSectionText = itemView.tvSection

    override fun bind(element: TokomemberIntroTextItem?) {
        element?.apply {
            tvSectionText.text = element.text
        }
        if (element != null) {
            listener.onItemDisplayed(element,adapterPosition)
        }
    }

    companion object {
        val LAYOUT_ID = R.layout.tm_dash_intro_text_item
    }

}
