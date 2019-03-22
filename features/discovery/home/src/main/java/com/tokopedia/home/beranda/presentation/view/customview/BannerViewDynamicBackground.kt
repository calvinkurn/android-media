package com.tokopedia.home.beranda.presentation.view.customview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.design.banner.BannerPagerAdapter
import com.tokopedia.design.banner.BannerView
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.CardBannerPagerAdapter
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.HomeBannerViewDecorator
import kotlinx.android.synthetic.main.layout_card_banner_dynamic_background.view.*
import java.util.*

class BannerViewDynamicBackground : BannerView {

    var isUseCrossfade = true

    internal lateinit var img_banner_background: ImageView

    internal var currentBitmapDrawable: BitmapDrawable? = null


    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun init() {
        val view = View.inflate(context, R.layout.layout_card_banner_dynamic_background, this)

        bannerRecyclerView = view.findViewById(R.id.banner_recyclerview)
        bannerIndicator = view.findViewById(R.id.banner_indicator_container)
        bannerSeeAll = view.findViewById(R.id.banner_see_all)
        img_banner_background = view.findViewById(R.id.img_banner_background)
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

        ImageHandler.loadImageBlurWithViewTarget(
                context,
                url,
                getBitmapImageViewTarget()
        )
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
            internal var currentImagePosition = 0
            var oldImagePosition = 0

//            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
//                if (isUseCrossfade) {
//                    if (newState == RecyclerView.SCROLL_STATE_IDLE
//                            && currentImagePosition != currentPosition
//                            && currentPosition != -1) {
//                        setBackgroundImageCrossfade()
//                        currentImagePosition = currentPosition
//                    }
//                }
//            }

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if (!isUseCrossfade) {
                    val manager : LinearLayoutManager =
                            recyclerView!!.layoutManager as LinearLayoutManager
                    val position = manager.findFirstCompletelyVisibleItemPosition()
                    Log.d("Fikry pos", " "+position)
                    if (
                            position != currentImagePosition && position != -1) {

                        val url = promoImageUrls[position]

//                        ImageHandler.loadImageBlur(
//                                context,
//                                getBitmapImageViewTarget(img_banner_background.getImg1()),
//                                url
//                        )
                        oldImagePosition = currentImagePosition
                        currentImagePosition = position
                    }
                } else {
                    val manager : LinearLayoutManager =
                            recyclerView!!.layoutManager as LinearLayoutManager
                    val position = manager.findFirstCompletelyVisibleItemPosition()
                    if (
                            position != currentImagePosition && position != -1) {

                        val url = promoImageUrls[position]
                        val oldUrl = promoImageUrls[oldImagePosition]

                        ImageHandler.loadImageBlurWithViewTarget(
                                context,
                                url,
                                getBitmapImageViewTarget()
                        )
                        oldImagePosition = currentImagePosition
                        currentImagePosition = position
                    }
                }
            }
        })
    }

    fun getBitmapImageViewTarget() : SimpleTarget<Bitmap> {
        return object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                val blurredBitmap = ImageHandler.blurStrong(context, resource)
                showImage(blurredBitmap)
            }
        }
    }

    fun showImage(bitmap: Bitmap) {
        if (currentBitmapDrawable == null) {
            img_banner_background.setImageBitmap(bitmap)
            currentBitmapDrawable = BitmapDrawable(resources, bitmap)
        } else {
            val td = TransitionDrawable(arrayOf<Drawable>(currentBitmapDrawable!!, BitmapDrawable(resources, bitmap)))
            img_banner_background.setImageDrawable(td)
            td.startTransition(250)
            currentBitmapDrawable = BitmapDrawable(resources, bitmap)
        }
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
