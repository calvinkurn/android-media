package com.tokopedia.settingbank.view.adapter

import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.settingbank.R
import com.tokopedia.settingbank.view.viewmodel.BankAccountViewModel
import kotlinx.android.synthetic.main.item_bank_account.*
/**
 * @author by nisie on 6/12/18.
 */
class BankAccountViewHolder(itemView : View) : AbstractViewHolder<BankAccountViewModel>(itemView) {



    companion object {
        val LAYOUT = R.layout.item_bank_account
    }

    override fun bind(element: BankAccountViewModel?) {

    }
}