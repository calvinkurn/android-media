package com.tokopedia.product.detail.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.design.component.TextViewCompat
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.shop.BlackBoxShipmentHolder
import com.tokopedia.product.detail.data.model.shop.ProductShopShipment
import com.tokopedia.product.detail.databinding.ItemShopShipmentBinding

class CourierTypeFactory: BaseAdapterTypeFactory(){

    fun type(data: BlackBoxShipmentHolder): Int = CourierViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type){
            CourierViewHolder.LAYOUT -> CourierViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

    class CourierViewHolder(view: View): AbstractViewHolder<BlackBoxShipmentHolder>(view) {
        companion object {
            val LAYOUT = R.layout.item_shop_shipment
        }

        private val binding = ItemShopShipmentBinding.bind(view)

        override fun bind(element: BlackBoxShipmentHolder) {
            with(binding){
                when(element){
                    is ProductShopShipment -> {
                        courierItemImage.visible()
                        courierItemName.setFontSize(TextViewCompat.FontSize.TITLE)
                        courierItemName.text = element.name
                        courierItemInfo.text = element.product.map { it.name }.joinToString(", ")
                        courierItemImage.loadIcon(element.image)
                    }
                    else -> {}
                }

            }
        }
    }
}