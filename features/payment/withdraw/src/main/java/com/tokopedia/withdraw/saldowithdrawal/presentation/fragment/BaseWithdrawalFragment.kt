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
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.coachmark.*
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
import com.tokopedia.withdraw.saldowithdrawal.analytics.WithdrawAnalytics
import com.tokopedia.withdraw.saldowithdrawal.di.component.WithdrawComponent
import com.tokopedia.withdraw.saldowithdrawal.domain.model.BankAccount
import com.tokopedia.withdraw.saldowithdrawal.domain.model.CheckEligible
import com.tokopedia.withdraw.saldowithdrawal.presentation.adapter.BankAccountAdapter
import com.tokopedia.withdraw.saldowithdrawal.presentation.dialog.DisabledAccountBottomSheet
import com.tokopedia.withdraw.saldowithdrawal.presentation.dialog.RekPremBankAccountInfoBottomSheet
import com.tokopedia.withdraw.saldowithdrawal.presentation.viewmodel.RekeningPremiumViewModel
import com.tokopedia.withdraw.saldowithdrawal.presentation.viewmodel.SaldoWithdrawalViewModel
import com.tokopedia.withdraw.saldowithdrawal.util.*
import kotlinx.android.synthetic.main.swd_fragment_base_withdrawal.*
import javax.inject.Inject

abstract class BaseWithdrawalFragment : BaseDaggerFragment(), BankAccountAdapter.BankAdapterListener {

    private lateinit var checkEligible: CheckEligible

    @Inject
    lateinit var userSession: dagger.Lazy<UserSession>

    @Inject
    lateinit var analytics: dagger.Lazy<WithdrawAnalytics>

    @Inject
    lateinit var remoteConfig: dagger.Lazy<SaldoWithdrawalRemoteConfig>

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    private val saldoWithdrawalViewModel: SaldoWithdrawalViewModel? by lazy(LazyThreadSafetyMode.NONE) {
        parentFragment?.let {
            val viewModelProvider = ViewModelProviders.of(it, viewModelFactory.get())
            viewModelProvider.get(SaldoWithdrawalViewModel::class.java)
        } ?: run {
            null
        }
    }

    private val rekeningPremiumViewModel: RekeningPremiumViewModel? by lazy(LazyThreadSafetyMode.NONE) {
        parentFragment?.let {
            val viewModelProvider = ViewModelProviders.of(it, viewModelFactory.get())
            viewModelProvider.get(RekeningPremiumViewModel::class.java)
        } ?: run {
            null
        }
    }

    private lateinit var bankAccountAdapter: BankAccountAdapter

    private var balance: Long = 0L

    private lateinit var accountBalanceType: SaldoWithdrawal

    override fun getScreenName(): String? = null

