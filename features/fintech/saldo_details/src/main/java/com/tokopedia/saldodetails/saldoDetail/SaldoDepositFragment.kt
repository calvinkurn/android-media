package com.tokopedia.saldodetails.saldoDetail

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.appbar.AppBarLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.remoteconfig.RemoteConfigKey.APP_ENABLE_SALDO_LOCK
import com.tokopedia.saldodetails.R
import com.tokopedia.saldodetails.commom.analytics.SaldoDetailsAnalytics
import com.tokopedia.saldodetails.commom.analytics.SaldoDetailsConstants
import com.tokopedia.saldodetails.commom.design.SaldoInstructionsBottomSheet
import com.tokopedia.saldodetails.commom.di.component.SaldoDetailsComponent
import com.tokopedia.saldodetails.commom.utils.ErrorMessage
import com.tokopedia.saldodetails.commom.utils.SaldoCoachMarkController
import com.tokopedia.saldodetails.commom.utils.SaldoDetailsRollenceUtil
import com.tokopedia.saldodetails.commom.utils.Success
import com.tokopedia.saldodetails.databinding.FragmentSaldoDepositBinding
import com.tokopedia.saldodetails.merchantDetail.credit.MerchantCreditDetailFragment
import com.tokopedia.saldodetails.merchantDetail.priority.MerchantSaldoPriorityFragment
import com.tokopedia.saldodetails.saldoDetail.domain.data.GqlDetailsResponse
import com.tokopedia.saldodetails.saldoDetail.domain.data.GqlMerchantCreditResponse
import com.tokopedia.saldodetails.saldoDetail.domain.data.Saldo
import com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.ui.SaldoTransactionHistoryFragment
import com.tokopedia.saldodetails.saldoHoldInfo.SaldoHoldInfoActivity
import com.tokopedia.seller.active.common.worker.UpdateShopActiveWorker
import com.tokopedia.seller_migration_common.isSellerMigrationEnabled
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.android.synthetic.main.fragment_saldo_deposit.*
import kotlinx.android.synthetic.main.fragment_saldo_deposit.view.*
import kotlinx.android.synthetic.main.saldo_deposit_header.*
import kotlinx.android.synthetic.main.saldo_deposit_header.view.*
import javax.inject.Inject

class SaldoDepositFragment : BaseDaggerFragment() {

    companion object {
        const val REQUEST_WITHDRAW_CODE = 3333
        const val SALDODETAIL_FINTECH_PLT = "saldodetailfintech_plt"
        const val SALDODETAIL_FINTECH_PLT_PREPARE_METRICS = "saldodetailfintech_plt_prepare_metrics"
        const val SALDODETAIL_FINTECH_PLT_NETWORK_METRICS = "saldodetailfintech_plt_network_metrics"
        const val SALDODETAIL_FINTECH_PLT_RENDER_METRICS = "saldodetailfintech_plt_render_metrics"

        const val IS_SELLER_ENABLED = "is_user_enabled"
        const val BUNDLE_PARAM_SELLER_DETAILS = "seller_details"
        const val BUNDLE_PARAM_MERCHANT_CREDIT_DETAILS = "merchant_credit_details"

        const val BUNDLE_SALDO_SELLER_TOTAL_BALANCE_INT = "seller_total_balance_int"
        const val BUNDLE_SALDO_BUYER_TOTAL_BALANCE_INT = "buyer_total_balance_int"
        private const val MCL_STATUS_ZERO = 0
        private const val MCL_STATUS_BLOCK1 = 700
        private const val MCL_STATUS_BLOCK3 = 999
        private const val IS_SELLER = "is_seller"
        const val BUNDLE_PARAM_MERCHANT_CREDIT_DETAILS_ID = "merchant_credit_details_id"
        const val BUNDLE_PARAM_SELLER_DETAILS_ID = "bundle_param_seller_details_id"

        private const val IS_WITHDRAW_LOCK = "is_lock"
        private const val MCL_LATE_COUNT = "late_count"
        private const val FIREBASE_FLAG_STATUS = "is_on"

        private const val animationRotationValue = 180f
        private const val animationRotationZeroValue = 0f
        private const val animationDuration: Long = 300

        private const val CHECK_VISIBILITY_DELAY: Long = 700

        fun createInstance(isSellerEnabled: Boolean): SaldoDepositFragment {
            val saldoDepositFragment = SaldoDepositFragment()
            val bundle = Bundle()
            bundle.putBoolean(IS_SELLER_ENABLED, isSellerEnabled)
            saldoDepositFragment.arguments = bundle
            return saldoDepositFragment
        }
    }

