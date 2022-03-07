package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.widget_business

import androidx.appcompat.widget.AppCompatImageView
import android.view.View
import android.widget.TextView
import com.tokopedia.home.R

class SizeMiddleBusinessViewHolder (
        itemView: View,
        listener: BusinessUnitItemViewListener
) : SizeSmallBusinessViewHolder(itemView, listener) {

    companion object {
        val LAYOUT: Int = R.layout.layout_template_mid_business
    }

    override fun getProductName(): TextView {
        return itemView.findViewById(R.id.productName)
    }

    override fun getIcon(): AppCompatImageView {
        return itemView.findViewById(R.id.icon)
    }

    override fun getTitle(): TextView {
        return itemView.findViewById(R.id.title)
    }

    override fun getSubtitle(): TextView {
        return itemView.findViewById(R.id.subtitle)
    }

}
