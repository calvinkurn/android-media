package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.topadsheadline

import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.domain.model.Product
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener
import com.tokopedia.topads.sdk.widget.TopAdsHeadlineView

class TopAdsHeadlineViewHolder (itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner), TopAdsBannerClickListener {

    private lateinit var topAdsHeadlineViewModel: TopAdsHeadlineViewModel
    private val topadsHeadlineView: TopAdsHeadlineView = itemView.findViewById(R.id.topads_headline_view)

    init {
//        topadsHeadlineView.setTopAdsBannerClickListener(this)
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        topAdsHeadlineViewModel = discoveryBaseViewModel as TopAdsHeadlineViewModel
        getSubComponent().inject(topAdsHeadlineViewModel)
        topadsHeadlineView.showShimmerView()
        fetchTopadsHeadlineAds()
    }

    private fun fetchTopadsHeadlineAds() {
        topadsHeadlineView.getHeadlineAds(topAdsHeadlineViewModel.getHeadlineAdsParam(), this::onSuccessResponse, this::hideHeadlineView)
    }


    private fun onSuccessResponse(cpmModel: CpmModel) {
        showHeadlineView(cpmModel)
    }

    private fun hideHeadlineView() {
        topadsHeadlineView.hideShimmerView()
        topadsHeadlineView.hide()
    }

    private fun showHeadlineView(cpmModel: CpmModel) {
        topadsHeadlineView.hideShimmerView()
        topadsHeadlineView.show()
        topadsHeadlineView.displayAds(cpmModel)
        cpmModel.let {
            topadsHeadlineView.addOnImpressionListener(ImpressHolder()) {
                (fragment as DiscoveryFragment).getDiscoveryAnalytics().onTopadsHeadlineImpression(it, adapterPosition)
            }
        }
    }

    override fun onBannerAdsClicked(position: Int, applink: String?, data: CpmData?) {
        data?.let {
            (fragment as DiscoveryFragment).getDiscoveryAnalytics().onTopAdsHeadlineAdsClick(position, applink, it)
        }
    }
}