package com.tokopedia.settingbank.banklist.v2.view.widgets

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.settingbank.R
import com.tokopedia.settingbank.banklist.v2.domain.BankAccount
import com.tokopedia.settingbank.banklist.v2.domain.KYCInfo
import com.tokopedia.settingbank.banklist.v2.util.AccountConfirmationType
import com.tokopedia.settingbank.banklist.v2.view.activity.AccountConfirmActivity

class AccountConfirmationBottomSheet(val context: Context, val kycInfo: KYCInfo) : CloseableBottomSheetDialog.CloseClickedListener {

    override fun onCloseDialog() {
    }

    lateinit var dialog: CloseableBottomSheetDialog

    private lateinit var bankAccount: BankAccount

    fun show(bankAccount: BankAccount) {
        this.bankAccount = bankAccount
        val view = createBottomSheetView()
        if (!::dialog.isInitialized)
            dialog = CloseableBottomSheetDialog.createInstanceCloseableRounded(context, this)
        dialog.setCustomContentView(view, context.resources.getString(R.string.sbank_confirm_bank_account), true)
        dialog.show()
    }

    private fun createBottomSheetView(): View {
        val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_confirm_account, null)
        if (kycInfo.isVerified)
            view.findViewById<View>(R.id.groupOtherAccount).visible()
        else
            view.findViewById<View>(R.id.groupOtherAccount).gone()

        view.findViewById<View>(R.id.viewCompanyAccountClickable).setOnClickListener {
            openConfirmBankAccountActivity(AccountConfirmationType.COMPANY.accountType)
        }
        view.findViewById<View>(R.id.viewFamilyAccountClickable).setOnClickListener {
            openConfirmBankAccountActivity(AccountConfirmationType.FAMILY.accountType)
        }
        view.findViewById<View>(R.id.viewOtherAccountClickable).setOnClickListener {
            openConfirmBankAccountActivity(AccountConfirmationType.OTHER.accountType)
        }

        return view
    }

    private fun openConfirmBankAccountActivity(accountType: Int) {
        context.startActivity(AccountConfirmActivity.createIntent(context, bankAccount, accountType, kycInfo.fullName))
        this.dialog.dismiss()
    }

}
