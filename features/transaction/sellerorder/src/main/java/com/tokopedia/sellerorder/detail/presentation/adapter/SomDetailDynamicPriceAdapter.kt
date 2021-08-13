package com.tokopedia.sellerorder.detail.presentation.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.detail.data.model.SomDetailPayments
import kotlinx.android.synthetic.main.item_dynamic_price_detail.view.*

class SomDetailDynamicPriceAdapter(private val dynamicPriceList: List<SomDetailPayments.PricingData>):
        RecyclerView.Adapter<SomDetailDynamicPriceAdapter.SomDetailDynamicPriceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SomDetailDynamicPriceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dynamic_price_detail, parent, false)
        return SomDetailDynamicPriceViewHolder(view)
    }

    override fun onBindViewHolder(holder: SomDetailDynamicPriceViewHolder, position: Int) {
        holder.bind(dynamicPriceList[position])
    }

    override fun getItemCount(): Int = dynamicPriceList.size

    inner class SomDetailDynamicPriceViewHolder(view: View): RecyclerView.ViewHolder(view) {

        fun bind(data: SomDetailPayments.PricingData) {
            with(itemView) {
                dynamic_price_label.text = data.label
                dynamic_price_value.text = data.value
                if(data.textColor.isNotBlank()) {
                    dynamic_price_label.setTextColor(Color.parseColor(data.textColor))
                    dynamic_price_value.setTextColor(Color.parseColor(data.textColor))
                }
            }
        }
    }
}