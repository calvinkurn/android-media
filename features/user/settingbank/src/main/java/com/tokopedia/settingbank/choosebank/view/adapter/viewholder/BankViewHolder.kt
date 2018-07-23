package com.tokopedia.settingbank.choosebank.view.adapter.viewholder

import android.view.View
import android.widget.RadioButton
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.settingbank.R
import com.tokopedia.settingbank.choosebank.view.listener.BankListener
import com.tokopedia.settingbank.choosebank.view.viewmodel.BankViewModel

/**
 * @author by nisie on 6/22/18.
 */

class BankViewHolder(val v: View, val listener: BankListener) :
        AbstractViewHolder<BankViewModel>(v) {

    companion object {
        val LAYOUT = R.layout.item_bank
    }

    val bankName: TextView = itemView.findViewById(R.id.bank_name)
    val bankRadio: RadioButton = itemView.findViewById(R.id.bank_radio)
    val container: View = itemView.findViewById(R.id.container)

    override fun bind(element: BankViewModel?) {
        if (element != null) {
            bankName.text = MethodChecker.fromHtml(element.bankName)

            if (element.isSelected) {
                bankRadio.isChecked = true
            }

            container.setOnClickListener { listener.onBankSelected(adapterPosition, element) }
            bankRadio.setOnClickListener { listener.onBankSelected(adapterPosition, element) }

        }
    }

}