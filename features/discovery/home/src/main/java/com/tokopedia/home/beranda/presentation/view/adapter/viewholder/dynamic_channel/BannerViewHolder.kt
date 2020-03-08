package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.HomeBannerAdapter
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.BannerViewModel
import com.tokopedia.home.beranda.presentation.view.analytics.HomeTrackingUtils
import com.tokopedia.home_page_banner.presentation.widgets.circularViewPager.CircularListener
import com.tokopedia.home_page_banner.presentation.widgets.circularViewPager.CircularModel
import com.tokopedia.home_page_banner.presentation.widgets.circularViewPager.CircularPageChangeListener
import com.tokopedia.home_page_banner.presentation.widgets.circularViewPager.CircularViewPager
import com.tokopedia.home_page_banner.presentation.widgets.pageIndicator.CircularPageIndicator
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.iris.util.*

/**
 * @author by errysuprayogi on 11/28/17.
 */

class BannerViewHolder(itemView: View, private val listener: HomeCategoryListener)
    : AbstractViewHolder<BannerViewModel>(itemView),
        CircularListener{
    private val context: Context = itemView.context
    private var slidesList: List<BannerSlidesModel>? = null
    private var isCache = true
    private val circularViewPager: CircularViewPager = itemView.findViewById(R.id.circular_view_pager)
    private val indicatorView: CircularPageIndicator = itemView.findViewById(R.id.indicator_banner)
    private val seeAllPromo: TextView = itemView.findViewById(R.id.see_all_promo)
    private val adapter = HomeBannerAdapter(listOf(), this)
    private val irisSession  = IrisSession(context)

    override fun bind(element: BannerViewModel) {
        try {
            slidesList = element.slides
            slidesList?.let {
                this.isCache = element.isCache
                initSeeAllPromo()
                initBanner(it.map{ CircularModel(it.id, it.imageUrl) })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun bind(element: BannerViewModel, payloads: MutableList<Any>) {
        try {
            slidesList = element.slides
            this.isCache = element.isCache
            element.slides?.let {
                circularViewPager.setItemList(it.map { CircularModel(it.id, it.imageUrl) })
                indicatorView.createIndicators(circularViewPager.indicatorCount, circularViewPager.indicatorPosition)
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun initSeeAllPromo(){
        seeAllPromo.setOnClickListener { onPromoAllClick() }
    }

    private fun initBanner(list: List<CircularModel>){
        circularViewPager.setIndicatorPageChangeListener(object: CircularViewPager.IndicatorPageChangeListener{
            override fun onIndicatorPageChange(newIndicatorPosition: Int) {
                indicatorView.animatePageSelected(newIndicatorPosition)
            }
        })

        circularViewPager.setPageChangeListener(object: CircularPageChangeListener{
            override fun onPageScrolled(position: Int) {
                onPromoScrolled(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                onPageDragStateChanged(state == CircularViewPager.SCROLL_STATE_DRAGGING)
            }
        })
        circularViewPager.setAdapter(adapter)
        circularViewPager.setItemList(list)
        indicatorView.createIndicators(circularViewPager.indicatorCount, circularViewPager.indicatorPosition)
    }

    override fun onClick(position: Int) {
        slidesList?.let {
            if(it.size > position) {
                if (it[position].type == BannerSlidesModel.TYPE_BANNER_PERSO) {
                    HomePageTracking.eventPromoOverlayClick(it[position])
                } else {
                    HomePageTracking.eventPromoClick(it[position])
                }
                listener.onPromoClick(position, it[position])
                HomeTrackingUtils.homeSlidingBannerClick(context, it[position], position)
            }
        }
    }

    private fun onPromoScrolled(position: Int) {
        if (listener.isHomeFragment) {
            slidesList?.let {
                HomeTrackingUtils.homeSlidingBannerImpression(context, it[position], position)
                listener.onPromoScrolled(it[position])
                if (it[position].type == BannerSlidesModel.TYPE_BANNER_PERSO &&
                        !it[position].isInvoke) {
                    listener.putEEToTrackingQueue(HomePageTracking.getBannerOverlayPersoImpressionDataLayer(
                            it[position]
                    ))
                } else if (!it[position].isInvoke) {
                    val dataLayer = HomePageTracking.getBannerImpressionDataLayer(
                            it[position]
                    )
                    dataLayer.put(KEY_SESSION_IRIS, irisSession.getSessionId())
                    listener.putEEToTrackingQueue(dataLayer)
                }
            }
        }
    }

    private fun onPageDragStateChanged(isDrag: Boolean) {
        listener.onPageDragStateChanged(isDrag)
    }

    private fun onPromoAllClick() {
        listener.onPromoAllClick()
    }

    fun onResume(){
        circularViewPager.resetImpressions()
        circularViewPager.resumeAutoScroll()
    }

    fun resetImpression(){
        circularViewPager.resetImpressions()
    }

    fun onPause(){
        circularViewPager.pauseAutoScroll()
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_banner
    }
}
