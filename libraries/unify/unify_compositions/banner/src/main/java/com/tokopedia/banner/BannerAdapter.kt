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
        return BannerViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.banner_item_layout, parent, false)
        )
    }
}

