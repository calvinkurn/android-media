package com.tokopedia.topads.dashboard.view.fragment

import android.content.Context
import android.graphics.drawable.VectorDrawable
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarRetry
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.constant.TopAdsFeature
import com.tokopedia.topads.common.data.internal.AutoAdsStatus.*
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.internal.ParamObject.AD_TYPE_PRODUCT_ADS
import com.tokopedia.topads.common.data.internal.ParamObject.KEY_AD_TYPE
import com.tokopedia.topads.common.data.model.WhiteListUserResponse
import com.tokopedia.topads.common.data.response.AutoAdsResponse
import com.tokopedia.topads.common.data.response.nongroupItem.GetDashboardProductStatistics
import com.tokopedia.topads.common.data.response.nongroupItem.NonGroupResponse
import com.tokopedia.topads.common.view.widget.AutoAdsWidgetCommon
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_1
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_2
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_3
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_4
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.DIHAPUS
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.GRUP
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.TANPA_GRUP
import com.tokopedia.topads.dashboard.data.constant.TopAdsStatisticsType
import com.tokopedia.topads.dashboard.data.model.AdStatusResponse
import com.tokopedia.topads.dashboard.data.model.DataStatistic
import com.tokopedia.topads.dashboard.data.model.FragmentTabItem
import com.tokopedia.topads.dashboard.data.utils.Utils
import com.tokopedia.topads.dashboard.data.utils.Utils.format
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.activity.TopAdsDashboardActivity
import com.tokopedia.topads.dashboard.view.adapter.TopAdsDashboardBasePagerAdapter
import com.tokopedia.topads.dashboard.view.adapter.autoads.AutoAdsItemsAdapterTypeFactoryImpl
import com.tokopedia.topads.dashboard.view.adapter.autoads.AutoAdsItemsListAdapter
import com.tokopedia.topads.dashboard.view.adapter.autoads.viewmodel.AutoAdsItemsEmptyModel
import com.tokopedia.topads.dashboard.view.adapter.autoads.viewmodel.AutoAdsItemsItemModel
import com.tokopedia.topads.dashboard.view.listener.TopAdsDashboardView
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDashboardPresenter
import com.tokopedia.topads.dashboard.view.sheet.DatePickerSheet
import com.tokopedia.topads.dashboard.view.sheet.TopadsGroupFilterSheet
import com.tokopedia.unifycomponents.*
import com.tokopedia.unifyprinciples.Typography
import java.util.*
import javax.inject.Inject
import kotlin.math.abs

/**
 * Created by hadi.putra on 23/04/18.
 */

private const val CLICK_COBA_SEKARANG = "click - coba sekarang"
private const val CLICK_COBA_AUO_ADS = "click - coba auto ads"
private const val CLICK_DATE_PICKER = "click - date filter dashboard iklan produk"
private const val CLICK_TANPA_GRUP = "click - tab iklan tanpa group"
private const val CLICK_MULAI_BERIKLAN = "click - mulai beriklan iklan produk dashboard"
private const val DEFAULT_FRAGMENT_LOAD_COUNT = 2


class TopAdsProductIklanFragment : TopAdsBaseTabFragment(), TopAdsDashboardView {

    private var searchBar: SearchBarUnify? = null
    private var filterCount: Typography? = null
    private var pager: ViewPager? = null
    private var loader: LoaderUnify? = null
    private var swipeRefreshLayout: SwipeToRefresh? = null
    private var emptyView: ConstraintLayout? = null
    private var progressView: LinearLayout? = null
    private var description: Typography? = null
    private var btnReload: UnifyButton? = null
    private var viewPagerFrag: ViewPager? = null
    private var autoadsLayout: LinearLayoutCompat? = null
    private var appBarLayout2: AppBarLayout? = null
    private var autoadsOnboarding: CardUnify? = null
    private var autoadsDeactivationProgress: CardUnify? = null
    private var autoadsEditWidget: AutoAdsWidgetCommon? = null
    private var graphLayout: CardUnify? = null
    private var tabLayout: TabsUnify? = null

    private var adCurrentState = 0
    private var datePickerSheet: DatePickerSheet? = null
    private var currentDateText: String = ""

    override fun getLayoutId(): Int {
        return R.layout.topads_dash_fragment_product_iklan
    }

