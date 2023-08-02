package com.tokopedia.withdraw.saldowithdrawal.presentation.adapter.viewholder

import android.content.Context
import android.graphics.PorterDuff
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
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
import com.tokopedia.unifyprinciples.R as principleR

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

        setBankImage(bankAccount)
        setBankName(bankAccount)
        setBankAccountNumber(context, bankAccount)
        setBankAdminFee(context, bankAccount)
        setRPicon(bankAccount, isRpLogoVisible)
        setSpecialOffer(bankAccount)
        configDisabledView(context, bankAccount, listener, onBankAccountSelected)
        setRadio(bankAccount)
        setButton(bankAccount, listener)
        setNotification(bankAccount)
    }

    private fun setBankImage(bankAccount: BankAccount) {
        bankAccount.bankImageUrl?.let {
            ivBankIcon.setImageUrl(it)
        }
    }

    private fun setBankName(bankAccount: BankAccount) {
        bankName.text = bankAccount.bankName
    }

    private fun setBankAccountNumber(context: Context, bankAccount: BankAccount) {
        bankAccountNumber.text = if (bankAccount.walletAppData.message.isNotEmpty()) {
            bankAccount.walletAppData.message
        }
        else if (bankAccount.accountName?.isNotEmpty() == true) {
            context.getString(R.string.swd_account_number_name, bankAccount.accountNo,
                bankAccount.accountName)
        } else bankAccount.accountNo

        if (bankAccountNumber.text.isEmpty()) bankAccountNumber.hide() else bankAccountNumber.show()
    }

    private fun setBankAdminFee(context: Context, bankAccount: BankAccount) {
        if (bankAccount.isGopay()) {
            bankAdminFee.shouldShowWithAction(bankAccount.notes.isNotEmpty()) {
                bankAdminFee.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml(bankAccount.notes, Html.FROM_HTML_MODE_LEGACY)
                } else {
                    Html.fromHtml(bankAccount.notes)
                }
            }
        }
        else if (bankAccount.adminFee > 0) {
            val bankAdminFeeStr: String = CurrencyFormatHelper.convertToRupiah(bankAccount.adminFee.toString())
            bankAdminFee.text = context.getString(R.string.swd_admin_fee, bankAdminFeeStr)
            bankAdminFee.visible()
        } else {
            bankAdminFee.text = ""
            bankAdminFee.gone()
        }
    }

    private fun setWarningMessage(context: Context, bankAccount: BankAccount) {
        if (bankAccount.warningMessage.isNullOrEmpty())
            warningMessage.gone()
        else {
            val color = getWarningTextColor(bankAccount.warningColor)
            warningMessage.visible()
            warningMessage.text = bankAccount.warningMessage
            warningMessage.setTextColor(ContextCompat.getColor(context, color))
        }
    }

    private fun setRPicon(bankAccount: BankAccount, isRpLogoVisible: Boolean) {
        if (bankAccount.haveRPProgram && isRpLogoVisible) {
            ivPremiumAccount.visible()
        } else {
            ivPremiumAccount.gone()
        }
    }

    private fun setSpecialOffer(bankAccount: BankAccount) {
        if (bankAccount.haveSpecialOffer) {
            tvSpecialOffer.visible()
        } else {
            tvSpecialOffer.gone()
        }
    }

    private fun configDisabledView(
        context: Context,
        bankAccount: BankAccount,
        listener: BankAccountAdapter.BankAdapterListener,
        onBankAccountSelected: (BankAccount) -> Unit
    ) {
        if (bankAccount.status == INACTIVE_BANK_STATUS) {
            val disabledColor = ContextCompat.getColor(context, principleR.color.Unify_NN400)
            bankName.setTextColor(disabledColor)
            bankAccountNumber.setTextColor(disabledColor)
            bankAdminFee.setTextColor(disabledColor)
            warningMessage.setTextColor(disabledColor)
            ivBankIcon.alpha = ALPHA_DISABLED
            ivPremiumAccount.alpha = ALPHA_DISABLED
            tvSpecialOffer.alpha = ALPHA_DISABLED
            itemView.setOnClickListener { listener.onDisabledBankClick(bankAccount) }
            ivPremiumAccount.setOnClickListener { listener.onDisabledBankClick(bankAccount) }
            radioBankSelector.isEnabled = false
            radioBankSelector.isClickable = false
            btnBankSelector.isEnabled = false
            btnBankSelector.isEnabled = false
            notificationNew.background.setColorFilter(
                ContextCompat.getColor(context, principleR.color.Unify_NN300),
                PorterDuff.Mode.SRC_ATOP
            )
        } else {
            ivBankIcon.alpha = ALPHA_ENABLED
            ivPremiumAccount.alpha = ALPHA_ENABLED
            tvSpecialOffer.alpha = ALPHA_ENABLED
            bankName.setTextColor(ContextCompat.getColor(context, principleR.color.Unify_NN950_96))
            bankAccountNumber.setTextColor(ContextCompat.getColor(context, principleR.color.Unify_NN950_68))
            bankAdminFee.setTextColor(ContextCompat.getColor(context, principleR.color.Unify_NN950_68))
            itemView.setOnClickListener { onBankAccountSelected(bankAccount) }
            ivPremiumAccount.setOnClickListener { listener.showPremiumAccountDialog(bankAccount) }
            radioBankSelector.isEnabled = true
            radioBankSelector.isClickable = true
            btnBankSelector.isEnabled = true
            btnBankSelector.isEnabled = true
            notificationNew.background.setColorFilter(
                ContextCompat.getColor(context, principleR.color.Unify_RN600),
                PorterDuff.Mode.SRC_ATOP
            )
            setWarningMessage(context, bankAccount)
        }
    }

    private fun setRadio(bankAccount: BankAccount) {
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
    }

    private fun setButton(bankAccount: BankAccount, listener: BankAccountAdapter.BankAdapterListener) {
        btnBankSelector.shouldShowWithAction(bankAccount.walletAppData.ctaLink.isNotEmpty()) {
            btnBankSelector.text = bankAccount.walletAppData.ctaCopyWriting
            btnBankSelector.setOnClickListener {
                listener.onButtonClicked(bankAccount.walletAppData.ctaLink)
            }
        }
    }

    private fun setNotification(bankAccount: BankAccount) {
        notificationNew.showWithCondition(bankAccount.isGopay())
    }

    private fun getWarningTextColor(warningColor: Int): Int {
        return if (warningColor == WARNING_COLOR_RED)
            com.tokopedia.unifyprinciples.R.color.Unify_RN500
        else com.tokopedia.unifyprinciples.R.color.Unify_NN950_32
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
