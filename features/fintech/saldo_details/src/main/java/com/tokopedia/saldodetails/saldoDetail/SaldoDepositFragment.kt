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
import com.tokopedia.utils.lifecycle.autoClearedNullable
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

    private var binding by autoClearedNullable<FragmentSaldoDepositBinding>()

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
            binding?.spAppBarLayout?.setExpanded(true)
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
    ): View? {
        binding = FragmentSaldoDepositBinding.inflate(layoutInflater, container, false)
        initViews()
        return binding?.root
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
        binding?.depositHeaderLayout?.apply {
            saldoCoachMarkController.addBalanceAnchorsForCoachMark(
                isBalanceShown,
                listOf(saldoBuyerDepositText, saldoSellerDepositText)
            )
            prepareSaldoCoachMark()
        }
    }

    fun startSaldoCoachMarkFlow(anchorView: View?) {
        saldoCoachMarkController.setSalesTabWidgetReady(anchorView)
        prepareSaldoCoachMark()
    }

    private fun prepareSaldoCoachMark() {
        if (activity is SaldoDepositActivity) {
            saldoCoachMarkController.startCoachMark()
            binding?.spAppBarLayout?.addOnOffsetChangedListener(AppBarLayout
                .OnOffsetChangedListener { _, _ ->
                    saldoCoachMarkController.updateCoachMarkOnScroll(
                        expandLayout
                    )
                })
        }
    }


    private fun initViews() {
        if (arguments != null) {
            isSellerEnabled = requireArguments().getBoolean(IS_SELLER_ENABLED)
        }
        setViewModelObservers()
        expandLayout = true
        binding?.depositHeaderLayout?.apply {
            if (expandLayout) {
                saldoTypeLl.show()
            } else {
                saldoDepositLayoutExpand.animate().rotation(animationRotationValue).duration = animationDuration
                saldoTypeLl.gone()
            }

            if (expandMerchantDetailLayout) {
                merchantDetailsLl.show()
            } else {
                merchantDetailLayoutExpand.animate().rotation(animationRotationValue).duration =
                    animationDuration
                merchantDetailsLl.gone()
            }
        }
        binding?.saldoHistoryLayout?.let {
            val saldoHistoryFragment = SaldoTransactionHistoryFragment()
            childFragmentManager.beginTransaction()
                .replace(
                    it.id,
                    saldoHistoryFragment,
                    "saldo History"
                )
                .commit()
            this.saldoHistoryFragment = saldoHistoryFragment
        }
        binding?.depositHeaderLayout?.merchantDetailsLl?.apply {
            if (isSellerMigrationEnabled(context)) {
                this.hide()
            } else {
                this.show()
            }
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
        binding?.depositHeaderLayout?.apply {
            cardWithdrawBalance.visible()
            localLoadSaldoBalance.gone()
        }
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
        binding?.depositHeaderLayout?.apply {
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
        binding?.depositHeaderLayout?.apply {
            saldoDepositLayoutExpand.setOnClickListener {
                if (expandLayout) {
                    saldoDepositLayoutExpand.animate().rotation(animationRotationValue).duration =
                        animationDuration
                    expandLayout = false
                    collapse(saldoTypeLl)
                    saldoCoachMarkController.handleCoachMarkVisibility(false)
                } else {
                    saldoDepositLayoutExpand.animate().rotation(animationRotationZeroValue).duration =
                        animationDuration
                    expandLayout = true
                    expand(saldoTypeLl)
                    saldoCoachMarkController.handleCoachMarkVisibility(true)
                }
            }

            merchantDetailLayoutExpand.setOnClickListener {
                if (expandMerchantDetailLayout) {
                    merchantDetailLayoutExpand.animate().rotation(animationRotationValue).duration =
                        animationDuration
                    expandMerchantDetailLayout = false
                    collapse(merchantDetailsLl)
                } else {
                    merchantDetailLayoutExpand.animate().rotation(animationRotationZeroValue).duration =
                        animationDuration
                    expandMerchantDetailLayout = true
                    expand(merchantDetailsLl)
                }
            }

            withdrawButton.setOnClickListener {
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
        }
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
        binding?.depositHeaderLayout?.apply {
            saldoDetailViewModel.isSeller = isSellerEnabled
            context?.resources?.let { res ->
                saldoDepositText.text =
                    res.getString(R.string.total_saldo_text)
            }
            saldoBuyerBalanceRl.show()
            saldoBuyerBalanceRl.show()

            saldoBuyerDepositTextInfo.setOnClickListener {
                showBottomSheetInfoDialog(
                    false
                )
            }

            saldoSellerDepositTextInfo
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
    }

    private fun hideUserFinancialStatusLayout() {
        binding?.depositHeaderLayout?.merchantStatusLl?.gone()
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
        binding?.depositHeaderLayout?.totalBalance?.apply {
            if (!TextUtils.isEmpty(summaryUsebleDepositIdr)) {
                this.text = summaryUsebleDepositIdr
                this.show()
            } else {
                this.gone()
            }
        }
    }

    private fun setWithdrawButtonState(state: Boolean) {
        binding?.depositHeaderLayout?.withdrawButton?.apply {
            buttonType = if (state) UnifyButton.Type.MAIN else UnifyButton.Type.ALTERNATE
            isEnabled = state
            isClickable = state
        }
    }

    @SuppressLint("Range")
    fun showErrorMessage(error: String) {
        view?.apply {
            Toaster.build(this, error, Toaster.TYPE_NORMAL, Toaster.TYPE_ERROR).show()
        }
    }

    private fun showHoldWarning(warningText: String) {
        binding?.depositHeaderLayout?.holdBalanceLayout?.apply {
            this.show()
            this.setHtmlDescription(
                String.format(
                    getString(
                        R.string.saldo_hold_balance_text,
                        warningText
                    )
                )
            )

            this.setDescriptionClickEvent(
                object : TickerCallback {
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {
                        saldoDetailsAnalytics.sendClickPaymentEvents(SaldoDetailsConstants.Action.SALDO_HOLD_STATUS_CLICK)
                        val intent = Intent(context, SaldoHoldInfoActivity::class.java)
                        startActivity(intent)
                    }

                    override fun onDismiss() {}
                })
        }
    }

    private fun hideSaldoPrioritasFragment() {
        binding?.depositHeaderLayout?.apply {
            saldoPrioritasWidget.show()
            Handler().postDelayed({
                if (merchantCreditLineWidget.visibility != View.VISIBLE) {
                    hideUserFinancialStatusLayout()
                }
            }, CHECK_VISIBILITY_DELAY)
        }
    }

    private fun hideMerchantCreditLineFragment() {
        binding?.depositHeaderLayout?.apply {
            merchantCreditLineWidget.gone()
            Handler().postDelayed({
                if (saldoPrioritasWidget.visibility != View.VISIBLE) {
                    hideUserFinancialStatusLayout()
                }
            }, CHECK_VISIBILITY_DELAY)
        }
    }

    private fun showTickerMessage(withdrawalTicker: String) {
        binding?.tickerSaldoWithdrawalInfo?.apply {
            this.show()
            this.setHtmlDescription(withdrawalTicker)
        }
    }

    private fun showTicker() {
        if (showMclBlockTickerFirebaseFlag) {
            binding?.layoutHoldwithdrawlDialog?.apply {
                this.visible()
                this.tickerTitle =
                    getString(R.string.saldo_lock_tickerTitle)
                val descriptionStr = getString(R.string.saldo_lock_tickerDescription, mclLateCount)
                val payNowLinkStr = getString(R.string.saldo_lock_pay_now)
                val combinedHtmlDescription = getString(
                    R.string.saldo_ticker_description_html,
                    descriptionStr, payNowLinkStr
                )
                this.setHtmlDescription(
                    combinedHtmlDescription
                )
                this.setDescriptionClickEvent(object :
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
                        binding?.layoutHoldwithdrawlDialog?.gone()
                    }

                })
            }
        }
    }

    private fun hideTickerMessage() {
        binding?.tickerSaldoWithdrawalInfo?.hide()
    }

    private fun setLateCount(count: Int) {
        mclLateCount = count
    }

    private fun hideWithdrawTicker() {
        binding?.layoutHoldwithdrawlDialog?.gone()
    }

    private fun showSellerSaldoRL() {
        binding?.depositHeaderLayout?.saldoSellerBalanceRl?.show()
    }

    private fun setBuyerSaldoBalance(balance: Long, text: String) {
        saldoBalanceBuyer = balance
        binding?.depositHeaderLayout?.buyerBalance?.text = text
    }

    private fun setSellerSaldoBalance(amount: Long, formattedAmount: String) {
        saldoBalanceSeller = amount
        binding?.depositHeaderLayout?.sellerBalance?.text = formattedAmount
    }

    private fun showBuyerSaldoRL() {
        binding?.depositHeaderLayout?.saldoBuyerBalanceRl?.show()
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
        binding?.depositHeaderLayout?.merchantStatusLl?.show()
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
            binding?.depositHeaderLayout?.merchantStatusLl?.show()
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
        binding?.depositHeaderLayout?.holdBalanceLayout?.hide()
    }

    override fun onDestroy() {
        super.onDestroy()
        performanceInterface.stopMonitoring()
    }
}
