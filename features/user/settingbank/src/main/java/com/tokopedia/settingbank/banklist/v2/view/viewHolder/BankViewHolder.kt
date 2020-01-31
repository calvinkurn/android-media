package com.tokopedia.settingbank.banklist.v2.view.viewHolder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.settingbank.R
import com.tokopedia.settingbank.banklist.v2.domain.Bank
import com.tokopedia.settingbank.banklist.v2.view.adapter.BankListClickListener

class BankViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    private var bank: Bank? = null

    fun bind(bank: Bank, listener: BankListClickListener?) {
        this.bank = bank
        view.findViewById<TextView>(R.id.tvBankNameWithAbbreviation)
                .text = "${bank.abbreviation ?: ""} (${bank.bankName})"
        view.setOnClickListener {
            this.bank?.let {
                listener?.onBankSelected(bank)
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.sbank_item_bank
    }

}