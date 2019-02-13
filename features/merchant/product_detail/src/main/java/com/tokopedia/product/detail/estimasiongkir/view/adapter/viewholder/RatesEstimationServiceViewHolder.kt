package com.tokopedia.product.detail.estimasiongkir.view.adapter.viewholder

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.estimasiongkir.data.model.ShippingServiceModel
import com.tokopedia.product.detail.estimasiongkir.view.adapter.ServiceProductAdapter
import kotlinx.android.synthetic.main.item_rates_estimation_service.view.*

class RatesEstimationServiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val adapter = ServiceProductAdapter()

    init {
        itemView.service_product_list.run {
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false)
            adapter = this@RatesEstimationServiceViewHolder.adapter
        }
    }

    fun bind(shippingServiceModel: ShippingServiceModel) {
        if(shippingServiceModel.etd.isEmpty()){
            itemView.service_title.text = itemView.context.getString(R.string.service_title_format_empty,
                    shippingServiceModel.name)
        } else {
            itemView.service_title.text = itemView.context.getString(R.string.service_title_format,
                    shippingServiceModel.name, shippingServiceModel.etd)
        }

        adapter.replaceProducts(shippingServiceModel.products)
    }
}
