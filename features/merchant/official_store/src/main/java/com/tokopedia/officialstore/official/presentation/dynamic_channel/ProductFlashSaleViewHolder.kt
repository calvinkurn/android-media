package com.tokopedia.officialstore.official.presentation.dynamic_channel

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.common.OfficialStoreConstant
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Channel
import com.tokopedia.officialstore.official.presentation.viewmodel.ProductFlashSaleDataModel
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter

class ProductFlashSaleViewHolder(
        view: View,
        private val channel: Channel,
        private val dcEventHandler: DynamicChannelEventHandler
) : AbstractViewHolder<ProductFlashSaleDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.layout_product_card_carousel_item
        private const val className: String = "com.tokopedia.officialstore.official.presentation.dynamic_channel.ProductFlashSaleViewHolder"
    }

    private val productCardView: ProductCardGridView? by lazy { view.findViewById<ProductCardGridView>(R.id.productCardView) }

    override fun bind(element: ProductFlashSaleDataModel) {
        setLayout(element)
    }

    private fun setLayout(element: ProductFlashSaleDataModel) {
        productCardView?.apply {
            applyCarousel()
            setProductModel(element.productModel)
            channel.grids?.getOrNull(adapterPosition)?.let {grid ->
                val impressHolder = ImpressHolder().apply {
                    if (grid.isImpressed)
                        invoke()
                }
                addOnImpressionListener(impressHolder) {
                    if (element.productModel.isTopAds) {
                        context?.run {
                            TopAdsUrlHitter(className).hitImpressionUrl(
                                    this,
                                    element.grid.impression,
                                    element.grid.id.toString(),
                                    element.grid.name,
                                    element.grid.imageUrl,
                                    OfficialStoreConstant.TopAdsComponent.getProductCardTopAdsComponentName(channel)
                            )
                        }
                    }
                    dcEventHandler.onFlashSaleCardImpressed(adapterPosition, element.grid, channel)
                    grid.isImpressed = true
                }
            }
            setOnClickListener {
                dcEventHandler.onFlashSaleCardClicked(adapterPosition, channel, element.grid, element.applink)
            }
        }
    }
}