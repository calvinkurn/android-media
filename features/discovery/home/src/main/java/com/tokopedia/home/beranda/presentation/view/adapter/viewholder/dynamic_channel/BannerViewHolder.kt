package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularListener
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularModel
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularPageChangeListener
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularViewPager
import com.tokopedia.circular_view_pager.presentation.widgets.pageIndicator.CircularPageIndicator
import com.tokopedia.home.R
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel
import com.tokopedia.home.beranda.helper.benchmark.TRACE_ON_BIND_BANNER_VIEWHOLDER
import com.tokopedia.home.beranda.helper.benchmark.BenchmarkHelper
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.HomeBannerAdapter
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomepageBannerDataModel

/**
 * @author by errysuprayogi on 11/28/17.
 */

class BannerViewHolder(itemView: View, private val listener: HomeCategoryListener)
    : AbstractViewHolder<HomepageBannerDataModel>(itemView),
        CircularListener {
    private var slidesList: List<BannerSlidesModel>? = null
    private var isCache = true
    private val circularViewPager: CircularViewPager = itemView.findViewById(R.id.circular_view_pager)
    private val indicatorView: CircularPageIndicator = itemView.findViewById(R.id.indicator_banner)
    private val seeAllPromo: TextView = itemView.findViewById(R.id.see_all_promo)
    private val adapter = HomeBannerAdapter(listOf(), this)

    override fun bind(element: HomepageBannerDataModel) {
        BenchmarkHelper.beginSystraceSection(TRACE_ON_BIND_BANNER_VIEWHOLDER)
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
        BenchmarkHelper.endSystraceSection()
    }

    override fun bind(element: HomepageBannerDataModel, payloads: MutableList<Any>) {
        BenchmarkHelper.beginSystraceSection(TRACE_ON_BIND_BANNER_VIEWHOLDER)
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
        BenchmarkHelper.endSystraceSection()
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

        circularViewPager.setPageChangeListener(object: CircularPageChangeListener {
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
                listener.onPromoClick(position, it[position])
            }
        }
    }

    private fun onPromoScrolled(position: Int) {
        if (listener.isMainViewVisible) {
            slidesList?.let {
                listener.onPromoScrolled(it[position])
                it[position].invoke()
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
        circularViewPager.reset()
    }

    fun onPause(){
        circularViewPager.pauseAutoScroll()
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_banner
    }
}
