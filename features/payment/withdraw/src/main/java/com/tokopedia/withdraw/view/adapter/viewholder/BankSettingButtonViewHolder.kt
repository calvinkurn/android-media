package com.tokopedia.withdraw.view.adapter.viewholder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.withdraw.R
import kotlinx.android.synthetic.main.item_add_bank_withdraw.view.*

class BankSettingButtonViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    init {
        itemView.findViewById<TextView>(R.id.text)
                .setCompoundDrawablesWithIntrinsicBounds(MethodChecker
                        .getDrawable(itemView.context, R.drawable.ic_add_round),
                        null, null, null);
    }

    private val tvSettingTitle: TextView = itemView.text

    fun bindData(numberOfAccount: Int, openAddBankAccount: () -> Unit,
                 openBankAccountSetting: () -> Unit) {
        val context: Context = itemView.context
        tvSettingTitle.text = when (numberOfAccount) {
            in 0..2 -> context.getString(R.string.title_add_account_bank)
            3 -> context.getString(R.string.title_set_account_bank)
            else -> context.getString(R.string.title_set_rekening_utama)
        }
        itemView.setOnClickListener {
            if (numberOfAccount < 3)
                openAddBankAccount()
            else
                openBankAccountSetting()
        }
    }

    companion object {
        val LAYOUT_ID = R.layout.item_add_bank_withdraw

        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup) = BankSettingButtonViewHolder(
                inflater.inflate(LAYOUT_ID, parent, false)
        )
    }

}