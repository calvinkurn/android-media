package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.BannerViewModel
import com.tokopedia.home.beranda.presentation.view.analytics.HomeTrackingUtils
import com.tokopedia.home_page_banner.presenter.handler.HomePageBannerListener
import com.tokopedia.home_page_banner.presenter.widgets.HomePageBannerView
import java.util.*

/**
 * @author by errysuprayogi on 11/28/17.
 */

class BannerViewHolder(itemView: View, private val listener: HomeCategoryListener)
    : AbstractViewHolder<BannerViewModel>(itemView),
        HomePageBannerListener{
    private val context: Context = itemView.context
    private var slidesList: List<BannerSlidesModel>? = null
    private var isCache = true

    override fun bind(element: BannerViewModel) {
        try {
            slidesList = element.slides
            slidesList?.let {
                this.isCache = element.isCache

                val promoUrls = ArrayList<String>()
                for (slidesModel in it) {
                    promoUrls.add(slidesModel.imageUrl)
                }
                val bannerView = itemView.findViewById<HomePageBannerView>(R.id.home_page_banner)
                bannerView?.showSeeAllPromo(it.isNotEmpty())
                bannerView?.buildView(promoUrls)
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

                if (!isCache) {
                    if (it[position].type == BannerSlidesModel.TYPE_BANNER_PERSO &&
                            !it[position].isInvoke) {
                        listener.putEEToTrackingQueue(HomePageTracking.getBannerOverlayPersoImpressionDataLayer(
                                it[position]
                        ))
                        it[position].invoke()
                    } else if (!it[position].isInvoke) {
                        listener.putEEToTrackingQueue(HomePageTracking.getBannerImpressionDataLayer(
                                it[position]
                        ))
                        it[position].invoke()
                    }
                }
            }
        }
    }

    override fun onPromoAllClick() {
        listener.onPromoAllClick()
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_banner
    }
}
