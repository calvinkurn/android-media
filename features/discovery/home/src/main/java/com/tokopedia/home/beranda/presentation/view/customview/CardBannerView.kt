package com.tokopedia.home.beranda.presentation.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

import com.tokopedia.design.banner.BannerPagerAdapter
import com.tokopedia.design.banner.BannerView
import com.tokopedia.home.beranda.presentation.view.adapter.CardBannerPagerAdapter
import com.tokopedia.home.R

import java.util.ArrayList

class CardBannerView : BannerView {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun init() {
        val view = View.inflate(context, R.layout.card_banner_view, this)
        bannerRecyclerView = view.findViewById(R.id.viewpager_banner_category)
        bannerIndicator = view.findViewById<ViewGroup>(R.id.indicator_banner_container)
        bannerSeeAll = view.findViewById(R.id.promo_link)
        indicatorItems = ArrayList()
        impressionStatusList = ArrayList()
        promoImageUrls = ArrayList()
    }

    override fun getBannerAdapter(): BannerPagerAdapter {
        return CardBannerPagerAdapter(promoImageUrls, onPromoClickListener)
    }

    override fun getIndicator(): Int {
        return R.drawable.home_indicator
    }

    override fun getIndicatorFocus(): Int {
        return R.drawable.home_indicator_focus
    }
}
