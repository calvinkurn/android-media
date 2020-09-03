package com.tokopedia.buyerorder.unifiedhistory.list.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.view.RefreshHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.ApplinkConst.UnifyOrder.*
import com.tokopedia.applink.RouteManager
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.unifiedhistory.common.di.UohComponentInstance
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.APP_LINK_TYPE
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.EMAIL_MUST_NOT_BE_EMPTY
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.END_DATE
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.FINISH_ORDER_BOTTOMSHEET_TITLE
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.FLIGHT_STATUS_OK
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.GQL_ATC
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.GQL_FINISH_ORDER
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.GQL_FLIGHT_EMAIL
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.GQL_LS_FINISH
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.GQL_LS_LACAK
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.GQL_MP_REJECT
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.GQL_RECHARGE_BATALKAN
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.GQL_TRACK
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.GQL_TRAIN_EMAIL
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.LS_LACAK_MWEB
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.REPLACE_ORDER_ID
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.START_DATE
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.TYPE_ACTION_BUTTON_LINK
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.WEB_LINK_TYPE
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.WRONG_FORMAT_EMAIL
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohUtils
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.*
import com.tokopedia.buyerorder.unifiedhistory.list.di.DaggerUohListComponent
import com.tokopedia.buyerorder.unifiedhistory.list.view.adapter.UohBottomSheetKebabMenuAdapter
import com.tokopedia.buyerorder.unifiedhistory.list.view.adapter.UohBottomSheetOptionAdapter
import com.tokopedia.buyerorder.unifiedhistory.list.view.adapter.UohItemAdapter
import com.tokopedia.buyerorder.unifiedhistory.list.view.viewmodel.UohListViewModel
import com.tokopedia.datepicker.DatePickerUnify
import com.tokopedia.design.utils.StringUtils
import com.tokopedia.kotlin.extensions.convertMonth
import com.tokopedia.kotlin.extensions.getCalculatedFormattedDate
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.bottomsheet_finish_order_uoh.view.*
import kotlinx.android.synthetic.main.bottomsheet_kebab_menu_uoh.view.*
import kotlinx.android.synthetic.main.bottomsheet_ls_finish_order_uoh.view.*
import kotlinx.android.synthetic.main.bottomsheet_option_uoh.*
import kotlinx.android.synthetic.main.bottomsheet_option_uoh.view.*
import kotlinx.android.synthetic.main.bottomsheet_send_email.*
import kotlinx.android.synthetic.main.bottomsheet_send_email.view.*
import kotlinx.android.synthetic.main.bottomsheet_send_email.view.btn_send_email
import kotlinx.android.synthetic.main.fragment_uoh_list.*
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap


/**
 * Created by fwidjaja on 29/06/20.
 */
