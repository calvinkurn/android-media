package com.tokopedia.product.detail.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.shop.ShopShipment
import kotlinx.android.synthetic.main.item_shop_shipment.view.*

class CourierTypeFactory: BaseAdapterTypeFactory(){

    fun type(data: ShopShipment): Int = CourierViewHolder.LAYOUT

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type){
            CourierViewHolder.LAYOUT -> CourierViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

    class CourierViewHolder(view: View?): AbstractViewHolder<ShopShipment>(view) {
        companion object {
            val LAYOUT = R.layout.item_shop_shipment
        }

        override fun bind(element: ShopShipment) {
            with(itemView){
                courier_item_name.text = element.name
                courier_item_info.text = element.product.map { it.name }.joinToString(", ")
                ImageHandler.loadImage(context, courier_item_image, element.image, -1)
            }
        }
    }
}