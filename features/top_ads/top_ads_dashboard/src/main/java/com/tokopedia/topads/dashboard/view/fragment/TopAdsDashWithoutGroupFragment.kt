package com.tokopedia.topads.dashboard.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.topads.common.data.model.GroupListDataItem
import com.tokopedia.topads.common.data.response.nongroupItem.GetDashboardProductStatistics
import com.tokopedia.topads.common.data.response.nongroupItem.NonGroupResponse
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.ACTION_DELETE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.TOASTER_DURATION
import com.tokopedia.topads.dashboard.data.model.CountDataItem
import com.tokopedia.topads.dashboard.data.utils.Utils
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.adapter.movetogroup.viewmodel.MovetoGroupEmptyModel
import com.tokopedia.topads.dashboard.view.adapter.movetogroup.viewmodel.MovetoGroupItemModel
import com.tokopedia.topads.dashboard.view.adapter.movetogroup.viewmodel.MovetoGroupModel
import com.tokopedia.topads.dashboard.view.adapter.non_group_item.NonGroupItemsAdapterTypeFactoryImpl
import com.tokopedia.topads.dashboard.view.adapter.non_group_item.NonGroupItemsListAdapter
import com.tokopedia.topads.dashboard.view.adapter.non_group_item.viewmodel.NonGroupItemsEmptyModel
import com.tokopedia.topads.dashboard.view.adapter.non_group_item.viewmodel.NonGroupItemsItemModel
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDashboardPresenter
import com.tokopedia.topads.dashboard.view.sheet.MovetoGroupSheetList
import com.tokopedia.topads.dashboard.view.sheet.TopadsGroupFilterSheet
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.topads_dash_fragment_non_group_list.*
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

class TopAdsDashWithoutGroupFragment : BaseDaggerFragment() {

    private lateinit var adapter: NonGroupItemsListAdapter
    private val EMPTY_SEARCH_VIEW = true
    private val PRODUCTS_WITHOUT_GROUP = -2
    private var deleteCancel = false
    private var SingleDelGroupId = ""
    private lateinit var recyclerviewScrollListener: EndlessRecyclerViewScrollListener
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var recyclerView: RecyclerView
    private var totalCount = 0
    private var totalPage = 0
    private var currentPageNum = 1
    private var adIds: MutableList<String> = mutableListOf()

    @Inject
    lateinit var topAdsDashboardPresenter: TopAdsDashboardPresenter

    private val groupFilterSheet: TopadsGroupFilterSheet by lazy {
            TopadsGroupFilterSheet.newInstance(context)
    }

    private val movetoGroupSheet: MovetoGroupSheetList by lazy {
        MovetoGroupSheetList.newInstance(requireContext())
    }