class UohListFragment: BaseDaggerFragment(), RefreshHandler.OnRefreshHandlerListener,
        UohBottomSheetOptionAdapter.ActionListener, UohBottomSheetKebabMenuAdapter.ActionListener,
        UohItemAdapter.ActionListener {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var uohItemAdapter: UohItemAdapter
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private lateinit var scrollRecommendationListener: EndlessRecyclerViewScrollListener
    private var refreshHandler: RefreshHandler? = null
    private var paramUohOrder = UohListParam()
    private var orderList: UohListOrder.Data.UohOrders = UohListOrder.Data.UohOrders()
    private var recommendationList: List<RecommendationWidget> = listOf()
    private var responseFinishOrder: UohFinishOrder.Data.FinishOrderBuyer = UohFinishOrder.Data.FinishOrderBuyer()
    private var responseLsPrintFinishOrder: LsPrintData.Data.Oiaction = LsPrintData.Data.Oiaction()
    private lateinit var uohBottomSheetOptionAdapter: UohBottomSheetOptionAdapter
    private lateinit var uohBottomSheetKebabMenuAdapter: UohBottomSheetKebabMenuAdapter
    private var bottomSheetOption: BottomSheetUnify? = null
    private var bottomSheetFinishOrder: BottomSheetUnify? = null
    private var bottomSheetLsFinishOrder: BottomSheetUnify? = null
    private var bottomSheetKebabMenu: BottomSheetUnify? = null
    private var bottomSheetResendEmail: BottomSheetUnify? = null
    private var currFilterKey: String = ""
    private var currFilterLabel: String = ""
    private var currFilterType: Int = -1
    private var filter1: SortFilterItem? = null
    private var filter2: SortFilterItem? = null
    private var filter3: SortFilterItem? = null
    private var defaultStartDate = ""
    private var defaultStartDateStr = ""
    private var defaultEndDate = ""
    private var defaultEndDateStr = ""
    private var arrayFilterDate = arrayOf<String>()
    private var onLoadMore = false
    private var onLoadMoreRecommendation = false
    private var currPage = 1
    private var currRecommendationListPage = 0
    private var textChangedJob: Job? = null
    private var isReset = false
    private var filterStatus = ""
    private var orderIdNeedUpdated = ""
    private var currIndexNeedUpdate = -1
    private var userSession: UserSessionInterface? = null
    private var chosenOrder: UohListOrder.Data.UohOrders.Order? = null
    private var isTyping = false
    private var isFilterClicked = false

    private val uohListViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[UohListViewModel::class.java]
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): UohListFragment {
            return UohListFragment().apply {
                arguments = bundle
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userSession = UserSession(context)
        if (arguments?.getString(SOURCE_FILTER) != null) {
            filterStatus = arguments?.getString(SOURCE_FILTER).toString()
            if (filterStatus.isNotEmpty()) {
                var status = ""
                when (filterStatus) {
                    PARAM_DALAM_PROSES -> {
                        status = DALAM_PROSES
                    }
                    PARAM_E_TIKET -> {
                        status = E_TIKET
                    }
                    PARAM_SEMUA_TRANSAKSI -> {
                        status = SEMUA_TRANSAKSI
                    }
                }
                paramUohOrder.status = status
                currFilterType = UohConsts.TYPE_FILTER_STATUS
                currFilterKey = status
                currFilterLabel = status
            }
        }
        loadOrderHistoryList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_uoh_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInitialValue()
        prepareLayout()
        observingData()
    }

    override fun onRefresh(view: View?) {
        onLoadMore = false
        onLoadMoreRecommendation = false
        currPage = 1
        currRecommendationListPage = 1
        uohItemAdapter.showLoader()
        loadOrderHistoryList()
    }

    private fun setInitialValue() {
        if (filterStatus.isEmpty()) {
            defaultStartDate = getCalculatedFormattedDate("yyyy-MM-dd", -90)
            defaultStartDateStr = getCalculatedFormattedDate("dd MMM yyyy", -90)
            defaultEndDate = Date().toFormattedString("yyyy-MM-dd")
            defaultEndDateStr = Date().toFormattedString("dd MMM yyyy")
            // paramUohOrder.createTimeStart = defaultStartDate
            // paramUohOrder.createTimeEnd = defaultEndDate
        }
        paramUohOrder.page = 1

        arrayFilterDate = resources.getStringArray(R.array.filter_date)
    }

    private fun observingData() {
        observingOrderHistory()
        observingRecommendationList()
        observingFinishOrder()
        observingAtc()
        observingLsFinishOrder()
        observingFlightResendEmail()
        observingTrainResendEmail()
        observingRechargeSetFail()
    }

    private fun prepareLayout() {
        refreshHandler = RefreshHandler(swipe_refresh_layout, this)
        refreshHandler?.setPullEnabled(true)

        uohItemAdapter = UohItemAdapter().apply {
            setActionListener(this@UohListFragment)
        }

        uohBottomSheetKebabMenuAdapter = UohBottomSheetKebabMenuAdapter(this)

        search_bar?.searchBarTextField?.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                textChangedJob?.cancel()
                textChangedJob = GlobalScope.launch(Dispatchers.Main) {
                    delay(500L)

                    if (!isTyping) {
                        isTyping = true

                        // resetting
                        resetFilter()
                    }

                    paramUohOrder.searchableText = s.toString()
                    refreshHandler?.startRefresh()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
        addEndlessScrollListener()
    }

    private fun addEndlessScrollListener() {
        val glm = GridLayoutManager(activity, 2)
        glm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (uohItemAdapter.getItemViewType(position)) {
                    UohItemAdapter.LAYOUT_EMPTY_STATE -> 2
                    UohItemAdapter.LAYOUT_RECOMMENDATION_LIST -> 1
                    else -> 2
                }
            }
        }

        scrollRecommendationListener = object : EndlessRecyclerViewScrollListener(glm) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                onLoadMoreRecommendation = true
                currentPage += 1
                // loadRecommendationList()
            }
        }

        rv_order_list?.layoutManager = glm
        rv_order_list?.adapter = uohItemAdapter
        rv_order_list?.addOnScrollListener(scrollRecommendationListener)
    }

    private fun loadOrderHistoryList() {
        paramUohOrder.page = currPage
        uohListViewModel.loadOrderList(GraphqlHelper.loadRawString(resources, R.raw.uoh_get_order_history), paramUohOrder)
    }

    private fun loadRecommendationList() {
        uohListViewModel.loadRecommendationList(currRecommendationListPage)
    }

    private fun observingOrderHistory() {
        if (orderIdNeedUpdated.isEmpty()) uohItemAdapter.showLoader()
        uohListViewModel.orderHistoryListResult.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is Success -> {
                    orderList = it.data

                    /*if (currFilterKey.isEmpty() && currFilterType == -1) {
                        if (orderList.filters.isNotEmpty() && orderList.categories.isNotEmpty()) {
                            renderChipsFilter()
                        }
                    } else {
                        renderChipsFilter()
                    }*/

                    if (!isFilterClicked) renderChipsFilter()

                    if (orderList.orders.isNotEmpty()) {
                        if (orderIdNeedUpdated.isEmpty()) {
                            renderOrderList()
                        } else {
                            loop@ for (i in orderList.orders.indices) {
                                if (orderList.orders[i].orderUUID.equals(orderIdNeedUpdated, true)) {
                                    uohItemAdapter.updateDataAtIndex(currIndexNeedUpdate, orderList.orders[i])
                                    orderIdNeedUpdated = ""
                                    break@loop
                                }
                            }
                        }
                    } else {
                        if (currPage == 1) {
                            loadRecommendationList()
                        }
                    }

                    if (orderList.tickers.isNotEmpty()) {
                        renderTicker()
                    } else {
                        ticker_info?.gone()
                    }
                    currPage += 1
                }
                is Fail -> {
                    // renderErrorOrderList(getString(R.string.error_list_title), getString(R.string.error_list_desc))
                }
            }
        })
    }

    private fun observingRecommendationList() {
        uohListViewModel.recommendationListResult.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is Success -> {
                    recommendationList = it.data
                    currRecommendationListPage += 1
                    if (recommendationList.isNotEmpty()) {
                        renderEmptyList()
                    }
                }
                is Fail -> {

                }
            }
        })
    }

    private fun observingFinishOrder() {
        uohListViewModel.finishOrderResult.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is Success -> {
                    responseFinishOrder = it.data
                    if (responseFinishOrder.success == 1) {
                        if (responseFinishOrder.message.isNotEmpty()) {
                            showToaster(responseFinishOrder.message.first(), Toaster.TYPE_NORMAL)
                        }
                        currPage -= 1
                        loadOrderHistoryList()
                    } else {
                        if (responseFinishOrder.message.isNotEmpty()) {
                            showToaster(responseFinishOrder.message.first(), Toaster.TYPE_ERROR)
                        } else {
                            context?.getString(R.string.fail_cancellation)?.let { it1 -> showToaster(it1, Toaster.TYPE_ERROR) }
                        }
                        chosenOrder?.let { it1 -> uohItemAdapter.updateDataAtIndex(currIndexNeedUpdate, it1) }
                    }
                }
                is Fail -> {
                    showToaster(responseFinishOrder.message.first(), Toaster.TYPE_ERROR)
                }
            }
        })
    }

    private fun observingAtc() {
        uohListViewModel.atcResult.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is Success -> {
                    val msg = StringUtils.convertListToStringDelimiter(it.data.atcMulti.buyAgainData.message, ",")
                    if (it.data.atcMulti.buyAgainData.success == 1) {
                        showToaster(msg, Toaster.TYPE_NORMAL)
                    } else {
                        showToaster(msg, Toaster.TYPE_ERROR)
                    }
                }
                is Fail -> {
                    context?.getString(R.string.fail_cancellation)?.let { it1 -> showToaster(it1, Toaster.TYPE_ERROR) }
                }
            }
        })
    }

    private fun observingLsFinishOrder() {
        uohListViewModel.lsPrintFinishOrderResult.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is Success -> {
                    responseLsPrintFinishOrder = it.data.oiaction
                    if (responseLsPrintFinishOrder.status == 200) {
                        if (responseLsPrintFinishOrder.data.message.isNotEmpty()) {
                            showToaster(responseLsPrintFinishOrder.data.message, Toaster.TYPE_NORMAL)
                        }
                        currPage -= 1
                        loadOrderHistoryList()
                    } else {
                        if (responseLsPrintFinishOrder.data.message.isNotEmpty()) {
                            showToaster(responseLsPrintFinishOrder.data.message, Toaster.TYPE_ERROR)
                        } else {
                            context?.getString(R.string.fail_cancellation)?.let { it1 -> showToaster(it1, Toaster.TYPE_ERROR) }
                        }
                        chosenOrder?.let { it1 -> uohItemAdapter.updateDataAtIndex(currIndexNeedUpdate, it1) }
                    }
                }
                is Fail -> {
                    showToaster(responseLsPrintFinishOrder.data.message, Toaster.TYPE_ERROR)
                }
            }
        })
    }

    private fun observingFlightResendEmail() {
        uohListViewModel.flightResendEmailResult.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is Success -> {
                    val flightEmailResponse = it.data.flightResendEmailV2
                    if (flightEmailResponse == null) {
                        bottomSheetKebabMenu?.tf_email?.setError(true)
                        bottomSheetKebabMenu?.tf_email?.setMessage(getString(R.string.toaster_failed_send_email))
                    } else {
                        if (flightEmailResponse.meta.status.equals(FLIGHT_STATUS_OK, true)) {
                            bottomSheetResendEmail?.dismiss()
                            bottomSheetKebabMenu?.dismiss()
                            showToaster(getString(R.string.toaster_succeed_send_email), Toaster.TYPE_NORMAL)
                        }
                    }
                }
                is Fail -> {
                    bottomSheetResendEmail?.tf_email?.setError(true)
                    bottomSheetResendEmail?.tf_email?.setMessage(getString(R.string.toaster_failed_send_email))
                }
            }
        })
    }

    private fun observingTrainResendEmail() {
        uohListViewModel.trainResendEmailResult.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is Success -> {
                    val trainEmailResponse = it.data.trainResendBookingEmail
                    if (trainEmailResponse == null) {
                        bottomSheetKebabMenu?.tf_email?.setError(true)
                        bottomSheetKebabMenu?.tf_email?.setMessage(getString(R.string.toaster_failed_send_email))
                    } else {
                        if (trainEmailResponse.success) {
                            bottomSheetResendEmail?.dismiss()
                            bottomSheetKebabMenu?.dismiss()
                            showToaster(getString(R.string.toaster_succeed_send_email), Toaster.TYPE_NORMAL)
                        }
                    }
                }
                is Fail -> {
                    bottomSheetResendEmail?.tf_email?.setError(true)
                    bottomSheetResendEmail?.tf_email?.setMessage(getString(R.string.toaster_failed_send_email))
                }
            }
        })
    }

    private fun observingRechargeSetFail() {
        uohListViewModel.rechargeSetFailResult.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is Success -> {
                    val isSuccess = it.data.rechargeSetOrderToFail.attributes.isSuccess
                    if (isSuccess) {
                        currPage -= 1
                        loadOrderHistoryList()
                    } else {
                        showToaster(it.data.rechargeSetOrderToFail.attributes.errorMessage, Toaster.TYPE_ERROR)
                    }
                }
                is Fail -> {
                    context?.getString(R.string.fail_cancellation)?.let { it1 -> showToaster(it1, Toaster.TYPE_ERROR) }
                }
            }
        })
    }

    @SuppressLint("SimpleDateFormat")
    private fun renderChipsFilter() {
        val chips = arrayListOf<SortFilterItem>()

        val typeDate = if (isReset || filterStatus.isNotEmpty()) {
            ChipsUnify.TYPE_NORMAL
        } else {
            ChipsUnify.TYPE_SELECTED
        }

        filter1 = SortFilterItem(UohConsts.ALL_DATE, typeDate, ChipsUnify.SIZE_MEDIUM)
        filter1?.listener = {
            uohBottomSheetOptionAdapter = UohBottomSheetOptionAdapter(this)
            showBottomSheetOptions(UohConsts.CHOOSE_DATE)
            val mapKey = HashMap<String, String>()
            var i = 0
            arrayFilterDate.forEach { optionDate ->
                mapKey["$i"] = optionDate
                i++
            }
            uohBottomSheetOptionAdapter.uohItemMapKeyList = mapKey
            uohBottomSheetOptionAdapter.filterType = UohConsts.TYPE_FILTER_DATE
            uohBottomSheetOptionAdapter.selectedKey = currFilterKey
            uohBottomSheetOptionAdapter.isReset = isReset
            uohBottomSheetOptionAdapter.notifyDataSetChanged()
        }
        if (filterStatus.isEmpty() && !isReset) {
            filter1?.title = arrayFilterDate[2]
        }
        filter1?.let {
            chips.add(it)
        }

        val typeStatus = if (filterStatus.isEmpty()) {
            ChipsUnify.TYPE_NORMAL
        } else {
            ChipsUnify.TYPE_SELECTED
        }
        filter2 = SortFilterItem(UohConsts.ALL_STATUS, typeStatus, ChipsUnify.SIZE_MEDIUM)
        filter2?.listener = {
            uohBottomSheetOptionAdapter = UohBottomSheetOptionAdapter(this)
            showBottomSheetOptions(UohConsts.CHOOSE_FILTERS)
            val mapKey = HashMap<String, String>()
            orderList.filters.forEach { option ->
                mapKey[option] = option
            }
            uohBottomSheetOptionAdapter.uohItemMapKeyList = mapKey
            uohBottomSheetOptionAdapter.filterType = UohConsts.TYPE_FILTER_STATUS
            uohBottomSheetOptionAdapter.selectedKey = currFilterKey
            uohBottomSheetOptionAdapter.isReset = isReset
            uohBottomSheetOptionAdapter.notifyDataSetChanged()
        }
        if (filterStatus.isNotEmpty() && !isReset) {
            filter2?.title = currFilterLabel
        }
        filter2?.let { chips.add(it) }

        filter3 = SortFilterItem(UohConsts.ALL_CATEGORIES, ChipsUnify.TYPE_NORMAL, ChipsUnify.SIZE_MEDIUM)
        filter3?.listener = {
            uohBottomSheetOptionAdapter = UohBottomSheetOptionAdapter(this)
            showBottomSheetOptions(UohConsts.CHOOSE_CATEGORIES)
            val mapKey = HashMap<String, String>()
            orderList.categories.forEach { category ->
                mapKey[category.value] = category.label
            }
            uohBottomSheetOptionAdapter.uohItemMapKeyList = mapKey
            uohBottomSheetOptionAdapter.filterType = UohConsts.TYPE_FILTER_CATEGORY
            uohBottomSheetOptionAdapter.selectedKey = currFilterKey
            uohBottomSheetOptionAdapter.isReset = isReset
            uohBottomSheetOptionAdapter.notifyDataSetChanged()
        }
        filter3?.let { chips.add(it) }

        uoh_sort_filter?.addItem(chips)
        uoh_sort_filter?.sortFilterPrefix?.setOnClickListener {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd")
            val outputFormat = SimpleDateFormat("d MMM yyyy")
            val limitDate = inputFormat.parse(orderList.dateLimit)
            val limitDateStr = outputFormat.format(limitDate)
            val resetMsg = resources.getString(R.string.uoh_reset_filter_msg).replace(UohConsts.DATE_LIMIT, limitDateStr)
            showToaster(resetMsg, Toaster.TYPE_NORMAL)
            resetFilter()
            refreshHandler?.startRefresh()
        }

        filter1?.refChipUnify?.setChevronClickListener {  }
        filter2?.refChipUnify?.setChevronClickListener {  }
        filter3?.refChipUnify?.setChevronClickListener {  }
    }

    private fun resetFilter() {
        isFilterClicked = false
        isReset = true
        uoh_sort_filter?.resetAllFilters()
        filter1?.title = UohConsts.ALL_DATE
        filter2?.title = UohConsts.ALL_STATUS
        filter3?.title = UohConsts.ALL_CATEGORIES
        paramUohOrder = UohListParam()
    }

    private fun renderTicker() {
        ticker_info?.visible()

        if (orderList.tickers.size > 1) {
            val listTickerData = arrayListOf<TickerData>()
            orderList.tickers.forEach {
                var desc = it.text
                if (it.action.appUrl.isNotEmpty() && it.action.label.isNotEmpty()) {
                    desc += " ${getString(R.string.buyer_ticker_info_selengkapnya)
                            .replace(UohConsts.TICKER_URL, it.action.appUrl)
                            .replace(UohConsts.TICKER_LABEL, it.action.label)}"
                }
                listTickerData.add(TickerData(it.title, desc, UohUtils.getTickerType(it.text), true))
            }
            context?.let {
                val adapter = TickerPagerAdapter(it, listTickerData)
                adapter.setPagerDescriptionClickEvent(object : TickerPagerCallback {
                    override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
                        // TODO : cek lagi url applink nya, make sure lagi nanti
                        RouteManager.route(context, linkUrl.toString())
                    }
                })
                ticker_info?.setDescriptionClickEvent(object: TickerCallback {
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {}

                    override fun onDismiss() {
                    }

                })
                ticker_info?.addPagerView(adapter, listTickerData)
            }
        } else {
            orderList.tickers.first().let {
                ticker_info?.tickerTitle = it.title
                var desc = it.text
                if (it.action.appUrl.isNotEmpty() && it.action.label.isNotEmpty()) {
                    desc += " ${getString(R.string.buyer_ticker_info_selengkapnya)
                            .replace(UohConsts.TICKER_URL, it.action.appUrl)
                            .replace(UohConsts.TICKER_LABEL, it.action.label)}"
                }
                ticker_info?.setHtmlDescription(desc)
                ticker_info?.tickerType = UohUtils.getTickerType(it.type)
                ticker_info?.setDescriptionClickEvent(object : TickerCallback {
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {
                        RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, linkUrl))
                    }

                    override fun onDismiss() {
                    }

                })
            }
        }
    }

    private fun renderOrderList() {
        refreshHandler?.finishRefresh()

        val listOrder = arrayListOf<UohTypeData>()
        orderList.orders.forEach {
            listOrder.add(UohTypeData(it, UohConsts.TYPE_ORDER_LIST))
        }

        if (!onLoadMore) {
            uohItemAdapter.addList(listOrder)
        } else {
            uohItemAdapter.appendList(listOrder)
            scrollListener.updateStateAfterGetData()
        }
    }

    private fun renderEmptyList() {
        refreshHandler?.finishRefresh()
        val searchBarIsNotEmpty = search_bar?.searchBarTextField?.text?.isNotEmpty()
        var searchBarEmpty = false
        searchBarIsNotEmpty?.let { searchBarEmpty = it }

        val emptyStatus: UohEmptyState?
        when {
            searchBarEmpty -> {
                emptyStatus = context?.let { context ->
                    view?.let { UohUtils.hideKeyBoard(context, it) }
                    ContextCompat.getDrawable(context, R.drawable.uoh_empty_search_list)?.let { drawable ->
                        UohEmptyState(drawable,
                                resources.getString(R.string.uoh_search_empty),
                                resources.getString(R.string.uoh_search_empty_desc),
                                false, "")
                    }
                }
            }
            paramUohOrder.status.isNotEmpty() -> {
                emptyStatus = context?.let { context ->
                    ContextCompat.getDrawable(context, R.drawable.uoh_empty_order_list)?.let { drawable ->
                        UohEmptyState(drawable,
                                resources.getString(R.string.uoh_filter_empty),
                                resources.getString(R.string.uoh_filter_empty_desc),
                                true, resources.getString(R.string.uoh_filter_empty_btn))
                    }
                }
            }
            else -> {
                emptyStatus = context?.let { context ->
                    ContextCompat.getDrawable(context, R.drawable.uoh_empty_order_list)?.let { drawable ->
                        UohEmptyState(drawable,
                                resources.getString(R.string.uoh_no_order),
                                resources.getString(R.string.uoh_no_order_desc),
                                true, resources.getString(R.string.uoh_no_order_btn))
                    }
                }
            }
        }

        val listRecomm = arrayListOf<UohTypeData>()
        if (!onLoadMoreRecommendation) {
            emptyStatus?.let { emptyState -> UohTypeData(emptyState, UohConsts.TYPE_EMPTY) }?.let { uohTypeData -> listRecomm.add(uohTypeData) }
            listRecomm.add(UohTypeData(getString(R.string.uoh_recommendation_title), UohConsts.TYPE_RECOMMENDATION_TITLE))
            recommendationList.first().recommendationItemList.forEach {
                listRecomm.add(UohTypeData(it, UohConsts.TYPE_RECOMMENDATION_ITEM))
            }
            uohItemAdapter.addList(listRecomm)
        } else {
            recommendationList.first().recommendationItemList.forEach {
                listRecomm.add(UohTypeData(it, UohConsts.TYPE_RECOMMENDATION_ITEM))
            }
            uohItemAdapter.appendList(listRecomm)
            scrollRecommendationListener.updateStateAfterGetData()
        }
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
                when (currFilterType) {
                    UohConsts.TYPE_FILTER_DATE -> {
                        filter1?.type = ChipsUnify.TYPE_SELECTED
                        if (currFilterKey.toInt() == 3) {
                            if (paramUohOrder.createTimeStart.isNotEmpty() && paramUohOrder.createTimeEnd.isNotEmpty()) {
                                val splitStartDate = paramUohOrder.createTimeStart.split('-')
                                val splitEndDate = paramUohOrder.createTimeEnd.split('-')
                                filter1?.title = "${splitStartDate[2]}/${splitStartDate[1]}/${splitStartDate[0]} - ${splitEndDate[2]}/${splitEndDate[1]}/${splitEndDate[0]}"
                            }
                        } else {
                            filter1?.title = currFilterLabel
                        }
                    }
                    UohConsts.TYPE_FILTER_STATUS -> {
                        filter2?.type = ChipsUnify.TYPE_SELECTED
                        filter2?.title = currFilterLabel
                    }
                    UohConsts.TYPE_FILTER_CATEGORY -> {
                        filter3?.type = ChipsUnify.TYPE_SELECTED
                        filter3?.title = currFilterLabel
                    }
                }
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

    private fun showBottomSheetKebabMenu() {
        val viewBottomSheet = View.inflate(context, R.layout.bottomsheet_kebab_menu_uoh, null)
        viewBottomSheet.rv_kebab.adapter = uohBottomSheetKebabMenuAdapter
        viewBottomSheet.rv_kebab.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)

        bottomSheetKebabMenu = BottomSheetUnify().apply {
            setChild(viewBottomSheet)
            setTitle(UohConsts.OTHERS)
            setCloseClickListener { dismiss() }
        }

        fragmentManager?.let { bottomSheetKebabMenu?.show(it, getString(R.string.show_bottomsheet)) }
    }

    private fun showBottomSheetFinishOrder(index: Int, orderId: String) {
        val viewBottomSheet = View.inflate(context, R.layout.bottomsheet_finish_order_uoh, null).apply {

            btn_finish_order?.setOnClickListener {
                bottomSheetFinishOrder?.dismiss()
                chosenOrder = uohItemAdapter.getDataAtIndex(index)
                uohItemAdapter.showLoaderAtIndex(index)
                currIndexNeedUpdate = index

                val paramFinishOrder = userSession?.userId?.let { it1 -> UohFinishOrderParam(orderId = orderId, userId = it1) }
                if (paramFinishOrder != null) {
                    uohListViewModel.doFinishOrder(GraphqlHelper.loadRawString(resources, R.raw.uoh_finish_order), paramFinishOrder)
                }
            }

            btn_ajukan_komplain?.setOnClickListener {
                // go to komplain page
            }
        }

        bottomSheetFinishOrder = BottomSheetUnify().apply {
            setChild(viewBottomSheet)
            setTitle(FINISH_ORDER_BOTTOMSHEET_TITLE)
            setCloseClickListener { dismiss() }
        }

        fragmentManager?.let { bottomSheetFinishOrder?.show(it, getString(R.string.show_bottomsheet)) }
    }

    private fun showBottomSheetLsFinishOrder(index: Int, orderId: String) {
        val viewBottomSheet = View.inflate(context, R.layout.bottomsheet_ls_finish_order_uoh, null)
        viewBottomSheet.btn_ls_finish_order?.setOnClickListener {
            bottomSheetLsFinishOrder?.dismiss()
            chosenOrder = uohItemAdapter.getDataAtIndex(index)
            uohItemAdapter.showLoaderAtIndex(index)
            currIndexNeedUpdate = index
            uohListViewModel.doLsPrintFinishOrder(GraphqlHelper.loadRawString(resources, R.raw.uoh_finish_lsprint), orderId)
        }

        viewBottomSheet.btn_ls_kembali?.setOnClickListener {
            bottomSheetLsFinishOrder?.dismiss()
        }

        bottomSheetLsFinishOrder = BottomSheetUnify().apply {
            setChild(viewBottomSheet)
            setTitle(FINISH_ORDER_BOTTOMSHEET_TITLE)
            setCloseClickListener { dismiss() }
        }

        fragmentManager?.let { bottomSheetLsFinishOrder?.show(it, getString(R.string.show_bottomsheet)) }
    }

    private fun showBottomSheetSendEmail(gqlGroup: String, index: Int) {
        val viewBottomSheet = View.inflate(context, R.layout.bottomsheet_send_email, null)
        viewBottomSheet.tf_email?.textFieldInput?.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val isEmailValid = UohUtils.isEmailValid(s.toString())
                viewBottomSheet.btn_send_email?.isEnabled = isEmailValid
                if (s.toString().isEmpty()) {
                    viewBottomSheet.tf_email?.setError(true)
                    viewBottomSheet.tf_email?.setMessage(EMAIL_MUST_NOT_BE_EMPTY)
                } else {
                    if (!isEmailValid) {
                        viewBottomSheet.tf_email?.setError(true)
                        viewBottomSheet.tf_email?.setMessage(WRONG_FORMAT_EMAIL)
                    } else {
                        viewBottomSheet.tf_email?.setError(false)
                        viewBottomSheet.tf_email?.setMessage("")
                    }
                }
            }

        })
        viewBottomSheet.btn_send_email?.setOnClickListener {
            if (gqlGroup.equals(GQL_FLIGHT_EMAIL, true)) {
                val flightQueryParam = Gson().fromJson(orderList.orders[index].metadata.queryParams, FlightQueryParams::class.java)
                val invoiceId = flightQueryParam.invoiceId
                val email = "${viewBottomSheet.tf_email.textFieldInput.text}"
                uohListViewModel.doFlightResendEmail(GraphqlHelper.loadRawString(resources, R.raw.uoh_send_eticket_flight), invoiceId, email)

            } else if (gqlGroup.equals(GQL_TRAIN_EMAIL, true)) {
                val trainQueryParam = Gson().fromJson(orderList.orders[index].metadata.queryParams, TrainQueryParams::class.java)
                val invoiceId = trainQueryParam.invoiceId
                val email = "${viewBottomSheet.tf_email.textFieldInput.text}"
                val param = TrainResendEmailParam(bookCode = invoiceId, email = email)
                uohListViewModel.doTrainResendEmail(GraphqlHelper.loadRawString(resources, R.raw.uoh_send_eticket_flight), param)
            }
        }

        viewBottomSheet.btn_ls_kembali?.setOnClickListener {
            bottomSheetLsFinishOrder?.dismiss()
        }

        bottomSheetResendEmail = BottomSheetUnify().apply {
            isFullpage = false
            isKeyboardOverlap = false
            setChild(viewBottomSheet)
            setTitle(FINISH_ORDER_BOTTOMSHEET_TITLE)
            setCloseClickListener { dismiss() }
        }

        fragmentManager?.let { bottomSheetResendEmail?.show(it, getString(R.string.show_bottomsheet)) }
    }

    override fun onOptionItemClick(option: String, label: String, filterType: Int) {
        isFilterClicked = true
        currFilterKey = option
        currFilterLabel = label
        currFilterType = filterType

        when (filterType) {
            UohConsts.TYPE_FILTER_DATE -> {
                when {
                    option.toInt() == 0 -> {
                        bottomSheetOption?.apply {
                            cl_choose_date?.gone()
                        }
                        paramUohOrder.createTimeStart = ""
                        paramUohOrder.createTimeEnd = ""

                    }
                    option.toInt() == 1 -> {
                        bottomSheetOption?.apply {
                            cl_choose_date?.gone()
                        }
                        val startDate = getCalculatedFormattedDate("yyyy-MM-dd", -30)
                        val endDate= Date().toFormattedString("yyyy-MM-dd")
                        paramUohOrder.createTimeStart = startDate
                        paramUohOrder.createTimeEnd = endDate

                    }
                    option.toInt() == 2 -> {
                        bottomSheetOption?.apply {
                            cl_choose_date?.gone()
                        }
                        val startDate = getCalculatedFormattedDate("yyyy-MM-dd", -90)
                        val endDate= Date().toFormattedString("yyyy-MM-dd")
                        paramUohOrder.createTimeStart = startDate
                        paramUohOrder.createTimeEnd = endDate

                    }
                    option.toInt() == 3 -> {
                        bottomSheetOption?.apply {
                            cl_choose_date?.visible()
                            tf_start_date?.textFieldInput?.setText(defaultStartDateStr)
                            tf_start_date?.textFieldInput?.isFocusable = false
                            tf_start_date?.textFieldInput?.isClickable = true
                            tf_start_date?.textFieldInput?.setOnClickListener { showDatePicker(START_DATE) }

                            tf_end_date?.textFieldInput?.setText(defaultEndDateStr)
                            tf_end_date?.textFieldInput?.isFocusable = false
                            tf_end_date?.textFieldInput?.isClickable = true
                            tf_end_date?.textFieldInput?.setOnClickListener { showDatePicker(END_DATE) }
                        }
                    }
                }
            }
            UohConsts.TYPE_FILTER_STATUS -> {
                paramUohOrder.status = option
            }
            UohConsts.TYPE_FILTER_CATEGORY -> {
                paramUohOrder.verticalCategory = option
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showDatePicker(flag: String) {
        context?.let {  context ->
            val minDate = Calendar.getInstance()

            val resultMinDate = orderList.dateLimit.split('-')
            minDate.set(Calendar.YEAR, resultMinDate[0].toInt())
            minDate.set(Calendar.MONTH, resultMinDate[1].toInt())
            minDate.set(Calendar.DATE, resultMinDate[2].toInt())
            val maxDate = Calendar.getInstance()
            val isEndDateFilled = paramUohOrder.createTimeEnd.isNotEmpty()
            if (isEndDateFilled && flag.equals(START_DATE, true)) {
                val splitEndDate = paramUohOrder.createTimeEnd.split('-')
                maxDate.set(splitEndDate[0].toInt(), splitEndDate[1].toInt() - 1, splitEndDate[2].toInt())
            }
            val isStartDateFilled = paramUohOrder.createTimeStart.isNotEmpty()
            if (isStartDateFilled && flag.equals(END_DATE, true)) {
                val splitStartDate = paramUohOrder.createTimeStart.split('-')
                minDate.set(splitStartDate[0].toInt(), splitStartDate[1].toInt() - 1, splitStartDate[2].toInt())
            }

            val currentDate = Calendar.getInstance()

            val splitDate: List<String>? = if (flag.equals(START_DATE, true)) {
                paramUohOrder.createTimeStart.split('-')
            } else {
                paramUohOrder.createTimeEnd.split('-')
            }

            splitDate?.let {
                currentDate.set(it[0].toInt(), it[1].toInt()-1, it[2].toInt())
                val datePicker = DatePickerUnify(context, minDate, currentDate, maxDate)
                fragmentManager?.let { it1 -> datePicker.show(it1, "") }
                datePicker.datePickerButton.setOnClickListener {
                    val resultDate = datePicker.getDate()
                    val monthInt = resultDate[1]+1
                    var monthStr = monthInt.toString()
                    if (monthStr.length == 1) monthStr = "0$monthStr"

                    var dateStr = resultDate[0].toString()
                    if (dateStr.length == 1) dateStr = "0$dateStr"

                    if (flag.equals(START_DATE, true)) {
                        paramUohOrder.createTimeStart = "${resultDate[2]}-$monthStr-$dateStr"
                        bottomSheetOption?.tf_start_date?.textFieldInput?.setText("$dateStr ${convertMonth(resultDate[1])} ${resultDate[2]}")
                        defaultStartDateStr = "$dateStr ${convertMonth(resultDate[1])} ${resultDate[2]}"
                    } else {
                        paramUohOrder.createTimeEnd = "${resultDate[2]}-$monthStr-$dateStr"
                        bottomSheetOption?.tf_end_date?.textFieldInput?.setText("$dateStr ${convertMonth(resultDate[1])} ${resultDate[2]}")
                        defaultEndDateStr = "$dateStr ${convertMonth(resultDate[1])} ${resultDate[2]}"
                    }
                    datePicker.dismiss()
                }
                if (flag.equals(START_DATE, true)) {
                    datePicker.setTitle(getString(R.string.uoh_custom_start_date))
                } else {
                    datePicker.setTitle(getString(R.string.uoh_custom_end_date))
                }
                datePicker.setCloseClickListener { datePicker.dismiss() }
            }
        }
    }

    private fun showToaster(message: String, type: Int) {
        val toasterSuccess = Toaster
        view?.let { v ->
            toasterSuccess.make(v, message, Toaster.LENGTH_SHORT, type, "")
        }
    }

    override fun onKebabMenuClicked(listDotMenu: List<UohListOrder.Data.UohOrders.Order.Metadata.DotMenu>) {
        showBottomSheetKebabMenu()
        uohBottomSheetKebabMenuAdapter.addList(listDotMenu)
    }

    override fun onKebabItemClick(dotMenu: UohListOrder.Data.UohOrders.Order.Metadata.DotMenu, index: Int) {
        if (dotMenu.actionType.equals(TYPE_ACTION_BUTTON_LINK, true)) {
            RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, dotMenu.appURL))
        } else {
            when {
                dotMenu.actionType.equals(GQL_FLIGHT_EMAIL, true) -> {
                    showBottomSheetSendEmail(GQL_FLIGHT_EMAIL, index)
                }
                dotMenu.actionType.equals(GQL_TRAIN_EMAIL, true) -> {
                    showBottomSheetSendEmail(GQL_TRAIN_EMAIL, index)
                }
                dotMenu.actionType.equals(GQL_MP_REJECT, true) -> {
                    goToBuyerCancellation()
                }
            }
        }
    }

    private fun goToBuyerCancellation() {

    }

    override fun onListItemClicked(detailUrl: UohListOrder.Data.UohOrders.Order.Metadata.DetailUrl) {
        /*if (verticalCategory.equals(UohConsts.LS_PRINT_VERTICAL_CATEGORY, true)) {
            val url = "m.tokopedia.com/order-details/lsprint/$verticalId&upstream=$upstream"
            RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, url))
        } else {
            val applink = "${UohConsts.APPLINK_BASE}${UohConsts.APPLINK_PATH_ORDER}/$verticalId?${UohConsts.APPLINK_PATH_UPSTREAM}$upstream"
            RouteManager.route(context, applink)
        }*/

        if (detailUrl.appTypeLink == WEB_LINK_TYPE) {
            RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, detailUrl.webURL))
        } else if (detailUrl.appTypeLink == APP_LINK_TYPE) {
            RouteManager.route(context, detailUrl.appURL)
        }
    }

    override fun onActionButtonClicked(button: UohListOrder.Data.UohOrders.Order.Metadata.Button,
                                       index: Int, orderUUID: String, verticalId: String, listProducts: String) {
        if (button.actionType.equals(TYPE_ACTION_BUTTON_LINK, true)) {
            RouteManager.route(context, button.appURL)
        } else {
            when {
                button.actionType.equals(GQL_FINISH_ORDER, true) -> {
                    orderIdNeedUpdated = orderUUID
                    showBottomSheetFinishOrder(index, verticalId)
                }
                button.actionType.equals(GQL_ATC, true) -> {
                    val listOfStrings = Gson().fromJson(listProducts, mutableListOf<String>().javaClass)
                    val jsonArray: JsonArray = Gson().toJsonTree(listOfStrings).asJsonArray
                    uohListViewModel.doAtc(GraphqlHelper.loadRawString(resources, R.raw.buy_again), jsonArray)
                }
                button.actionType.equals(GQL_TRACK, true) -> {
                    val applinkTrack = ApplinkConst.ORDER_TRACKING.replace(REPLACE_ORDER_ID, verticalId)
                    RouteManager.route(context, applinkTrack)
                }
                button.actionType.equals(GQL_LS_FINISH, true) -> {
                    orderIdNeedUpdated = orderUUID
                    showBottomSheetLsFinishOrder(index, verticalId)
                }
                button.actionType.equals(GQL_LS_LACAK, true) -> {
                    val linkUrl = LS_LACAK_MWEB.replace(REPLACE_ORDER_ID, verticalId)
                    RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, linkUrl))
                }
                button.actionType.equals(GQL_RECHARGE_BATALKAN, true) -> {
                    orderIdNeedUpdated = orderUUID
                    uohListViewModel.doRechargeSetFail(GraphqlHelper.loadRawString(resources, R.raw.recharge_set_fail), verticalId.toInt())
                }
            }
        }
    }
}