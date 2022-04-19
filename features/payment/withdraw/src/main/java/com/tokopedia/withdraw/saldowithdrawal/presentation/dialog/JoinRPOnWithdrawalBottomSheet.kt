package com.tokopedia.withdraw.saldowithdrawal.presentation.dialog

import android.content.Context
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.webview.TkpdWebView
import com.tokopedia.withdraw.R
import com.tokopedia.withdraw.saldowithdrawal.analytics.WithdrawAnalytics
import com.tokopedia.withdraw.saldowithdrawal.di.component.DaggerWithdrawComponent
import com.tokopedia.withdraw.saldowithdrawal.domain.model.BankAccount
import com.tokopedia.withdraw.saldowithdrawal.domain.model.JoinRekeningPremium
import com.tokopedia.withdraw.saldowithdrawal.helper.BulletPointSpan
import com.tokopedia.withdraw.saldowithdrawal.presentation.listener.WithdrawalJoinRPCallback
import com.tokopedia.withdraw.saldowithdrawal.presentation.viewmodel.JoinRekeningTermsConditionViewModel
import javax.inject.Inject

class JoinRPOnWithdrawalBottomSheet : BottomSheetUnify() {

    private val childLayoutRes = R.layout.swd_dialog_join_rp_on_withdrawal

    var callback: WithdrawalJoinRPCallback? = null
    private lateinit var tncTemplateStr: String
    private lateinit var bankAccount: BankAccount

    private lateinit var childView: View

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    @Inject
    lateinit var analytics: dagger.Lazy<WithdrawAnalytics>

    private val joinRekeningTermsConditionViewModel: JoinRekeningTermsConditionViewModel
            by lazy(LazyThreadSafetyMode.NONE) {
                ViewModelProviders.of(this,
                        viewModelFactory.get()).get(JoinRekeningTermsConditionViewModel::class.java)
            }


    private lateinit var joinRekeningPremium: JoinRekeningPremium

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(ARG_JOIN_REKENING_PREMIUM)) {
                initInjector()
                bankAccount = it.getParcelable(ARG_BANK_ACCOUNT) ?: BankAccount()
                joinRekeningPremium = it.getParcelable(ARG_JOIN_REKENING_PREMIUM) ?: JoinRekeningPremium()
                childView = LayoutInflater.from(context).inflate(childLayoutRes,
                        null, false)
                setChild(childView)
            } else
                dismiss()
        }
        addDataToUI()
        setCloseClickListener {
            dismiss()
            analytics.get().onBackFromWithdrawalJoinRPBottomSheet(bankAccount.bankName)
        }
    }

    private fun addDataToUI() {
        childView.findViewById<TextView>(R.id.tvJoinRekeningTitle).text = joinRekeningPremium.title
        context?.let {
            getDescription(it, childView.findViewById(R.id.descriptionBulletContainer))
            val tvTermsAndCondition = childView.findViewById<TextView>(R.id.tvJoinRekeningTNC)
            tvTermsAndCondition.movementMethod = LinkMovementMethod.getInstance()
            tvTermsAndCondition.text = createTermsAndConditionSpannable(it)
        }
        childView.findViewById<View>(R.id.btnJoinRPAndWithdrawal).setOnClickListener {
            dismiss()
            callback?.onWithdrawalAndJoinRekening(true)
            analytics.get().onClickWithdrawalBalanceAndJoin(bankAccount.bankName)
        }

        childView.findViewById<View>(R.id.btnWithdrawalOnly).setOnClickListener {
            dismiss()
            callback?.onWithdrawalAndJoinRekening(false)
            analytics.get().onClickOnlyWithdrawalSaldo(bankAccount.bankName)
        }

        analytics.get().onWithdrawalByJoiningOfferOpen(bankAccount.bankName)
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


    private fun getDescription(context: Context, container: LinearLayout) {
        if (!joinRekeningPremium.descriptionStringArray.isNullOrEmpty()) {
            val bulletSpacing = context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_8).toInt()
            val bulletRadius = context.resources.getDimension(R.dimen.swd_corner_radius)
            val color = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68)
            val bulletPointSpan = BulletPointSpan(bulletSpacing, bulletRadius, color)
            joinRekeningPremium.descriptionStringArray.forEach {
                val spannable = SpannableString(it)
                spannable.setSpan(bulletPointSpan, 0, it.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
                val view = LayoutInflater.from(context)
                        .inflate(R.layout.swd_item_join_rp_description, null, false)
                view.findViewById<TextView>(R.id.tvJoinRekeningDescription).text = spannable
                container.addView(view)
            }
        }
    }

    private fun createTermsAndConditionSpannable(context: Context): SpannableString {
        val originalText = getString(R.string.swd_tnc_rekening_account,
                joinRekeningPremium.programName)
        val termsAndConditionStr = getString(R.string.swd_tnc)
        val spannableString = SpannableString(originalText)
        val startIndex = originalText.indexOf(termsAndConditionStr)
        val endIndex = startIndex + termsAndConditionStr.length
        val color = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500)
        spannableString.setSpan(color, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                loadTermsAndCondition()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = color
            }
        }, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannableString
    }

    private fun observeViewModel() {
        joinRekeningTermsConditionViewModel.tncResponseMutableData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    this.tncTemplateStr = it.data
                    openTermsAndConditionBottomSheet()
                }
                is Fail -> {
                    showLoadingError(it.throwable)
                }
            }
        })
    }

    private fun loadTermsAndCondition() {
        if (!::tncTemplateStr.isInitialized) {
            observeViewModel()
            joinRekeningTermsConditionViewModel
                    .loadJoinRekeningTermsCondition(joinRekeningPremium.programID)
        } else {
            openTermsAndConditionBottomSheet()
        }
    }

    private fun showLoadingError(throwable: Throwable) {
        context?.let { context ->
            val message = ErrorHandler.getErrorMessage(context, throwable)
            view?.let { view ->
                Toaster.make(view, message, Toaster.LENGTH_SHORT)
            }
        }
    }

    private fun openTermsAndConditionBottomSheet() {
        activity?.let {
            val bottomSheetUnify = BottomSheetUnify()
            val view = layoutInflater.inflate(R.layout.swd_layout_withdraw_tnc,
                    null, true)
            val webView: TkpdWebView = view.findViewById(R.id.swd_tnc_webview)
            webView.loadData(tncTemplateStr, "text/html", "utf-8")
            bottomSheetUnify.setChild(view)
            bottomSheetUnify.show(it.supportFragmentManager, "")
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is WithdrawalJoinRPCallback) {
            callback = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }

    companion object {
        const val ARG_JOIN_REKENING_PREMIUM = "arg_join_rekening_premium"
        const val ARG_BANK_ACCOUNT = "arg_bank_account"

        fun getJoinRPOnWithdrawalBottomSheetInstance(bankAccount: BankAccount, joinRekeningPremium: JoinRekeningPremium)
                : JoinRPOnWithdrawalBottomSheet {
            return JoinRPOnWithdrawalBottomSheet().apply {
                val bundle = Bundle()
                bundle.putParcelable(ARG_JOIN_REKENING_PREMIUM, joinRekeningPremium)
                bundle.putParcelable(ARG_BANK_ACCOUNT, bankAccount)
                arguments = bundle
            }
        }
    }

}