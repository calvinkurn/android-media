package com.tokopedia.topads.dashboard.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarRetry
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.data.internal.AutoAdsStatus.*
import com.tokopedia.topads.common.data.response.AutoAdsResponse
import com.tokopedia.topads.common.data.response.nongroupItem.GetDashboardProductStatistics
import com.tokopedia.topads.common.data.response.nongroupItem.NonGroupResponse
import com.tokopedia.topads.common.view.widget.AutoAdsWidgetCommon
import com.tokopedia.topads.dashboard.R
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
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.setCounter
import kotlinx.android.synthetic.main.partial_top_ads_dashboard_statistics.*
import kotlinx.android.synthetic.main.topads_dash_auto_ads_onboarding_widget.*
import kotlinx.android.synthetic.main.topads_dash_fragment_product_iklan.*
import kotlinx.android.synthetic.main.topads_dash_layout_common_searchbar_layout.*
import kotlinx.android.synthetic.main.topads_dash_product_iklan_empty_view.*
import kotlinx.android.synthetic.main.topads_dash_product_iklan_empty_view.view.*
import java.util.*
import javax.inject.Inject
import kotlin.math.abs

/**
 * Created by hadi.putra on 23/04/18.
 */

private const val CLICK_COBA_SEKARANG = "click - coba sekarang"

class TopAdsProductIklanFragment : TopAdsBaseTabFragment(), TopAdsDashboardView {
    private var adCurrentState = 0
    private var datePickerSheet: DatePickerSheet? = null
    override fun getLayoutId(): Int {
        return R.layout.topads_dash_fragment_product_iklan
    }

    override fun setUpView(view: View) {
        recyclerView = view.findViewById(R.id.auto_ads_list)
        imgBg = view.findViewById(R.id.progressImg)
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
        get() = autoads_edit_widget

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
        activity?.let {
            GraphqlClient.init(it)
        }
        autoAdsAdapter = AutoAdsItemsListAdapter(AutoAdsItemsAdapterTypeFactoryImpl())

        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        topAdsDashboardPresenter.attachView(this)
        auto_ad_status_image.setImageDrawable(context?.getResDrawable(R.drawable.ill_iklan_otomatis))
        onBoarding.setOnClickListener {
            RouteManager.route(activity, ApplinkConstInternalTopAds.TOPADS_AUTOADS_ONBOARDING)
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsDashboardEvent(CLICK_COBA_SEKARANG, "")
        }
        loadData()
        btnFilter.setOnClickListener {
            groupFilterSheet.show(childFragmentManager, "")
            groupFilterSheet.onSubmitClick = { fetchData() }
        }
        swipe_refresh_layout.setOnRefreshListener {
            loadData()
        }
        activity?.run {
            snackbarRetry = NetworkErrorHelper.createSnackbarWithAction(this) { loadData() }
            snackbarRetry?.setColorActionRetry(ContextCompat.getColor(this, com.tokopedia.abstraction.R.color.green_400))
        }

        app_bar_layout_2?.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, offset ->
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
        Utils.setSearchListener(context, view, ::fetchData)
    }

    private fun renderManualViewPager() {
        view_pager_frag?.adapter = getViewPagerAdapter()
        view_pager_frag.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {}

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}

