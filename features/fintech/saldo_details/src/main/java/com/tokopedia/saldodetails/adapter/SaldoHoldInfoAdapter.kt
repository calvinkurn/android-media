package com.tokopedia.saldodetails.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.saldodetails.R
import com.tokopedia.saldodetails.response.model.saldoholdinfo.response.SellerDataItem
import com.tokopedia.saldodetails.viewholder.SaldoInfoItemViewHolder

class SaldoHoldInfoAdapter(val list: ArrayList<SellerDataItem>) : RecyclerView.Adapter<SaldoInfoItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaldoInfoItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hold_balance_info_item, parent, false)
        return SaldoInfoItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: SaldoInfoItemViewHolder, position: Int) {
        holder.bind(list[position])
    }
}