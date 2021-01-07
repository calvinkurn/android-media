package com.tokopedia.paylater.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.paylater.domain.model.CreditCardBank
import com.tokopedia.paylater.presentation.viewholder.CreditCardBankInfoViewHolder

class CreditCardAvailableBanksAdapter : RecyclerView.Adapter<CreditCardBankInfoViewHolder>() {
    private var bankList = arrayListOf<CreditCardBank>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditCardBankInfoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CreditCardBankInfoViewHolder.getViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: CreditCardBankInfoViewHolder, position: Int) {
        holder.bindData(bankList[position])
    }

    override fun getItemCount(): Int {
        return bankList.size
    }

    fun setBankList(bankList: ArrayList<CreditCardBank>) {
        this.bankList = bankList
        notifyDataSetChanged()
    }
}