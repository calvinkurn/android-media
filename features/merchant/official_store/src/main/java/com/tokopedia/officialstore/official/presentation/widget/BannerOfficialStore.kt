package com.tokopedia.officialstore.official.presentation.widget

import android.content.Context
import android.support.annotation.NonNull
import android.support.annotation.Nullable
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.banner.BannerView
import com.tokopedia.officialstore.R
import java.util.*

class BannerOfficialStore(@NonNull context: Context, @Nullable attrs: AttributeSet) :
        BannerView(context, attrs) {

    override fun init() {
        val view = View.inflate(this.context, R.layout.widget_official_banner, this)
        this.bannerRecyclerView = view.findViewById<View>(R.id.viewpager_banner_category) as RecyclerView
        this.bannerIndicator = view.findViewById<View>(R.id.indicator_banner_container) as ViewGroup
        this.bannerSeeAll = view.findViewById<View>(R.id.promo_link) as TextView
        this.indicatorItems = ArrayList()
        this.impressionStatusList = ArrayList()
        this.promoImageUrls = ArrayList()
    }

    override fun getIndicatorFocus(): Int {
        return R.drawable.indicator_official_banner_focus
    }
}