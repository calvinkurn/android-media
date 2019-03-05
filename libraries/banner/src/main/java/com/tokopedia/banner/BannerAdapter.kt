package com.tokopedia.banner

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.design.banner.BannerPagerAdapter
import com.tokopedia.design.banner.BannerView

/**
 * Created by meta on 28/02/19.
 * Credit Devara Fikry
 */

class BannerAdapter(bannerImageUrls : List<String>,
                    onPromoClickListener : BannerView.OnPromoClickListener) :
        BannerPagerAdapter(bannerImageUrls, onPromoClickListener) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.banner_item_layout, parent, false)

        val layoutParams = itemView.getLayoutParams()
        layoutParams.width = (parent.width * 0.9).toInt()
        itemView.setLayoutParams(layoutParams)

        return BannerViewHolder(itemView)
    }
}

