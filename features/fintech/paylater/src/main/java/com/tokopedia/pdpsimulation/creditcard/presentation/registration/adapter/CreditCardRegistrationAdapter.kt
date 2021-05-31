package com.tokopedia.pdpsimulation.creditcard.presentation.registration.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pdpsimulation.creditcard.domain.model.BankCardListItem
import com.tokopedia.pdpsimulation.creditcard.domain.model.CreditCardItem
import com.tokopedia.pdpsimulation.creditcard.presentation.registration.viewholder.CreditCardRegistrationViewHolder
import com.tokopedia.pdpsimulation.creditcard.presentation.simulation.viewholder.CreditCardBankShimmerViewHolder
import kotlinx.android.synthetic.main.base_payment_register_item.view.*

class CreditCardRegistrationAdapter(
        var bankList: ArrayList<BankCardListItem>,
        val clickListener: (ArrayList<CreditCardItem>, String?, String?) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            SHIMMER_VIEW -> CreditCardBankShimmerViewHolder.getViewHolder(inflater, parent)
            BANK_VIEW -> {
                val viewHolder = CreditCardRegistrationViewHolder.getViewHolder(inflater, parent)
                initListeners(viewHolder)
                viewHolder
            }
            else -> throw IllegalStateException("Unsupported type: $viewType")
        }
    }

    private fun initListeners(viewHolder: CreditCardRegistrationViewHolder) {
        viewHolder.view.ivActionArrow.setOnClickListener {
            if (viewHolder.adapterPosition != RecyclerView.NO_POSITION) {
                val bankModel = bankList[viewHolder.adapterPosition]
                val cardList = bankModel.cardList
                cardList?.let { clickListener(it, bankModel.bankName, bankModel.bankSlug) }
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CreditCardRegistrationViewHolder)
            holder.bindData(bankList[position])
    }

    override fun getItemCount(): Int {
        return if (bankList.isEmpty()) 4 else bankList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (bankList.isEmpty()) SHIMMER_VIEW
        else BANK_VIEW
    }

    companion object {
        const val SHIMMER_VIEW = 1
        const val BANK_VIEW = 2
    }
}