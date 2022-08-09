package com.tokopedia.withdraw.saldowithdrawal.presentation.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.withdraw.R
import com.tokopedia.withdraw.saldowithdrawal.analytics.WithdrawAnalytics
import com.tokopedia.withdraw.saldowithdrawal.di.component.WithdrawComponent
import com.tokopedia.withdraw.saldowithdrawal.domain.exception.SubmitWithdrawalException
import com.tokopedia.withdraw.saldowithdrawal.domain.model.*
import com.tokopedia.withdraw.saldowithdrawal.presentation.activity.WithdrawActivity
import com.tokopedia.withdraw.saldowithdrawal.presentation.adapter.SaldoWithdrawalPagerAdapter
import com.tokopedia.withdraw.saldowithdrawal.presentation.dialog.JoinRPOnWithdrawalBottomSheet
import com.tokopedia.withdraw.saldowithdrawal.presentation.listener.WithdrawalJoinRPCallback
import com.tokopedia.withdraw.saldowithdrawal.presentation.viewmodel.RekeningPremiumViewModel
import com.tokopedia.withdraw.saldowithdrawal.presentation.viewmodel.SaldoWithdrawalViewModel
import com.tokopedia.withdraw.saldowithdrawal.presentation.viewmodel.SubmitWithdrawalViewModel
import com.tokopedia.withdraw.saldowithdrawal.util.WithdrawConstant
import kotlinx.android.synthetic.main.swd_fragment_saldo_withdrawal.*
import javax.inject.Inject

