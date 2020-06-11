package com.tokopedia.withdraw.saldowithdrawal.presentation.adapter.viewholder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.withdraw.R
import kotlinx.android.synthetic.main.swd_item_add_bank.view.*

class BankSettingButtonViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val tvBankAccountSettingTitle: TextView = itemView.tvBankAccountSettingTitle

    fun bindData(numberOfAccount: Int, openAddBankAccount: () -> Unit,
                 openBankAccountSetting: () -> Unit) {
        val context: Context = itemView.context
        when (numberOfAccount) {
            in addBankAccountRange -> {
                tvBankAccountSettingTitle.setCompoundDrawablesWithIntrinsicBounds(MethodChecker
                        .getDrawable(itemView.context, R.drawable.swd_add_bank),
                        null, null, null);
                tvBankAccountSettingTitle.text = context.getString(R.string.swd_title_add_account_bank)
            }
            else -> {
                tvBankAccountSettingTitle.setCompoundDrawablesWithIntrinsicBounds(MethodChecker
                        .getDrawable(itemView.context, R.drawable.swd_ic_edit),
                        null, null, null);
                tvBankAccountSettingTitle.text = context.getString(R.string.swd_title_set_account_bank)
            }
        }
        itemView.setOnClickListener {
            if (numberOfAccount < 3)
                openAddBankAccount()
            else
                openBankAccountSetting()
        }
    }

    companion object {
        val LAYOUT_ID = R.layout.swd_item_add_bank
        val addBankAccountRange = 0..2

        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup) = BankSettingButtonViewHolder(
                inflater.inflate(LAYOUT_ID, parent, false)
        )
    }

}