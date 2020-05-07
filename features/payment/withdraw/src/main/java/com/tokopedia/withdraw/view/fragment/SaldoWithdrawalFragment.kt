package com.tokopedia.withdraw.view.fragment

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.network.URLGenerator
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.withdraw.R
import com.tokopedia.withdraw.WithdrawAnalytics
import com.tokopedia.withdraw.constant.WithdrawConstant
import com.tokopedia.withdraw.di.WithdrawComponent
import com.tokopedia.withdraw.domain.model.BankAccount
import com.tokopedia.withdraw.domain.model.WithdrawalRequest
import com.tokopedia.withdraw.domain.model.premiumAccount.CheckEligible
import com.tokopedia.withdraw.domain.model.premiumAccount.CopyWriting
import com.tokopedia.withdraw.domain.model.validatePopUp.ValidatePopUpData
import com.tokopedia.withdraw.domain.model.validatePopUp.ValidatePopUpWithdrawal
import com.tokopedia.withdraw.view.adapter.SaldoWithdrawalPagerAdapter
import com.tokopedia.withdraw.view.viewmodel.BankAccountListViewModel
import com.tokopedia.withdraw.view.viewmodel.RekeningPremiumViewModel
import com.tokopedia.withdraw.view.viewmodel.ValidatePopUpViewModel
import kotlinx.android.synthetic.main.swd_fragment_saldo_withdrawal.*
import kotlinx.android.synthetic.main.swd_layout_premium_account.*
import javax.inject.Inject

