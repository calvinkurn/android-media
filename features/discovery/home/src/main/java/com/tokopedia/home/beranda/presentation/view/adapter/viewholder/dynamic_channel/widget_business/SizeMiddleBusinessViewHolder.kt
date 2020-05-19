package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.widget_business

import androidx.appcompat.widget.AppCompatImageView
import android.view.View
import android.widget.TextView
import com.tokopedia.home.R
import kotlinx.android.synthetic.main.layout_template_icon_business_widget.view.*
import kotlinx.android.synthetic.main.layout_template_mid_business.view.*

class SizeMiddleBusinessViewHolder (
        itemView: View,
        listener: BusinessUnitItemViewListener
) : SizeSmallBusinessViewHolder(itemView, listener) {

    companion object {
        val LAYOUT: Int = R.layout.layout_template_mid_business
    }

    override fun getProductName(): TextView {
        return itemView.productName
    }

    override fun getIcon(): AppCompatImageView {
        return itemView.icon
    }

    override fun getTitle(): TextView {
        return itemView.title
    }

    override fun getSubtitle(): TextView {
        return itemView.subtitle
    }

}
