package com.tokopedia.saldodetails.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
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
import androidx.fragment.app.FragmentActivity
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.remoteconfig.RemoteConfigKey.APP_ENABLE_SALDO_LOCK
import com.tokopedia.saldodetails.commom.analytics.SaldoDetailsConstants
import com.tokopedia.saldodetails.contract.SaldoDetailContract
import com.tokopedia.saldodetails.design.UserStatusInfoBottomSheet
import com.tokopedia.saldodetails.di.SaldoDetailsComponentInstance
import com.tokopedia.saldodetails.presenter.SaldoDetailsPresenter
import com.tokopedia.saldodetails.response.model.GqlDetailsResponse
import com.tokopedia.saldodetails.response.model.GqlMerchantCreditResponse
import com.tokopedia.saldodetails.view.activity.SaldoDepositActivity
import com.tokopedia.showcase.*
import com.tokopedia.user.session.UserSession
import java.util.*
import javax.inject.Inject


class SaldoDepositFragment : BaseDaggerFragment(), SaldoDetailContract.View {

    private val animation_duration: Long = 300


    private val SHOW_CASE_DELAY: Long = 400
    @Inject
    lateinit var saldoDetailsPresenter: SaldoDetailsPresenter

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

    private val isSaldoNativeEnabled: Boolean
        get() = remoteConfig!!.getBoolean(RemoteConfigKey.SALDO_PRIORITAS_NATIVE_ANDROID,
                true)

    private val isMerchantCreditLineEnabled: Boolean
        get() = remoteConfig!!.getBoolean(RemoteConfigKey.APP_ENABLE_MERCHANT_CREDIT_LINE,
                true)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(com.tokopedia.saldodetails.R.layout.fragment_saldo_deposit, container, false)
        initViews(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRemoteConfig()
        initialVar()
        initListeners()
        startShowCase()
    }

    private fun initRemoteConfig() {
        remoteConfig = FirebaseRemoteConfigImpl(getContext())
    }

    private fun startShowCase() {
        Handler().postDelayed({ this.setShowCase() }, SHOW_CASE_DELAY)
    }

    private fun setShowCase() {
        val list = buildShowCase()
        if (list == null) {
            return
        }
        if (!ShowCasePreference.hasShown(context, SaldoDepositFragment::class.java.name)) {
            createShowCase().show(context as Activity?,
                    SaldoDepositFragment::class.java.name,
                    list)
        }
    }

    private fun createShowCase(): ShowCaseDialog {
        return ShowCaseBuilder()
                .backgroundContentColorRes(com.tokopedia.design.R.color.black)
                .titleTextColorRes(com.tokopedia.design.R.color.white)
                .textColorRes(com.tokopedia.design.R.color.grey_400)
                .textSizeRes(com.tokopedia.design.R.dimen.sp_12)
                .titleTextSizeRes(com.tokopedia.design.R.dimen.sp_16)
                .nextStringRes(com.tokopedia.design.R.string.intro_seller_saldo_finish_string)
                .useCircleIndicator(true)
                .clickable(true)
                .useArrow(true)
                .build()
    }

    private fun buildShowCase(): ArrayList<ShowCaseObject>? {

        val list = ArrayList<ShowCaseObject>()
        if (isSellerEnabled && getActivity() is SaldoDepositActivity) {
            list.add(ShowCaseObject(
                    buyerSaldoBalanceRL,
                    getString(com.tokopedia.saldodetails.R.string.saldo_total_balance_buyer),
                    getString(com.tokopedia.saldodetails.R.string.saldo_balance_buyer_desc),
                    ShowCaseContentPosition.BOTTOM,
                    Color.WHITE))

            list.add(ShowCaseObject(
                    sellerSaldoBalanceRL,
                    getString(com.tokopedia.saldodetails.R.string.saldo_total_balance_seller),
                    getString(com.tokopedia.saldodetails.R.string.saldo_intro_description_seller),
                    ShowCaseContentPosition.BOTTOM,
                    Color.WHITE))

            return list

        } else {
            return null
        }
    }

