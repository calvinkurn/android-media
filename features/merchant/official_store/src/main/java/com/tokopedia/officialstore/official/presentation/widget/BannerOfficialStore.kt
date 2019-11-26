package com.tokopedia.officialstore.official.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.tokopedia.banner.BannerView
import com.tokopedia.banner.BannerViewPagerAdapter
import com.tokopedia.officialstore.R
import java.util.*

class BannerOfficialStore(@NonNull context: Context, @Nullable attrs: AttributeSet) :
        BannerView(context, attrs) {

    override fun init() {
        val view = View.inflate(this.context, R.layout.widget_official_banner, this)
        this.bannerRecyclerView = view.findViewById(R.id.viewpager_banner_category)
        this.bannerIndicator = view.findViewById(R.id.indicator_banner_container)
        this.bannerSeeAll = view.findViewById(R.id.promo_link)
        this.indicatorItems = ArrayList()
        this.impressionStatusList = ArrayList()
        this.promoImageUrls = ArrayList()
    }

    override fun getIndicatorFocus(): Int {
        return R.drawable.indicator_official_banner_focus
    }

    override fun getBannerAdapter(): BannerViewPagerAdapter {
        return BannerOfficialStoreAdapter(promoImageUrls, onPromoClickListener)
    }

//    fun getAdapter(): BannerOfficialStoreAdapter {
//        return bannerAdapter as BannerOfficialStoreAdapter
//    }
}