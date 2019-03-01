package com.tokopedia.banner

import android.content.Context
import android.support.v7.widget.RecyclerView
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
                            context.resources.getDimensionPixelSize(R.dimen.dp_4),
                            context.resources.getDimensionPixelSize(R.dimen.dp_16),
                            context.resources.getDimensionPixelSize(R.dimen.dp_4))
            )
        }
        bannerRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var currentImagePosition = currentPosition

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && currentImagePosition != currentPosition
                        && currentPosition != -1) {
                    currentImagePosition = currentPosition
                }
            }
        })
    }

    fun setItems(bannerImageUrls: List<String>) {
        if (bannerPagerAdapter != null) {
            bannerPagerAdapter.clear()
            bannerPagerAdapter.setItems(bannerImageUrls)
            bannerPagerAdapter.notifyDataSetChanged()
        }
    }

    fun setOnItemClickListener(listener: OnPromoClickListener) {
        if (bannerPagerAdapter != null) {
            bannerPagerAdapter.setOnItemClickListener(listener)
        }
    }

    override fun getBannerPagerAdapter(): BannerPagerAdapter {
        return BannerAdapter(promoImageUrls, onPromoClickListener)
    }

    override fun getIndicator(): Int {
        return R.drawable.indicator_default
    }

    override fun getIndicatorFocus(): Int {
        return R.drawable.indicator_focus
    }

    fun getBannerSeeAll() : View {
        return bannerSeeAll
    }
}