    @SuppressLint("Range")
    private fun initViews(view: View) {

        if (arguments != null) {
            isSellerEnabled = arguments!!.getBoolean(IS_SELLER_ENABLED)
        }

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
        saldoDepositExpandIV!!.setImageDrawable(MethodChecker.getDrawable(getActivity(), com.tokopedia.design.R.drawable.ic_arrow_up_grey))
        layoutTicker = view.findViewById(com.tokopedia.saldodetails.R.id.layout_holdwithdrawl_dialog)
        tvTickerMessage = view.findViewById(com.tokopedia.design.R.id.tv_desc_info)
        ivDismissTicker = view.findViewById(com.tokopedia.design.R.id.iv_dismiss_ticker)

        if (expandLayout) {
            saldoTypeLL!!.visibility = View.VISIBLE
        } else {
            saldoDepositExpandIV!!.animate().rotation(180f).duration = animation_duration
            saldoTypeLL!!.visibility = View.GONE
        }

        if (expandMerchantDetailLayout) {
            merchantDetailLL!!.visibility = View.VISIBLE
        } else {
            merchantDetailsExpandIV!!.animate().rotation(180f).duration = animation_duration
            merchantDetailLL!!.visibility = View.GONE
        }

        saldoHistoryFragment = childFragmentManager.findFragmentById(com.tokopedia.saldodetails.R.id.saldo_history_layout) as SaldoTransactionHistoryFragment?
    }

    private fun initListeners() {

        saldoDepositExpandIV!!.setOnClickListener { v ->
            if (expandLayout) {
                saldoDepositExpandIV!!.animate().rotation(180f).duration = animation_duration
                expandLayout = false
                collapse(saldoTypeLL!!)
            } else {
                saldoDepositExpandIV!!.animate().rotation(0f).duration = animation_duration
                expandLayout = true
                expand(saldoTypeLL!!)
            }

        }

        merchantDetailsExpandIV!!.setOnClickListener { v ->
            if (expandMerchantDetailLayout) {
                merchantDetailsExpandIV!!.animate().rotation(180f).duration = animation_duration
                expandMerchantDetailLayout = false
                collapse(merchantDetailLL!!)
            } else {
                merchantDetailsExpandIV!!.animate().rotation(0f).duration = animation_duration
                expandMerchantDetailLayout = true
                expand(merchantDetailLL!!)
            }
        }

        drawButton!!.setOnClickListener { v ->
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

        checkBalanceStatus!!.setOnClickListener { v ->
            try {
                val intent = RouteManager.getIntent(context, ApplinkConst.INBOX_TICKET)
                startActivity(intent)
            } catch (e: Exception) {

            }
        }

        tickerMessageCloseButton!!.setOnClickListener { v -> tickerMessageRL!!.visibility = View.GONE }
    }

    private fun expand(v: View) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val targetHeight = v.measuredHeight    // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.layoutParams.height = 1
        v.visibility = View.VISIBLE
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
                    v.visibility = View.GONE
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
        androidx.appcompat.app.AlertDialog.Builder(Objects.requireNonNull<FragmentActivity>(getActivity()))
                .setTitle(getActivity()!!.getString(com.tokopedia.saldodetails.R.string.sp_alert_not_verified_yet_title))
                .setMessage(getActivity()!!.getString(com.tokopedia.saldodetails.R.string.sp_alert_not_verified_yet_body))
                .setPositiveButton(getActivity()!!.getString(com.tokopedia.saldodetails.R.string.sp_alert_not_verified_yet_positive)) { dialog, which ->
                    val intent = RouteManager.getIntent(getContext(), ApplinkConstInternalGlobal.SETTING_PROFILE)
                    startActivity(intent)
                    dialog.dismiss()
                }
                .setNegativeButton(getActivity()!!.getString(com.tokopedia.saldodetails.R.string.sp_alert_not_verified_yet_negative)) { dialog, which -> dialog.dismiss() }
                .setCancelable(false)
                .show()
    }

    private fun goToWithdrawActivity() {
        if (getActivity() != null) {
            val intent = RouteManager.getIntent(getActivity(),
                    ApplinkConstInternalGlobal.WITHDRAW)
            val bundle = Bundle()
            bundle.putBoolean(IS_SELLER, isSellerEnabled)
            intent.putExtras(bundle)
            saldoDetailsPresenter.onDrawClicked(intent, statusWithDrawLock, mclLateCount, showMclBlockTickerFirebaseFlag)
        }
    }

    private fun showSaldoWarningDialog() {
        androidx.appcompat.app.AlertDialog.Builder(Objects.requireNonNull<FragmentActivity>(getActivity()))
                .setTitle(getActivity()!!.getString(com.tokopedia.saldodetails.R.string.sp_saldo_withdraw_warning_title))
                .setMessage(getActivity()!!.getString(com.tokopedia.saldodetails.R.string.sp_saldo_withdraw_warning_desc))
                .setPositiveButton(
                        getActivity()!!.getString(com.tokopedia.saldodetails.R.string.sp_saldo_withdraw_warning_positiv_button)
                ) { dialog, which -> goToWithdrawActivity() }
                .setCancelable(true)
                .show()
    }

    override fun getSellerSaldoBalance(): Long {
        return saldoBalanceSeller
    }

    override fun getBuyerSaldoBalance(): Long {
        return saldoBalanceBuyer
    }

    override fun isUserSeller(): Boolean {
        return isSellerEnabled
    }

