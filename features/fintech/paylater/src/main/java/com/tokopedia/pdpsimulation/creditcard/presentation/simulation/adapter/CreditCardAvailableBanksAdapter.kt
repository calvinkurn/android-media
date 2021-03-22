package com.tokopedia.pdpsimulation.creditcard.presentation.simulation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pdpsimulation.creditcard.domain.model.SimulationBank
import com.tokopedia.pdpsimulation.creditcard.presentation.simulation.viewholder.CreditCardBankInfoViewHolder
import com.tokopedia.pdpsimulation.creditcard.presentation.simulation.viewholder.CreditCardBankShimmerViewHolder

class CreditCardAvailableBanksAdapter(
        var bankList: ArrayList<SimulationBank>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            SHIMMER_VIEW -> CreditCardBankShimmerViewHolder.getViewHolder(inflater, parent)
            BANK_VIEW -> CreditCardBankInfoViewHolder.getViewHolder(inflater, parent)
            else -> throw IllegalStateException("Unsupported type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CreditCardBankInfoViewHolder -> holder.bindData(bankList[position])
        }
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