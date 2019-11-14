package com.tokopedia.instantloan.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.instantloan.R
import com.tokopedia.instantloan.data.model.response.GqlLendingBannerData

class BannerPagerAdapter(private val context: Context, val bannerEntities: ArrayList<GqlLendingBannerData>, private val bannerClick: BannerClick) : PagerAdapter() {

    var bannerEntityList: ArrayList<GqlLendingBannerData> = bannerEntities
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
        val parentLinearLayout = mInflater.inflate(com.tokopedia.instantloan.R.layout.item_pager_banner, view, false) as LinearLayout
        val bannerImageView = parentLinearLayout.findViewById<ImageView>(R.id.image_banner_offer)
        parentLinearLayout.tag = bannerEntityList[position].bannerLink
        ImageHandler.LoadImage(bannerImageView, bannerEntityList[position].bannerImageUrl)
        view.addView(parentLinearLayout)
        parentLinearLayout.setOnClickListener { view1 -> bannerClick.onBannerClick(view1, position) }

        return parentLinearLayout
    }

    interface BannerClick {
        fun onBannerClick(view: View, position: Int)
    }
}
