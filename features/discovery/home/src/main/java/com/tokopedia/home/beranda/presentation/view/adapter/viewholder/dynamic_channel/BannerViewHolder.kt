package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.banner.BannerView
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel
import com.tokopedia.home.beranda.listener.ActivityStateListener
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.BannerViewModel
import com.tokopedia.home.beranda.presentation.view.analytics.HomeTrackingUtils
import com.tokopedia.home.beranda.presentation.view.customview.BannerViewDynamicBackground
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import java.util.*

/**
 * @author by errysuprayogi on 11/28/17.
 */

class BannerViewHolder(itemView: View, private val listener: HomeCategoryListener) : AbstractViewHolder<BannerViewModel>(itemView), BannerView.OnPromoClickListener, BannerView.OnPromoScrolledListener, BannerView.OnPromoAllClickListener, BannerView.OnPromoLoadedListener, BannerView.OnPromoDragListener, ActivityStateListener {
    private val bannerView: BannerViewDynamicBackground = itemView.findViewById(R.id.banner)
    private val context: Context = itemView.context
    private var slidesList: List<BannerSlidesModel>? = null

    init {
        bannerView.onPromoAllClickListener = this
        bannerView.onPromoClickListener = this
        bannerView.onPromoScrolledListener = this
        bannerView.setOnPromoLoadedListener(this)
        bannerView.setOnPromoDragListener(this)
        listener.setActivityStateListener(this)
    }

    override fun bind(element: BannerViewModel) {
        try {
            slidesList = element.slides
            slidesList?.let {
                if(!element.isCache) {
                    bannerView.addOnImpressionListener(
                            element, OnBannerImpressedListener(it, listener)
                    )
                }
                bannerView.shouldShowSeeAllButton(it.isNotEmpty())

                val promoUrls = ArrayList<String>()
                for (slidesModel in it) {
                    promoUrls.add(slidesModel.imageUrl)
                }
                bannerView.setPromoList(promoUrls)
                bannerView.buildView()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onPromoClick(position: Int) {
        slidesList?.let {
            if (it[position].type == BannerSlidesModel.TYPE_BANNER_PERSO) {
                HomePageTracking.eventPromoOverlayClick(context, it[position])
            } else {
                HomePageTracking.eventPromoClick(context, it[position])
            }
            listener.onPromoClick(position, it[position])
            HomeTrackingUtils.homeSlidingBannerClick(context, it[position], position)
        }
    }

    override fun onPromoScrolled(position: Int) {
        if (listener.isHomeFragment) {
            slidesList?.let {
                HomeTrackingUtils.homeSlidingBannerImpression(context, it[position], position)
                listener.onPromoScrolled(it[position])
            }
        }
    }

    override fun onPromoAllClick() {
        listener.onPromoAllClick()
    }

    override fun onPromoLoaded() {

    }

    override fun onPromoDragStart() {
        listener.onPromoDragStart()
    }

    override fun onPromoDragEnd() {
        listener.onPromoDragEnd()
    }

    override fun onPause() {
        bannerView.stopAutoScrollBanner()
    }

    override fun onResume() {
        bannerView.startAutoScrollBanner()
    }

    class OnBannerImpressedListener(private val bannerSlidesModel: List<BannerSlidesModel>,
                                                    private val listener: HomeCategoryListener) : ViewHintListener {
        override fun onViewHint() {
            val overlayBannerSlides = arrayListOf<BannerSlidesModel>()
            val generalBannerSlides = arrayListOf<BannerSlidesModel>()

            bannerSlidesModel.forEach{
                if (it.type == BannerSlidesModel.TYPE_BANNER_PERSO) {
                    overlayBannerSlides.add(it)
                } else {
                    generalBannerSlides.add(it)
                }
            }
            if (overlayBannerSlides.isNotEmpty()) {
                listener.putEEToTrackingQueue(HomePageTracking.getBannerOverlayPersoImpressionDataLayer(
                        overlayBannerSlides
                ))
            }
            if (generalBannerSlides.isNotEmpty()) {
                listener.putEEToTrackingQueue(HomePageTracking.getBannerImpressionDataLayer(
                        generalBannerSlides
                ))
            }
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_banner
        const val ATTRIBUTION = "attribution"
    }
}
