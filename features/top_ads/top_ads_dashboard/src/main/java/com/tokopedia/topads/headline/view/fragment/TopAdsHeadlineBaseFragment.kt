package com.tokopedia.topads.headline.view.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.topads.common.data.response.groupitem.GetTopadsDashboardGroupStatistics
import com.tokopedia.topads.common.data.response.groupitem.GroupItemResponse
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.SEVEN_DAYS_RANGE_INDEX
import com.tokopedia.topads.dashboard.data.constant.TopAdsStatisticsType
import com.tokopedia.topads.dashboard.data.model.CountDataItem
import com.tokopedia.topads.dashboard.data.model.DataStatistic
import com.tokopedia.topads.dashboard.data.utils.Utils
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.adapter.TopAdsStatisticPagerAdapter
import com.tokopedia.topads.dashboard.view.adapter.TopAdsTabAdapter
import com.tokopedia.topads.dashboard.view.fragment.TopAdsDashStatisticFragment
import com.tokopedia.topads.dashboard.view.fragment.TopAdsProductIklanFragment
import com.tokopedia.topads.dashboard.view.fragment.stats.*
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDashboardPresenter
import com.tokopedia.topads.dashboard.view.sheet.CustomDatePicker
import com.tokopedia.topads.dashboard.view.sheet.DatePickerSheet
import com.tokopedia.topads.dashboard.view.sheet.TopadsGroupFilterSheet
import com.tokopedia.topads.headline.view.activity.TopAdsHeadlineAdDetailViewActivity
import com.tokopedia.topads.headline.view.adapter.aditem.HeadLineAdItemsAdapterTypeFactoryImpl
import com.tokopedia.topads.headline.view.adapter.aditem.HeadLineAdItemsListAdapter
import com.tokopedia.topads.headline.view.adapter.aditem.viewmodel.HeadLineAdItemsEmptyViewModel
import com.tokopedia.topads.headline.view.adapter.aditem.viewmodel.HeadLineAdItemsItemViewModel
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.partial_top_ads_dashboard_statistics.*
import kotlinx.android.synthetic.main.topads_dash_fragment_headline_group_list.*
import kotlinx.android.synthetic.main.topads_dash_headline_layout.*
import kotlinx.android.synthetic.main.topads_dash_headline_layout.loader
import kotlinx.android.synthetic.main.topads_dash_layout_common_action_bar.*
import kotlinx.android.synthetic.main.topads_dash_layout_common_searchbar_layout.*
import kotlinx.android.synthetic.main.topads_dash_layout_hari_ini.*
import kotlinx.android.synthetic.main.topads_dash_layout_hari_ini.view.*
import kotlinx.android.synthetic.main.topads_dash_product_iklan_empty_view.*
import kotlinx.android.synthetic.main.topads_dash_product_iklan_empty_view.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.math.abs

/**
 * Created by Pika on 16/10/20.
 */

open class TopAdsHeadlineBaseFragment : BaseDaggerFragment(), CustomDatePicker.ActionListener {

    internal var startDate: Date? = null
    internal var endDate: Date? = null
    private var dataStatistic: DataStatistic? = null
    private var datePickerSheet: DatePickerSheet? = null

    @Inject
    lateinit var presenter: TopAdsDashboardPresenter
    private lateinit var recyclerView: RecyclerView
    private var totalCount = 0
    private var totalPage = 0
    private var currentPageNum = 1
    private var SingleDelGroupId = ""
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var recyclerviewScrollListener: EndlessRecyclerViewScrollListener
    private lateinit var adapter: HeadLineAdItemsListAdapter
    private var deleteCancel = false
    private val groupIds: MutableList<String> = mutableListOf()
    private var mCurrentState = TopAdsProductIklanFragment.State.IDLE
    private var collapseStateCallBack: AppBarActionHeadline? = null


    companion object {
        private const val CUREENTY_ACTIVATED = 1
        fun createInstance(): TopAdsHeadlineBaseFragment {
            return TopAdsHeadlineBaseFragment()
        }
    }

