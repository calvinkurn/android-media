package com.tokopedia.settingbank.view.widgets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import com.tokopedia.settingbank.R
import com.tokopedia.settingbank.analytics.BankSettingAnalytics
import com.tokopedia.settingbank.domain.model.BankAccount
import com.tokopedia.settingbank.domain.model.KYCInfo
import com.tokopedia.settingbank.util.AccountConfirmationType
import com.tokopedia.settingbank.view.activity.AccountDocumentActivity
import com.tokopedia.settingbank.view.activity.SettingBankActivity
import com.tokopedia.unifycomponents.BottomSheetUnify

class AccountConfirmationBottomSheet : BottomSheetUnify() {

    private var bankAccount: BankAccount? = null
    private var kycInfo: KYCInfo? = null

    private val settingAnalytics: BankSettingAnalytics by lazy {
        BankSettingAnalytics()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.run {

            if (containsKey(BANK_ACCOUNT))
                bankAccount = getParcelable(BANK_ACCOUNT)

            if (containsKey(KYC_INFO))
                kycInfo = getParcelable(KYC_INFO)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initializeBottomSheet()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initializeBottomSheet() {
        context?.run {
            val child = LayoutInflater.from(this)
                    .inflate(R.layout.bottom_sheet_confirm_account, ConstraintLayout(this), false)
            setTitle(TITLE)
            setChild(child)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addDataToUI()
    }

    private fun addDataToUI() {
        view?.let { view ->
            view.findViewById<View>(R.id.viewCompanyAccountClickable).setOnClickListener {
                openConfirmBankAccountActivity(AccountConfirmationType.COMPANY.accountType)
            }
            view.findViewById<View>(R.id.viewFamilyAccountClickable).setOnClickListener {
                openConfirmBankAccountActivity(AccountConfirmationType.FAMILY.accountType)
            }
            view.findViewById<View>(R.id.viewOtherAccountClickable).setOnClickListener {
                openConfirmBankAccountActivity(AccountConfirmationType.OTHER.accountType)
            }
            kycInfo?.let {
                if (it.isVerified)
                    setGroupVisibility(view, View.VISIBLE)
                else {
                    setGroupVisibility(view, View.GONE)
                }
            }
        }
    }

    private fun setGroupVisibility(view: View, visibility: Int) {
        view.findViewById<View>(R.id.viewOtherAccount).visibility = visibility
        view.findViewById<View>(R.id.viewOtherAccountClickable).visibility = visibility
        view.findViewById<View>(R.id.ivOtherAccount).visibility = visibility
        view.findViewById<View>(R.id.tvOtherAccount).visibility = visibility
        view.findViewById<View>(R.id.tvErrorInInputData).visibility = visibility
    }

    private fun openConfirmBankAccountActivity(accountType: Int) {
        activity?.run {
            settingAnalytics.eventRekeningConfirmationClick()
            bankAccount?.let { bankAccount ->
                kycInfo?.let { kycInfo ->
                    startActivityForResult(AccountDocumentActivity
                            .createIntent(this, bankAccount, accountType, kycInfo.fullName),
                            SettingBankActivity.REQUEST_ON_DOC_UPLOAD)
                }
            }
        }
        this.dismiss()
    }

    companion object {
        private val TITLE = "Konfirmasi rekening bank"
        private val BANK_ACCOUNT = "bank_account"
        private val KYC_INFO = "kyc_info"
        private val TAG = "AccountConfirmationBottomSheet"
        fun showBottomSheet(bankAccount: BankAccount,
                            kycInfo: KYCInfo,
                            activity: FragmentActivity?) {
            activity?.let {
                if (!activity.isFinishing) {
                    val bottomSheet = AccountConfirmationBottomSheet()
                    val argument = Bundle();
                    argument.putParcelable(BANK_ACCOUNT, bankAccount)
                    argument.putParcelable(KYC_INFO, kycInfo)
                    bottomSheet.arguments = argument
                    bottomSheet.show(activity.supportFragmentManager, TAG)
                }
            }
        }
    }

}
