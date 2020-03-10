package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.viewHolder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.dataModel.FlashSaleDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.listener.FlashSaleCardListener
import com.tokopedia.home.beranda.presentation.view.customview.ThematicCardView
import com.tokopedia.productcard.ProductCardFlashSaleModel
import com.tokopedia.productcard.ProductCardFlashSaleView

class FlashSaleViewHolder (view: View, private val listener: FlashSaleCardListener): AbstractViewHolder<FlashSaleDataModel>(view){
    private val productCardView: ProductCardFlashSaleView? by lazy { view.findViewById<ProductCardFlashSaleView>(R.id.productCardView) }
    override fun bind(element: FlashSaleDataModel) {
        setLayout(element)
    }

    private fun setLayout(element: FlashSaleDataModel){
//        val productCardModelLabelGroupList = element.labelGroups.map {
//            ProductCardFlashSaleModel.LabelGroup(position = it.position, type = it.type, title = it.title)
//        }

        productCardView?.run{
            setProductModel(
                    ProductCardFlashSaleModel(
                            slashedPrice = element.grid.slashedPrice,
                            productName = element.grid.name,
                            formattedPrice = element.grid.price,
                            productImageUrl = element.grid.imageUrl,
                            discountPercentage = element.grid.discount,
                            pdpViewCount = "",
                            stockBarLabel = element.grid.label
                    )
            )
            applyCarousel()
        }
    }


    companion object{
        val LAYOUT = R.layout.home_banner_item_flashsale_carousel
    }
}