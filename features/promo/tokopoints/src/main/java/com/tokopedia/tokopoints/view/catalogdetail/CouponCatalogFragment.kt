package com.tokopedia.tokopoints.view.catalogdetail

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.appcompat.app.AlertDialog
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
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
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
import com.tokopedia.tokopoints.view.util.CommonConstant.Companion.CATALOG_CLAIM_MESSAGE
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
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
    var progressBar: TimerUnifySingle? = null
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
    private var menu:Menu?=null

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
        this.menu=menu
        inflater.inflate(R.menu.menu_coupon_catalog, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        val id = item.itemId
        if (id == R.id.action_menu_share) {
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            sharingIntent.putExtra(Intent.EXTRA_TEXT, CommonConstant.WebLink.DETAIL + requireArguments().getString(CommonConstant.EXTRA_CATALOG_CODE))
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
        addValidationDialogObserver()
        addLatestStatusObserver()
        addStartSaveCouponObserver()
        addRedeemCouponObserver()
    }

    private fun addRedeemCouponObserver() = mViewModel.onRedeemCouponLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
        it?.let { RouteManager.route(context, it) }
    })

    private fun addStartSaveCouponObserver() = mViewModel.startSaveCouponLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
        when (it) {
            is Success -> redeemCoupon(it.data.cta, it.data.code, it.data.title, it.data.description, it.data.redeemMessage)
            is ValidationError<*, *> -> {
                if (it.data is ValidateMessageDialog) {
                    checkValidation(it.data.item, it.data.title, it.data.desc, it.data.messageCode)
                }
            }
        }
    })

    private fun addLatestStatusObserver() = mViewModel.latestStatusLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
        it?.let { refreshCatalog(it) }
    })

    private fun addValidationDialogObserver() = mViewModel.startValidateCouponLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
        it?.let {
            checkValidation(it.item, it.title, it.desc, it.messageCode)
        }
    })

    private fun addSendGiftDialogObserver() = mViewModel.sendGiftPageLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
        when (it) {
            is Success -> gotoSendGiftPage(it.data.id, it.data.title, it.data.pointStr, it.data.banner)
            is ValidationError<*, *> -> {
                if (it.data is PreValidateError)
                    onPreValidateError(it.data.title, it.data.message)
            }
        }
    })

    private fun addCatalogDetailObserver() = mViewModel.catalogDetailLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
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
        return requireContext()
    }

    override fun showLoader() {
        mContainerMain!!.displayedChild = CONTAINER_LOADER
    }

    override fun showError(hasInternet: Boolean) {
        if (menu != null) {
            setMenuVisibility(menu!!, false)
        }
        mContainerMain!!.displayedChild = CONTAINER_ERROR
        serverErrorView!!.showErrorUi(hasInternet)
    }

    private fun showCouponError() {
        if (menu != null) {
            setMenuVisibility(menu!!, false)
        }
        container?.displayedChild = CONTAINER_COUPON_ERROR
        btnError.setOnClickListener {
            RouteManager.route(context, ApplinkConst.TOKOPEDIA_REWARD)
        }
    }

    override fun hideLoader() {
        mContainerMain!!.displayedChild = CONTAINER_DATA
    }

    override fun populateDetail(data: CatalogsValueEntity) {
        catalogsValueEntity = data
        setCatalogToUi(data)
    }

    override fun getActivityContext(): Context {
        return requireActivity()
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
        serverErrorView?.setErrorButtonClickListener (View.OnClickListener {
            mViewModel.getCatalogDetail(code ?: "")

        })
    }

    override fun openWebView(url: String) {
        RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, url))
    }

    override fun redeemCoupon(cta: String?, code: String?, title: String?, description: String?, redeemMessage: String?) {
        val intent = RouteManager.getIntent(context,cta)
        intent.putExtra(CATALOG_CLAIM_MESSAGE,redeemMessage)
        startActivity(intent)
    }

    private fun showErrorDialog(item: CatalogsValueEntity, title: String?, message: String, resCode: Int) {
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

    override fun checkValidation(item: CatalogsValueEntity, title: String?, message: String, resCode: Int) {
        if (resCode == CommonConstant.CouponRedemptionCode.SUCCESS) {
            mViewModel.startSaveCoupon(item)
            AnalyticsTrackerUtil.sendEvent(context,
                    AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                    AnalyticsTrackerUtil.CategoryKeys.POPUP_KONFIRMASI,
                    AnalyticsTrackerUtil.ActionKeys.CLICK_TUKAR,
                    title)
        } else {
            showErrorDialog(item, title, message, resCode)
        }
    }

    override fun onRealCodeReFresh(realCode: String) {
        if (view == null || mSubscriptionCouponTimer == null) {
            return
        }
        val btnAction2: Typography = requireView().findViewById(R.id.button_action_2)
        if (realCode != null && !realCode.isEmpty()) {
            btnAction2.setText(R.string.tp_label_use)
            btnAction2.isEnabled = true
            mSubscriptionCouponTimer.unsubscribe()
            return
        }
        if (mRefreshRepeatCount >= CommonConstant.MAX_COUPON_RE_FETCH_COUNT) {
            btnAction2.setText(R.string.tp_label_refresh_repeat)
            btnAction2.isEnabled = true
            setButtonTextColor(btnAction2)
            mSubscriptionCouponTimer.unsubscribe()
        }
    }

    override fun onRealCodeReFreshError() {
        if (view == null || mSubscriptionCouponTimer == null) {
            return
        }
        val btnAction2: Typography = requireView().findViewById(R.id.button_action_2)
        btnAction2.setText(R.string.tp_label_refresh_repeat)
        btnAction2.isEnabled = true
        mSubscriptionCouponTimer.unsubscribe()
    }

    override fun refreshCatalog(data: CatalogStatusItem) {
        if (view == null) {
            return
        }
        val quota: Typography = requireView().findViewById(R.id.text_quota_count)
        val quotaContainer: LinearLayout? = view?.findViewById(R.id.quota_container)
        val btnAction2: Typography = requireView().findViewById(R.id.button_action_2)
        val imgBanner = requireView().findViewById<ImageView>(R.id.img_banner)
        btnAction2.isEnabled = !data.isDisabledButton
        if (data.isDisabledButton) {
            btnAction2.setTextColor(ContextCompat.getColor(btnAction2.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_32))
        } else {
            setButtonTextColor(btnAction2)
        }
        //Quota text handling
        if (data.upperTextDesc == null || data.upperTextDesc.isEmpty()) {
            quota.visibility = View.GONE
        } else {
            quotaContainer?.show()
            quota.visibility = View.VISIBLE
            val upperText = StringBuilder()
            for (i in data.upperTextDesc.indices) {
                if (i == 1) { //exclusive case for handling font color of second index.
                    upperText.append("<font color='${ColorUtil.getColorFromResToString(quota.context,com.tokopedia.unifyprinciples.R.color.Unify_Y400)}>" + data?.upperTextDesc?.get(i) + "</font>")
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
        pointValue?.setTextColor(ContextCompat.getColor(pointValue!!.getContext(), com.tokopedia.unifyprinciples.R.color.Unify_Y500))
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
                    com.tokopedia.unifyprinciples.R.color.Unify_G400))
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).isAllCaps = false
        }
        if (dialog.getButton(AlertDialog.BUTTON_NEGATIVE) != null) {
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).isAllCaps = false
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(activityContext,
                    com.tokopedia.unifyprinciples.R.color.Unify_N200))
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
        val quotaContainer = view?.findViewById<LinearLayout>(R.id.quota_container)
        val timerContainer = view?.findViewById<ConstraintLayout>(R.id.timer_container)
        progressBar = view?.findViewById<TimerUnifySingle>(R.id.timer_unify)
        val minUsageLabel = view?.findViewById<Typography>(R.id.tv_min_txn_label)
        val minUsageValue = view?.findViewById<Typography>(R.id.tv_min_txn_value)
        val transactionContainer = view?.findViewById<ConstraintLayout>(R.id.container_transaksi)
        val quota: Typography = requireView().findViewById(R.id.text_quota_count)
        val description: Typography = requireView().findViewById(R.id.text_description)
        val disabledError: Typography = requireView().findViewById(R.id.text_disabled_error)
        val giftSectionMainLayout: ConstraintLayout = requireView().findViewById(R.id.gift_section_main_layout)
        val giftImage: Typography = requireView().findViewById(R.id.gift_image)
        val giftButton: Typography = requireView().findViewById(R.id.gift_btn)
        val bottomSeparator = requireView().findViewById<View>(R.id.tp_bottom_separator)
        giftImage.setCompoundDrawablesWithIntrinsicBounds(MethodChecker.getDrawable(activity, R.drawable.ic_catalog_gift_btn), null, null, null)
        val btnAction2: Typography = requireView().findViewById(R.id.button_action_2)
        val imgBanner = requireView().findViewById<ImageView>(R.id.img_banner)
        val labelPoint: Typography = requireView().findViewById(R.id.text_point_label)
        val textDiscount: Typography = requireView().findViewById(R.id.text_point_discount)
        btnAction2.visibility = View.VISIBLE
        btnAction2.isEnabled = !data.isDisabledButton
        description.text = data.title
        btnAction2.text = data.buttonStr
        btnAction2.setBackgroundResource(R.drawable.bg_button_buy_orange_tokopoints)
        ImageHandler.loadImageFitCenter(imgBanner.context, imgBanner, data.imageUrlMobile)
        val tvHowToUse: TkpdWebView = requireView().findViewById(R.id.how_to_use_content)
        val tvTnc: TkpdWebView = requireView().findViewById(R.id.tnc_content)
        if (!data.tnc.isNullOrEmpty() && data.tnc != "<br>") {
            tvTnc.loadData(data.tnc, CommonConstant.COUPON_MIME_TYPE, CommonConstant.UTF_ENCODING)
        } else {
            view?.findViewById<Typography>(R.id.tnc)?.hide()
            view?.findViewById<View>(R.id.tp_mid_separator)?.hide()
            tvTnc.hide()
        }
        if (!data.howToUse.isNullOrEmpty() && data.howToUse != "<br>") {
            tvHowToUse.loadData(data.howToUse, CommonConstant.COUPON_MIME_TYPE, CommonConstant.UTF_ENCODING)
        } else {
            view?.findViewById<Typography>(R.id.how_to_use)?.hide()
            tvHowToUse.hide()
        }
        val pointValue: Typography = requireView().findViewById(R.id.text_point_value_coupon)
        pointValue.text = "Gratis"
        //Quota text handling
        if (data.upperTextDesc.isNullOrEmpty()) {
            quota.visibility = View.GONE
        } else {
            quotaContainer?.show()
            quota.visibility = View.VISIBLE
            val upperText = StringBuilder()
            for (i in data.upperTextDesc!!.indices) {
                if (i == 1) { //exclusive case for handling font color of second index.
                    upperText.append("<font color='${ColorUtil.getColorFromResToString(quota.context,com.tokopedia.unifyprinciples.R.color.Unify_Y400)}>" + data?.upperTextDesc?.get(i) + "</font>")
                } else {
                    upperText.append(data.upperTextDesc!![i]).append(" ")
                }
            }
            quota.text = MethodChecker.fromHtml(upperText.toString())
        }
        //Quota text handling
        if (data.disableErrorMessage.isNullOrEmpty()) {
            disabledError.visibility = View.GONE
        } else {
            quotaContainer?.show()
            disabledError.visibility = View.VISIBLE
            disabledError.text = data.disableErrorMessage
        }

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
            timerContainer?.visibility = View.VISIBLE
            showTimer(data)
        } else {
            timerContainer?.visibility = View.GONE
        }
        //disabling the coupons if not eligible for current membership
        if (data.isDisabled) {
            ImageUtil.dimImage(imgBanner)
            pointValue.setTextColor(ContextCompat.getColor(pointValue.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_44))
        } else {
            ImageUtil.unDimImage(imgBanner)
            pointValue.setTextColor(ContextCompat.getColor(pointValue.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_44))
        }
        if (data.isDisabledButton) {
            giftSectionMainLayout.visibility = View.GONE
            btnAction2.setTextColor(ContextCompat.getColor(btnAction2.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_20))
        } else {
            giftSectionMainLayout.visibility = View.VISIBLE
            setButtonTextColor(btnAction2)
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
        pointValueText = requireView().findViewById(R.id.text_point_value_label)
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

    private fun showTimer(item: CatalogsValueEntity) {
        progressBar?.timer?.cancel()
        val flipTimer = view?.findViewById<ViewFlipper>(R.id.flip_timer)
        val couponExpire = view?.findViewById<Typography>(R.id.text_timer_value)

        progressBar?.apply {
            onFinish = {
                flipTimer?.displayedChild = CONTAINER_DATE
                couponExpire?.text = resources.getString(R.string.tp_catalog_timer_expire)
            }
            timerTextWidth= TimerUnifySingle.TEXT_WRAP
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

    fun setButtonTextColor(btnContinue : Typography){
        context?.let {
            if (isDarkMode(it) || !isDarkMode(it)){
                btnContinue.setTextColor(ContextCompat.getColor(btnContinue.context, com.tokopedia.unifyprinciples.R.color.Unify_Static_White))
            }
        }
    }

    private fun setMenuVisibility(menu: Menu , isMenuVisible: Boolean){
            val menuItem = menu.findItem(R.id.action_menu_share)
            menuItem?.isVisible = isMenuVisible
    }

    override fun onDestroyView() {
        progressBar?.timer?.cancel()
        progressBar?.timer = null
        super.onDestroyView()
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