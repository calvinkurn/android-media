package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchResultProductTopAdsBannerLayoutBinding
import com.tokopedia.search.result.presentation.model.CpmDataView
import com.tokopedia.search.result.presentation.view.listener.BannerAdsListener
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener
import com.tokopedia.utils.view.binding.viewBinding

class CpmViewHolder(
        itemView: View,
        bannerAdsListener: BannerAdsListener?,
) : AbstractViewHolder<CpmDataView>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.search_result_product_top_ads_banner_layout
    }

    private var binding: SearchResultProductTopAdsBannerLayoutBinding? by viewBinding()

    init {
        binding?.adsBanner?.let {
            it.setTopAdsBannerClickListener { position, applink: String?, data: CpmData? ->
                bannerAdsListener?.onBannerAdsClicked(position, applink, data)
            }

            it.setTopAdsImpressionListener(object : TopAdsItemImpressionListener() {
                override fun onImpressionHeadlineAdsItem(position: Int, data: CpmData?) {
                    bannerAdsListener?.onBannerAdsImpressionListener(position, data)
                }
            })
        }
    }

    override fun bind(element: CpmDataView) {
        binding?.adsBanner?.displayAds(element.cpmModel)
    }
}