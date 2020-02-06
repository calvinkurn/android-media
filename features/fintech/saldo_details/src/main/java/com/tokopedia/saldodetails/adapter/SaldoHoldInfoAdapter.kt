package com.tokopedia.saldodetails.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.saldodetails.R
import com.tokopedia.saldodetails.response.model.saldoholdinfo.response.BuyerDataItem
import com.tokopedia.saldodetails.response.model.saldoholdinfo.response.SellerDataItem
import com.tokopedia.saldodetails.viewholder.SaldoInfoItemViewHolder

class SaldoHoldInfoAdapter(val list: ArrayList<Any>, var type: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var viewHolder: RecyclerView.ViewHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hold_balance_info_item, parent, false)
        viewHolder = SaldoInfoItemViewHolder(view)
        return viewHolder

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is SaldoInfoItemViewHolder)
            when (type) {
                1 -> holder.bind1(list[position] as SellerDataItem)
                else ->
                    holder.bind2(list[position] as BuyerDataItem)
            }
    }
}
