package com.tokopedia.withdraw.saldowithdrawal.presentation.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import com.tokopedia.withdraw.R
import com.tokopedia.withdraw.saldowithdrawal.analytics.WithdrawAnalytics
import com.tokopedia.withdraw.saldowithdrawal.di.component.WithdrawComponent
import com.tokopedia.withdraw.saldowithdrawal.domain.model.BankAccount
import com.tokopedia.withdraw.saldowithdrawal.domain.model.JoinPromptMessageResponse
import com.tokopedia.withdraw.saldowithdrawal.domain.model.SubmitWithdrawalResponse
import com.tokopedia.withdraw.saldowithdrawal.domain.model.WithdrawalRequest
import com.tokopedia.withdraw.saldowithdrawal.util.WithdrawConstant
import kotlinx.android.synthetic.main.swd_success_page.*
import javax.inject.Inject

class SuccessFragmentWithdrawal : BaseDaggerFragment(), TickerCallback {

    @Inject
    lateinit var analytics: dagger.Lazy<WithdrawAnalytics>

    private lateinit var withdrawalRequest: WithdrawalRequest
    private lateinit var withdrawalResponse: SubmitWithdrawalResponse

    override fun initInjector() {
        getComponent(WithdrawComponent::class.java).inject(this)
    }

    override fun getScreenName(): String {
        return WithdrawAnalytics.SCREEN_WITHDRAW_SUCCESS_PAGE
    }

    override fun onStart() {
        super.onStart()
        analytics.get().sendScreen(screenName)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(ARG_WITHDRAWAL_REQUEST)
                    && it.containsKey(ARG_SUBMIT_WITHDRAWAL_RESPONSE)) {
                withdrawalRequest = it.getParcelable(ARG_WITHDRAWAL_REQUEST) ?: WithdrawalRequest()
                withdrawalResponse = it.getParcelable(ARG_SUBMIT_WITHDRAWAL_RESPONSE) ?: SubmitWithdrawalResponse()
            } else {
                activity?.finish()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.swd_success_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inflateUi()
    }

    private fun inflateUi() {
        activity?.let { activity ->
            val bankAccount: BankAccount = withdrawalRequest.bankAccount
            tvWithdrawalTimeNote.text = withdrawalResponse.withdrawalNote
            tvBankName.text = bankAccount.bankName
            if (bankAccount.adminFee > 0) {
                tvAdminFees.text = String.format(activity.resources.getString(R.string.swd_admin_fee_msg)
                        ?: "", bankAccount.adminFee.toString())
                tvAdminFees.visible()
            }
            tvAccountNumber.text = bankAccount.accountNo + "-" + bankAccount.accountName
            tvTotalWithdrawalAmount.text = CurrencyFormatHelper.convertToRupiah(withdrawalRequest.withdrawal.toString())
            showRekeningWidgets(activity)
        }
        btnOpenSaldoDetail.setOnClickListener { onGoToSaldoDetail() }
    }

    private fun showRekeningWidgets(context: Context) {
        when {
            withdrawalRequest.isJoinRekeningPremium -> {
                withdrawalResponse.joinPromptMessageResponse?.let {
                    withdrawalSuccessTicker.visible()
                    cardUnifyJoinRekeningProgram.gone()
                    showJoinRekeningRequestTicker(it)
                }
            }
            withdrawalRequest.showJoinRekeningWidget -> {
                tvComeOnJoinRPDescriptionClickable.movementMethod = LinkMovementMethod()
                tvComeOnJoinRPDescriptionClickable
                        .text = getJoinRPProgramSpannableDescription(context)
                withdrawalSuccessTicker.gone()
                cardUnifyJoinRekeningProgram.visible()
                analytics.get().onShowJoinRekeningPremiumWidgetOnSuccessPage(
                        withdrawalRequest.bankAccount.bankName)
            }
            else -> {
                withdrawalSuccessTicker.gone()
                cardUnifyJoinRekeningProgram.gone()
                analytics.get().onNoTickerDisplayedOnSuccessPage(
                        getString(R.string.swd_label_no_ticker
                                , withdrawalRequest.bankAccount.bankName ?: ""))
            }
        }
    }

