package com.tokopedia.navigation.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.navigation.R
import com.tokopedia.navigation.domain.model.InboxTopAdsBannerUiModel
import com.tokopedia.topads.sdk.listener.TdnBannerResponseListener
import com.tokopedia.topads.sdk.listener.TopAdsImageViewClickListener
import com.tokopedia.topads.sdk.widget.TdnBannerView
import com.tokopedia.unifycomponents.toPx

class InboxTopAdsBannerViewHolder constructor(
    itemView: View?,
    private val tdnBannerResponseListener: TdnBannerResponseListener,
    private val topAdsClickListener: TopAdsImageViewClickListener,
) : AbstractViewHolder<InboxTopAdsBannerUiModel>(itemView) {

    private val topAdsBanner: TdnBannerView? = itemView?.findViewById(R.id.topads_banner)

    override fun bind(element: InboxTopAdsBannerUiModel, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty() && payloads[0] == PAYLOAD_UPDATE_AD) {
            bind(element)
        } else {
            super.bind(element, payloads)
        }
    }

    override fun bind(element: InboxTopAdsBannerUiModel) {
        bindTopAds(element)
        bindTdnBanner(element)
    }

    private fun bindTdnBanner(element: InboxTopAdsBannerUiModel) {
        element.ads?.let {
            topAdsBanner?.renderTdnBanner(it, 8.toPx(), ::onTdnBannerClicked)
        }
    }

    private fun bindTopAds(element: InboxTopAdsBannerUiModel) {

        if (!element.hasAds() && !element.requested) {
            topAdsBanner?.setTdnResponseListener(tdnBannerResponseListener)
            topAdsBanner?.getTdnData(
                SOURCE,
                adsCount = ADS_COUNT,
                dimenId = DIMEN_ID
            )
            element.requested = true
        }

    }

    private fun onTdnBannerClicked(applink: String) {
        topAdsClickListener.onTopAdsImageViewClicked(applink)
    }

    companion object {
        val LAYOUT = R.layout.item_inbox_top_ads_banner

        const val SOURCE = "5"
        const val ADS_COUNT = 4
        const val DIMEN_ID = 3

        @JvmStatic
        val PAYLOAD_UPDATE_AD = "payload_update_ad"
    }
}