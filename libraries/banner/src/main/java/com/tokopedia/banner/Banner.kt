package com.tokopedia.banner

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.design.banner.BannerPagerAdapter
import com.tokopedia.design.banner.BannerView

/**
 * Created by meta on 28/02/19.
 * Credit Devara Fikry
 */

class Banner : BannerView {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun init() {
        val view = View.inflate(context, R.layout.banner_layout, this)
        bannerRecyclerView = view.findViewById(R.id.banner_recyclerview)
        bannerIndicator = view.findViewById(R.id.banner_indicator_container)
        bannerSeeAll = view.findViewById(R.id.banner_see_all)
        indicatorItems = ArrayList()
        impressionStatusList = ArrayList()
        promoImageUrls = ArrayList()
    }

    override fun buildView() {
        super.buildView()
        if (bannerRecyclerView.itemDecorationCount.equals(0)) {
            bannerRecyclerView.addItemDecoration(
                    BannerDecorator(
                            context.resources.getDimensionPixelSize(R.dimen.dp_16),
                            context.resources.getDimensionPixelSize(R.dimen.dp_0),
                            context.resources.getDimensionPixelSize(R.dimen.dp_0),
                            context.resources.getDimensionPixelSize(R.dimen.dp_0))
            )
        }
    }

    override fun getBannerPagerAdapter(): BannerPagerAdapter {
        return BannerAdapter(promoImageUrls, onPromoClickListener)
    }

    override fun getIndicator(): Int {
        return R.drawable.banner_indicator_default
    }

    override fun getIndicatorFocus(): Int {
        return R.drawable.banner_indicator_focus
    }

    fun setBannerSeeAllTextColor(color: Int) {
        bannerSeeAll.setTextColor(color)
    }
}