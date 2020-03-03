package com.tokopedia.shop.home.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.tokopedia.banner.BannerView
import com.tokopedia.banner.BannerViewPagerAdapter
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.adapter.ShopHomeSliderBannerAdapter

/**
 * Created by rizqiaryansa on 2020-02-24.
 */

class BannerShopPage(@NonNull context: Context, @Nullable attrs: AttributeSet) :
        BannerView(context, attrs) {

    override fun init() {
        val view = View.inflate(this.context, R.layout.widget_slider_banner, this)
        this.bannerRecyclerView = view.findViewById(R.id.viewpager_slider_banner)
        this.bannerIndicator = view.findViewById(R.id.indicator_slider_banner_container)
        this.bannerSeeAll = view.findViewById(R.id.promo_link_slider_banner)
        this.bannerSeeAll.gone()
        this.indicatorItems = ArrayList()
        this.impressionStatusList = ArrayList()
        this.promoImageUrls = ArrayList()
    }

    override fun getIndicatorFocus(): Int {
        return R.drawable.indicator_slider_banner
    }

    override fun getBannerAdapter(): BannerViewPagerAdapter {
        return ShopHomeSliderBannerAdapter(this.promoImageUrls, this.onPromoClickListener)
    }
}