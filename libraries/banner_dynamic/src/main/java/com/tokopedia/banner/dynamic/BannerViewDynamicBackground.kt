package com.tokopedia.banner.dynamic

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.banner.Banner
import com.tokopedia.banner.BannerView
import com.tokopedia.banner.BannerViewPagerAdapter
import com.tokopedia.banner.dynamic.adapter.BannerDynamicPagerAdapter
import com.tokopedia.banner.dynamic.adapter.decorator.BannerDynamicViewDecorator
import com.tokopedia.banner.dynamic.util.ViewHelper
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show

/**
 * @author by furqan on 13/09/2019
 */
class BannerViewDynamicBackground @JvmOverloads constructor(context: Context, attrs: AttributeSet? =  null, defStyleAttr: Int  = 0)
    : BannerView(context, attrs, defStyleAttr) {

    private lateinit var imgBannerBackground: ImageView

    private lateinit var cardBannerView: Banner
    private lateinit var bannerRoot: View

    private var currentBitmapDrawable: BitmapDrawable? = null

    override fun init() {
        val view = View.inflate(context, R.layout.layout_card_banner_dynamic_background, this)
        view.findViewById<View>(R.id.overlay_round).background = MethodChecker.getDrawable(
                view.context, R.drawable.background_banner_image_mask)
        bannerRoot = view.findViewById(R.id.banner_root)
        bannerRecyclerView = view.findViewById(R.id.banner_recyclerview)
        bannerIndicator = view.findViewById(R.id.banner_indicator_container)
        imgBannerBackground = view.findViewById(R.id.img_banner_background)
        cardBannerView = view.findViewById(R.id.card_banner_view)
        bannerSeeAll = view.findViewById(R.id.banner_see_all)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (::cardBannerView.isInitialized) {
                cardBannerView.setMargin(
                        cardBannerView.left,
                        ViewHelper.getStatusBarHeight(context),
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
        bannerSeeAll.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G400));
        bannerSeeAll.typeface = Typeface.DEFAULT
        super.buildView()
        bannerRoot.visibility = View.VISIBLE
        val url = promoImageUrls[0]

        if (bannerRecyclerView.itemDecorationCount == 0) {
            bannerRecyclerView.addItemDecoration(
                    BannerDynamicViewDecorator(
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
                if (position != currentImagePosition && position != -1) {

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

    fun getBitmapImageViewTarget(): CustomTarget<Bitmap> {
        return object : CustomTarget<Bitmap>() {
            override fun onLoadCleared(placeholder: Drawable?) {

            }

            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                resource.run {
                    showImage(resource)
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

    override fun getBannerAdapter(): BannerViewPagerAdapter {
        return BannerDynamicPagerAdapter(promoImageUrls, onPromoClickListener)
    }

    override fun getIndicator(): Int {
        return R.drawable.ic_banner_dynamic_indicator
    }

    override fun getIndicatorFocus(): Int {
        return R.drawable.ic_banner_dynamic_indicator_focus
    }

    fun shouldShowSeeAllButton(show: Boolean) {
        if (show) {
            bannerSeeAll.show()
        } else {
            bannerSeeAll.hide()
        }
    }
}