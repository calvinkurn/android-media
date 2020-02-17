package com.tokopedia.tokopoints.view.cataloglisting

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.model.CatalogBanner

class CatalogBannerPagerAdapter(context: Context?, private val mItems: List<CatalogBanner>, presenter: CatalogListingPresenter) : PagerAdapter() {
    private val mInflater: LayoutInflater
    private val mPresenter: CatalogListingPresenter
    override fun destroyItem(container: View, position: Int, `object`: Any) {
        (container as ViewPager).removeView(`object` as View)
    }

    override fun getCount(): Int {
        return mItems.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val banner = mInflater.inflate(R.layout.tp_item_catalog_banner, view, false) as ImageView
        ImageHandler.loadImageFit2(banner.context, banner, mItems[position].imageUrl)
        banner.setOnClickListener { view1: View? ->
            if (URLUtil.isValidUrl(mItems[position].redirectUrl)) {
                mPresenter.view!!.openWebView(mItems[position].redirectUrl)
            } else {
                if (mPresenter.view!!.activityContext != null) {
                    RouteManager.route(mPresenter.view!!.activityContext, mItems[position].redirectUrl)
                }
            }
        }
        view.addView(banner)
        return banner
    }

    init {
        mInflater = LayoutInflater.from(context)
        mPresenter = presenter
    }
}