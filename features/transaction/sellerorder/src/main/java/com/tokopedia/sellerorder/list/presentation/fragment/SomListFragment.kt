package com.tokopedia.sellerorder.list.presentation.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.view.RefreshHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalOrder
import com.tokopedia.design.quickfilter.QuickFilterItem
import com.tokopedia.design.quickfilter.custom.CustomViewQuickFilterItem
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.kotlin.extensions.getCalculatedFormattedDate
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.analytics.SomAnalytics
import com.tokopedia.sellerorder.analytics.SomAnalytics.eventClickButtonPeluangInEmptyState
import com.tokopedia.sellerorder.analytics.SomAnalytics.eventClickOrder
import com.tokopedia.sellerorder.analytics.SomAnalytics.eventSubmitSearch
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.common.util.SomConsts.LIST_ORDER_SCREEN_NAME
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_ORDER_ID
import com.tokopedia.sellerorder.common.util.SomConsts.RESULT_ACCEPT_ORDER
import com.tokopedia.sellerorder.common.util.SomConsts.RESULT_CONFIRM_SHIPPING
import com.tokopedia.sellerorder.common.util.SomConsts.RESULT_PROCESS_REQ_PICKUP
import com.tokopedia.sellerorder.common.util.SomConsts.RESULT_REJECT_ORDER
import com.tokopedia.sellerorder.common.util.SomConsts.STATUS_ALL_ORDER
import com.tokopedia.sellerorder.common.util.SomConsts.STATUS_DELIVERED
import com.tokopedia.sellerorder.common.util.SomConsts.STATUS_ORDER_600
import com.tokopedia.sellerorder.common.util.SomConsts.STATUS_ORDER_699
import com.tokopedia.sellerorder.common.util.SomConsts.TAB_ACTIVE
import com.tokopedia.sellerorder.common.util.SomConsts.TAB_STATUS
import com.tokopedia.sellerorder.detail.data.model.SomAcceptOrder
import com.tokopedia.sellerorder.detail.data.model.SomRejectOrder
import com.tokopedia.sellerorder.detail.presentation.activity.SomDetailActivity
import com.tokopedia.sellerorder.list.data.model.SomListFilter
import com.tokopedia.sellerorder.list.data.model.SomListOrder
import com.tokopedia.sellerorder.list.data.model.SomListOrderParam
import com.tokopedia.sellerorder.list.data.model.SomListTicker
import com.tokopedia.sellerorder.list.di.SomListComponent
import com.tokopedia.sellerorder.list.presentation.activity.SomFilterActivity
import com.tokopedia.sellerorder.list.presentation.adapter.SomListItemAdapter
import com.tokopedia.sellerorder.list.presentation.viewmodel.SomListViewModel
import com.tokopedia.sellerorder.requestpickup.data.model.SomProcessReqPickup
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.empty_list.*
import kotlinx.android.synthetic.main.fragment_som_list.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.function.Predicate
import javax.inject.Inject
import kotlin.collections.HashMap

/**
 * Created by fwidjaja on 2019-08-23.
 */