    private val groupFilterSheet: TopadsGroupFilterSheet by lazy {
        context.run {
            TopadsGroupFilterSheet.newInstance(context!!)
        }
    }

    private val pagerAdapter: TopAdsStatisticPagerAdapter? by lazy {
        val fragmentList = listOf(
                TopAdsStatsImprFragment.createInstance(),
                TopAdsStatsKlikFragment.createInstance(),
                TopAdsStatsSpentFragment.createInstance(),
                TopAdsStatsIncomeFragment.createInstance(),
                TopAdsStatsCtrFragment.createInstance(),
                TopAdsStatsConversionFragment.createInstance(),
                TopAdsStatsAvgFragment.createInstance(),
                TopAdsStatsSoldFragment.createInstance())
        context?.run { TopAdsStatisticPagerAdapter(this, childFragmentManager, fragmentList) }
    }

    private val topAdsTabAdapter: TopAdsTabAdapter? by lazy {
        context?.run { TopAdsTabAdapter(this) }
    }

    protected val currentStatisticsFragment: TopAdsDashStatisticFragment?
        get() = pagerAdapter?.instantiateItem(pager, topAdsTabAdapter?.selectedTabPosition
                ?: 0) as? TopAdsDashStatisticFragment


    override fun getScreenName(): String {
        return TopAdsHeadlineBaseFragment::class.java.name
    }

    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = HeadLineAdItemsListAdapter(HeadLineAdItemsAdapterTypeFactoryImpl(::startSelectMode,
                ::singleItemDelete, ::statusChange, ::onGroupClicked))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(resources.getLayout(R.layout.topads_dash_headline_layout), container, false)
        recyclerView = view.findViewById(R.id.group_list)
        initAdapter()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loader.visibility = View.VISIBLE
        startDate = Utils.getStartDate()
        endDate = Utils.getEndDate()
        setDateRangeText(SEVEN_DAYS_RANGE_INDEX)
        initStatisticComponent()
        loadStatisticsData()
        btnFilter.setOnClickListener {
            groupFilterSheet.show()
            groupFilterSheet.onSubmitClick = { fetchData() }
        }
        close_butt.setOnClickListener {
            startSelectMode(false)
        }
        activate.setOnClickListener {
            performAction(TopAdsDashboardConstant.ACTION_ACTIVATE)
        }
        deactivate.setOnClickListener {
            performAction(TopAdsDashboardConstant.ACTION_DEACTIVATE)
        }
        delete.setOnClickListener {
            showConfirmationDialog()
        }
        btnAddItem.setOnClickListener {
            RouteManager.route(context, ApplinkConstInternalTopAds.TOPADS_CREATE_ADS)
        }
        Utils.setSearchListener(context, view, ::fetchData)

