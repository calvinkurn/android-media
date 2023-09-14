package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.circularsliderbanner

import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
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
import com.tokopedia.home_component.viewholders.BannerComponentViewHolder.Companion.FLING_DURATION
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.PageControl
import com.tokopedia.unifycomponents.PageControl.Companion.COLOR_INVERTED
import com.tokopedia.unifyprinciples.Typography

class CircularSliderBannerViewHolder(itemView: View, val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner), CircularListener {

    private val sliderBannerTitle: Typography = itemView.findViewById(R.id.title)
    private var sliderBannerViewModel: CircularSliderBannerViewModel? = null
    private val bannerCircularAdapter: BannerCircularAdapter = BannerCircularAdapter(listOf(), this)
    private val cvSliderBanner: CircularViewPager = itemView.findViewById(R.id.circular_slider_banner)
    private val sliderIndicator: CircularPageIndicator = itemView.findViewById(R.id.indicator_banner)
    private val sliderExpandableIndicator: BannerIndicator = itemView.findViewById(R.id.expandable_indicator_banner_turned_on)
    private val sliderTargetingIndicator: PageControl = itemView.findViewById(R.id.expandable_indicator_banner_turned_off)
    private var isDraggingWithExpandableIndicator = false
    private var expandableIndicatorPosition = 0
    private var debounceHandler = Handler(Looper.getMainLooper())
    private var debounceRunnable = Runnable {
        sliderExpandableIndicator.startIndicatorByPosition(expandableIndicatorPosition)
        isDraggingWithExpandableIndicator = false
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        sliderBannerViewModel = discoveryBaseViewModel as? CircularSliderBannerViewModel
        sliderBannerViewModel?.getItemsList()?.let {
            sendBannerImpression()
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
            sliderBannerViewModel?.getSyncPageLiveData()?.observe(
                it
            ) { needResync ->
                if (needResync) (fragment as? DiscoveryFragment)?.reSync()
            }
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
            sliderBannerViewModel?.apply {
                if (isExpandableIndicatorNeeded()) {
                    if (isDisabledAutoSlide()) {
                        setBannerExpandableIndicatorAnimationTurnedOff(
                            size = item.size
                        )
                    } else {
                        setBannerExpandableIndicatorAnimationTurnedOn(
                            size = item.size
                        )
                    }
                } else {
                    setBannerIndicatorAnimation()
                }
            }
        } else {
            sliderIndicator.hide()
            sliderExpandableIndicator.hide()
        }
    }

    private fun setBannerExpandableIndicatorAnimationTurnedOff(
        size: Int
    ) {
        setupExpandableIndicatorTurnedOff(
            size = size
        )
        setupBannerWithExpandableIndicatorTurnedOff()
    }

    private fun setBannerExpandableIndicatorAnimationTurnedOn(
        size: Int
    ) {
        setupExpandableIndicatorTurnedOn(
            size = size
        )
        setupBannerWithExpandableIndicatorTurnedOn()
    }

    private fun setupExpandableIndicatorTurnedOff(size: Int) {
        sliderTargetingIndicator.show()
        sliderTargetingIndicator.indicatorColorType = COLOR_INVERTED
        sliderTargetingIndicator.setIndicator(
            size = size
        )
    }

    private fun setupExpandableIndicatorTurnedOn(
        size: Int
    ) {
        sliderIndicator.hide()
        sliderExpandableIndicator.show()
        sliderExpandableIndicator.setBannerIndicators(size)
        sliderExpandableIndicator.setBannerListener(
            object : BannerIndicatorListener {
                override fun onChangePosition(position: Int) { /* nothing to do */ }

                override fun getCurrentPosition(position: Int) { /* nothing to do */ }

                override fun onChangeCurrentPosition(position: Int) {
                    cvSliderBanner.setCurrentPosition(position)
                }
            }
        )
    }

