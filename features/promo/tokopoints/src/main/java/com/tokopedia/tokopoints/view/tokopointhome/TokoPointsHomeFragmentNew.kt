package com.tokopedia.tokopoints.view.tokopointhome

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import com.google.android.material.tabs.TabLayout.ViewPagerOnTabSelectedListener
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.profilecompletion.view.activity.ProfileCompletionActivity
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.di.TokopointBundleComponent
import com.tokopedia.tokopoints.notification.TokoPointsNotificationManager
import com.tokopedia.tokopoints.notification.model.PopupNotification
import com.tokopedia.tokopoints.view.adapter.SectionCategoryAdapter
import com.tokopedia.tokopoints.view.cataloglisting.ValidateMessageDialog
import com.tokopedia.tokopoints.view.couponlisting.CouponListingStackedActivity.Companion.getCallingIntent
import com.tokopedia.tokopoints.view.customview.CustomViewPager
import com.tokopedia.tokopoints.view.customview.ServerErrorView
import com.tokopedia.tokopoints.view.customview.TokoPointToolbar
import com.tokopedia.tokopoints.view.customview.TokoPointToolbar.OnTokoPointToolbarClickListener
import com.tokopedia.tokopoints.view.firebaseAnalytics.TokopointPerformanceConstant.TokopointhomePlt.Companion.HOME_TOKOPOINT_PLT
import com.tokopedia.tokopoints.view.firebaseAnalytics.TokopointPerformanceConstant.TokopointhomePlt.Companion.HOME_TOKOPOINT_PLT_NETWORK_METRICS
import com.tokopedia.tokopoints.view.firebaseAnalytics.TokopointPerformanceConstant.TokopointhomePlt.Companion.HOME_TOKOPOINT_PLT_PREPARE_METRICS
import com.tokopedia.tokopoints.view.firebaseAnalytics.TokopointPerformanceConstant.TokopointhomePlt.Companion.HOME_TOKOPOINT_PLT_RENDER_METRICS
import com.tokopedia.tokopoints.view.firebaseAnalytics.TokopointPerformanceMonitoringListener
import com.tokopedia.tokopoints.view.fragment.StartPurchaseBottomSheet
import com.tokopedia.tokopoints.view.interfaces.onAppBarCollapseListener
import com.tokopedia.tokopoints.view.intro.RewardIntroActivity
import com.tokopedia.tokopoints.view.model.CatalogsValueEntity
import com.tokopedia.tokopoints.view.model.LobDetails
import com.tokopedia.tokopoints.view.model.rewardintro.TokopediaRewardIntroPage
import com.tokopedia.tokopoints.view.model.rewardtopsection.DynamicActionListItem
import com.tokopedia.tokopoints.view.model.rewardtopsection.TokopediaRewardTopSection
import com.tokopedia.tokopoints.view.model.section.SectionContent
import com.tokopedia.tokopoints.view.util.*
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.NotificationUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.*
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.tp_fragment_homepage_new.*
import kotlinx.android.synthetic.main.tp_home_point.*
import kotlinx.android.synthetic.main.tp_item_dynamic_action.view.*
import kotlinx.android.synthetic.main.tp_item_layout_dynamic_action.view.*
import kotlinx.android.synthetic.main.tp_layout_section_category_parent.*
import javax.inject.Inject

/*
 * Dynamic layout params are applied via
 * function setLayoutParams() because configuration in statusBarHeight
 * */
class TokoPointsHomeFragmentNew : BaseDaggerFragment(), TokoPointsHomeContract.View, View.OnClickListener, OnTokoPointToolbarClickListener, TokopointPerformanceMonitoringListener {
    private var mContainerMain: ViewFlipper? = null
    private var mTextMembershipValue: TextView? = null
    private var mTargetText: TextView? = null
    private var mTextMembershipValueBottom: TextView? = null
    private var mTextPoints: TextView? = null
    private var mTextPointsBottom: TextView? = null
    private var mTextLoyalty: TextView? = null
    private var mTextMembershipLabel: TextView? = null
    private var mImgEgg: ImageView? = null
    private var mImgEggBottom: ImageView? = null
    private var mImgBackground: ImageView? = null
    private var mTabLayoutPromo: TabLayout? = null
    private var mPagerPromos: CustomViewPager? = null
    private var bottomViewMembership: LinearLayout? = null
    private var appBarHeader: AppBarLayout? = null
    private var mRvDynamicLinks: RecyclerView? = null

