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

import java.util.ArrayList

class BannerPagerAdapter(context: Context, bannerEntities: List<BannerEntity>, private val bannerClick: BannerClick) : PagerAdapter() {

    var bannerEntityList: List<BannerEntity> = ArrayList()
    private val mInflater: LayoutInflater

    init {
        this.bannerEntityList = bannerEntities
        mInflater = LayoutInflater.from(context)
    }

    override fun destroyItem(container: View, position: Int, `object`: Any) {
        (container as ViewPager).removeView(`object` as View)
    }

    override fun getCount(): Int {
        return bannerEntityList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
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
