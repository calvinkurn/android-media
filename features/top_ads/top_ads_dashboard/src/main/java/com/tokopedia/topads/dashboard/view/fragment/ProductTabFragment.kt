package com.tokopedia.topads.dashboard.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.ACTION_ACTIVATE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.ACTION_DEACTIVATE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.ACTION_DELETE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.ACTION_MOVE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.TOASTER_DURATION
import com.tokopedia.topads.dashboard.data.model.CountDataItem
import com.tokopedia.topads.dashboard.data.model.GroupListDataItem
import com.tokopedia.topads.dashboard.data.model.nongroupItem.GetDashboardProductStatistics
import com.tokopedia.topads.dashboard.data.model.nongroupItem.NonGroupResponse
import com.tokopedia.topads.dashboard.data.utils.Utils
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.activity.TopAdsGroupDetailViewActivity
import com.tokopedia.topads.dashboard.view.adapter.movetogroup.viewmodel.MovetoGroupEmptyViewModel
import com.tokopedia.topads.dashboard.view.adapter.movetogroup.viewmodel.MovetoGroupItemViewModel
import com.tokopedia.topads.dashboard.view.adapter.movetogroup.viewmodel.MovetoGroupViewModel
import com.tokopedia.topads.dashboard.view.adapter.product.ProductAdapter
import com.tokopedia.topads.dashboard.view.adapter.product.ProductAdapterTypeFactoryImpl
import com.tokopedia.topads.dashboard.view.adapter.product.viewmodel.ProductEmptyViewModel
import com.tokopedia.topads.dashboard.view.adapter.product.viewmodel.ProductItemViewModel
import com.tokopedia.topads.dashboard.view.model.GroupDetailViewModel
import com.tokopedia.topads.dashboard.view.sheet.MovetoGroupSheetList
import com.tokopedia.topads.dashboard.view.sheet.TopadsGroupFilterSheet
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.topads_dash_fragment_non_group_list.actionbar
import kotlinx.android.synthetic.main.topads_dash_fragment_product_list.loader
import kotlinx.android.synthetic.main.topads_dash_layout_common_action_bar.*
import kotlinx.android.synthetic.main.topads_dash_layout_common_searchbar_layout.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Pika on 15/5/20.
 */

private const val CLICK_TAMBAH_PRODUK = "click - tambah produk"
class ProductTabFragment : BaseDaggerFragment() {


    private lateinit var adapter: ProductAdapter
    private var totalProductCount = -1
    private var singleAction = false
    private var getDateCallBack: FetchDate? = null
    private lateinit var recyclerviewScrollListener: EndlessRecyclerViewScrollListener
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var recyclerView: RecyclerView
    private var totalCount = 0
    private var totalPage = 0
    private var currentPageNum = 1
    private var adIds: MutableList<String> = mutableListOf()

