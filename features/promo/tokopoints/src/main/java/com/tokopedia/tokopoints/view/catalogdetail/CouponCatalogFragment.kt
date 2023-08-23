package com.tokopedia.tokopoints.view.catalogdetail

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.databinding.TpFragmentCouponCatalogBinding
import com.tokopedia.tokopoints.di.TokopointBundleComponent
import com.tokopedia.tokopoints.view.cataloglisting.ValidateMessageDialog
import com.tokopedia.tokopoints.view.couponlisting.CouponListingStackedActivity.Companion.getCallingIntent
import com.tokopedia.tokopoints.view.customview.ServerErrorView
import com.tokopedia.tokopoints.view.firebaseAnalytics.TokopointPerformanceConstant.CatalogDetailPlt.Companion.CATALOGDETAIL_TOKOPOINT_PLT
import com.tokopedia.tokopoints.view.firebaseAnalytics.TokopointPerformanceConstant.CatalogDetailPlt.Companion.CATALOGDETAIL_TOKOPOINT_PLT_NETWORK_METRICS
import com.tokopedia.tokopoints.view.firebaseAnalytics.TokopointPerformanceConstant.CatalogDetailPlt.Companion.CATALOGDETAIL_TOKOPOINT_PLT_PREPARE_METRICS
import com.tokopedia.tokopoints.view.firebaseAnalytics.TokopointPerformanceConstant.CatalogDetailPlt.Companion.CATALOGDETAIL_TOKOPOINT_PLT_RENDER_METRICS
import com.tokopedia.tokopoints.view.firebaseAnalytics.TokopointPerformanceMonitoringListener
import com.tokopedia.tokopoints.view.model.CatalogStatusItem
import com.tokopedia.tokopoints.view.model.CatalogsValueEntity
import com.tokopedia.tokopoints.view.sendgift.SendGiftFragment
import com.tokopedia.tokopoints.view.util.*
import com.tokopedia.tokopoints.view.util.CommonConstant.Companion.CATALOG_CLAIM_MESSAGE
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class CouponCatalogFragment : BaseDaggerFragment(), CouponCatalogContract.View, View.OnClickListener, TokopointPerformanceMonitoringListener {
    private var mContainerMain: ViewFlipper? = null
    private var serverErrorView: ServerErrorView? = null
    private val mSubscriptionCouponTimer: Subscription? = null
    private var mSubscriptionCatalogTimer: Subscription? = null
    private var progressBar: TimerUnifySingle? = null
    private val mRefreshRepeatCount = 0
    private var mCouponName: String? = null
    private var mTimer: CountDownTimer? = null
    private var mUserSession: UserSession? = null
    private var catalogsValueEntity: CatalogsValueEntity? = null
    private var pointValueText: TextView? = null
    private var pointValue: Typography? = null
    private var code: String? = null
    private var menu: Menu? = null
    private var quotaContainer: LinearLayout ? = null
    private var timerContainer: ConstraintLayout? = null
    private var minUsageLabel: Typography? = null
    private var minUsageValue: Typography? = null
    private var transactionContainer: ConstraintLayout? = null
    private var quota: Typography? = null
    private var description: Typography? = null
    private var disabledError: Typography? = null
    private var giftSectionMainLayout: ConstraintLayout? = null
    private var giftImage: Typography? = null
    private var giftButton: Typography? = null
    private var bottomSeparator: View? = null
    private var btnAction2: Typography? = null
    private var imgBanner: ImageView? = null
    private var labelPoint: Typography? = null
    private var textDiscount: Typography? = null
    private var tv_coupon_title: Typography? = null
    private var tv_code: Typography? = null
    private var tv_dynamic_infos: Typography? = null
    private var btn_action_claim: UnifyButton? = null
    private var layoutCouponCode: ConstraintLayout? = null
    private var tpBottomSeparator: View? = null
    private var catalogBottomSection: ConstraintLayout? = null
    private var btnContainer: RelativeLayout? = null
    private var btnError: UnifyButton? = null

    override val activityContext: Context
        get() = requireActivity()

    override val appContext: Context
        get() = requireContext()

    @Inject
    lateinit var factory: ViewModelFactory

    private var binding by autoClearedNullable<TpFragmentCouponCatalogBinding>()

    private val mViewModel: CouponCatalogViewModel by lazy { ViewModelProviders.of(this, factory)[CouponCatalogViewModel::class.java] }
    private var pageLoadTimePerformanceMonitoring: PageLoadTimePerformanceInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        startPerformanceMonitoring()
        mUserSession = UserSession(appContext)
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = TpFragmentCouponCatalogBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        this.menu = menu
        inflater.inflate(R.menu.menu_coupon_catalog, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        val id = item.itemId
        if (id == R.id.action_menu_share) {
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            sharingIntent.putExtra(Intent.EXTRA_TEXT, CommonConstant.WebLink.DETAIL + "/" + requireArguments().getString(CommonConstant.EXTRA_CATALOG_CODE))
            startActivity(Intent.createChooser(sharingIntent, null))
            return true
        }
        return true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initListener()
        if (arguments == null) {
            if (activity != null) {
                requireActivity().finish()
            }
            return
        }
        code = requireArguments().getString(CommonConstant.EXTRA_CATALOG_CODE)
        stopPreparePagePerformanceMonitoring()
        startNetworkRequestPerformanceMonitoring()
        mViewModel.getCatalogDetail(code ?: "")
        initObserver()
    }

    private fun initObserver() {
        addCatalogDetailObserver()
        addSendGiftDialogObserver()
        addLatestStatusObserver()
        addStartSaveCouponObserver()
        addRedeemCouponObserver()
    }

    private fun addRedeemCouponObserver() = mViewModel.onRedeemCouponLiveData.observe(
        viewLifecycleOwner,
        androidx.lifecycle.Observer {
            it?.let { RouteManager.route(context, it) }
        }
    )

    private fun addStartSaveCouponObserver() = mViewModel.startSaveCouponLiveData.observe(
        viewLifecycleOwner,
        androidx.lifecycle.Observer {
            when (it) {
                is Success -> redeemCoupon(it.data.cta, it.data.code, it.data.title, it.data.description, it.data.redeemMessage)
                is ValidationError<*, *> -> {
                    if (it.data is ValidateMessageDialog) {
                        showErrorDialog(it.data.desc, it.data.messageCode)
                    }
                }
                else -> {
                    // no-op
                }
            }
        }
    )
    private fun addLatestStatusObserver() = mViewModel.latestStatusLiveData.observe(
        viewLifecycleOwner,
        androidx.lifecycle.Observer {
            it?.let { refreshCatalog(it) }
        }
    )

    private fun addSendGiftDialogObserver() = mViewModel.sendGiftPageLiveData.observe(
        viewLifecycleOwner,
        androidx.lifecycle.Observer {
            when (it) {
                is Success -> gotoSendGiftPage(it.data.id, it.data.title, it.data.pointStr, it.data.banner)
                is ValidationError<*, *> -> {
                    if (it.data is PreValidateError) {
                        onPreValidateError(it.data.title, it.data.message)
                    }
                }
                else -> {
                    // no-op
                }
            }
        }
    )

    private fun addCatalogDetailObserver() = mViewModel.catalogDetailLiveData.observe(
        viewLifecycleOwner,
        androidx.lifecycle.Observer {
            when (it) {
                is Loading -> showLoader()
                is ErrorMessage -> {
                    hideLoader()
                    val internetStatus = NetworkDetector.isConnectedToInternet(context)
                    if (!internetStatus) {
                        showError(internetStatus)
                    } else {
                        showCouponError()
                    }
                }
                is Success -> {
                    stopNetworkRequestPerformanceMonitoring()
                    startRenderPerformanceMonitoring()
                    hideLoader()
                    populateDetail(it.data)
                    stopRenderPerformanceMonitoring()
                    stopPerformanceMonitoring()
                }
                else -> {
                    // no-op
                }
            }
        }
    )

    private fun initViews() {
        binding?.let { binding ->
            mContainerMain = binding.container
            serverErrorView = binding.serverErrorView
            quotaContainer = binding.tpContentCouponCatalog.quotaContainer
            timerContainer = binding.tpContentCouponCatalog.timerContainer
            progressBar = binding.tpContentCouponCatalog.timerUnify
            minUsageLabel = binding.tpContentCouponCatalog.tvMinTxnLabel
            minUsageValue = binding.tpContentCouponCatalog.tvMinTxnValue
            transactionContainer = binding.tpContentCouponCatalog.containerTransaksi
            quota = binding.tpContentCouponCatalog.textQuotaCount
            description = binding.tpContentCouponCatalog.textDescription
            disabledError = binding.tpContentCouponCatalog.textDisabledError
            giftSectionMainLayout = binding.tpContentCouponCatalog.giftSectionMainLayout
            giftImage = binding.tpContentCouponCatalog.giftImage
            giftButton = binding.tpContentCouponCatalog.giftBtn
            bottomSeparator = binding.tpContentCouponCatalog.tpBottomSeparator
            btnAction2 = binding.tpContentCouponCatalog.buttonAction2
            imgBanner = binding.tpContentCouponCatalog.imgBanner
            labelPoint = binding.tpContentCouponCatalog.textPointLabel
            textDiscount = binding.tpContentCouponCatalog.textPointDiscount
            pointValueText = binding.tpContentCouponCatalog.textPointValueLabel

            tv_coupon_title = binding.tpContentCouponCatalog.layoutCouponCode.tvCouponTitle
            tv_code = binding.tpContentCouponCatalog.layoutCouponCode.tvCode
            tv_code?.setCompoundDrawablesRelativeWithIntrinsicBounds(
                null,
                null,
                context?.let { ContextCompat.getDrawable(it, com.tokopedia.iconunify.R.drawable.iconunify_copy) },
                null
            )
            tv_dynamic_infos = binding.tpContentCouponCatalog.layoutCouponCode.tvDynamicInfos
            btn_action_claim = binding.tpContentCouponCatalog.btnActionClaim
            layoutCouponCode = binding.tpContentCouponCatalog.layoutCouponCode.root
            tpBottomSeparator = binding.tpContentCouponCatalog.tpBottomSeparator
            catalogBottomSection = binding.tpContentCouponCatalog.catalogBottomSection
            btnContainer = binding.tpContentCouponCatalog.btnContainer
            btnError = binding.tpCouponNotfoundError.btnError
        }
    }

    private fun initListener() {
        if (view == null) {
            return
        }
        serverErrorView?.setErrorButtonClickListener(
            View.OnClickListener {
                mViewModel.getCatalogDetail(code ?: "")
            }
        )
    }

    override fun openWebView(url: String) {
        RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, url))
    }

    override fun redeemCoupon(cta: String?, code: String?, title: String?, description: String?, redeemMessage: String?) {
        val intent = RouteManager.getIntent(context, cta)
        intent.putExtra(CATALOG_CLAIM_MESSAGE, redeemMessage)
        startActivity(intent)
    }

    private fun showErrorDialog(message: String, resCode: Int) {
        val dialogUnify: DialogUnify?
        val dialogUnifyType = DialogUnify.SINGLE_ACTION
        val labelPositive: String = getString(R.string.tp_label_ok)
        dialogUnify = context?.let { DialogUnify(it, dialogUnifyType, DialogUnify.NO_IMAGE) }
        dialogUnify?.setTitle(getString(R.string.tp_label_exchange_failed))
        dialogUnify?.setDescription(MethodChecker.fromHtml(message))
        if (labelPositive.isNotEmpty()) {
            dialogUnify?.setPrimaryCTAText(labelPositive)
            dialogUnify?.setPrimaryCTAClickListener {
                when (resCode) {
                    CommonConstant.CouponRedemptionCode.LOW_POINT -> {
                        dialogUnify.dismiss()
                        AnalyticsTrackerUtil.sendEvent(
                            context,
                            AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                            AnalyticsTrackerUtil.CategoryKeys.POPUP_PENUKARAN_POINT_TIDAK,
                            AnalyticsTrackerUtil.ActionKeys.CLICK_BELANJA,
                            ""
                        )
                    }
                    CommonConstant.CouponRedemptionCode.QUOTA_LIMIT_REACHED -> {
                        dialogUnify.dismiss()
                        AnalyticsTrackerUtil.sendEvent(
                            context,
                            AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                            AnalyticsTrackerUtil.CategoryKeys.POPUP_KUOTA_HABIS,
                            AnalyticsTrackerUtil.ActionKeys.CLICK_OK,
                            ""
                        )
                    }
                    else -> dialogUnify.dismiss()
                }
            }
        }
        dialogUnify?.show()
    }

    override fun onRealCodeReFresh(realCode: String) {
        if (view == null || mSubscriptionCouponTimer == null) {
            return
        }
        if (realCode.isNotEmpty()) {
            btnAction2?.setText(R.string.tp_label_use)
            btnAction2?.isEnabled = true
            mSubscriptionCouponTimer.unsubscribe()
            return
        }
        if (mRefreshRepeatCount >= CommonConstant.MAX_COUPON_RE_FETCH_COUNT) {
            btnAction2?.setText(R.string.tp_label_refresh_repeat)
            btnAction2?.isEnabled = true
            setButtonTextColor(btnAction2)
            mSubscriptionCouponTimer.unsubscribe()
        }
    }

    override fun onRealCodeReFreshError() {
        if (view == null || mSubscriptionCouponTimer == null) {
            return
        }
        btnAction2?.setText(R.string.tp_label_refresh_repeat)
        btnAction2?.isEnabled = true
        mSubscriptionCouponTimer.unsubscribe()
    }

    override fun refreshCatalog(data: CatalogStatusItem) {
        if (view == null) {
            return
        }
        btnAction2?.isEnabled = !data.isDisabledButton
        if (data.isDisabledButton) {
            btnAction2?.setTextColor(ContextCompat.getColor(activityContext, unifyprinciplesR.color.Unify_NN950_32))
        } else {
            setButtonTextColor(btnAction2)
        }
        updateQuotaValue(data.upperTextDesc)
        // disabling the coupons if not eligible for current membership
        if (data.isDisabled) {
            ImageUtil.dimImage(imgBanner)
        } else {
            ImageUtil.unDimImage(imgBanner)
        }
        pointValue?.setTextColor(ContextCompat.getColor(activityContext, unifyprinciplesR.color.Unify_YN500))
    }

    override fun onPreValidateError(title: String, message: String) {
        val dialogUnify = context?.let { DialogUnify(it, DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE) }
        dialogUnify?.setTitle(title)
        dialogUnify?.setDescription(message)
        context?.let {
            dialogUnify?.setPrimaryCTAText(it.getString(R.string.tp_label_ok))
        }
        dialogUnify?.setPrimaryCTAClickListener {
            dialogUnify.dismiss()
        }
        dialogUnify?.show()
    }

    // setting catalog values to ui
    private fun setCatalogToUi(data: CatalogsValueEntity) {
        if (view == null) {
            return
        }
        if (data.id == 0) {
            showCouponError()
            return
        }

        renderOldUi(data)
        if (data.catalogType != 12 && data.catalogType != 13) {
            btnAction2?.show()
            btnAction2?.isEnabled = !data.isDisabledButton
            description?.text = data.title
            btnAction2?.text = data.buttonStr
            btnAction2?.setBackgroundResource(R.drawable.bg_button_buy_green_tokopoints)
            layoutCouponCode?.hide()
            btn_action_claim?.hide()
        } else {
            giftSectionMainLayout?.hide()
            tpBottomSeparator?.hide()
            if (data.actionCTA?.isShown == true) {
                catalogBottomSection?.show()
                btn_action_claim?.show()
            } else {
                catalogBottomSection?.hide()
                btn_action_claim?.hide()
            }
            if (data.globalPromoCodes?.isNotEmpty() == true) {
                layoutCouponCode?.show()
            } else {
                layoutCouponCode?.hide()
            }
            data.globalPromoCodes?.first().let { promoCode ->
                run {
                    val code = promoCode?.code
                    tv_code?.setOnClickListener {
                        val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("Coupon Code", code)
                        clipboard.setPrimaryClip(clip)
                        promoCode?.toasters?.get(0)?.message?.let { it1 ->
                            view?.let {
                                Toaster.build(it, it1).show()
                            }
                        }
                    }
                    tv_code?.text = promoCode?.code
                    tv_coupon_title?.text = promoCode?.title
                    val sb = java.lang.StringBuilder()
                    promoCode?.dynamicInfos?.forEach {
                        sb.append("\u25CF $it")
                    }
                    tv_dynamic_infos?.text = sb
                }
            }
            when (data.actionCTA?.type) {
                CommonConstant.CTA_TYPE_REDIRECT -> {
                    if (data.actionCTA?.isShown == true) {
                        btnContainer?.show()
                        catalogBottomSection?.hide()
                        btn_action_claim?.text = data.actionCTA?.text
                        btn_action_claim?.isEnabled = data.actionCTA?.isDisabled == false
                        btn_action_claim?.setOnClickListener {
                            if (mUserSession?.isLoggedIn == true) {
                                RouteManager.route(context, data.actionCTA?.applink)
                            } else {
                                startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN), REQUEST_CODE_LOGIN)
                            }
                            AnalyticsTrackerUtil.sendEvent(
                                context,
                                AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                                AnalyticsTrackerUtil.CategoryKeys.PENUKARAN_POINT_DETAIL,
                                AnalyticsTrackerUtil.ActionKeys.CLICK_TUKAR,
                                mCouponName
                            )
                        }
                    } else {
                        catalogBottomSection?.hide()
                    }
                }
                CommonConstant.CTA_TYPE_REDEEM -> {
                    btn_action_claim?.hide()
                    catalogBottomSection?.show()
                    btnAction2?.text = "Klaim"
                    btnAction2?.isEnabled = data.actionCTA?.isDisabled == false
                }
            }
        }
        // start catalog status timer
        mSubscriptionCatalogTimer = Observable.interval(
            CommonConstant.DEFAULT_AUTO_REFRESH_S.toLong(),
            CommonConstant.DEFAULT_AUTO_REFRESH_S.toLong(),
            TimeUnit.MILLISECONDS
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Subscriber<Long?>() {
                override fun onCompleted() {}
                override fun onError(e: Throwable) {}
                override fun onNext(aLong: Long?) {
                    mViewModel.fetchLatestStatus(Arrays.asList(data.id))
                }
            })
        // Coupon impression ga
        AnalyticsTrackerUtil.sendEvent(
            context,
            AnalyticsTrackerUtil.EventKeys.EVENT_VIEW_COUPON,
            AnalyticsTrackerUtil.CategoryKeys.PENUKARAN_POINT_DETAIL,
            AnalyticsTrackerUtil.ActionKeys.VIEW_COUPON,
            mCouponName
        )
    }

    private fun renderOldUi(data: CatalogsValueEntity) {
        mCouponName = data.title
        giftImage?.setCompoundDrawablesWithIntrinsicBounds(MethodChecker.getDrawable(activity, R.drawable.ic_catalog_gift_btn), null, null, null)

        ImageHandler.loadImageFitCenter(imgBanner?.context, imgBanner, data.imageUrlMobile)
        val tvHowToUse: Typography = requireView().findViewById(R.id.how_to_use_content)
        val webTnc: WebView = requireView().findViewById(R.id.tnc_content)
        webTnc.setOnLongClickListener { _ -> true }
        webTnc.isVerticalScrollBarEnabled = false

        data.tnc?.let { webTnc.loadDataWithBaseURL(null, it, "text/html", "utf-8", null) }
        webTnc.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                if (url.startsWith("tokopedia://")) {
                    RouteManager.route(context, url)
                } else {
                    RouteManager.route(context, String.format(Locale.getDefault(), "%s?url=%s", ApplinkConst.WEBVIEW, url))
                }
                return true
            }
        })
        if (!data.howToUse.isNullOrEmpty() && data.howToUse != "<br>") {
            tvHowToUse.text = HtmlUrlHelper(
                data.howToUse ?: "",
                tvHowToUse.context
            ).spannedString
            tvHowToUse.movementMethod = getMovementMethod()
        } else {
            view?.findViewById<Typography>(R.id.how_to_use)?.hide()
            tvHowToUse.hide()
        }
        val pointValue: Typography = requireView().findViewById(R.id.text_point_value_coupon)
        pointValue.text = "Gratis"
        updateQuotaValue(data.upperTextDesc as MutableList<String>?)
        handleQuotaColor()
        // Quota text handling
        if (data.disableErrorMessage.isNullOrEmpty()) {
            disabledError?.hide()
        } else {
            quotaContainer?.show()
            disabledError?.show()
            disabledError?.text = data.disableErrorMessage
        }

        handleTimerTransactionVisibility(data)
        // disabling the coupons if not eligible for current membership
        if (data.isDisabled) {
            ImageUtil.dimImage(imgBanner)
            pointValue.setTextColor(ContextCompat.getColor(pointValue.context, unifyprinciplesR.color.Unify_NN950_44))
        } else {
            ImageUtil.unDimImage(imgBanner)
            pointValue.setTextColor(ContextCompat.getColor(pointValue.context, unifyprinciplesR.color.Unify_NN950_44))
        }
        if (data.isDisabledButton) {
            giftSectionMainLayout?.hide()
            btnAction2?.setTextColor(ContextCompat.getColor(activityContext, unifyprinciplesR.color.Unify_NN950_20))
        } else {
            giftSectionMainLayout?.show()
            setButtonTextColor(btnAction2)
        }
        if (data.pointsSlash <= 0) {
            labelPoint?.hide()
        } else {
            labelPoint?.show()
            labelPoint?.text = data.pointsSlashStr
            labelPoint?.apply {
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
        }
        if (data.discountPercentage <= 0) {
            textDiscount?.hide()
        } else {
            textDiscount?.show()
            textDiscount?.text = data.discountPercentageStr
        }
        if (data.isGift == 1) {
            if (data.isDisabledButton) {
                giftSectionMainLayout?.hide()
                bottomSeparator?.hide()
            } else {
                giftSectionMainLayout?.show()
                bottomSeparator?.show()
                giftButton?.setText(R.string.tp_label_send_now)
                giftButton?.setOnClickListener { view: View? -> mViewModel.startSendGift(data.id, data.title, data.pointsStr, data.imageUrlMobile) }
            }
        } else {
            giftSectionMainLayout?.hide()
            bottomSeparator?.hide()
        }
        // hide gift section when user is in public page
        if (!mUserSession!!.isLoggedIn) {
            giftSectionMainLayout?.hide()
            bottomSeparator?.hide()
        }
        btnAction2?.setOnClickListener { v: View? ->
            // call validate api the show dialog
            if (mUserSession?.isLoggedIn == true) {
                mViewModel.startSaveCoupon(data)
            } else {
                startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN), REQUEST_CODE_LOGIN)
            }
            AnalyticsTrackerUtil.sendEvent(
                context,
                AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                AnalyticsTrackerUtil.CategoryKeys.PENUKARAN_POINT_DETAIL,
                AnalyticsTrackerUtil.ActionKeys.CLICK_TUKAR,
                mCouponName
            )
        }
        if (!mUserSession!!.isLoggedIn) {
            pointValueText?.text = context?.resources?.getString(R.string.tp_masuk_tukar_point)
        }
    }

    private fun showTimer(item: CatalogsValueEntity) {
        progressBar?.timer?.cancel()
        val flipTimer = view?.findViewById<ViewFlipper>(R.id.flip_timer)
        val couponExpire = view?.findViewById<Typography>(R.id.text_timer_value)

        progressBar?.apply {
            onFinish = {
                flipTimer?.displayedChild = CONTAINER_DATE
                couponExpire?.text = resources.getString(R.string.tp_catalog_timer_expire)
            }
            timerTextWidth = TimerUnifySingle.TEXT_WRAP
        }
        if (item.activePeriod != null && item.activePeriod != "0" && item.activePeriod!!.toLong() > 0) {
            flipTimer?.displayedChild = CONTAINER_TIMER
            val timeToExpire = item.activePeriod?.toLong()?.let { convertSecondsToHrMmSs(it) }
            progressBar?.targetDate = timeToExpire
        } else {
            flipTimer?.displayedChild = CONTAINER_DATE
            couponExpire?.text = item.activePeriodDate
        }
    }

    private fun updateQuotaValue(data: MutableList<String>?) {
        if (data.isNullOrEmpty()) {
            quota?.visibility = View.GONE
        } else {
            quotaContainer?.show()
            handleQuotaColor()
            quota?.visibility = View.VISIBLE
            val upperText = StringBuilder()
            for (i in data.indices) {
                if (i == 1) { // exclusive case for handling font color of second index.
                    upperText.append(
                        "<font color='${ColorUtil.getColorFromResToString
                        (activityContext,unifyprinciplesR.color.Unify_RN500)}'>" + data[i] + "</font>"
                    )
                } else {
                    upperText.append(data[i]).append(" ")
                }
            }
            quota?.text = MethodChecker.fromHtml(upperText.toString())
        }
    }

    private fun handleQuotaColor() {
        if (context.isDarkMode()) {
            quota?.background?.setColorFilter(
                ContextCompat.getColor(activityContext, unifyprinciplesR.color.Unify_RN100),
                PorterDuff.Mode.SRC_IN
            )
            quota?.setTextColor(ContextCompat.getColor(activityContext, unifyprinciplesR.color.Unify_RN500))
        }
    }

    private fun handleTimerTransactionVisibility(data: CatalogsValueEntity) {
        if (data.minimumUsageLabel.isNullOrEmpty()) {
            transactionContainer?.hide()
        } else {
            transactionContainer?.show()
            minUsageLabel?.show()
            minUsageLabel?.text = data.minimumUsageLabel
        }

        if (data.minimumUsage.isNullOrEmpty()) {
            minUsageValue?.hide()
        } else {
            minUsageValue?.show()
            minUsageValue?.text = data.minimumUsage
        }
        if ((!data.activePeriod.isNullOrEmpty() && data.activePeriod?.toLong()!! > 0) || !data.activePeriodDate.isNullOrEmpty()) {
            timerContainer?.show()
            showTimer(data)
        } else {
            timerContainer?.hide()
        }
    }

    override fun gotoSendGiftPage(id: Int, title: String?, pointStr: String?, banner: String?) {
        val bundle = Bundle()
        bundle.putInt(CommonConstant.EXTRA_COUPON_ID, id)
        bundle.putString(CommonConstant.EXTRA_COUPON_TITLE, title)
        bundle.putString(CommonConstant.EXTRA_COUPON_POINT, pointStr)
        bundle.putString(CommonConstant.EXTRA_COUPON_BANNER, banner)
        val sendGiftFragment = SendGiftFragment()
        sendGiftFragment.arguments = bundle
        sendGiftFragment.show(childFragmentManager, CommonConstant.FRAGMENT_DETAIL_TOKOPOINT)
    }

    fun setButtonTextColor(btnContinue: Typography?) {
        context?.let {
            if (isDarkMode(it) || !isDarkMode(it)) {
                btnContinue?.setTextColor(ContextCompat.getColor(btnContinue.context, unifyprinciplesR.color.Unify_Static_White))
            }
        }
    }

    private fun setMenuVisibility(menu: Menu, isMenuVisible: Boolean) {
        val menuItem = menu.findItem(R.id.action_menu_share)
        menuItem?.isVisible = isMenuVisible
    }

    override fun onDestroyView() {
        progressBar?.timer?.cancel()
        progressBar?.timer = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        if (mTimer != null) {
            mTimer!!.cancel()
            mTimer = null
        }
        if (mSubscriptionCatalogTimer != null && !mSubscriptionCatalogTimer!!.isUnsubscribed) {
            mSubscriptionCatalogTimer!!.unsubscribe()
        }
        if (mSubscriptionCouponTimer != null && !mSubscriptionCouponTimer.isUnsubscribed) {
            mSubscriptionCouponTimer.unsubscribe()
        }
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        AnalyticsTrackerUtil.sendScreenEvent(activity, screenName)
    }

    override fun showLoader() {
        mContainerMain?.displayedChild = CONTAINER_LOADER
    }

    override fun showError(hasInternet: Boolean) {
        if (menu != null) {
            setMenuVisibility(menu!!, false)
        }
        mContainerMain?.displayedChild = CONTAINER_ERROR
        serverErrorView?.showErrorUi(hasInternet)
    }

    private fun showCouponError() {
        if (menu != null) {
            setMenuVisibility(menu!!, false)
        }
        mContainerMain?.displayedChild = CONTAINER_COUPON_ERROR
        btnError?.setOnClickListener {
            RouteManager.route(context, ApplinkConst.TOKOPEDIA_REWARD)
        }
    }

    override fun hideLoader() {
        mContainerMain?.displayedChild = CONTAINER_DATA
    }

    override fun populateDetail(data: CatalogsValueEntity) {
        catalogsValueEntity = data
        setCatalogToUi(data)
    }

    override fun getScreenName(): String {
        return AnalyticsTrackerUtil.ScreenKeys.COUPON_CATALOG_SCREEN_NAME
    }

    override fun initInjector() {
        getComponent(TokopointBundleComponent::class.java)
            .inject(this)
    }

    override fun onClick(source: View) {
        if (source.id == R.id.text_my_coupon) {
            startActivity(getCallingIntent(activityContext))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_LOGIN && resultCode == Activity.RESULT_OK) {
            pointValueText!!.text = resources.getString(R.string.points_saya)
            mViewModel.getCatalogDetail(code ?: "")
            val item = catalogsValueEntity
            item?.let { if (it.isDisabled) mViewModel.startSaveCoupon(it) }
        }
    }

    companion object {
        private const val CONTAINER_LOADER = 0
        private const val CONTAINER_DATA = 1
        private const val CONTAINER_ERROR = 2
        private const val REQUEST_CODE_LOGIN = 1
        private const val CONTAINER_COUPON_ERROR = 3
        private const val CONTAINER_TIMER = 0
        private const val CONTAINER_DATE = 1

        fun newInstance(extras: Bundle?): Fragment {
            val fragment: Fragment = CouponCatalogFragment()
            fragment.arguments = extras
            return fragment
        }
    }

    override fun startPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring = PageLoadTimePerformanceCallback(
            CATALOGDETAIL_TOKOPOINT_PLT_PREPARE_METRICS,
            CATALOGDETAIL_TOKOPOINT_PLT_NETWORK_METRICS,
            CATALOGDETAIL_TOKOPOINT_PLT_RENDER_METRICS,
            0,
            0,
            0,
            0,
            null
        )

        pageLoadTimePerformanceMonitoring?.startMonitoring(CATALOGDETAIL_TOKOPOINT_PLT)
        pageLoadTimePerformanceMonitoring?.startPreparePagePerformanceMonitoring()
    }

    override fun stopPreparePagePerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopPreparePagePerformanceMonitoring()
    }

    override fun stopPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopMonitoring()
        pageLoadTimePerformanceMonitoring = null
    }

    override fun startNetworkRequestPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.startNetworkRequestPerformanceMonitoring()
    }

    override fun stopNetworkRequestPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopNetworkRequestPerformanceMonitoring()
    }

    override fun startRenderPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.startRenderPerformanceMonitoring()
    }

    override fun stopRenderPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopRenderPerformanceMonitoring()
    }
}
