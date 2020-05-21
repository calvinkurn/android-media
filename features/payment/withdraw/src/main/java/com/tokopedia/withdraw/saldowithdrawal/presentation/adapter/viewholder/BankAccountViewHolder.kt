package com.tokopedia.withdraw.saldowithdrawal.presentation.adapter.viewholder

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.withdraw.R
import com.tokopedia.withdraw.saldowithdrawal.domain.model.BankAccount
import kotlinx.android.synthetic.main.swd_item_bank_account.view.*

class BankAccountViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val ivButtonRadio: ImageView = view.radio
    private val bankName: TextView = view.bank_name
    private val bankAccountNumber: TextView = view.bank_acc_name
    private val bankAdminFee: TextView = view.tvAdminFee

    fun bindData(bankAccount: BankAccount, onBankAccountSelected: (BankAccount) -> Unit) {
        val context = itemView.context

        bankName.text = bankAccount.bankName
        bankAccountNumber.text = String.format("%s • %s", bankAccount.accountNo,
                bankAccount.accountName)

        if (bankAccount.adminFee > 0) {
            val bankAdminFeeStr: String = CurrencyFormatUtil
                    .convertPriceValueToIdrFormat(bankAccount.adminFee, false)
            bankAdminFee.text = context.getString(R.string.swd_admin_fee, bankAdminFeeStr)
            bankAdminFee.visible()
        } else {
            bankAdminFee.text = ""
            bankAdminFee.gone()
        }

        val drawable: Drawable = when {
            bankAccount.status == 0L -> {
                MethodChecker.getDrawable(context, R.drawable.swd_radio_disabled)
            }
            bankAccount.isChecked -> {
                MethodChecker.getDrawable(context, R.drawable.swd_radio_button_selected)
            }
            else -> {
                MethodChecker.getDrawable(context, R.drawable.swd_radio_button_default)
            }
        }
        ivButtonRadio.setImageDrawable(drawable)

        if (bankAccount.status == 0L) {
            bankName.setTextColor(context.resources.getColor(R.color.swd_grey_100))
            bankAccountNumber.setTextColor(context.resources.getColor(R.color.swd_grey_100))
            bankAdminFee.setTextColor(context.resources.getColor(R.color.swd_grey_100))
            itemView.setOnClickListener { }
        } else {
            bankName.setTextColor(context.resources.getColor(R.color.grey_796))
            bankAccountNumber.setTextColor(context.resources.getColor(R.color.grey_button_compat))
            bankAdminFee.setTextColor(context.resources.getColor(R.color.black_40))
            itemView.setOnClickListener { onBankAccountSelected(bankAccount) }
        }
    }

    companion object{
        val LAYOUT_ID = R.layout.swd_item_bank_account

        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup) = BankAccountViewHolder(
                inflater.inflate(LAYOUT_ID, parent, false)
        )
    }


}