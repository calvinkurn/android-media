package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.viewHolder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.dataModel.FlashSaleDataModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.productcard.ProductCardFlashSaleView
import com.tokopedia.topads.sdk.utils.ImpresionTask

class FlashSaleViewHolder (view: View,
                           private val channels: DynamicHomeChannel.Channels):
        AbstractViewHolder<FlashSaleDataModel>(view) {

    companion object{
        val LAYOUT = R.layout.home_banner_item_flashsale_carousel
    }

    private val productCardView: ProductCardFlashSaleView? by lazy { view.findViewById<ProductCardFlashSaleView>(R.id.productCardView) }
    override fun bind(element: FlashSaleDataModel) {
        setLayout(element)
    }

    private fun setLayout(element: FlashSaleDataModel){
        productCardView?.run{
            applyCarousel()
            setProductModel(element.productModel)
            addOnImpressionListener(element.impressHolder) {
                if(element.grid.isTopads){
                    ImpresionTask().execute(element.grid.impression)
                }
                element.listener.onFlashSaleCardImpressed(adapterPosition, channels)
            }
            setOnClickListener {
                if(element.grid.isTopads){
                    ImpresionTask().execute(element.grid.productClickUrl)
                }
                element.listener.onFlashSaleCardClicked(adapterPosition, channels, element.grid, element.applink)
            }
        }
    }
}