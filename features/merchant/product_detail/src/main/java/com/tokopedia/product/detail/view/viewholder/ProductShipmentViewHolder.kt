package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ProductShipmentDataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener

/**
 * Created by Yehezkiel on 10/02/21
 */
class ProductShipmentViewHolder(view: View, private val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductShipmentDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_shipment
    }

    override fun bind(element: ProductShipmentDataModel?) {

    }
}