    private fun showJoinRekeningRequestTicker(joinPromptMessageResponse: JoinPromptMessageResponse) {
        withdrawalSuccessTicker.tickerTitle = joinPromptMessageResponse.title
        withdrawalSuccessTicker
                .setHtmlDescription(getString(R.string.swd_ticker_description_html,
                        joinPromptMessageResponse.description, joinPromptMessageResponse.actionText))
        withdrawalSuccessTicker.setDescriptionClickEvent(this)
        if (joinPromptMessageResponse.isSuccess) {
            withdrawalSuccessTicker.tickerType = Ticker.TYPE_ANNOUNCEMENT
            analytics.get().onViewRekeningPremiumApplicationIsINProgress(
                    withdrawalRequest.bankAccount.bankName)
        } else {
            withdrawalSuccessTicker.tickerType = Ticker.TYPE_WARNING
            analytics.get().onViewRekeningPremiumApplicationFailed(
                    String.format(LABEL_FORMAT_REASON,
                            withdrawalRequest.bankAccount.bankName,
                            joinPromptMessageResponse.description))
        }
    }


    private fun getJoinRPProgramSpannableDescription(context: Context): SpannableStringBuilder? {
        val originalText = getString(R.string.swd_come_on_join_rp_description)
        val tryNowStr = getString(R.string.swd_try_now)
        val spannableString = SpannableString(tryNowStr)
        val startIndex = 0
        val endIndex = spannableString.length
        val color = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500)
        spannableString.setSpan(color, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                WithdrawConstant.openRekeningAccountInfoPage(context)
                val label = String.format(LABEL_FORMAT_TICKER, withdrawalRequest.bankAccount.bankName,
                        getString(R.string.swd_come_on_join_rp))
                analytics.get().onSuccessPageRekeningPremiumLinkClick(label)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = color
            }
        }, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return SpannableStringBuilder.valueOf(originalText).append(" ").append(spannableString)
    }


    private fun onGoToSaldoDetail() {
        val eventLabel = when {
            withdrawalRequest.isJoinRekeningPremium -> {
                withdrawalResponse.joinPromptMessageResponse?.let {
                    String.format(LABEL_FORMAT_TICKER_REASON, withdrawalRequest.bankAccount.bankName,
                            it.title,it.description)
                }
            }
            withdrawalRequest.showJoinRekeningWidget -> {
                String.format(LABEL_FORMAT_TICKER_REASON, withdrawalRequest.bankAccount.bankName,
                        getString(R.string.swd_come_on_join_rp),
                        getString(R.string.swd_come_on_join_rp_description))
            }
            else -> String.format(LABEL_FORMAT_TICKER_REASON, withdrawalRequest.bankAccount.bankName, "", "")
        }
        activity?.let { activity ->
            val resultIntent = Intent()
            activity.setResult(Activity.RESULT_OK, resultIntent)
            eventLabel?.let {
                analytics.get().eventClickBackToSaldoPage(eventLabel)
            }
            activity.finish()
        }
    }


    override fun onDescriptionViewClick(linkUrl: CharSequence) {
        val joinPromptMessageResponse = withdrawalResponse.joinPromptMessageResponse
        joinPromptMessageResponse?.let {
            WithdrawConstant.openSessionBaseURL(context,
                    joinPromptMessageResponse.actionLink)
            if (joinPromptMessageResponse.isSuccess) {
                analytics.get().onSuccessPageRekeningPremiumLinkClick(
                        String.format(LABEL_FORMAT_TICKER, withdrawalRequest.bankAccount.bankName,
                                joinPromptMessageResponse.title)
                )
            } else {
                analytics.get().onClickUpgradeToPowerMerchant(
                        String.format(LABEL_FORMAT_TICKER, withdrawalRequest.bankAccount.bankName,
                                joinPromptMessageResponse.title)
                )
            }
        }

    }

    override fun onDismiss() {
        //no required as ticker don't have close button
    }

    fun onCloseButtonClick() {
        val joinPromptMessageResponse= withdrawalResponse.joinPromptMessageResponse
        val label = when {
            withdrawalRequest.isJoinRekeningPremium -> {
                joinPromptMessageResponse?.let {
                    String.format(LABEL_FORMAT_TICKER_REASON, withdrawalRequest.bankAccount.bankName,
                            joinPromptMessageResponse.title,
                            joinPromptMessageResponse.description)
                }
            }
            else -> String.format(LABEL_FORMAT_TICKER_REASON, withdrawalRequest.bankAccount.bankName, "", "")
        }
        label?.let {
            analytics.get().onClickCloseOnSuccessScreen(label)
        }
    }

    companion object {
        private const val LABEL_FORMAT_TICKER_REASON = "%s - ticker : %s  - reason : %s"
        private const val LABEL_FORMAT_TICKER = "%s - ticker : %s"
        private const val LABEL_FORMAT_REASON = "%s - reason : %s"
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