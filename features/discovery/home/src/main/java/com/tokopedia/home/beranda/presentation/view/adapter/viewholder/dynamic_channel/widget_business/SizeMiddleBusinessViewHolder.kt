package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.widget_business

import androidx.appcompat.widget.AppCompatImageView
import android.view.View
import android.widget.TextView
import com.tokopedia.home.R
import com.tokopedia.unifycomponents.CardUnify2

class SizeMiddleBusinessViewHolder (
    itemView: View,
    listener: BusinessUnitItemViewListener,
    cardInteraction: Int = CardUnify2.ANIMATE_OVERLAY
) : SizeSmallBusinessViewHolder(itemView, listener, cardInteraction) {

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
