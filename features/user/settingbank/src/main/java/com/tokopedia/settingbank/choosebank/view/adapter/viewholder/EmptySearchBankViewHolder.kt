package com.tokopedia.settingbank.choosebank.view.adapter.viewholder

import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.settingbank.R
import com.tokopedia.settingbank.choosebank.view.viewmodel.BankViewModel
import com.tokopedia.settingbank.choosebank.view.viewmodel.EmptySearchBankViewModel

/**
 * @author by nisie on 7/23/18.
 */

class EmptySearchBankViewHolder(val v: View) :
        AbstractViewHolder<EmptySearchBankViewModel>(v) {

    companion object {
        val LAYOUT = R.layout.settingbank_empty_search_result
    }

    private val titleText: TextView = itemView.findViewById(R.id.settingbank_empty_search_result_title)
    private val contentText: TextView = itemView.findViewById(R.id.settingbank_empty_search_result_content)

    override fun bind(element: EmptySearchBankViewModel?) {
        if (element != null) {

            titleText.visibility = View.INVISIBLE
            contentText.text = getString(R.string.hint_search_bank_no_result)

        }
    }

}