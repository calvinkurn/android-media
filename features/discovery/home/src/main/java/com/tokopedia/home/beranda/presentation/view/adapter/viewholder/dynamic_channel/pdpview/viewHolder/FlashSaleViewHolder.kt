package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.viewHolder

import android.content.Context
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R as home_componentR
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.dataModel.FlashSaleDataModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.productcard.ProductCardClickListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter

class FlashSaleViewHolder (view: View,
                           private val channels: DynamicHomeChannel.Channels):
        AbstractViewHolder<FlashSaleDataModel>(view) {

    companion object{
        val LAYOUT = home_componentR.layout.home_banner_item_flashsale_carousel
        private const val className = "com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.viewHolder.FlashSaleViewHolder"
    }

    private val productCardView: ProductCardGridView? by lazy { view.findViewById<ProductCardGridView>(home_componentR.id.productCardView) }

    override fun bind(element: FlashSaleDataModel) {
        setLayout(itemView.context, element)
    }

    private fun setLayout(context: Context, element: FlashSaleDataModel){
        productCardView?.run{
            applyCarousel()
            setProductModel(element.productModel)
            addOnImpressionListener(element.impressHolder) {
                if(element.grid.isTopads){
                    TopAdsUrlHitter(className).hitImpressionUrl(context, element.grid.impression,
                            element.grid.id,
                            element.grid.name,
                            element.grid.imageUrl)
                }
                element.listener.onFlashSaleCardImpressed(adapterPosition, channels, element.grid)
            }

            setProductCardViewClickListener(element)
        }
    }

    private fun ProductCardGridView.setProductCardViewClickListener(element: FlashSaleDataModel) {
        setOnClickListener(object : ProductCardClickListener {
            override fun onClick(v: View) {
                if(element.grid.isTopads){
                    TopAdsUrlHitter(className).hitClickUrl(context, element.grid.productClickUrl,
                        element.grid.id,
                        element.grid.name,
                        element.grid.imageUrl)
                }
                element.listener.onFlashSaleCardClicked(adapterPosition, channels, element.grid, element.applink)
            }
        })
    }
}
