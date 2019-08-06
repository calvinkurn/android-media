package com.tokopedia.travel.homepage.presentation.customview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils.getStatusBarHeight
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.banner.Banner
import com.tokopedia.design.banner.BannerPagerAdapter
import com.tokopedia.design.banner.BannerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.travel.homepage.R
import com.tokopedia.travel.homepage.presentation.adapter.TravelHomepageBannerPagerAdapter
import com.tokopedia.travel.homepage.presentation.adapter.itemdecoration.TravelHomepageBannerViewDecorator
import kotlinx.android.synthetic.main.travel_homepage_card_banner_dynamic_background.view.*

class TravelHomepageBannerViewDynamicBackground : BannerView {

    private lateinit var imgBannerBackground: ImageView

    private lateinit var cardBannerView: Banner

    private var currentBitmapDrawable: BitmapDrawable? = null

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun init() {
        val view = View.inflate(context, R.layout.travel_homepage_card_banner_dynamic_background, this)
        view.findViewById<View>(R.id.overlay_round)
                .setBackground(MethodChecker.getDrawable(
                        view.getContext(), R.drawable.background_banner_image_mask))
        bannerRecyclerView = view.findViewById(R.id.banner_recyclerview)
        bannerIndicator = view.findViewById(R.id.banner_indicator_container)
        bannerSeeAll = view.findViewById(R.id.banner_see_all)
        imgBannerBackground = view.findViewById(R.id.img_banner_background)
        cardBannerView = view.findViewById(R.id.card_banner_view)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (::cardBannerView.isInitialized) {
                cardBannerView.setMargin(
                        cardBannerView.left,
                        getStatusBarHeight(context),
                        cardBannerView.right,
                        cardBannerView.bottom
                )
            }
        } else {
            if (::cardBannerView.isInitialized) {
                cardBannerView.setMargin(
                        cardBannerView.left,
                        cardBannerView.top,
                        cardBannerView.right,
                        cardBannerView.bottom
                )
            }
        }

        indicatorItems = ArrayList()
        impressionStatusList = ArrayList()
        promoImageUrls = ArrayList()
    }

    override fun buildView() {
        bannerSeeAll.setTextColor(ContextCompat.getColor(context, R.color.medium_green));
        bannerSeeAll.typeface = Typeface.DEFAULT
        super.buildView()
        banner_root.visibility = View.VISIBLE
        val url = promoImageUrls[0]

        if (bannerRecyclerView.itemDecorationCount == 0) {
            bannerRecyclerView.addItemDecoration(
                    TravelHomepageBannerViewDecorator(
                            context.resources.getDimensionPixelSize(R.dimen.dp_16),
                            context.resources.getDimensionPixelSize(R.dimen.dp_2),
                            context.resources.getDimensionPixelSize(R.dimen.dp_16),
                            context.resources.getDimensionPixelSize(R.dimen.dp_2))
            )
        }

        //render first image on banner
        ImageHandler.loadImageBlurWithViewTarget(
                context.applicationContext,
                url,
                getBitmapImageViewTarget()
        )

        bannerRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var currentImagePosition = 0
            var oldImagePosition = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val manager: LinearLayoutManager =
                        recyclerView.layoutManager as LinearLayoutManager
                val position = manager.findFirstCompletelyVisibleItemPosition()
                if (
                        position != currentImagePosition && position != -1) {

                    val url = promoImageUrls[position]
                    ImageHandler.loadImageBlurWithViewTarget(
                            context.applicationContext,
                            url,
                            getBitmapImageViewTarget()
                    )
                    oldImagePosition = currentImagePosition
                    currentImagePosition = position
                }
            }
        })
    }

    fun getBitmapImageViewTarget(): SimpleTarget<Bitmap> {
        return object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(blurredBitmap: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                blurredBitmap?.run {
                    showImage(blurredBitmap)
                }
            }
        }
    }

    fun showImage(bitmap: Bitmap) {
        if (currentBitmapDrawable == null && ::imgBannerBackground.isInitialized) {
            imgBannerBackground.setImageBitmap(bitmap)
            currentBitmapDrawable = BitmapDrawable(resources, bitmap)
        } else if (::imgBannerBackground.isInitialized) {
            val td = TransitionDrawable(arrayOf<Drawable>(currentBitmapDrawable!!, BitmapDrawable(resources, bitmap)))
            imgBannerBackground.setImageDrawable(td)
            td.startTransition(250)
            currentBitmapDrawable = BitmapDrawable(resources, bitmap)
        }
    }

    override fun getBannerAdapter(): BannerPagerAdapter {
        return TravelHomepageBannerPagerAdapter(promoImageUrls, onPromoClickListener)
    }

    override fun getIndicator(): Int {
        return R.drawable.home_indicator
    }

    override fun getIndicatorFocus(): Int {
        return R.drawable.home_indicator_focus
    }

    fun shouldShowSeeAllButton(show: Boolean) {
        if (show) {
            bannerSeeAll.show()
        } else {
            bannerSeeAll.hide()
        }
    }
}
