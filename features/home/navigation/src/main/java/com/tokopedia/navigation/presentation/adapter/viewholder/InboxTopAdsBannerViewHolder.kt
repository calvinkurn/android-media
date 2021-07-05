package com.tokopedia.navigation.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.navigation.R
import com.tokopedia.navigation.domain.model.InboxTopAdsBannerUiModel
import com.tokopedia.topads.sdk.listener.TopAdsImageVieWApiResponseListener
import com.tokopedia.topads.sdk.listener.TopAdsImageViewClickListener
import com.tokopedia.topads.sdk.listener.TopAdsImageViewImpressionListener
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.topads.sdk.widget.TopAdsImageView
import com.tokopedia.unifycomponents.toPx

class InboxTopAdsBannerViewHolder constructor(
        itemView: View?,
        private val topAdsResponseListener: TopAdsImageVieWApiResponseListener,
        private val topAdsClickListener: TopAdsImageViewClickListener
) : AbstractViewHolder<InboxTopAdsBannerUiModel>(itemView) {

    private val topAdsBanner: TopAdsImageView? = itemView?.findViewById(R.id.topads_banner)

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
            topAdsBanner?.loadImage(it, 8.toPx()) { }
        }
    }

    private fun bindTopAds(element: InboxTopAdsBannerUiModel) {
        if (element.hasAd()) {
            topAdsBanner?.setTopAdsImageViewImpression(object : TopAdsImageViewImpressionListener {
                override fun onTopAdsImageViewImpression(viewUrl: String) {
                    if (!element.impressHolder.isInvoke) {
                        hitTopAdsImpression(viewUrl)
                        element.impressHolder.invoke()
                    }
                }
            })
            topAdsBanner?.setTopAdsImageViewClick(topAdsClickListener)
        } else {
            if (!element.requested) {
                topAdsBanner?.setApiResponseListener(topAdsResponseListener)
                topAdsBanner?.getImageData(SOURCE, ADS_COUNT, DIMEN_ID)
                element.requested = true
            }
        }
    }

    private fun hitTopAdsImpression(viewUrl: String) {
        itemView.context.let {
            TopAdsUrlHitter(it).hitImpressionUrl(
                    this::class.java.simpleName,
                    viewUrl,
                    "",
                    "",
                    ""
            )
        }
    }

    companion object {
        val LAYOUT = R.layout.item_inbox_top_ads_banner

        const val SOURCE = "5"
        const val ADS_COUNT = 1
        const val DIMEN_ID = 3

        @JvmStatic
        val PAYLOAD_UPDATE_AD = "payload_update_ad"
    }
}