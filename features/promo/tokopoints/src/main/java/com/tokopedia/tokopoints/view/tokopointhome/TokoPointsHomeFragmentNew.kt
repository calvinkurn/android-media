package com.tokopedia.tokopoints.view.tokopointhome

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.ViewFlipper
import androidx.appcompat.widget.AppCompatImageView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.quest_widget.listeners.QuestWidgetCallbacks
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.di.TokopointBundleComponent
import com.tokopedia.tokopoints.notification.model.popupnotif.PopupNotif
import com.tokopedia.tokopoints.notification.view.TokopointNotifActivity
import com.tokopedia.tokopoints.view.customview.ServerErrorView
import com.tokopedia.tokopoints.view.customview.TokoPointToolbar
import com.tokopedia.tokopoints.view.firebaseAnalytics.TokopointPerformanceConstant.TokopointhomePlt.Companion.HOME_TOKOPOINT_PLT
import com.tokopedia.tokopoints.view.firebaseAnalytics.TokopointPerformanceConstant.TokopointhomePlt.Companion.HOME_TOKOPOINT_PLT_NETWORK_METRICS
import com.tokopedia.tokopoints.view.firebaseAnalytics.TokopointPerformanceConstant.TokopointhomePlt.Companion.HOME_TOKOPOINT_PLT_PREPARE_METRICS
import com.tokopedia.tokopoints.view.firebaseAnalytics.TokopointPerformanceConstant.TokopointhomePlt.Companion.HOME_TOKOPOINT_PLT_RENDER_METRICS
import com.tokopedia.tokopoints.view.firebaseAnalytics.TokopointPerformanceMonitoringListener
import com.tokopedia.tokopoints.view.interfaces.onAppBarCollapseListener
import com.tokopedia.tokopoints.view.intro.RewardIntroActivity
import com.tokopedia.tokopoints.view.intro.RewardIntroFragment
import com.tokopedia.tokopoints.view.model.homeresponse.RewardsRecommendation
import com.tokopedia.tokopoints.view.model.homeresponse.TopSectionResponse
import com.tokopedia.tokopoints.view.model.rewardintro.TokopediaRewardIntroPage
import com.tokopedia.tokopoints.view.model.rewardtopsection.DynamicActionListItem
import com.tokopedia.tokopoints.view.model.section.SectionContent
import com.tokopedia.tokopoints.view.tokopointhome.banner.SectionVerticalBanner11ViewBinder
import com.tokopedia.tokopoints.view.tokopointhome.banner.SectionVerticalBanner21ViewBinder
import com.tokopedia.tokopoints.view.tokopointhome.banner.SectionVerticalBanner31ViewBinder
import com.tokopedia.tokopoints.view.tokopointhome.carousel.SectionHorizontalCarouselViewBinder
import com.tokopedia.tokopoints.view.tokopointhome.catalog.SectionHoriZontalCatalogViewBinder
import com.tokopedia.tokopoints.view.tokopointhome.category.SectionVerticalCategoryViewBinder
import com.tokopedia.tokopoints.view.tokopointhome.column.SectionVerticalColumnViewBinder
import com.tokopedia.tokopoints.view.tokopointhome.coupon.SectionHorizontalViewBinder
import com.tokopedia.tokopoints.view.tokopointhome.header.TopSectionVH
import com.tokopedia.tokopoints.view.tokopointhome.header.TopSectionViewBinder
import com.tokopedia.tokopoints.view.tokopointhome.merchantvoucher.MerchantVoucherViewBinder
import com.tokopedia.tokopoints.view.tokopointhome.recommendation.SectionRecomViewBinder
import com.tokopedia.tokopoints.view.tokopointhome.ticker.SectionTickerViewBinder
import com.tokopedia.tokopoints.view.tokopointhome.topads.SectionTopadsViewBinder
import com.tokopedia.tokopoints.view.tokopointhome.topquest.SectionTopQuestViewBinder
import com.tokopedia.tokopoints.view.util.*
import com.tokopedia.tokopoints.view.util.CommonConstant.SectionLayoutType.Companion.QUEST
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.NotificationUnify
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.tp_item_dynamic_action.view.*
import javax.inject.Inject
import kotlin.math.abs