    @Inject
    lateinit var viewFactory: ViewModelFactory
    private val mPresenter: TokoPointsHomeViewModel by lazy { ViewModelProviders.of(this, viewFactory).get(TokoPointsHomeViewModel::class.java) }
    private var mSumToken = 0
    private var mValueMembershipDescription: String? = null
    private var mStartPurchaseBottomSheet: StartPurchaseBottomSheet? = null
    private var tickerContainer: View? = null
    private var dynamicLinksContainer: View? = null
    private var containerEgg: LinearLayout? = null
    private var appBarCollapseListener: onAppBarCollapseListener? = null
    private var mExploreSectionPagerAdapter: ExploreSectionPagerAdapter? = null
    private var collapsingToolbarLayout: CollapsingToolbarLayout? = null
    private var coordinatorLayout: CoordinatorLayout? = null
    private var statusBarBgView: View? = null
    private var tokoPointToolbar: TokoPointToolbar? = null
    private var serverErrorView: ServerErrorView? = null
    private var userLoggedInStatus: Boolean? = null
    private var rewardsPointLayout: CardUnify? = null
    private var ivPointStack: AppCompatImageView? = null
    private var tvPointLabel: TextView? = null
    private var midSeparator: View? = null
    private var ivLoyaltyStack: AppCompatImageView? = null
    private var tvLoyaltyLabel: TextView? = null
    private var pointLayout: LinearLayout? = null

