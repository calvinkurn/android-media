package com.tokopedia.saldodetails.view.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.coachmark.CoachMarkBuilder
import com.tokopedia.coachmark.CoachMarkContentPosition
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.coachmark.CoachMarkPreference
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.remoteconfig.RemoteConfigKey.APP_ENABLE_SALDO_LOCK
import com.tokopedia.saldodetails.commom.analytics.SaldoDetailsConstants
import com.tokopedia.saldodetails.design.UserStatusInfoBottomSheet
import com.tokopedia.saldodetails.di.SaldoDetailsComponent
import com.tokopedia.saldodetails.response.model.GqlDetailsResponse
import com.tokopedia.saldodetails.response.model.GqlMerchantCreditResponse
import com.tokopedia.saldodetails.utils.ErrorMessage
import com.tokopedia.saldodetails.utils.Success
import com.tokopedia.saldodetails.view.activity.SaldoDepositActivity
import com.tokopedia.saldodetails.view.activity.SaldoHoldInfoActivity
import com.tokopedia.saldodetails.viewmodels.SaldoDetailViewModel
import com.tokopedia.seller.active.common.service.UpdateShopActiveService
import com.tokopedia.seller_migration_common.isSellerMigrationEnabled
import com.tokopedia.user.session.UserSession
import javax.inject.Inject


class SaldoDepositFragment : BaseDaggerFragment() {

    companion object {
        val REQUEST_WITHDRAW_CODE = 3333
        val KEY_CAN_SHOW_BALANCE_COACHMARK = "com.tokopedia.saldodetails.balance_coach_mark"
        val SALDODETAIL_FINTECH_PLT = "saldodetailfintech_plt"
        val SALDODETAIL_FINTECH_PLT_PREPARE_METRICS = "saldodetailfintech_plt_prepare_metrics"
        val SALDODETAIL_FINTECH_PLT_NETWORK_METRICS = "saldodetailfintech_plt_network_metrics"
        val SALDODETAIL_FINTECH_PLT_RENDER_METRICS = "saldodetailfintech_plt_render_metrics"

        val IS_SELLER_ENABLED = "is_user_enabled"
        val BUNDLE_PARAM_SELLER_DETAILS = "seller_details"
        val BUNDLE_PARAM_MERCHANT_CREDIT_DETAILS = "merchant_credit_details"

        val BUNDLE_SALDO_SELLER_TOTAL_BALANCE_INT = "seller_total_balance_int"
        val BUNDLE_SALDO_BUYER_TOTAL_BALANCE_INT = "buyer_total_balance_int"
        private val MCL_STATUS_ZERO = 0
        private val MCL_STATUS_BLOCK1 = 700
        private val MCL_STATUS_BLOCK2 = 701
        private val MCL_STATUS_BLOCK3 = 999
        private val IS_SELLER = "is_seller"
        val BUNDLE_PARAM_MERCHANT_CREDIT_DETAILS_ID = "merchant_credit_details_id"
        val BUNDLE_PARAM_SELLER_DETAILS_ID = "bundle_param_seller_details_id"

        private val IS_WITHDRAW_LOCK = "is_lock"
        private val MCL_LATE_COUNT = "late_count"
        private val FIREBASE_FLAG_STATUS = "is_on"

        fun createInstance(isSellerEnabled: Boolean): SaldoDepositFragment {
            val saldoDepositFragment = SaldoDepositFragment()
            val bundle = Bundle()
            bundle.putBoolean(IS_SELLER_ENABLED, isSellerEnabled)
            saldoDepositFragment.arguments = bundle
            return saldoDepositFragment
        }
    }

    private val animationDuration: Long = 300
    private val COACH_MARK_DELAY: Long = 400

    @Inject
    lateinit var userSession: UserSession
    private var totalBalanceTV: TextView? = null
    private var drawButton: TextView? = null

