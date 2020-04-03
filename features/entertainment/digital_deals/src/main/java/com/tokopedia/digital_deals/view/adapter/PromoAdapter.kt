@file:JvmName("PromoAdapter")
@file:JvmMultifileClass

package com.tokopedia.digital_deals.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularListener
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularModel
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularViewHolder
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularViewPagerAdapter

class PromoAdapter(itemList: List<CircularModel>, listener: CircularListener) : CircularViewPagerAdapter(itemList, listener) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CircularViewHolder {
        return PromosViewHolder(LayoutInflater.from(parent.context).inflate(com.tokopedia.digital_deals.R.layout.promo_item, parent, false))
    }
}

class PromosViewHolder(itemView: View) : CircularViewHolder(itemView) {
    private val promoImage: ImageView = itemView.findViewById(com.tokopedia.digital_deals.R.id.banner_item)
    var index: Int = 0

    override fun bind(item: CircularModel, listener: CircularListener) {
        ImageHandler.loadImage(itemView.context, promoImage, item.url, com.tokopedia.design.R.color.grey_1100, com.tokopedia.design.R.color.grey_1100)
        promoImage.setOnClickListener {
            listener.onClick(adapterPosition)
        }
    }
}
