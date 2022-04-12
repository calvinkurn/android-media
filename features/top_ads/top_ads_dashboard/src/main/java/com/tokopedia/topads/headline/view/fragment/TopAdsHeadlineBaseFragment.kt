package com.tokopedia.topads.headline.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.response.groupitem.GetTopadsDashboardGroupStatistics
import com.tokopedia.topads.common.data.response.groupitem.GroupItemResponse
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.GROUP_TYPE_HEADLINE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.HEADLINE_UPADTED
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
import com.tokopedia.topads.headline.view.adapter.HeadLineAdItemsAdapterTypeFactoryImpl
import com.tokopedia.topads.headline.view.adapter.HeadLineAdItemsListAdapter
import com.tokopedia.topads.headline.view.adapter.viewmodel.HeadLineAdItemsEmptyModel
import com.tokopedia.topads.headline.view.adapter.viewmodel.HeadLineAdItemsItemModel
import com.tokopedia.unifycomponents.*
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs

/**
 * Created by Pika on 16/10/20.
 */

private const val CLICK_MULAI_BERIKLAN = "click - mulai beriklan"
private const val VIEW_MULAI_BERIKLAN = "view - mulai iklan toko"
private const val CLICK_AKTIFKAN_LONG_PRESS = "click - aktifkan iklan on long press card"
private const val CLICK_NONAKTIFKAN_LONG_PRESS = "click - nonaktifkan iklan on long press card"
private const val CLICK_HAPUS_LONG_PRESS = "click - hapus iklan on long press card"
private const val CLICK_YA_HAPUS_LONG_PRESS = "click - ya hapus iklan on long press card"
private const val CLICK_GRUP_CARD = "click - group ads card"

open class TopAdsHeadlineBaseFragment : TopAdsBaseTabFragment() {

    private var loader: LoaderUnify? = null
    private var swipeRefreshLayout: SwipeToRefresh? = null
    private var emptyView: ConstraintLayout? = null
    private var headlineList: ConstraintLayout? = null
    private var appBarLayout2: AppBarLayout? = null
    private var hariIni: ConstraintLayout? = null
    private var textTitle: Typography? = null
    private var textDesc: Typography? = null
    private var mulaiBeriklan: UnifyButton? = null
    private var actionbar: ConstraintLayout? = null
    private var pager: ViewPager? = null
    private var searchBar: SearchBarUnify? = null
    private var btnFilter: UnifyImageButton? = null
    private var filterCount: Typography? = null
    private var btnAddItem: UnifyImageButton? = null
    private var closeButton: UnifyImageButton? = null
    private var activate: Typography? = null
    private var deactivate: Typography? = null
    private var movetogroup: Typography? = null
    private var delete: UnifyImageButton? = null

    private lateinit var recyclerView: RecyclerView

    @Inject
    lateinit var presenter: TopAdsDashboardPresenter

    @Inject
    lateinit var userSession: UserSessionInterface
    private var dataStatistic: DataStatistic? = null
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
    private var currentDateText: String = ""


    companion object {
        private const val CUREENTY_ACTIVATED = 1
        fun createInstance(): TopAdsHeadlineBaseFragment {
            return TopAdsHeadlineBaseFragment()
        }
    }

    private val groupFilterSheet: TopadsGroupFilterSheet by lazy {
        TopadsGroupFilterSheet.newInstance(context)
    }

    override fun getLayoutId(): Int {
        return R.layout.topads_dash_headline_layout
    }

