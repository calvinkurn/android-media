package com.tokopedia.withdraw.saldowithdrawal.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.withdraw.R
import com.tokopedia.withdraw.saldowithdrawal.analytics.WithdrawAnalytics
import com.tokopedia.withdraw.saldowithdrawal.di.component.DaggerWithdrawComponent
import com.tokopedia.withdraw.saldowithdrawal.domain.model.BankAccount
import com.tokopedia.withdraw.saldowithdrawal.util.WithdrawConstant
import javax.inject.Inject

class DisabledAccountBottomSheet : BottomSheetUnify() {

    private val childLayout = R.layout.swd_dialog_disabled_bank_account

    @Inject
    lateinit var analytics: dagger.Lazy<WithdrawAnalytics>

    lateinit var bankAccount: BankAccount

    private lateinit var childView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(ARG_BANK_ACCOUNT)) {
                bankAccount = it.getParcelable(ARG_BANK_ACCOUNT) ?: BankAccount()
            } else
                dismiss()
        }
        initInjector()
        childView = LayoutInflater.from(context).inflate(childLayout, null, false)
        setChild(childView)
        childView.findViewById<View>(R.id.btnCheckPremiumProgram).setOnClickListener {
            dismiss()
            WithdrawConstant.openRekeningAccountInfoPage(context)
            if (::bankAccount.isInitialized)
                analytics.get().onClickOpenRekPreInfoFromDisableAccount(
                        getString(R.string.swd_label_cek_rp_from_disable_bank_sheet,
                                bankAccount.bankName ?: ""))
        }
    }


    private fun initInjector() {
        activity?.let {
            DaggerWithdrawComponent.builder()
                    .baseAppComponent((it.application as BaseMainApplication).baseAppComponent)
                    .build().inject(this)
        } ?: run {
            dismiss()
        }
    }


    companion object {
        const val ARG_BANK_ACCOUNT = "arg_bank_account"

        fun getInstance(bankAccount: BankAccount)
                : DisabledAccountBottomSheet {
            return DisabledAccountBottomSheet().apply {
                val bundle = Bundle()
                bundle.putParcelable(ARG_BANK_ACCOUNT, bankAccount)
                arguments = bundle
            }
        }
    }


}