class SaldoWithdrawalFragment : BaseDaggerFragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSession

    @Inject
    lateinit var analytics: WithdrawAnalytics

    lateinit var withdrawalRequest: WithdrawalRequest

    private var sellerSaldoBalance: Long = 0L
    private var buyerSaldoBalance: Long = 0L
    private var showMclBlockTickerFirebaseFlag: Boolean = false
    private var mclLateCount = 0
    private var sellerWithdrawalLocked = 0

    private lateinit var checkEligible: CheckEligible
    private lateinit var saldoWithdrawalPagerAdapter: SaldoWithdrawalPagerAdapter

    private lateinit var bankAccountListViewModel: BankAccountListViewModel
    private lateinit var rekeningPremiumViewModel: RekeningPremiumViewModel
    private lateinit var validatePopUpViewModel: ValidatePopUpViewModel


    lateinit var validatePopUpAlertDialog: AlertDialog

    override fun getScreenName(): String? {
        return null
    }

    override fun initInjector() {
        getComponent(WithdrawComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            sellerSaldoBalance = it.getLong(ARG_SELLER_SALDO_BALANCE_LONG, 0L)
            buyerSaldoBalance = it.getLong(ARG_BUYER_SALDO_BALANCE_LONG, 0L)
            showMclBlockTickerFirebaseFlag = it.getBoolean(ARG_FIREBASE_FLAG_STATUS_BOOLEAN, false)
            mclLateCount = it.getInt(ARG_MCL_LATE_COUNT_INT, 0)
            sellerWithdrawalLocked = it.getInt(ARG_SELLER_WITHDRAWAL_LOCK_INT, 0)
        }
        initViewModels()
    }

    private fun initViewModels() {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
        bankAccountListViewModel = viewModelProvider.get(BankAccountListViewModel::class.java)
        rekeningPremiumViewModel = viewModelProvider.get(RekeningPremiumViewModel::class.java)
        validatePopUpViewModel = viewModelProvider.get(ValidatePopUpViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.swd_fragment_saldo_withdrawal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showSellerBlockedTicker()
        initializeViewPager()
        observeViewModel()
    }

    private fun observeViewModel() {
        bankAccountListViewModel.bankListResponseMutableData.observe(this, Observer {
            when (it) {
                is Success -> {
                    if (!userSession.isMsisdnVerified) {
                        showMustVerify()
                    }
                }
            }
        })

        rekeningPremiumViewModel.rekeningPremiumMutableData.observe(this, Observer {
            when (it) {
                is Success -> {
                    checkEligible = it.data
                    inflateRekeningPremiumWidget()
                }
                is Fail -> {
                    //block complete UI if loading failed...
                    //todo handle it as it is required during final withdrawal call...
                }
            }
        })


        validatePopUpViewModel.validatePopUpWithdrawalMutableData.observe(this, Observer {
            when (it) {
                is Success -> {
                    checkAndCreateValidatePopup(it.data)
                }
                is Fail -> {
                    //Tidak ada koneksi internet
                    //show toaster if failed
                    //todo handle with error message
                }
            }
        }
        )
    }

    private fun inflateRekeningPremiumWidget() {
        if (!::checkEligible.isInitialized)
            return
        val rekeningData = checkEligible.data
        val isClicked = WithdrawConstant.isRekeningPremiumWidgetClicked(context)
        if (rekeningData.isPowerMerchant) {
            val copyWriting = rekeningData.copyWriting
            copyWriting.let {
                layoutRekeningWidget.visibility = View.VISIBLE
                if (!isClicked)
                    tv_baru_tag.visibility = View.VISIBLE
                tv_rekeningTitle.text = copyWriting.title
                tv_briProgramDescription.text = Html.fromHtml(copyWriting.subtitle)
                tv_briProgramButton.text = copyWriting.cta
                tv_briProgramButton.setOnClickListener { onRekeningWidgetClick() }
            }
        }
    }

    private fun onRekeningWidgetClick() {
        if (!::checkEligible.isInitialized)
            return
        analytics.eventOnPremiumProgramWidgetClick()
        when (checkEligible.data.statusInt) {
            REKENING_ACCOUNT_APPROVED_IN -> {
                openBottomSheetForRekeningProgram(checkEligible.data.isIsPowerWD,
                        checkEligible.data.copyWriting)
            }
            else -> openRekeningAccountWebLink(checkEligible.data.copyWriting.url)
        }
    }

    private fun openBottomSheetForRekeningProgram(isRegisterForProgram: Boolean,
                                                  copyWriting: CopyWriting) {
        val briProgramBottomSheet = CloseableBottomSheetDialog
                .createInstanceRounded(activity);
        val view = layoutInflater.inflate(R.layout.swd_program_tarik_saldo,
                null, true)
        if (isRegisterForProgram) {
            (view.findViewById<View>(R.id.tv_wdProgramTitle) as TextView)
                    .text = getString(R.string.swd_rekening_premium)
            (view.findViewById<View>(R.id.tv_wdProgramDescription) as TextView)
                    .text = getString(R.string.swd_rekening_premium_description)
            (view.findViewById<View>(R.id.wdProgramContinue) as TextView)
                    .text = getString(R.string.swd_rekening_premium_btn)
            analytics.eventClickInfo()
        } else {
            (view.findViewById<View>(R.id.tv_wdProgramTitle) as TextView)
                    .text = getString(R.string.swd_rekening_premium)
            (view.findViewById<View>(R.id.tv_wdProgramDescription) as TextView)
                    .text = getString(R.string.swd_program_tarik_saldo_description)
            (view.findViewById<View>(R.id.wdProgramContinue) as TextView)
                    .text = getString(R.string.swd_program_tarik_btn)
            analytics.eventClickJoinNow()
        }

        view.findViewById<View>(R.id.wdProgramContinue).setOnClickListener {
            WithdrawConstant.saveRekeningPremiumWidgetClicked(context)
            briProgramBottomSheet.dismiss()
            openRekeningAccountWebLink(copyWriting.url)
        }

        briProgramBottomSheet.setContentView(view)
        briProgramBottomSheet.show()
    }

    private fun openRekeningAccountWebLink(url: String?) {
        url?.let {
            val resultGenerateUrl = URLGenerator.generateURLSessionLogin(
                    Uri.encode(url), userSession.deviceId, userSession.userId)
            RouteManager.route(context, resultGenerateUrl)
            analytics.eventClickGotoDashboard()
        }
    }

    private fun showMustVerify() {
        AlertDialog.Builder(activity!!)
                .setTitle(activity!!.getString(R.string.alert_not_verified_yet_title))
                .setMessage(activity!!.getString(R.string.alert_not_verified_yet_body))
                .setPositiveButton(activity!!.getString(R.string.alert_not_verified_yet_positive)) { dialog: DialogInterface?, which: Int ->
                    if (activity != null) {
                        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.SETTING_PROFILE)
                        startActivity(intent)
                        activity!!.finish()
                    }
                }
                .setNegativeButton(activity!!.getString(R.string.alert_not_verified_yet_negative)) { _: DialogInterface?, _: Int ->
                    if (activity != null) {
                        activity!!.finish()
                    }
                }
                .setCancelable(false)
                .show()
    }

    private fun initializeViewPager() {
        arguments?.let {
            saldoWithdrawalPagerAdapter = SaldoWithdrawalPagerAdapter(context!!, childFragmentManager)
            saldoWithdrawalPagerAdapter.fragmentList.add(BuyerSaldoWithdrawalFragment.getInstance(it))
            saldoWithdrawalPagerAdapter.fragmentList.add(SellerSaldoWithdrawalFragment.getInstance(it))
            viewPagerSaldoWithdrawal.adapter = saldoWithdrawalPagerAdapter
            tabSaldoWithdrawal.setupWithViewPager(viewPagerSaldoWithdrawal)
            selectInitialPage()
        }
    }

    private fun selectInitialPage() {
        if (buyerSaldoBalance == 0L) {
            tabSaldoWithdrawal.getTabAt(1)?.select()
        }
    }

    private fun showSellerBlockedTicker() {
        if ((sellerWithdrawalLocked == MCL_STATUS_BLOCK1
                        || sellerWithdrawalLocked == MCL_STATUS_BLOCK3) && showMclBlockTickerFirebaseFlag) {
            var tickerMsg = getString(R.string.saldolock_tickerDescription)
            val startIndex = tickerMsg.indexOf("Bayar Sekarang")
            val late = mclLateCount.toString()
            tickerMsg = String.format(resources.getString(R.string.saldolock_tickerDescription), late)
            val spannableString = SpannableString(tickerMsg)
            tickerLayout.findViewById<TextView>(R.id.tv_desc_info)
                    .movementMethod = LinkMovementMethod.getInstance()
            spannableString.setSpan(object : ClickableSpan() {
                override fun onClick(view: View) {
                    RouteManager.route(context, String.format("%s?url=%s",
                            ApplinkConst.WEBVIEW, WithdrawConstant.SALDOLOCK_PAYNOW_URL))
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                    ds.color = resources.getColor(R.color.tkpd_main_green)
                }
            }, startIndex - 1, tickerMsg.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            tickerLayout.findViewById<TextView>(R.id.tv_desc_info).text = spannableString
            tickerLayout.findViewById<View>(R.id.iv_dismiss_ticker)
                    .setOnClickListener { tickerLayout.visibility = View.GONE }
            tickerLayout.visibility = View.VISIBLE
        }

    }

    fun openAddBankAccount() {
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalGlobal.ADD_BANK)
        startActivityForResult(intent, BANK_SETTING_REQUEST_CODE)
    }

    fun openBankAccountSetting() {
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalGlobal.SETTING_BANK)
        startActivityForResult(intent, BANK_SETTING_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            BANK_SETTING_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (::bankAccountListViewModel.isInitialized)
                        bankAccountListViewModel.loadBankAccountList()
                    showToaster(getString(R.string.swd_bank_added_success))
                }
            }
            VERIFICATION_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.let {
                        onVerificationCompleted(data)
                    }
                }
            }
        }
    }

    private fun showToaster(message: String) {
        view?.let {
            Toaster.make(it, message, Toaster.LENGTH_SHORT)
        }
    }

    private fun getProgramName(): String {
        if (checkEligible.data.program != null)
            return checkEligible.data.program ?: ""
        return ""
    }

    fun initiateBuyerWithdrawal(selectedBankAccount: BankAccount, withdrawalAmount: Long) {
        withdrawalRequest = WithdrawalRequest(
                userId = userSession.userId, email = userSession.email,
                withdrawal = withdrawalAmount, bankAccount = selectedBankAccount,
                isSellerWithdrawal = false, programName = getProgramName())
        validatePopUpViewModel.checkForValidatePopup(selectedBankAccount)
    }

    fun initiateSellerWithdrawal(selectedBankAccount: BankAccount, withdrawalAmount: Long) {
        withdrawalRequest = WithdrawalRequest(
                userId = userSession.userId, email = userSession.email,
                withdrawal = withdrawalAmount, bankAccount = selectedBankAccount,
                isSellerWithdrawal = true, programName = getProgramName())
        validatePopUpViewModel.checkForValidatePopup(selectedBankAccount)
    }

    private fun checkAndCreateValidatePopup(validatePopUpWithdrawal: ValidatePopUpWithdrawal) {
        validatePopUpWithdrawal.data?.let {
            if (it.needShow) {
                showValidationPopUp(it)
            } else {
                openUserVerificationScreen()
            }
        } ?: run {
            openUserVerificationScreen()
        }
    }

    private fun showValidationPopUp(data: ValidatePopUpData) {
        validatePopUpAlertDialog = getConfirmationDialog(data.title, data.note,
                onContinue = {
                    analytics.eventClickContinueBtn()
                    validatePopUpAlertDialog.cancel()
                    openUserVerificationScreen()
                },
                onCancelClick = {
                    validatePopUpAlertDialog.cancel()
                }).create()
        validatePopUpAlertDialog.show()
    }

    private fun getConfirmationDialog(heading: String, description: String,
                                      onContinue: () -> Unit,
                                      onCancelClick: () -> Unit): AlertDialog.Builder {
        val dialogBuilder = AlertDialog.Builder(activity!!)
        val inflater = activity!!.layoutInflater
        val dialogView = inflater.inflate(R.layout.confirmation_dialog, null)
        (dialogView.findViewById<View>(R.id.heading) as TextView).text = heading
        (dialogView.findViewById<View>(R.id.description) as TextView).text = Html.fromHtml(description)
        dialogView.findViewById<View>(R.id.continue_btn).setOnClickListener { onContinue() }
        dialogView.findViewById<View>(R.id.back_btn).setOnClickListener { onCancelClick() }
        return dialogBuilder.setView(dialogView)
    }

    private fun openUserVerificationScreen() {
        val OTP_TYPE_ADD_BANK_ACCOUNT = 120
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalGlobal.COTP)
        val bundle = Bundle()
        bundle.putString(ApplinkConstInternalGlobal.PARAM_EMAIL, userSession.email)
        bundle.putString(ApplinkConstInternalGlobal.PARAM_MSISDN, userSession.phoneNumber)
        bundle.putBoolean(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, true)
        bundle.putInt(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, OTP_TYPE_ADD_BANK_ACCOUNT)
        bundle.putBoolean(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, true)
        intent.putExtras(bundle)
        startActivityForResult(intent, VERIFICATION_REQUEST_CODE)
    }

    private fun onVerificationCompleted(data: Intent) {
        if (data.hasExtra(ApplinkConstInternalGlobal.PARAM_UUID) ||
                ::withdrawalRequest.isInitialized) {
            val uuid: String = data.getStringExtra(ApplinkConstInternalGlobal.PARAM_UUID)
            submitWithdrawalRequest(withdrawalRequest, uuid)
        }
    }

    private fun submitWithdrawalRequest(withdrawalRequest: WithdrawalRequest, uuid: String) {
        view?.let {
            Toaster.make(it, "request submit call", Toaster.LENGTH_LONG)
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (::withdrawalRequest.isInitialized) {
            outState.putParcelable(WITHDRAWAL_REQUEST_DATA, withdrawalRequest)
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let {
            if (savedInstanceState.containsKey(WITHDRAWAL_REQUEST_DATA)) {
                withdrawalRequest = savedInstanceState.getParcelable(WITHDRAWAL_REQUEST_DATA)
            }
        }
    }

    companion object {
        const val WITHDRAWAL_REQUEST_DATA = "withdrawal_request_data"

        const val REKENING_ACCOUNT_APPROVED_IN: Int = 4

        const val BANK_SETTING_REQUEST_CODE = 3001
        const val VERIFICATION_REQUEST_CODE = 3002


        const val ARG_SELLER_SALDO_BALANCE_LONG = "seller_total_balance_int"
        const val ARG_BUYER_SALDO_BALANCE_LONG = "buyer_total_balance_int"
        const val ARG_SELLER_WITHDRAWAL_LOCK_INT = "is_lock"
        const val ARG_MCL_LATE_COUNT_INT = "late_count"
        const val ARG_FIREBASE_FLAG_STATUS_BOOLEAN = "is_on"


        const val MCL_STATUS_BLOCK1 = 700
        const val MCL_STATUS_BLOCK3 = 999

        @JvmStatic
        fun getFragmentInstance(bundle: Bundle) = SaldoWithdrawalFragment().apply {
            arguments = bundle
        }

    }
}
