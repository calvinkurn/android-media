package com.tokopedia.sellerorder.detail.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.ItemDynamicPriceDetailBinding
import com.tokopedia.sellerorder.detail.data.model.SomDetailPayments
import com.tokopedia.utils.view.binding.viewBinding

class SomDetailDynamicPriceAdapter(private val dynamicPriceList: List<SomDetailPayments.PricingData>) :
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

        private val binding by viewBinding<ItemDynamicPriceDetailBinding>()

        fun bind(data: SomDetailPayments.PricingData) {
            binding?.run {
                dynamicPriceLabel.text = data.label
                dynamicPriceValue.text = data.value
            }
        }
    }
}