    private var topSlideOffBar: RelativeLayout? = null
    private var holdBalanceLayout: RelativeLayout? = null
    private var amountBeingReviewed: TextView? = null
    private var saldoFrameLayout: View? = null
    private var tickerMessageRL: LinearLayout? = null
    private var tickeRMessageTV: TextView? = null
    private var tickerMessageCloseButton: ImageView? = null


    private var buyerSaldoBalanceRL: RelativeLayout? = null
    private var sellerSaldoBalanceRL: RelativeLayout? = null
    private var buyerBalanceTV: TextView? = null
    private var sellerBalanceTV: TextView? = null
    private var checkBalanceStatus: TextView? = null
    private var totalBalanceTitle: TextView? = null
    private var totalBalanceInfo: View? = null
    private var buyerBalanceInfoIcon: View? = null
    private var sellerBalanceInfoIcon: View? = null
    private var saldoBalanceSeparator: View? = null
    private var isSellerEnabled: Boolean = false

    private var saldoHistoryFragment: SaldoTransactionHistoryFragment? = null

    private var saldoBalanceSeller: Long = 0

    private var saldoBalanceBuyer: Long = 0

    private var saldoTypeLL: LinearLayout? = null
    private var merchantDetailLL: LinearLayout? = null

    private var saldoDepositExpandIV: ImageView? = null
    private var merchantDetailsExpandIV: ImageView? = null
    private var expandLayout: Boolean = false
    private var expandMerchantDetailLayout = true
    private var merchantCreditFrameLayout: View? = null
    private var merchantStatusLL: LinearLayout? = null
    private val CHECK_VISIBILITY_DELAY: Long = 700

    private var layoutTicker: View? = null
    private var tvTickerMessage: TextView? = null
    private var ivDismissTicker: ImageView? = null
    private var mclLateCount = 0
    private var statusWithDrawLock = -1
    private var showMclBlockTickerFirebaseFlag = false
    private var remoteConfig: FirebaseRemoteConfigImpl? = null
    private var saveInstanceCacheManager: SaveInstanceCacheManager? = null
    private val performanceInterface by lazy { PageLoadTimePerformanceCallback(SALDODETAIL_FINTECH_PLT_PREPARE_METRICS, SALDODETAIL_FINTECH_PLT_NETWORK_METRICS, SALDODETAIL_FINTECH_PLT_RENDER_METRICS) as PageLoadTimePerformanceInterface }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var saldoDetailViewModel: SaldoDetailViewModel

    private val isSaldoNativeEnabled: Boolean
        get() = remoteConfig!!.getBoolean(RemoteConfigKey.SALDO_PRIORITAS_NATIVE_ANDROID,
                true)

    private val isMerchantCreditLineEnabled: Boolean
        get() = remoteConfig!!.getBoolean(RemoteConfigKey.APP_ENABLE_MERCHANT_CREDIT_LINE,
                true)

