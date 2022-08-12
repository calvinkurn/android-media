package com.tokopedia.tokofood.feature.merchant.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokofood.databinding.ItemMerchantOpsHourBinding
import com.tokopedia.tokofood.feature.merchant.presentation.model.MerchantOpsHour
import com.tokopedia.tokofood.feature.merchant.presentation.viewholder.MerchantOpsHourViewHolder

class MerchantOpsHourAdapter : RecyclerView.Adapter<MerchantOpsHourViewHolder>() {

    private var merchantOpsHours: List<MerchantOpsHour> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MerchantOpsHourViewHolder {
        val binding = ItemMerchantOpsHourBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MerchantOpsHourViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MerchantOpsHourViewHolder, position: Int) {
        holder.bindData(merchantOpsHours[position])
    }

    override fun getItemCount(): Int {
        return merchantOpsHours.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setMerchantOpsHours(merchantOpsHours: List<MerchantOpsHour>) {
        this.merchantOpsHours = merchantOpsHours
        notifyDataSetChanged()
    }
}