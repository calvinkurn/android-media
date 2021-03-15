package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
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
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.PageControl

/**
 * @author by errysuprayogi on 11/28/17.
 */

class BannerViewHolder(itemView: View, private val listener: HomeCategoryListener?)
    : AbstractViewHolder<HomepageBannerDataModel>(itemView),
        CircularListener {
    private var slidesList: List<BannerSlidesModel>? = null
    private var isCache = true
    private val circularViewPager: CircularViewPager = itemView.findViewById(R.id.circular_view_pager)
    private val indicatorView: PageControl = itemView.findViewById(R.id.indicator_banner)
    private val seeAllPromo: Label = itemView.findViewById(R.id.see_more_label)
    private val adapter = HomeBannerAdapter(listOf(), this)

    init {
        indicatorView.activeColor = ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N0)
        indicatorView.inactiveColor = ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N0_32)
        seeAllPromo.unlockFeature = true
        val labelColorHexString = "#${Integer.toHexString(ContextCompat.getColor(itemView.context, R.color.home_dms_color_banner_label_type))}"
        seeAllPromo.setLabelType(labelColorHexString)
        seeAllPromo.opacityLevel = 0.9f
    }

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
                if (it.size > 5) {
                    indicatorView.setIndicator(it.size)
                } else {
                    indicatorView.setIndicator(it.size)
                }
                circularViewPager.setItemList(it.map { CircularModel(it.id, it.imageUrl) })
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
                indicatorView.setCurrentIndicator(newIndicatorPosition)
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
        if (list.size > 5) {
            indicatorView.setIndicator(list.size)
        } else {
            indicatorView.setIndicator(list.size)
        }
    }

    override fun onClick(position: Int) {
        slidesList?.let {
            if(it.size > position) {
                listener?.onPromoClick(position, it[position])
            }
        }
    }

    private fun onPromoScrolled(position: Int) {
        if (listener?.isMainViewVisible?:false && !isCache) {
            slidesList?.let {
                listener?.onPromoScrolled(it[position])
                it[position].invoke()
            }
        }
    }

    private fun onPageDragStateChanged(isDrag: Boolean) {
        listener?.onPageDragStateChanged(isDrag)
    }

    private fun onPromoAllClick() {
        listener?.onPromoAllClick()
    }

    fun onResume(){
        circularViewPager.resetScrollToStart()
        circularViewPager.resumeAutoScroll()
    }

    fun resetImpression(){
        circularViewPager.resetScrollToStart()
    }

    fun onPause(){
        circularViewPager.pauseAutoScroll()
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_banner
    }
}
