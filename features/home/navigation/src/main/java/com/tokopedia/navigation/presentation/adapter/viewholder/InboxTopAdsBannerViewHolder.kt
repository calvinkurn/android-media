package com.tokopedia.navigation.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.navigation.R
import com.tokopedia.navigation.domain.model.InboxTopAdsBannerUiModel
import com.tokopedia.topads.sdk.listener.TopAdsImageVieWApiResponseListener
import com.tokopedia.topads.sdk.widget.TopAdsImageView

class InboxTopAdsBannerViewHolder constructor(
        itemView: View?,
        private val listener: Listener
) : AbstractViewHolder<InboxTopAdsBannerUiModel>(itemView) {

    private val topAdsBanner: TopAdsImageView? = itemView?.findViewById(R.id.topads_banner)

    interface Listener {
        fun getTopAdsResponseListener(): TopAdsImageVieWApiResponseListener
    }

    override fun bind(element: InboxTopAdsBannerUiModel, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty() && payloads[0] == PAYLOAD_UPDATE_AD) {
            bind(element)
        } else {
            super.bind(element, payloads)
        }
    }

    override fun bind(element: InboxTopAdsBannerUiModel) {
        bindTopAds(element)
        bindTopAdsImage(element)
    }

    private fun bindTopAdsImage(element: InboxTopAdsBannerUiModel) {
        element.ad?.let {
            topAdsBanner?.loadImage(it) { }
        }
    }

    private fun bindTopAds(element: InboxTopAdsBannerUiModel) {
        if (element.hasAd()) return
        topAdsBanner?.setApiResponseListener(listener.getTopAdsResponseListener())
        topAdsBanner?.getImageData(
                "5",
                1,
                3,
                "",
                "",
                ""
        )
    }

    companion object {
        val LAYOUT = R.layout.item_inbox_top_ads_banner

        @JvmStatic
        val PAYLOAD_UPDATE_AD = "payload_update_ad"
    }
}