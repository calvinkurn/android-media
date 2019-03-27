package com.tokopedia.product.detail.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.design.component.TextViewCompat
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.shop.BBInfo
import com.tokopedia.product.detail.data.model.shop.BlackBoxShipmentHolder
import com.tokopedia.product.detail.data.model.shop.ShopShipment
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
                    is BBInfo -> {
                        courier_item_image.gone()
                        courier_item_name.setFontSize(TextViewCompat.FontSize.SMALL)
                        courier_item_name.text = element.name
                        courier_item_info.text = if (element.desc.isNotBlank()) "(${element.desc})" else ""
                    }
                    is ShopShipment -> {
                        courier_item_image.visible()
                        courier_item_name.setFontSize(TextViewCompat.FontSize.TITLE)
                        courier_item_name.text = element.name
                        courier_item_info.text = element.product.map { it.name }.joinToString(", ")
                        ImageHandler.loadImage(context, courier_item_image, element.image, -1)
                    }
                    else -> {}
                }

            }
        }
    }
}