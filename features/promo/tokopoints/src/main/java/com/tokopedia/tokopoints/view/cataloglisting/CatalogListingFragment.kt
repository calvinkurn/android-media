package com.tokopedia.tokopoints.view.cataloglisting

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.view.*
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.di.TokopointBundleComponent
import com.tokopedia.tokopoints.view.couponlisting.CouponListingStackedActivity.Companion.getCallingIntent
import com.tokopedia.tokopoints.view.customview.ServerErrorView
import com.tokopedia.tokopoints.view.firebaseAnalytics.TokopointPerformanceConstant.CataloglistPlt.Companion.CATALOGLIST_TOKOPOINT_PLT
import com.tokopedia.tokopoints.view.firebaseAnalytics.TokopointPerformanceConstant.CataloglistPlt.Companion.CATALOGLIST_TOKOPOINT_PLT_NETWORK_METRICS
import com.tokopedia.tokopoints.view.firebaseAnalytics.TokopointPerformanceConstant.CataloglistPlt.Companion.CATALOGLIST_TOKOPOINT_PLT_PREPARE_METRICS
import com.tokopedia.tokopoints.view.firebaseAnalytics.TokopointPerformanceConstant.CataloglistPlt.Companion.CATALOGLIST_TOKOPOINT_PLT_RENDER_METRICS
import com.tokopedia.tokopoints.view.firebaseAnalytics.TokopointPerformanceMonitoringListener
import com.tokopedia.tokopoints.view.interfaces.onAppBarCollapseListener
import com.tokopedia.tokopoints.view.model.CatalogBanner
import com.tokopedia.tokopoints.view.model.CatalogFilterBase
import com.tokopedia.tokopoints.view.model.CatalogSubCategory
import com.tokopedia.tokopoints.view.util.*
import com.tokopedia.unifycomponents.PageControl
import javax.inject.Inject

class CatalogListingFragment : BaseDaggerFragment(), CatalogListingContract.View, View.OnClickListener, TokopointPerformanceMonitoringListener {
    private var mContainerMain: ViewFlipper? = null
    private var mPagerSortType: ViewPager? = null
    private var mTabSortType: TabLayout? = null
    private var mTextPoints: TextView? = null
    private var mTextPointsBottom: TextView? = null
    private var mViewPagerAdapter: CatalogSortTypePagerAdapter? = null
    var mFlashTimer: CountDownTimer? = null
    private var mAppBarHeader: AppBarLayout? = null

    @Inject
    lateinit var factory: ViewModelFactory

    private val mViewModel: CatalogListingViewModel by lazy { ViewModelProviders.of(this, factory).get(CatalogListingViewModel::class.java) }

