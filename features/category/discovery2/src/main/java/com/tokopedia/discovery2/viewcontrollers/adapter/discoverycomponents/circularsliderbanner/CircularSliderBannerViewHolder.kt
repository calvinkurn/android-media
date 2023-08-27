package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.circularsliderbanner

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.applink.RouteManager
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularListener
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularModel
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularPageChangeListener
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularViewPager
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularViewPager.IndicatorPageChangeListener
import com.tokopedia.circular_view_pager.presentation.widgets.pageIndicator.CircularPageIndicator
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.Utils.Companion.routingBasedOnMoveAction
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.home_component.customview.bannerindicator.BannerIndicator
import com.tokopedia.home_component.customview.bannerindicator.BannerIndicatorListener
import com.tokopedia.home_component.viewholders.BannerComponentViewHolder
import com.tokopedia.home_component.viewholders.BannerRevampViewHolder
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifyprinciples.Typography

class CircularSliderBannerViewHolder(itemView: View, val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner), CircularListener {

    private val sliderBannerTitle: Typography = itemView.findViewById(R.id.title)
    private var sliderBannerViewModel: CircularSliderBannerViewModel? = null
    private val bannerCircularAdapter: BannerCircularAdapter = BannerCircularAdapter(listOf(), this)
    private val cvSliderBanner: CircularViewPager = itemView.findViewById(R.id.circular_slider_banner)
    private val sliderIndicator: CircularPageIndicator = itemView.findViewById(R.id.indicator_banner)
    private val sliderIndicatorAnimation: BannerIndicator = itemView.findViewById(R.id.indicator_banner_animation)
    private var isFromDrag = false
    private var isFromInitialize = false
    private var indicatorPosition = 0

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        sliderBannerViewModel = discoveryBaseViewModel as CircularSliderBannerViewModel
        sliderBannerViewModel?.getItemsList()?.let {
            sendBannerImpression(it)
            cvSliderBanner.setAdapter(bannerCircularAdapter)
            cvSliderBanner.setItemList(it)
            setSlideProperties(it)
            setUpIndicator(it)
        }
    }

    private fun setSlideProperties(item: ArrayList<CircularModel>) {
        bannerCircularAdapter.setIsInfinite(item.size > 1)
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let {
            sliderBannerViewModel?.getTitleLiveData()?.observe(
                it,
                Observer { item ->
                    sliderBannerTitle.setTextAndCheckShow(item)
                }
            )
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let { it ->
            sliderBannerViewModel?.getTitleLiveData()?.removeObservers(it)
        }
    }

    private fun setUpIndicator(item: ArrayList<CircularModel>) {
        if (item.size > 1) {
            if (sliderBannerViewModel?.getPropertyType() == "atf_banner") {
                sliderIndicatorAnimation.show()
                sliderIndicatorAnimation.setBannerTransitionDuration(5000)
                sliderIndicatorAnimation.setBannerIndicators(item.size)
            } else {
                sliderIndicator.createIndicators(cvSliderBanner.indicatorCount, cvSliderBanner.indicatorPosition)
                sliderIndicator.show()
            }
            cvSliderBanner.setIndicatorPageChangeListener(object : IndicatorPageChangeListener {
                override fun onIndicatorPageChange(newIndicatorPosition: Int) {
                    indicatorPosition = newIndicatorPosition
                    if (sliderBannerViewModel?.getPropertyType() == "atf_banner") {
                        sliderIndicatorAnimation.setBannerTransitionDuration(4400)
                    } else {
                        sliderIndicator.animatePageSelected(newIndicatorPosition)
                    }
                }
            })
        } else {
            sliderIndicator.hide()
            sliderIndicatorAnimation.hide()
        }
    }

    private fun sendBannerImpression(item: ArrayList<CircularModel>) {
        cvSliderBanner.setPageChangeListener(object : CircularPageChangeListener {
            override fun onPageScrolled(position: Int) {
                trackBannerImpressionGTM(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                when (state) {
                    ViewPager2.SCROLL_STATE_IDLE -> {
                        if (isFromDrag) {
                            sliderIndicatorAnimation.clearAnimation()
                            sliderIndicatorAnimation.setBannerTransitionDuration(5000)
                            sliderIndicatorAnimation.setBannerIndicators(item.size, indicatorPosition)
                            isFromDrag = false
                        }
                    }
                    ViewPager2.SCROLL_STATE_DRAGGING -> {
                        sliderIndicatorAnimation.pauseAnimation()
                        isFromDrag = true
                    }
                }
            }
        })
    }

    fun trackBannerImpressionGTM(position: Int = 0) {
        sliderBannerViewModel?.let { sliderBannerViewModel ->
            sliderBannerViewModel.getBannerItem(position)?.let {
                it.positionForParentItem = sliderBannerViewModel.getComponentPosition()
                (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackBannerImpression(
                    arrayListOf(it),
                    position,
                    Utils.getUserId(fragment.context)
                )
            }
        }
    }

    override fun onClick(position: Int) {
        sliderBannerViewModel?.let { sliderBannerViewModel ->
            sliderBannerViewModel.getBannerItem(position)?.let {
                it.positionForParentItem = sliderBannerViewModel.getComponentPosition()
                (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackBannerClick(
                    it,
                    position,
                    Utils.getUserId(fragment.context)
                )
                if (it.moveAction?.type != null) {
                    routingBasedOnMoveAction(it.moveAction, fragment)
                } else {
                    if (!it.applinks.isNullOrEmpty()) RouteManager.route(itemView.context, it.applinks)
                }
            }
        }
    }
}
