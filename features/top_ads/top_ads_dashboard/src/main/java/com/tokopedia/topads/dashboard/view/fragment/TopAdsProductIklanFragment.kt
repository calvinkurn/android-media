package com.tokopedia.topads.dashboard.view.fragment

import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarRetry
import com.tokopedia.applink.AppUtil
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.config.GlobalConfig
import com.tokopedia.datepicker.range.view.constant.DatePickerConstant
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.topads.auto.view.activity.AutoAdsOnboardingActivity
import com.tokopedia.topads.auto.view.widget.AutoAdsWidget
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.data.internal.AutoAdsStatus.*
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.TopAdsDashboardTracking
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CUSTOM_DATE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.DATE_RANGE_PRODUK
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.END_DATE_PRODUCT
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.SELLER_PACKAGENAME
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.START_DATE_PRODUCT
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardTrackerConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsStatisticsType
import com.tokopedia.topads.dashboard.data.model.AdStatusResponse
import com.tokopedia.topads.dashboard.data.model.AutoAdsResponse
import com.tokopedia.topads.dashboard.data.model.DataStatistic
import com.tokopedia.topads.dashboard.data.model.nongroupItem.GetDashboardProductStatistics
import com.tokopedia.topads.dashboard.data.model.nongroupItem.NonGroupResponse
import com.tokopedia.topads.dashboard.data.utils.Utils
import com.tokopedia.topads.dashboard.data.utils.Utils.format
import com.tokopedia.topads.dashboard.data.utils.Utils.outputFormat
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.adapter.GroupNonGroupPagerAdapter
import com.tokopedia.topads.dashboard.view.adapter.TopAdsStatisticPagerAdapter
import com.tokopedia.topads.dashboard.view.adapter.TopAdsTabAdapter
import com.tokopedia.topads.dashboard.view.adapter.autoads.AutoAdsItemsAdapterTypeFactoryImpl
import com.tokopedia.topads.dashboard.view.adapter.autoads.AutoAdsItemsListAdapter
import com.tokopedia.topads.dashboard.view.adapter.autoads.viewmodel.AutoAdsItemsEmptyViewModel
import com.tokopedia.topads.dashboard.view.adapter.autoads.viewmodel.AutoAdsItemsItemViewModel
import com.tokopedia.topads.dashboard.view.listener.TopAdsDashboardView
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDashboardPresenter
import com.tokopedia.topads.dashboard.view.sheet.CustomDatePicker
import com.tokopedia.topads.dashboard.view.sheet.DatePickerSheet
import com.tokopedia.topads.dashboard.view.sheet.TopadsGroupFilterSheet
import kotlinx.android.synthetic.main.partial_top_ads_dashboard_statistics.*
import kotlinx.android.synthetic.main.topads_dash_auto_ads_onboarding_widget.*
import kotlinx.android.synthetic.main.topads_dash_fragment_beranda_base.*
import kotlinx.android.synthetic.main.topads_dash_fragment_group_detail_view_layout.*
import kotlinx.android.synthetic.main.topads_dash_fragment_product_iklan.*
import kotlinx.android.synthetic.main.topads_dash_fragment_product_iklan.app_bar_layout_2
import kotlinx.android.synthetic.main.topads_dash_fragment_product_iklan.hari_ini
import kotlinx.android.synthetic.main.topads_dash_fragment_product_iklan.swipe_refresh_layout
import kotlinx.android.synthetic.main.topads_dash_fragment_product_iklan.tab_layout
import kotlinx.android.synthetic.main.topads_dash_fragment_product_iklan.view_pager_frag
import kotlinx.android.synthetic.main.topads_dash_layout_common_searchbar_layout.*
import kotlinx.android.synthetic.main.topads_dash_layout_hari_ini.*
import kotlinx.android.synthetic.main.topads_dash_layout_hari_ini.view.*
import kotlinx.android.synthetic.main.topads_dash_product_iklan_empty_view.*
import kotlinx.android.synthetic.main.topads_dash_product_iklan_empty_view.view.*
import java.util.*
import javax.inject.Inject
import kotlin.math.abs