    companion object {
        fun createInstance(bundle: Bundle): ProductTabFragment {
            val fragment = ProductTabFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private var deleteCancel = false

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy {
        ViewModelProviders.of(this, viewModelFactory)
    }
    private val viewModel by lazy {
        viewModelProvider.get(GroupDetailViewModel::class.java)
    }

    private val groupFilterSheet: TopadsGroupFilterSheet by lazy {
        context.run {
            TopadsGroupFilterSheet.newInstance(context!!)
        }
    }
    private val movetoGroupSheet: MovetoGroupSheetList by lazy {
        context.run {
            MovetoGroupSheetList.newInstance(context!!)
        }
    }

    override fun getScreenName(): String {
        return ProductTabFragment::class.java.name
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = ProductAdapter(ProductAdapterTypeFactoryImpl(::onCheckedChange, ::setSelectMode))
    }

    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(resources.getLayout(R.layout.topads_dash_fragment_product_list), container, false)
        recyclerView = view.findViewById(R.id.product_list)
        setAdapter()
        return view
    }

    private fun setAdapter() {
        layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerviewScrollListener = onRecyclerViewListener()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
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

    private fun fetchNextPage(page:Int) {
        loader.visibility = View.VISIBLE
        val startDate = getDateCallBack?.getStartDate() ?: ""
        val endDate = getDateCallBack?.getEndDate() ?: ""
        viewModel.getGroupProductData(resources, page, arguments?.getInt(TopAdsDashboardConstant.GROUP_ID)
                ?: 0, searchBar?.searchBarTextField?.text.toString(),
                groupFilterSheet.getSelectedSortId(), groupFilterSheet.getSelectedStatusId()
                , startDate, endDate, ::onProductFetch, ::onEmptyProduct)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchData()
        btnFilter.setOnClickListener {
            groupFilterSheet.show()
            groupFilterSheet.onSubmitClick = { fetchData() }
        }

        close_butt.setOnClickListener {
            setSelectMode(false)
        }
        activate.setOnClickListener {
            performAction(ACTION_ACTIVATE, null)
        }
        deactivate.setOnClickListener {
            performAction(ACTION_DEACTIVATE, null)
        }
        movetogroup.setOnClickListener {
            fetchgroupList("")
            movetoGroupSheet.show()
            movetoGroupSheet.onItemClick = {
                performAction(ACTION_MOVE, movetoGroupSheet.getSelectedFilter())
            }
            movetoGroupSheet.onItemSearch = {
                fetchgroupList(it)
            }
        }
        delete.setOnClickListener {
            showConfirmationDialog(context!!)
        }
        btnAddItem.setOnClickListener {
            startEditActivity()
        }
        setSearchBar()
        Utils.setSearchListener(context, view, ::fetchData)
    }

    private fun startEditActivity() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalTopAds.TOPADS_EDIT_ADS)?.apply {
            putExtra(TopAdsDashboardConstant.TAB_POSITION,0)
            putExtra(TopAdsDashboardConstant.GROUPID, arguments?.getInt(TopAdsDashboardConstant.GROUP_ID).toString())
            putExtra(TopAdsDashboardConstant.GROUPNAME, arguments?.getString(TopAdsDashboardConstant.GROUP_NAME))
        }
        startActivityForResult(intent, TopAdsDashboardConstant.EDIT_GROUP_REQUEST_CODE)
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsDashboardEvent(CLICK_TAMBAH_PRODUK, "")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TopAdsDashboardConstant.EDIT_GROUP_REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK)
                fetchData()
        }
    }

    private fun setSearchBar() {
        divider.visibility = View.VISIBLE
        btnAddItem.visibility = View.VISIBLE
    }

    private fun showConfirmationDialog(context: Context) {
        val dialog = DialogUnify(context, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        if (getAdIds().size == adapter.itemCount) {
            dialog.setTitle(context.getString(R.string.topads_dash_delete_all_product_title))
            dialog.setDescription(context.getString(R.string.topads_dash_delete_all_product_desc))
        } else
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

    fun fetchgroupList(search: String) {
        movetoGroupSheet.updateData(mutableListOf())
        viewModel.getGroupList(resources, search, ::onSuccessGroupList)
    }

    private fun onSuccessGroupList(list: List<GroupListDataItem>) {
        val groupList: MutableList<MovetoGroupViewModel> = mutableListOf()
        val groupIds: MutableList<String> = mutableListOf()

        list.forEach {
            if (it.groupName != arguments?.getString(TopAdsDashboardConstant.GROUP_NAME)) {
                groupList.add(MovetoGroupItemViewModel(it))
                groupIds.add(it.groupId.toString())
            }
        }
        if (list.isEmpty()) {
            movetoGroupSheet.setButtonDisable()
            groupList.add(MovetoGroupEmptyViewModel())
        } else
            viewModel.getCountProductKeyword(resources, groupIds,::onSuccessCount)
        movetoGroupSheet.updateData(groupList)
    }

    private fun onSuccessCount(countList: List<CountDataItem>) {
        movetoGroupSheet.updateKeyCount(countList)
        loader.visibility = View.GONE
    }

    private fun getAdIds(): MutableList<String> {
        val ads: MutableList<String> = mutableListOf()
        adapter.getSelectedItems().forEach {
            ads.add(it.data.adId.toString())
        }
        return ads
    }

    private fun onCheckedChange(pos: Int, isChecked: Boolean) {
        singleAction = true
        val actionActivate: String = if (isChecked)
            ACTION_ACTIVATE
        else
            ACTION_DEACTIVATE
        viewModel.setProductAction(::onSuccessAction, actionActivate,
                listOf((adapter.items[pos] as ProductItemViewModel).data.adId.toString()), resources, null)
    }

    private fun onSuccessAction() {
        if (!singleAction) {
            setSelectMode(false)
            fetchData()
        }
        singleAction = false
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

    private fun fetchData() {
        adIds.clear()
        currentPageNum = 1
        loader.visibility = View.VISIBLE
        adapter.items.clear()
        adapter.notifyDataSetChanged()
        val startDate = getDateCallBack?.getStartDate() ?: ""
        val endDate = getDateCallBack?.getEndDate() ?: ""

        viewModel.getGroupProductData(resources, 1, arguments?.getInt(TopAdsDashboardConstant.GROUP_ID)
                ?: 0, searchBar?.searchBarTextField?.text.toString(),
                groupFilterSheet.getSelectedSortId(), groupFilterSheet.getSelectedStatusId(), startDate, endDate, ::onProductFetch, ::onEmptyProduct)
    }

    private fun onProductFetch(response: NonGroupResponse.TopadsDashboardGroupProducts) {
        totalCount = response.meta.page.total
        totalPage = (totalCount / response.meta.page.perPage)  + 1
        loader.visibility = View.GONE
        if (searchBar?.searchBarTextField?.text.toString().isEmpty()
                && groupFilterSheet.getSelectedSortId() == ""
                && groupFilterSheet.getSelectedStatusId() == null) {
            totalProductCount = totalCount
        }
        response.data.forEach {
            adIds.add(it.adId.toString())
            adapter.items.add(ProductItemViewModel(it))
        }
        if(adIds.isNotEmpty()) {
            val startDate = getDateCallBack?.getStartDate() ?: ""
            val endDate = getDateCallBack?.getEndDate() ?: ""
            viewModel.getProductStats(resources, startDate, endDate, adIds, ::onSuccessStats)
        }
        setFilterCount()
        (activity as TopAdsGroupDetailViewActivity).setProductCount(totalCount)
    }

    private fun onSuccessStats(stats: GetDashboardProductStatistics) {
        adapter.setstatistics(stats.data)
    }

    private fun setFilterCount() {
        if (!groupFilterSheet.getFilterCount().isZero()) {
            filterCount.visibility = View.VISIBLE
            filterCount.text = groupFilterSheet.getFilterCount().toString()
        } else
            filterCount.visibility = View.GONE
    }

    private fun onEmptyProduct() {
        adapter.items.add(ProductEmptyViewModel())
        (activity as TopAdsGroupDetailViewActivity).setProductCount(0)
        adapter.notifyDataSetChanged()
        setFilterCount()
    }

    private fun performAction(actionActivate: String, selectedFilter: String?) {
        activity?.setResult(Activity.RESULT_OK)
        when (actionActivate) {
            ACTION_DELETE -> {
                view.let {
                    Toaster.make(it!!, getString(R.string.topads_without_product_del_toaster), TOASTER_DURATION.toInt(), Toaster.TYPE_NORMAL, getString(com.tokopedia.topads.common.R.string.topads_common_batal), View.OnClickListener {
                        deleteCancel = true
                    })
                }
                val coroutineScope = CoroutineScope(Dispatchers.Main)
                coroutineScope.launch {
                    delay(TOASTER_DURATION)
                    if (!deleteCancel) {
                        totalProductCount -= getAdIds().size
                        viewModel.setProductAction(::onSuccessAction, actionActivate, getAdIds(), resources, selectedFilter)
                        if (totalProductCount == 0) {
                            viewModel.setGroupAction(ACTION_DELETE, listOf(arguments?.getInt(TopAdsDashboardConstant.GROUP_ID).toString()),
                                    resources)
                            activity?.finish()
                        }
                    }
                    deleteCancel = false
                    setSelectMode(false)
                }
            }
            ACTION_MOVE -> {
                totalProductCount -= getAdIds().size
                viewModel.setProductAction(::onSuccessAction, actionActivate, getAdIds(), resources, selectedFilter)
                if (totalProductCount == 0) {
                    viewModel.setGroupAction(ACTION_DELETE, listOf(arguments?.getInt(TopAdsDashboardConstant.GROUP_ID).toString()), resources)
                    activity?.finish()
                }
            }
            else -> {
                viewModel.setProductAction(::onSuccessAction, actionActivate, getAdIds(), resources, selectedFilter)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FetchDate)
            getDateCallBack = context
    }

    override fun onDetach() {
        super.onDetach()
        getDateCallBack = null
    }

    interface FetchDate {
        fun getStartDate(): String
        fun getEndDate(): String
    }

}