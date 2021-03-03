package com.tokopedia.product.estimasiongkir.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.R
import com.tokopedia.product.estimasiongkir.data.model.shipping.ProductShippingServiceDataModel
import com.tokopedia.product.estimasiongkir.view.adapter.ProductServiceDetailAdapter

/**
 * Created by Yehezkiel on 26/01/21
 */
class ProductShippingServiceViewHolder(view: View) : AbstractViewHolder<ProductShippingServiceDataModel>(view) {
    companion object {
        val LAYOUT = R.layout.item_product_shipping_services
    }

    private val serviceName: com.tokopedia.unifyprinciples.Typography? = itemView.findViewById(R.id.product_shipping_service_name)
    private val rvServices: RecyclerView? = itemView.findViewById(R.id.rv_product_shipping_service)
    private val adapter = ProductServiceDetailAdapter()

    init {
        rvServices?.adapter = adapter
    }

    override fun bind(element: ProductShippingServiceDataModel) {
        serviceName?.text = element.serviceName
        adapter.updateServices(element.productService)
    }

}