class SaldoWithdrawalFragment : BaseDaggerFragment(), WithdrawalJoinRPCallback, CarouselUnify.OnActiveIndexChangedListener {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    private val viewModelProvider: ViewModelProvider by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProviders.of(this, viewModelFactory.get())
    }

    @Inject
    lateinit var userSession: dagger.Lazy<UserSession>

    @Inject
    lateinit var analytics: dagger.Lazy<WithdrawAnalytics>

    private lateinit var withdrawalRequest: WithdrawalRequest

    private var sellerSaldoBalance: Long = 0L
    private var buyerSaldoBalance: Long = 0L
    private var showMclBlockTickerFirebaseFlag: Boolean = false
    private var mclLateCount = 0
    private var sellerWithdrawalLocked = 0

    private lateinit var checkEligible: CheckEligible
    private lateinit var saldoWithdrawalPagerAdapter: SaldoWithdrawalPagerAdapter

    private lateinit var submitWithdrawalViewModel: SubmitWithdrawalViewModel

    private val saldoWithdrawalViewModel: SaldoWithdrawalViewModel by lazy(LazyThreadSafetyMode.NONE) {
        viewModelProvider.get(SaldoWithdrawalViewModel::class.java)
    }

    private val rekeningPremiumViewModel: RekeningPremiumViewModel by lazy(LazyThreadSafetyMode.NONE) {
        viewModelProvider.get(RekeningPremiumViewModel::class.java)
    }

    private lateinit var validatePopUpAlertDialog: DialogUnify

    private lateinit var bannerList: ArrayList<BannerData>

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
        submitWithdrawalViewModel = viewModelProvider.get(SubmitWithdrawalViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.swd_fragment_saldo_withdrawal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingLayout.setOnClickListener { }
        loadingLayout.visible()
        observeViewModel()
    }

    private fun observeViewModel() {
        rekeningPremiumViewModel.rekeningPremiumMutableData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    checkEligible = it.data
                    saldoWithdrawalViewModel.getRekeningBannerDataList()
                }
                is Fail -> {
                    handleGlobalError(it.throwable) { rekeningPremiumViewModel.loadRekeningPremiumData() }
                }
            }
        })

        saldoWithdrawalViewModel.bannerListLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    addBannerToUI(it.data)
                    showUIComponent()
                    saldoWithdrawalViewModel.getBankList()
                }
                is Fail -> {
                    handleGlobalError(it.throwable) { saldoWithdrawalViewModel.getRekeningBannerDataList() }
                }
            }
        })

        saldoWithdrawalViewModel.bankListResponseMutableData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    if (!userSession.get().isMsisdnVerified) {
                        showMustVerify()
                    }
                }
                is Fail -> {
                    handleGlobalError(it.throwable) { saldoWithdrawalViewModel.getBankList() }
                }
            }
        })

        saldoWithdrawalViewModel.validatePopUpWithdrawalMutableData.observe(viewLifecycleOwner, Observer {
            loadingLayout.hide()
            when (it) {
                is Success -> {
                    checkAndCreateValidatePopup(it.data)
                }
                is Fail -> {
                    handleThrowable(it.throwable)
                }
            }
        })

        submitWithdrawalViewModel.submitWithdrawalResponseLiveData.observe(viewLifecycleOwner, Observer {
            loadingLayout.hide()
            when (it) {
                is Success -> {
                    redirectToSuccessFragment(it.data)
                }
                is Fail -> {
                    handleThrowable(it.throwable)
                }
            }
        })
    }

    private fun addBannerToUI(data: ArrayList<BannerData>) {
        bannerList = data
        if (data.isNotEmpty()) {
            withdrawalRekeningCarouselView.visible()
            withdrawalRekeningCarouselView.apply {
                activeIndex = 0
                autoplay = false
                centerMode = true
                if (data.size > 1) {
                    indicatorPosition = CarouselUnify.INDICATOR_BL
                    slideToShow = HALF_SLIDE_NO
                    addItems(R.layout.swd_widget_banner_item, data as ArrayList<Any>, ::carouselItemListener)
                } else {
                    indicatorPosition = CarouselUnify.INDICATOR_HIDDEN
                    slideToShow = FULL_SLIDE_NO
                    addItems(R.layout.swd_widget_banner_single_item, data as ArrayList<Any>, ::carouselItemListener)
                }
                onActiveIndexChangedListener = this@SaldoWithdrawalFragment
            }
        }
    }


    override fun onActiveIndexChanged(prev: Int, current: Int) {
        if(::bannerList.isInitialized){
            val bannerData = bannerList[current]
            if (!bannerData.isInvoke) {
                bannerData.invoke()
                if (bannerData.status == BANNER_WITH_CONTENT) {
                    analytics.get().onViewRekeningPointWidget(bannerData.text1)
                    analytics.get().onBannerItemView(bannerData.id, true)
                }else{
                    analytics.get().onBannerItemView(bannerData.id, false)
                }
            }
        }
    }

    private fun onCarouselItemClick(bannerData: BannerData) {
        WithdrawConstant.openSessionBaseURL(context, bannerData.cta)
        if (bannerData.status == BANNER_WITH_CONTENT) {
            analytics.get().onRekeningBannerClick(bannerData.text1)
            analytics.get().onBannerItemClick(bannerData.id, true)
        } else {
            analytics.get().onBannerItemClick(bannerData.id, false)
        }
    }

    private fun carouselItemListener(view: View, data: Any) {
        val img: ImageUnify = view.findViewById(R.id.imageUnifyBackground)
        val tvBannerTitle: TextView = view.findViewById(R.id.tvBannerTitle)
        val tvBannerDescriptionOne: TextView = view.findViewById(R.id.tvBannerDescriptionOne)
        val tvBannerDescriptionTwo: TextView = view.findViewById(R.id.tvBannerDescriptionTwo)
        val bannerTextGroup: Group = view.findViewById(R.id.bannerTextGroup)
        val bannerData = data as BannerData
        if (bannerData.status == BANNER_WITH_CONTENT) {
            bannerTextGroup.visible()
            tvBannerTitle.text = bannerData.title
            tvBannerDescriptionOne.text = bannerData.text1
            tvBannerDescriptionTwo.text = bannerData.text2
            img.setImageUrl(bannerData.bgURL)
        } else {
            bannerTextGroup.gone()
            img.setImageUrl(bannerData.imgURL)
        }
        view.tag = bannerData
        view.setOnClickListener {
            if (it.tag is BannerData)
                onCarouselItemClick(it.tag as BannerData)
        }
        //this is to send first item is visible as carousel lib not sending it...
        if(!bannerList[0].isInvoke){
            onActiveIndexChanged(-1, 0)
        }
    }

    private fun handleThrowable(throwable: Throwable) {
        val errorMessage = when (throwable) {
            is SubmitWithdrawalException -> throwable.errorMessage
            else -> ErrorHandler.getErrorMessage(context, throwable)
        }
        showToaster(errorMessage, toasterType = Toaster.TYPE_ERROR)
    }

    private fun redirectToSuccessFragment(submitWithdrawalResponse: SubmitWithdrawalResponse) {
        loadingLayout.gone()
        (activity as WithdrawActivity).openSuccessFragment(withdrawalRequest, submitWithdrawalResponse)
    }

    private fun showUIComponent() {
        loadingLayout.gone()
        showSellerBlockedTicker()
        initializeViewPager()
    }

    private fun handleGlobalError(throwable: Throwable, retry: () -> Unit) {
        loadingLayout.gone()
        if (throwable is MessageErrorException) {
            swdGlobalError.setType(GlobalError.SERVER_ERROR)
        } else {
            swdGlobalError.setType(GlobalError.NO_CONNECTION)
        }
        swdGlobalError.visible()
        swdGlobalError.setActionClickListener {
            swdGlobalError.gone()
            loadingLayout.visible()
            retry.invoke()
        }
    }

    private fun showMustVerify() {
        context?.let { context ->
            DialogUnify(context = context,
                    actionType = DialogUnify.HORIZONTAL_ACTION,
                    imageType = DialogUnify.NO_IMAGE).apply {
                setTitle(getString(R.string.swd_alert_not_verified_yet_title))
                setDescription(getString(R.string.swd_alert_not_verified_yet_body))
                setPrimaryCTAText(getString(R.string.swd_alert_not_verified_yet_positive))
                setSecondaryCTAText(getString(R.string.swd_alert_not_verified_yet_negative))
                setPrimaryCTAClickListener {
                    val intent = RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.SETTING_PROFILE)
                    startActivity(intent)
                    activity?.finish()
                }
                setSecondaryCTAClickListener {
                    activity?.finish()
                }
                setCancelable(false)
                show()
            }
        }
    }

    private fun initializeViewPager() {
        arguments?.let {
            saldoWithdrawalPagerAdapter = SaldoWithdrawalPagerAdapter(requireContext(), childFragmentManager, it)
            viewPagerSaldoWithdrawal.adapter = saldoWithdrawalPagerAdapter
            tabSaldoWithdrawal.tabLayout.setupWithViewPager(viewPagerSaldoWithdrawal)
            if (buyerSaldoBalance == 0L) {
                tabSaldoWithdrawal.tabLayout.getTabAt(1)?.select()
            }
        }
    }

    private fun showSellerBlockedTicker() {
        if ((sellerWithdrawalLocked == MCL_STATUS_BLOCK1
                        || sellerWithdrawalLocked == MCL_STATUS_BLOCK3) && showMclBlockTickerFirebaseFlag) {
            saldoLockTicker.visible()
            saldoLockTicker.tickerTitle = getString(R.string.swd_lock_tickerTitle)
            val descriptionStr = getString(R.string.swd_lock_tickerDescription, mclLateCount)
            val payNowLinkStr = getString(R.string.swd_lock_pay_now)
            val combinedHtmlDescription = getString(R.string.swd_ticker_description_html,
                    descriptionStr, payNowLinkStr)
            saldoLockTicker.setHtmlDescription(combinedHtmlDescription)
            saldoLockTicker.setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    WithdrawConstant.openSessionBaseURL(context,
                            WithdrawConstant.SALDO_LOCK_PAY_NOW_URL)
                }

                override fun onDismiss() {
                    saldoLockTicker.gone()
                }

            })
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
                    saldoWithdrawalViewModel.getBankList()
                    showToaster(getString(R.string.swd_bank_added_success),
                            toasterType = Toaster.TYPE_NORMAL)
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

    private fun showToaster(message: String, toasterType: Int) {
        view?.let {
            Toaster.make(it, message, Toaster.LENGTH_SHORT, toasterType)
        }
    }

    private fun getProgramName(): String {
        if (checkEligible.data.program != null)
            return checkEligible.data.program ?: ""
        return ""
    }

    fun initiateBuyerWithdrawal(selectedBankAccount: BankAccount, withdrawalAmount: Long) {
        loadingLayout.visible()
        withdrawalRequest = WithdrawalRequest(
                userId = userSession.get().userId, email = userSession.get().email,
                withdrawal = withdrawalAmount, bankAccount = selectedBankAccount,
                isSellerWithdrawal = false, programName = getProgramName(), isJoinRekeningPremium = false)
        saldoWithdrawalViewModel.getValidatePopUpData(selectedBankAccount)
    }

    fun initiateSellerWithdrawal(selectedBankAccount: BankAccount, withdrawalAmount: Long) {
        loadingLayout.visible()
        withdrawalRequest = WithdrawalRequest(
                userId = userSession.get().userId, email = userSession.get().email,
                withdrawal = withdrawalAmount, bankAccount = selectedBankAccount,
                isSellerWithdrawal = true, programName = getProgramName(), isJoinRekeningPremium = false)
        saldoWithdrawalViewModel.getValidatePopUpData(selectedBankAccount)
    }

    private fun checkAndCreateValidatePopup(validatePopUpWithdrawal: ValidatePopUpWithdrawal) {
        when {
            validatePopUpWithdrawal.joinData.isJoinPrompt -> {
                activity?.let {
                    JoinRPOnWithdrawalBottomSheet
                            .getJoinRPOnWithdrawalBottomSheetInstance(withdrawalRequest.bankAccount,
                                    validatePopUpWithdrawal.joinData)
                            .show(it.supportFragmentManager, "")
                }
            }
            validatePopUpWithdrawal.data.needShow -> {
                context?.let {
                    showValidationPopUp(it, validatePopUpWithdrawal.data)
                }
            }
            else -> {
                openUserVerificationScreen()
            }
        }
    }

    override fun onWithdrawalAndJoinRekening(isJoinRP: Boolean) {
        withdrawalRequest.isJoinRekeningPremium = isJoinRP
        withdrawalRequest.showJoinRekeningWidget = !isJoinRP
        openUserVerificationScreen()
    }

    private fun showValidationPopUp(context: Context, data: ValidatePopUpData) {
        val note = HtmlCompat.fromHtml(data.note, HtmlCompat.FROM_HTML_MODE_LEGACY);
        validatePopUpAlertDialog = DialogUnify(context = context,
                actionType = DialogUnify.HORIZONTAL_ACTION,
                imageType = DialogUnify.NO_IMAGE).apply {
            setTitle(data.title)
            setDescription(note)
            setPrimaryCTAText(getString(R.string.swd_continue_pull))
            setSecondaryCTAText(getString(R.string.swd_back))
            setPrimaryCTAClickListener {
                analytics.get().eventClickContinueBtn()
                validatePopUpAlertDialog.cancel()
                openUserVerificationScreen()
            }
            setSecondaryCTAClickListener {
                validatePopUpAlertDialog.cancel()
            }
            show()
        }
    }

    private fun openUserVerificationScreen() {
        val OTP_TYPE_ADD_BANK_ACCOUNT = 120
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalUserPlatform.COTP)
        val bundle = Bundle()
        bundle.putString(ApplinkConstInternalGlobal.PARAM_EMAIL, userSession.get().email)
        bundle.putString(ApplinkConstInternalGlobal.PARAM_MSISDN, userSession.get().phoneNumber)
        bundle.putBoolean(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, true)
        bundle.putInt(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, OTP_TYPE_ADD_BANK_ACCOUNT)
        bundle.putBoolean(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, true)
        intent.putExtras(bundle)
        startActivityForResult(intent, VERIFICATION_REQUEST_CODE)
        analytics.get().eventClickWithdrawal()
    }

    private fun onVerificationCompleted(data: Intent) {
        if (data.hasExtra(ApplinkConstInternalGlobal.PARAM_UUID) ||
                ::withdrawalRequest.isInitialized) {
            val uuid: String = data.getStringExtra(ApplinkConstInternalGlobal.PARAM_UUID) ?: ""
            submitWithdrawalRequest(withdrawalRequest, uuid)
        }
    }

    private fun submitWithdrawalRequest(withdrawalRequest: WithdrawalRequest, uuid: String) {
        loadingLayout.visible()
        submitWithdrawalViewModel.submitWithdraw(withdrawalRequest, uuid)
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
                withdrawalRequest = savedInstanceState.getParcelable(WITHDRAWAL_REQUEST_DATA) ?: WithdrawalRequest()
            }
        }
    }

    companion object {
        const val BANNER_WITH_CONTENT = 2
        const val WITHDRAWAL_REQUEST_DATA = "withdrawal_request_data"
        const val BANK_SETTING_REQUEST_CODE = 3001
        const val VERIFICATION_REQUEST_CODE = 3002

        private const val HALF_SLIDE_NO = 1.1f
        private const val FULL_SLIDE_NO = 1.0f

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