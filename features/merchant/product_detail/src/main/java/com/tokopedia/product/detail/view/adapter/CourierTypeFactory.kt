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
import kotlinx.android.synthetic.main.item_shop_shipment.view.*

class CourierTypeFactory: BaseAdapterTypeFactory(){

    fun type(data: BlackBoxShipmentHolder): Int = CourierViewHolder.LAYOUT

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type){
            CourierViewHolder.LAYOUT -> CourierViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

    class CourierViewHolder(view: View?): AbstractViewHolder<BlackBoxShipmentHolder>(view) {
        companion object {
            val LAYOUT = R.layout.item_shop_shipment
        }

        override fun bind(element: BlackBoxShipmentHolder) {
            with(itemView){
                when(element){
                    is ProductShopShipment -> {
                        courier_item_image.visible()
                        courier_item_name.setFontSize(TextViewCompat.FontSize.TITLE)
                        courier_item_name.text = element.name
                        courier_item_info.text = element.product.map { it.name }.joinToString(", ")
                        courier_item_image.loadIcon(element.image)
                    }
                    else -> {}
                }

            }
        }
    }
}