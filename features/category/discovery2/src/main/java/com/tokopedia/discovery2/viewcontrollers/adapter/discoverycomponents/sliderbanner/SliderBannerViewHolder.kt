package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.sliderbanner

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
import com.tokopedia.discovery2.viewcontrollers.customview.SliderBannerView
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.widget_slider_banner.view.*

class SliderBannerViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView), SliderBannerView.SliderBannerViewInteraction {

    private val sliderBannerTitle: Typography = itemView.findViewById(R.id.title)
    private val sliderBannerView: SliderBannerView = itemView.findViewById(R.id.slider_banner_view)
    private lateinit var sliderBannerViewModel: SliderBannerViewModel
    private var sliderBannerRecycleAdapter: DiscoveryRecycleAdapter = DiscoveryRecycleAdapter(fragment)
    private var sliderBannerPagerSnapHelper: SliderBannerPagerSnapHelper? = null

    init {
        sliderBannerView.setSliderBannerViewInteraction(this)
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        sliderBannerViewModel = discoveryBaseViewModel as SliderBannerViewModel
        setUpObservers()
    }

    private fun setUpObservers() {
        sliderBannerViewModel.getComponentsLiveData().observe(fragment.viewLifecycleOwner, Observer { item ->
            sliderBannerTitle.setTextAndCheckShow(item.title)
        })

        sliderBannerViewModel.getListDataLiveData().observe(fragment.viewLifecycleOwner, Observer { item ->
            sliderBannerRecycleAdapter.setDataList(item)
        })
    }

    override fun attachAndStartScrolling() {
        sliderBannerView.apply {
            sliderBannerRecyclerView.apply {
                adapter = sliderBannerRecycleAdapter
                layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
                sliderBannerPagerSnapHelper = SliderBannerPagerSnapHelper(this)
                sliderBannerPagerSnapHelper?.attachToRecyclerView(this)
                addItemDecoration(getBannerDotIndicator(this.context))

            }
        }
    }

    private fun getBannerDotIndicator(context: Context): BannerDotIndicator {
        context.run {
            val radius = resources.getDimensionPixelSize(R.dimen.radius)
            val padding = resources.getDimensionPixelSize(R.dimen.itemDecorationInnerPadding)
            val indicatorPadding = resources.getDimensionPixelSize(R.dimen.itemDecorationOuterPadding)
            val activeColor = ContextCompat.getColor(context, R.color.activeBannerDot)
            val inActiveColor = ContextCompat.getColor(context, R.color.inActiveBannerDot)
            return BannerDotIndicator(radius, padding, indicatorPadding, activeColor, inActiveColor, BannerDotIndicator.SLIDER_BANNER_INDICATOR)
        }
    }

    override fun detachAndStopScrolling() {
        sliderBannerPagerSnapHelper?.stopAutoScrollBanner()
    }

    inner class SliderBannerPagerSnapHelper(recyclerView: RecyclerView) : BannerPagerSnapHelper(recyclerView) {
        override fun setAutoScrollOnProgress(autoScrollOnProgress: Boolean) {
            sliderBannerView.apply {
                setAutoScrollOnProgressValue(autoScrollOnProgress)
            }
        }

        override fun isAutoScrollEnabled(): Boolean {
            return sliderBannerView.getAutoScrollEnabled()
        }

        override fun isAutoScrollOnProgress(): Boolean {
            return sliderBannerView.getAutoScrollOnProgressLiveData().value ?: false
        }
    }

}