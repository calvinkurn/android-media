package com.tokopedia.search.result.product.cpm

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.discovery.common.reimagine.Search2Component
import com.tokopedia.kotlin.extensions.view.addOnImpression1pxListener
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchResultProductTopAdsBannerLayoutReimagineBinding
import com.tokopedia.topads.sdk.TopAdsConstants.LAYOUT_2
import com.tokopedia.topads.sdk.TopAdsConstants.LAYOUT_5
import com.tokopedia.topads.sdk.TopAdsConstants.LAYOUT_6
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.domain.model.Product
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class CpmReimagineViewHolder(
    itemView: View,
    private val bannerAdsListener: BannerAdsListener?,
    private val reimagineSearch2Component: Search2Component = Search2Component.CONTROL,
) : AbstractViewHolder<CpmDataView>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.search_result_product_top_ads_banner_layout_reimagine
    }

    private var binding: SearchResultProductTopAdsBannerLayoutReimagineBinding? by viewBinding()

    override fun bind(element: CpmDataView) {
        adjustMargin(reimagineSearch2Component.isReimagineShopAds(), element)

        binding?.adsBanner?.run {
            setTopAdsBannerClickListener(object : TopAdsBannerClickListener {
                override fun onBannerAdsClicked(position: Int, applink: String?, data: CpmData?) {
                    bannerAdsListener?.onBannerAdsClicked(position, applink, data, element, true)
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
                bannerAdsListener?.onBannerAdsImpression1PxListener(element, true)
            }

            displayHeadlineAds(element.cpmModel)
        }
    }

    private fun adjustMargin(isReimagine: Boolean, itemCPM: CpmDataView) {
        val cpmData = itemCPM.cpmModel.data.firstOrNull()
        if (cpmData?.cpm?.layout == LAYOUT_2 && isReimagine) {
            adjustMarginLayout2Reimagine()
        } else {
            adjustMarginLayoutControl()
        }
    }

    private fun adjustMarginLayout2Reimagine() {
        val adsBannerLayout = binding?.adsBanner ?: return
        val context = adsBannerLayout.context
        val marginTopBottom = context.resources.getDimensionPixelSize(unifyprinciplesR.dimen.unify_space_8)
        val marginLeftRight = context.resources.getDimensionPixelSize(unifyprinciplesR.dimen.unify_space_4)
        binding?.adsBanner?.setMargin(left = marginLeftRight, top= marginTopBottom, right= marginLeftRight, bottom= marginTopBottom)
    }

    private fun adjustMarginLayoutControl() {
        val adsBannerLayout = binding?.adsBanner ?: return
        adsBannerLayout.setMargin(left = 0, top= 0, right= 0, bottom= 0)
    }
}
