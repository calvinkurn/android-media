package com.tokopedia.tokopoints.view.coupondetail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer

import com.google.android.material.snackbar.Snackbar

import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AlertDialog

import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.di.TokopointBundleComponent
import com.tokopedia.tokopoints.view.couponlisting.CouponListingStackedActivity
import com.tokopedia.tokopoints.view.customview.SwipeCardView
import com.tokopedia.tokopoints.view.fragment.CloseableBottomSheetFragment
import com.tokopedia.tokopoints.view.validatePin.ValidateMerchantPinFragment
import com.tokopedia.tokopoints.view.model.CatalogsValueEntity
import com.tokopedia.tokopoints.view.model.CouponSwipeDetail
import com.tokopedia.tokopoints.view.model.CouponSwipeUpdate
import com.tokopedia.tokopoints.view.model.CouponValueEntity
import com.tokopedia.tokopoints.view.util.*
import com.tokopedia.unifyprinciples.Typography

import java.util.Locale
import java.util.concurrent.TimeUnit

import javax.inject.Inject

import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

import com.tokopedia.tokopoints.view.util.CommonConstant.COUPON_MIME_TYPE
import com.tokopedia.tokopoints.view.util.CommonConstant.UTF_ENCODING
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.android.synthetic.main.tp_content_coupon_detail.*
import kotlinx.android.synthetic.main.tp_fragment_coupon_detail.*
import kotlinx.android.synthetic.main.tp_layout_coupon_detail_button.*
import kotlinx.android.synthetic.main.tp_layout_swipe_coupon_code.*
import kotlinx.android.synthetic.main.tp_layput_container_swipe.*


