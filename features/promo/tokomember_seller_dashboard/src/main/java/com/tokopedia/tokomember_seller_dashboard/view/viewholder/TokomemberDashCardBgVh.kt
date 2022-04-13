package com.tokopedia.tokomember_seller_dashboard.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberProgramBgItem
import kotlinx.android.synthetic.main.tm_dash_colorbg_item.view.*

class TokomemberDashCardBgVh(val view: View)
    : AbstractViewHolder<TokomemberProgramBgItem>(view) {

    private val tmProgramColorBg = itemView.colorBg

    override fun bind(element: TokomemberProgramBgItem?) {
        element?.apply {
            tmProgramColorBg.loadImage(imageUrl)
        }
    }

    companion object {
        val LAYOUT_ID = R.layout.tm_dash_colorbg_item
    }
}