    override fun setUpView(view: View) {
        loader = view.findViewById(R.id.loader)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        emptyView = view.findViewById(R.id.empty_view)
        headlineList = view.findViewById(R.id.headlinList)
        appBarLayout2 = view.findViewById(R.id.app_bar_layout_2)
        hariIni = view.findViewById(R.id.hari_ini)
        textTitle = view.findViewById(R.id.text_title)
        textDesc = view.findViewById(R.id.text_desc)
        mulaiBeriklan = view.findViewById(R.id.mulai_beriklan)
        actionbar = view.findViewById(R.id.actionbar)
        pager = view.findViewById(R.id.pager)
        searchBar = view.findViewById(R.id.searchBar)
        btnFilter = view.findViewById(R.id.btnFilter)
        filterCount = view.findViewById(R.id.filterCount)
        btnAddItem = view.findViewById(R.id.btnAddItem)
        closeButton = view.findViewById(R.id.close_butt)
        activate = view.findViewById(R.id.activate)
        deactivate = view.findViewById(R.id.deactivate)
        movetogroup = view.findViewById(R.id.movetogroup)
        delete = view.findViewById(R.id.delete)
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

    override fun getCustomDateText(customDateText: String) {
        currentDateText = customDateText
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter =
            HeadLineAdItemsListAdapter(HeadLineAdItemsAdapterTypeFactoryImpl(::startSelectMode,
                ::singleItemDelete, ::statusChange, ::editGroup, ::onGroupClicked))
    }

    private fun editGroup(groupId: String) {
        val intent =
            RouteManager.getIntent(context, ApplinkConstInternalTopAds.TOPADS_HEADLINE_ADS_EDIT)
                ?.apply {
                    putExtra(TopAdsDashboardConstant.TAB_POSITION, 0)
                    putExtra(ParamObject.GROUP_ID, groupId.toString())
                }
        activity?.startActivityForResult(intent, TopAdsDashboardConstant.EDIT_HEADLINE_REQUEST_CODE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loader?.visibility = View.VISIBLE
        loadStatisticsData()
        btnFilter?.setOnClickListener {
            groupFilterSheet.show(childFragmentManager, "")
            groupFilterSheet.showAdplacementFilter(false)
            groupFilterSheet.onSubmitClick = { fetchData() }
        }
        closeButton?.setOnClickListener {
            startSelectMode(false)
        }
        activate?.setOnClickListener {
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendHeadlineAdsEvent(
                CLICK_AKTIFKAN_LONG_PRESS,
                "{${userSession.shopId}} - {${TextUtils.join(",", getAdIds())}}",
                userSession.userId)
            performAction(TopAdsDashboardConstant.ACTION_ACTIVATE)
        }
        deactivate?.setOnClickListener {
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendHeadlineAdsEvent(
                CLICK_NONAKTIFKAN_LONG_PRESS,
                "{${userSession.shopId}} - {${TextUtils.join(",", getAdIds())}}",
                userSession.userId)
            performAction(TopAdsDashboardConstant.ACTION_DEACTIVATE)
        }
        delete?.setOnClickListener {
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendHeadlineAdsEvent(CLICK_HAPUS_LONG_PRESS,
                "{${userSession.shopId}} - {${TextUtils.join(",", getAdIds())}}",
                userSession.userId)
            showConfirmationDialog()
        }
        btnAddItem?.setOnClickListener {
            RouteManager.route(context, ApplinkConstInternalTopAds.TOPADS_CREATE_ADS)
        }
        Utils.setSearchListener(context, view, ::fetchData)

        swipeRefreshLayout?.setOnRefreshListener {
            fetchData()
            loadStatisticsData()
        }
        appBarLayout2?.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, offset ->
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
        swipeRefreshLayout?.isEnabled = state == TopAdsProductIklanFragment.State.EXPANDED
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
        presenter.getStatistic(startDate!!, endDate!!, TopAdsStatisticsType.HEADLINE_ADS,
            "-1", ::onSuccessGetStatisticsInfo)
    }

    private fun onSuccessGetStatisticsInfo(dataStatistic: DataStatistic) {
        loader?.visibility = View.GONE
        this.dataStatistic = dataStatistic
        if (this.dataStatistic != null && dataStatistic.cells.isNotEmpty()) {
            topAdsTabAdapter?.setSummary(dataStatistic.summary,
                resources.getStringArray(R.array.top_ads_tab_statistics_labels))
            topAdsTabAdapter?.hideTabforHeadline()
        }
        val fragment = pager?.let { it.adapter?.instantiateItem(it, it.currentItem) } as? Fragment
        if (fragment != null && fragment is TopAdsDashStatisticFragment) {
            fragment.showLineGraph(this.dataStatistic)
        }
    }

    private fun onGroupClicked(id: String, priceSpent: String) {
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendHeadlineAdsEvent(CLICK_GRUP_CARD,
            "{${userSession.shopId}} - {$id}",
            userSession.userId)
        val intent = Intent(context, TopAdsHeadlineAdDetailViewActivity::class.java)
        intent.putExtra(TopAdsDashboardConstant.GROUP_ID, id)
        intent.putExtra(TopAdsDashboardConstant.PRICE_SPEND, priceSpent)
        startActivityForResult(intent, HEADLINE_UPADTED)
    }

    private fun showEmptyView() {
        appBarLayout2?.visibility = View.GONE
        headlineList?.visibility = View.GONE
        emptyView?.visibility = View.VISIBLE
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendHeadlineAdsEvent(VIEW_MULAI_BERIKLAN,
            "{${userSession.shopId}}",
            userSession.userId)
        mulaiBeriklan?.setOnClickListener {
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendHeadlineAdsEvent(CLICK_MULAI_BERIKLAN,
                "{${userSession.shopId}}",
                userSession.userId)
            RouteManager.route(context, ApplinkConstInternalTopAds.TOPADS_HEADLINE_ADS_CREATION)
        }
        emptyView?.findViewById<ImageUnify>(R.id.image_empty)
            ?.setImageDrawable(context?.getResDrawable(R.drawable.topads_dashboard_empty_product))
        textTitle?.text = getString(R.string.topads_headline_empty_state_title)
        textDesc?.text = getString(R.string.topads_headline_empty_state_desc)
        hariIni?.visibility = View.GONE
    }

    private fun singleItemDelete(pos: Int) {
        singleDelGroupId = (adapter.items[pos] as HeadLineAdItemsItemModel).data.groupId.toString()
        performAction(TopAdsDashboardConstant.ACTION_DELETE)
    }

    private fun statusChange(pos: Int, status: Int) {
        if (status != CUREENTY_ACTIVATED)
            presenter.setGroupAction(::onSuccessAction, TopAdsDashboardConstant.ACTION_ACTIVATE,
                listOf((adapter.items[pos] as HeadLineAdItemsItemModel).data.groupId.toString()),
                resources)
        else
            presenter.setGroupAction(::onSuccessAction, TopAdsDashboardConstant.ACTION_DEACTIVATE,
                listOf((adapter.items[pos] as HeadLineAdItemsItemModel).data.groupId.toString()),
                resources)
    }

    private fun performAction(actionActivate: String) {
        if (actionActivate == TopAdsDashboardConstant.ACTION_DELETE) {
            view?.let {
                val desc = if (getAdIds().size > 1)
                    String.format(getString(R.string.topads_dash_headline_delete_toast_multiple),
                        getAdIds().size)
                else
                    getString(R.string.topads_dash_headline_delete_toast)
                Toaster.make(it,
                    desc,
                    TopAdsDashboardConstant.TOASTER_DURATION.toInt(),
                    Toaster.TYPE_NORMAL,
                    getString(com.tokopedia.topads.common.R.string.topads_common_batal),
                    View.OnClickListener {
                        deleteCancel = true
                    })
            }
            val coroutineScope = CoroutineScope(Dispatchers.Main)
            coroutineScope.launch {
                delay(TopAdsDashboardConstant.TOASTER_DURATION)
                if (activity != null && isAdded) {
                    if (!deleteCancel)
                        presenter.setGroupAction(::onSuccessAction,
                            actionActivate, getAdIds(), resources)
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
            dialog.setTitle(String.format(getString(R.string.topads_dash_headline_bulk_delete_title),
                adapter.getSelectedItems().size))
            dialog.setDescription(getString(R.string.topads_dasg_headline_bulk_delete_desc))
            dialog.setPrimaryCTAText(getString(com.tokopedia.topads.common.R.string.topads_common_cancel_btn))
            dialog.setSecondaryCTAText(getString(R.string.topads_dash_ya_hapus))
            dialog.setPrimaryCTAClickListener {
                dialog.dismiss()
            }
            dialog.setSecondaryCTAClickListener {
                TopAdsCreateAnalytics.topAdsCreateAnalytics.sendHeadlineAdsEvent(
                    CLICK_YA_HAPUS_LONG_PRESS,
                    "{${userSession.shopId}} - {${TextUtils.join(",", getAdIds())}}",
                    userSession.userId)
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
            actionbar?.visibility = View.VISIBLE
            movetogroup?.visibility = View.GONE
            btnAddItem?.visibility = View.VISIBLE
        } else {
            adapter.setSelectMode(false)
            actionbar?.visibility = View.GONE
            btnAddItem?.visibility = View.GONE
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
        presenter.getGroupData(currentPage,
            searchBar?.searchBarTextField?.text.toString(),
            groupFilterSheet.getSelectedSortId(), groupFilterSheet.getSelectedStatusId(),
            Utils.format.format(startDate), Utils.format.format(endDate),
            GROUP_TYPE_HEADLINE, this::onSuccessGroupResult)
    }

    private fun onSuccessGroupResult(response: GroupItemResponse.GetTopadsDashboardGroups) {
        val totalCount = response.meta.page.total
        totalPage = (totalCount / response.meta.page.perPage) + 1
        recyclerviewScrollListener.updateStateAfterGetData()
        loader?.visibility = View.GONE
        response.data.forEach {
            groupIds.add(it.groupId.toString())
            adapter.items.add(HeadLineAdItemsItemModel(it))
        }
        if (adapter.items.size.isZero()) {
            onEmptyResult()
        } else if (groupIds.isNotEmpty()) {
            presenter.getGroupStatisticsData(1, ",", "", 0, "",
                "", groupIds, ::onSuccessStatistics)
            presenter.getCountProductKeyword(resources, groupIds, ::onSuccessCount)
        }
        setFilterCount()
    }

    private fun onSuccessStatistics(statistics: GetTopadsDashboardGroupStatistics) {
        adapter.setstatistics(statistics.data)
    }

    private fun onSuccessCount(countList: List<CountDataItem>) {
        adapter.setItemCount(countList)
        loader?.visibility = View.GONE
    }

    private fun setFilterCount() {
        if (!groupFilterSheet.getFilterCount().isZero()) {
            filterCount?.visibility = View.VISIBLE
            filterCount?.text = groupFilterSheet.getFilterCount().toString()
        } else
            filterCount?.visibility = View.GONE
    }

    private fun onEmptyResult() {
        adapter.items.add(HeadLineAdItemsEmptyModel())
        if (searchBar?.searchBarTextField?.text.toString().isEmpty()) {
            adapter.setEmptyView(!TopAdsDashboardConstant.EMPTY_SEARCH_VIEW,
                groupFilterSheet.getSelectedText(context))
        } else {
            adapter.setEmptyView(TopAdsDashboardConstant.EMPTY_SEARCH_VIEW)
        }
    }

    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }

    private fun fetchData() {
        swipeRefreshLayout?.isRefreshing = false
        groupIds.clear()
        currentPageNum = 1
        loader?.visibility = View.VISIBLE
        adapter.items.clear()
        adapter.notifyDataSetChanged()
        presenter.getGroupData(currentPageNum,
            searchBar?.searchBarTextField?.text.toString(), groupFilterSheet.getSelectedSortId(),
            groupFilterSheet.getSelectedStatusId(), Utils.format.format(startDate),
            Utils.format.format(endDate), GROUP_TYPE_HEADLINE, this::onSuccessGroupResult)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == HEADLINE_UPADTED) {
            if (resultCode == Activity.RESULT_OK)
                fetchData()
        }
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