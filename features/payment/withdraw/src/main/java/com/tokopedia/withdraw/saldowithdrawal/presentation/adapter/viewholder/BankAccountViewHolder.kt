package com.tokopedia.withdraw.saldowithdrawal.presentation.adapter.viewholder

import android.os.Build
import android.text.Html
import android.text.Html.FROM_HTML_MODE_COMPACT
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.NotificationUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import com.tokopedia.withdraw.R
import com.tokopedia.withdraw.saldowithdrawal.domain.model.BankAccount
import com.tokopedia.withdraw.saldowithdrawal.presentation.adapter.BankAccountAdapter
import kotlinx.android.synthetic.main.swd_item_bank_account.view.*

class BankAccountViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val radioBankSelector: RadioButtonUnify = view.radioBankSelector
    private val btnBankSelector: UnifyButton = view.btnBankSelector
    private val bankName: TextView = view.tvBankName
    private val bankAccountNumber: TextView = view.tvBankAccountHolderName
    private val bankAdminFee: TextView = view.tvAdminFee
    private val ivPremiumAccount: ImageView = view.ivPremiumAccount
    private val tvSpecialOffer: TextView = view.tvSpecialOffer
    private val warningMessage: TextView = view.tvWarningMessage
    private val ivBankIcon: ImageUnify = view.ivBankIcon
    private val notificationNew: NotificationUnify = view.notificationNew

    fun bindData(bankAccount: BankAccount, onBankAccountSelected: (BankAccount) -> Unit,
                 listener: BankAccountAdapter.BankAdapterListener,
                 isRpLogoVisible: Boolean) {
        val context = itemView.context

        bankAccount.bankImageUrl?.let {
            ivBankIcon.setImageUrl(it)
        }

        bankName.text = bankAccount.bankName
        bankAccountNumber.text = if (bankAccount.walletAppData.message.isNotEmpty()) {
            bankAccount.walletAppData.message
        }
        else if (bankAccount.accountName?.isNotEmpty() == true) {
            context.getString(R.string.swd_account_number_name, bankAccount.accountNo,
                bankAccount.accountName)
        } else bankAccount.accountNo

        if (bankAccountNumber.text.isEmpty()) bankAccountNumber.hide() else bankAccountNumber.show()

        if (bankAccount.isGopay()) {
            bankAdminFee.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(bankAccount.notes, Html.FROM_HTML_MODE_LEGACY)
            } else {
                Html.fromHtml(bankAccount.notes)
            }
            bankAdminFee.visible()
        }
        else if (bankAccount.adminFee > 0) {
            val bankAdminFeeStr: String = CurrencyFormatHelper.convertToRupiah(bankAccount.adminFee.toString())
            bankAdminFee.text = context.getString(R.string.swd_admin_fee, bankAdminFeeStr)
            bankAdminFee.visible()
        } else {
            bankAdminFee.text = ""
            bankAdminFee.gone()
        }

        if (bankAccount.warningMessage.isNullOrEmpty())
            warningMessage.gone()
        else {
            val color = getWarningTextColor(bankAccount.warningColor)
            warningMessage.visible()
            warningMessage.text = bankAccount.warningMessage
            warningMessage.setTextColor(ContextCompat.getColor(context, color))
        }

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
            radioBankSelector.isEnabled = false
            radioBankSelector.isClickable = false
            btnBankSelector.isEnabled = false
            btnBankSelector.isEnabled = false
        } else {
            ivPremiumAccount.alpha = ALPHA_ENABLED
            tvSpecialOffer.alpha = ALPHA_ENABLED
            bankName.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
            bankAccountNumber.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
            bankAdminFee.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
            itemView.setOnClickListener { onBankAccountSelected(bankAccount) }
            ivPremiumAccount.setOnClickListener { listener.showPremiumAccountDialog(bankAccount) }
            radioBankSelector.isEnabled = true
            radioBankSelector.isClickable = true
            btnBankSelector.isEnabled = true
            btnBankSelector.isEnabled = true
        }

        radioBankSelector.shouldShowWithAction(
            (bankAccount.walletAppData.ctaLink.isEmpty() &&
                bankAccount.walletAppData.message.isEmpty() &&
                bankAccount.isGopay()) ||
                !bankAccount.isGopay()
        ) {
            radioBankSelector.setOnClickListener {
                itemView.performClick()
            }
            radioBankSelector.isChecked = bankAccount.isChecked && bankAccount.status != INACTIVE_BANK_STATUS
        }

        btnBankSelector.shouldShowWithAction(bankAccount.walletAppData.ctaLink.isNotEmpty()) {
            btnBankSelector.text = bankAccount.walletAppData.ctaCopyWriting
            btnBankSelector.setOnClickListener {
                RouteManager.route(it.context, bankAccount.walletAppData.ctaLink)
            }
        }

        notificationNew.showWithCondition(bankAccount.isGopay())
    }

    private fun getWarningTextColor(warningColor: Int): Int {
        return if (warningColor == WARNING_COLOR_RED)
            com.tokopedia.unifyprinciples.R.color.Unify_R600
        else com.tokopedia.unifyprinciples.R.color.Unify_N700_32
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
        const val WARNING_COLOR_RED = 1
        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup) = BankAccountViewHolder(
                inflater.inflate(LAYOUT_ID, parent, false)
        )
    }

}
