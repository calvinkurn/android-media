package com.tokopedia.product.detail.estimasiongkir.view.adapter.viewholder

import android.graphics.Typeface
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.estimasiongkir.data.model.ShippingServiceModel
import com.tokopedia.product.detail.estimasiongkir.view.adapter.ServiceProductAdapter
import kotlinx.android.synthetic.main.item_rates_estimation_blackbox.view.*
import kotlinx.android.synthetic.main.item_rates_estimation_service.view.*

sealed class BaseRatesEstimationViewHolder(view: View): RecyclerView.ViewHolder(view)

class RatesEstimationBBViewHolder(view: View): BaseRatesEstimationViewHolder(view){

    fun bindTitle(titleDelivery: String, titleEstimation: String){
        itemView.subtitle.gone()
        with(itemView.label_view){
            title = titleDelivery
            setContent(titleEstimation)
            setContentTypeface(Typeface.BOLD)
        }
    }

    fun bind(shippingServiceModel: ShippingServiceModel){
        if (shippingServiceModel.notes.isNotBlank()){
            itemView.subtitle.visible()
            itemView.subtitle.text = String.format("(%s)", shippingServiceModel.notes)
        } else
            itemView.subtitle.gone()

        with(itemView.label_view){
            title = shippingServiceModel.name
            setSubTitle(shippingServiceModel.etd)
            setContent(shippingServiceModel.rangePrice)
            setContentTypeface(Typeface.NORMAL)
        }
    }
}

class RatesEstimationServiceViewHolder(itemView: View) : BaseRatesEstimationViewHolder(itemView) {
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
