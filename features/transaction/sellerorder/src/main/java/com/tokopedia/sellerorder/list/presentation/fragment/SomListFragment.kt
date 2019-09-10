package com.tokopedia.sellerorder.list.presentation.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.view.RefreshHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalOrder
import com.tokopedia.design.quickfilter.QuickFilterItem
import com.tokopedia.design.quickfilter.custom.CustomViewQuickFilterItem
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.util.SomConsts.TAB_ACTIVE
import com.tokopedia.sellerorder.list.data.model.SomListFilter
import com.tokopedia.sellerorder.list.data.model.SomListOrder
import com.tokopedia.sellerorder.list.data.model.SomListOrderParam
import com.tokopedia.sellerorder.list.data.model.SomListTicker
import com.tokopedia.sellerorder.list.di.SomListComponent
import com.tokopedia.sellerorder.list.presentation.adapter.SomListItemAdapter
import com.tokopedia.sellerorder.list.presentation.viewmodel.SomListViewModel
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.empty_list.*
import kotlinx.android.synthetic.main.fragment_som_list.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

/**
 * Created by fwidjaja on 2019-08-23.
 */
class SomListFragment: BaseDaggerFragment(), RefreshHandler.OnRefreshHandlerListener, SearchInputView.Listener, SearchInputView.ResetListener {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var somListItemAdapter: SomListItemAdapter
    private var filterList: List<SomListFilter.Data.OrderFilterSom.StatusList> = listOf()
    private var orderList: List<SomListOrder.Data.OrderList.Order> = listOf()
    private var mapOrderStatus = HashMap<String, List<Int>>()
    private var paramOrder =  SomListOrderParam()
    private var refreshHandler: RefreshHandler? = null
    private var isLoading = false
    private var tabActive = ""