/**
 * Created by hadi.putra on 23/04/18.
 */

private const val CLICK_COBA_SEKARANG = "click - coba sekarang"

class TopAdsProductIklanFragment : BaseDaggerFragment(), TopAdsDashboardView, CustomDatePicker.ActionListener {
    private var adCurrentState = 0
    private var datePickerSheet: DatePickerSheet? = null
    private var groupPagerAdapter: GroupNonGroupPagerAdapter? = null
    private lateinit var autoAdsAdapter: AutoAdsItemsListAdapter
    private var currentPageNum = 1
    private var collapseStateCallBack: AppBarAction? = null
    private val SEVEN_DAYS_RANGE_INDEX = 2
    private lateinit var recyclerviewScrollListener: EndlessRecyclerViewScrollListener
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var imgBg: ConstraintLayout
    private var totalCount = 0
    private var totalPage = 0


    val autoAdsWidget: AutoAdsWidget?
        get() = autoads_edit_widget

    private val pagerAdapter: TopAdsStatisticPagerAdapter? by lazy {
        val fragmentList = listOf(
                TopAdsStatisticImprFragment.createInstance(),
                TopAdsStatisticKlikFragment.createInstance(),
                TopAdsStatisticSpentFragment.createInstance(),
                TopAdsStatisticIncomeFragment.createInstance(),
                TopAdsStatisticCtrFragment.createInstance(),
                TopAdsStatisticConversionFragment.createInstance(),
                TopAdsStatisticAvgFragment.createInstance(),
                TopAdsStatisticSoldFragment.createInstance())
        context?.run { TopAdsStatisticPagerAdapter(this, childFragmentManager, fragmentList) }
    }

    enum class State {
        EXPANDED, COLLAPSED, IDLE
    }

    private var mCurrentState = State.IDLE

    private val topAdsTabAdapter: TopAdsTabAdapter? by lazy {
        context?.run { TopAdsTabAdapter(this) }
    }
    private var snackbarRetry: SnackbarRetry? = null

    internal var startDate: Date? = null
    internal var endDate: Date? = null

    internal var dataStatistic: DataStatistic? = null

    private val groupFilterSheet: TopadsGroupFilterSheet by lazy {
        context.run {
            TopadsGroupFilterSheet.newInstance(context!!)
        }
    }

    @TopAdsStatisticsType
    internal var selectedStatisticType: Int = TopAdsStatisticsType.PRODUCT_ADS

    @Inject
    lateinit var topAdsDashboardPresenter: TopAdsDashboardPresenter

    internal var tracker: TopAdsDashboardTracking? = null

    protected val currentStatisticsFragment: TopAdsDashboardStatisticFragment?
        get() = pagerAdapter?.instantiateItem(pager, topAdsTabAdapter?.selectedTabPosition
                ?: 0) as? TopAdsDashboardStatisticFragment

