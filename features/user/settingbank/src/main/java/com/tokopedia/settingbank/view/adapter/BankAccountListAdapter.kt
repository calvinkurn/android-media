package com.tokopedia.settingbank.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.settingbank.domain.model.BankAccount
import com.tokopedia.settingbank.domain.model.TemplateData
import com.tokopedia.settingbank.view.viewHolder.BankTNCViewHolder
import com.tokopedia.settingbank.view.viewHolder.BankAccountViewHolder


class BankAccountListAdapter(var bankList: ArrayList<BankAccount>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_BANK = 0
    private val VIEW_CATATAN = 1

    lateinit var inflater: LayoutInflater

    private var templateData: TemplateData? = null

    var bankAccountClickListener: BankAccountClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (!::inflater.isInitialized)
            inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_CATATAN -> {
                val itemView = inflater.inflate(BankTNCViewHolder.LAYOUT, parent, false)
                BankTNCViewHolder(itemView)
            }
            else -> {
                val itemView = inflater.inflate(BankAccountViewHolder.LAYOUT, parent, false)
                BankAccountViewHolder(itemView)
            }
        }

    }

    fun updateItem(list: ArrayList<BankAccount>) {
        bankList = list
        notifyDataSetChanged()
    }

    fun updateBankTNCNote(templateData: TemplateData) {
        this.templateData = templateData
        notifyDataSetChanged()
    }

    fun getBankAccountListSize(): Int = bankList.size

    override fun getItemCount(): Int {
        return templateData?.let { bankList.size + 1 } ?: bankList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == bankList.size) VIEW_CATATAN else VIEW_BANK
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BankAccountViewHolder -> holder.bind(bankList[position], bankAccountClickListener)
            is BankTNCViewHolder -> holder.bind(templateData)
        }
    }
}

interface BankAccountClickListener {
    fun deleteBankAccount(bankAccount: BankAccount)
    fun onClickDataContent(bankAccount: BankAccount)
}