    protected fun initialVar() {
        saldoDetailsPresenter.isSeller = isSellerEnabled
        totalBalanceTitle!!.text = resources.getString(com.tokopedia.saldodetails.R.string.total_saldo_text)
        totalBalanceInfo!!.visibility = View.GONE
        buyerSaldoBalanceRL!!.visibility = View.VISIBLE
        sellerSaldoBalanceRL!!.visibility = View.VISIBLE

        totalBalanceInfo!!.setOnClickListener { v -> showBottomSheetInfoDialog(false) }

        buyerBalanceInfoIcon!!.setOnClickListener { v -> showBottomSheetInfoDialog(false) }

        sellerBalanceInfoIcon!!.setOnClickListener { v -> showBottomSheetInfoDialog(true) }


        if (getActivity() != null) {
            if (isSaldoNativeEnabled && isMerchantCreditLineEnabled) {
                saldoDetailsPresenter.getUserFinancialStatus()
            } else {

                if (isSaldoNativeEnabled) {
                    saldoDetailsPresenter.getMerchantSaldoDetails()
                } else {
                    hideSaldoPrioritasFragment()
                }

                if (isMerchantCreditLineEnabled) {
                    saldoDetailsPresenter.getMerchantCreditLineDetails()
                } else {
                    hideMerchantCreditLineFragment()
                }
            }
        }
    }

    override fun hideUserFinancialStatusLayout() {
        merchantStatusLL!!.visibility = View.GONE
    }

    private fun showBottomSheetInfoDialog(isSellerClicked: Boolean) {
        val userStatusInfoBottomSheet = UserStatusInfoBottomSheet(context!!)

        if (isSellerClicked) {
            userStatusInfoBottomSheet.setBody(resources.getString(com.tokopedia.saldodetails.R.string.saldo_balance_seller_desc))
            userStatusInfoBottomSheet.setTitle(resources.getString(com.tokopedia.saldodetails.R.string.saldo_total_balance_seller))
        } else {
            userStatusInfoBottomSheet.setBody(resources.getString(com.tokopedia.saldodetails.R.string.saldo_balance_buyer_desc))
            userStatusInfoBottomSheet.setTitle(resources.getString(com.tokopedia.saldodetails.R.string.saldo_total_balance_buyer))
        }

        userStatusInfoBottomSheet.setButtonText(getString(com.tokopedia.saldodetails.R.string.sp_saldo_withdraw_warning_positiv_button))
        userStatusInfoBottomSheet.show()
    }

    override fun initInjector() {

        val saldoDetailsComponent = SaldoDetailsComponentInstance.getComponent(Objects.requireNonNull<FragmentActivity>(getActivity()).getApplication())
        saldoDetailsComponent!!.inject(this)
        saldoDetailsPresenter.attachView(this)
    }

    override fun getScreenName(): String? {
        return null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        onFirstTimeLaunched()
    }

    private fun onFirstTimeLaunched() {
        val remoteConfig = FirebaseRemoteConfigImpl(getContext())
        showMclBlockTickerFirebaseFlag = remoteConfig.getBoolean(APP_ENABLE_SALDO_LOCK, false)
        saldoDetailsPresenter.getSaldoBalance()
        saldoDetailsPresenter.getTickerWithdrawalMessage()
        saldoDetailsPresenter.getMCLLateCount()
    }

