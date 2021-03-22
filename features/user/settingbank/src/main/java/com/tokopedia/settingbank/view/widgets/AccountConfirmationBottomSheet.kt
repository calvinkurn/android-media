package com.tokopedia.settingbank.view.widgets

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.settingbank.R
import com.tokopedia.settingbank.analytics.BankSettingAnalytics
import com.tokopedia.settingbank.domain.model.BankAccount
import com.tokopedia.settingbank.domain.model.KYCInfo
import com.tokopedia.settingbank.util.AccountConfirmationType
import com.tokopedia.settingbank.view.activity.AccountDocumentActivity
import com.tokopedia.settingbank.view.activity.SettingBankActivity

class AccountConfirmationBottomSheet(val activity: Activity,
                                     val kycInfo: KYCInfo,
                                     val settingAnalytics: BankSettingAnalytics) : CloseableBottomSheetDialog.CloseClickedListener {

    override fun onCloseDialog() {
    }

    lateinit var dialog: CloseableBottomSheetDialog

    private lateinit var bankAccount: BankAccount

    fun show(bankAccount: BankAccount) {
        this.bankAccount = bankAccount
        val view = createBottomSheetView()
        if (!::dialog.isInitialized)
            dialog = CloseableBottomSheetDialog.createInstanceCloseableRounded(activity, this)
        dialog.setCustomContentView(view, activity.resources.getString(R.string.sbank_confirm_bank_account), true)
        dialog.show()
    }

    private fun createBottomSheetView(): View {
        val view = LayoutInflater.from(activity).inflate(R.layout.bottom_sheet_confirm_account, null)
        view.findViewById<View>(R.id.viewCompanyAccountClickable).setOnClickListener {
            openConfirmBankAccountActivity(AccountConfirmationType.COMPANY.accountType)
        }
        view.findViewById<View>(R.id.viewFamilyAccountClickable).setOnClickListener {
            openConfirmBankAccountActivity(AccountConfirmationType.FAMILY.accountType)
        }
        view.findViewById<View>(R.id.viewOtherAccountClickable).setOnClickListener {
            openConfirmBankAccountActivity(AccountConfirmationType.OTHER.accountType)
        }

        if (kycInfo.isVerified)
            setGroupVisibility(view, View.VISIBLE)
        else {
            setGroupVisibility(view, View.GONE)
        }

        return view
    }

    private fun setGroupVisibility(view: View, visibility: Int) {
        view.findViewById<View>(R.id.viewOtherAccount).visibility = visibility
        view.findViewById<View>(R.id.viewOtherAccountClickable).visibility = visibility
        view.findViewById<View>(R.id.ivOtherAccount).visibility = visibility
        view.findViewById<View>(R.id.tvOtherAccount).visibility = visibility
        view.findViewById<View>(R.id.tvErrorInInputData).visibility = visibility
    }

    private fun openConfirmBankAccountActivity(accountType: Int) {
        settingAnalytics.eventRekeningConfirmationClick()
        activity.startActivityForResult(AccountDocumentActivity.createIntent(activity, bankAccount, accountType, kycInfo.fullName),
                SettingBankActivity.REQUEST_ON_DOC_UPLOAD)
        this.dialog.dismiss()
    }

}
