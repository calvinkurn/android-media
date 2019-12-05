package com.tokopedia.saldodetails.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.saldodetails.R
import com.tokopedia.saldodetails.response.model.saldoholdinfo.response.BuyerDataItem
import com.tokopedia.saldodetails.response.model.saldoholdinfo.response.SellerDataItem
import com.tokopedia.saldodetails.viewholder.SaldoInfoItemHeaderViewHolder
import com.tokopedia.saldodetails.viewholder.SaldoInfoItemViewHolder

class SaldoHoldInfoAdapter(val list: ArrayList<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var viewHolder: RecyclerView.ViewHolder
    val HEADER_VIEW_TYPE = 0
    val CARD_VIEW_TYPE = 1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            0 -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.hold_balance_info_header_item, parent, false)
                viewHolder = SaldoInfoItemHeaderViewHolder(view)
            }
            1 -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.hold_balance_info_item, parent, false)
                viewHolder = SaldoInfoItemViewHolder(view)
            }
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SaldoInfoItemHeaderViewHolder) {
            holder.bind()
        } else if (holder is SaldoInfoItemViewHolder) {
            holder.bind1()
            holder.bind2()
        }
    }

    override fun getItemViewType(position: Int): Int {

        if (list[position] is BuyerDataItem) {
            return CARD_VIEW_TYPE
        } else if (list[position] is SellerDataItem) {
            return CARD_VIEW_TYPE
        } else {
            return HEADER_VIEW_TYPE
        }
    }
}