typealias SectionItemBinder = SectionItemViewBinder<Any, RecyclerView.ViewHolder>

/*
 * Dynamic layout params are applied via
 * function setLayoutParams() because configuration in statusBarHeight
 * */
class TokoPointsHomeFragmentNew : BaseDaggerFragment(), TokoPointsHomeContract.View, View.OnClickListener, TokopointPerformanceMonitoringListener, TopSectionVH.CardRuntimeHeightListener, QuestWidgetCallbacks,
    TopSectionVH.RefreshOnTierUpgrade {
    private var mContainerMain: ViewFlipper? = null
    private var mPagerPromos: RecyclerView? = null

    @Inject
    lateinit var viewFactory: ViewModelFactory
    @Inject
    lateinit var trackingQueue: TrackingQueue
    private val mPresenter: TokoPointsHomeViewModel by lazy { ViewModelProviders.of(this, viewFactory).get(TokoPointsHomeViewModel::class.java) }
    private var mValueMembershipDescription: String? = null
    private var appBarCollapseListener: onAppBarCollapseListener? = null
    private var collapsingToolbarLayout: CollapsingToolbarLayout? = null
    private var coordinatorLayout: CoordinatorLayout? = null
    private var statusBarBgView: View? = null
    private var tokoPointToolbar: TokoPointToolbar? = null
    private var serverErrorView: ServerErrorView? = null
    lateinit var appBarHeader: AppBarLayout
    private var pageLoadTimePerformanceMonitoring: PageLoadTimePerformanceInterface? = null
    private val dynamicItem = "dynamicItem"
    private val toolbarItemList = mutableListOf<NotificationUnify>()
    private var adapter: SectionAdapter? = null
    private val viewBinders = mutableMapOf<String, SectionItemBinder>()
    private val sectionList: ArrayList<Any> = ArrayList()
    lateinit var sectionListViewBinder: SectionHorizontalViewBinder
    var listener: RewardsRecomListener? = null
    lateinit var mUsersession: UserSession
    private var questWidgetPosition = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        startPerformanceMonitoring()
        mUsersession = UserSession(context)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.tp_home_layout_container, container, false)
        initViews(view)
        hideStatusBar()
        (activity as BaseSimpleActivity?)!!.setSupportActionBar(tokoPointToolbar)
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar)
        collapsingToolbarLayout?.setExpandedTitleColor(resources.getColor(android.R.color.transparent))
        collapsingToolbarLayout?.setTitle(" ")
        mPagerPromos?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val offset = recyclerView.computeVerticalScrollOffset()
                handleAppBarOffsetChange(offset)
                handleAppBarIconChange(offset)
            }
        })
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stopPreparePagePerformanceMonitoring()
        startNetworkRequestPerformanceMonitoring()
        initListener()
        mPresenter.getTokoPointDetail()
        tokoPointToolbar?.setTitle(R.string.tp_title_tokopoints)
        initObserver()
    }

    private fun initObserver() {
        addTokopointDetailObserver()
        addRewardIntroObserver()
    }

    private fun setLayoutParams(cardheight: Int , heightBackground:Float) {
        val imgEggView = view?.findViewById<AppCompatImageView>(R.id.img_egg)
        val imgBgHeader = view?.findViewById<ImageView>(R.id.img_bg_header)
        val statusBarHeight = getStatusBarHeight(activity)
        val layoutParams = tokoPointToolbar?.layoutParams as FrameLayout.LayoutParams
        layoutParams.topMargin = statusBarHeight
        tokoPointToolbar?.layoutParams = layoutParams
        val imageEggLp = imgEggView?.layoutParams as? RelativeLayout.LayoutParams
        imageEggLp?.topMargin = (statusBarHeight + requireActivity().resources.getDimension(R.dimen.tp_top_margin_big_image)).toInt()
        imgEggView?.layoutParams = imageEggLp
        val imageBigLp = imgBgHeader?.layoutParams as? RelativeLayout.LayoutParams
        imageBigLp?.height = (statusBarHeight + heightBackground + cardheight).toInt()
        imgBgHeader?.layoutParams = imageBigLp
    }

    private fun setStatusBarViewHeight() {
        if (activity != null) statusBarBgView?.layoutParams?.height = getStatusBarHeight(activity)
    }

    @SuppressLint("DeprecatedMethod")
    private fun hideStatusBar() {
        coordinatorLayout?.fitsSystemWindows = false
        coordinatorLayout?.requestApplyInsets()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var flags = coordinatorLayout?.systemUiVisibility
            flags = flags?.or(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
            if (flags != null) {
                coordinatorLayout?.systemUiVisibility = flags
            }
            if (context != null) {
                activity?.window?.statusBarColor = androidx.core.content.ContextCompat.getColor(
                    requireContext(),
                    com.tokopedia.unifyprinciples.R.color.Unify_N0
                )
            }
        }
        activity?.window?.decorView?.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        activity?.window?.statusBarColor = Color.TRANSPARENT
    }

    private fun handleAppBarOffsetChange(offset: Int) {
        val toolbarTransitionRange = (resources.getDimensionPixelSize(R.dimen.tp_home_top_bg_height)
                - tokoPointToolbar!!.height - getStatusBarHeight(activity))
        var offsetAlpha = 255f / toolbarTransitionRange * (toolbarTransitionRange - offset)
        if (offsetAlpha < 0) {
            offsetAlpha = 0f
        }
        if (offsetAlpha >= 255) {
            offsetAlpha = 255f
        }
        var alpha = offsetAlpha / 255 - 1
        if (alpha < 0) alpha *= -1
        statusBarBgView?.alpha = alpha
        if (alpha > 0.5) tokoPointToolbar?.switchToDarkMode() else tokoPointToolbar?.switchToTransparentMode()
        tokoPointToolbar?.applyAlphaToToolbarBackground(alpha)
    }

    private fun handleAppBarIconChange(verticalOffset: Int) {
        val verticalOffset1 = abs(verticalOffset)
        if (verticalOffset1 >= (resources.getDimensionPixelSize(R.dimen.tp_home_top_bg_height))) {
            tokoPointToolbar?.showToolbarIcon()
        } else
            tokoPointToolbar?.hideToolbarIcon()
    }

    private fun addRewardIntroObserver() = mPresenter.rewardIntroData.observe(viewLifecycleOwner, Observer {
        it?.let {
            when (it) {
                is Success -> {
                    showOnBoardingTooltip(it.data.tokopediaRewardIntroPage)
                }
                else -> {}
            }
        }
    })

    private fun addTokopointDetailObserver() = mPresenter.tokopointDetailLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
        it?.let {
            when (it) {
                is Loading -> showLoading()
                is ErrorMessage -> {
                    hideLoading()
                    onError(SHOW_ERROR_TOOLBAR, NetworkDetector.isConnectedToInternet(context))
                }
                is Success -> {
                    hideLoading()
                    stopNetworkRequestPerformanceMonitoring()
                    startRenderPerformanceMonitoring()
                    setOnRecyclerViewLayoutReady()
                    renderRewardUi(
                        it.data.topSectionResponse,
                        it.data.sectionList,
                        it.data.recomData
                    )
                }
                else -> {}
            }
        }
    })

    override fun onError(error: Int, hasInternet: Boolean) {
        if (mContainerMain != null) {
            mContainerMain?.displayedChild = CONTAINER_ERROR
            serverErrorView?.showErrorUi(hasInternet,error)
        }
    }

    private fun initViews(view: View) {
        listener = getRecommendationListener()
        coordinatorLayout = view.findViewById(R.id.container)
        mContainerMain = view.findViewById(R.id.container_main)
        mPagerPromos = view.findViewById(R.id.view_pager_promos)
        appBarHeader = view.findViewById(R.id.app_bar)
        statusBarBgView = view.findViewById(R.id.status_bar_bg)
        tokoPointToolbar = view.findViewById(R.id.toolbar_tokopoint)
        serverErrorView = view.findViewById(R.id.server_error_view)

        setStatusBarViewHeight()
    }

    private fun initListener() {
        if (view == null) {
            return
        }
        view?.findViewById<View>(R.id.img_egg)?.setOnClickListener(this)
        view?.findViewById<View>(R.id.text_membership_value)?.setOnClickListener(this)
        serverErrorView?.setErrorButtonClickListener(View.OnClickListener {
            mPresenter.getTokoPointDetail()
        })
        serverErrorView?.setErrorBackButtonClickListener(View.OnClickListener { activity?.onBackPressed() })
    }

    override fun openWebView(url: String) {
        RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, url)
    }

    private fun showOnBoardingTooltip(data: TokopediaRewardIntroPage?) {
        if (data != null && data.resultStatus?.code == "200") {
            val bundle = Bundle()
            bundle.putParcelable(RewardIntroFragment.INTRO_KEY, data)
            startActivity(RewardIntroActivity.getCallingIntent(requireContext(), bundle))
            activity?.finish()
        } else
            return
    }

    private fun addDynamicToolbar(dynamicActionList: List<DynamicActionListItem?>?) {
        dynamicActionList?.forEachIndexed { index, it ->
            it?.let { item ->
                tokoPointToolbar?.addItem(it)?.apply {
                    toolbarItemList.add(this.notif_dynamic)
                    setOnClickListener {
                        RouteManager.route(context, item.cta?.appLink)
                        hideNotification(index, dynamicActionList)

                        AnalyticsTrackerUtil.sendEvent(
                            AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                            AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                            AnalyticsTrackerUtil.ActionKeys.KEY_EVENT_CLICK_DYNAMICITEM,
                            item.cta?.text ?: "",
                            AnalyticsTrackerUtil.EcommerceKeys.BUSINESSUNIT,
                            AnalyticsTrackerUtil.EcommerceKeys.CURRENTSITE
                        )
                    }
                }
            }
        }
    }

    private fun hideNotification(index: Int, dynamicActionList: List<DynamicActionListItem?>?) {
        toolbarItemList[index].hide()
        dynamicActionList?.get(index)?.counter?.isShowCounter = false
        adapter?.notifyItemChanged(0)
    }

    override fun renderRewardUi(topSectionData: TopSectionResponse?, sections: List<SectionContent>, recommList : RewardsRecommendation?) {

        if (topSectionData?.tokopediaRewardTopSection?.dynamicActionList.isNullOrEmpty() || sections.isEmpty()) {
            onError(SHOW_ERROR_TOOLBAR, true)
            return
        }
        mContainerMain?.displayedChild = CONTAINER_MAIN
        addDynamicToolbar(topSectionData?.tokopediaRewardTopSection?.dynamicActionList)

        if (adapter == null) {
            val topSectionViewBinder = TopSectionViewBinder(topSectionData, this, toolbarItemList)
            @Suppress("UNCHECKED_CAST")
            viewBinders.put(
                    CommonConstant.SectionLayoutType.TOPHEADER,
                    topSectionViewBinder as SectionItemBinder)
            topSectionData?.let { sectionList.add(0,it) }

            if (!sections.isNullOrEmpty()) {
                for (sectionContent in sections) {
                    if (sectionContent.layoutCouponAttr.couponList.isNotEmpty()) {
                        sectionListViewBinder = SectionHorizontalViewBinder()
                        @Suppress("UNCHECKED_CAST")
                        viewBinders.put(
                                sectionContent.layoutType,
                                sectionListViewBinder as SectionItemBinder)
                        sectionList.add(sectionContent)
                    }
                    if (sectionContent.layoutTickerAttr.tickerList.isNotEmpty()) {
                        val sectionTickerViewBinder = SectionTickerViewBinder()
                        @Suppress("UNCHECKED_CAST")
                        viewBinders.put(
                                sectionContent.layoutType,
                                sectionTickerViewBinder as SectionItemBinder)
                        sectionList.add(sectionContent)
                    }

                    if (sectionContent.layoutCategoryAttr.categoryTokopointsList.isNotEmpty()) {
                        val sectionCategoryViewBinder = SectionVerticalCategoryViewBinder()
                        @Suppress("UNCHECKED_CAST")
                        viewBinders.put(
                                sectionContent.layoutType,
                                sectionCategoryViewBinder as SectionItemBinder)
                        sectionList.add(sectionContent)
                    }

                    if (sectionContent.layoutCatalogAttr.catalogList.isNotEmpty()) {
                        val sectionCatalogListViewBinder = SectionHoriZontalCatalogViewBinder(mPresenter)
                        @Suppress("UNCHECKED_CAST")
                        viewBinders.put(
                                sectionContent.layoutType,
                                sectionCatalogListViewBinder as SectionItemBinder)
                        sectionList.add(sectionContent)
                    }

                    if (sectionContent.layoutTopAdsAttr.jsonTopAdsDisplayParam.isNotEmpty()) {

                        val sectionTopAdsViewBinder = SectionTopadsViewBinder()
                        @Suppress("UNCHECKED_CAST")
                        viewBinders.put(
                                sectionContent.layoutType,
                                sectionTopAdsViewBinder as SectionItemBinder)
                        sectionList.add(sectionContent)

                    }

                    if (!sectionContent.layoutMerchantCouponAttr.catalogMVCWithProductsList.isNullOrEmpty()) {

                        val merchantVoucherViewBinder = MerchantVoucherViewBinder()
                        @Suppress("UNCHECKED_CAST")
                        viewBinders.put(
                                sectionContent.layoutType,
                                merchantVoucherViewBinder as SectionItemBinder)
                        sectionList.add(sectionContent)
                    }

                    if (!sectionContent.layoutQuestWidgetAttr.jsonQuestWidgetDisplayParam.isNullOrEmpty()) {
                        // add Quest View binder here
                        val sectionTopQuestViewBinder = SectionTopQuestViewBinder(this)
                        @Suppress("UNCHECKED_CAST")
                        if(sectionList.any { it is SectionContent }){
                            if(sectionContent.layoutType == QUEST) {
                                viewBinders.put(
                                    sectionContent.layoutType,
                                    sectionTopQuestViewBinder as SectionItemBinder
                                )
                                sectionList.add(sectionContent)
                            }
                        }
                    }


                        when (sectionContent.layoutBannerAttr.bannerType) {
                        CommonConstant.BannerType.BANNER_2_1 -> {
                            val verticalImagesViewBinder = SectionVerticalBanner21ViewBinder()
                            @Suppress("UNCHECKED_CAST")
                            viewBinders.put(
                                    sectionContent.layoutBannerAttr.bannerType,
                                    verticalImagesViewBinder as SectionItemBinder)
                            sectionList.add(sectionContent)

                        }
                        CommonConstant.BannerType.BANNER_3_1 -> {
                            val verticalBanner31ViewBinder = SectionVerticalBanner31ViewBinder()
                            @Suppress("UNCHECKED_CAST")
                            viewBinders.put(
                                    sectionContent.layoutBannerAttr.bannerType,
                                    verticalBanner31ViewBinder as SectionItemBinder)
                            sectionList.add(sectionContent)
                        }

                        CommonConstant.BannerType.BANNER_1_1 -> {
                            val verticalBanner11ViewBinder = SectionVerticalBanner11ViewBinder()
                            @Suppress("UNCHECKED_CAST")
                            viewBinders.put(
                                    sectionContent.layoutBannerAttr.bannerType,
                                    verticalBanner11ViewBinder as SectionItemBinder)
                            sectionList.add(sectionContent)
                        }
                        CommonConstant.BannerType.COLUMN_2_1_BY_1,
                        CommonConstant.BannerType.COLUMN_2_3_BY_4,
                        CommonConstant.BannerType.COLUMN_3_1_BY_1 -> {
                            val verticalColumn234ViewBinder = SectionVerticalColumnViewBinder()
                            @Suppress("UNCHECKED_CAST")
                            viewBinders.put(
                                    sectionContent.layoutBannerAttr.bannerType,
                                    verticalColumn234ViewBinder as SectionItemBinder)
                            sectionList.add(sectionContent)
                        }

                        CommonConstant.BannerType.CAROUSEL_1_1,
                        CommonConstant.BannerType.CAROUSEL_2_1,
                        CommonConstant.BannerType.CAROUSEL_3_1 -> {
                            val verticalCarousel31ViewBinder = SectionHorizontalCarouselViewBinder()
                            @Suppress("UNCHECKED_CAST")
                            viewBinders.put(
                                    sectionContent.layoutBannerAttr.bannerType,
                                    verticalCarousel31ViewBinder as SectionItemBinder)
                            sectionList.add(sectionContent)
                        }
                        else -> {
                        }
                    }
                }
            }

            if (recommList?.recommendationWrapper?.isNotEmpty() == true) {
                val sectionRecomViewBinder =
                    recommList?.let { SectionRecomViewBinder(it, listener!!) }
                @Suppress("UNCHECKED_CAST")
                viewBinders.put(
                    CommonConstant.SectionLayoutType.RECOMM,
                    sectionRecomViewBinder as SectionItemBinder
                )
                recommList?.let { sectionList.add(it) }
            }

            adapter = SectionAdapter(viewBinders)
            adapter?.addItem(sectionList)
            mPagerPromos?.apply {
                layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
            }
            mPagerPromos?.setItemViewCacheSize(20)
            mPagerPromos?.isDrawingCacheEnabled = true
            mPagerPromos?.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
            mPagerPromos?.itemAnimator = null
            mPagerPromos?.setHasFixedSize(true)
            if (mPagerPromos?.adapter == null) {
                mPagerPromos?.adapter = adapter
            }
        }

        AnalyticsTrackerUtil.sendEvent(mUsersession.userId,
                AnalyticsTrackerUtil.EventKeys.VIEW_TOKOPOINT_IRIS,
                AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                AnalyticsTrackerUtil.ActionKeys.VIEW_HOMEPAGE,
                "", AnalyticsTrackerUtil.EcommerceKeys.BUSINESSUNIT,
                AnalyticsTrackerUtil.EcommerceKeys.CURRENTSITE)
    }

    override fun onResume() {
        super.onResume()
        AnalyticsTrackerUtil.sendScreenEvent(activity, screenName)

        if(this.questWidgetPosition != -1 && this.questWidgetPosition != sectionList.size - 1
            && this.questWidgetPosition != 0 && (sectionList[questWidgetPosition] as SectionContent).layoutType == QUEST){
            adapter?.notifyItemChanged(this.questWidgetPosition)
        }
    }

    private fun getRecommendationListener(): RewardsRecomListener {

        return object : RewardsRecomListener {

            override fun onProductImpression(item: RecommendationItem, position: Int) {
                val productIdString: String = if (item.productId != null) {
                    item.productId.toString()
                } else {
                    ""
                }
                AnalyticsTrackerUtil.impressionProductRecomItem(
                    productIdString,
                    position,
                    item.recommendationType,
                    item.isTopAds,
                    item.isFreeOngkirActive,
                    "none / other",
                    item.categoryBreadcrumbs,
                    item.name,
                    "none / other",
                    item.price,
                    trackingQueue
                )
            }

            override fun onProductClick(
                item: RecommendationItem,
                layoutType: String?,
                vararg position: Int
            ) {

                val productIdString: String = if (item.productId != null) {
                    item.productId.toString()
                } else {
                    ""
                }
                AnalyticsTrackerUtil.clickProductRecomItem(
                    item.productId.toString(),
                    position[0],
                    item.recommendationType,
                    item.isTopAds,
                    item.isFreeOngkirActive,
                    "none / other",
                    item.categoryBreadcrumbs,
                    item.name,
                    "none / other",
                    item.price
                )

                val intent = RouteManager.getIntent(
                    context,
                    ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                    item.productId.toString()
                )
                if (position.isNotEmpty()) intent.putExtra(
                    PDP_EXTRA_UPDATED_POSITION,
                    position[0]
                )
                this@TokoPointsHomeFragmentNew.startActivity(intent)
            }

            override fun onProductImpression(item: RecommendationItem) {

            }

            override fun onWishlistV2Click(item: RecommendationItem, isAddWishlist: Boolean) {}
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPagerPromos?.adapter = null
        mPagerPromos?.layoutManager = null
        adapter = null
    }

    override fun showLoading() {
        mContainerMain?.displayedChild = CONTAINER_LOADER
    }

    override fun hideLoading() {
        mContainerMain?.displayedChild = CONTAINER_MAIN
    }

    override fun getAppContext(): Context {
        return requireActivity()
    }

    override fun getActivityContext(): Context {
        return requireActivity()
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
        if ( source.id == R.id.img_egg || source.id == R.id.text_membership_value) {
            RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW_TITLE, getString(R.string.tp_label_membership), CommonConstant.WebLink.MEMBERSHIP)
            AnalyticsTrackerUtil.sendEvent(context,
                    AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                    AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                    AnalyticsTrackerUtil.ActionKeys.CLICK_STATUS_MEMBERSHIP,
                    mValueMembershipDescription)


        } else if (source.id == R.id.text_failed_action) {
            mPresenter.getTokoPointDetail()
        }
    }

    companion object {
        private const val CONTAINER_LOADER = 0
        private const val CONTAINER_MAIN = 1
        private const val CONTAINER_ERROR = 2
        private const val SHOW_ERROR_TOOLBAR = 1
        const val PDP_EXTRA_UPDATED_POSITION = "wishlistUpdatedPosition"
        const val REQUEST_FROM_TP_NOTIFICATION = 138

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
        mPagerPromos?.viewTreeObserver
                ?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        if (pageLoadTimePerformanceMonitoring != null) {
                            stopRenderPerformanceMonitoring()
                            stopPerformanceMonitoring()
                        }
                        pageLoadTimePerformanceMonitoring = null
                        mPagerPromos?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
                    }
                })
    }

    override fun setCardLayoutHeight(height: Int , heightBackground: Float) {
        setLayoutParams(height , heightBackground)
    }

    override fun deleteQuestWidget() {
        // delete widget
    }

    override fun updateQuestWidget(position: Int) {
        this.questWidgetPosition = position
    }

    override fun questLogin(){

    }

    override fun refreshReward(popupNotification: PopupNotif?) {
        activity?.let {
            if (!it.isFinishing && !it.isDestroyed) {
                startActivityForResult(
                    Intent(
                        TokopointNotifActivity.getIntent(
                            it,
                            popupNotification
                        )
                    ), REQUEST_FROM_TP_NOTIFICATION
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            REQUEST_FROM_TP_NOTIFICATION -> {
                mPresenter.getTokoPointDetail()
            }
        }
    }

}
