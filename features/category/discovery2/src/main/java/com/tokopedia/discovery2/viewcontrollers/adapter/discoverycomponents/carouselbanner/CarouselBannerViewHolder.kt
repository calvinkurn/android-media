package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.carouselbanner

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.customview.BannerDotIndicator
import com.tokopedia.discovery2.viewcontrollers.customview.BannerPagerSnapHelper
import com.tokopedia.discovery2.viewcontrollers.customview.DiscoveryBannerView
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.widget_recycler_view.view.*

class CarouselBannerViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView), DiscoveryBannerView.DiscoveryBannerViewInteraction {

    private val carouselBannerTitle: Typography = itemView.findViewById(R.id.title)
    private val discoveryBannerView: DiscoveryBannerView = itemView.findViewById(R.id.carousel_banner_view)
    private lateinit var carouselBannerViewModel: CarouselBannerViewModel
    private var carouselBannerRecycleAdapter: DiscoveryRecycleAdapter = DiscoveryRecycleAdapter(fragment)
    private var carouselBannerPagerSnapHelper: CarouselBannerPagerSnapHelper? = null
    private lateinit var bannerDotIndicator: BannerDotIndicator

    init {
        fragment.context?.let {
            bannerDotIndicator = getBannerDotIndicator(it)
        }
        discoveryBannerView.setCarouselBannerViewInteraction(this)
        attachRecyclerView()
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        carouselBannerViewModel = discoveryBaseViewModel as CarouselBannerViewModel
        setUpObservers()
    }

    private fun setUpObservers() {
        if (!carouselBannerViewModel.getComponentLiveData().hasActiveObservers()) {
            carouselBannerViewModel.getComponentLiveData().observe(fragment.viewLifecycleOwner, Observer { item ->
                carouselBannerTitle.setTextAndCheckShow(item.title)
            })
        }

        if (!carouselBannerViewModel.getListDataLiveData().hasActiveObservers()) {
            carouselBannerViewModel.getListDataLiveData().observe(fragment.viewLifecycleOwner, Observer { item ->
                carouselBannerRecycleAdapter.setDataList(item)
            })
        }

        if (carouselBannerViewModel.getSeeAllButtonLiveData().hasActiveObservers()) {
            carouselBannerViewModel.getSeeAllButtonLiveData().observe(fragment.viewLifecycleOwner, Observer {
                it?.let {
                    bannerDotIndicator.setBtnAppLink(it)
                }
            })
        }
    }

    override fun attachRecyclerView() {
        discoveryBannerView.apply {
            bannerRecyclerView.apply {
                adapter = carouselBannerRecycleAdapter
                layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
                carouselBannerPagerSnapHelper = CarouselBannerPagerSnapHelper(this)
                carouselBannerPagerSnapHelper?.attachToRecyclerView(this)
                addItemDecoration(bannerDotIndicator)
            }
        }
    }

    override fun startScrolling() {
        carouselBannerPagerSnapHelper?.startAutoScrollBanner()
    }

    private fun getBannerDotIndicator(context: Context): BannerDotIndicator {
        context.run {
            val radius = resources.getDimensionPixelSize(R.dimen.dp_4)
            val padding = resources.getDimensionPixelSize(R.dimen.dp_5)
            val indicatorPadding = resources.getDimensionPixelSize(R.dimen.dp_8)
            val activeColor = ContextCompat.getColor(context, R.color.activeBannerDot)
            val inActiveColor = ContextCompat.getColor(context, R.color.inActiveBannerDot)
            return BannerDotIndicator(radius, padding, indicatorPadding, activeColor, inActiveColor, BannerDotIndicator.CAROUSEL_BANNER_INDICATOR)
        }
    }

    override fun stopScrolling() {
        carouselBannerPagerSnapHelper?.stopAutoScrollBanner()
    }

    inner class CarouselBannerPagerSnapHelper(recyclerView: RecyclerView) : BannerPagerSnapHelper(recyclerView) {
        override fun setAutoScrollOnProgress(autoScrollOnProgress: Boolean) {
            discoveryBannerView.apply {
                setAutoScrollOnProgressValue(autoScrollOnProgress)
            }
        }

        override fun isAutoScrollEnabled(): Boolean {
            return discoveryBannerView.getAutoScrollEnabled()
        }

        override fun isAutoScrollOnProgress(): Boolean {
            return discoveryBannerView.getAutoScrollOnProgressLiveData().value ?: false
        }
    }
}