package com.tokopedia.digital.home.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularListener
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularModel
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularViewHolder
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularViewPagerAdapter
import com.tokopedia.circular_view_pager.presentation.widgets.shimmeringImageView.ShimmeringImageView
import com.tokopedia.digital.home.R

class RechargeItemBannerAdapter(itemList: List<CircularModel>, listener: CircularListener) : CircularViewPagerAdapter(itemList, listener) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CircularViewHolder {
        return RechargeHomepageBannerImageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_recharge_home_banner_item, parent, false))
    }

    class RechargeHomepageBannerImageViewHolder(itemView: View): CircularViewHolder(itemView) {
        companion object{
            private const val FPM_HOMEPAGE_BANNER = "homepage_banner"
        }
        override fun bind(item: CircularModel, listener: CircularListener) {
            itemView.findViewById<ShimmeringImageView>(R.id.recharge_image_banner_homepage).tag = item.url
            itemView.findViewById<ShimmeringImageView>(R.id.recharge_image_banner_homepage).loadImage(item.url, FPM_HOMEPAGE_BANNER)
            itemView.findViewById<ShimmeringImageView>(R.id.recharge_image_banner_homepage).setOnClickListener { listener.onClick(adapterPosition) }
        }
    }
}