    override fun setUpView(view: View) {
        recyclerView = view.findViewById(R.id.auto_ads_list)
        imgBg = view.findViewById(R.id.progressImg)
        searchBar = view.findViewById(R.id.searchBar)
        filterCount = view.findViewById(R.id.filterCount)
        pager = view.findViewById(R.id.pager)
        loader = view.findViewById(R.id.loader)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        emptyView = view.findViewById(R.id.empty_view)
        progressView = view.findViewById(R.id.progressView)
        description = view.findViewById(R.id.description)
        btnReload = view.findViewById(R.id.btnReload)
        viewPagerFrag = view.findViewById(R.id.view_pager_frag)
        autoadsLayout = view.findViewById(R.id.autoads_layout)
        appBarLayout2 = view.findViewById(R.id.app_bar_layout_2)
        autoadsOnboarding = view.findViewById(R.id.autoadsOnboarding)
        autoadsDeactivationProgress = view.findViewById(R.id.autoadsDeactivationProgress)
        autoadsEditWidget = view.findViewById(R.id.autoads_edit_widget)
        graphLayout = view.findViewById(R.id.graph_layout)
        tabLayout = view.findViewById(R.id.tab_layout)
    }

    override fun getChildScreenName(): String {
        return TopAdsProductIklanFragment::class.java.name
    }

    override fun loadChildStatisticsData() {
        fetchData()
        loadStatisticsData()
    }

    override fun renderGraph() {
        currentStatisticsFragment?.showLineGraph(dataStatistic)
    }

    override fun getCustomDateText(customDateText: String) {
        currentDateText = customDateText
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsGroupEvent(CLICK_DATE_PICKER,
            customDateText)
    }

    private var groupPagerAdapter: TopAdsDashboardBasePagerAdapter? = null
    private lateinit var autoAdsAdapter: AutoAdsItemsListAdapter
    private var currentPageNum = 1
    private var collapseStateCallBack: AppBarAction? = null
    private var adTypeCallBack: AdInfo? = null
    private lateinit var recyclerviewScrollListener: EndlessRecyclerViewScrollListener
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var imgBg: ConstraintLayout
    private var totalCount = 0
    private var totalPage = 0

    private val autoAdsWidget: AutoAdsWidgetCommon?
        get() = autoadsEditWidget

    enum class State {
        EXPANDED, COLLAPSED, IDLE
    }

    private var mCurrentState = State.IDLE
    private var snackbarRetry: SnackbarRetry? = null
    private var dataStatistic: DataStatistic? = null

    private val groupFilterSheet: TopadsGroupFilterSheet by lazy {
        context.run {
            TopadsGroupFilterSheet.newInstance(this)
        }
    }

    @TopAdsStatisticsType
    internal var selectedStatisticType: Int = TopAdsStatisticsType.PRODUCT_ADS

    @Inject
    lateinit var topAdsDashboardPresenter: TopAdsDashboardPresenter


    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        autoAdsAdapter = AutoAdsItemsListAdapter(AutoAdsItemsAdapterTypeFactoryImpl())

        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        topAdsDashboardPresenter.attachView(this)
        view.findViewById<ImageUnify>(R.id.auto_ad_status_image)
            ?.setImageDrawable(context?.getResDrawable(R.drawable.ill_iklan_otomatis))
        view.findViewById<UnifyButton>(R.id.onBoarding)?.setOnClickListener {
            RouteManager.route(activity, ApplinkConstInternalTopAds.TOPADS_AUTOADS_ONBOARDING)
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsDashboardEvent(CLICK_COBA_SEKARANG,
                "")
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsGroupEvent(
                CLICK_COBA_AUO_ADS, "")
        }
        loadData()
        view.findViewById<UnifyImageButton>(R.id.btnFilter)?.setOnClickListener {
            groupFilterSheet.show(childFragmentManager, "")
            groupFilterSheet.onSubmitClick = { fetchData() }
        }
        swipeRefreshLayout?.setOnRefreshListener {
            loadData()
        }
        activity?.run {
            snackbarRetry = NetworkErrorHelper.createSnackbarWithAction(this) { loadData() }
            snackbarRetry?.setColorActionRetry(ContextCompat.getColor(this,
                com.tokopedia.abstraction.R.color.Unify_GN500))
        }

