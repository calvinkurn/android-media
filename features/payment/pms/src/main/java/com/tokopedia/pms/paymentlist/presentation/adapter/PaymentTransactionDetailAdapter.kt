package com.tokopedia.pms.paymentlist.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pms.R
import com.tokopedia.pms.paymentlist.domain.data.VaTransactionItem

class PaymentTransactionDetailAdapter(var transactionList: ArrayList<VaTransactionItem>) :
    RecyclerView.Adapter<PaymentTransactionDetailAdapter.PaymentTransactionDetailViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PaymentTransactionDetailViewHolder {
        return PaymentTransactionDetailViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(LAYOUT_ID, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PaymentTransactionDetailViewHolder, position: Int) {
       //holder.bind(transactionList[position])
    }

    override fun getItemCount() = 3

    class PaymentTransactionDetailViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(vaTransactionItem: VaTransactionItem) {

        }
    }

    companion object {
        val LAYOUT_ID = R.layout.item_transaction_detail_item
    }
}