    private val coachMark by lazy {
        CoachMarkBuilder().build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        performanceInterface.startMonitoring(SALDODETAIL_FINTECH_PLT)
        performanceInterface.startPreparePagePerformanceMonitoring()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(com.tokopedia.saldodetails.R.layout.fragment_saldo_deposit, container, false)
        initViews(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRemoteConfig()
        initListeners()
        initialVar()
        startCoachMark()
        context?.let { UpdateShopActiveService.startService(it) }
    }

    private fun initRemoteConfig() {
        remoteConfig = FirebaseRemoteConfigImpl(context)
    }

    private fun startCoachMark() {
        Handler().postDelayed({ this.setCoachMark() }, COACH_MARK_DELAY)
    }

    private fun setCoachMark() {
        if (isBalanceCoachMarkShown().not() && context != null) {
            val list = buildCoachMark() ?: return
            updateBalanceCoachMarkShown()
            coachMark.show(activity, KEY_CAN_SHOW_BALANCE_COACHMARK, list)
        }
    }

    private fun isBalanceCoachMarkShown(): Boolean {
        context?.let {
            return CoachMarkPreference.hasShown(it, KEY_CAN_SHOW_BALANCE_COACHMARK)
        } ?: run { return true }
    }

    private fun updateBalanceCoachMarkShown() {
        context?.let { CoachMarkPreference.setShown(it, KEY_CAN_SHOW_BALANCE_COACHMARK, true) }
    }

    private fun buildCoachMark(): ArrayList<CoachMarkItem>? {
        val list = ArrayList<CoachMarkItem>()
        return if (isSellerEnabled && activity is SaldoDepositActivity) {
            list.add(CoachMarkItem(
                    buyerSaldoBalanceRL,
                    getString(com.tokopedia.saldodetails.R.string.saldo_total_balance_buyer),
                    getString(com.tokopedia.saldodetails.R.string.saldo_balance_buyer_desc),
                    CoachMarkContentPosition.BOTTOM,
                    ContextCompat.getColor(requireContext(),
                            com.tokopedia.unifyprinciples.R.color.Unify_N700_68)
            ))

            list.add(CoachMarkItem(
                    sellerSaldoBalanceRL,
                    getString(com.tokopedia.saldodetails.R.string.saldo_total_balance_seller),
                    getString(com.tokopedia.saldodetails.R.string.saldo_intro_description_seller),
                    CoachMarkContentPosition.BOTTOM,
                    ContextCompat.getColor(requireContext(),
                            com.tokopedia.unifyprinciples.R.color.Unify_N700_68)
            ))
            list
        } else null
    }

    @SuppressLint("Range")
    private fun initViews(view: View) {

        if (arguments != null) {
            isSellerEnabled = requireArguments().getBoolean(IS_SELLER_ENABLED)
        }
        setViewModelObservers()

        expandLayout = isSellerEnabled

        totalBalanceTitle = view.findViewById(com.tokopedia.saldodetails.R.id.saldo_deposit_text)
        totalBalanceInfo = view.findViewById(com.tokopedia.saldodetails.R.id.saldo_deposit_text_info)

        buyerBalanceInfoIcon = view.findViewById(com.tokopedia.saldodetails.R.id.saldo_buyer_deposit_text_info)
        sellerBalanceInfoIcon = view.findViewById(com.tokopedia.saldodetails.R.id.saldo_seller_deposit_text_info)
        totalBalanceTV = view.findViewById(com.tokopedia.saldodetails.R.id.total_balance)
        drawButton = view.findViewById(com.tokopedia.saldodetails.R.id.withdraw_button)
        topSlideOffBar = view.findViewById(com.tokopedia.saldodetails.R.id.deposit_header)
        holdBalanceLayout = view.findViewById(com.tokopedia.saldodetails.R.id.hold_balance_layout)
        amountBeingReviewed = view.findViewById(com.tokopedia.saldodetails.R.id.amount_review)
        checkBalanceStatus = view.findViewById(com.tokopedia.saldodetails.R.id.check_balance)
        saldoFrameLayout = view.findViewById(com.tokopedia.saldodetails.R.id.saldo_prioritas_widget)
        merchantCreditFrameLayout = view.findViewById(com.tokopedia.saldodetails.R.id.merchant_credit_line_widget)
        tickerMessageRL = view.findViewById(com.tokopedia.saldodetails.R.id.ticker_message_layout)
        tickeRMessageTV = view.findViewById(com.tokopedia.saldodetails.R.id.ticker_message_text)
        tickerMessageCloseButton = view.findViewById(com.tokopedia.saldodetails.R.id.close_ticker_message)
        buyerBalanceTV = view.findViewById(com.tokopedia.saldodetails.R.id.buyer_balance)
        sellerBalanceTV = view.findViewById(com.tokopedia.saldodetails.R.id.seller_balance)
        buyerSaldoBalanceRL = view.findViewById(com.tokopedia.saldodetails.R.id.saldo_buyer_balance_rl)
        sellerSaldoBalanceRL = view.findViewById(com.tokopedia.saldodetails.R.id.saldo_seller_balance_rl)
        saldoBalanceSeparator = view.findViewById(com.tokopedia.saldodetails.R.id.saldo_balance_separator)
        saldoDepositExpandIV = view.findViewById(com.tokopedia.saldodetails.R.id.saldo_deposit_layout_expand)
        merchantDetailsExpandIV = view.findViewById(com.tokopedia.saldodetails.R.id.merchant_detail_layout_expand)
        saldoTypeLL = view.findViewById(com.tokopedia.saldodetails.R.id.saldo_type_ll)
        merchantDetailLL = view.findViewById(com.tokopedia.saldodetails.R.id.merchant_details_ll)
        merchantStatusLL = view.findViewById(com.tokopedia.saldodetails.R.id.merchant_status_ll)
        saldoDepositExpandIV!!.setImageDrawable(MethodChecker.getDrawable(activity, com.tokopedia.design.R.drawable.ic_arrow_up_grey))
        layoutTicker = view.findViewById(com.tokopedia.saldodetails.R.id.layout_holdwithdrawl_dialog)
        tvTickerMessage = view.findViewById(com.tokopedia.design.R.id.tv_desc_info)
        ivDismissTicker = view.findViewById(com.tokopedia.design.R.id.iv_dismiss_ticker)

        if (expandLayout) {
            saldoTypeLL!!.show()
        } else {
            saldoDepositExpandIV!!.animate().rotation(180f).duration = animationDuration
            saldoTypeLL!!.gone()
        }

        if (expandMerchantDetailLayout) {
            merchantDetailLL!!.show()
        } else {
            merchantDetailsExpandIV!!.animate().rotation(180f).duration = animationDuration
            merchantDetailLL!!.gone()
        }

        val saldoHistoryFragment = SaldoTransactionHistoryFragment()
        childFragmentManager.beginTransaction()
                .replace(com.tokopedia.saldodetails.R.id.saldo_history_layout, saldoHistoryFragment, "saldo History")
                .commit()
        this.saldoHistoryFragment = saldoHistoryFragment

        if (isSellerMigrationEnabled(context)) {
            merchantDetailLL?.hide()
        } else {
            merchantDetailLL?.show()
        }
    }

    private fun setViewModelObservers() {

        saldoDetailViewModel.gqlUserSaldoBalanceLiveData.observe(context as AppCompatActivity,
                androidx.lifecycle.Observer {

                    when (it) {
                        is Success -> {
                            setSellerSaldoBalance(it.data.saldo!!.sellerUsable, it.data.saldo!!.sellerUsableFmt!!)
                            showSellerSaldoRL()

                            setBuyerSaldoBalance(it.data.saldo!!.buyerUsable, it.data.saldo!!.buyerUsableFmt!!)
                            showBuyerSaldoRL()

                            val totalBalance = it.data.saldo!!.buyerUsable + it.data.saldo!!.sellerUsable
                            setBalance(totalBalance, CurrencyFormatUtil.convertPriceValueToIdrFormat(totalBalance, false))
                            setWithdrawButtonState(totalBalance != 0L)

                            val holdBalance = (it.data.saldo!!.buyerHold + it.data.saldo!!.sellerHold).toFloat()
                            if (holdBalance > 0) {
                                showHoldWarning(CurrencyFormatUtil.convertPriceValueToIdrFormat(holdBalance.toDouble(), false))
                            } else {
                                hideWarning()
                            }
                        }
                        is ErrorMessage<*, *> -> {
                            if (it.data is Int) {
                                setRetry(getString(it.data))
                            } else {
                                setRetry()
                            }
                        }
                        else -> {
                            setRetry(getString(com.tokopedia.saldodetails.R.string.sp_empty_state_error))
                        }
                    }
                })

        saldoDetailViewModel.gqlMerchantSaldoDetailLiveData.observe(context as AppCompatActivity,
                androidx.lifecycle.Observer {
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
                })

        saldoDetailViewModel.gqlMerchantCreditDetailLiveData.observe(context as AppCompatActivity,
                androidx.lifecycle.Observer {
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
                })

        saldoDetailViewModel.gqlLateCountResponseLiveData.observe(context as AppCompatActivity,
                androidx.lifecycle.Observer {
                    when (it) {
                        is Success -> {
                            setLateCount(it.data.mclGetLatedetails!!.lateCount)
                        }
                        else -> {
                            hideWithdrawTicker()
                        }
                    }
                })

        saldoDetailViewModel.gqlTickerWithdrawalLiveData.observe(context as AppCompatActivity,
                androidx.lifecycle.Observer {
                    when (it) {
                        is Success -> {
                            if (!TextUtils.isEmpty(it.data.withdrawalTicker!!.tickerMessage)) {
                                showTickerMessage(it.data.withdrawalTicker!!.tickerMessage!!)
                            } else {
                                hideTickerMessage()
                            }
                        }
                        else -> {
                            hideTickerMessage()
                        }
                    }
                })
    }

    private fun initListeners() {

        saldoDepositExpandIV!!.setOnClickListener {
            if (expandLayout) {
                saldoDepositExpandIV!!.animate().rotation(180f).duration = animationDuration
                expandLayout = false
                collapse(saldoTypeLL!!)
            } else {
                saldoDepositExpandIV!!.animate().rotation(0f).duration = animationDuration
                expandLayout = true
                expand(saldoTypeLL!!)
            }

        }

        merchantDetailsExpandIV!!.setOnClickListener {
            if (expandMerchantDetailLayout) {
                merchantDetailsExpandIV!!.animate().rotation(180f).duration = animationDuration
                expandMerchantDetailLayout = false
                collapse(merchantDetailLL!!)
            } else {
                merchantDetailsExpandIV!!.animate().rotation(0f).duration = animationDuration
                expandMerchantDetailLayout = true
                expand(merchantDetailLL!!)
            }
        }

        drawButton!!.setOnClickListener {
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

        checkBalanceStatus!!.setOnClickListener {
            val intent = Intent(context, SaldoHoldInfoActivity::class.java)
            startActivity(intent)
        }

        tickerMessageCloseButton!!.setOnClickListener { tickerMessageRL!!.gone() }
    }

    private fun expand(v: View) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val targetHeight = v.measuredHeight    // Older versions of android (pre API 21) cancel animations for views with a height of 0.
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
                    v.layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
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
        activity?.let {
            androidx.appcompat.app.AlertDialog.Builder(it)
                    .setTitle(getString(com.tokopedia.saldodetails.R.string.sp_alert_not_verified_yet_title))
                    .setMessage(getString(com.tokopedia.saldodetails.R.string.sp_alert_not_verified_yet_body))
                    .setPositiveButton(getString(com.tokopedia.saldodetails.R.string.sp_alert_not_verified_yet_positive)) { dialog, which ->
                        val intent = RouteManager.getIntent(getContext(), ApplinkConstInternalGlobal.SETTING_PROFILE)
                        startActivity(intent)
                        dialog.dismiss()
                    }
                    .setNegativeButton(getString(com.tokopedia.saldodetails.R.string.sp_alert_not_verified_yet_negative)) { dialog, which -> dialog.dismiss() }
                    .setCancelable(false)
                    .show()
        }
    }

    private fun goToWithdrawActivity() {
        if (activity != null) {
            val intent = RouteManager.getIntent(activity,
                    ApplinkConstInternalGlobal.WITHDRAW)
            val bundle = Bundle()
            bundle.putBoolean(IS_SELLER, isSellerEnabled)
            intent.putExtras(bundle)
            onDrawClicked(intent)
        }
    }

    private fun onDrawClicked(intent: Intent) {
        val userSession = UserSession(context)
        if (userSession.hasPassword()) {

            val sellerBalance = getSellerSaldoBalance()
            val buyerBalance = getBuyerSaldoBalance()

            val minSaldoLimit: Long = 10000
            if (sellerBalance < minSaldoLimit && buyerBalance < minSaldoLimit) {
                showErrorMessage(getString(com.tokopedia.saldodetails.R.string.saldo_min_withdrawal_error))
            } else {
                val withdrawActivityBundle = Bundle()
                withdrawActivityBundle.putBoolean(FIREBASE_FLAG_STATUS, showMclBlockTickerFirebaseFlag)
                withdrawActivityBundle.putInt(IS_WITHDRAW_LOCK, statusWithDrawLock)
                withdrawActivityBundle.putInt(MCL_LATE_COUNT, mclLateCount)
                withdrawActivityBundle.putBoolean(IS_SELLER, isSellerEnabled)
                withdrawActivityBundle.putLong(BUNDLE_SALDO_BUYER_TOTAL_BALANCE_INT, getBuyerSaldoBalance())
                withdrawActivityBundle.putLong(BUNDLE_SALDO_SELLER_TOTAL_BALANCE_INT, getSellerSaldoBalance())
                intent.putExtras(withdrawActivityBundle)
                startActivityForResult(intent, REQUEST_WITHDRAW_CODE)
            }
        } else {
            showWithdrawalNoPassword()
        }
    }

    private fun showSaldoWarningDialog() {
        activity?.let {
            androidx.appcompat.app.AlertDialog.Builder(it)
                    .setTitle(getString(com.tokopedia.saldodetails.R.string.sp_saldo_withdraw_warning_title))
                    .setMessage(getString(com.tokopedia.saldodetails.R.string.sp_saldo_withdraw_warning_desc))
                    .setPositiveButton(getString(com.tokopedia.saldodetails.R.string.sp_saldo_withdraw_warning_positiv_button)
                    ) { dialog, which -> goToWithdrawActivity() }
                    .setCancelable(true)
                    .show()
        }
    }

    private fun getSellerSaldoBalance(): Long {
        return saldoBalanceSeller
    }

    private fun getBuyerSaldoBalance(): Long {
        return saldoBalanceBuyer
    }

    fun isUserSeller(): Boolean {
        return isSellerEnabled
    }

    protected fun initialVar() {
        saldoDetailViewModel.isSeller = isSellerEnabled
        totalBalanceTitle!!.text = resources.getString(com.tokopedia.saldodetails.R.string.total_saldo_text)
        totalBalanceInfo!!.gone()
        buyerSaldoBalanceRL!!.show()
        sellerSaldoBalanceRL!!.show()

        totalBalanceInfo!!.setOnClickListener { showBottomSheetInfoDialog(false) }

        buyerBalanceInfoIcon!!.setOnClickListener { showBottomSheetInfoDialog(false) }

        sellerBalanceInfoIcon!!.setOnClickListener { showBottomSheetInfoDialog(true) }

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
        merchantStatusLL!!.gone()
    }

    private fun showBottomSheetInfoDialog(isSellerClicked: Boolean) {
        context?.let { context ->
            UserStatusInfoBottomSheet(context).apply {
                if (isSellerClicked) {
                    setBody(getString(com.tokopedia.saldodetails.R.string.saldo_balance_seller_desc))
                    setTitle(getString(com.tokopedia.saldodetails.R.string.saldo_total_balance_seller))
                } else {
                    setBody(getString(com.tokopedia.saldodetails.R.string.saldo_balance_buyer_desc))
                    setTitle(getString(com.tokopedia.saldodetails.R.string.saldo_total_balance_buyer))
                }

                setButtonText(getString(com.tokopedia.saldodetails.R.string.sp_saldo_withdraw_warning_positiv_button))
                show()
            }
        }
    }

    override fun initInjector() {

        activity?.let {
            val saldoDetailsComponent = getComponent(SaldoDetailsComponent::class.java)
            saldoDetailsComponent.inject(this)


            if (context is AppCompatActivity) {
                val viewModelProvider = ViewModelProviders.of(context as AppCompatActivity, viewModelFactory)
                saldoDetailViewModel = viewModelProvider[SaldoDetailViewModel::class.java]
            }
        }
    }

    override fun getScreenName(): String? {
        return null
    }

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

    private fun showWithdrawalNoPassword() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(resources.getString(com.tokopedia.saldodetails.R.string.sp_error_deposit_no_password_title))
        builder.setMessage(resources.getString(com.tokopedia.saldodetails.R.string.sp_error_deposit_no_password_content))
        builder.setPositiveButton(resources.getString(com.tokopedia.saldodetails.R.string.sp_error_no_password_yes)) { dialogInterface, i ->
            intentToAddPassword(requireContext())
            dialogInterface.dismiss()
        }
        builder.setNegativeButton(getString(com.tokopedia.saldodetails.R.string.sp_cancel)) { dialogInterface, i -> dialogInterface.dismiss() }
        val dialog = builder.create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(MethodChecker.getColor(context, com.tokopedia.design.R.color.black_54))
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).isAllCaps = false
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(MethodChecker.getColor(context, com.tokopedia.design.R.color.tkpd_main_green))
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).isAllCaps = false
    }

    private fun intentToAddPassword(context: Context) {
        context.startActivity(RouteManager.getIntent(context, ApplinkConstInternalGlobal.ADD_PASSWORD))
    }

    private fun setBalance(totalBalance: Long, summaryUsebleDepositIdr: String) {
        if (!TextUtils.isEmpty(summaryUsebleDepositIdr)) {
            totalBalanceTV!!.text = summaryUsebleDepositIdr
            totalBalanceTV!!.show()
        } else {
            totalBalanceTV!!.gone()
        }

    }

    private fun setWithdrawButtonState(state: Boolean) {
        if (state) {
            drawButton!!.setTextColor(Color.WHITE)
        } else {
            drawButton!!.setTextColor(resources.getColor(com.tokopedia.design.R.color.black_26))
        }
        drawButton!!.isEnabled = state
        drawButton!!.isClickable = state
    }

    @SuppressLint("Range")
    fun showErrorMessage(error: String) {
        NetworkErrorHelper.showRedCloseSnackbar(activity, error)
    }

    private fun showHoldWarning(warningText: String) {
        holdBalanceLayout!!.show()
        amountBeingReviewed!!.text = String.format(resources.getString(com.tokopedia.saldodetails.R.string.saldo_hold_balance_text), warningText)
        amountBeingReviewed!!.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun hideSaldoPrioritasFragment() {
        saldoFrameLayout!!.show()
        Handler().postDelayed({
            if (merchantCreditFrameLayout!!.visibility != View.VISIBLE) {
                hideUserFinancialStatusLayout()
            }
        }, CHECK_VISIBILITY_DELAY)
    }

    private fun hideMerchantCreditLineFragment() {
        merchantCreditFrameLayout!!.gone()
        Handler().postDelayed({
            if (saldoFrameLayout!!.visibility != View.VISIBLE) {
                hideUserFinancialStatusLayout()
            }
        }, CHECK_VISIBILITY_DELAY)
    }

    private fun showTickerMessage(withdrawalTicker: String) {
        tickerMessageRL!!.show()
        tickeRMessageTV!!.text = withdrawalTicker
    }

    private fun showTicker() {

        if (showMclBlockTickerFirebaseFlag) {
            var tickerMsg = getString(com.tokopedia.design.R.string.saldolock_tickerDescription)
            val startIndex = tickerMsg.indexOf(resources.getString(com.tokopedia.saldodetails.R.string.tickerClickableText))
            val late = Integer.toString(mclLateCount)
            tickerMsg = String.format(resources.getString(com.tokopedia.design.R.string.saldolock_tickerDescription), late)
            val ss = SpannableString(tickerMsg)

            tvTickerMessage!!.movementMethod = LinkMovementMethod.getInstance()

            if (startIndex != -1) {
                ss.setSpan(object : ClickableSpan() {
                    override fun onClick(view: View) {
                        RouteManager.route(context, String.format("%s?url=%s",
                                ApplinkConst.WEBVIEW, SaldoDetailsConstants.SALDOLOCK_PAYNOW_URL))
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.isUnderlineText = false
                        ds.color = resources.getColor(com.tokopedia.design.R.color.tkpd_main_green)
                    }
                }, startIndex - 1, tickerMsg.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }


            tvTickerMessage!!.text = ss
            ivDismissTicker!!.setOnClickListener { v -> layoutTicker!!.gone() }
            layoutTicker!!.show()
        }

    }

    private fun hideTickerMessage() {
        tickerMessageRL!!.gone()
    }

    private fun setLateCount(count: Int) {
        mclLateCount = count
    }

    private fun hideWithdrawTicker() {
        layoutTicker!!.gone()
    }

    private fun showSellerSaldoRL() {
        sellerSaldoBalanceRL!!.show()
    }

    private fun setBuyerSaldoBalance(balance: Long, text: String) {
        saldoBalanceBuyer = balance
        buyerBalanceTV!!.text = text
    }

    private fun setSellerSaldoBalance(amount: Long, formattedAmount: String) {
        saldoBalanceSeller = amount
        sellerBalanceTV!!.text = formattedAmount
    }

    private fun showBuyerSaldoRL() {
        buyerSaldoBalanceRL!!.show()
    }

    private fun showMerchantCreditLineFragment(response: GqlMerchantCreditResponse?) {
        if (response != null && response.isEligible) {
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
        merchantStatusLL!!.show()
        val bundle = Bundle()
        saveInstanceCacheManager = context?.let { context -> SaveInstanceCacheManager(context, true) }
        saveInstanceCacheManager?.put(BUNDLE_PARAM_MERCHANT_CREDIT_DETAILS, response)
        if (!saveInstanceCacheManager?.id.isNullOrBlank()) {
            bundle.putString(BUNDLE_PARAM_MERCHANT_CREDIT_DETAILS_ID, saveInstanceCacheManager?.id)
        }
        childFragmentManager
                .beginTransaction()
                .replace(com.tokopedia.saldodetails.R.id.merchant_credit_line_widget,
                        MerchantCreditDetailFragment.newInstance(bundle))
                .commit()
    }

    private fun showSaldoPrioritasFragment(gqlDetailsResponse: GqlDetailsResponse?) {
        if (gqlDetailsResponse != null && gqlDetailsResponse.isEligible) {
            merchantStatusLL!!.show()
            val bundle = Bundle()
            saveInstanceCacheManager = context?.let { context -> SaveInstanceCacheManager(context, true) }
            saveInstanceCacheManager?.put(BUNDLE_PARAM_SELLER_DETAILS, gqlDetailsResponse)
            if (!saveInstanceCacheManager?.id.isNullOrBlank()) {
                bundle.putString(BUNDLE_PARAM_SELLER_DETAILS_ID, saveInstanceCacheManager?.id)
            }
            childFragmentManager
                    .beginTransaction()
                    .replace(com.tokopedia.saldodetails.R.id.saldo_prioritas_widget,
                            MerchantSaldoPriorityFragment.newInstance(bundle))
                    .commit()
        } else {
            hideSaldoPrioritasFragment()
        }
    }

    private fun hideWarning() {
        holdBalanceLayout!!.hide()
    }

    fun refresh() {
        if (::saldoDetailViewModel.isInitialized)
            saldoDetailViewModel.getUserSaldoBalance()
        saldoHistoryFragment!!.onRefresh()
    }

    private fun setRetry() {
        NetworkErrorHelper.createSnackbarWithAction(activity) { saldoDetailViewModel.getUserSaldoBalance() }.showRetrySnackbar()
    }

    private fun setRetry(error: String) {
        NetworkErrorHelper.createSnackbarWithAction(activity, error
        ) { saldoDetailViewModel.getUserSaldoBalance() }.showRetrySnackbar()
    }

    override fun onDestroy() {
        super.onDestroy()
        performanceInterface.stopMonitoring()
    }
}
