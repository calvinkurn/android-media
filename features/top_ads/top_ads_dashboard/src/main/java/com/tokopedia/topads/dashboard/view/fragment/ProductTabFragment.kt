package com.tokopedia.topads.dashboard.view.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.ACTION_ACTIVATE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.ACTION_DEACTIVATE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.ACTION_DELETE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.ACTION_MOVE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.GROUP_UPDATED
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.TOASTER_DURATION
import com.tokopedia.topads.dashboard.data.model.DashGroupListResponse
import com.tokopedia.topads.dashboard.data.model.nongroupItem.WithoutGroupDataItem
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
import kotlinx.android.synthetic.main.topads_dash_fragment_product_list.*
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
class ProductTabFragment : BaseDaggerFragment() {


    private lateinit var adapter: ProductAdapter
    private var totalProductCount = -1
    private var singleAction = false
    private var getDateCallBack: FetchDate? = null

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
        return inflater.inflate(resources.getLayout(R.layout.topads_dash_fragment_product_list), container, false)
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
        product_list.adapter = adapter
        product_list.layoutManager = LinearLayoutManager(context)
    }

    private fun startEditActivity() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalTopAds.TOPADS_EDIT_ADS)?.apply {
            putExtra(TopAdsDashboardConstant.GROUPID, arguments?.getInt(TopAdsDashboardConstant.GROUP_ID).toString())
            putExtra(TopAdsDashboardConstant.GROUPNAME, arguments?.getString(TopAdsDashboardConstant.GROUP_NAME))
        }
        startActivityForResult(intent, TopAdsDashboardConstant.EDIT_GROUP_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TopAdsDashboardConstant.EDIT_GROUP_REQUEST_CODE) {
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
        dialog.setPrimaryCTAText(context.getString(R.string.topads_common_cancel_btn))
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

    private fun onSuccessGroupList(list: List<DashGroupListResponse.GetTopadsDashboardGroups.GroupDataItem>) {
        val grouplist: MutableList<MovetoGroupViewModel> = mutableListOf()
        list.forEach {
            if (it.groupName != arguments?.getString(TopAdsDashboardConstant.GROUP_NAME))
                grouplist.add(MovetoGroupItemViewModel(it))
        }
        if (list.isEmpty()) {
            movetoGroupSheet.setButtonDisable()
            grouplist.add(MovetoGroupEmptyViewModel())
        }
        movetoGroupSheet.updateData(grouplist)
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
        loader.visibility = View.VISIBLE
        adapter.items.clear()
        adapter.notifyDataSetChanged()
        val startDate = getDateCallBack?.getStartDate() ?: ""
        val endDate = getDateCallBack?.getEndDate() ?: ""

        viewModel.getGroupProductData(resources, arguments?.getInt(TopAdsDashboardConstant.GROUP_ID)
                ?: 0, searchBar?.searchBarTextField?.text.toString(),
                groupFilterSheet.getSelectedSortId(), groupFilterSheet.getSelectedStatusId(), startDate, endDate, ::onProductFetch, ::onEmptyProduct)
    }

    private fun onProductFetch(data: List<WithoutGroupDataItem>) {
        loader.visibility = View.GONE
        if (searchBar?.searchBarTextField?.text.toString().isEmpty()
                && groupFilterSheet.getSelectedSortId() == ""
                && groupFilterSheet.getSelectedStatusId() == null) {
            totalProductCount = data.size
        }
        data.forEach {
            adapter.items.add(ProductItemViewModel(it))
        }
        adapter.notifyDataSetChanged()
        setFilterCount()
        (activity as TopAdsGroupDetailViewActivity).setProductCount(adapter.itemCount)
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
        activity?.setResult(GROUP_UPDATED)
        when (actionActivate) {
            ACTION_DELETE -> {
                view.let {
                    Toaster.make(it!!, getString(R.string.topads_without_product_del_toaster), TOASTER_DURATION.toInt(), Toaster.TYPE_NORMAL, getString(R.string.topads_common_batal), View.OnClickListener {
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