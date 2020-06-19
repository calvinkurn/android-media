package com.tokopedia.topads.dashboard.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.ACTION_ACTIVATE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.ACTION_DEACTIVATE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.EMPTY_SEARCH_VIEW
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.TOASTER_DURATION
import com.tokopedia.topads.dashboard.data.model.groupitem.DataItem
import com.tokopedia.topads.dashboard.data.utils.Utils
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.adapter.group_item.GroupItemsAdapterTypeFactoryImpl
import com.tokopedia.topads.dashboard.view.adapter.group_item.GroupItemsListAdapter
import com.tokopedia.topads.dashboard.view.adapter.group_item.viewmodel.GroupItemsEmptyViewModel
import com.tokopedia.topads.dashboard.view.adapter.group_item.viewmodel.GroupItemsItemViewModel
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDashboardPresenter
import com.tokopedia.topads.dashboard.view.sheet.TopadsGroupFilterSheet
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_top_ads_dashboard.*
import kotlinx.android.synthetic.main.topads_dash_fragment_auto_ads_list.*
import kotlinx.android.synthetic.main.topads_dash_layout_common_action_bar.*
import kotlinx.android.synthetic.main.topads_dash_layout_common_searchbar_layout.*
import kotlinx.android.synthetic.main.topads_dash_fragment_group_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Pika on 2/6/20.
 */

class TopAdsDashGroupFragment : BaseDaggerFragment() {

    private lateinit var adapter: GroupItemsListAdapter

    @Inject
    lateinit var topAdsDashboardPresenter: TopAdsDashboardPresenter
    private var deleteCancel = false


    override fun getScreenName(): String {
        return TopAdsDashGroupFragment::class.java.name
    }

    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }

    private val groupFilterSheet: TopadsGroupFilterSheet by lazy {
        context.run {
            TopadsGroupFilterSheet.newInstance(context!!)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(resources.getLayout(R.layout.topads_dash_fragment_group_list), container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = GroupItemsListAdapter(GroupItemsAdapterTypeFactoryImpl(::startSelectMode, ::singleItemDelete, ::statusChange))
    }

    private fun initAdapter() {
        group_list.isNestedScrollingEnabled = false
        group_list.adapter = adapter
        group_list.layoutManager = LinearLayoutManager(context)
    }

    private fun statusChange(pos: Int, status: Int) {
        if (status != 1)
            topAdsDashboardPresenter.setGroupAction(::onSuccessAction, ACTION_ACTIVATE,
                    listOf((adapter.items[pos] as GroupItemsItemViewModel).data.groupId.toString()), resources)
        else
            topAdsDashboardPresenter.setGroupAction(::onSuccessAction, ACTION_DEACTIVATE,
                    listOf((adapter.items[pos] as GroupItemsItemViewModel).data.groupId.toString()), resources)
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
        topAdsDashboardPresenter.setGroupAction(::onSuccessAction, TopAdsDashboardConstant.ACTION_DELETE,
                listOf((adapter.items[pos] as GroupItemsItemViewModel).data.groupId.toString()), resources)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fetchData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        btnFilter.setOnClickListener {
            groupFilterSheet.show()
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
        Utils.setSearchListener(view, ::onSuccessSearch, ::onSearchClear)
        group_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val scrollState = newState == RecyclerView.SCROLL_STATE_IDLE
                (parentFragment as TopAdsDashboardFragment).setScroll(scrollState)

            }

        })

    }

    private fun onSuccessSearch(search: String) {
        fetchData()
    }

    private fun onSearchClear() {
        fetchData()
    }

    private fun showConfirmationDialog() {
        val dialog = DialogUnify(context!!, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setTitle(String.format(getString(R.string.topads_dash_confirm_delete_group_title), adapter.getSelectedItems().size))
        dialog.setDescription(getString(R.string.topads_dash_confirm_delete_group_desc))
        dialog.setPrimaryCTAText(getString(R.string.topads_common_cancel_btn))
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


    private fun onEmptyResult() {
        adapter.items.add(GroupItemsEmptyViewModel())
        if (searchBar?.searchBarTextField?.text.toString().isEmpty()) {
            adapter.setEmptyView(!EMPTY_SEARCH_VIEW)
        } else {
            adapter.setEmptyView(EMPTY_SEARCH_VIEW)
        }
        setFilterCount()
        (parentFragment as TopAdsDashboardFragment).setGroupCount(0)
    }

    private fun onSuccessResult(data: List<DataItem>) {
        data.forEach {
            adapter.items.add(GroupItemsItemViewModel(it))
        }
        adapter.notifyDataSetChanged()
        (parentFragment as TopAdsDashboardFragment).setGroupCount(adapter.itemCount)
        setFilterCount()
    }

    private fun setFilterCount() {
        if (!groupFilterSheet.getFilterCount().isZero()) {
            filterCount.visibility = View.VISIBLE
            filterCount.text = groupFilterSheet.getFilterCount().toString()
        } else
            filterCount.visibility = View.GONE
    }

    private fun fetchData() {
        adapter.items.clear()
        adapter.notifyDataSetChanged()
        topAdsDashboardPresenter.getGroupData(resources, searchBar?.searchBarTextField?.text.toString(), groupFilterSheet.getSelectedSortId(), groupFilterSheet.getSelectedStatusId(), this::onSuccessResult, this::onEmptyResult)
    }

    private fun performAction(actionActivate: String) {
        if (actionActivate == TopAdsDashboardConstant.ACTION_DELETE) {
            view.let {
                view.let {
                    Toaster.make(it!!, getString(R.string.topads_dash_with_grup_delete_toast), TOASTER_DURATION.toInt(), Toaster.TYPE_NORMAL, getString(R.string.topads_common_batal), View.OnClickListener {
                        deleteCancel = true
                    })
                }
            }
            val coroutineScope = CoroutineScope(Dispatchers.Main)
            coroutineScope.launch {
                delay(TOASTER_DURATION)
                if (!deleteCancel)
                    topAdsDashboardPresenter.setGroupAction(::onSuccessAction, actionActivate, getAdIds(), resources)
                deleteCancel = false
                startSelectMode(false)
            }
        } else {
            topAdsDashboardPresenter.setGroupAction(::onSuccessAction, actionActivate, getAdIds(), resources)
        }
    }

    private fun getAdIds(): MutableList<String> {
        val ads: MutableList<String> = mutableListOf()
        adapter.getSelectedItems().forEach {
            ads.add(it.data.groupId.toString())
        }
        return ads
    }

    private fun onSuccessAction(action: String) {
        startSelectMode(false)
        fetchData()
    }
}