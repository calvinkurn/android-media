package com.tokopedia.search.result.product.cpm

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpression1pxListener
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchResultProductTopAdsBannerLayoutBinding
import com.tokopedia.topads.sdk.TopAdsConstants.LAYOUT_5
import com.tokopedia.topads.sdk.TopAdsConstants.LAYOUT_6
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.domain.model.Product
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener
import com.tokopedia.utils.view.binding.viewBinding

class CpmViewHolder(
    itemView: View,
    private val bannerAdsListener: BannerAdsListener?,
) : AbstractViewHolder<CpmDataView>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.search_result_product_top_ads_banner_layout
    }

    private var binding: SearchResultProductTopAdsBannerLayoutBinding? by viewBinding()

    override fun bind(element: CpmDataView) {
        binding?.adsBanner?.run {
            setTopAdsBannerClickListener(object : TopAdsBannerClickListener {
                override fun onBannerAdsClicked(position: Int, applink: String?, data: CpmData?) {
                    bannerAdsListener?.onBannerAdsClicked(position, applink, data, element)
                }
            })
            setTopAdsImpressionListener(object : TopAdsItemImpressionListener() {
                override fun onImpressionHeadlineAdsItem(position: Int, data: CpmData) {
                    bannerAdsListener?.onBannerAdsImpressionListener(position, data, element, bindingAdapterPosition)
                    if (data.cpm.layout == LAYOUT_6 || data.cpm.layout == LAYOUT_5) {
                        bannerAdsListener?.onTopAdsCarouselItemImpressionListener(binding?.adsBanner?.impressionCount ?: 0)
                    }
                }

                override fun onImpressionProductAdsItem(
                    position: Int,
                    product: Product?,
                    data: CpmData
                ) {
                    bannerAdsListener?.onBannerAdsProductImpressionListener(
                        position,
                        product,
                        element,
                    )
                }
            })

            addOnImpression1pxListener(element.byteIOImpressHolder) {
                bannerAdsListener?.onBannerAdsImpression1PxListener(element, false)
            }

            displayHeadlineAds(element.cpmModel)
        }
    }
}