    @Inject
    lateinit var userSession: UserSession

    @Inject
    lateinit var saldoDetailsAnalytics: SaldoDetailsAnalytics

    private lateinit var binding: FragmentSaldoDepositBinding

    private var isSellerEnabled: Boolean = false
    private var saldoHistoryFragment: SaldoTransactionHistoryFragment? = null
    private var saldoBalanceSeller: Long = 0
    private var saldoBalanceBuyer: Long = 0
    private var expandLayout: Boolean = false
    private var expandMerchantDetailLayout = false
    private var mclLateCount = 0
    private var statusWithDrawLock = -1
    private var showMclBlockTickerFirebaseFlag = false
    private var remoteConfig: FirebaseRemoteConfigImpl? = null
    private var saveInstanceCacheManager: SaveInstanceCacheManager? = null

    private val performanceInterface by lazy {
        PageLoadTimePerformanceCallback(
            SALDODETAIL_FINTECH_PLT_PREPARE_METRICS,
            SALDODETAIL_FINTECH_PLT_NETWORK_METRICS,
            SALDODETAIL_FINTECH_PLT_RENDER_METRICS
        )
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var saldoDetailViewModel: SaldoDetailViewModel

    private val saldoCoachMarkController: SaldoCoachMarkController by lazy {
        SaldoCoachMarkController(requireContext()) {
            sp_app_bar_layout.setExpanded(true)
        }
    }


    private val isSaldoNativeEnabled: Boolean
        get() = remoteConfig?.getBoolean(
            RemoteConfigKey.SALDO_PRIORITAS_NATIVE_ANDROID,
            true
        ) ?: true

    private val isMerchantCreditLineEnabled: Boolean
        get() = remoteConfig?.getBoolean(
            RemoteConfigKey.APP_ENABLE_MERCHANT_CREDIT_LINE,
            true
        ) ?: true

    override fun onCreate(savedInstanceState: Bundle?) {
        performanceInterface.startMonitoring(SALDODETAIL_FINTECH_PLT)
        performanceInterface.startPreparePagePerformanceMonitoring()
        super.onCreate(savedInstanceState)
        saldoDetailsAnalytics.sendOpenScreenEvent()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSaldoDepositBinding.inflate(layoutInflater, container, false)
        initViews(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRemoteConfig()
        initListeners()
        initialVar()
        context?.let { UpdateShopActiveWorker.execute(it) }
    }

    private fun initRemoteConfig() {
        remoteConfig = FirebaseRemoteConfigImpl(context)
    }

    private fun addBalanceAnchorsForCoachMark(isBalanceShown: Boolean) {
        saldoCoachMarkController.addBalanceAnchorsForCoachMark(
            isBalanceShown,
            listOf(saldo_buyer_deposit_text, saldo_seller_deposit_text)
        )
        prepareSaldoCoachMark()
    }

    fun startSaldoCoachMarkFlow(anchorView: View?) {
        saldoCoachMarkController.setSalesTabWidgetReady(anchorView)
        prepareSaldoCoachMark()
    }

    private fun prepareSaldoCoachMark() {
        if (activity is SaldoDepositActivity) {
            saldoCoachMarkController.startCoachMark()
            sp_app_bar_layout.addOnOffsetChangedListener(AppBarLayout
                .OnOffsetChangedListener { _, _ ->
                    saldoCoachMarkController.updateCoachMarkOnScroll(
                        expandLayout
                    )
                })
        }
    }

    @SuppressLint("Range")
    private fun initViews(view: View) {

        if (arguments != null) {
            isSellerEnabled = requireArguments().getBoolean(IS_SELLER_ENABLED)
        }
        setViewModelObservers()

        expandLayout = true

        if (expandLayout) {
            binding.depositHeaderLayout.withdrawLayout.cardWithdrawBalance.llWithdrawBalance.saldo_type_ll.show()
        } else {
            binding.depositHeaderLayout.saldoBalanceLayout.withdraw_layout.cardWithdrawBalance.llWithdrawBalance.main_balance_rl.saldo_deposit_layout_expand.animate().rotation(animationRotationValue).duration =
                animationDuration
            binding.depositHeaderLayout.withdrawLayout.cardWithdrawBalance.llWithdrawBalance.saldo_type_ll.gone()
        }

        if (expandMerchantDetailLayout) {
            binding.depositHeaderLayout.merchantDetailsLl.show()
        } else {
            binding.depositHeaderLayout.merchantStatusLl.merchant_detail_ll.merchant_detail_layout_expand.animate().rotation(animationRotationValue).duration =
                animationDuration
            binding.depositHeaderLayout.merchantDetailsLl.gone()
        }

        val saldoHistoryFragment = SaldoTransactionHistoryFragment()
        childFragmentManager.beginTransaction()
            .replace(
                binding.saldoHistoryLayout.id,
                saldoHistoryFragment,
                "saldo History"
            )
            .commit()
        this.saldoHistoryFragment = saldoHistoryFragment

        if (isSellerMigrationEnabled(context)) {
            binding.depositHeaderLayout.merchantDetailsLl.hide()
        } else {
            binding.depositHeaderLayout.merchantDetailsLl.show()
        }
    }

    private fun setViewModelObservers() {

        saldoDetailViewModel.gqlUserSaldoBalanceLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    if (it.data.saldo.isHaveError) {
                        onSaldoBalanceLoadingError()
                    } else {
                        onUserSaldoBalanceLoaded(it.data.saldo)
                    }
                }
                is ErrorMessage<*, *> -> onSaldoBalanceLoadingError()
            }
        }

