package com.tokopedia.settingbank.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.settingbank.domain.model.BankAccount
import com.tokopedia.settingbank.domain.model.TemplateData
import com.tokopedia.settingbank.view.viewHolder.BankTNCViewHolder
import com.tokopedia.settingbank.view.viewHolder.BankAccountViewHolder


class BankAccountListAdapter(var bankList: ArrayList<BankAccount>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var inflater: LayoutInflater

    var bankAccountClickListener: BankAccountClickListener? = null
    private var hasBankTncNote: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (!::inflater.isInitialized)
            inflater = LayoutInflater.from(parent.context)

        val itemView = inflater.inflate(viewType, parent, false)

        return when (viewType) {
            BankTNCViewHolder.LAYOUT -> BankTNCViewHolder(itemView)
            else -> BankAccountViewHolder(itemView)
        }
    }

    fun updateItem(list: ArrayList<BankAccount>) {
        bankList = list
        notifyDataSetChanged()
    }

    fun addBankTNCNote() {
        hasBankTncNote = true
        notifyDataSetChanged()
    }

    fun getBankAccountListSize(): Int = bankList.size

    override fun getItemCount(): Int {
        return if (hasBankTncNote) bankList.size + 1 else bankList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0 && hasBankTncNote)
            BankTNCViewHolder.LAYOUT
        else
            BankAccountViewHolder.LAYOUT
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BankAccountViewHolder -> holder.bind(
                bankList[if (hasBankTncNote) position - 1 else position],
                bankAccountClickListener,
            )
            is BankTNCViewHolder -> holder.bind()
        }
    }
}

interface BankAccountClickListener {
    fun deleteBankAccount(bankAccount: BankAccount)
    fun onClickDataContent(bankAccount: BankAccount)
}
