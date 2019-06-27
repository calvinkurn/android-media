package com.tokopedia.product.detail.estimasiongkir.view.adapter.viewholder

import android.graphics.Typeface
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.estimasiongkir.data.model.v3.ServiceModel
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

    fun bind(shippingServiceModel: ServiceModel){
        if (shippingServiceModel.texts.notes.isNotBlank()){
            itemView.subtitle.visible()
            itemView.subtitle.text = String.format("(%s)",
                    if (shippingServiceModel.status == 200) shippingServiceModel.texts.notes
                    else shippingServiceModel.error.message)
        } else
            itemView.subtitle.gone()

        with(itemView.label_view){
            title = shippingServiceModel.name
            setContent(shippingServiceModel.texts.rangePrice)
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

    fun bind(shippingServiceModel: ServiceModel) {
        itemView.service_title.text = itemView.context.getString(R.string.service_title_format_empty,
                shippingServiceModel.name)

        adapter.replaceProducts(shippingServiceModel.products)
    }
}
