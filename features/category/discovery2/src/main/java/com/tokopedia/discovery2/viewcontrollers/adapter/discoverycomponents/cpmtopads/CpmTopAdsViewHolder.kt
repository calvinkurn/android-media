package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.cpmtopads

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener
import com.tokopedia.topads.sdk.widget.TopAdsBannerView
import com.tokopedia.usecase.coroutines.Success

class CpmTopAdsViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    private var adsBannerView: TopAdsBannerView = itemView.findViewById(R.id.cpm_ads_banner)
    private lateinit var cpmTopAdsViewModel: CpmTopAdsViewModel

    init {
        adsBannerView.setTopAdsBannerClickListener(TopAdsBannerClickListener { position: Int, applink: String?, data: CpmData? ->
            RouteManager.route(itemView.context, applink)
            sendClickGTMTracking(position, data)
        })

        adsBannerView.setTopAdsImpressionListener(object : TopAdsItemImpressionListener() {
            override fun onImpressionHeadlineAdsItem(position: Int, data: CpmData) {
                sendViewGTMImpression(position, data)
            }
        })
    }


    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        cpmTopAdsViewModel = discoveryBaseViewModel as CpmTopAdsViewModel
        getSubComponent().inject(cpmTopAdsViewModel)
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        cpmTopAdsViewModel.getCpmData().observe(fragment.viewLifecycleOwner, Observer { item ->
            when (item) {
                is Success -> {
                    adsBannerView.displayAds(item.data)
                }
            }
        })
    }

    private fun sendClickGTMTracking(position: Int, cpmData: CpmData?) {
        val componentDataItem = cpmTopAdsViewModel.getComponentData()
        val componentPosition = cpmTopAdsViewModel.getComponentPosition()
        val cpmProductPosition = position - 1

        if (cpmData != null) {
            if (position == 0) {
                (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackClickTopAdsShop(componentDataItem, cpmData)
            } else if (cpmProductPosition >= 0) {
                (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackClickTopAdsProducts(componentDataItem, cpmData, componentPosition, cpmProductPosition, cpmTopAdsViewModel.isUserLoggedIn())
            }
        }
    }

    private fun sendViewGTMImpression(position: Int, cpmData: CpmData?) {
        val componentDataItem = cpmTopAdsViewModel.getComponentData()
        val componentPosition = cpmTopAdsViewModel.getComponentPosition()
        val cpmProductPosition = position - 1

        if (cpmData != null && cpmProductPosition >= 0) {
            if (position == 1) {
                (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackEventImpressionTopAdsShop(componentDataItem, cpmData)
                fragment.getDiscoveryAnalytics().trackTopAdsProductImpression(componentDataItem, cpmData, componentPosition, cpmProductPosition, cpmTopAdsViewModel.isUserLoggedIn())
            } else {
                (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackTopAdsProductImpression(componentDataItem, cpmData, componentPosition, cpmProductPosition, cpmTopAdsViewModel.isUserLoggedIn())
            }
        }
    }
}