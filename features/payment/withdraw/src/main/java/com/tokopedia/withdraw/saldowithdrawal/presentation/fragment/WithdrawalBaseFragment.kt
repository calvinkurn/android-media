package com.tokopedia.withdraw.saldowithdrawal.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import com.tokopedia.utils.text.currency.NumberTextWatcher
import com.tokopedia.utils.text.currency.StringUtils
import com.tokopedia.webview.TkpdWebView
import com.tokopedia.withdraw.R
import com.tokopedia.withdraw.saldowithdrawal.WithdrawAnalytics
import com.tokopedia.withdraw.saldowithdrawal.constant.BuyerSaldoWithdrawal
import com.tokopedia.withdraw.saldowithdrawal.constant.SaldoWithdrawal
import com.tokopedia.withdraw.saldowithdrawal.constant.SellerSaldoWithdrawal
import com.tokopedia.withdraw.saldowithdrawal.constant.WithdrawConstant
import com.tokopedia.withdraw.saldowithdrawal.di.component.WithdrawComponent
import com.tokopedia.withdraw.saldowithdrawal.domain.model.BankAccount
import com.tokopedia.withdraw.saldowithdrawal.presentation.adapter.BankAccountAdapter
import com.tokopedia.withdraw.saldowithdrawal.presentation.adapter.layoutmanager.NonScrollableLinerLayoutManager
import com.tokopedia.withdraw.saldowithdrawal.presentation.viewmodel.SaldoWithdrawalViewModel
import kotlinx.android.synthetic.main.swd_fragment_base_withdrawal.*
import javax.inject.Inject

abstract class WithdrawalBaseFragment : BaseDaggerFragment(), BankAccountAdapter.BankAdapterListener {

    @Inject
    lateinit var userSession: UserSession

    @Inject
    lateinit var analytics: WithdrawAnalytics

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    private lateinit var bankAccountListViewModel: BankAccountListViewModel
    private lateinit var bankAccountAdapter: BankAccountAdapter

    private var balance: Long = 0L

    private lateinit var accountBalanceType: SaldoWithdrawal

    override fun getScreenName(): String? = null

