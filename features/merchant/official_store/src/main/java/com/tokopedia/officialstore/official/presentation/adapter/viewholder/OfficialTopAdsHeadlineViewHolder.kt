package com.tokopedia.officialstore.official.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.OfficialTopAdsHeadlineDataModel
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener
import com.tokopedia.topads.sdk.widget.TopAdsHeadlineView


class OfficialTopAdsHeadlineViewHolder(
    private val view: View,
    private val onTopAdsHeadlineClicked: (applink: String) -> Unit
) :
    AbstractViewHolder<OfficialTopAdsHeadlineDataModel>(view), TopAdsBannerClickListener {

    var topAdsHeadlineView: TopAdsHeadlineView? = null

    companion object {
        val LAYOUT = R.layout.widget_official_top_ads_headline
    }

    override fun bind(element: OfficialTopAdsHeadlineDataModel) {
        bindView()
        bindItem(element)
    }

    private fun bindView() {
        topAdsHeadlineView = itemView.findViewById(R.id.widgetTopAdsHeadline)
        topAdsHeadlineView?.setTopAdsBannerClickListener(this)
    }

    private fun bindItem(element: OfficialTopAdsHeadlineDataModel) {
        topAdsHeadlineView?.displayAds(element.topAdsHeadlineResponse.displayAds)
        topAdsHeadlineView?.hideShimmerView()
    }

    override fun onBannerAdsClicked(position: Int, applink: String?, data: CpmData?) {
        applink?.let { onTopAdsHeadlineClicked.invoke(it) }
    }
}