    override fun showWithdrawalNoPassword() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(resources.getString(com.tokopedia.saldodetails.R.string.sp_error_deposit_no_password_title))
        builder.setMessage(resources.getString(com.tokopedia.saldodetails.R.string.sp_error_deposit_no_password_content))
        builder.setPositiveButton(resources.getString(com.tokopedia.saldodetails.R.string.sp_error_no_password_yes)) { dialogInterface, i ->
            intentToAddPassword(context!!)
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

    override fun setBalance(totalBalance: Long, summaryUsebleDepositIdr: String) {
        if (!TextUtils.isEmpty(summaryUsebleDepositIdr)) {
            totalBalanceTV!!.text = summaryUsebleDepositIdr
            totalBalanceTV!!.visibility = View.VISIBLE
        } else {
            totalBalanceTV!!.visibility = View.GONE
        }

    }

    override fun setWithdrawButtonState(state: Boolean) {
        if (state) {
            drawButton!!.setTextColor(Color.WHITE)
        } else {
            drawButton!!.setTextColor(resources.getColor(com.tokopedia.design.R.color.black_26))
        }
        drawButton!!.isEnabled = state
        drawButton!!.isClickable = state
    }

    @SuppressLint("Range")
    override fun showErrorMessage(error: String) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), error)
    }

    override fun showHoldWarning(text: String) {
        holdBalanceLayout!!.visibility = View.VISIBLE
        amountBeingReviewed!!.text = String.format(resources.getString(com.tokopedia.saldodetails.R.string.saldo_hold_balance_text), text)
        amountBeingReviewed!!.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun hideSaldoPrioritasFragment() {
        saldoFrameLayout!!.visibility = View.GONE
        Handler().postDelayed({
            if (merchantCreditFrameLayout!!.visibility != View.VISIBLE) {
                hideUserFinancialStatusLayout()
            }
        }, CHECK_VISIBILITY_DELAY)
    }

    override fun hideMerchantCreditLineFragment() {
        merchantCreditFrameLayout!!.visibility = View.GONE
        Handler().postDelayed({
            if (saldoFrameLayout!!.visibility != View.VISIBLE) {
                hideUserFinancialStatusLayout()
            }
        }, CHECK_VISIBILITY_DELAY)
    }

    override fun showTickerMessage(withdrawalTicker: String) {
        tickerMessageRL!!.visibility = View.VISIBLE
        tickeRMessageTV!!.text = withdrawalTicker
    }

    fun showTicker() {

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
            ivDismissTicker!!.setOnClickListener { v -> layoutTicker!!.visibility = View.GONE }
            layoutTicker!!.visibility = View.VISIBLE
        }

    }

    override fun hideTickerMessage() {
        tickerMessageRL!!.visibility = View.GONE
    }

    override fun setLateCount(count: Int) {
        mclLateCount = count
    }

    override fun hideWithdrawTicker() {
        layoutTicker!!.visibility = View.GONE
    }

    override fun showSellerSaldoRL() {
        sellerSaldoBalanceRL!!.visibility = View.VISIBLE
    }

    override fun setBuyerSaldoBalance(balance: Long, text: String) {
        saldoBalanceBuyer = balance
        buyerBalanceTV!!.text = text
    }

    override fun setSellerSaldoBalance(amount: Long, formattedAmount: String) {
        saldoBalanceSeller = amount
        sellerBalanceTV!!.text = formattedAmount
    }

    override fun showBuyerSaldoRL() {
        buyerSaldoBalanceRL!!.visibility = View.VISIBLE
    }

    override fun showMerchantCreditLineFragment(response: GqlMerchantCreditResponse?) {
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

    fun showMerchantCreditLineWidget(response: GqlMerchantCreditResponse) {
        merchantStatusLL!!.show()
        val bundle = Bundle()
        saveInstanceCacheManager = SaveInstanceCacheManager(context!!, true)
        saveInstanceCacheManager!!.put(BUNDLE_PARAM_MERCHANT_CREDIT_DETAILS, response)
        if (!saveInstanceCacheManager?.id.isNullOrBlank()) {
            bundle.putString(BUNDLE_PARAM_MERCHANT_CREDIT_DETAILS_ID, saveInstanceCacheManager?.id)
        }
        childFragmentManager
                .beginTransaction()
                .replace(com.tokopedia.saldodetails.R.id.merchant_credit_line_widget, MerchantCreditDetailFragment.newInstance(bundle))
                .commit()
    }

    override fun showSaldoPrioritasFragment(gqlDetailsResponse: GqlDetailsResponse?) {
        if (gqlDetailsResponse != null && gqlDetailsResponse.isEligible) {
            merchantStatusLL!!.show()
            val bundle = Bundle()
            saveInstanceCacheManager = SaveInstanceCacheManager(context!!, true)
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

    override fun hideWarning() {
        holdBalanceLayout!!.hide()
    }

    override fun refresh() {
        saldoHistoryFragment!!.onRefresh()
    }

    override fun showEmptyState() {
        NetworkErrorHelper.showEmptyState(getActivity(), view) { saldoDetailsPresenter.getSaldoBalance() }
        try {
            val retryLoad = view!!.findViewById<View>(com.tokopedia.abstraction.R.id.main_retry)
            retryLoad.translationY = (topSlideOffBar!!.height / 2).toFloat()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

    }

    override fun setRetry() {
        NetworkErrorHelper.createSnackbarWithAction(getActivity()) { saldoDetailsPresenter.getSaldoBalance() }.showRetrySnackbar()
    }

    override fun setRetry(error: String) {
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), error
        ) { saldoDetailsPresenter.getSaldoBalance() }.showRetrySnackbar()
    }

    override fun onDestroy() {
        saldoDetailsPresenter.detachView()
        super.onDestroy()
    }

    companion object {

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

        fun createInstance(isSellerEnabled: Boolean): SaldoDepositFragment {
            val saldoDepositFragment = SaldoDepositFragment()
            val bundle = Bundle()
            bundle.putBoolean(IS_SELLER_ENABLED, isSellerEnabled)
            saldoDepositFragment.arguments = bundle
            return saldoDepositFragment
        }
    }
}

