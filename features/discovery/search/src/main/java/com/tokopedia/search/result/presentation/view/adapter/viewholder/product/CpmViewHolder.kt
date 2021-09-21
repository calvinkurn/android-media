package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.CpmDataView
import com.tokopedia.search.result.presentation.view.listener.BannerAdsListener
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener
import com.tokopedia.topads.sdk.widget.TopAdsBannerView

class CpmViewHolder(
        itemView: View,
        bannerAdsListener: BannerAdsListener?,
) : AbstractViewHolder<CpmDataView>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.search_result_product_top_ads_banner_layout
    }

    private val adsBannerView: TopAdsBannerView = itemView.findViewById(R.id.ads_banner)

    init {
        adsBannerView.setTopAdsBannerClickListener { position, applink: String?, data: CpmData? ->
            bannerAdsListener?.onBannerAdsClicked(position, applink, data)
        }

        adsBannerView.setTopAdsImpressionListener(object : TopAdsItemImpressionListener() {
            override fun onImpressionHeadlineAdsItem(position: Int, data: CpmData?) {
                bannerAdsListener?.onBannerAdsImpressionListener(position, data)
            }
        })
    }

    override fun bind(element: CpmDataView) {
        adsBannerView.displayAds(element.cpmModel)
    }
}