class CouponDetailFragment : BaseDaggerFragment(), CouponDetailContract.View, View.OnClickListener {
    private var mSubscriptionCouponTimer: Subscription? = null
    private var mRefreshRepeatCount = 0
    private var mCouponName: String? = null
    var mTimer: CountDownTimer? = null
    var phoneVerificationState: Boolean? = null
    var mCTA: String = ""
    var mCode: String = ""

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    val mPresenter: CouponDetailViewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(CouponDetailViewModel::class.java) }

    private var mRealCode: String? = null
    private var mBottomSheetFragment: CloseableBottomSheetFragment? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initInjector()
        val view = inflater.inflate(R.layout.tp_fragment_coupon_detail, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        initListener()
        mPresenter.isPhonerVerfied()
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

    private fun observeUserInfo() = mPresenter.userInfo.observe(this, Observer {
        it.let {
            phoneVerificationState = it.verifiedMsisdn
        }
    })

    private fun obserserFinish() = mPresenter.finish.observe(this, Observer {
        it?.let {
            activity?.finish()
        }
    })

    private fun onbserveOnRedeemCoupon() = mPresenter.onRedeemCoupon.observe(this, Observer {
        it?.let {
            when (it) {
                is ErrorMessage -> RouteManager.route(context, it.data)
                is Success -> RouteManager.route(context, it.data)
            }
            return@let
        }
    })

    private fun observeRefetchCoupon() = mPresenter.onReFetch.observe(this, Observer {
        it?.let {
            when (it) {
                is ErrorMessage -> onRealCodeReFreshError()
                is Success -> onRealCodeReFresh(it.data)
            }
        }
    })

    private fun observePinPage() = mPresenter.pinPageData.observe(this, Observer {
        it?.let { showPinPage(it.code, it.pinText) }
    })

    private fun observeOnSwipeCoupon() = mPresenter.onCouponSwipe.observe(this, Observer {
        it.let {
            when (it) {
                is ErrorMessage -> onSwipeError(it.data)
                is Success -> onSwipeResponse(it.data, "", "")
            }
        }
    })

    private fun observeSwipeDetail() = mPresenter.swipeDetail.observe(this, Observer {
        it?.let { setSwipeUi(it) }
    })

    private fun observeCouponDetail() = mPresenter.detailLiveData.observe(this, Observer {
        it?.let {
            when (it) {
                is Loading -> showLoader()
                is ErrorMessage -> showError(NetworkDetector.isConnectedToInternet(context))
                is Success -> setCouponToUi(it.data)
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

    override fun getAppContext(): Context? {
        return activity
    }

    override fun showLoader() {
        container?.displayedChild = CONTAINER_LOADER
    }

    override fun showError(networkError: Boolean) {
        container?.displayedChild = CONTAINER_ERROR
        server_error_view?.showErrorUi(networkError)
    }

    override fun hideLoader() {
        container?.displayedChild = CONTAINER_DATA
    }

    override fun populateDetail(data: CouponValueEntity) {
        container?.postDelayed({ setCouponToUi(data) }, CommonConstant.UI_SETTLING_DELAY_MS2.toLong())
    }

    override fun getActivityContext(): Context? {
        return activity
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
        server_error_view?.setErrorButtonClickListener { view -> mPresenter.onErrorButtonClick() }
    }

    override fun openWebView(url: String) {
        RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, url)
    }

    override fun showRedeemCouponDialog(cta: String, code: String, title: String) {
        context?.let {
            val adb = AlertDialog.Builder(context!!)
            adb.setTitle(R.string.tp_label_use_coupon)
            val messageBuilder = StringBuilder()
                    .append(getString(R.string.tp_label_coupon))
                    .append(" ")
                    .append("<strong>")
                    .append(title)
                    .append("</strong>")
                    .append(" ")
                    .append(getString(R.string.tp_mes_coupon_part_2))
            adb.setMessage(MethodChecker.fromHtml(messageBuilder.toString()))
            adb.setPositiveButton(R.string.tp_label_use) { dialogInterface, i ->
                //Call api to validate the coupon
                mPresenter.redeemCoupon(code, cta)

                AnalyticsTrackerUtil.sendEvent(context,
                        AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                        AnalyticsTrackerUtil.CategoryKeys.POPUP_KONFIRMASI_GUNAKAN_KUPON,
                        AnalyticsTrackerUtil.ActionKeys.CLICK_GUNAKAN,
                        title)
            }
            adb.setNegativeButton(R.string.tp_label_later) { dialogInterface, i ->
                AnalyticsTrackerUtil.sendEvent(context,
                        AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                        AnalyticsTrackerUtil.CategoryKeys.POPUP_KONFIRMASI_GUNAKAN_KUPON,
                        AnalyticsTrackerUtil.ActionKeys.CLICK_NANTI_SAJA,
                        title)
            }
            val dialog = adb.create()
            dialog.show()
            decorateDialog(dialog)
        }

    }

    private fun openPhoneVerificationBottomSheet() {
        val view = LayoutInflater.from(context).inflate(R.layout.phoneverification_bottomsheet, null, false)
        val closeableBottomSheetDialog = CloseableBottomSheetDialog.createInstanceRounded(context)
        closeableBottomSheetDialog.setCustomContentView(view, "", false)
        val btnVerifikasi = view.findViewById<UnifyButton>(R.id.btn_verifikasi)
        val btnCancel = view.findViewById<AppCompatImageView>(R.id.cancel_verifikasi)
        btnVerifikasi.setOnClickListener {
            val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.ADD_PHONE)
            startActivityForResult(intent, REQUEST_CODE_VERIFICATION_PHONE)

            AnalyticsTrackerUtil.sendEvent(context,
                    AnalyticsTrackerUtil.EventKeys.KEY_EVENT_PROFILE_VALUE,
                    AnalyticsTrackerUtil.CategoryKeys.KEY_EVENT_CATEGORY_PROFILE_VALUE,
                    AnalyticsTrackerUtil.ActionKeys.KEY_EVENT_ACTION_PROFILE_VALUE,
                    "")
            closeableBottomSheetDialog.cancel()

        }
        btnCancel.setOnClickListener {
            closeableBottomSheetDialog.cancel()
            AnalyticsTrackerUtil.sendEvent(context,
                    AnalyticsTrackerUtil.EventKeys.KEY_EVENT_PROFILE_VALUE,
                    AnalyticsTrackerUtil.CategoryKeys.KEY_EVENT_CATEGORY_PROFILE_VALUE,
                    AnalyticsTrackerUtil.ActionKeys.KEY_EVENT_ACTION_PROFILE_VALUE_BATAL, "")
        }

        closeableBottomSheetDialog.show()
    }

    override fun showRedeemFullError(item: CatalogsValueEntity, title: String, desc: String) {

    }

    override fun onRealCodeReFresh(realCode: String?) {
        if (mSubscriptionCouponTimer == null) {
            return
        }
        try {
            view?.apply {
                this@CouponDetailFragment.mRealCode = realCode
                if (TextUtils.isEmpty(realCode)) {
                    btn_continue.setText(R.string.tp_label_use)
                    btn_continue.isEnabled = true
                    progress_refetch_code.visibility = View.GONE
                    btn_continue.setTextColor(ContextCompat.getColor(activityContext!!, com.tokopedia.design.R.color.white))
                    mSubscriptionCouponTimer?.unsubscribe()
                    return
                }

                if (mRefreshRepeatCount >= CommonConstant.MAX_COUPON_RE_FETCH_COUNT) {
                    btn_continue.setText(R.string.tp_label_refresh_repeat)
                    btn_continue.isEnabled = true
                    progress_refetch_code.visibility = View.GONE
                    btn_continue.setTextColor(ContextCompat.getColor(activityContext!!, com.tokopedia.design.R.color.white))
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
            val btnAction2 = view!!.findViewById<TextView>(com.tokopedia.session.R.id.btn_continue)
            val progressBar = view!!.findViewById<ProgressBar>(R.id.progress_refetch_code)
            btnAction2.setText(R.string.tp_label_refresh_repeat)
            btnAction2.isEnabled = true
            progressBar.visibility = View.GONE
            btnAction2.setTextColor(ContextCompat.getColor(activityContext!!, com.tokopedia.design.R.color.white))
            mSubscriptionCouponTimer!!.unsubscribe()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun decorateDialog(dialog: AlertDialog) {
        if (dialog.getButton(AlertDialog.BUTTON_POSITIVE) != null) {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(activityContext!!,
                    com.tokopedia.design.R.color.tkpd_main_green))
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).isAllCaps = false
        }

        if (dialog.getButton(AlertDialog.BUTTON_NEGATIVE) != null) {
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).isAllCaps = false
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(activityContext!!,
                    com.tokopedia.design.R.color.grey_warm))
        }
    }

    private fun setCouponToUi(data: CouponValueEntity) {
        if (view == null || data.isEmpty) {
            return
        }
        hideLoader()
        mCouponName = data.title
        mCTA = data.cta
        mCode = data.realCode as String
        val description = view!!.findViewById<TextView>(R.id.tv_title)
        val label = view!!.findViewById<TextView>(R.id.text_time_label)
        val value = view!!.findViewById<Typography>(R.id.text_time_value)
        val btnAction2 = view!!.findViewById<TextView>(R.id.btn_continue)
        val imgBanner = view!!.findViewById<ImageView>(R.id.img_banner_coupon)
        val imgLabel = view!!.findViewById<ImageView>(R.id.img_time)
        val textMinExchangeValue = view!!.findViewById<TextView>(R.id.tv_min_txn_value)
        val textMinExchangeLabel = view!!.findViewById<TextView>(R.id.tv_min_txn_label)
        val imgMinExchange = view!!.findViewById<ImageView>(R.id.iv_rp)
        val progressBar = view!!.findViewById<ProgressBar>(R.id.progress_refetch_code)

        description.text = data.title
        ImageHandler.loadImageFitCenter(imgBanner.context, imgBanner, data.imageUrlMobile)

        if (data.usage != null) {
            label.visibility = View.VISIBLE
            label.text = data.usage.text
            value.visibility = View.VISIBLE
            imgLabel.visibility = View.VISIBLE
            value.text = data.usage.usageStr.trim { it <= ' ' }

            if (data.usage.btnUsage != null) {
                if (data.usage.btnUsage.type.equals("invisible", ignoreCase = true)) {
                    btnAction2.visibility = View.GONE
                } else {
                    btnAction2.visibility = View.VISIBLE
                }
            }


        }

        if (TextUtils.isEmpty(data.minimumUsageLabel)) {
            textMinExchangeLabel.visibility = View.GONE
            imgMinExchange.visibility = View.GONE
        } else {
            imgMinExchange.visibility = View.VISIBLE
            textMinExchangeLabel.visibility = View.VISIBLE
            textMinExchangeLabel.text = data.minimumUsageLabel
        }

        if (TextUtils.isEmpty(data.minimumUsage)) {
            textMinExchangeValue.visibility = View.GONE
        } else {
            textMinExchangeValue.visibility = View.VISIBLE
            textMinExchangeValue.text = data.minimumUsage
        }

        if (data.usage != null && (data.usage.activeCountDown > 0 || data.usage.expiredCountDown <= 0)) {
            imgLabel.setColorFilter(ContextCompat.getColor(imgLabel.context, R.color.tp_coupon_disable), android.graphics.PorterDuff.Mode.SRC_IN)
            imgMinExchange.setColorFilter(ContextCompat.getColor(imgMinExchange.context, R.color.tp_coupon_disable), android.graphics.PorterDuff.Mode.SRC_IN)
        } else {
            imgLabel.setColorFilter(ContextCompat.getColor(imgLabel.context, com.tokopedia.design.R.color.medium_green), android.graphics.PorterDuff.Mode.SRC_IN)
            imgMinExchange.setColorFilter(ContextCompat.getColor(imgMinExchange.context, com.tokopedia.design.R.color.medium_green), android.graphics.PorterDuff.Mode.SRC_IN)
        }

        this.mRealCode = data.realCode
        btnAction2.setOnClickListener { v ->

            val variant = RemoteConfigInstance.getInstance().abTestPlatform.getString(AB_TEST_PHONE_VERIFICATION_KEY, AB_TESTING_CTA_VARIANT_A)
            if (phoneVerificationState == false && variant == AB_TESTING_CTA_VARIANT_A) {
                openPhoneVerificationBottomSheet()
            } else {
                val code = mRealCode as String
                if (!TextUtils.isEmpty(code)) {
                    showRedeemCouponDialog(data.cta, code, data.title)
                    AnalyticsTrackerUtil.sendEvent(context,
                            AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                            AnalyticsTrackerUtil.CategoryKeys.KUPON_MILIK_SAYA_DETAIL,
                            AnalyticsTrackerUtil.ActionKeys.CLICK_GUNAKAN,
                            mCouponName)
                } else {
                    if (arguments != null && arguments!!.getString(CommonConstant.EXTRA_COUPON_CODE) != null) {
                        btnAction2.isEnabled = false
                        btnAction2.setTextColor(resources.getColor(com.tokopedia.abstraction.R.color.black_12))
                        progressBar.visibility = View.VISIBLE
                        btnAction2.text = ""
                        mPresenter.reFetchRealCode()
                    }
                }
            }
        }
        setupInfoPager(data.howToUse, data.tnc)

        if (data.realCode != null && !data.realCode.isEmpty()) {
            btnAction2.setText(R.string.tp_label_use)
            btnAction2.isEnabled = true
            btnAction2.setTextColor(resources.getColor(com.tokopedia.design.R.color.white))
            progressBar.visibility = View.GONE
        } else {
            //check for real_code and start rxjava-timer
            btnAction2.isEnabled = false
            btnAction2.setTextColor(resources.getColor(com.tokopedia.abstraction.R.color.black_12))
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


    private fun setSwipeUi(swipeDetail: CouponSwipeDetail) = view?.apply {
        ll_container_button.displayedChild = CONTAINER_SWIPE
        card_swipe?.apply {
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
            if (swipeDetail.partnerCode != null && !swipeDetail.partnerCode.isEmpty()) {
                couponCode = swipeDetail.partnerCode
            }
            showBarCodeView(swipeDetail.note, "", "")

        }
    }


    private fun setupInfoPager(info: String, tnc: String) {
        view?.apply {
            tnc_content.loadData(getLessDisplayData(tnc, tnc_see_more), COUPON_MIME_TYPE, UTF_ENCODING)
            how_to_use_content.loadData(getLessDisplayData(info, how_to_use_see_more), COUPON_MIME_TYPE, UTF_ENCODING)

            tnc_see_more.setOnClickListener { v -> loadWebViewInBottomsheet(tnc, getString(R.string.tnc_coupon_catalog)) }
            how_to_use_see_more.setOnClickListener { v -> loadWebViewInBottomsheet(info, getString(R.string.how_to_use_coupon_catalog)) }
            ll_bottom_button.visibility = View.VISIBLE
        }
    }

    private fun loadWebViewInBottomsheet(data: String?, title: String?) {
        val bottomSheet = CloseableBottomSheetDialog.createInstanceRounded(activity)
        val view = layoutInflater.inflate(R.layout.catalog_bottomsheet, null, true)
        val webView = view.findViewById<WebView>(R.id.catalog_webview)
        val closeBtn = view.findViewById<ImageView>(R.id.close_button)
        val titleView = view.findViewById<Typography>(R.id.title_closeable)

        webView.loadData(data, COUPON_MIME_TYPE, UTF_ENCODING)
        closeBtn.setOnClickListener { v -> bottomSheet.dismiss() }
        titleView.text = title
        bottomSheet.setCustomContentView(view, title, false)
        bottomSheet.show()
    }

    private fun addCountDownTimer(item: CouponValueEntity, label: Typography, btnContinue: TextView) {
        if (mTimer != null || view == null) {
            mTimer?.cancel()
        }

        if (item.usage.activeCountDown < 1) {
            if (item.usage.expiredCountDown > 0 && item.usage.expiredCountDown <= CommonConstant.COUPON_SHOW_COUNTDOWN_MAX_LIMIT_S) {
                val progressBar = view!!.findViewById<ProgressBar>(R.id.progress_timer)
                progressBar.visibility = View.VISIBLE
                progressBar.max = CommonConstant.COUPON_SHOW_COUNTDOWN_MAX_LIMIT_S.toInt()
                label.setTextColor(context!!.resources.getColor(com.tokopedia.design.R.color.r_400))
                label.setType(Typography.SMALL)
                label.invalidate()
                mTimer = object : CountDownTimer(item.usage.expiredCountDown * 1000, 1000) {
                    override fun onTick(l: Long) {
                        label.setPadding(resources.getDimensionPixelSize(R.dimen.tp_padding_regular),
                                resources.getDimensionPixelSize(R.dimen.tp_padding_xsmall),
                                resources.getDimensionPixelSize(R.dimen.tp_padding_regular),
                                resources.getDimensionPixelSize(R.dimen.tp_padding_xsmall))
                        item.usage.expiredCountDown = l / 1000
                        val seconds = (l / 1000).toInt() % 60
                        val minutes = (l / (1000 * 60) % 60).toInt()
                        val hours = (l / (1000 * 60 * 60) % 24).toInt()
                        label.text = String.format(Locale.ENGLISH, "%02d : %02d : %02d", hours, minutes, seconds)
                        progressBar.progress = l.toInt() / 1000
                    }

                    override fun onFinish() {
                        progressBar.visibility = View.GONE
                        label.text = "00 : 00 : 00"
                        btnContinue.text = "Expired"
                        btnContinue.isEnabled = false
                        btnContinue.setTextColor(ContextCompat.getColor(btnContinue.context, com.tokopedia.abstraction.R.color.black_12))
                    }
                }.start()
            } else {
                btnContinue.text = item.usage.btnUsage.text
                btnContinue.isEnabled = true
                btnContinue.setTextColor(ContextCompat.getColor(btnContinue.context, com.tokopedia.design.R.color.white))
            }
        } else {
            if (item.usage.activeCountDown > 0) {
                btnContinue.isEnabled = false
                btnContinue.setTextColor(ContextCompat.getColor(btnContinue.context, com.tokopedia.abstraction.R.color.black_12))
                if (item.usage.activeCountDown <= CommonConstant.COUPON_SHOW_COUNTDOWN_MAX_LIMIT_S) {
                    mTimer = object : CountDownTimer(item.usage.activeCountDown * 1000, 1000) {
                        override fun onTick(l: Long) {
                            item.usage.activeCountDown = l / 1000
                            val seconds = (l / 1000).toInt() % 60
                            val minutes = (l / (1000 * 60) % 60).toInt()
                            val hours = (l / (1000 * 60 * 60) % 24).toInt()
                            btnContinue.text = String.format(Locale.ENGLISH, "%02d : %02d : %02d", hours, minutes, seconds)
                        }

                        override fun onFinish() {
                            btnContinue.isEnabled = true
                            btnContinue.text = item.usage.btnUsage.text
                        }
                    }.start()
                }
            } else {
                btnContinue.text = item.usage.usageStr
                btnContinue.isEnabled = true
                btnContinue.setTextColor(ContextCompat.getColor(btnContinue.context, com.tokopedia.design.R.color.white))
            }
        }
    }

    override fun onSwipeResponse(data: CouponSwipeUpdate, qrCodeLink: String, barCodeLink: String) {
        card_swipe?.couponCode = data.partnerCode
        showBarCodeView(data.note, qrCodeLink, barCodeLink)
    }

    private fun showBarCodeView(note: String?, qrCodeLink: String, barCodeLink: String) {
        barcode_container?.apply {
            visibility = View.GONE
            if (!qrCodeLink.isEmpty()) {
                btn_qrcode.visibility = View.VISIBLE
                view_code_separator.visibility = View.VISIBLE
                text_swipe_note.gravity = Gravity.LEFT
            }

            if (!barCodeLink.isEmpty()) {
                btn_barcode.visibility = View.VISIBLE
                text_swipe_note.gravity = Gravity.LEFT
            }

            if (note != null && !note.isEmpty()) {
                text_swipe_note.visibility = View.VISIBLE
                text_swipe_note.text = note
                text_swipe_note.setTextColor(ContextCompat.getColor(activityContext!!, com.tokopedia.design.R.color.black_38))
                visibility = View.VISIBLE
            } else {
                text_swipe_note.visibility = View.GONE
            }
        }

    }

    override fun onSwipeError(errorMessage: String) {
        card_swipe?.let {
            it.reset()
            SnackbarManager.make(it, errorMessage, Snackbar.LENGTH_SHORT).show()

        }
    }

    fun showPinPage(code: String, pinInfo: String) {
        val bundle = Bundle()
        bundle.putString(CommonConstant.EXTRA_PIN_INFO, pinInfo)
        bundle.putString(CommonConstant.EXTRA_COUPON_ID, code)
        val fragment = ValidateMerchantPinFragment.newInstance(bundle)
        fragment.setmValidatePinCallBack { couponSwipeUpdate ->
            mBottomSheetFragment?.dismiss()
            card_swipe?.couponCode = couponSwipeUpdate.partnerCode
            showBarCodeView(couponSwipeUpdate.note, "", "")
        }
        mBottomSheetFragment = CloseableBottomSheetFragment.newInstance(fragment, true,
                getString(R.string.tp_masukan_pin), CloseableBottomSheetFragment.STATE_FULL,
                object : CloseableBottomSheetFragment.ClosableCallback {
                    override fun onCloseClick(bottomSheetFragment: BottomSheetDialogFragment) {
                        if (TextUtils.isEmpty(card_swipe?.couponCode)) {
                            card_swipe?.reset()
                        }
                        mBottomSheetFragment = null
                    }

                })
        activity?.supportFragmentManager?.let {
            mBottomSheetFragment?.showNow(it, "")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_VERIFICATION_PHONE -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        phoneVerificationState=true
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
            val AB_TESTING_CTA_VARIANT_A = "CTA Phone Verify 2"
            val AB_TEST_PHONE_VERIFICATION_KEY = "CTA Phone Verify 2"
            private val REQUEST_CODE_VERIFICATION_PHONE = 301
            private val CONTAINER_LOADER = 0
            private val CONTAINER_DATA = 1
            private val CONTAINER_ERROR = 2
            private val CONTAINER_SWIPE = 1


            fun newInstance(extras: Bundle): Fragment {
                val fragment = CouponDetailFragment()
                fragment.arguments = extras
                return fragment
            }
        }

    }

