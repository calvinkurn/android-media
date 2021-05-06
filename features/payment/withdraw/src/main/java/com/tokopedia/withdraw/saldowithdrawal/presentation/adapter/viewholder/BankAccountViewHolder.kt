package com.tokopedia.withdraw.saldowithdrawal.presentation.adapter.viewholder

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import com.tokopedia.withdraw.R
import com.tokopedia.withdraw.saldowithdrawal.domain.model.BankAccount
import com.tokopedia.withdraw.saldowithdrawal.presentation.adapter.BankAccountAdapter
import kotlinx.android.synthetic.main.swd_item_bank_account.view.*

class BankAccountViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val ivButtonRadio: ImageView = view.ivBankSelector
    private val bankName: TextView = view.tvBankName
    private val bankAccountNumber: TextView = view.tvBankAccountHolderName
    private val bankAdminFee: TextView = view.tvAdminFee
    private val ivPremiumAccount: ImageView = view.ivPremiumAccount
    private val tvSpecialOffer: TextView = view.tvSpecialOffer

    fun bindData(bankAccount: BankAccount, onBankAccountSelected: (BankAccount) -> Unit,
                 listener: BankAccountAdapter.BankAdapterListener,
                 isRpLogoVisible: Boolean) {
        val context = itemView.context

        bankName.text = bankAccount.bankName
        bankAccountNumber.text = context.getString(R.string.swd_account_number_name, bankAccount.accountNo,
                bankAccount.accountName)

        if (bankAccount.adminFee > 0) {
            val bankAdminFeeStr: String = CurrencyFormatHelper.convertToRupiah(bankAccount.adminFee.toString())
            bankAdminFee.text = context.getString(R.string.swd_admin_fee, bankAdminFeeStr)
            bankAdminFee.visible()
        } else {
            bankAdminFee.text = ""
            bankAdminFee.gone()
        }

        val drawable: Drawable = when {
            (bankAccount.status == INACTIVE_BANK_STATUS) -> {
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

        if (bankAccount.haveRPProgram && isRpLogoVisible) {
            ivPremiumAccount.visible()
        } else {
            ivPremiumAccount.gone()
        }

        if (bankAccount.haveSpecialOffer) {
            tvSpecialOffer.visible()
        } else {
            tvSpecialOffer.gone()
        }

        if (bankAccount.status == INACTIVE_BANK_STATUS) {
            val disabledColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_32)
            bankName.setTextColor(disabledColor)
            bankAccountNumber.setTextColor(disabledColor)
            bankAdminFee.setTextColor(disabledColor)
            ivPremiumAccount.alpha = ALPHA_DISABLED
            tvSpecialOffer.alpha = ALPHA_DISABLED
            itemView.setOnClickListener { listener.onDisabledBankClick(bankAccount) }
            ivPremiumAccount.setOnClickListener { listener.onDisabledBankClick(bankAccount) }
        } else {
            ivPremiumAccount.alpha = ALPHA_ENABLED
            tvSpecialOffer.alpha = ALPHA_ENABLED
            bankName.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
            bankAccountNumber.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
            bankAdminFee.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
            itemView.setOnClickListener { onBankAccountSelected(bankAccount) }
            ivPremiumAccount.setOnClickListener { listener.showPremiumAccountDialog(bankAccount) }
        }

    }

    fun getPowerMerchantImageView(): View = ivPremiumAccount

    fun isPowerMerchantVisible(): Boolean {
        return ivPremiumAccount.isVisible
    }

    companion object {
        val LAYOUT_ID = R.layout.swd_item_bank_account
        const val ALPHA_DISABLED = 0.5F
        const val ALPHA_ENABLED = 1.0F
        const val INACTIVE_BANK_STATUS = 0
        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup) = BankAccountViewHolder(
                inflater.inflate(LAYOUT_ID, parent, false)
        )
    }

}