    override fun getScreenName(): String {
        return TopAdsDashWithoutGroupFragment::class.java.name
    }

    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(resources.getLayout(R.layout.topads_dash_fragment_non_group_list), container, false)
        recyclerView = view.findViewById(R.id.non_group_list)
        setAdapter()
        return view
    }

    private fun setAdapter() {
        layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerviewScrollListener = onRecyclerViewListener()
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.adapter = adapter
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
        val startDate = Utils.format.format((parentFragment as TopAdsProductIklanFragment).startDate)
        val endDate = Utils.format.format((parentFragment as TopAdsProductIklanFragment).endDate)
        topAdsDashboardPresenter.getGroupProductData(page, PRODUCTS_WITHOUT_GROUP, searchBar?.searchBarTextField?.text.toString(),
                groupFilterSheet.getSelectedSortId(), groupFilterSheet.getSelectedStatusId(),
                startDate, endDate, ::onSuccessResult, ::onEmptyResult)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = NonGroupItemsListAdapter(NonGroupItemsAdapterTypeFactoryImpl(::setSelectMode, ::singleItemDelete,
                ::statusChange, ::onEditProduct))
    }

    private fun setSelectMode(select: Boolean) {
        if (select) {
            adapter.setSelectMode(true)
            actionbar.visibility = View.VISIBLE
        } else {
            adapter.setSelectMode(false)
            actionbar.visibility = View.GONE
        }
    }

    private fun onEditProduct(groupId: Int, adPriceBid: Int) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalTopAds.TOPADS_EDIT_WITHOUT_GROUP).apply {
            putExtra(TopAdsDashboardConstant.GROUPID, groupId.toString())
            putExtra(TopAdsDashboardConstant.PRICEBID, adPriceBid)
        }
        startActivityForResult(intent, TopAdsDashboardConstant.EDIT_WITHOUT_GROUP_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TopAdsDashboardConstant.EDIT_WITHOUT_GROUP_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK)
                fetchData()
        }
    }

    private fun singleItemDelete(pos: Int) {
        SingleDelGroupId = (adapter.items[pos] as NonGroupItemsItemModel).data.adId.toString()
        performAction(ACTION_DELETE, null)
    }

    private fun statusChange(pos: Int, status: Int) {
        if (status != 1) {
            topAdsDashboardPresenter.setProductAction(::onSuccessAction, TopAdsDashboardConstant.ACTION_ACTIVATE,
                    listOf((adapter.items[pos] as NonGroupItemsItemModel).data.adId.toString()), null)
        } else {
            topAdsDashboardPresenter.setProductAction(::onSuccessAction, TopAdsDashboardConstant.ACTION_DEACTIVATE,
                    listOf((adapter.items[pos] as NonGroupItemsItemModel).data.adId.toString()), null)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchData()
        btnFilter.setOnClickListener {
            groupFilterSheet.show(childFragmentManager, "")
            groupFilterSheet.onSubmitClick = { fetchData() }
        }

        close_butt.setOnClickListener {
            setSelectMode(false)
        }
        activate.setOnClickListener {
            performAction(TopAdsDashboardConstant.ACTION_ACTIVATE, null)

        }
        deactivate.setOnClickListener {
            performAction(TopAdsDashboardConstant.ACTION_DEACTIVATE, null)
        }
        movetogroup.setOnClickListener {
            fetchgroupList("")
            movetoGroupSheet.show()
            movetoGroupSheet.onItemClick = {
                performAction(TopAdsDashboardConstant.ACTION_MOVE, movetoGroupSheet.getSelectedFilter())
            }
            movetoGroupSheet.onItemSearch = {
                fetchgroupList(it)
            }
        }
        delete.setOnClickListener {
            showConfirmationDialog(requireContext())
        }
        Utils.setSearchListener(context, view, ::fetchData)
    }

    private fun fetchgroupList(search: String) {
        movetoGroupSheet.updateData(mutableListOf())
        topAdsDashboardPresenter.getGroupList(search, ::onSuccessGroupList)
    }

    private fun onSuccessGroupList(list: List<GroupListDataItem>) {
        val groupList: MutableList<MovetoGroupModel> = mutableListOf()
        val groupIds: MutableList<String> = mutableListOf()
        recyclerviewScrollListener.updateStateAfterGetData()
        list.forEach {
            groupList.add(MovetoGroupItemModel(it))
            groupIds.add(it.groupId)
        }
        if (list.isEmpty()) {
            movetoGroupSheet.setButtonDisable()
            groupList.add(MovetoGroupEmptyModel())
        } else
            topAdsDashboardPresenter.getCountProductKeyword(resources, groupIds, ::onSuccessCount)

        movetoGroupSheet.updateData(groupList)
    }

    private fun onSuccessCount(countList: List<CountDataItem>) {
        movetoGroupSheet.updateKeyCount(countList)
        loader.visibility = View.GONE
    }

    private fun showConfirmationDialog(context: Context) {
        val dialog = DialogUnify(context, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setTitle(String.format(context.getString(R.string.topads_dash_confirm_delete_product), adapter.getSelectedItems().size))
        dialog.setPrimaryCTAText(context.getString(com.tokopedia.topads.common.R.string.topads_common_cancel_btn))
        dialog.setSecondaryCTAText(context.getString(R.string.topads_dash_ya_hapus))
        dialog.setPrimaryCTAClickListener {
            dialog.dismiss()
        }
        dialog.setSecondaryCTAClickListener {
            dialog.dismiss()
            performAction(ACTION_DELETE, null)
        }
        dialog.show()
    }

    private fun performAction(actionActivate: String, selectedFilter: String?) {
        if (actionActivate == ACTION_DELETE) {
            view.let {
                view.let {
                    Toaster.make(it!!, getString(R.string.topads_without_product_del_toaster), TOASTER_DURATION.toInt(), Toaster.TYPE_NORMAL, getString(com.tokopedia.topads.common.R.string.topads_common_batal), View.OnClickListener {
                        deleteCancel = true
                    })
                }
            }
            val coroutineScope = CoroutineScope(Dispatchers.Main)
            coroutineScope.launch {
                delay(TOASTER_DURATION)
                if (activity != null && isAdded) {
                    if (!deleteCancel)
                        topAdsDashboardPresenter.setProductAction(::onSuccessAction, actionActivate, getAdIds(), selectedFilter)
                    SingleDelGroupId = ""
                    deleteCancel = false
                    setSelectMode(false)
                }
            }
        } else {
            topAdsDashboardPresenter.setProductAction(::onSuccessAction, actionActivate, getAdIds(), selectedFilter)
            SingleDelGroupId = ""
        }

    }

    private fun getAdIds(): MutableList<String> {
        val ads: MutableList<String> = mutableListOf()
        return if (SingleDelGroupId.isEmpty()) {
            adapter.getSelectedItems().forEach {
                ads.add(it.data.adId.toString())
            }
            ads
        } else {
            mutableListOf(SingleDelGroupId)

        }
    }

    private fun onSuccessAction() {
        setSelectMode(false)
        fetchData()
    }

    private fun onEmptyResult() {
        adapter.items.add(NonGroupItemsEmptyModel())
        if (searchBar?.searchBarTextField?.text.toString().isEmpty())
            adapter.setEmptyView(!EMPTY_SEARCH_VIEW)
        else
            adapter.setEmptyView(EMPTY_SEARCH_VIEW)
        setFilterCount()
        (parentFragment as TopAdsProductIklanFragment).setNonGroupCount(0)
    }

    private fun onSuccessResult(response: NonGroupResponse.TopadsDashboardGroupProducts) {
        totalCount = response.meta.page.total
        totalPage = if (totalCount % response.meta.page.perPage == 0) {
            totalCount / response.meta.page.perPage
        } else
            (totalCount / response.meta.page.perPage) + 1
        recyclerviewScrollListener.updateStateAfterGetData()
        loader.visibility = View.GONE
        response.data.forEach {
            adIds.add(it.adId.toString())
            adapter.items.add(NonGroupItemsItemModel(it))
        }
        if (adIds.isNotEmpty()) {
            val startDate = Utils.format.format((parentFragment as TopAdsProductIklanFragment).startDate)
            val endDate = Utils.format.format((parentFragment as TopAdsProductIklanFragment).endDate)
            topAdsDashboardPresenter.getProductStats(resources, startDate, endDate, adIds, groupFilterSheet.getSelectedSortId(), groupFilterSheet.getSelectedStatusId()
                    ?: 0, ::OnSuccessStats)
        }
        (parentFragment as TopAdsProductIklanFragment).setNonGroupCount(totalCount)
        setFilterCount()
    }

    private fun OnSuccessStats(stats: GetDashboardProductStatistics) {
        adapter.setstatistics(stats.data)
    }

    private fun setFilterCount() {
        if (!groupFilterSheet.getFilterCount().isZero()) {
            filterCount.visibility = View.VISIBLE
            filterCount.text = groupFilterSheet.getFilterCount().toString()
        } else
            filterCount.visibility = View.GONE
    }

    private fun fetchData() {
        adIds.clear()
        currentPageNum = 1
        loader.visibility = View.VISIBLE
        adapter.items.clear()
        adapter.notifyDataSetChanged()
        val startDate = Utils.format.format((parentFragment as TopAdsProductIklanFragment).startDate)
        val endDate = Utils.format.format((parentFragment as TopAdsProductIklanFragment).endDate)
        topAdsDashboardPresenter.getGroupProductData(1, PRODUCTS_WITHOUT_GROUP, searchBar?.searchBarTextField?.text.toString(),
                groupFilterSheet.getSelectedSortId(), groupFilterSheet.getSelectedStatusId(),
                startDate, endDate, ::onSuccessResult, ::onEmptyResult)
    }
}