    private fun setupBannerWithExpandableIndicatorTurnedOff() {
        /**
         * disable auto-scroll on banner and let the slider banner determines the position of the indicator
         */
        disableBannerAutoScroll()
        cvSliderBanner.setIndicatorPageChangeListener(
            object : IndicatorPageChangeListener {
                override fun onIndicatorPageChange(newIndicatorPosition: Int) {
                    /**
                     * re-synchronize the component below this widget
                     */
                    sliderBannerViewModel?.onBannerChanged(newIndicatorPosition)
                    sliderTargetingIndicator.setCurrentIndicator(newIndicatorPosition)
                }
            }
        )
    }

    private fun setupBannerWithExpandableIndicatorTurnedOn() {
        /**
         * disable auto-scroll on banner and let the expandable indicator determines the position of the banner
         */
        disableBannerAutoScroll()
        cvSliderBanner.setIndicatorPageChangeListener(
            object : IndicatorPageChangeListener {
                override fun onIndicatorPageChange(newIndicatorPosition: Int) {
                    if (newIndicatorPosition != expandableIndicatorPosition) {
                        expandableIndicatorPosition = newIndicatorPosition
                        /**
                         * If [isDraggingWithExpandableIndicator] is true then do debounce to start indicator animation,
                         * this indicator needs debounce to avoid the animation starting simultaneously with other indicators,
                         * because when swiping manually quickly, sometimes the indicator animation is not back to the initial state.
                         */
                        if (isDraggingWithExpandableIndicator) {
                            debounceHandler.removeCallbacks(debounceRunnable)
                            debounceHandler.postDelayed(
                                debounceRunnable,
                                FLING_DURATION.toLong()
                            )
                        }
                    }
                }
            }
        )
    }

    private fun disableBannerAutoScroll() {
        cvSliderBanner.pauseAutoScroll()
        cvSliderBanner.isResumingAutoScrollDisabled = true
    }

    private fun setBannerIndicatorAnimation() {
        cvSliderBanner.setIndicatorPageChangeListener(object : IndicatorPageChangeListener {
            override fun onIndicatorPageChange(newIndicatorPosition: Int) {
                sliderIndicator.animatePageSelected(newIndicatorPosition)
            }
        })
        sliderExpandableIndicator.hide()
        sliderIndicator.show()
        sliderIndicator.createIndicators(cvSliderBanner.indicatorCount, cvSliderBanner.indicatorPosition)
    }

    private fun sendBannerImpression() {
        cvSliderBanner.setPageChangeListener(object : CircularPageChangeListener {
            override fun onPageScrolled(position: Int) {
                trackBannerImpressionGTM(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                /**
                 * Make sure slider expandable indicator already initialized (call [setBannerIndicators] to initialize).
                 *
                 * @see ViewPager2.SCROLL_STATE_IDLE
                 * Start the animation of expandable indicator only when user drags the banner but not swiping the page of the banner,
                 * that means user still on the same page. This is needed because [onIndicatorPageChange] is not triggered if still on the same page.
                 *
                 * @see ViewPager2.SCROLL_STATE_DRAGGING
                 * Pause the animation of expandable indicator and set [isDraggingWithExpandableIndicator] variable to true,
                 * the variable will be used as one of indicators to start the animation again when slider banner has been changed.
                 **/
                if (sliderExpandableIndicator.isInitialized) {
                    when (state) {
                        ViewPager2.SCROLL_STATE_IDLE -> {
                            if (isDraggingWithExpandableIndicator && sliderExpandableIndicator.isCurrentPosition(expandableIndicatorPosition)) {
                                sliderExpandableIndicator.startIndicatorByPosition(expandableIndicatorPosition)
                                isDraggingWithExpandableIndicator = false
                            }
                        }
                        ViewPager2.SCROLL_STATE_DRAGGING -> {
                            sliderExpandableIndicator.pauseAnimation()
                            isDraggingWithExpandableIndicator = true
                        }
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
