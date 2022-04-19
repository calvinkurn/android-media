package com.tokopedia.withdraw.saldowithdrawal.presentation.adapter.viewholder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.withdraw.R
import com.tokopedia.withdraw.saldowithdrawal.domain.model.CheckEligible
import kotlinx.android.synthetic.main.swd_item_add_bank.view.*

class BankSettingButtonViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val tvBankAccountSettingTitle: TextView = itemView.tvBankAccountSettingTitle

    fun bindData(numberOfAccount: Int, checkEligible: CheckEligible?,
                 openAddBankAccount: () -> Unit,
                 openBankAccountSetting: () -> Unit) {
        val context: Context = itemView.context
        val isJoinedRekeningPremium = checkEligible?.let {
            it.data.isIsPowerWD
        } ?: run {
            false
        }
        val drawable =when (numberOfAccount) {
            in addBankAccountRange -> {
                tvBankAccountSettingTitle.text = context.getString(R.string.swd_title_add_account_bank)
                MethodChecker.getDrawable(itemView.context, R.drawable.swd_add_bank)
            }
            else -> {
                tvBankAccountSettingTitle.text = context.getString(R.string.swd_title_set_account_bank)
                MethodChecker.getDrawable(itemView.context, R.drawable.swd_ic_edit)
            }
        }

        if (isJoinedRekeningPremium) {
            drawable.alpha = DISABLED_DRAWABLE_ALPHA
            tvBankAccountSettingTitle
                    .setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_32))
        } else {
            drawable.alpha = ENABLE_DRAWABLE_ALPHA
            tvBankAccountSettingTitle
                    .setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
            itemView.setOnClickListener {
                if (numberOfAccount < MAX_BANK_ACCOUNT_LIMIT)
                    openAddBankAccount()
                else
                    openBankAccountSetting()
            }
        }

        tvBankAccountSettingTitle.setCompoundDrawablesWithIntrinsicBounds(drawable,
                null, null, null)
    }

    companion object {
        private const val ENABLE_DRAWABLE_ALPHA = 255
        private const val DISABLED_DRAWABLE_ALPHA = 255
        private const val MAX_BANK_ACCOUNT_LIMIT = 3
        val LAYOUT_ID = R.layout.swd_item_add_bank
        private val addBankAccountRange = 0..2

        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup) = BankSettingButtonViewHolder(
                inflater.inflate(LAYOUT_ID, parent, false)
        )
    }

}