        appBarLayout2?.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, offset ->
            when {
                offset == 0 -> {
                    if (mCurrentState != State.EXPANDED) {
                        onStateChanged(State.EXPANDED)
                    }
                    mCurrentState = State.EXPANDED
                }
                abs(offset) >= appBarLayout.totalScrollRange -> {
                    if (mCurrentState != State.COLLAPSED) {
                        onStateChanged(State.COLLAPSED)
                    }
                    mCurrentState = State.COLLAPSED
                }
                else -> {
                    if (mCurrentState != State.IDLE) {
                        onStateChanged(State.IDLE)
                    }
                    mCurrentState = State.IDLE
                }
            }
        })
        tabLayout?.getUnifyTabLayout()
            ?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab?) {}
                override fun onTabUnselected(tab: TabLayout.Tab?) {}
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when (tab?.position) {
                        CONST_1 -> {
                            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsGroupEvent(
                                CLICK_TANPA_GRUP,
                                "")
                        }
                    }
                }
            })
        Utils.setSearchListener(context, view, ::fetchData)
    }

    private fun renderManualViewPager() {
        viewPagerFrag?.adapter = getViewPagerAdapter()
        viewPagerFrag?.offscreenPageLimit = fragmentLoadCountThree
        viewPagerFrag?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {}

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}

            override fun onPageSelected(position: Int) {
                loadStatisticsData()
            }
        })
        viewPagerFrag?.let { tabLayout?.setupWithViewPager(it) }
    }

    private fun prepareBundle(): Bundle {
        val bundle = Bundle()
        bundle.putString(KEY_AD_TYPE, AD_TYPE_PRODUCT_ADS)
        return bundle
    }

    private fun getViewPagerAdapter(): TopAdsDashboardBasePagerAdapter {
        val list: ArrayList<FragmentTabItem> = arrayListOf()
        tabLayout?.getUnifyTabLayout()?.removeAllTabs()
        tabLayout?.tabLayout?.tabMode = TabLayout.MODE_SCROLLABLE
        tabLayout?.addNewTab(GRUP)
        tabLayout?.addNewTab(TANPA_GRUP)
        tabLayout?.addNewTab(DIHAPUS)
        list.add(FragmentTabItem(GRUP, TopAdsDashGroupFragment.createInstance(prepareBundle())))
        list.add(FragmentTabItem(TANPA_GRUP,
            TopAdsDashWithoutGroupFragment.createInstance(prepareBundle())))
        list.add(FragmentTabItem(DIHAPUS,
            TopAdsDashDeletedGroupFragment.createInstance(prepareBundle())))
        val adapter = TopAdsDashboardBasePagerAdapter(childFragmentManager, 0)
        adapter.setList(list)
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
        topAdsDashboardPresenter.getGroupProductData(page,
            null, searchBar?.searchBarTextField?.text.toString(),
            groupFilterSheet.getSelectedSortId(), null,
            format.format(startDate ?: Date()), format.format(endDate ?: Date()),
            0, this::onSuccessResult, this::onEmptyResult)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (datePickerSheet != null) {
            datePickerSheet?.dismissDialog()
            datePickerSheet = null
        }
    }

    private fun noAds() {
        progressView?.gone()
        /*ad switching in progress*/
        adTypeCallBack?.adInfo(MANUAL_AD)
        if (checkInProgress()) {
            showProgressLayout()
        } else {
            setNoAdsView()
        }
    }

    private fun isAttached(): Boolean {
        return activity != null && isAdded
    }

    private fun showProgressLayout() {
        btnReload?.setOnClickListener {
            swipeRefreshLayout?.isRefreshing = true
            loadData()
        }
        if (STATUS_IN_PROGRESS_ACTIVE == adCurrentState)
            description?.text = getString(R.string.topads_dash_auto_ads_enable_msg)
        else if (STATUS_IN_PROGRESS_INACTIVE == adCurrentState)
            description?.text = getString(R.string.topads_dash_auto_ads_disable_msg)
        autoadsOnboarding?.gone()
        graphLayout?.gone()
        progressView?.visible()
        autoadsLayout?.gone()
        graphLayout?.gone()
        tabLayout?.gone()
        viewPagerFrag?.gone()
    }

    private fun setEmptyView() {
        viewPagerFrag?.gone()
        autoadsLayout?.gone()
        appBarLayout2?.gone()
        emptyView?.findViewById<ImageUnify>(R.id.image_empty)
            ?.setImageDrawable(context?.getResDrawable(R.drawable.topads_dashboard_empty_product))
        emptyView?.visible()
        view?.findViewById<UnifyButton>(R.id.mulai_beriklan)?.setOnClickListener {
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendAutoAdsEvent(
                CLICK_MULAI_BERIKLAN, "")
            RouteManager.route(context, ApplinkConstInternalTopAds.TOPADS_CREATE_ADS)
        }
    }


    private fun setNoAdsView() {
        viewPagerFrag?.gone()
        autoadsLayout?.gone()
        appBarLayout2?.gone()
        emptyView?.findViewById<ImageUnify>(R.id.image_empty)
            ?.setImageDrawable(context?.getResDrawable(R.drawable.topads_dashboard_no_ads))
        emptyView?.findViewById<Typography>(R.id.text_desc)?.text =
            getString(R.string.topads_dashboard_empty_ads_desc)
        emptyView?.visible()
        view?.findViewById<UnifyButton>(R.id.mulai_beriklan)?.setOnClickListener {
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendAutoAdsEvent(
                CLICK_MULAI_BERIKLAN, "")
            RouteManager.route(context, ApplinkConstInternalTopAds.TOPADS_CREATE_ADS)
        }
    }

    private fun noProduct() {
        progressView?.gone()
        adTypeCallBack?.onNoProduct(true)
        adTypeCallBack?.adInfo(MANUAL_AD)
        if (adCurrentState == STATUS_ACTIVE || adCurrentState == STATUS_NOT_DELIVERED) {
            autoAds()
        } else {
            setEmptyView()
        }
    }

    private fun checkInProgress(): Boolean {
        return STATUS_IN_PROGRESS_ACTIVE == adCurrentState || STATUS_IN_PROGRESS_AUTOMANAGE == adCurrentState || STATUS_IN_PROGRESS_INACTIVE == adCurrentState
    }

    private fun manualAds() {
        autoAdsWidget?.gone()
        autoadsLayout?.gone()
        if (checkInProgress()) {
            context?.run {
                imgBg.background = VectorDrawableCompat.create(resources, com.tokopedia.topads.common.R.drawable.topads_common_blue_bg, null)
            }
            autoadsDeactivationProgress?.visibility = View.VISIBLE
            showProgressLayout()
            autoadsOnboarding?.gone()
        } else {
            setCommonViewVisible()
            setManualAds(true)
            adTypeCallBack?.adInfo(MANUAL_AD)
            renderManualViewPager()
        }
    }

    private fun setManualAds(manual: Boolean) {
        if (manual) {
            tabLayout?.visible()
            viewPagerFrag?.visible()
            autoadsDeactivationProgress?.gone()
            autoadsOnboarding?.visible()
        } else {
            viewPagerFrag?.gone()
            autoadsLayout?.visible()
            tabLayout?.gone()
            autoadsOnboarding?.gone()
        }
    }

    private fun autoAds() {
        autoAdsWidget?.loadData(0)
        autoAdsWidget?.visibility = View.VISIBLE
        if (checkInProgress()) {
            showProgressLayout()
        } else {
            setCommonViewVisible()
            adTypeCallBack?.adInfo(SINGLE_AD)
            setAutoAdsAdapter()
            setManualAds(false)
            fetchData()
        }
    }

    private fun setCommonViewVisible() {
        graphLayout?.visible()
        progressView?.gone()
        appBarLayout2?.visible()
        emptyView?.gone()
    }

    private fun fetchData() {
        currentPageNum = 1
        autoAdsAdapter.items.clear()
        autoAdsAdapter.notifyDataSetChanged()
        topAdsDashboardPresenter.getGroupProductData(1,
            null,
            searchBar?.searchBarTextField?.text.toString(),
            groupFilterSheet.getSelectedSortId(),
            null,
            format.format(startDate),
            format.format(endDate),
            0,
            this::onSuccessResult,
            this::onEmptyResult)
    }

    private fun onSuccessResult(response: NonGroupResponse.TopadsDashboardGroupProducts) {
        totalCount = response.meta.page.total
        totalPage = if (totalCount % response.meta.page.perPage == 0) {
            totalCount / response.meta.page.perPage
        } else
            (totalCount / response.meta.page.perPage) + 1
        recyclerviewScrollListener.updateStateAfterGetData()
        loader?.visibility = View.GONE
        recyclerviewScrollListener.updateStateAfterGetData()
        val adIds: MutableList<String> = mutableListOf()
        response.data.forEach {
            adIds.add(it.adId)
            autoAdsAdapter.items.add(AutoAdsItemsItemModel(it))
        }
        val resources = context?.resources
        if (adIds.isNotEmpty() && resources != null) {
            topAdsDashboardPresenter.getProductStats(resources,
                format.format(startDate),
                format.format(endDate),
                adIds,
                groupFilterSheet.getSelectedSortId(),
                0,
                ::onSuccessStats)
        }
        if (!groupFilterSheet.getFilterCount().isZero()) {
            filterCount?.visibility = View.VISIBLE
            filterCount?.text = groupFilterSheet.getFilterCount().toString()
        } else
            filterCount?.visibility = View.GONE
        groupFilterSheet.removeStatusFilter()
    }

    private fun onSuccessStats(stats: GetDashboardProductStatistics) {
        autoAdsAdapter.setstatistics(stats.data)
    }

    private fun onEmptyResult() {
        autoAdsAdapter.items.add(AutoAdsItemsEmptyModel())
        autoAdsAdapter.notifyDataSetChanged()
    }

    override fun onDateRangeChanged() {
        super.onDateRangeChanged()
        loadData()
    }

    private fun loadData() {
        try {
            topAdsDashboardPresenter.getAutoAdsStatus(requireContext().resources, ::onSuccessAdsInfo)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    private fun loadStatisticsData() {
        if (startDate == null || endDate == null) return
        var adType = (activity as TopAdsDashboardActivity?)?.getAdInfo()
            ?: MANUAL_AD

        if ((activity as TopAdsDashboardActivity?)?.getAdInfo() == MANUAL_AD && tabLayout?.tabLayout?.selectedTabPosition == 1)
            adType = SINGLE_AD

        topAdsDashboardPresenter.getStatistic(startDate ?: Date(), endDate
            ?: Date(), selectedStatisticType, adType, ::onSuccesGetStatisticsInfo)
    }

    override fun onErrorGetShopInfo(throwable: Throwable) {
        if (isAttached()) {
            swipeRefreshLayout?.isRefreshing = false
            snackbarRetry?.showRetrySnackbar()
        }
    }

    override fun onErrorGetStatisticsInfo(throwable: Throwable) {
        if (isAttached()) {
            swipeRefreshLayout?.isRefreshing = false
            snackbarRetry?.showRetrySnackbar()
        }
    }

    private fun onSuccesGetStatisticsInfo(dataStatistic: DataStatistic) {
        if (isAttached()) {
            swipeRefreshLayout?.isRefreshing = false
            snackbarRetry?.hideRetrySnackbar()
            this.dataStatistic = dataStatistic
            val resources = context?.resources
            if (this.dataStatistic != null && dataStatistic.cells.isNotEmpty() && resources != null) {
                topAdsTabAdapter?.setSummary(dataStatistic.summary,
                    resources.getStringArray(R.array.top_ads_tab_statistics_labels))
            }
            val fragment =
                pager?.let { it.adapter?.instantiateItem(it, it.currentItem) } as? Fragment
            if (fragment != null && fragment is TopAdsDashStatisticFragment) {
                fragment.showLineGraph(this.dataStatistic)
            }
        }
    }

    private fun onSuccessAdsInfo(data: AutoAdsResponse.TopAdsGetAutoAds.Data) {
        adCurrentState = data.status
        val resources = context?.resources ?: return
        topAdsDashboardPresenter.getAdsStatus(resources)
    }

    override fun onSuccessAdStatus(data: AdStatusResponse.TopAdsGetShopInfo.Data) {
        swipeRefreshLayout?.isRefreshing = false
        when (data.category) {
            CONST_1 -> noProduct()
            CONST_2 -> noAds()
            CONST_3 -> manualAds()
            CONST_4 -> autoAds()
            else -> manualAds()
        }
        loadStatisticsData()
    }

    override fun onError(message: String) {
        val errorMessage =
            com.tokopedia.topads.common.data.util.Utils.getErrorMessage(context, message)
        view?.let {
            Toaster.build(it, errorMessage,
                Snackbar.LENGTH_LONG,
                Toaster.TYPE_ERROR,
                getString(com.tokopedia.topads.common.R.string.topads_common_text_ok)).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        topAdsDashboardPresenter.detachView()
    }

    override fun setGroupCount(size: Int) {
        tabLayout?.getUnifyTabLayout()?.getTabAt(0)?.setCounter(size)
    }

    fun setNonGroupCount(size: Int) {
        tabLayout?.getUnifyTabLayout()?.getTabAt(1)?.setCounter(size)
    }

    override fun setDeletedGroupCount(size: Int) {
        tabLayout?.getUnifyTabLayout()?.getTabAt(2)?.setCounter(size)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AppBarAction)
            collapseStateCallBack = context

        if (context is AdInfo)
            adTypeCallBack = context
    }

    override fun onDetach() {
        super.onDetach()
        collapseStateCallBack = null
        adTypeCallBack = null
    }

    private fun onStateChanged(state: State?) {
        collapseStateCallBack?.setAppBarState(state)
        swipeRefreshLayout?.isEnabled = state == State.EXPANDED
    }

    interface AdInfo {
        fun adInfo(adInfo: String)
        fun onNoProduct(isNoProduct: Boolean)
    }

    interface AppBarAction {
        fun setAppBarState(state: State?)
    }

    companion object {
        private const val fragmentLoadCountThree = 3
        const val MILLISECONDS_PER_INCH = 200f
        const val MANUAL_AD = "-1"
        const val SINGLE_AD = "-2"
        fun createInstance(): TopAdsProductIklanFragment {
            return TopAdsProductIklanFragment()
        }
    }
}
