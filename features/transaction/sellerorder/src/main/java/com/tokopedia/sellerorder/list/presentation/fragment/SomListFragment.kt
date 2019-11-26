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
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_ORDER_ID
import com.tokopedia.sellerorder.common.util.SomConsts.RESULT_ACCEPT_ORDER
import com.tokopedia.sellerorder.common.util.SomConsts.RESULT_CONFIRM_SHIPPING
import com.tokopedia.sellerorder.common.util.SomConsts.RESULT_PROCESS_REQ_PICKUP
import com.tokopedia.sellerorder.common.util.SomConsts.RESULT_REJECT_ORDER
import com.tokopedia.sellerorder.common.util.SomConsts.STATUS_ALL_ORDER
import com.tokopedia.sellerorder.common.util.SomConsts.TAB_ACTIVE
import com.tokopedia.sellerorder.confirmshipping.data.model.SomConfirmShipping
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
import com.tokopedia.sellerorder.list.presentation.bottomsheet.TickerDetailBottomSheetFragment
import com.tokopedia.sellerorder.list.presentation.viewmodel.SomListViewModel
import com.tokopedia.sellerorder.requestpickup.data.model.SomConfirmReqPickup
import com.tokopedia.sellerorder.requestpickup.data.model.SomProcessReqPickup
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.empty_list.*
import kotlinx.android.synthetic.main.fragment_som_list.*
import java.text.SimpleDateFormat
import java.util.*
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
    private var filterList: List<SomListFilter.Data.OrderFilterSom.StatusList> = listOf()
    private var orderList: List<SomListOrder.Data.OrderList.Order> = listOf()
    private var mapOrderStatus = HashMap<String, List<Int>>()
    private var paramOrder =  SomListOrderParam()
    private var refreshHandler: RefreshHandler? = null
    private var isLoading = false
    private var tabActive = ""
    private val FLAG_DETAIL = 3333
    private val FLAG_CONFIRM_REQ_PICKUP = 3553
    private val FLAG_CONFIRM_SHIPPING = 3555

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
        order_list_rv?.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = somListItemAdapter
        }
    }

    private fun setListeners() {
        search_input_view?.setListener(this)
        search_input_view?.setResetListener(this)
        search_input_view?.searchTextView?.setOnClickListener { search_input_view?.searchTextView?.isCursorVisible = true }

        filter_action_button.setOnClickListener {
            // startActivity(Intent(context, SomFilterActivity::class.java))
            val intentFilter = context?.let { ctx -> SomFilterActivity.createIntent(ctx, paramOrder) }
            startActivityForResult(intentFilter, REQUEST_FILTER)
        }
    }

    private fun setInitialValue() {
        paramOrder.startDate = getCalculatedFormattedDate("dd/MM/yyyy", -90)
        paramOrder.endDate = Date().toFormattedString("dd/MM/yyyy")

        println("++ paramOrder.startDate = ${paramOrder.startDate}")
        println("++ paramOrder.endDate = ${paramOrder.endDate}")
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
                var indexTicker = 0
                tickerList.forEach {
                    if (it.isActive) {
                        // listTickerData.add(TickerData("", it.body, Ticker.TYPE_ANNOUNCEMENT, true, indexTicker))
                        listTickerData.add(TickerData("", it.shortDesc, Ticker.TYPE_ANNOUNCEMENT, true))
                        indexTicker++
                    }
                }

                context?.let {
                    val adapter = TickerPagerAdapter(it, listTickerData)
                    /*adapter.setPagerDescriptionClickEvent(object: TickerPagerCallback {
                        override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
                            println("++ linkUrl = $linkUrl, itemData = ${itemData.toString()}")
                            val index = itemData as Int
                            // showTickerBottomSheet(listTickerData[index].description)
                            RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, linkUrl))
                        }
                    })*/
                    adapter.setPagerDescriptionClickEvent(object: TickerPagerCallback {
                        override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
                            // changed to open bottomsheet
                            RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, linkUrl))
                            // showTickerBottomSheet(listTickerData[])
                        }

                    })
                    ticker_info?.addPagerView(adapter, listTickerData)
                }
            } else {
                tickerList.first().let {
                    ticker_info?.visibility = View.VISIBLE
                    ticker_info?.setHtmlDescription(it.shortDesc)
                    ticker_info?.tickerType = Ticker.TYPE_ANNOUNCEMENT
                    ticker_info?.setDescriptionClickEvent(object : TickerCallback {
                        override fun onDescriptionViewClick(linkUrl: CharSequence) {
                            // changed to open bottomsheet
                            RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, linkUrl))
                            // showTickerBottomSheet(it.body)
                        }

                        override fun onDismiss() {}

                    })
                }
            }
        } else {
            ticker_info?.visibility = View.GONE
        }
    }

    private fun showTickerBottomSheet(desc: String) {
        val tickerDetailBottomSheetFragment =
                TickerDetailBottomSheetFragment.newInstance(desc)
        tickerDetailBottomSheetFragment.show(fragmentManager, "")
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
                filterItem.setColorBorder(R.color.tkpd_main_green)
                filterItem.isSelected = true
                paramOrder.statusList = it.orderStatusIdList

            }  else {
                filterItem.setColorBorder(R.color.gray_background)
                filterItem.isSelected = false
            }
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
                    println("++ selected tabActive = $tabActive")
                    if (listOrderStatusId.isNotEmpty()) {
                        paramOrder.statusList = listOrderStatusId
                        refreshHandler?.startRefresh()
                    }
                }
            }

            var intervalDays = -60
            if (keySelected == STATUS_ALL_ORDER) intervalDays = -90
            // paramOrder.startDate = getCalculatedFormattedDate("dd/MM/yyyy", intervalDays)

            println("++ paramOrder.startDate = ${paramOrder.startDate}")
            println("++ paramOrder.endDate = ${paramOrder.endDate}")
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun observingOrders() {
        somListViewModel.orderListResult.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is Success -> {
                    orderList = it.data
                    if (orderList.isNotEmpty()) renderOrderList()
                    else {
                        if (tabActive == getString(R.string.key_all_order)) renderCekPeluang()
                        else if (paramOrder.startDate.isNotEmpty() || paramOrder.endDate.isNotEmpty()) {
                            val inputFormat = SimpleDateFormat("dd/MM/yyyy")
                            val outputFormat = SimpleDateFormat("dd MMM yyyy")
                            val startDate = inputFormat.parse(paramOrder.startDate)
                            val startDateStr = outputFormat.format(startDate)
                            val endDate = inputFormat.parse(paramOrder.endDate)
                            val endDateStr = outputFormat.format(endDate)
                            renderFilterEmpty(getString(R.string.empty_search_title) + " " + startDateStr + " - " + endDateStr, getString(R.string.empty_search_desc))
                        } else renderFilterEmpty(getString(R.string.empty_filter_title), getString(R.string.empty_filter_desc))
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

    private fun renderFilterEmpty(title: String, desc: String) {
        refreshHandler?.finishRefresh()
        order_list_rv.visibility = View.GONE
        empty_state_order_list.visibility = View.VISIBLE
        title_empty?.text = title
        desc_empty?.text = desc
        btn_cek_peluang?.visibility = View.GONE
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
        isLoading = true
        loadOrderList()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_FILTER && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                if (data.hasExtra(SomConsts.PARAM_LIST_ORDER)) {
                    paramOrder = data.getParcelableExtra(SomConsts.PARAM_LIST_ORDER)
                    renderFilter()
                }
            }
        } else if (requestCode == FLAG_DETAIL && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                if (data.hasExtra(RESULT_ACCEPT_ORDER)) {
                    val resultAcceptOrder = data.getParcelableExtra<SomAcceptOrder.Data.AcceptOrder>(RESULT_ACCEPT_ORDER)
                    refreshThenShowToasterOk(resultAcceptOrder.listMessage.first())

                } else if (data.hasExtra(RESULT_REJECT_ORDER)) {
                    val resultRejectOrder = data.getParcelableExtra<SomRejectOrder.Data.RejectOrder>(RESULT_REJECT_ORDER)
                    refreshThenShowToasterOk(resultRejectOrder.message.first())

                }
            }
        } else if (requestCode == FLAG_CONFIRM_REQ_PICKUP && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                if (data.hasExtra(RESULT_PROCESS_REQ_PICKUP)) {
                    val resultProcessReqPickup = data.getParcelableExtra<SomProcessReqPickup.Data.MpLogisticRequestPickup>(RESULT_PROCESS_REQ_PICKUP)
                    refreshThenShowToasterOk(resultProcessReqPickup.listMessage.first())
                }
            }
        } else if (requestCode == FLAG_CONFIRM_SHIPPING  && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                if (data.hasExtra(RESULT_CONFIRM_SHIPPING)) {
                    val resultConfirmShippingMsg = data.getStringExtra(RESULT_CONFIRM_SHIPPING)
                    refreshThenShowToasterOk(resultConfirmShippingMsg)
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
        Intent(activity, SomDetailActivity::class.java).apply {
            putExtra(PARAM_ORDER_ID, orderId)
            startActivityForResult(this, FLAG_DETAIL)
        }
    }
}