package com.tokopedia.sellerhome.view.viewholder

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.tokopedia.banner.BannerView
import com.tokopedia.banner.BannerViewPagerAdapter
import com.tokopedia.sellerhome.R

class BannerCarousel(@NonNull context: Context, @Nullable attrs: AttributeSet) :
        BannerView(context, attrs) {

    override fun init() {
        val view = View.inflate(context, R.layout.sah_banner_layout, this)
        bannerRecyclerView = view.findViewById(R.id.rv_banner)
        bannerIndicator = view.findViewById(R.id.ll_indicator_container)
        bannerSeeAll = view.findViewById(R.id.tv_see_all)
        indicatorItems = ArrayList()
        impressionStatusList = ArrayList()
        promoImageUrls = ArrayList()
    }

    override fun getIndicatorFocus(): Int {
        return R.drawable.banner_green_indicator_focus
    }

    override fun getBannerAdapter(): BannerViewPagerAdapter {
        return BannerCarouselAdapter(promoImageUrls, onPromoClickListener)
    }
}