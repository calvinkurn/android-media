package com.tokopedia.entertainment.home.widget

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.tokopedia.banner.BannerView
import com.tokopedia.banner.BannerViewPagerAdapter
import com.tokopedia.entertainment.R
import com.tokopedia.officialstore.official.presentation.widget.EventHomeBannerAdapter

/**
 * Author errysuprayogi on 27,January,2020
 */
class EventHomeBannerView(@NonNull context: Context, @Nullable attrs: AttributeSet) : BannerView(context, attrs) {

//    override fun init() {
//        val view = View.inflate(this.context, R.layout.ent_banner_view, this)
//        this.bannerRecyclerView = view.findViewById(R.id.viewpager_banner_category)
//        this.bannerIndicator = view.findViewById(R.id.indicator_banner_container)
//        this.bannerSeeAll = view.findViewById(R.id.promo_link)
//        this.indicatorItems = ArrayList()
//        this.impressionStatusList = ArrayList()
//        this.promoImageUrls = ArrayList()
//    }

    override fun getIndicatorFocus(): Int {
        return R.drawable.indicator_entertainment_banner_focus
    }

    override fun getBannerAdapter(): BannerViewPagerAdapter {
        return EventHomeBannerAdapter(promoImageUrls, onPromoClickListener)
    }
}