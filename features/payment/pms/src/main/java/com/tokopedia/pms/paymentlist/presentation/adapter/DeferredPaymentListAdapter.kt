package com.tokopedia.pms.paymentlist.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pms.paymentlist.domain.data.BankTransferPaymentModel
import com.tokopedia.pms.paymentlist.domain.data.BasePaymentModel
import com.tokopedia.pms.paymentlist.domain.data.CreditCardPaymentModel
import com.tokopedia.pms.paymentlist.presentation.viewholder.BankTransferViewHolder
import com.tokopedia.pms.paymentlist.presentation.viewholder.CommonPaymentTransferViewHolder
import com.tokopedia.pms.paymentlist.presentation.viewholder.CreditCardTransferViewHolder

class DeferredPaymentListAdapter(private val actionItemListener: (Int, BasePaymentModel) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var deferredPaymentList = ArrayList<BasePaymentModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_BANK_TRANSFER -> BankTransferViewHolder.getViewHolder(
                LayoutInflater.from(parent.context),
                parent,
                actionItemListener
            )
            TYPE_CREDIT_CARD -> CreditCardTransferViewHolder.getViewHolder(
                LayoutInflater.from(
                    parent.context
                ), parent, actionItemListener
            )
            TYPE_GENERAL_PAYMENT -> CommonPaymentTransferViewHolder.getViewHolder(
                LayoutInflater.from(
                    parent.context
                ), parent, actionItemListener
            )
            else -> throw IllegalStateException("No such payment type found")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = deferredPaymentList[position]
        when {
            holder is BankTransferViewHolder && item is BankTransferPaymentModel -> holder.bind(item)
            holder is CreditCardTransferViewHolder && item is CreditCardPaymentModel -> holder.bind(
                item
            )
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

    fun addItems(data: ArrayList<BasePaymentModel>) {
        val positionStart = deferredPaymentList.size
        deferredPaymentList.addAll(data)
        if (positionStart == 0) notifyDataSetChanged()
        else notifyItemRangeInserted(positionStart, data.size)
    }

    fun clearAllElements() {
        deferredPaymentList.clear()
        notifyDataSetChanged()
    }

    private companion object {
        const val TYPE_GENERAL_PAYMENT = 1
        const val TYPE_BANK_TRANSFER = 2
        const val TYPE_CREDIT_CARD = 3
    }
}