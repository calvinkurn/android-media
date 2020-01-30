package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.sliderbanner

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.customview.SliderBannerView
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.unifyprinciples.Typography

class SliderBannerViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {

    private val sliderBannerTitle: Typography = itemView.findViewById(R.id.title)
    private val sliderBannerView: SliderBannerView = itemView.findViewById(R.id.slider_banner_view)
    private lateinit var sliderBannerViewModel: SliderBannerViewModel
    private var sliderBannerRecycleAdapter: DiscoveryRecycleAdapter = DiscoveryRecycleAdapter(fragment)

    init {
        sliderBannerView.setAdapter(sliderBannerRecycleAdapter)
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
            sliderBannerView.setDataItems(item)
            sliderBannerView.buildView()
        })
    }

}