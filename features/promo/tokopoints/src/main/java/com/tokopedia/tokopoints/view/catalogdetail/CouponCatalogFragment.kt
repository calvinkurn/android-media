package com.tokopedia.tokopoints.view.catalogdetail

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
import android.webkit.WebView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat.invalidateOptionsMenu
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
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.tokopoints.R
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
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession
import com.tokopedia.webview.TkpdWebView
import kotlinx.android.synthetic.main.tp_coupon_notfound_error.*
import kotlinx.android.synthetic.main.tp_fragment_coupon_detail.*
import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CouponCatalogFragment : BaseDaggerFragment(), CouponCatalogContract.View, View.OnClickListener, TokopointPerformanceMonitoringListener {
    private var mContainerMain: ViewFlipper? = null
    private var serverErrorView: ServerErrorView? = null
    private val mSubscriptionCouponTimer: Subscription? = null
    private var mSubscriptionCatalogTimer: Subscription? = null
    private val mRefreshRepeatCount = 0
    private var mCouponName: String? = null
    var mTimer: CountDownTimer? = null
    var mUserSession: UserSession? = null
    private var catalogsValueEntity: CatalogsValueEntity? = null
    private var pointValueText: TextView? = null
    var pointValue: Typography? = null
    var textUserPoint: Typography? = null
    var code: String? = null
    var userPoints: String? = null

    @Inject
    lateinit var factory: ViewModelFactory

    private val mViewModel: CouponCatalogViewModel by lazy { ViewModelProviders.of(this, factory)[CouponCatalogViewModel::class.java] }
    private var pageLoadTimePerformanceMonitoring: PageLoadTimePerformanceInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        startPerformanceMonitoring()
        mUserSession = UserSession(appContext)
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initInjector()
        val view = inflater.inflate(R.layout.tp_fragment_coupon_catalog, container, false)
        initViews(view)
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_coupon_catalog, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        val id = item.itemId
        if (id == R.id.action_menu_share) {
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            sharingIntent.putExtra(Intent.EXTRA_TEXT, CommonConstant.WebLink.DETAIL + arguments!!.getString(CommonConstant.EXTRA_CATALOG_CODE))
            startActivity(Intent.createChooser(sharingIntent, null))
            return true
        }
        return true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        if (arguments == null) {
            if (activity != null) {
                activity!!.finish()
            }
            return
        }
        code = arguments!!.getString(CommonConstant.EXTRA_CATALOG_CODE)
        stopPreparePagePerformanceMonitoring()
        startNetworkRequestPerformanceMonitoring()
        mViewModel.getCatalogDetail(code ?: "")
        initObserver()
    }

    private fun initObserver() {
        addCatalogDetailObserver()
        addSendGiftDialogObserver()
        addValidationDialogObserver()
        addLatestStatusObserver()
        addStartSaveCouponObserver()
        addRedeemCouponObserver()
    }

    private fun addRedeemCouponObserver() = mViewModel.onRedeemCouponLiveData.observe(this, androidx.lifecycle.Observer {
        it?.let { RouteManager.route(context, it) }
    })

    private fun addStartSaveCouponObserver() = mViewModel.startSaveCouponLiveData.observe(this, androidx.lifecycle.Observer {
        when (it) {
            is Success -> redeemCoupon(it.data.cta, it.data.code, it.data.title, it.data.description)
            is ValidationError<*, *> -> {
                if (it.data is ValidateMessageDialog) {
                    checkValidation(it.data.item, it.data.title, it.data.desc, it.data.messageCode)
                }
            }
        }
    })

    private fun addLatestStatusObserver() = mViewModel.latestStatusLiveData.observe(this, androidx.lifecycle.Observer {
        it?.let { refreshCatalog(it) }
    })

    private fun addValidationDialogObserver() = mViewModel.startValidateCouponLiveData.observe(this, androidx.lifecycle.Observer {
        it?.let {
            checkValidation(it.item, it.title, it.desc, it.messageCode)
        }
    })

    private fun addSendGiftDialogObserver() = mViewModel.sendGiftPageLiveData.observe(this, androidx.lifecycle.Observer {
        when (it) {
            is Success -> gotoSendGiftPage(it.data.id, it.data.title, it.data.pointStr, it.data.banner)
            is ValidationError<*, *> -> {
                if (it.data is PreValidateError)
                    onPreValidateError(it.data.title, it.data.message)
            }
        }
    })

    private fun addCatalogDetailObserver() = mViewModel.catalogDetailLiveData.observe(this, androidx.lifecycle.Observer {
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
        }
    })

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

    override fun getAppContext(): Context {
        return context!!
    }

    override fun showLoader() {
        mContainerMain!!.displayedChild = CONTAINER_LOADER
    }

    override fun showError(hasInternet: Boolean) {
        mContainerMain!!.displayedChild = CONTAINER_ERROR
        serverErrorView!!.showErrorUi(hasInternet)
    }

    private fun showCouponError() {
        container?.displayedChild = CONTAINER_COUPON_ERROR
        btnError.setOnClickListener {
            RouteManager.route(context, ApplinkConst.TOKOPEDIA_REWARD)
        }
        invalidateOptionsMenu(activity)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val registrar = menu.findItem(R.id.action_menu_share)
        registrar.isVisible = false
        return
    }

    override fun hideLoader() {
        mContainerMain!!.displayedChild = CONTAINER_DATA
    }

    override fun populateDetail(data: CatalogsValueEntity) {
        catalogsValueEntity = data
        setCatalogToUi(data)
    }

    override fun getActivityContext(): Context {
        return activity!!
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

    private fun initViews(view: View) {
        mContainerMain = view.findViewById(R.id.container)
        serverErrorView = view.findViewById(R.id.server_error_view)
    }

    private fun initListener() {
        if (view == null) {
            return
        }
        serverErrorView!!.setErrorButtonClickListener { view: View? ->
            mViewModel.getCatalogDetail(code ?: "")
        }
    }

    override fun openWebView(url: String) {
        RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, url))
    }

    override fun redeemCoupon(cta: String, code: String, title: String, description: String) {
        //Call api to validate the coupon

        val btn1: UnifyButton
        val btn2: UnifyButton
        val titleDialog: TextView
        val descDialog: Typography
        val adb = AlertDialog.Builder(context!!)
        val altTitle = "Kupon berhasil diklaim"
        val altDescription = "Selamat! Kamu berhasil klaim kupon ini. Yuk pakai kuponnya supaya belanjamu lebih hemat."
        val view = LayoutInflater.from(context).inflate(R.layout.tp_upcoming_feature_dialog, null)
        adb.setView(view)
        btn1 = view.findViewById(R.id.btn_route)
        btn2 = view.findViewById(R.id.btn_route_kupon)
        titleDialog = view.findViewById(R.id.tv_dialogTitle)
        descDialog = view.findViewById(R.id.tv_dialogDesc)
        titleDialog.text = altTitle
        descDialog.text = altDescription
        val alertDialog = adb.create()
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        alertDialog.setCancelable(true)
        alertDialog.setCanceledOnTouchOutside(true)
        btn1.setOnClickListener { v: View? ->
            mViewModel.redeemCoupon(code, cta)
            alertDialog.dismiss()
        }
        btn2.setOnClickListener {
            startActivity(getCallingIntent(activityContext))
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    private fun showErrorDialog(item: CatalogsValueEntity, title: String, message: String, resCode: Int){
        val adb = AlertDialog.Builder(activityContext)
        val labelPositive: String
        var labelNegative: String? = null
        when (resCode) {
            CommonConstant.CouponRedemptionCode.LOW_POINT -> labelPositive = getString(R.string.tp_label_ok)
            CommonConstant.CouponRedemptionCode.PROFILE_INCOMPLETE -> {
                labelPositive = getString(R.string.tp_label_complete_profile)
                labelNegative = getString(R.string.tp_label_later)
            }
            CommonConstant.CouponRedemptionCode.QUOTA_LIMIT_REACHED -> labelPositive = getString(R.string.tp_label_ok)
            else -> labelPositive = getString(R.string.tp_label_ok)
        }
        if (title == null || title.isEmpty()) {
            adb.setTitle(R.string.tp_label_exchange_failed)
        } else {
            adb.setTitle(title)
        }
        adb.setMessage(MethodChecker.fromHtml(message))
        if (labelNegative != null && !labelNegative.isEmpty()) {
            adb.setNegativeButton(labelNegative) { dialogInterface: DialogInterface?, i: Int ->
                when (resCode) {
                    CommonConstant.CouponRedemptionCode.PROFILE_INCOMPLETE -> AnalyticsTrackerUtil.sendEvent(context,
                            AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                            AnalyticsTrackerUtil.CategoryKeys.POPUP_VERIFIED,
                            AnalyticsTrackerUtil.ActionKeys.CLICK_NANTI_SAJA,
                            "")
                    CommonConstant.CouponRedemptionCode.SUCCESS -> AnalyticsTrackerUtil.sendEvent(context,
                            AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                            AnalyticsTrackerUtil.CategoryKeys.POPUP_KONFIRMASI,
                            AnalyticsTrackerUtil.ActionKeys.CLICK_BATAL,
                            title)
                    else -> {
                    }
                }
            }
        }
        adb.setPositiveButton(labelPositive) { dialogInterface: DialogInterface, i: Int ->
            when (resCode) {
                CommonConstant.CouponRedemptionCode.LOW_POINT -> {
                    dialogInterface.cancel()
                    AnalyticsTrackerUtil.sendEvent(context,
                            AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                            AnalyticsTrackerUtil.CategoryKeys.POPUP_PENUKARAN_POINT_TIDAK,
                            AnalyticsTrackerUtil.ActionKeys.CLICK_BELANJA,
                            "")
                }
                CommonConstant.CouponRedemptionCode.QUOTA_LIMIT_REACHED -> {
                    dialogInterface.cancel()
                    AnalyticsTrackerUtil.sendEvent(context,
                            AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                            AnalyticsTrackerUtil.CategoryKeys.POPUP_KUOTA_HABIS,
                            AnalyticsTrackerUtil.ActionKeys.CLICK_OK,
                            "")
                }
                CommonConstant.CouponRedemptionCode.PROFILE_INCOMPLETE -> {
                    val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.PROFILE_COMPLETION)
                    startActivity(intent)
                    AnalyticsTrackerUtil.sendEvent(context,
                            AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                            AnalyticsTrackerUtil.CategoryKeys.POPUP_VERIFIED,
                            AnalyticsTrackerUtil.ActionKeys.CLICK_INCOMPLETE_PROFILE,
                            "")
                }
                else -> dialogInterface.cancel()
            }
        }
        val dialog = adb.create()
        dialog.show()
        decorateDialog(dialog)
    }

    override fun checkValidation(item: CatalogsValueEntity, title: String, message: String, resCode: Int) {
       if (resCode == CommonConstant.CouponRedemptionCode.SUCCESS){
           mViewModel.startSaveCoupon(item)
           AnalyticsTrackerUtil.sendEvent(context,
                   AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                   AnalyticsTrackerUtil.CategoryKeys.POPUP_KONFIRMASI,
                   AnalyticsTrackerUtil.ActionKeys.CLICK_TUKAR,
                   title)
       } else {
           showErrorDialog(item,title,message,resCode)
       }
    }

    override fun onRealCodeReFresh(realCode: String) {
        if (view == null || mSubscriptionCouponTimer == null) {
            return
        }
        val btnAction2: Typography = view!!.findViewById(R.id.button_action_2)
        if (realCode != null && !realCode.isEmpty()) {
            btnAction2.setText(R.string.tp_label_use)
            btnAction2.isEnabled = true
            mSubscriptionCouponTimer.unsubscribe()
            return
        }
        if (mRefreshRepeatCount >= CommonConstant.MAX_COUPON_RE_FETCH_COUNT) {
            btnAction2.setText(R.string.tp_label_refresh_repeat)
            btnAction2.isEnabled = true
            btnAction2.setTextColor(ContextCompat.getColor(activityContext, com.tokopedia.design.R.color.white))
            mSubscriptionCouponTimer.unsubscribe()
        }
    }

    override fun onRealCodeReFreshError() {
        if (view == null || mSubscriptionCouponTimer == null) {
            return
        }
        val btnAction2: Typography = view!!.findViewById(R.id.button_action_2)
        btnAction2.setText(R.string.tp_label_refresh_repeat)
        btnAction2.isEnabled = true
        mSubscriptionCouponTimer.unsubscribe()
    }

    override fun refreshCatalog(data: CatalogStatusItem) {
        if (view == null) {
            return
        }
        val quota: Typography = view!!.findViewById(R.id.text_quota_count)
        val btnAction2: Typography = view!!.findViewById(R.id.button_action_2)
        val imgBanner = view!!.findViewById<ImageView>(R.id.img_banner)
        btnAction2.isEnabled = !data.isDisabledButton
        if (data.isDisabledButton) {
            btnAction2.setTextColor(ContextCompat.getColor(btnAction2.context, R.color.disabled_color))
        } else {
            btnAction2.setTextColor(ContextCompat.getColor(btnAction2.context, com.tokopedia.design.R.color.white))
        }
        //Quota text handling
        if (data.upperTextDesc == null || data.upperTextDesc.isEmpty()) {
            quota.visibility = View.GONE
        } else {
            quota.visibility = View.VISIBLE
            val upperText = StringBuilder()
            for (i in data.upperTextDesc.indices) {
                if (i == 1) { //exclusive case for handling font color of second index.
                    upperText.append("<font color='#ff5722'>" + data.upperTextDesc[i] + "</font>")
                } else {
                    upperText.append(data.upperTextDesc[i]).append(" ")
                }
            }
            quota.text = MethodChecker.fromHtml(upperText.toString())
        }
        //disabling the coupons if not eligible for current membership
        if (data.isDisabled) {
            ImageUtil.dimImage(imgBanner)
        } else {
            ImageUtil.unDimImage(imgBanner)
        }
        pointValue?.setTextColor(ContextCompat.getColor(pointValue!!.getContext(), com.tokopedia.design.R.color.unify_Y500))
    }

    override fun onPreValidateError(title: String, message: String) {
        val adb = AlertDialog.Builder(activityContext)
        adb.setTitle(title)
        adb.setMessage(message)
        adb.setPositiveButton(R.string.tp_label_ok
        ) { dialogInterface: DialogInterface?, i: Int -> }
        val dialog = adb.create()
        dialog.show()
        decorateDialog(dialog)
    }

    private fun decorateDialog(dialog: AlertDialog) {
        if (dialog.getButton(AlertDialog.BUTTON_POSITIVE) != null) {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(activityContext,
                    com.tokopedia.design.R.color.tkpd_main_green))
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).isAllCaps = false
        }
        if (dialog.getButton(AlertDialog.BUTTON_NEGATIVE) != null) {
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).isAllCaps = false
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(activityContext,
                    com.tokopedia.design.R.color.grey_warm))
        }
    }

    //setting catalog values to ui
    private fun setCatalogToUi(data: CatalogsValueEntity) {
        if (view == null) {
            return
        }
        if (data.id == 0) {
            showCouponError()
            return
        }
        mCouponName = data.title
        val quota: Typography = view!!.findViewById(R.id.text_quota_count)
        val description: Typography = view!!.findViewById(R.id.text_description)
        val disabledError: Typography = view!!.findViewById(R.id.text_disabled_error)
        val giftSectionMainLayout: ConstraintLayout = view!!.findViewById(R.id.gift_section_main_layout)
        val giftImage: Typography = view!!.findViewById(R.id.gift_image)
        val giftButton: Typography = view!!.findViewById(R.id.gift_btn)
        val bottomSeparator = view!!.findViewById<View>(R.id.tp_bottom_separator)
        giftImage.setCompoundDrawablesWithIntrinsicBounds(MethodChecker.getDrawable(activity, R.drawable.ic_catalog_gift_btn), null, null, null)
        val btnAction2: Typography = view!!.findViewById(R.id.button_action_2)
        val imgBanner = view!!.findViewById<ImageView>(R.id.img_banner)
        val labelPoint: Typography = view!!.findViewById(R.id.text_point_label)
        val textDiscount: Typography = view!!.findViewById(R.id.text_point_discount)
        btnAction2.visibility = View.VISIBLE
        btnAction2.isEnabled = !data.isDisabledButton
        description.text = data.title
        btnAction2.text = data.buttonStr
        btnAction2.setBackgroundResource(R.drawable.bg_button_buy_orange_tokopoints)
        ImageHandler.loadImageFitCenter(imgBanner.context, imgBanner, data.imageUrlMobile)
        val tvHowToUse: TkpdWebView = view!!.findViewById(R.id.how_to_use_content)
        val tvTnc: TkpdWebView = view!!.findViewById(R.id.tnc_content)
        val tncSeeMore: Typography = view!!.findViewById(R.id.tnc_see_more)
        val howToUseSeeMore: Typography = view!!.findViewById(R.id.how_to_use_see_more)
        tvTnc.loadData(getLessDisplayData(data.tnc, tncSeeMore), CommonConstant.COUPON_MIME_TYPE, CommonConstant.UTF_ENCODING)
        tvHowToUse.loadData(getLessDisplayData(data.howToUse, howToUseSeeMore), CommonConstant.COUPON_MIME_TYPE, CommonConstant.UTF_ENCODING)
        tncSeeMore.setOnClickListener { v: View? -> loadWebViewInBottomsheet(data.tnc, getString(R.string.tnc_coupon_catalog)) }
        howToUseSeeMore.setOnClickListener { v: View? -> loadWebViewInBottomsheet(data.howToUse, getString(R.string.how_to_use_coupon_catalog)) }
        val pointValue: Typography = view!!.findViewById(R.id.text_point_value_coupon)
        pointValue.text = "Gratis"
        //Quota text handling
        if (data.upperTextDesc == null || data.upperTextDesc.isEmpty()) {
            quota.visibility = View.GONE
        } else {
            quota.visibility = View.VISIBLE
            val upperText = StringBuilder()
            for (i in data.upperTextDesc.indices) {
                if (i == 1) { //exclusive case for handling font color of second index.
                    upperText.append("<font color='#ff5722'>" + data.upperTextDesc[i] + "</font>")
                } else {
                    upperText.append(data.upperTextDesc[i]).append(" ")
                }
            }
            quota.text = MethodChecker.fromHtml(upperText.toString())
        }
        //Quota text handling
        if (data.disableErrorMessage == null || data.disableErrorMessage.isEmpty()) {
            disabledError.visibility = View.GONE
        } else {
            disabledError.visibility = View.VISIBLE
            disabledError.text = data.disableErrorMessage
        }
        //disabling the coupons if not eligible for current membership
        if (data.isDisabled) {
            ImageUtil.dimImage(imgBanner)
            pointValue.setTextColor(ContextCompat.getColor(pointValue.context, com.tokopedia.design.R.color.black_54))
        } else {
            ImageUtil.unDimImage(imgBanner)
            pointValue.setTextColor(ContextCompat.getColor(pointValue.context, com.tokopedia.design.R.color.black_54))
        }
        if (data.isDisabledButton) {
            giftSectionMainLayout.visibility = View.GONE
            btnAction2.setTextColor(ContextCompat.getColor(btnAction2.context, com.tokopedia.abstraction.R.color.black_12))
        } else {
            giftSectionMainLayout.visibility = View.VISIBLE
            btnAction2.setTextColor(ContextCompat.getColor(btnAction2.context, com.tokopedia.design.R.color.white))
        }
        if (data.pointsSlash <= 0) {
            labelPoint.visibility = View.GONE
        } else {
            labelPoint.visibility = View.VISIBLE
            labelPoint.text = data.pointsSlashStr
            labelPoint.paintFlags = labelPoint.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
        if (data.discountPercentage <= 0) {
            textDiscount.visibility = View.GONE
        } else {
            textDiscount.visibility = View.VISIBLE
            textDiscount.text = data.discountPercentageStr
        }
        if (data.isGift == 1) {
            if (data.isDisabledButton) {
                giftSectionMainLayout.visibility = View.GONE
                bottomSeparator.visibility = View.GONE
            } else {
                giftSectionMainLayout.visibility = View.VISIBLE
                bottomSeparator.visibility = View.VISIBLE
                giftButton.setText(R.string.tp_label_send_now)
                giftButton.setOnClickListener { view: View? -> mViewModel.startSendGift(data.id, data.title, data.pointsStr, data.imageUrlMobile) }
            }
        } else {
            giftSectionMainLayout.visibility = View.GONE
            bottomSeparator.visibility = View.GONE
        }
        //hide gift section when user is in public page
        if (!mUserSession!!.isLoggedIn) {
            giftSectionMainLayout.visibility = View.GONE
            bottomSeparator.visibility = View.GONE
        }
        btnAction2.setOnClickListener { v: View? ->
            //call validate api the show dialog
            if (mUserSession!!.isLoggedIn) {
                mViewModel.startValidateCoupon(data)
            } else {
                startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN), REQUEST_CODE_LOGIN)
            }
            AnalyticsTrackerUtil.sendEvent(context,
                    AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                    AnalyticsTrackerUtil.CategoryKeys.PENUKARAN_POINT_DETAIL,
                    AnalyticsTrackerUtil.ActionKeys.CLICK_TUKAR,
                    mCouponName)
        }
        pointValueText = view!!.findViewById(R.id.text_point_value_label)
        if (!mUserSession!!.isLoggedIn) {
            pointValueText!!.setText("Masuk untuk tukar Points")
        }
        //start catalog status timer
        mSubscriptionCatalogTimer = Observable.interval(CommonConstant.DEFAULT_AUTO_REFRESH_S.toLong(), CommonConstant.DEFAULT_AUTO_REFRESH_S.toLong(), TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Long?>() {
                    override fun onCompleted() {}
                    override fun onError(e: Throwable) {}
                    override fun onNext(aLong: Long?) {
                        mViewModel.fetchLatestStatus(Arrays.asList(data.id))
                    }
                })
        //Coupon impression ga
        AnalyticsTrackerUtil.sendEvent(context,
                AnalyticsTrackerUtil.EventKeys.EVENT_VIEW_COUPON,
                AnalyticsTrackerUtil.CategoryKeys.PENUKARAN_POINT_DETAIL,
                AnalyticsTrackerUtil.ActionKeys.VIEW_COUPON,
                mCouponName)
    }

    private fun loadWebViewInBottomsheet(data: String, title: String) {
        val bottomSheet = BottomSheetUnify()
        bottomSheet.setShowListener {
            val sideMargin = 16.toPx()
            bottomSheet.bottomSheetWrapper.setPadding(0, 0, 0, 0)
            (bottomSheet.bottomSheetHeader.layoutParams as LinearLayout.LayoutParams).setMargins(sideMargin, sideMargin, sideMargin, sideMargin)
        }
        val view = layoutInflater.inflate(R.layout.catalog_bottomsheet, null, false)
        val webView = view.findViewById<WebView>(R.id.catalog_webview)
        webView.loadData(data, CommonConstant.COUPON_MIME_TYPE, CommonConstant.UTF_ENCODING)
        bottomSheet.apply {
            setChild(view)
            setTitle(title)
            showCloseIcon = true
            isDragable = true
            isHideable = true
        }
        bottomSheet.show(childFragmentManager, "")
    }

    override fun gotoSendGiftPage(id: Int, title: String, pointStr: String, banner: String) {
        val bundle = Bundle()
        bundle.putInt(CommonConstant.EXTRA_COUPON_ID, id)
        bundle.putString(CommonConstant.EXTRA_COUPON_TITLE, title)
        bundle.putString(CommonConstant.EXTRA_COUPON_POINT, pointStr)
        bundle.putString(CommonConstant.EXTRA_COUPON_BANNER, banner)
        val sendGiftFragment = SendGiftFragment()
        sendGiftFragment.arguments = bundle
        sendGiftFragment.show(childFragmentManager, CommonConstant.FRAGMENT_DETAIL_TOKOPOINT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_LOGIN && resultCode == Activity.RESULT_OK) {
            pointValueText!!.text = resources.getString(R.string.points_saya)
            mViewModel.getCatalogDetail(code ?: "")
            val item = catalogsValueEntity
            item?.let { if (it.isDisabled) mViewModel.startValidateCoupon(it) }
        }
    }

    companion object {
        private const val CONTAINER_LOADER = 0
        private const val CONTAINER_DATA = 1
        private const val CONTAINER_ERROR = 2
        private const val REQUEST_CODE_LOGIN = 1
        private const val CONTAINER_COUPON_ERROR = 3

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