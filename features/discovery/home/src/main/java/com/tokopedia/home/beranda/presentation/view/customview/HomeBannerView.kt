package com.tokopedia.home.beranda.presentation.view.customview

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.design.banner.BannerPagerAdapter
import com.tokopedia.design.banner.BannerView
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.HomeBannerPagerAdapter
import kotlinx.android.synthetic.main.widget_banner_home.view.*

class HomeBannerView(context : Context) : BannerView(context) {
    override fun inflateView(): View {
        return View.inflate(context, R.layout.widget_banner_home, this);
    }

    override fun buildView() {
        super.buildView()
        bannerRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val url = promoImageUrls.get(currentPosition)
                ImageHandler.loadImageBlur(
                        context,
                        img_banner_background,
                        url
                )
            }
        })
    }

    override fun getBannerAdapter(): BannerPagerAdapter {
        return HomeBannerPagerAdapter(promoImageUrls, onPromoClickListener);
    }
}