        hari_ini?.date_image?.setImageDrawable(context?.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_ic_calendar))
        hari_ini?.next_image?.setImageDrawable(context?.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_ic_arrow))
        hari_ini?.setOnClickListener {
            showBottomSheet()
        }

        swipe_refresh_layout.setOnRefreshListener {
            fetchData()
            loadStatisticsData()
        }
        app_bar_layout_2?.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, offset ->
            when {
                offset == 0 -> {
                    if (mCurrentState != TopAdsProductIklanFragment.State.EXPANDED) {
                        onStateChanged(TopAdsProductIklanFragment.State.EXPANDED);
                    }
                    mCurrentState = TopAdsProductIklanFragment.State.EXPANDED;
                }
                abs(offset) >= appBarLayout.totalScrollRange -> {
                    if (mCurrentState != TopAdsProductIklanFragment.State.COLLAPSED) {
                        onStateChanged(TopAdsProductIklanFragment.State.COLLAPSED);
                    }
                    mCurrentState = TopAdsProductIklanFragment.State.COLLAPSED;
                }
                else -> {
                    if (mCurrentState != TopAdsProductIklanFragment.State.IDLE) {
                        onStateChanged(TopAdsProductIklanFragment.State.IDLE);
                    }
                    mCurrentState = TopAdsProductIklanFragment.State.IDLE;
                }
            }
        })
    }

    private fun onStateChanged(state: TopAdsProductIklanFragment.State?) {
        collapseStateCallBack?.setAppBarStateHeadline(state)
        swipe_refresh_layout.isEnabled = state == TopAdsProductIklanFragment.State.EXPANDED
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.getShopAdsInfo {
            val info = it.topadsGetShopInfoV2.data.ads[1]
            if (info.type == "headline") {
                if (!info.isUsed) {
                    showEmptyView()
                } else {
                    fetchData()
                }
            }
        }
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
                return TopAdsProductIklanFragment.MILLISECONDS_PER_INCH / displayMetrics.densityDpi
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
                currentStatisticsFragment?.showLineGraph(dataStatistic)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    private fun initTabLayouTitles() {
        topAdsTabAdapter?.setSummary(null, resources.getStringArray(R.array.top_ads_tab_statistics_labels))
    }

    private fun loadStatisticsData() {
        if (startDate == null || endDate == null) return
        presenter.getTopAdsStatistic(startDate!!, endDate!!, TopAdsStatisticsType.HEADLINE_ADS, "-1", ::onSuccesGetStatisticsInfo)
    }

    private fun onSuccesGetStatisticsInfo(dataStatistic: DataStatistic) {
        loader.visibility = View.GONE
        this.dataStatistic = dataStatistic
        if (this.dataStatistic != null && dataStatistic.cells.isNotEmpty()) {
            topAdsTabAdapter?.setSummary(dataStatistic.summary, resources.getStringArray(R.array.top_ads_tab_statistics_labels))
        }
        val fragment = pager.adapter?.instantiateItem(pager, pager.currentItem) as? Fragment
        if (fragment != null && fragment is TopAdsDashStatisticFragment) {
            fragment.showLineGraph(this.dataStatistic)
        }
    }

    private fun onGroupClicked(id: Int, priceSpent: String) {
        val intent = Intent(context, TopAdsHeadlineAdDetailViewActivity::class.java)
        intent.putExtra(TopAdsDashboardConstant.GROUP_ID, id)
        intent.putExtra(TopAdsDashboardConstant.PRICE_SPEND, priceSpent)
        startActivity(intent)
    }

    private fun showEmptyView() {
        empty_view?.visibility = View.VISIBLE
        empty_view.image_empty.setImageDrawable(context?.getResDrawable(R.drawable.topads_dashboard_empty_product))
        text_title.text = getString(R.string.topads_headline_empty_state_title)
        text_desc.text = getString(R.string.topads_headline_empty_state_desc)
        hari_ini?.visibility = View.GONE
    }

    private fun singleItemDelete(pos: Int) {
        SingleDelGroupId = (adapter.items[pos] as HeadLineAdItemsItemViewModel).data.groupId.toString()
        performAction(TopAdsDashboardConstant.ACTION_DELETE)
    }

    private fun statusChange(pos: Int, status: Int) {
        if (status != CUREENTY_ACTIVATED)
            presenter.setGroupAction(::onSuccessAction, TopAdsDashboardConstant.ACTION_ACTIVATE,
                    listOf((adapter.items[pos] as HeadLineAdItemsItemViewModel).data.groupId.toString()), resources)
        else
            presenter.setGroupAction(::onSuccessAction, TopAdsDashboardConstant.ACTION_DEACTIVATE,
                    listOf((adapter.items[pos] as HeadLineAdItemsItemViewModel).data.groupId.toString()), resources)
    }

    private fun performAction(actionActivate: String) {
        if (actionActivate == TopAdsDashboardConstant.ACTION_DELETE) {
            view.let {
                Toaster.make(it!!, getString(R.string.topads_dash_with_grup_delete_toast), TopAdsDashboardConstant.TOASTER_DURATION.toInt(), Toaster.TYPE_NORMAL, getString(com.tokopedia.topads.common.R.string.topads_common_batal), View.OnClickListener {
                    deleteCancel = true
                })
            }
            val coroutineScope = CoroutineScope(Dispatchers.Main)
            coroutineScope.launch {
                delay(TopAdsDashboardConstant.TOASTER_DURATION)
                if (activity != null && isAdded) {
                    if (!deleteCancel)
                        presenter.setGroupAction(::onSuccessAction, actionActivate, getAdIds(), resources)
                    deleteCancel = false
                    startSelectMode(false)
                    SingleDelGroupId = ""
                }
            }
        } else {
            presenter.setGroupAction(::onSuccessAction, actionActivate, getAdIds(), resources)
            SingleDelGroupId = ""
        }
    }

    private fun getAdIds(): MutableList<String> {
        val ads: MutableList<String> = mutableListOf()
        return if (SingleDelGroupId.isEmpty()) {
            adapter.getSelectedItems().forEach {
                ads.add(it.data.groupId.toString())
            }
            ads
        } else {
            mutableListOf(SingleDelGroupId)
        }
    }

    private fun showConfirmationDialog() {
        val dialog = DialogUnify(context!!, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setTitle(String.format(getString(R.string.topads_dash_confirm_delete_group_title), adapter.getSelectedItems().size))
        dialog.setDescription(getString(R.string.topads_dash_confirm_delete_group_desc))
        dialog.setPrimaryCTAText(getString(com.tokopedia.topads.common.R.string.topads_common_cancel_btn))
        dialog.setSecondaryCTAText(getString(R.string.topads_dash_ya_hapus))
        dialog.setPrimaryCTAClickListener {
            dialog.dismiss()
        }
        dialog.setSecondaryCTAClickListener {
            dialog.dismiss()
            performAction(TopAdsDashboardConstant.ACTION_DELETE)
        }
        dialog.show()
    }

    private fun showBottomSheet() {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val index = sharedPref?.getInt(TopAdsDashboardConstant.DATE_RANGE_BERANDA, 2)
        val customStartDate = sharedPref?.getString(TopAdsDashboardConstant.START_DATE_BERANDA, "")
        val customEndDate = sharedPref?.getString(TopAdsDashboardConstant.END_DATE_BERANDA, "")
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


    private fun handleDate(date1: Long, date2: Long, position: Int) {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putInt(TopAdsDashboardConstant.DATE_RANGE_BERANDA, position)
            commit()
        }
        startDate = Date(date1)
        endDate = Date(date2)
        setDateRangeText(position)
        fetchData()
        loadStatisticsData()
    }

    private fun startCustomDatePicker() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1)
        val selectDate: String = Utils.format.format(calendar.time)
        calendar.add(Calendar.YEAR, -1)
        val date = calendar.time
        val minDate = Utils.format.format(date)
        val maxDate: String = Utils.format.format(Date())
        val sheet = CustomDatePicker.getInstance(minDate, maxDate, selectDate)
        sheet.setTitle(resources.getString(R.string.topads_dash_choose_date))
        sheet.setListener(this)
        sheet.show(childFragmentManager, "datepicker")
    }

    private fun setDateRangeText(position: Int) {
        when (position) {
            1 -> current_date.text = context?.getString(com.tokopedia.datepicker.range.R.string.yesterday)
            0 -> current_date.text = context?.getString(R.string.topads_dash_hari_ini)
            2 -> current_date.text = context?.getString(com.tokopedia.datepicker.range.R.string.seven_days_ago)
            else -> {
                val text = Utils.outputFormat.format(startDate) + " - " + Utils.outputFormat.format(endDate)
                current_date.text = text
            }
        }
    }

    private fun onSuccessAction(action: String) {
        startSelectMode(false)
        fetchData()
    }

    private fun startSelectMode(select: Boolean) {
        if (select) {
            adapter.setSelectMode(true)
            actionbar.visibility = View.VISIBLE
            movetogroup.visibility = View.GONE
            btnAddItem.visibility = View.VISIBLE
        } else {
            adapter.setSelectMode(false)
            actionbar.visibility = View.GONE
            btnAddItem.visibility = View.GONE
        }
    }

    private fun initAdapter() {
        layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerviewScrollListener = onRecyclerViewListener()
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
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

    private fun fetchNextPage(currentPage: Int) {
        presenter.getGroupData(resources, currentPage, searchBar?.searchBarTextField?.text.toString(),
                groupFilterSheet.getSelectedSortId(), groupFilterSheet.getSelectedStatusId(),
                Utils.format.format(startDate), Utils.format.format(endDate), 3,
                this::onSuccessGroupResult)
    }

    private fun onSuccessGroupResult(response: GroupItemResponse.GetTopadsDashboardGroups) {
        totalCount = response.meta.page.total
        totalPage = (totalCount / response.meta.page.perPage) + 1
        recyclerviewScrollListener.updateStateAfterGetData()
        loader.visibility = View.GONE
        response.data.forEach {
            groupIds.add(it.groupId.toString())
            adapter.items.add(HeadLineAdItemsItemViewModel(it))
        }
        if (adapter.items.size.isZero()) {
            onEmptyResult()
        } else if (groupIds.isNotEmpty()) {
            presenter.getGroupStatisticsData(resources, 1, ",", "", 0,
                    "", "", groupIds, ::onSuccessStatistics)
            presenter.getCountProductKeyword(resources, groupIds, ::onSuccessCount)
        }
        setFilterCount()
    }

    private fun onSuccessStatistics(statistics: GetTopadsDashboardGroupStatistics) {
        adapter.setstatistics(statistics.data)
    }

    private fun onSuccessCount(countList: List<CountDataItem>) {
        adapter.setItemCount(countList)
        loader.visibility = View.GONE
    }

    private fun setFilterCount() {
        if (!groupFilterSheet.getFilterCount().isZero()) {
            filterCount.visibility = View.VISIBLE
            filterCount.text = groupFilterSheet.getFilterCount().toString()
        } else
            filterCount.visibility = View.GONE
    }

    private fun onEmptyResult() {
        adapter.items.add(HeadLineAdItemsEmptyViewModel())
        if (searchBar?.searchBarTextField?.text.toString().isEmpty()) {
            adapter.setEmptyView(!TopAdsDashboardConstant.EMPTY_SEARCH_VIEW)
        } else {
            adapter.setEmptyView(TopAdsDashboardConstant.EMPTY_SEARCH_VIEW)
        }
    }

    private fun fetchData() {
        swipe_refresh_layout.isRefreshing = false
        groupIds.clear()
        currentPageNum = 1
        loader.visibility = View.VISIBLE
        adapter.items.clear()
        adapter.notifyDataSetChanged()
        presenter.getGroupData(resources, 1, searchBar?.searchBarTextField?.text.toString(),
                groupFilterSheet.getSelectedSortId(), groupFilterSheet.getSelectedStatusId(),
                Utils.format.format(startDate), Utils.format.format(endDate), 3,
                this::onSuccessGroupResult)
    }

    override fun onCustomDateSelected(dateSelected: Date, dateEnd: Date) {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putInt(TopAdsDashboardConstant.DATE_RANGE_BERANDA, TopAdsDashboardConstant.CUSTOM_DATE)
            commit()
        }
        startDate = dateSelected
        with(sharedPref.edit()) {
            putString(TopAdsDashboardConstant.START_DATE_BERANDA, Utils.outputFormat.format(startDate))
            commit()
        }
        endDate = dateEnd
        with(sharedPref.edit()) {
            putString(TopAdsDashboardConstant.END_DATE_BERANDA, Utils.outputFormat.format(endDate))
            commit()
        }
        setDateRangeText(TopAdsDashboardConstant.CUSTOM_DATE)
        loadStatisticsData()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AppBarActionHeadline)
            collapseStateCallBack = context
    }

    override fun onDetach() {
        super.onDetach()
        collapseStateCallBack = null
    }

    interface AppBarActionHeadline {
        fun setAppBarStateHeadline(state: TopAdsProductIklanFragment.State?)
    }

}