    private var bottomViewMembership: LinearLayout? = null
    private var mContainerPointDetail: ConstraintLayout? = null
    private var appBarCollapseListener: onAppBarCollapseListener? = null
    private var isPointsAvailable = false
    private var menuItemFilter: MenuItem? = null
    private var serverErrorView: ServerErrorView? = null
    private var pageLoadTimePerformanceMonitoring: PageLoadTimePerformanceInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        startPerformanceMonitoring()
        super.onCreate(savedInstanceState)
    }

    override fun onErrorFilter(errorMessage: String, hasInternet: Boolean) {
        mContainerMain!!.displayedChild = CONTAINER_ERROR
        serverErrorView!!.showErrorUi(hasInternet)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initInjector()
        val view = inflater.inflate(R.layout.tp_fragment_catalog_listing, container, false)
        initViews(view)
        setHasOptionsMenu(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        serverErrorView = view.findViewById(R.id.server_error_view)

        stopPreparePagePerformanceMonitoring()
        startNetworkRequestPerformanceMonitoring()
        initListener()
        requestHomePageData()
        initObserver()
    }

    private fun initObserver() {
        addBannerOberser()
        addFlterObserver()
        addPointObserver()
    }

    private fun addPointObserver() = mViewModel.pointLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
        it?.let {
            when (it) {
                is ErrorMessage -> onErrorPoint(it.data)
                is Success -> onSuccessPoints(it.data.points.rewardStr,
                        it.data.points.reward,
                        it.data.tier.nameDesc,
                        it.data.tier.eggImageUrl)
            }
        }
    })

    private fun addFlterObserver() = mViewModel.filterLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
        it?.let {
            when (it) {
                is Loading -> showLoader()
                is ErrorMessage -> onErrorFilter(it.data, NetworkDetector.isConnectedToInternet(context))
                is Success -> {
                    stopNetworkRequestPerformanceMonitoring()
                    startRenderPerformanceMonitoring()
                    onSuccessFilter(it.data)
                    stopRenderPerformanceMonitoring()
                    stopPerformanceMonitoring()
                }
            }
        }
    })

    private fun addBannerOberser() = mViewModel.bannerLiveDate.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
        it?.let {
            when (it) {
                is Success -> onSuccessBanners(it.data.banners)
                is ErrorMessage -> onErrorBanners(it.data)
            }
        }
    })

    override fun onDestroy() {
        super.onDestroy()
        if (mFlashTimer != null) {
            mFlashTimer!!.cancel()
            mFlashTimer = null
        }
    }

    override fun onResume() {
        super.onResume()
        AnalyticsTrackerUtil.sendScreenEvent(activity, screenName)
    }

    override fun getAppContext(): Context {
        return context!!
    }

    override fun refreshTab() {
           val fragment = mViewPagerAdapter!!.getRegisteredFragment(mPagerSortType!!.currentItem) as CatalogListItemFragment?
           if (fragment != null && fragment.isAdded) {
               fragment.viewModel.pointRange = mViewModel.pointRangeId
               fragment.getCatalogList(mViewModel.currentCategoryId, mViewModel.currentSubCategoryId)
           }
    }

    override fun showLoader() {
        mContainerMain!!.displayedChild = CONTAINER_LOADER
    }

    override fun onErrorBanners(errorMessage: String) {}
    override fun onSuccessBanners(banners: List<CatalogBanner>) {
        hideLoader()
        if (banners == null || banners.isEmpty()) {
            return
        }
        val pager: ViewPager = view!!.findViewById(R.id.view_pager_banner)
        pager.adapter = CatalogBannerPagerAdapter(context, banners, this)
        //adding bottom dots(Page Indicator)
        val pageIndicator: PageControl? = view?.findViewById(R.id.page_indicator)
        pageIndicator?.setCurrentIndicator(0)
        pageIndicator?.setIndicator(banners.size)
        view!!.findViewById<View>(R.id.container_pager).visibility = View.VISIBLE
        mAppBarHeader!!.addOnOffsetChangedListener(offsetChangedListenerAppBarElevation)
    }

    override fun onSuccessPoints(rewardStr: String, rewardValue: Int, membership: String, eggUrl: String) {
        if (!rewardStr.isEmpty()) mTextPoints!!.text = rewardStr
        mTextPointsBottom!!.text = CurrencyHelper.convertPriceValue(rewardValue.toDouble(), false)
        isPointsAvailable = true
        mAppBarHeader!!.addOnOffsetChangedListener(offsetChangedListenerAppBarElevation)
    }

    override fun onErrorPoint(errorMessage: String) {}

    override fun onSuccessFilter(filters: CatalogFilterBase) {
        hideLoader()
        //Setting up subcategories types tabs
        if (filters == null || filters.categories == null || filters.categories.isEmpty()) { //To ensure get data loaded for very first time for first fragment(Providing a small to ensure fragment get displayed).
            mViewPagerAdapter = CatalogSortTypePagerAdapter(childFragmentManager, filters.categories[0].id, null)
            mViewPagerAdapter!!.setPointsAvailable(isPointsAvailable)
            //TODO please replace with
            mViewModel.currentCategoryId = 0
            mViewModel.currentSubCategoryId = 0
            mPagerSortType!!.postDelayed({ refreshTab() }, CommonConstant.TAB_SETUP_DELAY_MS.toLong())
            mTabSortType!!.visibility = View.GONE
        } else if (filters.categories[0] != null
                && (filters.categories[0].isHideSubCategory || filters.categories[0].subCategory == null || filters.categories[0].subCategory.isEmpty())) {
            mViewPagerAdapter = CatalogSortTypePagerAdapter(childFragmentManager, filters.categories[0].id, null)
            mViewPagerAdapter!!.setPointsAvailable(isPointsAvailable)
            mPagerSortType!!.adapter = mViewPagerAdapter
            mTabSortType!!.setupWithViewPager(mPagerSortType)
            mTabSortType!!.visibility = View.GONE
            if (TextUtils.isEmpty(filters.categories[0].name)) {
                updateToolbarTitle("Semua Kupon")
            } else {
                updateToolbarTitle(filters.categories[0].name)
            }
            mViewModel.currentCategoryId = filters.categories[0].id
            mViewModel.currentSubCategoryId = 0
            mPagerSortType!!.postDelayed({ refreshTab() }, CommonConstant.TAB_SETUP_DELAY_MS.toLong())
        } else if (filters.categories[0] != null
                && filters.categories[0].subCategory != null) {
            mTabSortType!!.visibility = View.VISIBLE
            updateToolbarTitle(filters.categories[0].name)
            mViewPagerAdapter = CatalogSortTypePagerAdapter(childFragmentManager, filters.categories[0].id, filters.categories[0].subCategory)
            mViewPagerAdapter!!.setPointsAvailable(isPointsAvailable)
            mPagerSortType!!.adapter = mViewPagerAdapter
            mTabSortType!!.setupWithViewPager(mPagerSortType)
            mPagerSortType!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
                override fun onPageSelected(position: Int) {
                    AnalyticsTrackerUtil.sendEvent(context,
                            AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                            AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS_PENUKARAN_POINT,
                            "click " + filters.categories[0].subCategory[position].name,
                            "")
                    val fragment = mViewPagerAdapter!!.getRegisteredFragment(position) as CatalogListItemFragment?
                    if (fragment != null
                            && fragment.isAdded) {
                        mViewModel.currentCategoryId = filters.categories[0].id
                        mViewModel.currentSubCategoryId = filters.categories[0].subCategory[position].id
                        fragment.viewModel.pointRange = mViewModel.pointRangeId
                        fragment.getCatalogList(mViewModel.currentCategoryId, mViewModel.currentSubCategoryId)
                    }
                }

                override fun onPageScrollStateChanged(state: Int) {}
            })
            //excluding extra padding from tabs
            TabUtil.wrapTabIndicatorToTitle(mTabSortType,
                    resources.getDimension(R.dimen.tp_margin_medium).toInt(),
                    resources.getDimension(R.dimen.tp_margin_regular).toInt())
            mPagerSortType!!.postDelayed({
                val selectedTabIndex = getSelectedCategoryIndex(filters.categories[0].subCategory)
                if (selectedTabIndex == 0) { // Special handling for zeroth index
                    mViewModel.currentCategoryId = filters.categories[0].id
                    mViewModel.currentSubCategoryId = filters.categories[0].subCategory[0].id
                    refreshTab()
                } else {
                    mPagerSortType!!.setCurrentItem(selectedTabIndex, false)
                }
            }, CommonConstant.TAB_SETUP_DELAY_MS.toLong())
        }
    }

    override fun hideLoader() {
        mContainerMain!!.displayedChild = CONTAINER_DATA
    }

    override fun gotoMyCoupons() {
        startActivity(getCallingIntent(context!!))
        AnalyticsTrackerUtil.sendEvent(activityContext,
                AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS_PENUKARAN_POINT,
                AnalyticsTrackerUtil.ActionKeys.CLICK_SELL_ALL_COUPON,
                "")
    }

    override fun getActivityContext(): Context {
        return activity!!
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CatalogListingActivity) appBarCollapseListener = context
    }

    override fun getScreenName(): String {
        return AnalyticsTrackerUtil.ScreenKeys.CATALOG_LISTING_SCREEN_NAME
    }

    override fun initInjector() {
        getComponent(TokopointBundleComponent::class.java).inject(this)
    }

    override fun onClick(source: View) {
        if (source.id == R.id.text_my_coupon) {
            gotoMyCoupons()
        } else if (source.id == R.id.text_failed_action) {
            requestHomePageData()
            mViewModel!!.getPointData()
        } else if (source.id == R.id.text_membership_label
                || source.id == R.id.bottom_view_membership) {
            RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, CommonConstant.WebLink.MEMBERSHIP, getString(R.string.tp_label_membership))
            AnalyticsTrackerUtil.sendEvent(source.context,
                    AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                    AnalyticsTrackerUtil.CategoryKeys.PENUKARAN_POINT,
                    AnalyticsTrackerUtil.ActionKeys.CLICK_MEM_BOTTOM,
                    "")
        }
    }

    private fun requestHomePageData() {
        if (isSeeAllPage) {
            mViewModel.getHomePageData("", "", false)
        } else {
            mViewModel.getPointData()
            mViewModel.getHomePageData(arguments?.getString(CommonConstant.ARGS_SLUG_CATEGORY),
                    arguments?.getString(CommonConstant.ARGS_SLUG_SUB_CATEGORY), false)
        }
    }

    private fun initViews(view: View) {
        mContainerMain = view.findViewById(R.id.container_main)
        mPagerSortType = view.findViewById(R.id.view_pager_sort_type)
        mTabSortType = view.findViewById(R.id.tabs_sort_type)
        mTextPoints = view.findViewById(R.id.text_point_value)
        bottomViewMembership = view.findViewById(R.id.bottom_view_membership)
        mContainerPointDetail = view.findViewById(R.id.container_point_detail)
        mTextPointsBottom = view.findViewById(R.id.text_my_points_value_bottom)
        mAppBarHeader = view.findViewById(R.id.app_bar_header)
        appBarCollapseListener!!.hideToolbarElevation()
        if (arguments != null && arguments!!.getInt(CommonConstant.EXTRA_COUPON_COUNT) <= 0) {
            view.findViewById<View>(R.id.text_my_coupon).visibility = View.GONE
        }
    }

    var offsetChangedListenerAppBarElevation = OnOffsetChangedListener { appBarLayout, verticalOffset ->
        var verticalOffset = verticalOffset
        if (appBarCollapseListener != null) {
            verticalOffset = Math.abs(verticalOffset)
            if (verticalOffset >= appBarLayout.totalScrollRange) { //Appbar is hidden now
                appBarCollapseListener!!.hideToolbarElevation()
            } else {
                appBarCollapseListener!!.showToolbarElevation()
            }
        }
    }

    private fun initListener() {
        if (view == null) {
            return
        }
        view!!.findViewById<View>(R.id.text_my_coupon).setOnClickListener(this)
        view!!.findViewById<View>(R.id.text_failed_action).setOnClickListener(this)
        bottomViewMembership!!.setOnClickListener(this)
        mTextPointsBottom!!.setOnClickListener(this)
    }

    override fun openWebView(url: String) {
        if (context != null) context!!.startActivity(RouteManager.getIntent(activityContext, url))
    }

    private fun updateToolbarTitle(title: String?) {
        if (activity != null && title != null) {
            (activity as BaseSimpleActivity?)!!.updateTitle(title)
        }
    }

    private val isSeeAllPage: Boolean
        get() = (arguments == null
                || (arguments!!.getString(CommonConstant.ARGS_SLUG_CATEGORY)).isNullOrEmpty())

    private fun getSelectedCategoryIndex(data: List<CatalogSubCategory>): Int {
        var counter = 0
        for (item in data) {
            if (item.isSelected) {
                break
            }
            counter++
        }
        return counter
    }

    companion object {
        private const val CONTAINER_LOADER = 0
        private const val CONTAINER_DATA = 1
        private const val CONTAINER_ERROR = 2
        private const val CONTAINER_EMPTY = 3
        fun newInstance(extras: Bundle?): Fragment {
            val fragment: Fragment = CatalogListingFragment()
            fragment.arguments = extras
            return fragment
        }
    }

    override fun startPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring = PageLoadTimePerformanceCallback(
                CATALOGLIST_TOKOPOINT_PLT_PREPARE_METRICS,
                CATALOGLIST_TOKOPOINT_PLT_NETWORK_METRICS,
                CATALOGLIST_TOKOPOINT_PLT_RENDER_METRICS,
                0,
                0,
                0,
                0,
                null
        )

        pageLoadTimePerformanceMonitoring?.startMonitoring(CATALOGLIST_TOKOPOINT_PLT)
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