package com.tokopedia.power_merchant.subscribe.view_old.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view_old.model.PowerMerchantItemView
import com.tokopedia.power_merchant.subscribe.view_old.model.PowerMerchantItemView.*
import com.tokopedia.power_merchant.subscribe.view_old.viewholder.PowerMerchantItemViewHolder
import com.tokopedia.power_merchant.subscribe.view_old.viewholder.PowerMerchantItemViewHolder.*

class PowerMerchantViewAdapter(
    private val listener: PmViewHolderListener? = null
): RecyclerView.Adapter<PowerMerchantItemViewHolder>() {

    var items: List<PowerMerchantItemView> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PowerMerchantItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return PowerMerchantItemViewHolder(itemView, listener)
    }

    override fun getItemCount(): Int = items.count()

    override fun onBindViewHolder(holder: PowerMerchantItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemViewType(position: Int): Int {
        return when(items[position]) {
            is PowerMerchantFeature -> R.layout.item_power_merchant_feature
            else -> R.layout.item_power_merchant_benefit
        }
    }
}

