package com.tokopedia.home.beranda.presentation.view.customview

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.design.banner.BannerPagerAdapter
import com.tokopedia.design.banner.BannerView
import com.tokopedia.home.beranda.presentation.view.adapter.CardBannerPagerAdapter
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.HomeBannerViewDecorator

import java.util.ArrayList

class BannerViewDynamicBackground : BannerView {

    var isUseCrossfade = true;

    internal lateinit var img_banner_background: ImageView

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun init() {
        val view = View.inflate(context, R.layout.layout_card_banner_dynamic_background, this)
        bannerRecyclerView = view.findViewById(R.id.viewpager_banner_category)
        bannerIndicator = view.findViewById<ViewGroup>(R.id.indicator_banner_container)
        bannerSeeAll = view.findViewById(R.id.promo_link)
        img_banner_background = view.findViewById(R.id.img_banner_background)
        indicatorItems = ArrayList()
        impressionStatusList = ArrayList()
        promoImageUrls = ArrayList()
    }

    override fun buildView() {
        super.buildView()
        setBackgroundImage()
        if (bannerRecyclerView.itemDecorationCount == 0) {
            bannerRecyclerView.addItemDecoration(
                    HomeBannerViewDecorator(
                            context.resources.getDimensionPixelSize(R.dimen.dp_16),
                            context.resources.getDimensionPixelSize(R.dimen.dp_2),
                            context.resources.getDimensionPixelSize(R.dimen.dp_16),
                            context.resources.getDimensionPixelSize(R.dimen.dp_2))
            )
        }
        bannerSeeAll.setOnClickListener {
            isUseCrossfade = !isUseCrossfade
        }
        bannerRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            internal var currentImagePosition = currentPosition

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && currentImagePosition != currentPosition
                        && currentPosition != -1) {
                    if (isUseCrossfade) {
                        setBackgroundImageCrossfade()
                    } else {
                        setBackgroundImage()
                    }
                    currentImagePosition = currentPosition
                }
            }
        })
    }

    private fun setBackgroundImage() {
        val url = promoImageUrls[currentPosition]
        ImageHandler.loadImageBlur(
                context,
                img_banner_background,
                url
        )
    }

    private fun setBackgroundImageCrossfade() {
        val url = promoImageUrls[currentPosition]
        ImageHandler.loadImageBlurCrossfade(
                context,
                img_banner_background,
                url
        )
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