    private val somListViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[SomListViewModel::class.java]
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): SomListFragment {
            return SomListFragment().apply {
                arguments = Bundle().apply {
                    putString(TAB_ACTIVE, bundle.getString(TAB_ACTIVE))
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
            println("++ tabActive = $tabActive")
        }
        loadInitial()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_som_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareLayout()
        setListener()
        setInitialValue()
        observingTicker()
        observingFilter()
        observingOrders()
    }

    private fun prepareLayout() {
        refreshHandler = RefreshHandler(activity, view, this)
        refreshHandler?.setPullEnabled(true)
        somListItemAdapter = SomListItemAdapter()
        order_list_rv?.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = somListItemAdapter
        }
    }

    private fun setListener() {
        search_input_view?.setListener(this)
        search_input_view?.setResetListener(this)
        search_input_view?.searchTextView?.setOnClickListener { search_input_view?.searchTextView?.isCursorVisible = true }
    }

    private fun setInitialValue() {
        paramOrder.endDate = Date().toFormattedString("dd/MM/yyyy")
    }

    private fun loadInitial() {
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
                    // loadOrderList()
                    refreshHandler?.startRefresh()
                    renderFilter()
                }
                is Fail -> {
                    quick_filter?.visibility = View.GONE
                }
            }
        })
    }

    private fun loadOrderList() {
        somListViewModel.loadOrderList(GraphqlHelper.loadRawString(resources, R.raw.gql_som_order), paramOrder)
    }

    private fun renderInfoTicker(tickerList: List<SomListTicker.Data.OrderTickers.Tickers>) {
        if (tickerList.isNotEmpty()) {
            if (tickerList.size > 1) {
                val listTickerData = arrayListOf<TickerData>()
                tickerList.forEach {
                    if (it.isActive) {
                        listTickerData.add(TickerData("", it.body, Ticker.TYPE_ANNOUNCEMENT, true))
                    }
                }

                context?.let {
                    val adapter = TickerPagerAdapter(it, listTickerData)
                    adapter.setDescriptionClickEvent(object: TickerCallback {
                        override fun onDescriptionViewClick(link: CharSequence?) {
                            RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, link))
                        }

                        override fun onDismiss() {}
                    })
                    ticker_info?.addPagerView(adapter, listTickerData)
                }
            } else {
                tickerList.first().let {
                    ticker_info?.setHtmlDescription(it.body)
                    ticker_info?.tickerType = Ticker.TYPE_ANNOUNCEMENT
                }
                ticker_info?.setDescriptionClickEvent(object : TickerCallback {
                    override fun onDescriptionViewClick(link: CharSequence?) {
                        RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, link))
                    }

                    override fun onDismiss() {}

                })
            }
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

            if (it.isChecked || tabActive.equals(it.key, true)) {
                currentIndex = index
                filterItem.setColorBorder(R.color.tkpd_main_green)
                filterItem.isSelected = true
                paramOrder.statusList = it.orderStatusIdList
                refreshHandler?.startRefresh()

            } else {
                filterItem.setColorBorder(R.color.gray_background)
                filterItem.isSelected = false
            }

            listQuickFilter.add(filterItem)
            mapOrderStatus[it.key] = it.orderStatusIdList
            index++
        }

        quick_filter.renderFilter(listQuickFilter, currentIndex)
        quick_filter.setListener { keySelected ->
            mapOrderStatus.forEach { (key, listOrderStatusId) ->
                if (keySelected.equals(key, true)) {
                    tabActive = keySelected
                    println("++ selected tabActive = $tabActive")
                    if (listOrderStatusId.isNotEmpty()) {
                        paramOrder.statusList = listOrderStatusId
                        refreshHandler?.startRefresh()
                    }
                }
            }
        }
    }

    private fun observingOrders() {
        somListViewModel.orderListResult.observe(this, Observer {
            when (it) {
                is Success -> {
                    orderList = it.data
                    if (orderList.isNotEmpty()) renderOrderList()
                    else {
                        if (tabActive == getString(R.string.key_all_order)) renderCekPeluang()
                        else renderFilterEmpty()
                    }
                }
                is Fail -> {
                    order_list_rv?.visibility = View.GONE
                }
            }
        })
    }

    private fun renderOrderList() {
        refreshHandler?.finishRefresh()
        empty_state_order_list.visibility = View.GONE
        order_list_rv.visibility = View.VISIBLE
        somListItemAdapter.somItemList = orderList.toMutableList()
        somListItemAdapter.notifyDataSetChanged()
    }

    private fun renderErrorPage() {

    }

    private fun renderFilterEmpty() {
        refreshHandler?.finishRefresh()
        order_list_rv.visibility = View.GONE
        empty_state_order_list.visibility = View.VISIBLE
        title_empty?.text = getString(R.string.empty_filter_title)
        desc_empty?.text = getString(R.string.empty_filter_desc)
        btn_cek_peluang?.visibility = View.GONE
    }

    private fun renderSearchEmpty() {

    }

    private fun renderCekPeluang() {
        refreshHandler?.finishRefresh()
        order_list_rv.visibility = View.GONE
        empty_state_order_list.visibility = View.VISIBLE
        title_empty?.text = getString(R.string.empty_peluang_title)
        desc_empty?.text = getString(R.string.empty_peluang_desc)
        btn_cek_peluang?.visibility = View.VISIBLE
        btn_cek_peluang?.setOnClickListener {
            startActivity(RouteManager.getIntent(context, ApplinkConstInternalOrder.OPPORTUNITY))
        }
    }

    override fun onSearchReset() { }

    override fun onSearchSubmitted(text: String?) {
        text?.let {
            paramOrder.search = text
            // loadOrderList()
            refreshHandler?.startRefresh()
        }
    }

    override fun onSearchTextChanged(text: String?) {
        text?.let {
            paramOrder.search = text
            // loadOrderList()
            refreshHandler?.startRefresh()
        }
    }

    override fun onRefresh(view: View?) {
        isLoading = true
        loadOrderList()
    }
}