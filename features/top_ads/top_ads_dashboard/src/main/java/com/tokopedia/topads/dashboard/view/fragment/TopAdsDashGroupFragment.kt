package com.tokopedia.topads.dashboard.view.fragment

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.applink.AppUtil
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.data.response.groupitem.GetTopadsDashboardGroupStatistics
import com.tokopedia.topads.common.data.response.groupitem.GroupItemResponse
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.ACTION_ACTIVATE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.ACTION_DEACTIVATE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.EDIT_GROUP_REQUEST_CODE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.EMPTY_SEARCH_VIEW
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.GROUP_TYPE_PRODUCT
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.GROUP_UPDATED
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.SELLER_PACKAGENAME
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.TOASTER_DURATION
import com.tokopedia.topads.dashboard.data.model.CountDataItem
import com.tokopedia.topads.dashboard.data.utils.Utils
import com.tokopedia.topads.dashboard.data.utils.Utils.format
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.activity.TopAdsGroupDetailViewActivity
import com.tokopedia.topads.dashboard.view.adapter.group_item.GroupItemsAdapterTypeFactoryImpl
import com.tokopedia.topads.dashboard.view.adapter.group_item.GroupItemsListAdapter
import com.tokopedia.topads.dashboard.view.adapter.group_item.viewmodel.GroupItemsEmptyModel
import com.tokopedia.topads.dashboard.view.adapter.group_item.viewmodel.GroupItemsItemModel
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDashboardPresenter
import com.tokopedia.topads.dashboard.view.sheet.TopadsGroupFilterSheet
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.topads_dash_fragment_group_list.*
import kotlinx.android.synthetic.main.topads_dash_layout_common_action_bar.*
import kotlinx.android.synthetic.main.topads_dash_layout_common_searchbar_layout.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Pika on 2/6/20.
 */

private const val CLICK_GROUP_TITLE = "click - group title"

class TopAdsDashGroupFragment : BaseDaggerFragment() {

    private lateinit var adapter: GroupItemsListAdapter
    private val CUREENTY_ACTIVATED = 1
    private var SingleDelGroupId = ""
    private lateinit var recyclerviewScrollListener: EndlessRecyclerViewScrollListener
    private lateinit var layoutManager: LinearLayoutManager

    @Inject
    lateinit var topAdsDashboardPresenter: TopAdsDashboardPresenter
    private var deleteCancel = false
    private lateinit var recyclerView: RecyclerView
    private var totalCount = 0
    private var totalPage = 0
    private var currentPageNum = 1
    val groupIds: MutableList<String> = mutableListOf()
    private lateinit var loader: LoaderUnify


