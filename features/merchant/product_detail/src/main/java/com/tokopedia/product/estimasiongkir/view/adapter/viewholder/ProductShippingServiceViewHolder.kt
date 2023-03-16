package com.tokopedia.product.estimasiongkir.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.product.detail.R
import com.tokopedia.product.estimasiongkir.data.model.shipping.ProductShippingServiceDataModel
import com.tokopedia.product.estimasiongkir.data.model.shipping.WhiteLabelDataModel
import com.tokopedia.product.estimasiongkir.data.model.v3.ServiceBasedShipment
import com.tokopedia.product.estimasiongkir.view.adapter.ProductServiceDetailAdapter
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Yehezkiel on 26/01/21
 */
class ProductShippingServiceViewHolder(view: View) :
    AbstractViewHolder<ProductShippingServiceDataModel>(view) {
    companion object {
        val LAYOUT = R.layout.item_product_shipping_services
    }

    private val serviceName: Typography? = itemView.findViewById(R.id.product_shipping_service_name)
    private val rvServices: RecyclerView? = itemView.findViewById(R.id.rv_product_shipping_service)
    private val whiteLabelEta: Typography? = itemView.findViewById(R.id.service_whitelabel_eta)
    private val whiteLabelPrice: Typography? = itemView.findViewById(R.id.service_whitelabel_price)
    private val adapter = ProductServiceDetailAdapter()

    init {
        rvServices?.adapter = adapter
    }

    override fun bind(element: ProductShippingServiceDataModel) {
        serviceName?.text = element.serviceName
        bindWhiteLabel(element.whiteLabelData)
        adapter.updateServices(element.productService)
    }

    private fun bindWhiteLabel(data: WhiteLabelDataModel?) {
        if (data == null) return
        val textEta = data.eta
        whiteLabelEta?.showIfWithBlock(textEta.isNotEmpty()) {
            text = textEta
        }

        val textPrice = data.price
        whiteLabelPrice?.showIfWithBlock(textPrice.isNotEmpty()) {
            text = textPrice
        }
    }

}
