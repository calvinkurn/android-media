package com.tokopedia.search.result.product.cpm

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.discovery.common.reimagine.Search2Component
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchResultProductTopAdsBannerLayoutBinding
import com.tokopedia.topads.sdk.TopAdsConstants.LAYOUT_5
import com.tokopedia.topads.sdk.TopAdsConstants.LAYOUT_6
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener
import com.tokopedia.utils.view.binding.viewBinding

class CpmViewHolder(
    itemView: View,
    bannerAdsListener: BannerAdsListener?,
    private val reimagineSearch2Component: Search2Component = Search2Component.CONTROL,
) : AbstractViewHolder<CpmDataView>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.search_result_product_top_ads_banner_layout
    }

    private var binding: SearchResultProductTopAdsBannerLayoutBinding? by viewBinding()

    init {
        binding?.adsBanner?.let {
            it.setTopAdsBannerClickListener(object : TopAdsBannerClickListener {
                override fun onBannerAdsClicked(position: Int, applink: String?, data: CpmData?) {
                    bannerAdsListener?.onBannerAdsClicked(position, applink, data)
                }
            })
            it.setTopAdsImpressionListener(object : TopAdsItemImpressionListener() {
                override fun onImpressionHeadlineAdsItem(position: Int, data: CpmData) {
                    bannerAdsListener?.onBannerAdsImpressionListener(position, data)
                    if (data.cpm.layout == LAYOUT_6 || data.cpm.layout == LAYOUT_5) {
                        bannerAdsListener?.onTopAdsCarouselItemImpressionListener(binding?.adsBanner?.impressionCount ?: 0)
                    }
                }
            })
        }
    }

    private fun isReimagine(): Boolean = reimagineSearch2Component.isReimagineShopAds() || reimagineSearch2Component.isReimagineQuickFilter()
    private fun isMultiline(): Boolean = !reimagineSearch2Component.isReimagineQuickFilter()

    override fun bind(element: CpmDataView) {
        binding?.adsBanner?.displayHeadlineAds(element.cpmModel, isReimagine = isReimagine(), hasMultilineProductName = isMultiline())
    }
}
