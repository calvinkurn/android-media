package com.tokopedia.purchase_platform.common.feature.promonoteligible

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.purchase_platform.common.databinding.ItemPromoRedStateBinding

/**
 * Created by Irfan Khoirul on 2019-06-21.
 */

class PromoNotEligibleAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var notEligiblePromoHolderDataList = arrayListOf<NotEligiblePromoHolderdata>()

    override fun getItemViewType(position: Int): Int {
        return PromoNotEligibleViewHolder.LAYOUT
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemPromoRedStateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PromoNotEligibleViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return notEligiblePromoHolderDataList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = notEligiblePromoHolderDataList[position]
        (holder as PromoNotEligibleViewHolder).bind(data)
    }
}