    override fun initInjector() {
        getComponent(WithdrawComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModels()
    }

    private fun initViewModels() {
        var viewModelProvider: ViewModelProvider
        parentFragment?.let {
            viewModelProvider = ViewModelProviders.of(it, viewModelFactory)
            bankAccountListViewModel = viewModelProvider.get(BankAccountListViewModel::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.swd_fragment_base_withdrawal,
                container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreated(savedInstanceState)
        if (balance > 0) {
            initBankAccountRecycler()
            setCurrencyTextWatcherToSaldoInput()
            withdrawalButton.isEnabled = false
            tvCopyAllSaldoAmount.setOnClickListener { copyAllBalanceToWithdrawalAmount() }
            context?.let {
                tvTermsAndCondition.text = createTermsAndConditionSpannable(it)
            }
            tvTermsAndCondition.movementMethod = LinkMovementMethod.getInstance()
            saldoWithdrawalViewModel?.let { observeBaseViewModel() }
        }
    }

    abstract fun onViewCreated(savedInstanceState: Bundle?)

    abstract fun updateWithdrawalHint(bankAccount: BankAccount?,
                                      withdrawalAmount: Long)

    abstract fun updateWithdrawalButtonState(bankAccount: BankAccount?,
                                             withdrawalAmount: Long)

    private fun observeBaseViewModel() {
        bankAccountListViewModel.bankListResponseMutableData.observe(this, Observer {
            when (it) {
                is Success -> {
                    updateBankAccountAdapter(it.data)
                }
                is Fail -> {
                    updateBankAccountAdapter(arrayListOf())
                }
            }
        })
    }

    fun changeHint(isError: Boolean, hintText: String) {
        tfWithdrawal.setError(isError)
        tfWithdrawal.setMessage(hintText)
    }

    fun updateWithdrawalButton(canWithdraw: Boolean) {
        withdrawalButton.isEnabled = canWithdraw
        if (canWithdraw) {
            withdrawalButton.setOnClickListener {
                initiateWithdrawal()
            }
        }
    }

    fun setBalanceType(accountBalanceType: SaldoWithdrawal, balance: Long) {
        this.accountBalanceType = accountBalanceType
        this.balance = balance
        when (accountBalanceType) {
            is BuyerSaldoWithdrawal -> tvAccountBalanceType.text = getString(R.string.swd_saldo_refund)
            is SellerSaldoWithdrawal -> tvAccountBalanceType.text = getString(R.string.swd_saldo_seller)
        }
        this.balance = balance
        if (balance > 0L) {
            tvTotalSaldoBalance.text = getString(R.string.swd_rp,
                    CurrencyFormatHelper.convertToRupiah(balance.toString()))
        } else {
            showBlankState()
        }
    }

    private fun showBlankState() {
        editable_group.gone()
        ivLockButton.gone()
        emptyGroup.visibility = View.VISIBLE
        val message = when (accountBalanceType) {
            is BuyerSaldoWithdrawal -> getString(R.string.swd_refund_empty_msg)
            is SellerSaldoWithdrawal -> getString(R.string.swd_penghasilan_empty_msg)

        }
        emptySaldoDescription.text = message
    }

    private fun initBankAccountRecycler() {
        context?.let {
            recyclerBankList.layoutManager = NonScrollableLinerLayoutManager(context!!)
            bankAccountAdapter = BankAccountAdapter(analytics, this)
            recyclerBankList.adapter = bankAccountAdapter
        }
    }

    private fun updateBankAccountAdapter(data: ArrayList<BankAccount>) {
        recyclerBankList.post {
            bankAccountAdapter.updateBankList(data)
            bankAccountAdapter.notifyDataSetChanged()
            onBankAccountChanged()
        }
    }

    private fun setCurrencyTextWatcherToSaldoInput() {
        tfWithdrawal.textFieldInput.filters = arrayOf<InputFilter>(InputFilter
                .LengthFilter(WithdrawConstant.MAX_WITHDRAWAL_INPUT_LENGTH))
        tfWithdrawal.textFieldInput.addTextChangedListener(watcher())
    }

    private fun watcher(): NumberTextWatcher? {
        return object : NumberTextWatcher(tfWithdrawal.textFieldInput, "0") {
            override fun onNumberChanged(number: Double) {
                val withdrawal: Long = number.toLong()
                updateWithdrawalHint(bankAccountAdapter.getSelectedBankAccount(), withdrawal)
                if (withdrawal == 0L) {
                    tfWithdrawal.textFieldInput.setSelection(tfWithdrawal.textFieldInput.length())
                }
                updateWithdrawalButtonState(bankAccountAdapter.getSelectedBankAccount(), withdrawal)
            }
        }
    }

    private fun copyAllBalanceToWithdrawalAmount() {
        tfWithdrawal.textFieldInput.setText(balance.toString())
        tfWithdrawal.textFieldInput.setSelection(tfWithdrawal.textFieldInput.length())
        analytics.eventClickWithdrawalAll();
    }

    private fun createTermsAndConditionSpannable(context: Context): SpannableStringBuilder? {
        val originalText = getString(R.string.swd_tnc_full_text)
        val readMoreText = getString(R.string.swd_tnc_clickable_text)
        val spannableString = SpannableString(readMoreText)
        val startIndex = 0
        val endIndex = spannableString.length
        val color = ContextCompat.getColor(context, R.color.Green_G500)
        spannableString.setSpan(color, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                openTermsAndConditionBottomSheet()
                analytics.eventClickTANDC()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = color
            }
        }, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return SpannableStringBuilder.valueOf(originalText).append(" ").append(spannableString)
    }

    private fun openTermsAndConditionBottomSheet() {
        activity?.let {
            val bottomSheetUnify = BottomSheetUnify()
            val view = layoutInflater.inflate(R.layout.swd_layout_withdraw_tnc,
                    null, true)
            val webView: TkpdWebView = view.findViewById(R.id.swd_tnc_webview)
            webView.loadAuthUrl(WithdrawConstant.WEB_TNC_URL, userSession)
            bottomSheetUnify.setChild(view)
            bottomSheetUnify.show(it.supportFragmentManager, "")
        }

    }

    fun lockWithdrawal(isLocked: Boolean) {
        if (isLocked) {
            withdrawalButton.isEnabled = false
            withdrawalButton.isClickable = false
            ivLockButton.visible()
        } else {
            withdrawalButton.isEnabled = true
            withdrawalButton.isClickable = true
            ivLockButton.gone()
        }
    }

    override fun openAddBankAccount() {
        parentFragment?.let {
            if (it is SaldoWithdrawalFragment) {
                it.openAddBankAccount()
            }
        }
    }

    override fun openBankAccountSetting() {
        parentFragment?.let {
            if (it is SaldoWithdrawalFragment) {
                it.openBankAccountSetting()
            }
        }
    }

    override fun onBankAccountChanged() {
        val withdrawal: Long = StringUtils.convertToNumeric(tfWithdrawal.textFieldInput.text.toString(),
                false).toLong()
        updateWithdrawalHint(bankAccountAdapter.getSelectedBankAccount(), withdrawal)
        if (withdrawal == 0L) {
            tfWithdrawal.textFieldInput.setSelection(tfWithdrawal.textFieldInput.length())
        }
        updateWithdrawalButtonState(bankAccountAdapter.getSelectedBankAccount(), withdrawal)
    }

    private fun initiateWithdrawal() {
        analytics.eventClickTarikSaldo()
        bankAccountAdapter.getSelectedBankAccount()?.let { bankAccount ->
            val withdrawalAmount = StringUtils
                    .convertToNumeric(tfWithdrawal.textFieldInput.text.toString(),
                            false).toLong()
            if (parentFragment is SaldoWithdrawalFragment) {
                when (accountBalanceType) {
                    is BuyerSaldoWithdrawal -> {
                        (parentFragment as SaldoWithdrawalFragment)
                                .initiateBuyerWithdrawal(bankAccount, withdrawalAmount)
                    }
                    is SellerSaldoWithdrawal -> {
                        (parentFragment as SaldoWithdrawalFragment)
                                .initiateSellerWithdrawal(bankAccount, withdrawalAmount)
                    }
                }
            }
        }
    }

}