    //   private var tvNonLoginCta: TextView? = null
    private var pageLoadTimePerformanceMonitoring: PageLoadTimePerformanceInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        startPerformanceMonitoring()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (arguments != null) {
            userLoggedInStatus = arguments!!.getBoolean(CommonConstant.BUNDLE_ARGS_USER_IS_LOGGED_IN)
        }
        val view = inflater.inflate(R.layout.tp_fragment_homepage_new, container, false)
        initViews(view)
        hideStatusBar()
        (activity as BaseSimpleActivity?)!!.setSupportActionBar(tokoPointToolbar)
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar)
        collapsingToolbarLayout?.setExpandedTitleColor(resources.getColor(android.R.color.transparent))
        collapsingToolbarLayout?.setTitle(" ")
        appBarHeader!!.addOnOffsetChangedListener(OnOffsetChangedListener { appBarLayout: AppBarLayout?, verticalOffset: Int -> handleAppBarOffsetChange(verticalOffset) })
        appBarHeader?.addOnOffsetChangedListener(OnOffsetChangedListener { appBarLayout: AppBarLayout?, verticalOffset: Int -> handleAppBarIconChange(appBarLayout, verticalOffset) })
        setLayoutParams()
        return view
    }

    private fun handleAppBarIconChange(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        val verticalOffset1 = Math.abs(verticalOffset)
        if (verticalOffset1 >= appBarLayout?.totalScrollRange!! - tickerContainer!!.height - dynamicLinksContainer!!.height - (card_point.height) / 2) {
            tokoPointToolbar?.showToolbarIcon()
        } else
            tokoPointToolbar?.hideToolbarIcon()
    }

    private fun setLayoutParams() {
        val statusBarHeight = getStatusBarHeight(activity)
        val layoutParams = tokoPointToolbar!!.layoutParams as FrameLayout.LayoutParams
        layoutParams.topMargin = statusBarHeight
        tokoPointToolbar!!.layoutParams = layoutParams
        val imageEggLp = mImgEgg!!.layoutParams as RelativeLayout.LayoutParams
        imageEggLp.topMargin = (statusBarHeight + activity!!.resources.getDimension(R.dimen.tp_top_margin_big_image)).toInt()
        mImgEgg!!.layoutParams = imageEggLp
        val imageBigLp = mImgBackground!!.layoutParams as RelativeLayout.LayoutParams
        imageBigLp.height = (statusBarHeight + activity!!.resources.getDimension(R.dimen.tp_home_top_bg_height)).toInt()
        mImgBackground!!.layoutParams = imageBigLp
    }

    private fun hideStatusBar() {
        coordinatorLayout!!.fitsSystemWindows = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            coordinatorLayout!!.requestApplyInsets()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var flags = coordinatorLayout!!.systemUiVisibility
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            coordinatorLayout!!.systemUiVisibility = flags
            activity!!.window.statusBarColor = Color.WHITE
        }
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
        }
        if (Build.VERSION.SDK_INT >= 19) {
            activity!!.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            activity!!.window.statusBarColor = Color.TRANSPARENT
        }
    }

    var offsetChangedListenerBottomView = OnOffsetChangedListener { appBarLayout, verticalOffset ->
        var verticalOffset = verticalOffset
        verticalOffset = Math.abs(verticalOffset)
        if (verticalOffset >= appBarLayout.totalScrollRange - tickerContainer!!.height - dynamicLinksContainer!!.height) {
            slideUp()
        } else {
            slideDown()
        }
    }

    private fun handleAppBarOffsetChange(offset: Int) {
        val positiveOffset = offset * -1
        val toolbarTransitionRange = (resources.getDimensionPixelSize(R.dimen.tp_home_top_bg_height)
                - tokoPointToolbar!!.height - getStatusBarHeight(activity))
        var offsetAlpha = 255f / toolbarTransitionRange * (toolbarTransitionRange - positiveOffset)
        if (offsetAlpha < 0) {
            offsetAlpha = 0f
        }
        if (offsetAlpha >= 255) {
            offsetAlpha = 255f
        }
        var alpha = offsetAlpha / 255 - 1
        if (alpha < 0) alpha = alpha * -1
        statusBarBgView?.alpha = alpha
        if (alpha > 0.5) tokoPointToolbar?.switchToDarkMode() else tokoPointToolbar?.switchToTransparentMode()
        tokoPointToolbar?.applyAlphaToToolbarBackground(alpha)
    }

    private fun slideUp() {
        if (bottomViewMembership?.visibility != View.VISIBLE) {
            val layoutParams = containerEgg?.layoutParams as CoordinatorLayout.LayoutParams
            layoutParams.setMargins(0, 0, 0, resources.getDimensionPixelOffset(com.tokopedia.design.R.dimen.dp_90))
            val bottomUp = AnimationUtils.loadAnimation(bottomViewMembership?.context,
                    R.anim.tp_bottom_up)
            bottomViewMembership?.startAnimation(bottomUp)
            bottomViewMembership?.visibility = View.VISIBLE
        }
    }

    private fun slideDown() {
        if (bottomViewMembership?.visibility != View.GONE) {
            val layoutParams = containerEgg?.layoutParams as CoordinatorLayout.LayoutParams
            layoutParams.setMargins(0, 0, 0, resources.getDimensionPixelOffset(R.dimen.tp_margin_large))
            val slideDown = AnimationUtils.loadAnimation(bottomViewMembership?.context,
                    R.anim.tp_bottom_down)
            bottomViewMembership?.startAnimation(slideDown)
            bottomViewMembership?.visibility = View.GONE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stopPreparePagePerformanceMonitoring()
        startNetworkRequestPerformanceMonitoring()
        initListener()
        mPresenter.getTokoPointDetail()
        mPresenter.getRewardIntroData()
        tokoPointToolbar?.setTitle(R.string.tp_title_tokopoints)
        tokoPointToolbar?.setOnTokoPointToolbarClickListener(this)
        TokoPointsNotificationManager.fetchNotification(activity, "main", childFragmentManager)
        //mPresenter.tokopointOnboarding2020(this)
        initObserver()
    }

    private fun initObserver() {
        addStartValidateObserver()
        addStartSaveCouponObserver()
        addRedeemCouponObserver()
        addTokopointDetailObserver()
        addRewardIntroObserver()
    }

    private fun addRewardIntroObserver() = mPresenter.rewardIntroData.observe(this, Observer {
        it?.let {
            when (it) {
                is Success -> {
                    showOnBoardingTooltip(it.data.tokopediaRewardIntroPage)
                }
            }
        }
    })

    private fun addTokopointDetailObserver() = mPresenter.tokopointDetailLiveData.observe(this, androidx.lifecycle.Observer {
        it?.let {
            when (it) {
                is Loading -> showLoading()
                is ErrorMessage -> {
                    hideLoading()
                    onError(it.data, NetworkDetector.isConnectedToInternet(context))
                }
                is Success -> {
                    hideLoading()
                    stopNetworkRequestPerformanceMonitoring()
                    startRenderPerformanceMonitoring()
                    setOnRecyclerViewLayoutReady()
                    onSuccessResponse(it.data.tokoPointEntity, it.data.sectionList)
                }
            }
        }
    })

    private fun addRedeemCouponObserver() = mPresenter.onRedeemCouponLiveData.observe(this, androidx.lifecycle.Observer {
        it?.let { RouteManager.route(context, it) }
    })

    private fun addStartSaveCouponObserver() = mPresenter.startSaveCouponLiveData.observe(this, androidx.lifecycle.Observer {
        it?.let {
            when (it) {
                is Success -> showConfirmRedeemDialog(it.data.cta, it.data.code, it.data.title)
                is ValidationError<*, *> -> {
                    if (it.data is ValidateMessageDialog) {
                        showValidationMessageDialog(it.data.item, it.data.title, it.data.desc, it.data.messageCode)
                    }
                }
            }
        }
    })

    private fun addStartValidateObserver() = mPresenter.startValidateCouponLiveData.observe(this, androidx.lifecycle.Observer {
        it?.let {
            showValidationMessageDialog(it.item, it.title, it.desc, it.messageCode)
        }
    })

    override fun onResume() {
        super.onResume()
        mPresenter.couponCount
        AnalyticsTrackerUtil.sendScreenEvent(activity, screenName)
    }

    override fun showLoading() {
        mContainerMain?.displayedChild = CONTAINER_LOADER
    }

    override fun hideLoading() {
        mContainerMain?.displayedChild = CONTAINER_DATA
    }

    override fun getAppContext(): Context {
        return activity!!
    }

    override fun getActivityContext(): Context {
        return activity!!
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is TokoPointsHomeNewActivity) appBarCollapseListener = context
    }

    override fun getScreenName(): String {
        return AnalyticsTrackerUtil.ScreenKeys.HOME_PAGE_SCREEN_NAME
    }

    override fun initInjector() {
        getComponent(TokopointBundleComponent::class.java).inject(this)
    }

    override fun onClick(source: View) {
        if (source.id == R.id.text_membership_label || source.id == R.id.img_egg || source.id == R.id.text_membership_value) {
            RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW_TITLE, getString(R.string.tp_label_membership), CommonConstant.WebLink.MEMBERSHIP)
            AnalyticsTrackerUtil.sendEvent(context,
                    AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                    AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                    AnalyticsTrackerUtil.ActionKeys.CLICK_STATUS_MEMBERSHIP,
                    mValueMembershipDescription)
        } else if (source.id == R.id.view_loyalty_bottom) {
            RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW_TITLE, getString(R.string.tp_label_membership), CommonConstant.WebLink.MEMBERSHIP)
            AnalyticsTrackerUtil.sendEvent(context,
                    AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                    AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                    AnalyticsTrackerUtil.ActionKeys.CLICK_MEM_BOTTOM,
                    "")
        } else if (source.id == R.id.text_failed_action) {
            mPresenter.getTokoPointDetail()
        }
    }

    private fun initViews(view: View) {
        coordinatorLayout = view.findViewById(R.id.container)
        mContainerMain = view.findViewById(R.id.container_main)
        mTextMembershipValue = view.findViewById(R.id.text_membership_value)
        mTargetText = view.findViewById(R.id.tv_targetText)
        mTextMembershipLabel = view.findViewById(R.id.text_membership_label)
        mTextPoints = view.findViewById(R.id.text_my_points_value)
        mTextLoyalty = view.findViewById(R.id.text_loyalty_value)
        mImgEgg = view.findViewById(R.id.img_egg)
        mTabLayoutPromo = view.findViewById(R.id.tab_layout_promos)
        mPagerPromos = view.findViewById(R.id.view_pager_promos)
        mPagerPromos?.disableScroll(true)
        mTextMembershipValueBottom = view.findViewById(R.id.text_loyalty_value_bottom)
        mTextPointsBottom = view.findViewById(R.id.text_my_points_value_bottom)
        mImgEggBottom = view.findViewById(R.id.img_loyalty_stack_bottom)
        mImgBackground = view.findViewById(R.id.img_bg_header)
        appBarHeader = view.findViewById(R.id.app_bar)
        bottomViewMembership = view.findViewById(R.id.bottom_view_membership)
        tickerContainer = view.findViewById(R.id.cons_ticker_container)
        containerEgg = view.findViewById(R.id.container_fab_egg_token)
        mRvDynamicLinks = view.findViewById(R.id.rv_dynamic_link)
        dynamicLinksContainer = view.findViewById(R.id.container_dynamic_links)
        statusBarBgView = view.findViewById(R.id.status_bar_bg)
        tokoPointToolbar = view.findViewById(R.id.toolbar_tokopoint)
        serverErrorView = view.findViewById(R.id.server_error_view)
        rewardsPointLayout = view.findViewById(R.id.card_point)
        ivPointStack = view.findViewById(R.id.img_points_stack)
        tvPointLabel = view.findViewById(R.id.text_my_points_label)
        midSeparator = view.findViewById(R.id.line_separator_points_vertical)
        ivLoyaltyStack = view.findViewById(R.id.img_loyalty_stack)
        pointLayout = view.findViewById(R.id.layout_homepoint)
        tvLoyaltyLabel = view.findViewById(R.id.text_loyalty_label)
        setStatusBarViewHeight()
    }

    private fun setStatusBarViewHeight() {
        if (activity != null) statusBarBgView?.layoutParams?.height = getStatusBarHeight(activity)
    }

    private fun initListener() {
        if (view == null) {
            return
        }
        view?.findViewById<View>(R.id.view_loyalty_bottom)?.setOnClickListener(this)
        view?.findViewById<View>(R.id.view_point_bottom)?.setOnClickListener(this)
        view?.findViewById<View>(R.id.img_egg)?.setOnClickListener(this)
        view?.findViewById<View>(R.id.text_membership_value)?.setOnClickListener(this)
        view?.findViewById<View>(R.id.container_fab_egg_token)?.setOnClickListener(this)
    }

    override fun openWebView(url: String) {
        RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, url)
    }

    override fun onError(error: String, hasInternet: Boolean) {
        if (mContainerMain != null) {
            mContainerMain?.displayedChild = CONTAINER_ERROR
            serverErrorView?.showErrorUi(hasInternet)
        }
    }

    override fun showRedeemCouponDialog(cta: String, code: String, title: String) {
        val adb = AlertDialog.Builder(activityContext)
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
        adb.setPositiveButton(R.string.tp_label_use) { dialogInterface: DialogInterface?, i: Int ->
            //Call api to validate the coupon
            mPresenter.redeemCoupon(code, cta)
            AnalyticsTrackerUtil.sendEvent(context,
                    AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                    AnalyticsTrackerUtil.CategoryKeys.POPUP_KONFIRMASI_GUNAKAN_KUPON,
                    AnalyticsTrackerUtil.ActionKeys.CLICK_GUNAKAN,
                    title)
        }
        adb.setNegativeButton(R.string.tp_label_later) { dialogInterface: DialogInterface?, i: Int ->
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

    override fun showConfirmRedeemDialog(cta: String, code: String, title: String) {
        val adb = AlertDialog.Builder(activityContext)
        adb.setNegativeButton(R.string.tp_label_use) { dialogInterface: DialogInterface?, i: Int ->
            showRedeemCouponDialog(cta, code, title)
            AnalyticsTrackerUtil.sendEvent(context,
                    AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                    AnalyticsTrackerUtil.CategoryKeys.POPUP_PENUKARAN_BERHASIL,
                    AnalyticsTrackerUtil.ActionKeys.CLICK_GUNAKAN,
                    title)
        }
        adb.setPositiveButton(R.string.tp_label_view_coupon) { dialogInterface: DialogInterface?, i: Int ->
            startActivity(getCallingIntent(activityContext))
            AnalyticsTrackerUtil.sendEvent(context,
                    AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                    AnalyticsTrackerUtil.CategoryKeys.POPUP_PENUKARAN_BERHASIL,
                    AnalyticsTrackerUtil.ActionKeys.CLICK_LIHAT_KUPON,
                    "")
        }
        adb.setTitle(R.string.tp_label_successful_exchange)
        val dialog = adb.create()
        dialog.show()
        decorateDialog(dialog)
        AnalyticsTrackerUtil.sendEvent(context,
                AnalyticsTrackerUtil.EventKeys.EVENT_VIEW_COUPON,
                AnalyticsTrackerUtil.CategoryKeys.POPUP_PENUKARAN_BERHASIL,
                AnalyticsTrackerUtil.ActionKeys.VIEW_REDEEM_SUCCESS,
                title)
    }

    override fun showValidationMessageDialog(item: CatalogsValueEntity, title: String, message: String, resCode: Int) {
        val adb = AlertDialog.Builder(activityContext)
        val labelPositive: String
        var labelNegative: String? = null
        when (resCode) {
            CommonConstant.CouponRedemptionCode.LOW_POINT -> labelPositive = getString(R.string.tp_label_ok)
            CommonConstant.CouponRedemptionCode.PROFILE_INCOMPLETE -> {
                labelPositive = getString(R.string.tp_label_complete_profile)
                labelNegative = getString(R.string.tp_label_later)
            }
            CommonConstant.CouponRedemptionCode.SUCCESS -> {
                labelPositive = getString(R.string.tp_label_exchange)
                labelNegative = getString(R.string.tp_label_betal)
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
                    startActivity(Intent(appContext, ProfileCompletionActivity::class.java))
                    AnalyticsTrackerUtil.sendEvent(context,
                            AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                            AnalyticsTrackerUtil.CategoryKeys.POPUP_VERIFIED,
                            AnalyticsTrackerUtil.ActionKeys.CLICK_INCOMPLETE_PROFILE,
                            "")
                }
                CommonConstant.CouponRedemptionCode.SUCCESS -> {
                    mPresenter!!.startSaveCoupon(item)
                    AnalyticsTrackerUtil.sendEvent(context,
                            AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                            AnalyticsTrackerUtil.CategoryKeys.POPUP_KONFIRMASI,
                            AnalyticsTrackerUtil.ActionKeys.CLICK_TUKAR,
                            title)
                }
                else -> dialogInterface.cancel()
            }
        }
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

    private fun showOnBoardingTooltip(data: TokopediaRewardIntroPage?) {
        if (data != null) {
            val bundle = Bundle()
            bundle.putParcelable("intro", data)
            startActivity(RewardIntroActivity.getCallingIntent(context!!, bundle))
        }
    }

    override fun renderTicker(content: SectionContent) {
        if (view == null || content == null || content.layoutTickerAttr == null || content.layoutTickerAttr.tickerList == null || content.layoutTickerAttr.tickerList.isEmpty()) {
            tickerContainer?.visibility = View.GONE
            return
        }

        val pager: Ticker? = view?.findViewById(R.id.ticker_new)
        val emptyTitle = ""
        var link = ""
        var desc = ""
        val tickerDataList: MutableList<TickerData> = java.util.ArrayList()
        for ((i, tickerItem) in content.layoutTickerAttr.tickerList.withIndex()) {
            link = if (tickerItem.metadata[i].link[CommonConstant.TickerMapKeys.APP_LINK]?.length != 0) {
                tickerItem.metadata[i].link[CommonConstant.TickerMapKeys.APP_LINK].toString()
            } else {
                tickerItem.metadata[i].link[CommonConstant.TickerMapKeys.URL].toString()
            }
            desc = tickerItem.metadata[i].text[CommonConstant.TickerMapKeys.CONTENT].toString()
            tickerDataList.add(TickerData(emptyTitle, "$desc $link", Ticker.TYPE_ANNOUNCEMENT, true))
        }
        val adapter = TickerPagerAdapter(context, tickerDataList)
        pager?.addPagerView(adapter, tickerDataList)

        pager?.setDescriptionClickEvent(object : TickerCallback {

            override fun onDescriptionViewClick(linkUrl: CharSequence) {

                if (linkUrl.startsWith(CommonConstant.TickerMapKeys.TOKOPEDIA)) {
                    RouteManager.route(context, linkUrl.toString())

                } else {
                    RouteManager.route(context, String.format("%s?url=%", ApplinkConst.WEBVIEW, linkUrl.toString()))

                }

                AnalyticsTrackerUtil.sendEvent(context,
                        AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                        AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                        AnalyticsTrackerUtil.ActionKeys.CLICK_TICKER,
                        "$desc $link")

            }

            override fun onDismiss() {
            }

        })
        adapter.setPagerDescriptionClickEvent(object : TickerPagerCallback {
            override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
                AnalyticsTrackerUtil.sendEvent(context,
                        AnalyticsTrackerUtil.EventKeys.EVENT_VIEW_TOKOPOINT,
                        AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                        AnalyticsTrackerUtil.ActionKeys.VIEW_TICKER,
                        "")
            }

        })
        tickerContainer?.visibility = View.VISIBLE
    }

    override fun renderCategory(content: SectionContent) {
        if (content.layoutCategoryAttr == null || content.layoutCategoryAttr.categoryTokopointsList == null || content.layoutCategoryAttr.categoryTokopointsList.isEmpty()) {
            return
        }
        dynamicLinksContainer?.visibility = View.VISIBLE
        val manager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        mRvDynamicLinks?.layoutManager = manager
        mRvDynamicLinks?.adapter = SectionCategoryAdapter(activityContext, content.layoutCategoryAttr.categoryTokopointsList)
    }

    override fun renderToolbarWithHeader(data: TokopediaRewardTopSection) {
        tokoPointToolbar?.setScrolledItem(data.dynamicActionList)
        mTextMembershipLabel?.text = data.introductionText

        data.target?.let {
            mTargetText?.setTextColor(Color.parseColor("#" + it.textColor))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mTargetText?.text = Html.fromHtml(it.text, Html.FROM_HTML_MODE_LEGACY)
            } else {
                mTargetText?.text = Html.fromHtml(it.text)
            }
            container_target.setBackgroundColor(Color.parseColor("#" + it.backgroundColor))
            container_target.setOnClickListener {
                RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW_TITLE, getString(R.string.tp_label_membership), CommonConstant.WebLink.MEMBERSHIP)
            }
        }

        ImageHandler.loadImageCircle2(activityContext, mImgEgg, data.profilePicture)
        mTextMembershipValueBottom?.text = mValueMembershipDescription
        collapsingToolbarLayout?.title = data.title

        if (data.tier != null) {
            mTextMembershipValue?.text = data.tier.nameDesc
            ImageHandler.loadImageFitCenter(mImgBackground?.context, mImgBackground, data.backgroundImageURL)
        }

        renderDynamicActionList(data.dynamicActionList)
    }

    fun renderDynamicActionList(dynamicActionList: List<DynamicActionListItem?>?) {

        dynamicActionList?.let { dynamicItemActionList ->
            val param = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1F)
            for (item in dynamicItemActionList) {
                val view = View.inflate(this.context, R.layout.tp_item_layout_dynamic_action, null)
                view.iv_dynamic.loadImage(item?.iconImageURL!!)
                view.tv_dynamic.text = item.cta?.text
                if (item.counter?.isShowCounter != null && item.counter.counterStr != null && item.counter.counterStr.isNotEmpty()) {
                    view.notif_dynamic.visibility = View.VISIBLE
                    view.notif_dynamic.setNotification(item.counter.counterStr, NotificationUnify.TEXT_TYPE, NotificationUnify.COLOR_PRIMARY)
                }
                view.setOnClickListener {
                    RouteManager.route(context, item.cta?.appLink)
                    view.notif_dynamic.visibility = View.GONE
                }
                layout_homepoint.addView(view, param)
            }
        }
    }

    override fun renderPurchaseBottomsheet(data: LobDetails) {
        if (data == null || view == null) {
            return
        }
        if (mStartPurchaseBottomSheet == null) {
            mStartPurchaseBottomSheet = StartPurchaseBottomSheet()
        }
        mStartPurchaseBottomSheet!!.setData(data)
    }

    override fun renderSections(sections: List<SectionContent>) {
        if (sections == null) { //TODO hide all section container
            return
        }
        val exploreSectionItem: MutableList<SectionContent> = ArrayList()
        for (sectionContent in sections) {
            when (sectionContent.layoutType) {
                CommonConstant.SectionLayoutType.TICKER -> renderTicker(sectionContent)
                CommonConstant.SectionLayoutType.CATEGORY -> renderCategory(sectionContent)
                CommonConstant.SectionLayoutType.COUPON, CommonConstant.SectionLayoutType.CATALOG, CommonConstant.SectionLayoutType.BANNER -> exploreSectionItem.add(sectionContent)
                else -> {
                }
            }
        }
        //init explore and kupon-saya tab
        renderExploreSectionTab(exploreSectionItem)
    }

    override fun renderExploreSectionTab(sections: List<SectionContent>) {
        if (sections.isEmpty()) { //TODO hide tab or show empty box
        }
        mExploreSectionPagerAdapter = ExploreSectionPagerAdapter(activityContext, mPresenter, sections)
        mExploreSectionPagerAdapter?.setRefreshing(false)
        mPagerPromos?.adapter = mExploreSectionPagerAdapter
        mPagerPromos?.addOnPageChangeListener(TabLayoutOnPageChangeListener(mTabLayoutPromo))
        mTabLayoutPromo?.addOnTabSelectedListener(ViewPagerOnTabSelectedListener(mPagerPromos))
        mPagerPromos?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    appBarHeader!!.addOnOffsetChangedListener(offsetChangedListenerBottomView)
                    mPresenter.pagerSelectedItem = position
                    AnalyticsTrackerUtil.sendEvent(context,
                            AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                            AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                            AnalyticsTrackerUtil.ActionKeys.CLICK_EXPLORE,
                            "")
                } else {
                    appBarHeader!!.removeOnOffsetChangedListener(offsetChangedListenerBottomView)
                    slideDown()
                    mPresenter.pagerSelectedItem = position
                    AnalyticsTrackerUtil.sendEvent(context,
                            AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                            AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                            AnalyticsTrackerUtil.ActionKeys.CLICK_KUPON_SAYA,
                            "")
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    override fun onSuccessResponse(data: TokopediaRewardTopSection, sections: List<SectionContent>) {
        mContainerMain?.displayedChild = CONTAINER_DATA
        renderToolbarWithHeader(data)
        renderSections(sections)
    }

    override fun showTokopoint2020(data: PopupNotification) {
        if (data.title.isEmpty() || data.title == null || data.appLink.isEmpty() || data.appLink == null) {
            return
        }
        val btn: UnifyButton
        val titleDialog: Typography
        val descDialog: Typography
        val boxImageView: ImageView
        val adb = AlertDialog.Builder(context!!)
        val view = LayoutInflater.from(context).inflate(R.layout.tp_upcoming_feature_dialog, null)
        adb.setView(view)
        btn = view.findViewById(R.id.btn_route)
        titleDialog = view.findViewById(R.id.tv_dialogTitle)
        descDialog = view.findViewById(R.id.tv_dialogDesc)
        boxImageView = view.findViewById(R.id.iv_banner)
        titleDialog.text = data.title
        descDialog.text = data.text
        btn.text = data.buttonText
        ImageHandler.loadImageFitCenter(context, boxImageView, data.imageURL)
        val alertDialog = adb.create()
        alertDialog.window.setBackgroundDrawableResource(android.R.color.transparent)
        alertDialog.setCancelable(false)
        alertDialog.setCanceledOnTouchOutside(false)
        btn.setOnClickListener { v: View? ->
            RouteManager.route(context, data.appLink)
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    override fun onToolbarMyCouponClick() {
        if (activity == null) {
            return
        }
        startActivity(getCallingIntent(activity!!))
        AnalyticsTrackerUtil.sendEvent(context,
                AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                AnalyticsTrackerUtil.ActionKeys.CLICK_COUNTER_KUPON_SAYA,
                "")
    }

    override fun onDestroyView() {
        if (mExploreSectionPagerAdapter != null) {
            mExploreSectionPagerAdapter!!.onDestroyView()
        }
        super.onDestroyView()
    }

    companion object {
        private const val FPM_TOKOPOINT = "ft_tokopoint"
        private const val CONTAINER_LOADER = 0
        private const val CONTAINER_DATA = 1
        private const val CONTAINER_ERROR = 2
        private const val REQUEST_CODE_LOGIN = 1
        fun newInstance(): TokoPointsHomeFragmentNew {
            return TokoPointsHomeFragmentNew()
        }

        fun getStatusBarHeight(context: Context?): Int {
            var height = 0
            val resId = context!!.resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resId > 0) {
                height = context.resources.getDimensionPixelSize(resId)
            }
            return height
        }

        fun setWindowFlag(activity: Activity?, bits: Int, on: Boolean) {
            val win = activity!!.window
            val winParams = win.attributes
            if (on) {
                winParams.flags = winParams.flags or bits
            } else {
                winParams.flags = winParams.flags and bits.inv()
            }
            win.attributes = winParams
        }
    }

    override fun startPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring = PageLoadTimePerformanceCallback(
                HOME_TOKOPOINT_PLT_PREPARE_METRICS,
                HOME_TOKOPOINT_PLT_NETWORK_METRICS,
                HOME_TOKOPOINT_PLT_RENDER_METRICS,
                0,
                0,
                0,
                0,
                null
        )

        pageLoadTimePerformanceMonitoring?.startMonitoring(HOME_TOKOPOINT_PLT)
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

    private fun setOnRecyclerViewLayoutReady() {
        rv_dynamic_link.viewTreeObserver
                .addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        if (pageLoadTimePerformanceMonitoring != null) {
                            stopRenderPerformanceMonitoring()
                            stopPerformanceMonitoring()
                        }
                        pageLoadTimePerformanceMonitoring = null
                        rv_dynamic_link.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                })
    }
}