class SomListFragment: BaseDaggerFragment(), RefreshHandler.OnRefreshHandlerListener,
        SearchInputView.Listener, SearchInputView.ResetListener, SomListItemAdapter.ActionListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var somListItemAdapter: SomListItemAdapter
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private var filterList: List<SomListFilter.Data.OrderFilterSom.StatusList> = listOf()
    private var orderList: SomListOrder.Data.OrderList = SomListOrder.Data.OrderList()
    private var mapOrderStatus = HashMap<String, List<Int>>()
    private var paramOrder =  SomListOrderParam()
    private var refreshHandler: RefreshHandler? = null
    private var isLoading = false
    private var tabActive = ""
    private var tabStatus = ""
    private val FLAG_DETAIL = 3333
    private val FLAG_CONFIRM_REQ_PICKUP = 3553
    private var isFilterApplied = false
    private var defaultStartDate = ""
    private var defaultEndDate = ""
    private var nextOrderId = 0
    private var onLoadMore = false

    private val somListViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[SomListViewModel::class.java]
    }

    companion object {
        private const val REQUEST_FILTER = 2888

        @JvmStatic
        fun newInstance(bundle: Bundle): SomListFragment {
            return SomListFragment().apply {
                arguments = Bundle().apply {
                    putString(TAB_ACTIVE, bundle.getString(TAB_ACTIVE))
                    putString(TAB_STATUS, bundle.getString(TAB_STATUS))
                }
            }
        }
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(SomListComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            tabActive = arguments?.getString(TAB_ACTIVE).toString()
            tabStatus = arguments?.getString(TAB_STATUS).toString()
        }
        loadInitial()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_som_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareLayout()
        setListeners()
        setInitialValue()
        observingTicker()
        observingFilter()
        observingOrders()
    }

    private fun prepareLayout() {
        refreshHandler = RefreshHandler(activity, view, this)
        refreshHandler?.setPullEnabled(true)
        somListItemAdapter = SomListItemAdapter()
        somListItemAdapter.setActionListener(this)
        addEndlessScrollListener()
    }

    private fun addEndlessScrollListener() {
        order_list_rv?.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = somListItemAdapter
            scrollListener = object: EndlessRecyclerViewScrollListener(layoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int) {
                    onLoadMore = true
                    println("++ onLoadMore - nextOrderId = $nextOrderId")
                    if (nextOrderId != 0) {
                        loadOrderList(nextOrderId)
                    }
                }
            }
            addOnScrollListener(scrollListener)
        }
    }

    private fun setListeners() {
        search_input_view?.setListener(this)
        search_input_view?.setResetListener(this)
        search_input_view?.searchTextView?.setOnClickListener { search_input_view?.searchTextView?.isCursorVisible = true }

        filter_action_button.setOnClickListener {
            SomAnalytics.eventClickFilterButtonOnOrderList()
            val intentFilter = context?.let { ctx -> SomFilterActivity.createIntent(ctx, paramOrder) }
            startActivityForResult(intentFilter, REQUEST_FILTER)
        }
    }

    private fun setInitialValue() {
        defaultStartDate = getCalculatedFormattedDate("dd/MM/yyyy", -90)
        defaultEndDate = Date().toFormattedString("dd/MM/yyyy")
        paramOrder.startDate = defaultStartDate
        paramOrder.endDate = defaultEndDate
    }

    private fun loadInitial() {
        activity?.let { SomAnalytics.sendScreenName(it, LIST_ORDER_SCREEN_NAME) }
        somListViewModel.loadSomListData(
                GraphqlHelper.loadRawString(resources, R.raw.gql_som_ticker),
                GraphqlHelper.loadRawString(resources, R.raw.gql_som_filter))
    }

    private fun observingTicker() = somListViewModel.tickerListResult.observe(this, Observer {
            when (it) {
                is Success -> {
                    renderInfoTicker(it.data)
                }
                is Fail -> {
                    ticker_info?.visibility = View.GONE
                }
            }
        })

    private fun observingFilter() {
        somListViewModel.filterListResult.observe(this, Observer {
            when (it) {
                is Success -> {
                    filterList = it.data
                    renderFilter()
                }
                is Fail -> {
                    quick_filter?.visibility = View.GONE
                }
            }
        })
    }

    private fun loadOrderList(nextOrderId: Int) {
        paramOrder.nextOrderId = nextOrderId
        somListViewModel.loadOrderList(GraphqlHelper.loadRawString(resources, R.raw.gql_som_order), paramOrder)
    }

    private fun renderInfoTicker(tickerList: List<SomListTicker.Data.OrderTickers.Tickers>) {
        if (tickerList.isNotEmpty()) {
            ticker_info?.visibility = View.VISIBLE
            if (tickerList.size > 1) {
                val listTickerData = arrayListOf<TickerData>()
                var indexTicker = 0
                tickerList.forEach {
                    if (it.isActive) {
                        listTickerData.add(TickerData("", it.shortDesc + " ${getString(R.string.ticker_info_selengkapnya)}", Ticker.TYPE_ANNOUNCEMENT, true))
                        indexTicker++
                    }
                }

                context?.let {
                    val adapter = TickerPagerAdapter(it, listTickerData)
                    adapter.setPagerDescriptionClickEvent(object: TickerPagerCallback {
                        override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
                            RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, linkUrl))
                            SomAnalytics.eventClickSeeMoreOnTicker()
                        }

                    })
                    ticker_info?.addPagerView(adapter, listTickerData)
                }
            } else {
                tickerList.first().let {
                    ticker_info?.setHtmlDescription(it.shortDesc + " ${getString(R.string.ticker_info_selengkapnya)}")
                    ticker_info?.tickerType = Ticker.TYPE_ANNOUNCEMENT
                    ticker_info?.setDescriptionClickEvent(object : TickerCallback {
                        override fun onDescriptionViewClick(linkUrl: CharSequence) {
                            RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, linkUrl))
                            SomAnalytics.eventClickSeeMoreOnTicker()
                        }

                        override fun onDismiss() {}

                    })
                }
            }
            SomAnalytics.eventViewTicker()
        } else {
            ticker_info?.visibility = View.GONE
        }
    }

    private fun renderFilter() {
        val listQuickFilter = arrayListOf<QuickFilterItem>()
        var index = 0
        var currentIndex = 0
        filterList.forEach {
            val filterItem = CustomViewQuickFilterItem()
            filterItem.name = it.orderStatus
            if (it.orderStatusAmount > 0) filterItem.name += " (" + it.orderStatusAmount + ")"

            filterItem.type = it.key

            if (it.isChecked || tabActive.equals(it.key, true) || paramOrder.statusList == it.orderStatusIdList) {
                currentIndex = index
                filterItem.setColorBorder(com.tokopedia.design.R.color.tkpd_main_green)
                filterItem.isSelected = true
                if (paramOrder.statusList.isEmpty()) {
                    if (tabStatus.equals(STATUS_DELIVERED, true)) {
                        val listPesananTiba = ArrayList<Int>()
                        if (it.orderStatusIdList.contains(STATUS_ORDER_600)) listPesananTiba.add(STATUS_ORDER_600)
                        if (it.orderStatusIdList.contains(STATUS_ORDER_699)) listPesananTiba.add(STATUS_ORDER_699)
                        paramOrder.statusList = listPesananTiba
                    } else {
                        paramOrder.statusList = it.orderStatusIdList
                    }
                }
            }  /*else {
                filterItem.setColorBorder(com.tokopedia.design.R.color.gray_background)
                filterItem.isSelected = false
            }*/
            refreshHandler?.startRefresh()

            listQuickFilter.add(filterItem)
            mapOrderStatus[it.key] = it.orderStatusIdList
            index++
        }

        quick_filter?.renderFilter(listQuickFilter, currentIndex)
        quick_filter?.setListener { keySelected ->
            mapOrderStatus.forEach { (key, listOrderStatusId) ->
                if (keySelected.equals(key, true)) {
                    tabActive = keySelected
                    SomAnalytics.eventClickQuickFilter(tabActive)
                    println("++ selected tabActive = $tabActive")
                    if (listOrderStatusId.isNotEmpty()) {
                        this.paramOrder.statusList = listOrderStatusId
                        refreshHandler?.startRefresh()
                    }
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun observingOrders() {
        somListViewModel.orderListResult.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is Success -> {
                    orderList = it.data
                    nextOrderId = orderList.cursorOrderId
                    println("++ nextOrderId = $nextOrderId")
                    if (orderList.orders.isNotEmpty()) renderOrderList()
                    else {
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
                            renderCekPeluang()
                        }
                    }
                }
                is Fail -> {
                    renderErrorOrderList(getString(R.string.error_list_title), getString(R.string.error_list_desc))
                }
            }
        })
    }

    private fun renderOrderList() {
        refreshHandler?.finishRefresh()
        empty_state_order_list.visibility = View.GONE
        order_list_rv.visibility = View.VISIBLE

        if (!onLoadMore) {
            somListItemAdapter.somItemList = orderList.orders.toMutableList()
        } else {
            somListItemAdapter.addItems(orderList.orders)
            scrollListener.updateStateAfterGetData()
        }
        somListItemAdapter.notifyDataSetChanged()
    }

    private fun renderFilterEmpty(title: String, desc: String) {
        refreshHandler?.finishRefresh()
        order_list_rv.visibility = View.GONE
        empty_state_order_list.visibility = View.VISIBLE
        title_empty?.text = title
        desc_empty?.text = desc
        btn_cek_peluang?.visibility = View.GONE
        SomAnalytics.eventViewEmptyState(tabActive)
    }

    private fun renderErrorOrderList(title: String, desc: String) {
        refreshHandler?.finishRefresh()
        order_list_rv.visibility = View.GONE
        empty_state_order_list.visibility = View.VISIBLE
        title_empty?.text = title
        desc_empty?.text = desc
        ic_empty?.loadImageDrawable(R.drawable.ic_som_error_list)
        btn_cek_peluang?.apply {
            visibility = View.VISIBLE
            text = getString(R.string.retry_load_list)
            setOnClickListener {
                refreshHandler?.startRefresh()
            }
        }
    }

    private fun renderCekPeluang() {
        refreshHandler?.finishRefresh()
        order_list_rv.visibility = View.GONE
        empty_state_order_list.visibility = View.VISIBLE
        title_empty?.text = getString(R.string.empty_peluang_title)
        desc_empty?.text = getString(R.string.empty_peluang_desc)
        btn_cek_peluang?.visibility = View.VISIBLE
        btn_cek_peluang?.setOnClickListener {
            eventClickButtonPeluangInEmptyState(tabActive)
            startActivity(RouteManager.getIntent(context, ApplinkConstInternalOrder.OPPORTUNITY))
        }
    }

    override fun onSearchReset() { }

    override fun onSearchSubmitted(text: String?) {
        text?.let {
            eventSubmitSearch(text)
            paramOrder.search = text
            refreshHandler?.startRefresh()
        }
    }

    override fun onSearchTextChanged(text: String?) {
        text?.let {
            paramOrder.search = text
            refreshHandler?.startRefresh()
        }
    }

    override fun onRefresh(view: View?) {
        addEndlessScrollListener()
        onLoadMore = false
        isLoading = true
        somListItemAdapter.removeAll()
        nextOrderId = 0
        loadOrderList(nextOrderId)
        if (isFilterApplied) filter_action_button?.rightIconDrawable = resources.getDrawable(R.drawable.ic_som_check)
        else filter_action_button?.rightIconDrawable = null
    }

    private fun checkFilterApplied(paramOrder: SomListOrderParam): Boolean {
        var isApplied = false
        if (paramOrder.search.isNotEmpty()) isApplied = true
        if (!paramOrder.startDate.equals(defaultStartDate, true)) isApplied = true
        if (!paramOrder.endDate.equals(defaultEndDate, true)) isApplied = true
        if (paramOrder.statusList.isNotEmpty()) isApplied = true
        if (paramOrder.shippingList.isNotEmpty()) isApplied = true
        if (paramOrder.orderTypeList.isNotEmpty()) isApplied = true
        return isApplied
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_FILTER && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                if (data.hasExtra(SomConsts.PARAM_LIST_ORDER)) {
                    paramOrder = data.getParcelableExtra(SomConsts.PARAM_LIST_ORDER)
                    isFilterApplied = checkFilterApplied(paramOrder)
                    tabActive = ""
                    renderFilter()
                }
            }
        } else if (requestCode == FLAG_DETAIL && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                when {
                    data.hasExtra(RESULT_ACCEPT_ORDER) -> {
                        val resultAcceptOrder = data.getParcelableExtra<SomAcceptOrder.Data.AcceptOrder>(RESULT_ACCEPT_ORDER)
                        refreshThenShowToasterOk(resultAcceptOrder.listMessage.first())

                    }
                    data.hasExtra(RESULT_REJECT_ORDER) -> {
                        val resultRejectOrder = data.getParcelableExtra<SomRejectOrder.Data.RejectOrder>(RESULT_REJECT_ORDER)
                        refreshThenShowToasterOk(resultRejectOrder.message.first())

                    }
                    data.hasExtra(RESULT_CONFIRM_SHIPPING) -> {
                        val resultConfirmShippingMsg = data.getStringExtra(RESULT_CONFIRM_SHIPPING)
                        refreshThenShowToasterOk(resultConfirmShippingMsg)
                    }
                }
            }
        } else if (requestCode == FLAG_CONFIRM_REQ_PICKUP && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                if (data.hasExtra(RESULT_PROCESS_REQ_PICKUP)) {
                    val resultProcessReqPickup = data.getParcelableExtra<SomProcessReqPickup.Data.MpLogisticRequestPickup>(RESULT_PROCESS_REQ_PICKUP)
                    refreshThenShowToasterOk(resultProcessReqPickup.listMessage.first())
                }
            }
        }
    }

    private fun refreshThenShowToasterOk(message: String) {
        refreshHandler?.startRefresh()
        val toasterSuccess = Toaster
        view?.let { v ->
            toasterSuccess.make(v, message, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL, SomConsts.ACTION_OK)
        }
    }

    override fun onListItemClicked(orderId: String) {
        eventClickOrder(tabActive)
        Intent(activity, SomDetailActivity::class.java).apply {
            putExtra(PARAM_ORDER_ID, orderId)
            startActivityForResult(this, FLAG_DETAIL)
        }
    }
}