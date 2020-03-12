package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.viewHolder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.dataModel.FlashSaleDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.listener.FlashSaleCardListener
import com.tokopedia.home.beranda.presentation.view.customview.ThematicCardView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.productcard.ProductCardFlashSaleModel
import com.tokopedia.productcard.ProductCardFlashSaleView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class FlashSaleViewHolder (view: View, private val listener: FlashSaleCardListener,
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
                listener.onFlashSaleCardImpressed(adapterPosition, channels)
            }
            setOnClickListener {
                listener.onFlashSaleCardClicked(adapterPosition, channels, element.grid)
            }
        }
    }
}