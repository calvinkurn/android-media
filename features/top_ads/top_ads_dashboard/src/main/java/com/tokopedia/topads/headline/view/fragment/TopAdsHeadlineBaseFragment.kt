package com.tokopedia.topads.headline.view.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
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
import com.tokopedia.topads.dashboard.data.constant.TopAdsStatisticsType
import com.tokopedia.topads.dashboard.data.model.CountDataItem
import com.tokopedia.topads.dashboard.data.model.DataStatistic
import com.tokopedia.topads.dashboard.data.utils.Utils
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.fragment.TopAdsBaseTabFragment
import com.tokopedia.topads.dashboard.view.fragment.TopAdsDashStatisticFragment
import com.tokopedia.topads.dashboard.view.fragment.TopAdsProductIklanFragment
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDashboardPresenter
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
import kotlinx.android.synthetic.main.topads_dash_product_iklan_empty_view.*
import kotlinx.android.synthetic.main.topads_dash_product_iklan_empty_view.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs

/**
 * Created by Pika on 16/10/20.
 */

open class TopAdsHeadlineBaseFragment : TopAdsBaseTabFragment() {

    private var dataStatistic: DataStatistic? = null

    @Inject
    lateinit var presenter: TopAdsDashboardPresenter
    private lateinit var recyclerView: RecyclerView
    private var totalCount = 0
    private var totalPage = 0
    private var currentPageNum = 1
    private var singleDelGroupId = ""
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var recyclerviewScrollListener: EndlessRecyclerViewScrollListener
    private lateinit var adapter: HeadLineAdItemsListAdapter
    private var deleteCancel = false
    private val groupIds: MutableList<String> = mutableListOf()
    private var mCurrentState = TopAdsProductIklanFragment.State.IDLE
    private var collapseStateCallBack: AppBarActionHeadline? = null


    companion object {
        private const val CUREENTY_ACTIVATED = 1
        private const val GROUP_TYPE_HEADLINE = 3
        fun createInstance(): TopAdsHeadlineBaseFragment {
            return TopAdsHeadlineBaseFragment()
        }
    }

    private val groupFilterSheet: TopadsGroupFilterSheet by lazy {
        context.run {
            TopadsGroupFilterSheet.newInstance(context!!)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.topads_dash_headline_layout
    }

    override fun setUpView(view: View) {
        recyclerView = view.findViewById(R.id.group_list)
        initAdapter()
    }

    override fun getChildScreenName(): String {
        return TopAdsHeadlineBaseFragment::class.java.name
    }

    override fun loadChildStatisticsData() {
        fetchData()
        loadStatisticsData()
    }

    override fun renderGraph() {
        currentStatisticsFragment?.showLineGraph(dataStatistic)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = HeadLineAdItemsListAdapter(HeadLineAdItemsAdapterTypeFactoryImpl(::startSelectMode,
                ::singleItemDelete, ::statusChange, ::onGroupClicked))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loader.visibility = View.VISIBLE
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

    private fun loadStatisticsData() {
        if (startDate == null || endDate == null) return
        presenter.getTopAdsStatistic(startDate!!, endDate!!, TopAdsStatisticsType.HEADLINE_ADS, "-1", ::onSuccessGetStatisticsInfo)
    }

    private fun onSuccessGetStatisticsInfo(dataStatistic: DataStatistic) {
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
        app_bar_layout_2?.visibility = View.GONE
        empty_view?.visibility = View.VISIBLE
        empty_view.image_empty.setImageDrawable(context?.getResDrawable(R.drawable.topads_dashboard_empty_product))
        text_title.text = getString(R.string.topads_headline_empty_state_title)
        text_desc.text = getString(R.string.topads_headline_empty_state_desc)
        hari_ini?.visibility = View.GONE
    }

    private fun singleItemDelete(pos: Int) {
        singleDelGroupId = (adapter.items[pos] as HeadLineAdItemsItemViewModel).data.groupId.toString()
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
            view?.let {
                val desc = if (getAdIds().size > 1)
                    String.format(getString(R.string.topads_dash_headline_delete_toast_multiple), getAdIds().size)
                else
                    getString(R.string.topads_dash_headline_delete_toast)
                Toaster.make(it, desc, TopAdsDashboardConstant.TOASTER_DURATION.toInt(), Toaster.TYPE_NORMAL, getString(com.tokopedia.topads.common.R.string.topads_common_batal), View.OnClickListener {
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
                    singleDelGroupId = ""
                }
            }
        } else {
            presenter.setGroupAction(::onSuccessAction, actionActivate, getAdIds(), resources)
            singleDelGroupId = ""
        }
    }

    private fun getAdIds(): MutableList<String> {
        val ads: MutableList<String> = mutableListOf()
        return if (singleDelGroupId.isEmpty()) {
            adapter.getSelectedItems().forEach {
                ads.add(it.data.groupId.toString())
            }
            ads
        } else {
            mutableListOf(singleDelGroupId)
        }
    }

    private fun showConfirmationDialog() {
        context?.let {
            val dialog = DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
            dialog.setTitle(String.format(getString(R.string.topads_dash_headline_bulk_delete_title), adapter.getSelectedItems().size))
            dialog.setDescription(getString(R.string.topads_dasg_headline_bulk_delete_desc))
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
                Utils.format.format(startDate), Utils.format.format(endDate), GROUP_TYPE_HEADLINE,
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

    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
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
                Utils.format.format(startDate), Utils.format.format(endDate), GROUP_TYPE_HEADLINE,
                this::onSuccessGroupResult)
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