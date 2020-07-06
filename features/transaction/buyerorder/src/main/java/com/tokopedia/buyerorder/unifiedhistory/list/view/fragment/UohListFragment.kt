package com.tokopedia.buyerorder.unifiedhistory.list.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.view.RefreshHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.unifiedhistory.common.di.UohComponentInstance
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohListOrder
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohListParam
import com.tokopedia.buyerorder.unifiedhistory.list.di.DaggerUohListComponent
import com.tokopedia.buyerorder.unifiedhistory.list.view.adapter.UohBottomSheetOptionAdapter
import com.tokopedia.buyerorder.unifiedhistory.list.view.adapter.UohListItemAdapter
import com.tokopedia.buyerorder.unifiedhistory.list.view.viewmodel.UohListViewModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.ticker.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.bottomsheet_option_uoh.view.*
import kotlinx.android.synthetic.main.bottomsheet_option_uoh.view.btn_apply
import kotlinx.android.synthetic.main.fragment_uoh_list.*
import javax.inject.Inject


/**
 * Created by fwidjaja on 29/06/20.
 */
class UohListFragment: BaseDaggerFragment(), RefreshHandler.OnRefreshHandlerListener,
        UohBottomSheetOptionAdapter.ActionListener {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var uohListItemAdapter: UohListItemAdapter
    private var refreshHandler: RefreshHandler? = null
    private var paramUohOrder = UohListParam()
    private var orderList: UohListOrder.Data.UohOrders = UohListOrder.Data.UohOrders()
    private lateinit var uohBottomSheetOptionAdapter: UohBottomSheetOptionAdapter
    private var bottomSheetOption: BottomSheetUnify? = null
    private var currOption: String = ""
    private var currType: Int = -1

    private val uohListViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[UohListViewModel::class.java]
    }

    companion object {
        @JvmStatic
        fun newInstance(): UohListFragment {
            return UohListFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadOrderHistoryList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_uoh_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareLayout()
        observingOrderHistory()
    }

    override fun onRefresh(view: View?) {
        loadOrderHistoryList()
    }

    private fun prepareLayout() {
        refreshHandler = RefreshHandler(swipe_refresh_layout, this)
        refreshHandler?.setPullEnabled(true)
        uohListItemAdapter = UohListItemAdapter()

        rv_order_list?.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = uohListItemAdapter
        }
    }

    private fun loadOrderHistoryList() {
        uohListViewModel.loadOrderList(GraphqlHelper.loadRawString(resources, R.raw.uoh_get_order_history), paramUohOrder)
    }

    private fun observingOrderHistory() {
        uohListViewModel.orderHistoryListResult.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is Success -> {
                    orderList = it.data
                    // nextOrderId = orderList.cursorOrderId
                    if (orderList.filters.isNotEmpty() && orderList.categories.isNotEmpty()) {
                        renderChipsFilter()
                    }

                    if (orderList.orders.isNotEmpty()) {
                        renderOrderList()
                    } else {
                        renderEmptyList()
                    }

                    if (orderList.tickers.isNotEmpty()) {
                        renderTicker()
                    } else {
                        ticker_info?.gone()
                    }

                    /*else {
                        if (isFilterApplied) {
                            if (!paramOrder.startDate.equals(defaultStartDate, true) || !paramOrder.endDate.equals(defaultEndDate, true)) {
                                val inputFormat = SimpleDateFormat("dd/MM/yyyy")
                                val outputFormat = SimpleDateFormat("dd MMM yyyy")
                                val startDate = inputFormat.parse(paramOrder.startDate)
                                val startDateStr = outputFormat.format(startDate)
                                val endDate = inputFormat.parse(paramOrder.endDate)
                                val endDateStr = outputFormat.format(endDate)
                                renderFilterEmpty(getString(R.string.empty_search_title) + " " + startDateStr + " - " + endDateStr, getString(R.string.empty_search_desc))
                            } else {
                                renderFilterEmpty(getString(R.string.empty_filter_title), getString(R.string.empty_filter_desc))
                            }
                        } else {
                            renderEmptyOrderList()
                            showCoachMarkProductsEmpty()
                        }
                    }*/
                }
                is Fail -> {
                    // renderErrorOrderList(getString(R.string.error_list_title), getString(R.string.error_list_desc))
                }
            }
        })
    }

    private fun renderChipsFilter() {
        val chips = arrayListOf<SortFilterItem>()

        val filter1 = SortFilterItem(UohConsts.ALL_DATE, ChipsUnify.TYPE_NORMAL, ChipsUnify.SIZE_MEDIUM)
        filter1.listener = {
            filter1.type = if(filter1.type == ChipsUnify.TYPE_NORMAL) {
                ChipsUnify.TYPE_SELECTED
            } else {
                ChipsUnify.TYPE_NORMAL
            }
            println("++ SHOW BOTTOMSHEET 1!")
        }
        chips.add(filter1)

        val filter2 = SortFilterItem(UohConsts.ALL_FILTERS, ChipsUnify.TYPE_NORMAL, ChipsUnify.SIZE_MEDIUM)
        filter2.listener = {
            filter2.type = if(filter2.type == ChipsUnify.TYPE_NORMAL) {
                ChipsUnify.TYPE_SELECTED
            } else {
                ChipsUnify.TYPE_NORMAL
            }

            uohBottomSheetOptionAdapter = UohBottomSheetOptionAdapter(this)
            showBottomSheetOptions(UohConsts.CHOOSE_FILTERS)
            val mapKey = HashMap<String, String>()
            orderList.filters.forEach { option ->
                mapKey[option] = option
            }
            uohBottomSheetOptionAdapter.uohItemMapKeyList = mapKey
            uohBottomSheetOptionAdapter.filterType = UohConsts.TYPE_FILTER_STATUS
            uohBottomSheetOptionAdapter.notifyDataSetChanged()
        }
        chips.add(filter2)

        val filter3 = SortFilterItem(UohConsts.ALL_CATEGORIES, ChipsUnify.TYPE_NORMAL, ChipsUnify.SIZE_MEDIUM)
        filter3.listener = {
            filter3.type = if(filter3.type == ChipsUnify.TYPE_NORMAL) {
                ChipsUnify.TYPE_SELECTED
            } else {
                ChipsUnify.TYPE_NORMAL
            }
            uohBottomSheetOptionAdapter = UohBottomSheetOptionAdapter(this)
            showBottomSheetOptions(UohConsts.CHOOSE_CATEGORIES)
            val mapKey = HashMap<String, String>()
            orderList.categories.forEach { category ->
                mapKey[category.value] = category.label
            }
            uohBottomSheetOptionAdapter.uohItemMapKeyList = mapKey
            uohBottomSheetOptionAdapter.filterType = UohConsts.TYPE_FILTER_CATEGORY
            uohBottomSheetOptionAdapter.notifyDataSetChanged()
        }
        chips.add(filter3)

        uoh_sort_filter?.addItem(chips)
        uoh_sort_filter?.sortFilterPrefix?.setOnClickListener {
            uoh_sort_filter?.resetAllFilters()
        }
        filter1.refChipUnify.setChevronClickListener {  }
        filter2.refChipUnify.setChevronClickListener {  }
        filter3.refChipUnify.setChevronClickListener {  }
    }

    private fun renderTicker() {
        val listTickerData = arrayListOf<TickerData>()
        listTickerData.add(TickerData("Ini Testing Feature Flag 1", "This content can consists of Html Tags", Ticker.TYPE_ANNOUNCEMENT, true))
        listTickerData.add(TickerData("Ini Testing Feature Flag 2", "This content can consists of Html Tags", Ticker.TYPE_ANNOUNCEMENT, true))
        context?.let {
            val adapter = TickerPagerAdapter(it, listTickerData)
            adapter.setPagerDescriptionClickEvent(object : TickerPagerCallback {
                override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
                    RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, linkUrl))
                }
            })
            ticker_info?.setDescriptionClickEvent(object: TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {}

                override fun onDismiss() {
                }

            })
            ticker_info?.addPagerView(adapter, listTickerData)
        }
    }

    private fun renderOrderList() {
        refreshHandler?.finishRefresh()
        empty_state_order_list?.visibility = View.GONE
        rv_order_list?.visibility = View.VISIBLE
        uohListItemAdapter.addList(orderList.orders)
    }

    private fun renderEmptyList() {
        refreshHandler?.finishRefresh()
        rv_order_list?.visibility = View.GONE
        empty_state_order_list?.visibility = View.VISIBLE

    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        activity?.let {
            DaggerUohListComponent.builder()
                    .uohComponent(UohComponentInstance.getUohComponent(it.application))
                    .build()
                    .inject(this)
        }
    }

    private fun showBottomSheetOptions(title: String) {
        val viewBottomSheet = View.inflate(context, R.layout.bottomsheet_option_uoh, null).apply {
            rv_option?.apply {
                layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
                adapter = uohBottomSheetOptionAdapter
            }

            btn_apply?.setOnClickListener {
                bottomSheetOption?.dismiss()
                refreshHandler?.startRefresh()
            }
        }

        bottomSheetOption = BottomSheetUnify().apply {
            setChild(viewBottomSheet)
            setTitle(title)
            setCloseClickListener { dismiss() }
        }

        fragmentManager?.let { bottomSheetOption?.show(it, getString(R.string.show_bottomsheet)) }
    }

    override fun onOptionItemClick(option: String, filterType: Int) {
        println("++ option = $option, filter type = $filterType")
        currOption = option
        currType = filterType

        when (filterType) {
            UohConsts.TYPE_FILTER_DATE -> {

            }
            UohConsts.TYPE_FILTER_STATUS -> {
                paramUohOrder.input.status = option
            }
            UohConsts.TYPE_FILTER_CATEGORY -> {
                paramUohOrder.input.verticalCategory = option
            }
        }
    }
}