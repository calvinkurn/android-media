package com.tokopedia.paylater.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.paylater.domain.model.SimulationBank
import com.tokopedia.paylater.presentation.viewholder.CreditCardBankInfoViewHolder
import com.tokopedia.paylater.presentation.viewholder.CreditCardBankShimmerViewHolder

class CreditCardAvailableBanksAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var bankList = arrayListOf<SimulationBank>()

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


    fun setBankList(bankList: ArrayList<SimulationBank>) {
        this.bankList = bankList
        notifyDataSetChanged()
    }

    companion object {
        const val SHIMMER_VIEW = 1
        const val BANK_VIEW = 2
    }
}