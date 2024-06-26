package com.tokopedia.tokopoints.view.coupondetail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.media.loader.loadImageFitCenter
import com.tokopedia.media.loader.wrapper.MediaCacheStrategy
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.databinding.TpFragmentCouponDetailBinding
import com.tokopedia.tokopoints.di.TokopointBundleComponent
import com.tokopedia.tokopoints.view.couponlisting.CouponListingStackedActivity
import com.tokopedia.tokopoints.view.customview.SwipeCardView
import com.tokopedia.tokopoints.view.firebaseAnalytics.TokopointPerformanceConstant.CoupondetailPlt.Companion.COUPONDETAIL_TOKOPOINT_PLT
import com.tokopedia.tokopoints.view.firebaseAnalytics.TokopointPerformanceConstant.CoupondetailPlt.Companion.COUPONDETAIL_TOKOPOINT_PLT_NETWORK_METRICS
import com.tokopedia.tokopoints.view.firebaseAnalytics.TokopointPerformanceConstant.CoupondetailPlt.Companion.COUPONDETAIL_TOKOPOINT_PLT_PREPARE_METRICS
import com.tokopedia.tokopoints.view.firebaseAnalytics.TokopointPerformanceConstant.CoupondetailPlt.Companion.COUPONDETAIL_TOKOPOINT_PLT_RENDER_METRICS
import com.tokopedia.tokopoints.view.firebaseAnalytics.TokopointPerformanceMonitoringListener
import com.tokopedia.tokopoints.view.fragment.CloseableBottomSheetFragment
import com.tokopedia.tokopoints.view.model.CatalogsValueEntity
import com.tokopedia.tokopoints.view.model.CouponSwipeDetail
import com.tokopedia.tokopoints.view.model.CouponSwipeUpdate
import com.tokopedia.tokopoints.view.model.CouponValueEntity
import com.tokopedia.tokopoints.view.util.*
import com.tokopedia.tokopoints.view.util.CommonConstant.Companion.CATALOG_CLAIM_MESSAGE
import com.tokopedia.tokopoints.view.validatePin.ValidateMerchantPinFragment
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.lifecycle.autoClearedNullable
import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class CouponDetailFragment : BaseDaggerFragment(), CouponDetailContract.View, View.OnClickListener, TokopointPerformanceMonitoringListener {
    private var mSubscriptionCouponTimer: Subscription? = null
    private var mRefreshRepeatCount = 0
    private var mCouponName: String? = null
    var mTimer: CountDownTimer? = null
    var phoneVerificationState: Boolean? = null
    var mCTA: String = ""
    var mCode: String = ""
    private var pageLoadTimePerformanceMonitoring: PageLoadTimePerformanceInterface? = null
    private var redeemMessage: String = ""

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var binding by autoClearedNullable<TpFragmentCouponDetailBinding>()

    val mPresenter: CouponDetailViewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(CouponDetailViewModel::class.java) }

    private var mRealCode: String? = null
    private var mBottomSheetFragment: CloseableBottomSheetFragment? = null

    override val activityContext: Context?
        get() = activity

    override val appContext: Context?
        get() = activity

    override fun onCreate(savedInstanceState: Bundle?) {
        startPerformanceMonitoring()
        super.onCreate(savedInstanceState)
        redeemMessage = arguments?.getString(CATALOG_CLAIM_MESSAGE, "") ?: ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = TpFragmentCouponDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        initListener()
        stopPreparePagePerformanceMonitoring()
        startNetworkRequestPerformanceMonitoring()
        mPresenter.isPhonerVerfied()
        ToasterHelper.showCouponClaimToast(redeemMessage,view, BOTTOM_HEIGHT_TOASTER)
    }

    private fun initObserver() {
        obserserFinish()
        observeCouponDetail()
        observeSwipeDetail()
        observeOnSwipeCoupon()
        observePinPage()
        observeRefetchCoupon()
        onbserveOnRedeemCoupon()
        observeUserInfo()
    }

    private fun observeUserInfo() = mPresenter.userInfo.observe(viewLifecycleOwner, Observer {
        it.let {
            phoneVerificationState = it.mfGetUserInfo?.verifiedMsisdn
        }
    })

    private fun obserserFinish() = mPresenter.finish.observe(viewLifecycleOwner, Observer {
        it?.let {
            activity?.finish()
        }
    })

    private fun onbserveOnRedeemCoupon() = mPresenter.onRedeemCoupon.observe(viewLifecycleOwner, Observer {
        it?.let {
            when (it) {
                is ErrorMessage -> RouteManager.route(context, it.data)
                is Success -> showToasterAndRedirect(it.data)
                else -> {
                    //no-op
                }
            }
            return@let
        }
    })

    private fun observeRefetchCoupon() = mPresenter.onReFetch.observe(viewLifecycleOwner, Observer {
        it?.let {
            when (it) {
                is ErrorMessage -> onRealCodeReFreshError()
                is Success -> onRealCodeReFresh(it.data)
                else -> {
                    //no-op
                }
            }
        }
    })

    private fun observePinPage() = mPresenter.pinPageData.observe(viewLifecycleOwner, Observer {
        it?.let { showPinPage(it.code, it.pinText) }
    })

    private fun observeOnSwipeCoupon() = mPresenter.onCouponSwipe.observe(viewLifecycleOwner, Observer {
        it.let {
            when (it) {
                is ErrorMessage -> onSwipeError(it.data)
                is Success -> onSwipeResponse(it.data, "", "")
                else -> {
                    //no-op
                }
            }
        }
    })

    private fun observeSwipeDetail() = mPresenter.swipeDetail.observe(viewLifecycleOwner, Observer {
        it?.let { setSwipeUi(it) }
    })

    private fun observeCouponDetail() = mPresenter.detailLiveData.observe(viewLifecycleOwner, Observer {
        it?.let {
            when (it) {
                is Loading -> showLoader()
                is ErrorMessage -> {
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
                    setCouponToUi(it.data)
                    stopRenderPerformanceMonitoring()
                    stopPerformanceMonitoring()
                }
                else -> {
                    //no-op
                }
            }
        }
    })

    override fun onDestroy() {
        if (mTimer != null) {
            mTimer!!.cancel()
            mTimer = null
        }

        if (mSubscriptionCouponTimer != null && !mSubscriptionCouponTimer!!.isUnsubscribed) {
            mSubscriptionCouponTimer!!.unsubscribe()
        }

        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        AnalyticsTrackerUtil.sendScreenEvent(activity, screenName)
    }

    override fun showLoader() {
        binding?.container?.displayedChild = CONTAINER_LOADER
    }

    override fun showError(networkError: Boolean) {
        binding?.apply {
            container.displayedChild = CONTAINER_ERROR
            serverErrorView.showErrorUi(networkError)
        }
    }

    private fun showCouponError() {
        binding?.apply {
            container.displayedChild = CONTAINER_COUPON_ERROR
            serverErrorView.setErrorButtonClickListener {
                RouteManager.route(context, ApplinkConst.TOKOPEDIA_REWARD)
            }
        }
    }

    override fun hideLoader() {
        binding?.container?.displayedChild = CONTAINER_DATA
    }

    override fun populateDetail(data: CouponValueEntity) {
        binding?.container?.postDelayed({ setCouponToUi(data) }, CommonConstant.UI_SETTLING_DELAY_MS2.toLong())
    }

    override fun getScreenName(): String {
        return AnalyticsTrackerUtil.ScreenKeys.COUPON_DETAIL_SCREEN_NAME
    }

    override fun initInjector() {
        getComponent(TokopointBundleComponent::class.java).inject(this)
    }

    override fun onClick(source: View) {
        if (source.id == R.id.text_my_coupon) {
            val context = activityContext
            context?.let { startActivity(CouponListingStackedActivity.getCallingIntent(context)) }
        }
    }

    private fun initListener() {
        binding?.serverErrorView?.setErrorButtonClickListener(View.OnClickListener { mPresenter.onErrorButtonClick() })
    }

    override fun openWebView(url: String) {
        RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, url)
    }

    private fun openPhoneVerificationBottomSheet() {
        val view = LayoutInflater.from(context).inflate(R.layout.phoneverification_bottomsheet, null, false)
        val closeableBottomSheetDialog = BottomSheetUnify()
        closeableBottomSheetDialog.apply {
            setChild(view)
            showCloseIcon = false
            showHeader = false
        }
        val btnVerifikasi = view.findViewById<UnifyButton>(R.id.btn_verifikasi)
        val btnCancel = view.findViewById<AppCompatImageView>(R.id.cancel_verifikasi)
        btnVerifikasi.setOnClickListener {
            val intent = RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.ADD_PHONE)
            startActivityForResult(intent, REQUEST_CODE_VERIFICATION_PHONE)

            AnalyticsTrackerUtil.sendEvent(context,
                    AnalyticsTrackerUtil.EventKeys.KEY_EVENT_PROFILE_VALUE,
                    AnalyticsTrackerUtil.CategoryKeys.KEY_EVENT_CATEGORY_PROFILE_VALUE,
                    AnalyticsTrackerUtil.ActionKeys.KEY_EVENT_ACTION_PROFILE_VALUE,
                    "")
            closeableBottomSheetDialog.dismiss()

        }
        btnCancel.setOnClickListener {
            closeableBottomSheetDialog.dismiss()
            AnalyticsTrackerUtil.sendEvent(context,
                    AnalyticsTrackerUtil.EventKeys.KEY_EVENT_PROFILE_VALUE,
                    AnalyticsTrackerUtil.CategoryKeys.KEY_EVENT_CATEGORY_PROFILE_VALUE,
                    AnalyticsTrackerUtil.ActionKeys.KEY_EVENT_ACTION_PROFILE_VALUE_BATAL, "")
        }

        closeableBottomSheetDialog.show(childFragmentManager, "")
    }

    override fun showRedeemFullError(item: CatalogsValueEntity, title: String, desc: String) {

    }

    override fun onRealCodeReFresh(realCode: String?) {
        if (mSubscriptionCouponTimer == null) {
            return
        }
        try {
            binding?.tpContentCouponDetail?.llBottomButton?.apply {
                this@CouponDetailFragment.mRealCode = realCode
                if (TextUtils.isEmpty(realCode)) {
                    btnContinue.setText(R.string.tp_label_use)
                    btnContinue.isEnabled = true
                    progressRefetchCode.visibility = View.GONE
                    setButtonTextColor(btnContinue)
                    mSubscriptionCouponTimer?.unsubscribe()
                    return
                }

                if (mRefreshRepeatCount >= CommonConstant.MAX_COUPON_RE_FETCH_COUNT) {
                    btnContinue.setText(R.string.tp_label_refresh_repeat)
                    btnContinue.isEnabled = true
                    progressRefetchCode.visibility = View.GONE
                    setButtonTextColor(btnContinue)
                    mSubscriptionCouponTimer?.unsubscribe()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onRealCodeReFreshError() {
        if (view == null || mSubscriptionCouponTimer == null) {
            return
        }

        try {
            val btnAction2 = view?.findViewById<UnifyButton>(R.id.btn_continue)
            val progressBar = view?.findViewById<ProgressBar>(R.id.progress_refetch_code)
            btnAction2?.setText(R.string.tp_label_refresh_repeat)
            btnAction2?.isEnabled = true
            progressBar?.visibility = View.GONE
            if (btnAction2 != null) {
                setButtonTextColor(btnAction2)
            }
            mSubscriptionCouponTimer?.unsubscribe()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun setCouponToUi(data: CouponValueEntity) {
        binding?.apply {
            if (data.isEmpty) {
                showCouponError()
                return
            }
            hideLoader()
            mCouponName = data.title
            mCTA = data.cta
            mCode = data.realCode
            val description = tpContentCouponDetail.tvTitle
            val label = tpContentCouponDetail.textTimeLabel
            val value = tpContentCouponDetail.textTimeValue
            val btnAction2 = tpContentCouponDetail.llBottomButton.btnContinue
            val imgBanner = tpContentCouponDetail.imgBannerCoupon
            val imgLabel = tpContentCouponDetail.imgTime
            val textMinExchangeValue = tpContentCouponDetail.tvMinTxnValue
            val textMinExchangeLabel = tpContentCouponDetail.tvMinTxnLabel
            val imgMinExchange = tpContentCouponDetail.ivRp
            val progressBar = tpContentCouponDetail.llBottomButton.progressRefetchCode

            description.text = data.title
            imgBanner?.loadImageFitCenter(data.imageUrlMobile)

            if (data.isIs_show_button) {
                btnAction2.show()
                tpContentCouponDetail.llBottomButton.root.show()
            }
            else{
                btnAction2.hide()
                tpContentCouponDetail.llBottomButton.root.hide()
            }
            label.visibility = View.VISIBLE
            label.text = data.usage.text
            value.visibility = View.VISIBLE
            imgLabel.visibility = View.VISIBLE
            value.text = data.usage.usageStr.trim { it <= ' ' }
            if (data.usage.btnUsage.type.equals("invisible", ignoreCase = true)) {
                tpContentCouponDetail.llBottomButton.root.hide()
                btnAction2.visibility = View.GONE
            } else {
                tpContentCouponDetail.llBottomButton.root.show()
                btnAction2.visibility = View.VISIBLE
            }
            if (data.usage.btnUsage.type.equals("disable", ignoreCase = true)) {
                btnAction2.setTextColor(MethodChecker.getColor(context, unifyprinciplesR.color.Unify_NN950))
                btnAction2.background?.colorFilter = PorterDuffColorFilter(MethodChecker.getColor(context, unifyprinciplesR.color.Unify_NN50), PorterDuff.Mode.SRC_IN)
                btnAction2.isEnabled = false
            }


            if (data.minimumUsageLabel.isEmpty()) {
                textMinExchangeLabel.hide()
                imgMinExchange.hide()
                textMinExchangeValue.hide()
            } else {
                textMinExchangeValue.show()
                textMinExchangeValue.text = data.minimumUsage
                imgMinExchange.show()
                textMinExchangeLabel.show()
                textMinExchangeLabel.text = data.minimumUsageLabel
            }

            if (data.usage.activeCountDown > 0 || data.usage.expiredCountDown <= 0) {
                imgLabel.setColorFilter(ContextCompat.getColor(imgLabel.context, unifyprinciplesR.color.Unify_NN500), android.graphics.PorterDuff.Mode.SRC_IN)
                imgMinExchange.setColorFilter(ContextCompat.getColor(imgMinExchange.context, unifyprinciplesR.color.Unify_NN500), android.graphics.PorterDuff.Mode.SRC_IN)
            } else {
                imgLabel.setColorFilter(ContextCompat.getColor(imgLabel.context, unifyprinciplesR.color.Unify_GN500), android.graphics.PorterDuff.Mode.SRC_IN)
                imgMinExchange.setColorFilter(ContextCompat.getColor(imgMinExchange.context, unifyprinciplesR.color.Unify_GN500), android.graphics.PorterDuff.Mode.SRC_IN)
            }

            this@CouponDetailFragment.mRealCode = data.realCode
            btnAction2.setOnClickListener {

                if (phoneVerificationState != null && phoneVerificationState == false) {
                    openPhoneVerificationBottomSheet()
                } else {
                    val code = mRealCode as String
                    if (!TextUtils.isEmpty(code)) {
                        mPresenter.redeemCoupon(code, data.cta)
                        AnalyticsTrackerUtil.sendEvent(context,
                            AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                            AnalyticsTrackerUtil.CategoryKeys.KUPON_MILIK_SAYA_DETAIL,
                            AnalyticsTrackerUtil.ActionKeys.CLICK_GUNAKAN,
                            mCouponName)
                    } else {
                        if (arguments != null && arguments?.getString(CommonConstant.EXTRA_COUPON_CODE) != null) {
                            btnAction2.isEnabled = false
                            btnAction2.setTextColor(resources.getColor(unifyprinciplesR.color.Unify_NN950_20))
                            progressBar.visibility = View.VISIBLE
                            btnAction2.text = ""
                            mPresenter.reFetchRealCode()
                        }
                    }
                }
            }
            setupInfoPager(data.howToUse, data.tnc)

            if (data.realCode.isNotEmpty()) {
                btnAction2.setText(R.string.tp_label_use)
                btnAction2.isEnabled = true
                setButtonTextColor(btnAction2)
                progressBar.visibility = View.GONE
            } else {
                //check for real_code and start rxjava-timer
                btnAction2.isEnabled = false
                btnAction2.setTextColor(resources.getColor(unifyprinciplesR.color.Unify_NN950_20))
                progressBar.visibility = View.VISIBLE

                mSubscriptionCouponTimer = Observable.interval(CommonConstant.COUPON_RE_FETCH_DELAY_S.toLong(), CommonConstant.COUPON_RE_FETCH_DELAY_S.toLong(), TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Subscriber<Long>() {
                        override fun onCompleted() {

                        }

                        override fun onError(e: Throwable) {

                        }

                        override fun onNext(aLong: Long?) {
                            if (arguments != null && arguments!!.getString(CommonConstant.EXTRA_COUPON_CODE) != null) {
                                mPresenter.reFetchRealCode()
                                mRefreshRepeatCount++
                            }
                        }
                    })
            }

            addCountDownTimer(data, value, btnAction2)

            //Coupon impression ga
            AnalyticsTrackerUtil.sendEvent(context,
                AnalyticsTrackerUtil.EventKeys.EVENT_VIEW_COUPON,
                AnalyticsTrackerUtil.CategoryKeys.KUPON_MILIK_SAYA_DETAIL,
                AnalyticsTrackerUtil.ActionKeys.VIEW_MY_COUPON_DETAIL,
                mCouponName)
        }
    }


    private fun setSwipeUi(swipeDetail: CouponSwipeDetail) = binding?.apply {
        tpContentCouponDetail.llBottomButton.root.show()
        tpContentCouponDetail.llBottomButton.layoutCouponSwipe.root.show()
        tpContentCouponDetail.llBottomButton.layoutCouponSwipe.cardSwipe.apply {
            setTitle(swipeDetail.text)
            setOnSwipeListener(object : SwipeCardView.OnSwipeListener {
                override fun onComplete() {
                    mPresenter.onSwipeComplete()
                    AnalyticsTrackerUtil.sendEvent(activityContext,
                        AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                        AnalyticsTrackerUtil.CategoryKeys.KUPON_MILIK_SAYA_DETAIL,
                        AnalyticsTrackerUtil.ActionKeys.SWIPE_COUPON,
                        "")
                }

                override fun onPartialSwipe() {
                    AnalyticsTrackerUtil.sendEvent(activityContext,
                        AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                        AnalyticsTrackerUtil.CategoryKeys.KUPON_MILIK_SAYA_DETAIL,
                        AnalyticsTrackerUtil.ActionKeys.SWIPE_COUPON,
                        "")
                }
            })
            if (swipeDetail.partnerCode.isNotEmpty()) {
                couponCode = swipeDetail.partnerCode
            }
            showBarCodeView(swipeDetail.note, "", "")
        }
    }


    private fun setupInfoPager(howToUse: String, tnc: String) {
        val tvHowToUse: Typography? = view?.findViewById(R.id.how_to_use_content)
        val tvTnc: Typography? = view?.findViewById(R.id.tnc_content)

        if (tnc.isNotEmpty() && tnc != "<br>") {
            tvTnc?.text = context?.let {
                HtmlUrlHelper(
                    tnc, it
                ).spannedString
            }
            tvTnc?.movementMethod = getMovementMethod()
        } else {
            view?.findViewById<Typography>(R.id.tnc)?.hide()
            view?.findViewById<View>(R.id.mid_separator)?.hide()
            tvTnc?.hide()
        }
        if (howToUse.isNotEmpty() && howToUse != "<br>") {
            tvHowToUse?.text = context?.let {
                HtmlUrlHelper(
                    howToUse, it
                ).spannedString
            }
            tvHowToUse?.movementMethod = getMovementMethod()
        } else {
            view?.findViewById<Typography>(R.id.how_to_use)?.hide()
            view?.findViewById<View>(R.id.mid_separator)?.hide()
            tvHowToUse?.hide()
        }
    }

    private fun addCountDownTimer(item: CouponValueEntity, label: Typography?, btnContinue: UnifyButton?) {
        if (mTimer != null || view == null) {
            mTimer?.cancel()
        }

        if (item.usage.activeCountDown < 1) {
            if (item.usage.expiredCountDown > 0 && item.usage.expiredCountDown <= CommonConstant.COUPON_SHOW_COUNTDOWN_MAX_LIMIT_S) {
                val progressBar = view?.findViewById<TimerUnifySingle>(R.id.progress_timer)
                progressBar?.visibility = View.VISIBLE
                label?.hide()
                progressBar?.apply {
                    timerTextWidth = TimerUnifySingle.TEXT_WRAP
                    onFinish = {
                        progressBar.visibility = View.GONE
                        label?.show()
                        label?.text = "00 : 00 : 00"
                        btnContinue?.text = "Expired"
                        btnContinue?.isEnabled = false
                        btnContinue?.setTextColor(ContextCompat.getColor(btnContinue.context, unifyprinciplesR.color.Unify_NN950_20))
                    }
                    onTick = {
                        item.usage.expiredCountDown = it / 1000
                    }
                }

                val timerValue = convertSecondsToHrMmSs(item.usage.expiredCountDown)
                progressBar?.targetDate = timerValue
            } else {
                btnContinue?.text = item.usage.btnUsage.text
                btnContinue?.isEnabled = true
                if (btnContinue != null) {
                    setButtonTextColor(btnContinue)
                }
            }
        } else {
            if (item.usage.activeCountDown > 0) {
                btnContinue?.isEnabled = false
                btnContinue?.setTextColor(ContextCompat.getColor(btnContinue.context, unifyprinciplesR.color.Unify_NN950_20))
                if (item.usage.activeCountDown <= CommonConstant.COUPON_SHOW_COUNTDOWN_MAX_LIMIT_S) {
                    mTimer = object : CountDownTimer(item.usage.activeCountDown * 1000, 1000) {
                        override fun onTick(l: Long) {
                            item.usage.activeCountDown = l / 1000
                            val seconds = (l / 1000).toInt() % 60
                            val minutes = (l / (1000 * 60) % 60).toInt()
                            val hours = (l / (1000 * 60 * 60) % 24).toInt()
                            btnContinue?.text = String.format(Locale.ENGLISH, "%02d : %02d : %02d", hours, minutes, seconds)
                        }

                        override fun onFinish() {
                            btnContinue?.isEnabled = true
                            btnContinue?.text = item.usage.btnUsage.text
                        }
                    }.start()
                }
            } else {
                btnContinue?.text = item.usage.usageStr
                btnContinue?.isEnabled = true
                if (btnContinue != null) {
                    setButtonTextColor(btnContinue)
                }
            }
        }
    }

    override fun onSwipeResponse(data: CouponSwipeUpdate, qrCodeLink: String, barCodeLink: String) {
        binding?.tpContentCouponDetail?.llBottomButton?.layoutCouponSwipe?.cardSwipe?.couponCode = data.partnerCode
        showBarCodeView(data.note, qrCodeLink, barCodeLink)
    }

    private fun showBarCodeView(note: String?, qrCodeLink: String, barCodeLink: String) {
        binding?.tpContentCouponDetail?.apply {
            barcodeContainer.visibility = View.GONE
            if (qrCodeLink.isNotEmpty()) {
                tpLayoutSwipeCouponCode.btnBarcode.visibility = View.VISIBLE
                tpLayoutSwipeCouponCode.viewCodeSeparator.visibility = View.VISIBLE
                tpLayoutSwipeCouponCode.textSwipeNote.gravity = Gravity.LEFT
            }

            if (barCodeLink.isNotEmpty()) {
                tpLayoutSwipeCouponCode.btnBarcode.visibility = View.VISIBLE
                tpLayoutSwipeCouponCode.textSwipeNote.gravity = Gravity.LEFT
            }

            if (note != null && note.isNotEmpty()) {
                tpLayoutSwipeCouponCode.textSwipeNote.visibility = View.VISIBLE
                tpLayoutSwipeCouponCode.textSwipeNote.text = note
                tpLayoutSwipeCouponCode.textSwipeNote.setTextColor(ContextCompat.getColor(activityContext!!, unifyprinciplesR.color.Unify_NN950_32))
                barcodeContainer.visibility = View.VISIBLE
            } else {
                tpLayoutSwipeCouponCode.textSwipeNote.visibility = View.GONE
            }
        }
    }

    override fun onSwipeError(errorMessage: String) {
        binding?.apply {
            tpContentCouponDetail.llBottomButton.layoutCouponSwipe.cardSwipe.let {
                it.reset()
                view?.let { view -> Toaster.make(view, errorMessage, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR) }
            }
        }
    }

    fun showPinPage(code: String, pinInfo: String) {
        binding?.apply {
            val bundle = Bundle()
            bundle.putString(CommonConstant.EXTRA_PIN_INFO, pinInfo)
            bundle.putString(CommonConstant.EXTRA_COUPON_ID, code)
            val fragment = ValidateMerchantPinFragment.newInstance(bundle)
            fragment.setmValidatePinCallBack(object : ValidateMerchantPinFragment.ValidatePinCallBack {
                override fun onSuccess(couponSwipeUpdate: CouponSwipeUpdate?) {
                    tpContentCouponDetail.llBottomButton.layoutCouponSwipe.cardSwipe.couponCode = couponSwipeUpdate?.partnerCode
                    showBarCodeView(couponSwipeUpdate?.note, "", "")
                    mBottomSheetFragment?.dismiss()
                }
            })
            mBottomSheetFragment = CloseableBottomSheetFragment.newInstance(fragment, true,
                getString(R.string.tp_masukan_pin), CloseableBottomSheetFragment.STATE_FULL,
                object : CloseableBottomSheetFragment.ClosableCallback {
                    override fun onCloseClick(bottomSheetFragment: BottomSheetDialogFragment) {
                        if (TextUtils.isEmpty(tpContentCouponDetail.llBottomButton.layoutCouponSwipe.cardSwipe.couponCode)) {
                            tpContentCouponDetail.llBottomButton.layoutCouponSwipe.cardSwipe.reset()
                        }
                        mBottomSheetFragment = null
                    }

                })
            activity?.supportFragmentManager?.let {
                mBottomSheetFragment?.showNow(it, "")
            }
        }
    }

    private fun showToasterAndRedirect(data: String) {
        view?.let { ToasterHelper.showCouponClaimToast(resources.getString(R.string.tp_coupon_autoapply_msg), it,BOTTOM_HEIGHT_TOASTER) }
        Handler().postDelayed({ RouteManager.route(context, data) }, 1000)
    }

    fun setButtonTextColor(btnContinue : UnifyButton){
        context?.let {
            if (isDarkMode(it) || !isDarkMode(it)){
                btnContinue.setTextColor(ContextCompat.getColor(btnContinue.context, unifyprinciplesR.color.Unify_Static_White))
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_VERIFICATION_PHONE -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        phoneVerificationState = true
                        mPresenter.redeemCoupon(mCode, mCTA)
                    }
                    Activity.RESULT_CANCELED -> {
                        super.onActivityResult(requestCode, resultCode, data)
                    }
                }
            }
        }
    }

    companion object {
        private val REQUEST_CODE_VERIFICATION_PHONE = 301
        private val CONTAINER_LOADER = 0
        private val CONTAINER_DATA = 1
        private val CONTAINER_ERROR = 2
        private val CONTAINER_SWIPE = 1
        private val CONTAINER_COUPON_ERROR = 3
        private const val BOTTOM_HEIGHT_TOASTER = 76

        fun newInstance(extras: Bundle): Fragment {
            val fragment = CouponDetailFragment()
            fragment.arguments = extras
            return fragment
        }
    }

    override fun startPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring = PageLoadTimePerformanceCallback(
                COUPONDETAIL_TOKOPOINT_PLT_PREPARE_METRICS,
                COUPONDETAIL_TOKOPOINT_PLT_NETWORK_METRICS,
                COUPONDETAIL_TOKOPOINT_PLT_RENDER_METRICS,
                0,
                0,
                0,
                0,
                null
        )

        pageLoadTimePerformanceMonitoring?.startMonitoring(COUPONDETAIL_TOKOPOINT_PLT)
        pageLoadTimePerformanceMonitoring?.startPreparePagePerformanceMonitoring()
    }

    override fun stopPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopMonitoring()
        pageLoadTimePerformanceMonitoring = null
    }

    override fun stopPreparePagePerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopPreparePagePerformanceMonitoring()
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