        saldoDetailViewModel.gqlMerchantSaldoDetailLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    performanceInterface.stopNetworkRequestPerformanceMonitoring()
                    performanceInterface.startRenderPerformanceMonitoring()
                    showSaldoPrioritasFragment(it.data.data)
                    performanceInterface.stopRenderPerformanceMonitoring()
                }
                else -> {
                    performanceInterface.stopNetworkRequestPerformanceMonitoring()
                    hideSaldoPrioritasFragment()
                }
            }
        }

        saldoDetailViewModel.gqlMerchantCreditDetailLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    performanceInterface.stopNetworkRequestPerformanceMonitoring()
                    performanceInterface.startRenderPerformanceMonitoring()
                    showMerchantCreditLineFragment(it.data.data)
                    performanceInterface.stopRenderPerformanceMonitoring()
                }
                else -> {
                    performanceInterface.stopNetworkRequestPerformanceMonitoring()
                    hideMerchantCreditLineFragment()
                }
            }
        }

        saldoDetailViewModel.gqlLateCountResponseLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    it.data.mclGetLatedetails?.let{
                        details ->
                        setLateCount(details.lateCount)
                    }
                }
                else -> {
                    hideWithdrawTicker()
                }
            }
        }

        saldoDetailViewModel.gqlTickerWithdrawalLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    it.data.withdrawalTicker?.let {
                        t ->
                        if (!TextUtils.isEmpty(t.tickerMessage)) {
                            showTickerMessage(t.tickerMessage ?: "")
                        } else {
                            hideTickerMessage()
                        }
                    }?:run{
                        hideTickerMessage()
                    }
                }
                else -> {
                    hideTickerMessage()
                }
            }
        }
    }

    private fun onUserSaldoBalanceLoaded(saldo: Saldo) {
        addBalanceAnchorsForCoachMark(true)
        cardWithdrawBalance.visible()
        localLoadSaldoBalance.gone()
        setSellerSaldoBalance(
            saldo.sellerUsable,
            saldo.sellerUsableFmt ?: ""
        )
        showSellerSaldoRL()

        setBuyerSaldoBalance(
            saldo.buyerUsable,
            saldo.buyerUsableFmt ?: ""
        )
        showBuyerSaldoRL()

        val totalBalance =
            saldo.buyerUsable + saldo.sellerUsable
        setBalance(
            CurrencyFormatUtil.convertPriceValueToIdrFormat(
                totalBalance,
                false
            )
        )
        setWithdrawButtonState(totalBalance != 0L)

        val holdBalance =
            (saldo.buyerHold + saldo.sellerHold).toDouble()
        if (holdBalance > 0) {
            showHoldWarning(
                CurrencyFormatUtil.convertPriceValueToIdrFormat(
                    holdBalance,
                    false
                )
            )
        } else {
            hideWarning()
        }

    }

    private fun onSaldoBalanceLoadingError() {
        saldoDetailsAnalytics.sendApiFailureEvents(SaldoDetailsConstants.EventLabel.SALDO_FETCH_BALANCE)
        addBalanceAnchorsForCoachMark(false)
        cardWithdrawBalance.gone()
        localLoadSaldoBalance.visible()
        localLoadSaldoBalance.refreshBtn?.setOnClickListener {
            cardWithdrawBalance.visible()
            localLoadSaldoBalance.gone()
            refresh()
        }
        localLoadSaldoBalance.localLoadTitle = getString(R.string.saldo_balance_load_fail_title)
        localLoadSaldoBalance.localLoadDescription =
            getString(R.string.saldo_balance_load_fail_desc)
    }


    private fun refresh() {
        if (::saldoDetailViewModel.isInitialized) {
            saldoDetailViewModel.getUserSaldoBalance()
            saldoHistoryFragment?.onRefresh()
        }
    }

    fun resetPageAfterWithdrawal() {
        if (::saldoDetailViewModel.isInitialized) {
            saldoDetailViewModel.getUserSaldoBalance()
            saldoHistoryFragment?.startInitialFetch()
        }
    }

    private fun initListeners() {

        binding.depositHeaderLayout.saldoBalanceLayout.withdraw_layout.cardWithdrawBalance.llWithdrawBalance.main_balance_rl.saldo_deposit_layout_expand.setOnClickListener {
            if (expandLayout) {
                binding.depositHeaderLayout.saldoBalanceLayout.withdraw_layout.cardWithdrawBalance.llWithdrawBalance.main_balance_rl.saldo_deposit_layout_expand.animate().rotation(animationRotationValue).duration =
                    animationDuration
                expandLayout = false
                collapse(binding.depositHeaderLayout.withdrawLayout.cardWithdrawBalance.llWithdrawBalance.saldo_type_ll)
                saldoCoachMarkController.handleCoachMarkVisibility(false)
            } else {
                binding.depositHeaderLayout.saldoBalanceLayout.withdraw_layout.cardWithdrawBalance.llWithdrawBalance.main_balance_rl.saldo_deposit_layout_expand.animate().rotation(animationRotationZeroValue).duration =
                    animationDuration
                expandLayout = true
                expand(binding.depositHeaderLayout.withdrawLayout.cardWithdrawBalance.llWithdrawBalance.saldo_type_ll)
                saldoCoachMarkController.handleCoachMarkVisibility(true)
            }
        }

        binding.depositHeaderLayout.merchantStatusLl.merchant_detail_ll.merchant_detail_layout_expand.setOnClickListener {
            if (expandMerchantDetailLayout) {
                binding.depositHeaderLayout.merchantStatusLl.merchant_detail_ll.merchant_detail_layout_expand.animate().rotation(animationRotationValue).duration =
                    animationDuration
                expandMerchantDetailLayout = false
                collapse(binding.depositHeaderLayout.merchantDetailsLl)
            } else {
                binding.depositHeaderLayout.merchantStatusLl.merchant_detail_ll.merchant_detail_layout_expand.animate().rotation(animationRotationZeroValue).duration =
                    animationDuration
                expandMerchantDetailLayout = true
                expand(binding.depositHeaderLayout.merchantDetailsLl)
            }
        }

        binding.spAppBarLayout.deposit_header_layout.saldo_balance_layout.withdraw_button?.setOnClickListener {
            saldoDetailsAnalytics.sendClickPaymentEvents(SaldoDetailsConstants.Action.SALDO_WITHDRAWAL_CLICK)
            try {
                if (!userSession.isMsisdnVerified) {
                    showMustVerify()
                } else if (!userSession.hasShownSaldoWithdrawalWarning()) {
                    userSession.setSaldoWithdrawalWaring(true)
                    showSaldoWarningDialog()
                } else {
                    goToWithdrawActivity()
                }
            } catch (e: Exception) {

            }
        }

        binding.depositHeaderLayout.saldoBalanceLayout.ticker_message_layout.close_ticker_message.setOnClickListener { binding.depositHeaderLayout.saldoBalanceLayout.ticker_message_layout.gone() }
    }

    private fun expand(v: View) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val targetHeight =
            v.measuredHeight    // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.layoutParams.height = 1
        v.show()
        val a = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                v.layoutParams.height = if (interpolatedTime == 1f)
                    ViewGroup.LayoutParams.WRAP_CONTENT
                else
                    (targetHeight * interpolatedTime).toInt()
                v.requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }
        a.duration = (targetHeight / v.context.resources.displayMetrics.density).toInt().toLong()
        v.startAnimation(a)
    }

    private fun collapse(v: View) {
        val initialHeight = v.measuredHeight
        val a = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                if (interpolatedTime == 1f) {
                    v.gone()
                } else {
                    v.layoutParams.height =
                        initialHeight - (initialHeight * interpolatedTime).toInt()
                    v.requestLayout()
                }
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }
        a.duration = (initialHeight / v.context.resources.displayMetrics.density).toInt().toLong()
        v.startAnimation(a)
    }


    private fun showMustVerify() {
        context?.let { context ->
            DialogUnify(context, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(getString(R.string.sp_alert_not_verified_yet_title))
                setDescription(getString(R.string.sp_alert_not_verified_yet_body))
                setPrimaryCTAText(getString(R.string.sp_alert_not_verified_yet_positive))
                setSecondaryCTAText(getString(R.string.sp_alert_not_verified_yet_negative))
                setPrimaryCTAClickListener {
                    val intent = RouteManager.getIntent(
                        getContext(),
                        ApplinkConstInternalUserPlatform.SETTING_PROFILE
                    )
                    startActivity(intent)
                    dismiss()
                }
                setSecondaryCTAClickListener { dismiss() }
                setOverlayClose(false)
                show()
            }
        }
    }

    private fun goToWithdrawActivity() {
        if (activity != null) {
            val intent = RouteManager.getIntent(
                activity,
                ApplinkConstInternalGlobal.WITHDRAW
            )
            val bundle = Bundle()
            bundle.putBoolean(IS_SELLER, isSellerEnabled)
            intent.putExtras(bundle)
            onDrawClicked(intent)
        }
    }

    private fun onDrawClicked(intent: Intent) {
        val sellerBalance = getSellerSaldoBalance()
        val buyerBalance = getBuyerSaldoBalance()

        val minSaldoLimit: Long = 10000
        if (sellerBalance < minSaldoLimit && buyerBalance < minSaldoLimit) {
            showErrorMessage(getString(R.string.saldo_min_withdrawal_error))
        } else {
            val withdrawActivityBundle = Bundle()
            withdrawActivityBundle.putBoolean(
                FIREBASE_FLAG_STATUS,
                showMclBlockTickerFirebaseFlag
            )
            withdrawActivityBundle.putInt(IS_WITHDRAW_LOCK, statusWithDrawLock)
            withdrawActivityBundle.putInt(MCL_LATE_COUNT, mclLateCount)
            withdrawActivityBundle.putBoolean(IS_SELLER, isSellerEnabled)
            withdrawActivityBundle.putLong(
                BUNDLE_SALDO_BUYER_TOTAL_BALANCE_INT,
                getBuyerSaldoBalance()
            )
            withdrawActivityBundle.putLong(
                BUNDLE_SALDO_SELLER_TOTAL_BALANCE_INT,
                getSellerSaldoBalance()
            )
            intent.putExtras(withdrawActivityBundle)
            startActivityForResult(intent, REQUEST_WITHDRAW_CODE)
        }
    }

    private fun showSaldoWarningDialog() {
        context?.let { context ->
            DialogUnify(context, DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(getString(R.string.sp_saldo_withdraw_warning_title))
                setDescription(getString(R.string.sp_saldo_withdraw_warning_desc))
                setPrimaryCTAText(getString(R.string.sp_saldo_withdraw_warning_positiv_button))
                setPrimaryCTAClickListener {
                    goToWithdrawActivity()
                    dismiss()
                }
                setOverlayClose(false)
                show()
            }
        }
    }

    private fun getSellerSaldoBalance(): Long {
        return saldoBalanceSeller
    }

    private fun getBuyerSaldoBalance(): Long {
        return saldoBalanceBuyer
    }

    private fun initialVar() {
        saldoDetailViewModel.isSeller = isSellerEnabled
        context?.resources?.let { res ->
            binding.depositHeaderLayout.withdrawLayout.cardWithdrawBalance.llWithdrawBalance.main_balance_rl.saldo_deposit_text.text =
                res.getString(R.string.total_saldo_text)
        }
        binding.depositHeaderLayout.withdrawLayout.cardWithdrawBalance.llWithdrawBalance.saldo_type_ll.saldo_buyer_balance_rl.show()
        binding.depositHeaderLayout.withdrawLayout.cardWithdrawBalance.llWithdrawBalance.saldo_type_ll.saldo_buyer_balance_rl.show()

        binding.depositHeaderLayout.withdrawLayout.cardWithdrawBalance.llWithdrawBalance.saldo_type_ll.saldo_buyer_balance_rl.saldo_buyer_deposit_text_info.setOnClickListener {
            showBottomSheetInfoDialog(
                false
            )
        }

        binding.depositHeaderLayout.withdrawLayout.cardWithdrawBalance.llWithdrawBalance.saldo_type_ll.saldo_seller_balance_rl.saldo_seller_deposit_text_info
            .setOnClickListener { showBottomSheetInfoDialog(true) }

        performanceInterface.stopPreparePagePerformanceMonitoring()
        performanceInterface.startNetworkRequestPerformanceMonitoring()

        if (activity != null) {
            if (isSaldoNativeEnabled) {
                saldoDetailViewModel.getMerchantSaldoDetails()
            } else {
                hideSaldoPrioritasFragment()
            }

            if (isMerchantCreditLineEnabled) {
                saldoDetailViewModel.getMerchantCreditLineDetails()
            } else {
                hideMerchantCreditLineFragment()
            }
        }
    }

    private fun hideUserFinancialStatusLayout() {
        binding.depositHeaderLayout.merchantStatusLl.gone()
    }

    private fun showBottomSheetInfoDialog(isSellerClicked: Boolean) {
        context?.let {
            val bundle = Bundle()
            if (isSellerClicked) {
                bundle.putString(
                    SaldoInstructionsBottomSheet.TITLE_TEXT,
                    it.getString(R.string.saldo_total_balance_seller)
                )
                bundle.putBoolean(SaldoInstructionsBottomSheet.SALDO_TYPE, true)
            } else {
                bundle.putString(
                    SaldoInstructionsBottomSheet.TITLE_TEXT,
                    it.getString(R.string.saldo_total_balance_buyer)
                )
                bundle.putBoolean(SaldoInstructionsBottomSheet.SALDO_TYPE, false)
            }
            SaldoInstructionsBottomSheet.show(bundle, childFragmentManager)
        }
    }

    override fun initInjector() {

        activity?.let {
            val saldoDetailsComponent = getComponent(SaldoDetailsComponent::class.java)
            saldoDetailsComponent.inject(this)


            if (context is AppCompatActivity) {
                val viewModelProvider =
                    ViewModelProviders.of(context as AppCompatActivity, viewModelFactory)
                saldoDetailViewModel = viewModelProvider[SaldoDetailViewModel::class.java]
            }
        }
    }

    override fun getScreenName() = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        onFirstTimeLaunched()
    }

    private fun onFirstTimeLaunched() {
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        showMclBlockTickerFirebaseFlag = remoteConfig.getBoolean(APP_ENABLE_SALDO_LOCK, false)
        saldoDetailViewModel.getUserSaldoBalance()
        saldoDetailViewModel.getTickerWithdrawalMessage()
        saldoDetailViewModel.getMerchantCreditLateCountValue()
    }

    private fun setBalance(summaryUsebleDepositIdr: String) {
        if (!TextUtils.isEmpty(summaryUsebleDepositIdr)) {
            binding.depositHeaderLayout.withdrawLayout.cardWithdrawBalance.llWithdrawBalance.main_balance_rl.total_balance.text =
                summaryUsebleDepositIdr
            binding.depositHeaderLayout.withdrawLayout.cardWithdrawBalance.llWithdrawBalance.main_balance_rl.total_balance.show()
        } else {
            binding.depositHeaderLayout.withdrawLayout.cardWithdrawBalance.llWithdrawBalance.main_balance_rl.total_balance.gone()
        }

    }

    private fun setWithdrawButtonState(state: Boolean) {
        binding.spAppBarLayout.deposit_header_layout.saldo_balance_layout.withdraw_button.buttonType =
            if (state) UnifyButton.Type.MAIN else UnifyButton.Type.ALTERNATE
        binding.spAppBarLayout.deposit_header_layout.saldo_balance_layout.withdraw_button.isEnabled =
            state
        binding.spAppBarLayout.deposit_header_layout.saldo_balance_layout.withdraw_button.isClickable =
            state
    }

    @SuppressLint("Range")
    fun showErrorMessage(error: String) {
        view?.apply {
            Toaster.build(this, error, Toaster.TYPE_NORMAL, Toaster.TYPE_ERROR).show()
        }
    }

    private fun showHoldWarning(warningText: String) {
        binding.depositHeaderLayout.withdrawLayout.hold_balance_ll.hold_balance_layout.show()
        binding.depositHeaderLayout.withdrawLayout.hold_balance_ll.hold_balance_layout.setHtmlDescription(
            String.format(
                getString(
                    R.string.saldo_hold_balance_text,
                    warningText
                )
            )
        )
        binding.depositHeaderLayout.withdrawLayout.hold_balance_ll.hold_balance_layout.setDescriptionClickEvent(
            object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    saldoDetailsAnalytics.sendClickPaymentEvents(SaldoDetailsConstants.Action.SALDO_HOLD_STATUS_CLICK)
                    val intent = Intent(context, SaldoHoldInfoActivity::class.java)
                    startActivity(intent)
                }

                override fun onDismiss() {}
            })
    }

    private fun hideSaldoPrioritasFragment() {
        binding.depositHeaderLayout.merchantDetailsLl.saldo_prioritas_widget.show()
        Handler().postDelayed({
            if (binding.depositHeaderLayout.merchantDetailsLl.merchant_credit_line_widget.visibility != View.VISIBLE) {
                hideUserFinancialStatusLayout()
            }
        }, CHECK_VISIBILITY_DELAY)
    }

    private fun hideMerchantCreditLineFragment() {
        binding.depositHeaderLayout.merchantDetailsLl.merchant_credit_line_widget.gone()
        Handler().postDelayed({
            if (binding.depositHeaderLayout.merchantDetailsLl.saldo_prioritas_widget.visibility != View.VISIBLE) {
                hideUserFinancialStatusLayout()
            }
        }, CHECK_VISIBILITY_DELAY)
    }

    private fun showTickerMessage(withdrawalTicker: String) {
        binding.depositHeaderLayout.saldoBalanceLayout.ticker_message_layout.show()
        binding.depositHeaderLayout.saldoBalanceLayout.ticker_message_layout.ticker_message_text.text =
            withdrawalTicker
    }

    private fun showTicker() {
        if (showMclBlockTickerFirebaseFlag) {
            binding.spAppBarLayout.layout_holdwithdrawl_dialog.visible()
            binding.spAppBarLayout.layout_holdwithdrawl_dialog.tickerTitle =
                getString(R.string.saldo_lock_tickerTitle)
            val descriptionStr = getString(R.string.saldo_lock_tickerDescription, mclLateCount)
            val payNowLinkStr = getString(R.string.saldo_lock_pay_now)
            val combinedHtmlDescription = getString(
                R.string.saldo_ticker_description_html,
                descriptionStr, payNowLinkStr
            )
            binding.spAppBarLayout.layout_holdwithdrawl_dialog.setHtmlDescription(
                combinedHtmlDescription
            )
            binding.spAppBarLayout.layout_holdwithdrawl_dialog.setDescriptionClickEvent(object :
                TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    RouteManager.route(
                        context, String.format(
                            "%s?url=%s",
                            ApplinkConst.WEBVIEW, SaldoDetailsConstants.SALDOLOCK_PAYNOW_URL
                        )
                    )
                }

                override fun onDismiss() {
                    binding.spAppBarLayout.layout_holdwithdrawl_dialog.gone()
                }

            })
        }
    }

    private fun hideTickerMessage() {
        binding.depositHeaderLayout.saldoBalanceLayout.ticker_message_layout.gone()
    }

    private fun setLateCount(count: Int) {
        mclLateCount = count
    }

    private fun hideWithdrawTicker() {
        binding.spAppBarLayout.layout_holdwithdrawl_dialog.gone()
    }

    private fun showSellerSaldoRL() {
        binding.depositHeaderLayout.withdrawLayout.cardWithdrawBalance.llWithdrawBalance.saldo_type_ll.saldo_seller_balance_rl.show()
    }

    private fun setBuyerSaldoBalance(balance: Long, text: String) {
        saldoBalanceBuyer = balance
        binding.depositHeaderLayout.withdrawLayout.cardWithdrawBalance.llWithdrawBalance.saldo_type_ll.saldo_buyer_balance_rl.buyer_balance
            .text = text
    }

    private fun setSellerSaldoBalance(amount: Long, formattedAmount: String) {
        saldoBalanceSeller = amount
        binding.depositHeaderLayout.withdrawLayout.cardWithdrawBalance.llWithdrawBalance.saldo_type_ll.saldo_seller_balance_rl.seller_balance
            .text = formattedAmount
    }

    private fun showBuyerSaldoRL() {
        binding.depositHeaderLayout.withdrawLayout.cardWithdrawBalance.llWithdrawBalance.saldo_type_ll.saldo_buyer_balance_rl.show()
    }

    private fun showMerchantCreditLineFragment(response: GqlMerchantCreditResponse?) {
        if (SaldoDetailsRollenceUtil.shouldShowModalTokoWidget() && response != null && response.isEligible) {
            statusWithDrawLock = response.status
            when (statusWithDrawLock) {
                MCL_STATUS_ZERO -> hideMerchantCreditLineFragment()
                MCL_STATUS_BLOCK1 -> {
                    showTicker()
                    showMerchantCreditLineWidget(response)
                }
                MCL_STATUS_BLOCK3 -> {
                    showTicker()
                    hideMerchantCreditLineFragment()
                }
                else -> showMerchantCreditLineWidget(response)
            }
        } else {
            hideMerchantCreditLineFragment()
        }
    }

    private fun showMerchantCreditLineWidget(response: GqlMerchantCreditResponse?) {
        binding.depositHeaderLayout.merchantStatusLl.show()
        val bundle = Bundle()
        saveInstanceCacheManager =
            context?.let { context -> SaveInstanceCacheManager(context, true) }
        saveInstanceCacheManager?.put(BUNDLE_PARAM_MERCHANT_CREDIT_DETAILS, response)
        if (!saveInstanceCacheManager?.id.isNullOrBlank()) {
            bundle.putString(BUNDLE_PARAM_MERCHANT_CREDIT_DETAILS_ID, saveInstanceCacheManager?.id)
        }
        childFragmentManager
            .beginTransaction()
            .replace(
                R.id.merchant_credit_line_widget,
                MerchantCreditDetailFragment.newInstance(bundle)
            )
            .commit()
    }

    private fun showSaldoPrioritasFragment(gqlDetailsResponse: GqlDetailsResponse?) {
        if (gqlDetailsResponse != null && gqlDetailsResponse.isEligible) {
            binding.depositHeaderLayout.merchantStatusLl.show()
            val bundle = Bundle()
            saveInstanceCacheManager =
                context?.let { context -> SaveInstanceCacheManager(context, true) }
            saveInstanceCacheManager?.put(BUNDLE_PARAM_SELLER_DETAILS, gqlDetailsResponse)
            if (!saveInstanceCacheManager?.id.isNullOrBlank()) {
                bundle.putString(BUNDLE_PARAM_SELLER_DETAILS_ID, saveInstanceCacheManager?.id)
            }
            childFragmentManager
                .beginTransaction()
                .replace(
                    R.id.saldo_prioritas_widget,
                    MerchantSaldoPriorityFragment.newInstance(bundle)
                )
                .commit()
        } else {
            hideSaldoPrioritasFragment()
        }
    }

    private fun hideWarning() {
        binding.depositHeaderLayout.withdrawLayout.hold_balance_ll.hold_balance_layout.hide()
    }

    override fun onDestroy() {
        super.onDestroy()
        performanceInterface.stopMonitoring()
    }
}