    override fun getScreenName(): String {
        return TopAdsDashGroupFragment::class.java.name
    }

    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }

    private val groupFilterSheet: TopadsGroupFilterSheet by lazy {
        TopadsGroupFilterSheet.newInstance(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(resources.getLayout(R.layout.topads_dash_fragment_group_list), container, false)
        recyclerView = view.findViewById(R.id.group_list)
        loader = view.findViewById(R.id.loader)
        initAdapter()
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = GroupItemsListAdapter(GroupItemsAdapterTypeFactoryImpl(::startSelectMode,
                ::singleItemDelete, ::statusChange, ::editGroup, ::onGroupClicked))
    }

    private fun editGroup(groupId: Int) {
        if (AppUtil.isSellerInstalled(context)) {
            val intent = RouteManager.getIntent(context, ApplinkConstInternalTopAds.TOPADS_EDIT_ADS)?.apply {
                putExtra(TopAdsDashboardConstant.TAB_POSITION, 2)
                putExtra(TopAdsDashboardConstant.GROUPID, groupId.toString())
            }
            startActivityForResult(intent, EDIT_GROUP_REQUEST_CODE)

        } else {
            RouteManager.route(context, ApplinkConstInternalMechant.MERCHANT_REDIRECT_CREATE_SHOP)
        }
    }

    private fun onGroupClicked(id: Int, priceSpent: String, groupName: String) {
        if (AppUtil.isSellerInstalled(context)) {
            val intent = Intent(context, TopAdsGroupDetailViewActivity::class.java)
            intent.putExtra(TopAdsDashboardConstant.GROUP_ID, id)
            intent.putExtra(TopAdsDashboardConstant.PRICE_SPEND, priceSpent)
            intent.component = ComponentName(SELLER_PACKAGENAME, TopAdsGroupDetailViewActivity::class.java.name)
            startActivityForResult(intent, GROUP_UPDATED)
        } else {
            RouteManager.route(context, ApplinkConstInternalMechant.MERCHANT_REDIRECT_CREATE_SHOP)
        }
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsDashboardEvent(CLICK_GROUP_TITLE, groupName)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EDIT_GROUP_REQUEST_CODE || requestCode == GROUP_UPDATED) {
            if (resultCode == Activity.RESULT_OK)
                fetchData()
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
        val startDate = format.format((parentFragment as TopAdsProductIklanFragment).startDate)
        val endDate = format.format((parentFragment as TopAdsProductIklanFragment).endDate)
        topAdsDashboardPresenter.getGroupData(currentPage, searchBar?.searchBarTextField?.text.toString(), groupFilterSheet.getSelectedSortId(),
                groupFilterSheet.getSelectedStatusId(), startDate,
                endDate, GROUP_TYPE_PRODUCT, this::onSuccessGroupResult)
    }

    private fun statusChange(pos: Int, status: Int) {
        if (status != CUREENTY_ACTIVATED)
            topAdsDashboardPresenter.setGroupAction(::onSuccessAction, ACTION_ACTIVATE,
                    listOf((adapter.items[pos] as GroupItemsItemModel).data.groupId.toString()), resources)
        else
            topAdsDashboardPresenter.setGroupAction(::onSuccessAction, ACTION_DEACTIVATE,
                    listOf((adapter.items[pos] as GroupItemsItemModel).data.groupId.toString()), resources)
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

    private fun singleItemDelete(pos: Int) {
        SingleDelGroupId = (adapter.items[pos] as GroupItemsItemModel).data.groupId.toString()
        performAction(TopAdsDashboardConstant.ACTION_DELETE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchData()
        btnFilter.setOnClickListener {
            groupFilterSheet.show(childFragmentManager, "")
            groupFilterSheet.onSubmitClick = { fetchData() }
        }
        close_butt.setOnClickListener {
            startSelectMode(false)
        }
        activate.setOnClickListener {
            performAction(ACTION_ACTIVATE)
        }
        deactivate.setOnClickListener {
            performAction(ACTION_DEACTIVATE)
        }

        delete.setOnClickListener {
            showConfirmationDialog()
        }
        btnAddItem.setOnClickListener {
            RouteManager.route(context, ApplinkConstInternalTopAds.TOPADS_CREATE_ADS)
        }
        Utils.setSearchListener(context, view, ::fetchData)
    }

    private fun showConfirmationDialog() {
        context?.let {
            val dialog = DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
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

    }

    private fun onEmptyResult() {
        adapter.items.add(GroupItemsEmptyModel())
        if (searchBar?.searchBarTextField?.text.toString().isEmpty()) {
            adapter.setEmptyView(!EMPTY_SEARCH_VIEW)
        } else {
            adapter.setEmptyView(EMPTY_SEARCH_VIEW)
        }
        (parentFragment as TopAdsProductIklanFragment).setGroupCount(0)
    }

    private fun setFilterCount() {
        if (!groupFilterSheet.getFilterCount().isZero()) {
            filterCount.visibility = View.VISIBLE
            filterCount.text = groupFilterSheet.getFilterCount().toString()
        } else
            filterCount.visibility = View.GONE
    }

    private fun fetchData() {
        groupIds.clear()
        currentPageNum = 1
        loader.visibility = View.VISIBLE
        adapter.items.clear()
        adapter.notifyDataSetChanged()
        val startDate = format.format((parentFragment as TopAdsProductIklanFragment).startDate)
        val endDate = format.format((parentFragment as TopAdsProductIklanFragment).endDate)
        topAdsDashboardPresenter.getGroupData(1, searchBar?.searchBarTextField?.text.toString(), groupFilterSheet.getSelectedSortId(),
                groupFilterSheet.getSelectedStatusId(), startDate,
                endDate, 1, this::onSuccessGroupResult)
    }

    private fun onSuccessGroupResult(response: GroupItemResponse.GetTopadsDashboardGroups) {
        totalCount = response.meta.page.total
        totalPage = if (totalCount % response.meta.page.perPage == 0) {
            totalCount / response.meta.page.perPage
        } else
            (totalCount / response.meta.page.perPage) + 1
        recyclerviewScrollListener.updateStateAfterGetData()
        loader.visibility = View.GONE
        response.data.forEach {
            groupIds.add(it.groupId.toString())
            adapter.items.add(GroupItemsItemModel(it))
        }
        (parentFragment as TopAdsProductIklanFragment).setGroupCount(totalCount)
        if (adapter.items.size.isZero()) {
            onEmptyResult()
        } else if (groupIds.isNotEmpty()) {
            val startDate = format.format((parentFragment as TopAdsProductIklanFragment).startDate)
            val endDate = format.format((parentFragment as TopAdsProductIklanFragment).endDate)
            topAdsDashboardPresenter.getGroupStatisticsData(1, ",", "", 0, startDate,
                    endDate, groupIds, ::onSuccessStatistics)
            topAdsDashboardPresenter.getCountProductKeyword(resources, groupIds, ::onSuccessCount)
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

    private fun performAction(actionActivate: String) {
        if (actionActivate == TopAdsDashboardConstant.ACTION_DELETE) {
            view.let {
                Toaster.make(it!!, getString(R.string.topads_dash_with_grup_delete_toast), TOASTER_DURATION.toInt(), Toaster.TYPE_NORMAL, getString(com.tokopedia.topads.common.R.string.topads_common_batal), View.OnClickListener {
                    deleteCancel = true
                })
            }
            val coroutineScope = CoroutineScope(Dispatchers.Main)
            coroutineScope.launch {
                delay(TOASTER_DURATION)
                if (activity != null && isAdded) {
                    if (!deleteCancel)
                        topAdsDashboardPresenter.setGroupAction(::onSuccessAction, actionActivate, getAdIds(), resources)
                    deleteCancel = false
                    startSelectMode(false)
                    SingleDelGroupId = ""
                }
            }
        } else {
            topAdsDashboardPresenter.setGroupAction(::onSuccessAction, actionActivate, getAdIds(), resources)
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

    private fun onSuccessAction(action: String) {
        startSelectMode(false)
        fetchData()
    }
}