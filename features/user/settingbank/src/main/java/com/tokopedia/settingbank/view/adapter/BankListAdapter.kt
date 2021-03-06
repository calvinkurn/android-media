package com.tokopedia.settingbank.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.settingbank.domain.model.Bank
import com.tokopedia.settingbank.view.viewHolder.BankViewHolder

class BankListAdapter(var bankList: ArrayList<Bank>) : RecyclerView.Adapter<BankViewHolder>() {

    lateinit var inflater: LayoutInflater

    internal var listener : BankListClickListener?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankViewHolder {
        if (!::inflater.isInitialized)
            inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(BankViewHolder.LAYOUT, parent, false)
        return BankViewHolder(itemView)


    }

    fun updateItem(list: ArrayList<Bank>) {
        bankList = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return bankList.size
    }

    override fun onBindViewHolder(holder: BankViewHolder, position: Int) {
        holder.bind(bankList[position], listener)
    }
}

interface BankListClickListener {
    fun onBankSelected(bank: Bank)
}