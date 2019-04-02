package com.tokopedia.instantloan.view.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.instantloan.R
import com.tokopedia.instantloan.data.model.response.BannerEntity

class BannerPagerAdapter(private val context: Context, val bannerEntities: List<BannerEntity>, private val bannerClick: BannerClick) : PagerAdapter() {

    var bannerEntityList: List<BannerEntity> = bannerEntities
    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun destroyItem(container: View, position: Int, any: Any) {
        (container as ViewPager).removeView(any as View)
    }

    override fun getCount(): Int {
        return bannerEntityList.size
    }

    override fun isViewFromObject(view: View, any: Any): Boolean {
        return view == any
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val banner = mInflater.inflate(R.layout.item_pager_banner, view, false) as ImageView
        ImageHandler.LoadImage(banner, bannerEntityList[position].image)
        view.addView(banner)

        banner.tag = bannerEntityList[position].link
        banner.setOnClickListener { view1 -> bannerClick.onBannerClick(view1, position) }

        return banner
    }

    interface BannerClick {
        fun onBannerClick(view: View, position: Int)
    }
}
