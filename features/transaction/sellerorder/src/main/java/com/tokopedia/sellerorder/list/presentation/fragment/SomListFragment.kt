package com.tokopedia.sellerorder.list.presentation.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.quickfilter.QuickFilterItem
import com.tokopedia.design.quickfilter.custom.CustomViewQuickFilterItem
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_CLIENT
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_SELLER
import com.tokopedia.sellerorder.list.data.model.SomListFilter
import com.tokopedia.sellerorder.list.data.model.SomListOrder
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
import kotlinx.android.synthetic.main.fragment_som_list.*
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-08-23.
 */
class SomListFragment: BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var somListItemAdapter: SomListItemAdapter
    private var filterList: List<SomListFilter.Data.OrderFilterSom.StatusList> = listOf()
    private var orderList: List<SomListOrder.Data.OrderList.Order> = listOf()
    private val listAllOrderStatusId = arrayListOf<List<Int>>()

    private val somListViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[SomListViewModel::class.java]
    }

    companion object {
        @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        @JvmStatic
        fun newInstance(): SomListFragment {
            return SomListFragment()
        }
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(SomListComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadInitial()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_som_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observingTicker()
        observingFilter()
        observingOrders()
    }

    private fun loadInitial() {
        somListViewModel.loadSomListData(
                GraphqlHelper.loadRawString(resources, R.raw.gql_som_ticker),
                GraphqlHelper.loadRawString(resources, R.raw.gql_som_filter))
    }

    /*private fun loadFilter() {
        somListViewModel.getFilterList(GraphqlHelper.loadRawString(resources, R.raw.gql_som_filter), true)
    }

    private fun loadOrders(statusList: List<Int>) {
        somListViewModel.getOrdersAsync(GraphqlHelper.loadRawString(resources, R.raw.gql_som_order), statusList, true).start()
    }*/

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
                    loadOrderList(filterList[0].orderStatusIdList)
                    renderFilter()
                }
                is Fail -> {
                    quick_filter?.visibility = View.GONE
                }
            }
        })
    }

    private fun loadOrderList(statusList: List<Int>) {
        somListViewModel.loadOrderList(GraphqlHelper.loadRawString(resources, R.raw.gql_som_order), statusList)
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
        filterList.forEach {
            val filterItem = CustomViewQuickFilterItem()
            filterItem.name = it.orderStatus
            if (it.orderStatusAmount > 0) filterItem.name += " (" + it.orderStatusAmount + ")"

            if (it.isChecked) {
                filterItem.setColorBorder(R.color.tkpd_main_green)
                filterItem.isSelected = true
            } else {
                filterItem.setColorBorder(R.color.gray_background)
                filterItem.isSelected = false
            }

            listQuickFilter.add(filterItem)
            listAllOrderStatusId.add(it.orderStatusIdList)
        }

        quick_filter.renderFilter(listQuickFilter)
    }

    /*private fun initOrderList() {
        somListItemAdapter = SomListItemAdapter()
        order_list_rv?.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = somListItemAdapter
        }

        val testSomListItem = arrayListOf<String>()
        testSomListItem.add("TEST INVOICE1")
        testSomListItem.add("TEST INVOICE2")
        testSomListItem.add("TEST INVOICE3")

        somListItemAdapter.somItemList = testSomListItem.toMutableList()
        somListItemAdapter.notifyDataSetChanged()
    }*/

    private fun observingOrders() {
        somListViewModel.orderListResult.observe(this, Observer {
            when (it) {
                is Success -> {
                    orderList = it.data
                    renderOrderList()
                }
                is Fail -> {
                    order_list_rv?.visibility = View.GONE
                }
            }
        })
    }

    private fun renderOrderList() {
        somListItemAdapter = SomListItemAdapter()
        order_list_rv?.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = somListItemAdapter
        }
        somListItemAdapter.somItemList = orderList.toMutableList()
    }
}