    override fun initInjector() {
        getComponent(WithdrawComponent::class.java).inject(this)
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
            rekeningPremiumViewModel?.let { observeCheckRPEligibility() }
        }
    }

    abstract fun onViewCreated(savedInstanceState: Bundle?)

    abstract fun updateWithdrawalHint(bankAccount: BankAccount?,
                                      withdrawalAmount: Long)

    abstract fun updateWithdrawalButtonState(bankAccount: BankAccount?,
                                             withdrawalAmount: Long)

    private fun observeCheckRPEligibility() {
        rekeningPremiumViewModel?.rekeningPremiumMutableData?.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    checkEligible = it.data
                    saldoWithdrawalViewModel?.let { observeBankListResponse() }
                }
            }
        })
    }

    private fun observeBankListResponse() {
        saldoWithdrawalViewModel?.bankListResponseMutableData?.observe(viewLifecycleOwner, Observer {
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
            recyclerBankList.layoutManager = object : LinearLayoutManager(it) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            bankAccountAdapter = BankAccountAdapter(analytics.get(), this, isRekPremLogoVisible())
            recyclerBankList.adapter = bankAccountAdapter
        }
    }

    private fun isRekPremLogoVisible(): Boolean {
        return when (accountBalanceType) {
            is SellerSaldoWithdrawal -> true
            is BuyerSaldoWithdrawal -> remoteConfig.get().isRekeningPremiumLogoVisibleToAll()
        }
    }

    private fun updateBankAccountAdapter(data: ArrayList<BankAccount>) {
        recyclerBankList.post {
            val needToShowRPCoachMark = isRekeningPremiumCoachMarkShown()
            bankAccountAdapter.updateBankList(data, checkEligible, !needToShowRPCoachMark)
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
        analytics.get().eventClickWithdrawalAll();
    }

    private fun createTermsAndConditionSpannable(context: Context): SpannableStringBuilder? {
        val originalText = getString(R.string.swd_tnc_full_text)
        val readMoreText = getString(R.string.swd_tnc_clickable_text)
        val spannableString = SpannableString(readMoreText)
        val startIndex = 0
        val endIndex = spannableString.length
        val color = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500)
        spannableString.setSpan(color, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                openTermsAndConditionBottomSheet()
                analytics.get().eventClickTANDC()
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
            val bottomSheetUnify = BottomSheetUnify().apply {
                isDragable = true
                isFullpage = true
                isHideable = true
            }
            bottomSheetUnify.setTitle(getString(R.string.swd_tnc_title))
            val view = layoutInflater.inflate(R.layout.swd_layout_withdraw_tnc,
                    null, true)
            val webView: TkpdWebView = view.findViewById(R.id.swd_tnc_webview)
            webView.settings.javaScriptEnabled = true
            webView.loadAuthUrl(WithdrawConstant.WEB_TNC_URL, userSession.get())
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

    override fun showCoachMarkOnRPIcon(iconView: View) {
        if (!isRekeningPremiumCoachMarkShown() && context != null) {
            updateRekeningPremiumCoachMarkShown()
            val coachMarks = ArrayList<CoachMarkItem>()
            coachMarks.add(CoachMarkItem(iconView,
                    getString(R.string.swd_join_premium_account_icon_title),
                    getString(R.string.swd_join_premium_account_icon_description),
                    CoachMarkContentPosition.TOP, ContextCompat.getColor(requireContext(),
                    com.tokopedia.unifyprinciples.R.color.Unify_N700_68)))
            val coachMark = CoachMarkBuilder()
                    .build()
            coachMark.show(activity, KEY_CAN_SHOW_RP_COACH_MARK, coachMarks, 0)
        }
    }

    override fun onDisabledBankClick(bankAccount: BankAccount) {
        if (checkEligible.data.isIsPowerWD)
            activity?.let {
                val disabledAccountBottomSheet = DisabledAccountBottomSheet.getInstance(bankAccount)
                disabledAccountBottomSheet.isFullpage = false
                disabledAccountBottomSheet.show(it.supportFragmentManager, "")
                disabledAccountBottomSheet.setCloseClickListener {
                    disabledAccountBottomSheet.dismiss()
                    analytics.get().onDisableAccountInfoSheetClose(bankAccount.bankName)
                }
                analytics.get().onDisableAccountInfoSheetOpen(bankAccount.bankName)
            }
        analytics.get().onDisableAccountClick(bankAccount.bankName)
    }

    override fun showPremiumAccountDialog(bankAccount: BankAccount) {
        activity?.let { activity ->
            val premiumAccountBottomSheet = RekPremBankAccountInfoBottomSheet
                    .getInstance(checkEligible, bankAccount)
            premiumAccountBottomSheet.isFullpage = false
            premiumAccountBottomSheet.show(activity.supportFragmentManager, "")
            analytics.get().onRekeningPremiumLogoClick()
        }
    }

    private fun initiateWithdrawal() {
        analytics.get().eventClickTarikSaldo()
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

    private fun isRekeningPremiumCoachMarkShown(): Boolean {
        context?.let {
            return CoachMarkPreference.hasShown(it, KEY_CAN_SHOW_RP_COACH_MARK)
        } ?: run {
            return true
        }
    }

    private fun updateRekeningPremiumCoachMarkShown() {
        context?.let {
            CoachMarkPreference.setShown(it, KEY_CAN_SHOW_RP_COACH_MARK, true)
        }
    }

    companion object {
        const val KEY_CAN_SHOW_RP_COACH_MARK = "com.tokopedia.withdraw.saldowithdrawal.rekprem_logo_coach_mark"
    }
}