    override fun getScreenName(): String? = null

    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        activity?.let {
            GraphqlClient.init(it)
            tracker = TopAdsDashboardTracking()
        }
        autoAdsAdapter = AutoAdsItemsListAdapter(AutoAdsItemsAdapterTypeFactoryImpl())

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.topads_dash_fragment_product_iklan, container, false)
        recyclerView = view.findViewById(R.id.auto_ads_list)
        imgBg = view.findViewById(R.id.progressImg)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        topAdsDashboardPresenter.attachView(this)
        initStatisticComponent()

        auto_ad_status_image.setImageDrawable(context?.getResDrawable(R.drawable.ill_iklan_otomatis))
        onBoarding.setOnClickListener {
            if (GlobalConfig.isSellerApp())
                RouteManager.route(activity, ApplinkConstInternalTopAds.TOPADS_AUTOADS_ONBOARDING)
            else {
                if (AppUtil.isSellerInstalled(context)) {
                    val intent = RouteManager.getIntent(context, ApplinkConstInternalTopAds.TOPADS_AUTOADS_ONBOARDING)
                    intent.component = ComponentName(SELLER_PACKAGENAME, AutoAdsOnboardingActivity::class.java.name)
                    startActivity(intent)
                } else {
                    RouteManager.route(context, ApplinkConstInternalMechant.MERCHANT_REDIRECT_CREATE_SHOP)
                }
            }
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsDashboardEvent(CLICK_COBA_SEKARANG, "")
        }
        setDateRangeText(SEVEN_DAYS_RANGE_INDEX)
        startDate = Utils.getStartDate()
        endDate = Utils.getEndDate()
        topAdsDashboardPresenter.saveDate(startDate!!, endDate!!)
        topAdsDashboardPresenter.saveSelectionDatePicker()
        loadData()
        hari_ini?.date_image?.setImageDrawable(context?.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_ic_calendar))
        hari_ini?.next_image?.setImageDrawable(context?.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_ic_arrow))
        hari_ini?.setOnClickListener {
            showBottomSheet()
        }
        btnFilter.setOnClickListener {
            groupFilterSheet.show()
            groupFilterSheet.onSubmitClick = { fetchData() }
        }
        swipe_refresh_layout.setOnRefreshListener {
            loadData()
        }
        snackbarRetry = NetworkErrorHelper.createSnackbarWithAction(activity) { loadData() }
        snackbarRetry?.setColorActionRetry(ContextCompat.getColor(activity!!, com.tokopedia.design.R.color.green_400))

        app_bar_layout_2.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, offset ->
            when {
                offset == 0 -> {
                    if (mCurrentState != State.EXPANDED) {
                        onStateChanged(app_bar_layout_2, State.EXPANDED);
                    }
                    mCurrentState = State.EXPANDED;
                }
                abs(offset) >= app_bar_layout_2.totalScrollRange -> {
                    if (mCurrentState != State.COLLAPSED) {
                        onStateChanged(app_bar_layout_2, State.COLLAPSED);
                    }
                    mCurrentState = State.COLLAPSED;
                }
                else -> {
                    if (mCurrentState != State.IDLE) {
                        onStateChanged(app_bar_layout_2, State.IDLE);
                    }
                    mCurrentState = State.IDLE;
                }
            }
        })
        Utils.setSearchListener(context, view, ::fetchData)
    }

    private fun renderManualViewPager() {
        tab_layout.visibility = View.VISIBLE
        view_pager_frag?.adapter = getViewPagerAdapter()
        view_pager_frag.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tab_layout))
        tab_layout.setupWithViewPager(view_pager_frag)
    }

    private fun getViewPagerAdapter(): GroupNonGroupPagerAdapter? {
        val list: ArrayList<Fragment> = arrayListOf()
        list.add(TopAdsDashGroupFragment())
        list.add(TopAdsDashWithoutGroupFragment())
        val adapter = GroupNonGroupPagerAdapter(childFragmentManager, 0)
        adapter.setData(list)
        groupPagerAdapter = adapter
        return adapter
    }

    private fun setAutoAdsAdapter() {
        layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerviewScrollListener = onRecyclerViewListener()
        recyclerView.adapter = autoAdsAdapter
        recyclerView.layoutManager = layoutManager
        recyclerView.addOnScrollListener(recyclerviewScrollListener)
    }


    private fun onRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if (currentPageNum < totalPage) {
                    currentPageNum++
                    fetchNextPage(currentPageNum)
                }
            }
        }
    }

    private fun fetchNextPage(page: Int) {
        topAdsDashboardPresenter.getGroupProductData(resources, page, null, searchBar?.searchBarTextField?.text.toString(),
                groupFilterSheet.getSelectedSortId(), null, format.format(startDate), format.format(endDate), this::onSuccessResult, this::onEmptyResult)
    }

    private fun showBottomSheet() {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val index = sharedPref?.getInt(DATE_RANGE_PRODUK, 2)
        val customStartDate = sharedPref?.getString(START_DATE_PRODUCT, "")
        val customEndDate = sharedPref?.getString(END_DATE_PRODUCT, "")
        val dateRange: String
        dateRange = if (customStartDate?.isNotEmpty()!!) {
            "$customStartDate - $customEndDate"
        } else
            context?.getString(R.string.topads_dash_custom_date_desc) ?: ""
        datePickerSheet = DatePickerSheet.newInstance(context!!, index ?: 2, dateRange)
        datePickerSheet?.show()
        datePickerSheet?.onItemClick = { date1, date2, position ->
            handleDate(date1, date2, position)
        }
        datePickerSheet?.customDatepicker = {
            startCustomDatePicker()
        }
    }

    private fun startCustomDatePicker() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1)
        val selectDate: String = format.format(calendar.time)
        calendar.add(Calendar.YEAR, -1)
        val date = calendar.time
        val minDate = format.format(date)
        val maxDate: String = format.format(Date())
        val sheet = CustomDatePicker.getInstance(minDate, maxDate, selectDate)
        sheet.setTitle(resources.getString(R.string.topads_dash_choose_date))
        sheet.setListener(this)
        sheet.show(childFragmentManager, "datepicker")
    }

    override fun onDestroy() {
        super.onDestroy()
        if (datePickerSheet != null) {
            datePickerSheet?.dismissDialog()
            datePickerSheet = null
        }
    }

    private fun noAds() {
        /*ad switching in progress*/
        if (STATUS_IN_PROGRESS_ACTIVE == adCurrentState || STATUS_IN_PROGRESS_AUTOMANAGE == adCurrentState || STATUS_IN_PROGRESS_INACTIVE == adCurrentState) {
            app_bar_layout_2.visibility = View.VISIBLE
            autoads_layout.visibility = View.VISIBLE
        } else {
            manualAds()
        }
    }

    private fun setEmptyView() {
        view_pager_frag.visibility = View.GONE
        autoads_layout.visibility = View.GONE
        app_bar_layout_2.visibility = View.GONE
        empty_view.image_empty.setImageDrawable(context?.getResDrawable(R.drawable.topads_dashboard_empty_product))
        empty_view.visibility = View.VISIBLE
        mulai_beriklan.setOnClickListener {
            if (GlobalConfig.isSellerApp())
                RouteManager.route(context, ApplinkConstInternalTopAds.TOPADS_CREATE_ADS)
            else
                openCreateForm()
        }
    }

    private fun noProduct() {
        if (adCurrentState == STATUS_ACTIVE || adCurrentState == STATUS_NOT_DELIVERED) {
            autoAds()
        } else {
            setEmptyView()
        }
    }

    private fun manualAds() {
        empty_view.visibility = View.GONE
        view_pager_frag.visibility = View.VISIBLE
        autoads_layout.visibility = View.GONE
        autoAdsWidget?.visibility = View.GONE
        if (adCurrentState == STATUS_IN_PROGRESS_INACTIVE) {
            imgBg.background = AppCompatResources.getDrawable(context!!, com.tokopedia.topads.auto.R.drawable.topads_blue_bg)
            autoadsDeactivationProgress?.visibility = View.VISIBLE
            autoadsOnboarding.visibility = View.GONE
        } else {
            autoadsDeactivationProgress?.visibility = View.GONE
            autoadsOnboarding.visibility = View.VISIBLE
            renderManualViewPager()
        }
    }


    private fun fetchData() {
        currentPageNum = 1
        autoAdsAdapter.items.clear()
        autoAdsAdapter.notifyDataSetChanged()
        topAdsDashboardPresenter.getGroupProductData(resources, 1, null, searchBar?.searchBarTextField?.text.toString(),
                groupFilterSheet.getSelectedSortId(), null, format.format(startDate), format.format(endDate), this::onSuccessResult, this::onEmptyResult)
    }

    private fun autoAds() {
        setAutoAdsAdapter()
        autoAdsWidget?.loadData(0)
        autoAdsWidget?.visibility = View.VISIBLE
        view_pager_frag.visibility = View.GONE
        autoads_layout.visibility = View.VISIBLE
        tab_layout.visibility = View.GONE
        empty_view.visibility = View.GONE
        autoadsOnboarding.visibility = View.GONE
        fetchData()
    }

    private fun initStatisticComponent() {
        val tabLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        recyclerview_tabLayout.layoutManager = tabLayoutManager
        topAdsTabAdapter?.setListener(object : TopAdsTabAdapter.OnRecyclerTabItemClick {
            override fun onTabItemClick(position: Int) {
                pager.currentItem = position
            }
        })
        recyclerview_tabLayout.adapter = topAdsTabAdapter
        val smoothScroller = object : LinearSmoothScroller(activity!!) {
            override fun getHorizontalSnapPreference(): Int {
                return SNAP_TO_START
            }

            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                return MILLISECONDS_PER_INCH / displayMetrics.densityDpi
            }
        }

        pager.offscreenPageLimit = TopAdsDashboardConstant.OFFSCREEN_PAGE_LIMIT
        initTabLayouTitles()
        pager.adapter = pagerAdapter
        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                smoothScroller.targetPosition = position
                tabLayoutManager.startSmoothScroll(smoothScroller)
                topAdsTabAdapter?.selected(position)
                trackingStatisticBar(position)
                currentStatisticsFragment?.updateDataStatistic(dataStatistic)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    private fun trackingStatisticBar(position: Int) {
        when (position) {
            0 -> onImpressionSelected()
            1 -> onClickSelected()
            2 -> onCtrSelected()
            3 -> onConversionSelected()
            4 -> onAverageConversionSelected()
            5 -> onCostSelected()
            else -> {
            }
        }
    }

    private fun initTabLayouTitles() {
        topAdsTabAdapter?.setSummary(null, resources.getStringArray(R.array.top_ads_tab_statistics_labels))
    }

    private fun openCreateForm() {
        if (AppUtil.isSellerInstalled(context)) {
            val intent = RouteManager.getIntent(context, ApplinkConstInternalTopAds.TOPADS_CREATE_CHOOSER)
            intent.component = ComponentName(SELLER_PACKAGENAME, TopAdsDashboardConstant.SELLER_CREATE_FORM_PATH)
            startActivity(intent)
        } else {
            RouteManager.route(context, ApplinkConstInternalMechant.MERCHANT_REDIRECT_CREATE_SHOP)
        }
    }

    private fun onSuccessResult(response: NonGroupResponse.TopadsDashboardGroupProducts) {
        totalCount = response.meta.page.total
        totalPage = (totalCount / response.meta.page.perPage) + 1
        recyclerviewScrollListener.updateStateAfterGetData()
        loader.visibility = View.GONE
        recyclerviewScrollListener.updateStateAfterGetData()
        val adIds: MutableList<String> = mutableListOf()
        response.data.forEach {
            adIds.add(it.adId.toString())
            autoAdsAdapter.items.add(AutoAdsItemsItemViewModel(it))
        }
        if (adIds.isNotEmpty()) {
            topAdsDashboardPresenter.getProductStats(resources, format.format(startDate), format.format(endDate), adIds, ::OnSuccessStats)
        }
        if (!groupFilterSheet.getFilterCount().isZero()) {
            filterCount.visibility = View.VISIBLE
            filterCount.text = groupFilterSheet.getFilterCount().toString()
        } else
            filterCount.visibility = View.GONE
        groupFilterSheet.removeStatusFilter()
    }

    private fun OnSuccessStats(stats: GetDashboardProductStatistics) {
        autoAdsAdapter.setstatistics(stats.data)
    }

    private fun onEmptyResult() {
        autoAdsAdapter.items.add(AutoAdsItemsEmptyViewModel())
        autoAdsAdapter.notifyDataSetChanged()
    }

    fun loadData() {
        topAdsDashboardPresenter.getAutoAdsStatus(resources)
        loadStatisticsData()
    }

    private fun loadStatisticsData() {
        if (startDate == null || endDate == null) return
        topAdsDashboardPresenter.getTopAdsStatistic(startDate!!, endDate!!, selectedStatisticType, ::onSuccesGetStatisticsInfo)
    }

    private fun handleDate(date1: Long, date2: Long, position: Int) {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putInt(DATE_RANGE_PRODUK, position)
            commit()
        }
        startDate = Date(date1)
        endDate = Date(date2)
        setDateRangeText(position)
        loadData()
    }

    private fun trackingDateTopAds(lastSelection: Int, selectionType: Int) {
        if (selectionType == DatePickerConstant.SELECTION_TYPE_CUSTOM_DATE) {
            tracker?.eventTopAdsShopChooseDateCustom()
        } else if (selectionType == DatePickerConstant.SELECTION_TYPE_PERIOD_DATE) {
            when (lastSelection) {
                0 -> tracker?.eventTopAdsShopDatePeriod(TopAdsDashboardTrackerConstant.PERIOD_OPTION_TODAY)
                1 -> tracker?.eventTopAdsShopDatePeriod(TopAdsDashboardTrackerConstant.PERIOD_OPTION_YESTERDAY)
                2 -> tracker?.eventTopAdsShopDatePeriod(TopAdsDashboardTrackerConstant.PERIOD_OPTION_LAST_7_DAY)
                3 -> tracker?.eventTopAdsShopDatePeriod(TopAdsDashboardTrackerConstant.PERIOD_OPTION_LAST_1_MONTH)
                4 -> tracker?.eventTopAdsShopDatePeriod(TopAdsDashboardTrackerConstant.PERIOD_OPTION_THIS_MONTH)
                else -> {
                }
            }
        }
    }

    override fun onLoadTopAdsShopDepositError(throwable: Throwable) {
        snackbarRetry?.showRetrySnackbar()
    }

    override fun onErrorGetShopInfo(throwable: Throwable) {
        swipe_refresh_layout.isRefreshing = false
        snackbarRetry?.showRetrySnackbar()
    }

    override fun onErrorGetStatisticsInfo(throwable: Throwable) {
        swipe_refresh_layout.isRefreshing = false
        snackbarRetry?.showRetrySnackbar()
    }

    fun onSuccesGetStatisticsInfo(dataStatistic: DataStatistic) {
        swipe_refresh_layout.isRefreshing = false
        snackbarRetry?.hideRetrySnackbar()
        this.dataStatistic = dataStatistic
        if (this.dataStatistic != null) {
            topAdsTabAdapter?.setSummary(dataStatistic.summary, resources.getStringArray(R.array.top_ads_tab_statistics_labels))
        }
        val fragment = pager.adapter?.instantiateItem(pager, pager.currentItem) as? Fragment
        if (fragment != null && fragment is TopAdsDashboardStatisticFragment) {
            fragment.updateDataStatistic(this.dataStatistic)
        }
    }

    override fun onSuccessAdsInfo(data: AutoAdsResponse.TopAdsGetAutoAds.Data) {
        adCurrentState = data.status
        topAdsDashboardPresenter.getAdsStatus(GraphqlHelper.loadRawString(resources, R.raw.query_ads_create_ads_creation_shop_info))
    }

    override fun onSuccessAdStatus(data: AdStatusResponse.TopAdsGetShopInfo.Data) {
        swipe_refresh_layout.isRefreshing = false
        when (data.category) {
            1 -> noProduct()
            2 -> noAds()
            3 -> manualAds()
            4 -> autoAds()
            else -> manualAds()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        topAdsDashboardPresenter.detachView()
    }

    private fun onCostSelected() {
        when (selectedStatisticType) {
            TopAdsStatisticsType.PRODUCT_ADS -> tracker?.eventTopAdsProductStatisticBar(TopAdsDashboardTrackerConstant.STATISTIC_OPTION_CPC)
            TopAdsStatisticsType.SHOP_ADS -> tracker?.eventTopAdsShopStatisticBar(TopAdsDashboardTrackerConstant.STATISTIC_OPTION_CPC)
            else -> {
            }
        }
    }

    private fun onAverageConversionSelected() {
        when (selectedStatisticType) {
            TopAdsStatisticsType.PRODUCT_ADS -> tracker?.eventTopAdsProductStatisticBar(TopAdsDashboardTrackerConstant.STATISTIC_OPTION_AVERAGE_CONVERSION)
            TopAdsStatisticsType.SHOP_ADS -> tracker?.eventTopAdsShopStatisticBar(TopAdsDashboardTrackerConstant.STATISTIC_OPTION_AVERAGE_CONVERSION)
            else -> {
            }
        }
    }

    private fun onConversionSelected() {
        when (selectedStatisticType) {
            TopAdsStatisticsType.PRODUCT_ADS -> tracker?.eventTopAdsProductStatisticBar(TopAdsDashboardTrackerConstant.STATISTIC_OPTION_CONVERSION)
            TopAdsStatisticsType.SHOP_ADS -> tracker?.eventTopAdsShopStatisticBar(TopAdsDashboardTrackerConstant.STATISTIC_OPTION_CONVERSION)
            else -> {
            }
        }
    }

    private fun onCtrSelected() {
        when (selectedStatisticType) {
            TopAdsStatisticsType.PRODUCT_ADS -> tracker?.eventTopAdsProductStatisticBar(TopAdsDashboardTrackerConstant.STATISTIC_OPTION_CTR)
            TopAdsStatisticsType.SHOP_ADS -> tracker?.eventTopAdsShopStatisticBar(TopAdsDashboardTrackerConstant.STATISTIC_OPTION_CTR)
            else -> {
            }
        }
    }

    private fun onClickSelected() {
        when (selectedStatisticType) {
            TopAdsStatisticsType.PRODUCT_ADS -> tracker?.eventTopAdsProductStatisticBar(TopAdsDashboardTrackerConstant.STATISTIC_OPTION_CLICK)
            TopAdsStatisticsType.SHOP_ADS -> tracker?.eventTopAdsShopStatisticBar(TopAdsDashboardTrackerConstant.STATISTIC_OPTION_CLICK)
            else -> {
            }
        }
    }

    private fun onImpressionSelected() {
        when (selectedStatisticType) {
            TopAdsStatisticsType.PRODUCT_ADS -> tracker?.eventTopAdsProductStatisticBar(TopAdsDashboardTrackerConstant.STATISTIC_OPTION_IMPRESSION)
            TopAdsStatisticsType.SHOP_ADS -> tracker?.eventTopAdsShopStatisticBar(TopAdsDashboardTrackerConstant.STATISTIC_OPTION_IMPRESSION)
            else -> {
            }
        }
    }

    companion object {
        val MILLISECONDS_PER_INCH = 200f

        fun createInstance(): TopAdsProductIklanFragment {
            return TopAdsProductIklanFragment()
        }
    }

    fun setGroupCount(size: Int) {
        groupPagerAdapter?.setTitleGroup(String.format(getString(R.string.topads_dash_group), size))
    }

    fun setNonGroupCount(size: Int) {
        groupPagerAdapter?.setTitleProduct(String.format(getString(R.string.topads_dash_non_group), size))
    }

    override fun onCustomDateSelected(dateStart: Date, dateEnd: Date) {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putInt(DATE_RANGE_PRODUK, CUSTOM_DATE)
            commit()
        }
        startDate = dateStart
        with(sharedPref.edit()) {
            putString(START_DATE_PRODUCT, outputFormat.format(startDate))
            commit()
        }
        endDate = dateEnd
        with(sharedPref.edit()) {
            putString(END_DATE_PRODUCT, outputFormat.format(endDate))
            commit()
        }
        setDateRangeText(CUSTOM_DATE)
        loadData()
    }

    private fun setDateRangeText(position: Int) {
        when (position) {
            1 -> current_date.text = context?.getString(com.tokopedia.datepicker.range.R.string.yesterday)
            0 -> current_date.text = context?.getString(R.string.topads_dash_hari_ini)
            2 -> current_date.text = context?.getString(com.tokopedia.datepicker.range.R.string.seven_days_ago)
            else -> {
                val text = outputFormat.format(startDate) + " - " + outputFormat.format(endDate)
                current_date.text = text
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AppBarAction) {
            collapseStateCallBack = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        collapseStateCallBack = null
    }

    private fun onStateChanged(appBarLayout: AppBarLayout?, state: State?) {
        collapseStateCallBack?.setAppBarState(state)
        swipe_refresh_layout.isEnabled = state == State.EXPANDED
    }

    interface AppBarAction {
        fun setAppBarState(state: State?)
    }
}