            override fun onPageSelected(position: Int) {
                loadStatisticsData()
            }
        })
        tab_layout?.setupWithViewPager(view_pager_frag)
    }

    private fun getViewPagerAdapter(): TopAdsDashboardBasePagerAdapter? {
        val list: ArrayList<FragmentTabItem> = arrayListOf()
        tab_layout?.getUnifyTabLayout()?.removeAllTabs()
        tab_layout?.addNewTab(GRUP)
        tab_layout?.addNewTab(TANPA_GRUP)
        list.add(FragmentTabItem(GRUP, TopAdsDashGroupFragment()))
        list.add(FragmentTabItem(TANPA_GRUP, TopAdsDashWithoutGroupFragment()))
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
        topAdsDashboardPresenter.getGroupProductData(page, null, searchBar?.searchBarTextField?.text.toString(), groupFilterSheet.getSelectedSortId(),
                null, format.format(startDate ?: Date()), format.format(endDate
                ?: Date()), this::onSuccessResult, this::onEmptyResult)
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
            manualAds()
        }
    }

    private fun isAttached(): Boolean {
        return activity != null && isAdded
    }

    private fun showProgressLayout() {
        btnReload?.setOnClickListener {
            swipe_refresh_layout.isRefreshing = true
            loadData()
        }
        if (STATUS_IN_PROGRESS_ACTIVE == adCurrentState)
            description?.text = getString(R.string.topads_dash_auto_ads_enable_msg)
        else if (STATUS_IN_PROGRESS_INACTIVE == adCurrentState)
            description?.text = getString(R.string.topads_dash_auto_ads_disable_msg)
        autoadsOnboarding?.gone()
        graph_layout?.gone()
        progressView?.visible()
        autoads_layout?.gone()
        graph_layout?.gone()
        tab_layout?.gone()
        view_pager_frag?.gone()
    }

    private fun setEmptyView() {
        view_pager_frag?.gone()
        autoads_layout?.gone()
        app_bar_layout_2?.gone()
        empty_view?.image_empty?.setImageDrawable(context?.getResDrawable(R.drawable.topads_dashboard_empty_product))
        empty_view?.visible()
        mulai_beriklan.setOnClickListener {
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
        autoads_layout.gone()
        if (checkInProgress()) {
            context?.run {
                imgBg.background = AppCompatResources.getDrawable(this, com.tokopedia.topads.common.R.drawable.topads_common_blue_bg)
            }
            autoadsDeactivationProgress?.visibility = View.VISIBLE
            showProgressLayout()
            autoadsOnboarding.gone()
        } else {
            setCommonViewVisible()
            setManualAds(true)
            adTypeCallBack?.adInfo(MANUAL_AD)
            renderManualViewPager()
        }
    }

    private fun setManualAds(manual: Boolean) {
        if (manual) {
            tab_layout?.visible()
            view_pager_frag?.visible()
            autoadsDeactivationProgress?.gone()
            autoadsOnboarding?.visible()
        } else {
            view_pager_frag?.gone()
            autoads_layout?.visible()
            tab_layout?.gone()
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
        graph_layout?.visible()
        progressView?.gone()
        app_bar_layout_2?.visible()
        empty_view?.gone()
    }

    private fun fetchData() {
        currentPageNum = 1
        autoAdsAdapter.items.clear()
        autoAdsAdapter.notifyDataSetChanged()
        topAdsDashboardPresenter.getGroupProductData(1, null, searchBar?.searchBarTextField?.text.toString(), groupFilterSheet.getSelectedSortId(),
                null, format.format(startDate), format.format(endDate), this::onSuccessResult, this::onEmptyResult)
    }

    private fun onSuccessResult(response: NonGroupResponse.TopadsDashboardGroupProducts) {
        totalCount = response.meta.page.total
        totalPage = if (totalCount % response.meta.page.perPage == 0) {
            totalCount / response.meta.page.perPage
        } else
            (totalCount / response.meta.page.perPage) + 1
        recyclerviewScrollListener.updateStateAfterGetData()
        loader.visibility = View.GONE
        recyclerviewScrollListener.updateStateAfterGetData()
        val adIds: MutableList<String> = mutableListOf()
        response.data.forEach {
            adIds.add(it.adId.toString())
            autoAdsAdapter.items.add(AutoAdsItemsItemModel(it))
        }
        if (adIds.isNotEmpty()) {
            topAdsDashboardPresenter.getProductStats(resources, format.format(startDate), format.format(endDate), adIds, groupFilterSheet.getSelectedSortId(), 0, ::OnSuccessStats)
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
        autoAdsAdapter.items.add(AutoAdsItemsEmptyModel())
        autoAdsAdapter.notifyDataSetChanged()
    }

    private fun loadData() {
        topAdsDashboardPresenter.getAutoAdsStatus(resources, ::onSuccessAdsInfo)
    }

    private fun loadStatisticsData() {
        if (startDate == null || endDate == null) return
        var adType = (activity as TopAdsDashboardActivity?)?.getAdInfo()
                ?: MANUAL_AD

        if ((activity as TopAdsDashboardActivity?)?.getAdInfo() == MANUAL_AD && tab_layout?.tabLayout?.selectedTabPosition == 1)
            adType = SINGLE_AD

        topAdsDashboardPresenter.getStatistic(startDate ?: Date(), endDate
                ?: Date(), selectedStatisticType, adType, ::onSuccesGetStatisticsInfo)
    }

    override fun onErrorGetShopInfo(throwable: Throwable) {
        if (isAttached()) {
            swipe_refresh_layout.isRefreshing = false
            snackbarRetry?.showRetrySnackbar()
        }
    }

    override fun onErrorGetStatisticsInfo(throwable: Throwable) {
        if (isAttached()) {
            swipe_refresh_layout.isRefreshing = false
            snackbarRetry?.showRetrySnackbar()
        }
    }

    private fun onSuccesGetStatisticsInfo(dataStatistic: DataStatistic) {
        if (isAttached()) {
            swipe_refresh_layout.isRefreshing = false
            snackbarRetry?.hideRetrySnackbar()
            this.dataStatistic = dataStatistic
            if (this.dataStatistic != null && dataStatistic.cells.isNotEmpty()) {
                topAdsTabAdapter?.setSummary(dataStatistic.summary, resources.getStringArray(R.array.top_ads_tab_statistics_labels))
            }
            val fragment = pager.adapter?.instantiateItem(pager, pager.currentItem) as? Fragment
            if (fragment != null && fragment is TopAdsDashStatisticFragment) {
                fragment.showLineGraph(this.dataStatistic)
            }
        }
    }

    private fun onSuccessAdsInfo(data: AutoAdsResponse.TopAdsGetAutoAds.Data) {
        adCurrentState = data.status
        topAdsDashboardPresenter.getAdsStatus(resources)
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
        loadStatisticsData()
    }

    override fun onError(message: String) {
        val errorMessage = com.tokopedia.topads.common.data.util.Utils.getErrorMessage(context, message)
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

    fun setGroupCount(size: Int) {
        tab_layout?.getUnifyTabLayout()?.getTabAt(0)?.setCounter(size)
    }

    fun setNonGroupCount(size: Int) {
        tab_layout?.getUnifyTabLayout()?.getTabAt(1)?.setCounter(size)
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
        swipe_refresh_layout.isEnabled = state == State.EXPANDED
    }

    interface AdInfo {
        fun adInfo(adInfo: String)
        fun onNoProduct(isNoProduct: Boolean)
    }

    interface AppBarAction {
        fun setAppBarState(state: State?)
    }

    companion object {
        const val MILLISECONDS_PER_INCH = 200f
        const val MANUAL_AD = "-1"
        const val SINGLE_AD = "-2"
        fun createInstance(): TopAdsProductIklanFragment {
            return TopAdsProductIklanFragment()
        }
    }
}