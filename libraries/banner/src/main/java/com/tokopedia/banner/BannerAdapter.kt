package com.tokopedia.banner

import android.content.Context
import android.graphics.Point
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.tokopedia.design.banner.BannerPagerAdapter
import com.tokopedia.design.banner.BannerView
import kotlinx.android.synthetic.main.banner_item_layout.view.*

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

