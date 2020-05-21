package com.tokopedia.withdraw.saldowithdrawal.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.withdraw.R
import com.tokopedia.withdraw.saldowithdrawal.WithdrawAnalytics
import com.tokopedia.withdraw.saldowithdrawal.di.component.WithdrawComponent
import com.tokopedia.withdraw.saldowithdrawal.domain.model.BankAccount
import com.tokopedia.withdraw.saldowithdrawal.domain.model.SubmitWithdrawalResponse
import com.tokopedia.withdraw.saldowithdrawal.domain.model.WithdrawalRequest
import kotlinx.android.synthetic.main.swd_success_page.*
import javax.inject.Inject

class SuccessFragmentWithdrawal : BaseDaggerFragment() {

    @Inject
    lateinit var analytics: WithdrawAnalytics

    lateinit var withdrawalRequest: WithdrawalRequest
    lateinit var withdrawalResponse: SubmitWithdrawalResponse

    override fun initInjector() {
        getComponent(WithdrawComponent::class.java).inject(this)
    }

    override fun getScreenName(): String {
        return WithdrawAnalytics.SCREEN_WITHDRAW_SUCCESS_PAGE
    }

    override fun onStart() {
        super.onStart()
        analytics.sendScreen(screenName)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(ARG_WITHDRAWAL_REQUEST)
                    && it.containsKey(ARG_SUBMIT_WITHDRAWAL_RESPONSE)) {
                withdrawalRequest = it.getParcelable(ARG_WITHDRAWAL_REQUEST)
                withdrawalResponse = it.getParcelable(ARG_SUBMIT_WITHDRAWAL_RESPONSE)
            } else {
                activity?.finish()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.swd_success_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inflateUi()
    }

    private fun inflateUi() {
        activity?.let { activity ->
            val bankAccount: BankAccount = withdrawalRequest.bankAccount
            tvWithdrawalNote.text = withdrawalResponse.withdrawalNote
            tvBankName.text = bankAccount.bankName
            if (bankAccount.adminFee > 0) {
                tvAdminFees.text = String.format(activity.resources.getString(R.string.swd_admin_fee_msg)
                        ?: "", bankAccount.adminFee.toString())
                tvAdminFees.visibility = View.VISIBLE
            }
            tvAccountNumber.text = bankAccount.accountNo + "-" + bankAccount.accountName
            tvTotalWithdrawalAmount.text = CurrencyFormatUtil
                    .convertPriceValueToIdrFormat(withdrawalRequest.withdrawal, false)
        }
        btnOpenSaldoDetail.setOnClickListener { onGoToSaldoDetail() }
        btnShopOnTokopedia.setOnClickListener { onGoToHome() }
    }

    private fun onGoToSaldoDetail() {
        activity?.let { activity ->
            val resultIntent = Intent()
            activity.setResult(Activity.RESULT_OK, resultIntent)
            analytics.eventClickBackToSaldoPage()
            activity.finish()
        }
    }

    private fun onGoToHome() {
        activity?.let { activity ->
            RouteManager.route(context, ApplinkConst.HOME, "")
            analytics.eventClicGoToHomePage()
            activity.finish()
        }

    }

    companion object {
        private const val ARG_WITHDRAWAL_REQUEST = "arg_withdrawal_request"
        private const val ARG_SUBMIT_WITHDRAWAL_RESPONSE = "arg_submit_withdrawal_response"

        fun getInstance(withdrawalRequest: WithdrawalRequest,
                        submitWithdrawalResponse: SubmitWithdrawalResponse): Fragment {
            val successFragment: Fragment = SuccessFragmentWithdrawal()
            val bundle = Bundle()
            bundle.putParcelable(ARG_WITHDRAWAL_REQUEST, withdrawalRequest)
            bundle.putParcelable(ARG_SUBMIT_WITHDRAWAL_RESPONSE, submitWithdrawalResponse)
            successFragment.arguments = bundle
            return successFragment
        }
    }
}