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
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.banner_item_layout, parent, false)

        val layoutParams = itemView.layoutParams

        if (parent.width != 0 ) {
            layoutParams.width = (parent.width * 0.9).toInt()
        } else {
            val mWinMgr = itemView.context
                    .getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = mWinMgr.defaultDisplay
            val size = Point()
            display.getSize(size)
            val width = size.x

            layoutParams.width = (width * 0.9).toInt()
        }

        itemView.layoutParams = layoutParams
        itemView.banner_card.visibility = View.VISIBLE

        return BannerViewHolder(itemView)
    }
}

