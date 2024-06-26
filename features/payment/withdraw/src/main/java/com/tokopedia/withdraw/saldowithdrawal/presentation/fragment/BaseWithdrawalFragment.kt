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
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.coachmark.*
import com.tokopedia.config.GlobalConfig
import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.showWithCondition
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
import com.tokopedia.withdraw.saldowithdrawal.presentation.dialog.GopayRedirectionBottomSheet
import com.tokopedia.withdraw.saldowithdrawal.presentation.dialog.GopayWithdrawLimitBottomSheet
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.swd_fragment_base_withdrawal,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreated(savedInstanceState)
        if (balance > 0) {
            initBankAccountRecycler()
            setCurrencyTextWatcherToSaldoInput()
            withdrawalButton.isEnabled = false
            tvCopyAllSaldoAmount.setOnClickListener { copyAllBalanceToWithdrawalAmount() }
            tvBankSetting.setOnClickListener { openBankAccountSetting() }
            context?.let {
                tvTermsAndCondition.text = createTermsAndConditionSpannable(it)
            }
            tvTermsAndCondition.movementMethod = LinkMovementMethod.getInstance()
            rekeningPremiumViewModel?.let { observeCheckRPEligibility() }
        }
    }

    abstract fun onViewCreated(savedInstanceState: Bundle?)

    abstract fun updateWithdrawalHint(
        bankAccount: BankAccount?,
        withdrawalAmount: Long
    )

    abstract fun updateWithdrawalButtonState(
        bankAccount: BankAccount?,
        withdrawalAmount: Long
    )

    private fun observeCheckRPEligibility() {
        rekeningPremiumViewModel?.rekeningPremiumMutableData?.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        checkEligible = it.data
                        saldoWithdrawalViewModel?.let { observeBankListResponse() }
                    }
                    else -> {
                        // no op
                    }
                }
            }
        )
    }

    private fun observeBankListResponse() {
        saldoWithdrawalViewModel?.bankListResponseMutableData?.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        val gopayBank = it.data.bankAccountList.find { bank -> bank.isGopay() }
                        gopayBank?.let { bank ->
                            bank.gopayData = it.data.gopayData
                        }

                        updateBankAccountAdapter(it.data.bankAccountList)
                        setGopayTicker(it.data.bankAccountList)
                        tickerRP.showWithCondition(checkEligible.data.isIsPowerWD)
                    }
                    is Fail -> {
                        updateBankAccountAdapter(arrayListOf())
                    }
                }
            }
        )
    }

    fun changeHint(isError: Boolean, hintText: String) {
        tfWithdrawal.isInputError = isError
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
            tvTotalSaldoBalance.text = getString(
                R.string.swd_rp,
                CurrencyFormatHelper.convertToRupiah(balance.toString())
            )
        } else {
            showBlankState()
        }
    }

    private fun showBlankState() {
        editable_group.gone()
        bottom_content_group.gone()
        ivLockButton.gone()
        tvBankSetting.gone()
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

    private fun setGopayTicker(data: ArrayList<BankAccount>) {
        val gopayBankAccount = data.firstOrNull { it.isGopay() }
        val shouldShowInitially = gopayBankAccount?.defaultBankAccount == true
            && gopayBankAccount.isGopayEligible()
        val gopayData = gopayBankAccount?.gopayData

        gopayBankAccount?.let {
            tvGopayTickerDescription.text = gopayData?.widgetNote
            tvGopayTickerTitle.text = gopayData?.limitCopyWriting
            tvGopayTickerLimit.text = gopayData?.limit
            icGopayTickerInformation.setOnClickListener {
                activity?.let { activity ->
                    gopayData?.let {
                        GopayWithdrawLimitBottomSheet.getInstance(gopayData).show(
                            activity.supportFragmentManager,
                            GopayWithdrawLimitBottomSheet.TAG
                        )
                    }
                }
            }
        }

        gopayTickerGroup.showWithCondition(shouldShowInitially)
    }

    private fun setCurrencyTextWatcherToSaldoInput() {
        tfWithdrawal.editText.filters = arrayOf<InputFilter>(
            InputFilter
                .LengthFilter(WithdrawConstant.MAX_WITHDRAWAL_INPUT_LENGTH)
        )
        tfWithdrawal.editText.addTextChangedListener(watcher())
    }

    private fun watcher(): NumberTextWatcher? {
        return object : NumberTextWatcher(tfWithdrawal.editText, "0") {
            override fun onNumberChanged(number: Double) {
                val withdrawal: Long = number.toLong()
                updateWithdrawalHint(bankAccountAdapter.getSelectedBankAccount(), withdrawal)
                if (withdrawal == 0L) {
                    tfWithdrawal.editText.setSelection(tfWithdrawal.editText.length())
                }
                updateWithdrawalButtonState(bankAccountAdapter.getSelectedBankAccount(), withdrawal)
            }
        }
    }

    private fun copyAllBalanceToWithdrawalAmount() {
        tfWithdrawal.editText.setText(balance.toString())
        tfWithdrawal.editText.setSelection(tfWithdrawal.editText.length())
        analytics.get().eventClickWithdrawalAll()
    }

    private fun createTermsAndConditionSpannable(context: Context): SpannableStringBuilder? {
        val originalText = getString(R.string.swd_tnc_full_text)
        val readMoreText = getString(R.string.swd_tnc_clickable_text)
        val endText = getString(R.string.swd_tnc_end)
        val spannableString = SpannableString(readMoreText)
        val startIndex = 0
        val endIndex = spannableString.length
        val color = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
        spannableString.setSpan(color, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(
            object : ClickableSpan() {
                override fun onClick(widget: View) {
                    openTermsAndConditionBottomSheet()
                    analytics.get().eventClickTANDC()
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                    ds.color = color
                }
            },
            startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return SpannableStringBuilder.valueOf(originalText).append(" ").append(spannableString)
            .append(" ").append(endText)
    }

    private fun openTermsAndConditionBottomSheet() {
        activity?.let {
            val bottomSheetUnify = BottomSheetUnify().apply {
                isDragable = true
                isFullpage = true
                isHideable = true
            }
            bottomSheetUnify.setTitle(getString(R.string.swd_tnc_title))
            val view = layoutInflater.inflate(
                R.layout.swd_layout_withdraw_tnc,
                null,
                true
            )
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

    private fun trackOpenBankAccountSetting() {
        analytics.get().onClickManageAccount()
    }

    override fun onButtonClicked(applink: String) {
        if (GlobalConfig.isSellerApp() && (applink.contains(ApplinkConst.GOPAY_KYC) || applink.contains(
                ApplinkConst.LINK_ACCOUNT))) {
            activity?.let {
                GopayRedirectionBottomSheet.getInstance(
                    TokopediaImageUrl.IMG_OPEN_IN_MAIN_APP_WITHDRAW,
                    if (isUpgradeApplink(applink))
                        getString(R.string.swd_dialog_gopay_redirection_upgrade_title)
                    else getString(R.string.swd_dialog_gopay_redirection_activate_title),
                    if (isUpgradeApplink(applink))
                        getString(R.string.swd_dialog_gopay_redirection_upgrade_description)
                    else getString(R.string.swd_dialog_gopay_redirection_activate_description),
                    applink
                ).show(it.supportFragmentManager, GopayRedirectionBottomSheet.TAG)
            }
        } else {
            RouteManager.route(context, applink)
        }
    }

    override fun openAddBankAccount() {
        parentFragment?.let {
            if (it is SaldoWithdrawalFragment) {
                it.openAddBankAccount()
                trackOpenBankAccountSetting()
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
        gopayTickerGroup.showWithCondition(bankAccountAdapter.getSelectedBankAccount()?.isGopayEligible() == true)

        try {
            tfWithdrawal?.editText?.let { textFieldInput ->
                val inputText = (textFieldInput.text ?: "").toString()
                val withdrawal: Long = StringUtils.convertToNumeric(
                    inputText,
                    false
                ).toLong()
                updateWithdrawalHint(bankAccountAdapter.getSelectedBankAccount(), withdrawal)
                if (withdrawal == 0L) {
                    textFieldInput.setSelection(textFieldInput.length())
                }
                updateWithdrawalButtonState(bankAccountAdapter.getSelectedBankAccount(), withdrawal)
            }
        } catch (e: Exception) {
        }
    }

    override fun showCoachMarkOnRPIcon(iconView: View) {
        if (!isRekeningPremiumCoachMarkShown() && context != null) {
            updateRekeningPremiumCoachMarkShown()
            val coachMarks = ArrayList<CoachMarkItem>()
            coachMarks.add(
                CoachMarkItem(
                    iconView,
                    getString(R.string.swd_join_premium_account_icon_title),
                    getString(R.string.swd_join_premium_account_icon_description),
                    CoachMarkContentPosition.TOP,
                    ContextCompat.getColor(
                        requireContext(),
                        com.tokopedia.unifyprinciples.R.color.Unify_NN950_68
                    )
                )
            )
            val coachMark = CoachMarkBuilder()
                .build()
            coachMark.show(activity, KEY_CAN_SHOW_RP_COACH_MARK, coachMarks, 0)
        }
    }

    override fun showCoachMarkOnGopayBank(view: View, bankAccount: BankAccount) {
        if (!isGopayWithdrawCoachMarkShown() && context != null) {
            updateGopayWithdrawCoachmarkShown()

            val ctaLink = bankAccount.walletAppData.ctaLink

            val description = if (ctaLink.isEmpty()) getString(R.string.swd_coachmark_gopay_wd_description)
            else if (isUpgradeApplink(ctaLink)) getString(R.string.swd_coachmark_gopay_wd_upgrade_description)
            else getString(R.string.swd_coachmark_gopay_wd_activate_description)

            val coachMarkItem = ArrayList<CoachMark2Item>()
            coachMarkItem.add(
                CoachMark2Item(
                    view,
                    getString(R.string.swd_coachmark_gopay_wd_title),
                    description,
                    CoachMark2.POSITION_BOTTOM
                )
            )

            context?.let {
                CoachMark2(it).showCoachMark(coachMarkItem, null, 0)
            }
        }
    }

    override fun onDisabledBankClick(bankAccount: BankAccount) {
        if (checkEligible.data.isIsPowerWD) {
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
                .convertToNumeric(
                    tfWithdrawal.editText.text.toString(),
                    false
                ).toLong()
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

    private fun isGopayWithdrawCoachMarkShown(): Boolean {
        context?.let {
            return CoachMarkPreference.hasShown(it, KEY_CAN_SHOW_GOPAY_WITHDRAW_COACH_MARK)
        } ?: run {
            return true
        }
    }

    private fun updateRekeningPremiumCoachMarkShown() {
        context?.let {
            CoachMarkPreference.setShown(it, KEY_CAN_SHOW_RP_COACH_MARK, true)
        }
    }

    private fun updateGopayWithdrawCoachmarkShown() {
        context?.let {
            CoachMarkPreference.setShown(it, KEY_CAN_SHOW_GOPAY_WITHDRAW_COACH_MARK, true)
        }
    }

    private fun isUpgradeApplink(applink: String): Boolean = applink.contains(ApplinkConst.GOPAY_KYC)

    protected fun getExceedingWording(bankAccount: BankAccount?): String {
        return context?.let {
            if (bankAccount?.isGopay() == true)
                it.getString(R.string.swd_saldo_exceeding_withdraw_balance_gopay)
            else
                it.getString(R.string.swd_saldo_exceeding_withdraw_balance)
        } ?: ""
    }

    companion object {
        const val KEY_CAN_SHOW_RP_COACH_MARK = "com.tokopedia.withdraw.saldowithdrawal.rekprem_logo_coach_mark"
        const val KEY_CAN_SHOW_GOPAY_WITHDRAW_COACH_MARK = "com.tokopedia.withdraw.saldowithdrawal.gopay_withdraw_coach_mark"
    }
}
