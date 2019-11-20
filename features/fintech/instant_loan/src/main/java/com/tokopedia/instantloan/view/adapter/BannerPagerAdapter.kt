package com.tokopedia.instantloan.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.instantloan.data.model.response.GqlLendingBannerData

class BannerPagerAdapter(val bannerEntities: ArrayList<GqlLendingBannerData>,
                         private val bannerClick: BannerClick) : PagerAdapter() {


    override fun destroyItem(container: View, position: Int, any: Any) {
        (container as ViewPager).removeView(any as View)
    }

    override fun getCount(): Int {
        return bannerEntities.size
    }

    override fun isViewFromObject(view: View, any: Any): Boolean {
        return view == any
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val parentLinearLayout = LayoutInflater.from(view.context)
                .inflate(com.tokopedia.instantloan.R.layout.item_pager_banner, view, false) as LinearLayout
        val bannerImageView = parentLinearLayout.
                findViewById<ImageView>(com.tokopedia.instantloan.R.id.image_banner_offer)
        parentLinearLayout.tag = bannerEntities[position].bannerLink
        ImageHandler.LoadImage(bannerImageView, bannerEntities[position].bannerImageUrl)
        view.addView(parentLinearLayout)
        parentLinearLayout.setOnClickListener { view1 -> bannerClick.onBannerClick(view1, position) }

        return parentLinearLayout
    }

    interface BannerClick {
        fun onBannerClick(view: View, position: Int)
    }
}
