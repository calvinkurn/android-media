package com.tokopedia.pdpsimulation.creditcard.presentation.tnc.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pdpsimulation.creditcard.presentation.tnc.viewholder.CreditCardMinTransactionTableViewHolder
import com.tokopedia.pdpsimulation.creditcard.presentation.tnc.viewholder.CreditCardServiceFeeTableContentViewHolder

class CreditCardPdpTableInfoAdapter(var tableList: ArrayList<ArrayList<String>>, private val tableType: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (tableType) {
            VIEW_TYPE_TABLE_MIN_TRX -> CreditCardMinTransactionTableViewHolder.getViewHolder(inflater, parent)
            VIEW_TYPE_TABLE_SERVICE -> CreditCardServiceFeeTableContentViewHolder.getViewHolder(inflater, parent)
            else -> throw IllegalStateException("Invalid layout")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CreditCardMinTransactionTableViewHolder -> holder.bindData(tableList[position], position)
            is CreditCardServiceFeeTableContentViewHolder -> holder.bindData(tableList[position], position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return tableType
    }

    override fun getItemCount(): Int {
        return tableList.size
    }

    companion object {
        const val VIEW_TYPE_TABLE_MIN_TRX = 2
        const val VIEW_TYPE_TABLE_SERVICE = 3
    }
}