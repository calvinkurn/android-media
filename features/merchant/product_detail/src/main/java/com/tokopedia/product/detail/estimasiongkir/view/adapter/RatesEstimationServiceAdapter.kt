package com.tokopedia.product.detail.estimasiongkir.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.estimasiongkir.data.model.ShippingServiceModel
import com.tokopedia.product.detail.estimasiongkir.view.adapter.viewholder.BaseRatesEstimationViewHolder
import com.tokopedia.product.detail.estimasiongkir.view.adapter.viewholder.RatesEstimationBBViewHolder
import com.tokopedia.product.detail.estimasiongkir.view.adapter.viewholder.RatesEstimationServiceViewHolder


import java.util.ArrayList

class RatesEstimationServiceAdapter : RecyclerView.Adapter<BaseRatesEstimationViewHolder>() {
    private val shippingServiceModels = ArrayList<ShippingServiceModel>()
    var isBlackbox: Boolean = false
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRatesEstimationViewHolder {
        return if (viewType == TYPE_NON_BLACKBOX){
            RatesEstimationServiceViewHolder(parent.inflateLayout(R.layout.item_rates_estimation_service))
        } else {
            RatesEstimationBBViewHolder(parent.inflateLayout(R.layout.item_rates_estimation_blackbox))
        }

    }

    override fun onBindViewHolder(holder: BaseRatesEstimationViewHolder, position: Int) {
        when(holder){
            is RatesEstimationServiceViewHolder -> holder.bind(shippingServiceModels[position])
            is RatesEstimationBBViewHolder -> {
                if (position == 0)
                    holder.bindTitle(TITLE_DELIVERY, TITLE_ESTIMATION)
                else
                    holder.bind(shippingServiceModels[position - 1])
            }
        }
    }

    override fun getItemCount() = shippingServiceModels.size +
            if (isBlackbox && shippingServiceModels.isNotEmpty()) 1 else 0

    override fun getItemViewType(position: Int): Int {
        return if (!isBlackbox) TYPE_NON_BLACKBOX
        else if (position == 0) TYPE_BLACKBOX_TITLE
        else TYPE_BLACKBOX_CONTENT
    }

    fun updateShippingServices(shippingServiceModels: List<ShippingServiceModel>) {
        this.shippingServiceModels.clear()
        this.shippingServiceModels.addAll(shippingServiceModels)
        notifyDataSetChanged()
    }

    companion object {
        private const val TYPE_BLACKBOX_TITLE = 1
        private const val TYPE_BLACKBOX_CONTENT = 2
        private const val TYPE_NON_BLACKBOX = 3

        private const val TITLE_DELIVERY = "Pengiriman"
        private const val TITLE_ESTIMATION = "Estimasi Ongkir"
    }
}
