package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.circularsliderbanner

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.tokopedia.applink.RouteManager
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularListener
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularModel
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularPageChangeListener
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularViewPager
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularViewPager.IndicatorPageChangeListener
import com.tokopedia.circular_view_pager.presentation.widgets.pageIndicator.CircularPageIndicator
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifyprinciples.Typography


class CircularSliderBannerViewHolder(itemView: View, val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner), CircularListener {

    private val sliderBannerTitle: Typography = itemView.findViewById(R.id.title)
    private lateinit var sliderBannerViewModel: CircularSliderBannerViewModel
    private lateinit var bannerCircularAdapter: BannerCircularAdapter
    private val cvSliderBanner: CircularViewPager = itemView.findViewById(R.id.circular_slider_banner)
    private val sliderIndicator: CircularPageIndicator = itemView.findViewById(R.id.indicator_banner)

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        sliderBannerViewModel = discoveryBaseViewModel as CircularSliderBannerViewModel
        sliderBannerViewModel.getItemsList()?.let {
            bannerCircularAdapter = BannerCircularAdapter(it, this)
            cvSliderBanner.setAdapter(bannerCircularAdapter)
            setSlideProperties(it)
            setUpIndicator(it)
        }
        sendFirstBannerImpression()
        sendBannerImpression()
    }

    private fun setSlideProperties(item: ArrayList<CircularModel>) {
        bannerCircularAdapter.setIsInfinite(item.size > 1)
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let {
            sliderBannerViewModel.getTitleLiveData().observe(it, Observer { item ->
                sliderBannerTitle.setTextAndCheckShow(item)
            })
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let { it ->
            sliderBannerViewModel.getTitleLiveData().removeObservers(it)
        }
    }

    private fun setUpIndicator(item: ArrayList<CircularModel>) {
        if (item.size > 1) {
            sliderIndicator.show()
            setBannerIndicatorAnimation()
        } else {
            sliderIndicator.hide()
        }
    }

    private fun setBannerIndicatorAnimation() {
        cvSliderBanner.setIndicatorPageChangeListener(object : IndicatorPageChangeListener {
            override fun onIndicatorPageChange(newIndicatorPosition: Int) {
                sliderIndicator.animatePageSelected(newIndicatorPosition)
            }
        })
        sliderIndicator.createIndicators(cvSliderBanner.indicatorCount, cvSliderBanner.indicatorPosition)
    }

    private fun sendFirstBannerImpression() {
        trackBannerImpressionGTM(0)
    }

    private fun sendBannerImpression() {
        cvSliderBanner.setPageChangeListener(object : CircularPageChangeListener {
            override fun onPageScrolled(position: Int) {
                trackBannerImpressionGTM(position)
            }
            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    fun trackBannerImpressionGTM(position : Int = 0){
        sliderBannerViewModel.getBannerItem(position)?.let {
            (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackBannerImpression(arrayListOf(it), sliderBannerViewModel.getComponentPosition())
        }
    }

    override fun onClick(position: Int) {
        sliderBannerViewModel.getBannerItem(position)?.let {
            (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackBannerClick(it, sliderBannerViewModel.getComponentPosition())
            if (!it.applinks.isNullOrEmpty()) RouteManager.route(itemView.context, it.applinks)
        }
    }
}