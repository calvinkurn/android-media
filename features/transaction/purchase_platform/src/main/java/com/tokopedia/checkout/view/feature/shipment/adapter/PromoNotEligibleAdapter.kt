package com.tokopedia.checkout.view.feature.shipment.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.checkout.view.feature.shipment.viewholder.PromoNotEligibleViewHolder
import com.tokopedia.checkout.view.feature.shipment.viewmodel.NotEligiblePromoHolderdata

/**
 * Created by Irfan Khoirul on 2019-06-21.
 */

class PromoNotEligibleAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var notEligiblePromoHolderDataList = arrayListOf<NotEligiblePromoHolderdata>()

    override fun getItemViewType(position: Int): Int {
        return PromoNotEligibleViewHolder.LAYOUT
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return PromoNotEligibleViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notEligiblePromoHolderDataList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = notEligiblePromoHolderDataList[position]
        (holder as PromoNotEligibleViewHolder).bind(data)
    }
}