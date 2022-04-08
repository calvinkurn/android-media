package com.tokopedia.tokomember_seller_dashboard.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberIntroHeaderItem
import kotlinx.android.synthetic.main.tm_dash_intro_header.view.*

class TokomemberIntroHeaderVh(val view: View)
    : AbstractViewHolder<TokomemberIntroHeaderItem>(view) {

    private val tvHeaderItemTitle = itemView.tvTitle
    private val tvHeaderItemDescription = itemView.tvSubtitle

    override fun bind(element: TokomemberIntroHeaderItem?) {
        element?.apply {
            tvHeaderItemTitle.text = element.title
            tvHeaderItemDescription.text = element.description
        }
    }

    companion object {
        val LAYOUT_ID = R.layout.tm_dash_intro_header
    }
}
