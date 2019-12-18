package com.tokopedia.settingbank.choosebank.view.adapter.viewholder

import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
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
            if (element.highlight.isNullOrBlank()) {
                bankName.text = MethodChecker.fromHtml(element.bankName)
            } else if (element.highlight != null && element.bankName != null) {
                highlight(bankName, element.highlight!!, element.bankName!!)
            }

            bankRadio.isChecked = element.isSelected

            container.setOnClickListener { listener.onBankSelected(adapterPosition, element) }
            bankRadio.setOnClickListener { listener.onBankSelected(adapterPosition, element) }

        }
    }

    private fun highlight(bankNameTV: TextView, highlight: String, bankNameText: String) {
        val spannableString = SpannableString(bankNameText)

        var indexOfKeyword = spannableString.toString().toLowerCase().indexOf(highlight)

        while (indexOfKeyword < bankNameText.length && indexOfKeyword >= 0) {
            spannableString.setSpan(ForegroundColorSpan(MethodChecker.getColor(bankNameTV.context, R.color.settingbank_medium_green)), indexOfKeyword,
                    indexOfKeyword +
                            highlight.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            indexOfKeyword = spannableString.toString().indexOf(highlight, indexOfKeyword + highlight
                    .length)
        }

        bankNameTV.text = spannableString
    }

}