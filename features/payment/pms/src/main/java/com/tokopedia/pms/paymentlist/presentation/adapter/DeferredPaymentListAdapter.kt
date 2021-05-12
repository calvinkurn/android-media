package com.tokopedia.pms.paymentlist.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pms.paymentlist.domain.data.*
import com.tokopedia.pms.paymentlist.presentation.viewholder.BankTransferViewHolder
import com.tokopedia.pms.paymentlist.presentation.viewholder.CommonPaymentTransferViewHolder
import com.tokopedia.pms.paymentlist.presentation.viewholder.CreditCardTransferViewHolder
import java.lang.IllegalStateException

class DeferredPaymentListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var deferredPaymentList = ArrayList<BasePaymentModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            TYPE_BANK_TRANSFER -> BankTransferViewHolder.getViewHolder(LayoutInflater.from(parent.context), parent)
            TYPE_CREDIT_CARD -> CreditCardTransferViewHolder.getViewHolder(LayoutInflater.from(parent.context), parent)
            TYPE_GENERAL_PAYMENT -> CommonPaymentTransferViewHolder.getViewHolder(LayoutInflater.from(parent.context), parent)
            else -> throw IllegalStateException("No such payment type found")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = deferredPaymentList[position]
        when {
            holder is BankTransferViewHolder && item is BankTransferPaymentModel -> holder.bind(item)
            holder is CreditCardTransferViewHolder && item is CreditCardPaymentModel -> holder.bind(item)
            holder is CommonPaymentTransferViewHolder -> holder.bind(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (deferredPaymentList[position]) {
            is CreditCardPaymentModel -> TYPE_CREDIT_CARD
            is BankTransferPaymentModel -> TYPE_BANK_TRANSFER
            else -> TYPE_GENERAL_PAYMENT
        }
    }

    override fun getItemCount() = deferredPaymentList.size

    fun addItems(list: ArrayList<BasePaymentModel>) {
        deferredPaymentList.addAll(list)
        notifyDataSetChanged()
    }

    private companion object {
        const val TYPE_GENERAL_PAYMENT = 1
        const val TYPE_BANK_TRANSFER = 2
        const val TYPE_CREDIT_CARD = 3
    }
}