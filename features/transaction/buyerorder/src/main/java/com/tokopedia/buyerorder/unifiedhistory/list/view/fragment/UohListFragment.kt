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
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.ALL_CATEGORIES
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.ALL_DATE
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.ALL_STATUS
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.ALL_TRANSACTIONS
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.APP_LINK_TYPE
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.CTA_ATC
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.EE_PRODUCT_ID
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.EE_PRODUCT_PRICE
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.EE_QUANTITY
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.EE_SHOP_ID
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.EMAIL_MUST_NOT_BE_EMPTY
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.END_DATE
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.FINISH_ORDER_BOTTOMSHEET_TITLE
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.FLIGHT_STATUS_OK
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.GQL_ATC
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.GQL_FINISH_ORDER
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.GQL_FLIGHT_EMAIL
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.GQL_LS_FINISH
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.GQL_LS_LACAK
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.GQL_MP_CHAT
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.GQL_MP_FINISH
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.GQL_RECHARGE_BATALKAN
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.GQL_TRACK
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.GQL_TRAIN_EMAIL
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.LS_LACAK_MWEB
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.REPLACE_ORDER_ID
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.START_DATE
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.TYPE_ACTION_BUTTON_LINK
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.URL_RESO
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.WEB_LINK_TYPE
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.WRONG_FORMAT_EMAIL
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohUtils
import com.tokopedia.buyerorder.unifiedhistory.list.analytics.UohAnalytics
import com.tokopedia.buyerorder.unifiedhistory.list.analytics.data.model.ECommerceAdd
import com.tokopedia.buyerorder.unifiedhistory.list.analytics.data.model.ECommerceAddRecommendation
import com.tokopedia.buyerorder.unifiedhistory.list.analytics.data.model.ECommerceClick
import com.tokopedia.buyerorder.unifiedhistory.list.analytics.data.model.ECommerceImpressions
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.*
import com.tokopedia.buyerorder.unifiedhistory.list.di.DaggerUohListComponent
import com.tokopedia.buyerorder.unifiedhistory.list.view.adapter.UohBottomSheetKebabMenuAdapter
import com.tokopedia.buyerorder.unifiedhistory.list.view.adapter.UohBottomSheetOptionAdapter
import com.tokopedia.buyerorder.unifiedhistory.list.view.adapter.UohItemAdapter
import com.tokopedia.buyerorder.unifiedhistory.list.view.viewmodel.UohListViewModel
import com.tokopedia.datepicker.DatePickerUnify
import com.tokopedia.datepicker.datetimepicker.DateTimePickerUnify
import com.tokopedia.design.utils.StringUtils
import com.tokopedia.kotlin.extensions.convertMonth
import com.tokopedia.kotlin.extensions.getCalculatedFormattedDate
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.Toaster
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
import kotlinx.android.synthetic.main.fragment_uoh_list.*
import kotlinx.coroutines.*
import java.net.URLDecoder
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
    private var currFilterDateKey: String = ""
    private var currFilterStatusKey: String = ""
    private var currFilterCategoryKey: String = ""
    private var currFilterDateLabel: String = ""
    private var currFilterStatusLabel: String = ""
    private var currFilterCategoryLabel: String = ""
    private var currFilterType: Int = -1
    private var tempFilterDateKey: String = ""
    private var tempFilterStatusKey: String = ""
    private var tempFilterCategoryKey: String = ""
    private var tempFilterDateLabel: String = ""
    private var tempFilterStatusLabel: String = ""
    private var tempFilterCategoryLabel: String = ""
    private var tempFilterType: Int = -1
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
    private var isFetchRecommendation = false
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
                        paramUohOrder.createTimeStart = ""
                        paramUohOrder.createTimeEnd = ""
                    }
                    PARAM_E_TIKET -> {
                        status = E_TIKET
                        paramUohOrder.createTimeStart = ""
                        paramUohOrder.createTimeEnd = ""
                    }
                    PARAM_SEMUA_TRANSAKSI -> {
                        status = SEMUA_TRANSAKSI
                    }
                    PARAM_MARKETPLACE -> {
                        status = SEMUA_TRANSAKSI
                        paramUohOrder.verticalCategory = PARAM_MARKETPLACE
                    }
                    PARAM_MARKETPLACE_DALAM_PROSES -> {
                        status = DALAM_PROSES
                        paramUohOrder.verticalCategory = PARAM_MARKETPLACE
                    }
                }
                paramUohOrder.status = status
                currFilterType = UohConsts.TYPE_FILTER_STATUS
                currFilterStatusKey = status
                currFilterStatusLabel = status
            }
        }
        setInitialValue()
        loadOrderHistoryList("")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_uoh_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareLayout()
        observingData()
    }

    override fun onRefresh(view: View?) {
        onLoadMore = false
        onLoadMoreRecommendation = false
        currPage = 1
        currRecommendationListPage = 1
        uohItemAdapter.showLoader()
        loadOrderHistoryList("")
    }

    private fun setInitialValue() {
        if (filterStatus.equals(PARAM_SEMUA_TRANSAKSI, true) || filterStatus.equals(PARAM_MARKETPLACE, true)) {
            setDefaultDate()
        }
        paramUohOrder.page = 1
        arrayFilterDate = resources.getStringArray(R.array.filter_date)
    }

    private fun setDefaultDate() {
        defaultStartDate = getCalculatedFormattedDate("yyyy-MM-dd", -90)
        defaultStartDateStr = getCalculatedFormattedDate("dd MMM yyyy", -90)
        defaultEndDate = Date().toFormattedString("yyyy-MM-dd")
        defaultEndDateStr = Date().toFormattedString("dd MMM yyyy")
        paramUohOrder.createTimeStart = defaultStartDate
        paramUohOrder.createTimeEnd = defaultEndDate
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
                        resetFilter()
                    }

                    view?.let { context?.let { it1 -> UohUtils.hideKeyBoard(it1, it) } }
                    paramUohOrder.searchableText = s.toString()
                    refreshHandler?.startRefresh()
                    userSession?.userId?.let { UohAnalytics.submitSearch(s.toString(), it) }
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
                currentPage += 1
                if (isFetchRecommendation) {
                    onLoadMoreRecommendation = true
                    loadRecommendationList()
                } else {
                    onLoadMore = true
                    loadOrderHistoryList("")
                }
            }
        }

        rv_order_list?.layoutManager = glm
        rv_order_list?.adapter = uohItemAdapter
        rv_order_list?.addOnScrollListener(scrollRecommendationListener)
    }

    private fun loadOrderHistoryList(uuid: String) {
        if (uuid.isNotEmpty()) {
            paramUohOrder.uUID = uuid
        } else {
            paramUohOrder.uUID = ""
        }
        paramUohOrder.page = currPage
        uohListViewModel.loadOrderList(GraphqlHelper.loadRawString(resources, R.raw.uoh_get_order_history), paramUohOrder)
    }

    private fun loadRecommendationList() {
        isFetchRecommendation = true
        uohListViewModel.loadRecommendationList(currRecommendationListPage)
    }

    private fun observingOrderHistory() {
        if (orderIdNeedUpdated.isEmpty()) uohItemAdapter.showLoader()
        uohListViewModel.orderHistoryListResult.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is Success -> {
                    orderList = it.data

                    if (!isFilterClicked && currPage == 1) renderChipsFilter()

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
                        userSession?.isLoggedIn?.let { it1 -> userSession?.userId?.let { it2 -> UohAnalytics.viewOrderListPage(it1, it2) } }
                    } else {
                        if (currPage == 1) {
                            loadRecommendationList()
                        }
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
                        loadOrderHistoryList(orderIdNeedUpdated)
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
                        showToasterAtc(msg, Toaster.TYPE_NORMAL)
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
                        loadOrderHistoryList(orderIdNeedUpdated)
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
                        loadOrderHistoryList(orderIdNeedUpdated)
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

        val typeDate = if (isReset || (paramUohOrder.createTimeStart.isEmpty() && paramUohOrder.createTimeEnd.isEmpty())) {
            ChipsUnify.TYPE_NORMAL
        } else {
            ChipsUnify.TYPE_SELECTED
        }

        filter1 = SortFilterItem(UohConsts.ALL_DATE, typeDate, ChipsUnify.SIZE_SMALL)
        filter1?.listener = {
            uohBottomSheetOptionAdapter = UohBottomSheetOptionAdapter(this)
            showBottomSheetFilterOptions(UohConsts.CHOOSE_DATE)
            val arrayListMap = arrayListOf<HashMap<String, String>>()
            var i = 0
            arrayFilterDate.forEach { optionDate ->
                val mapKey = HashMap<String, String>()
                mapKey["$i"] = optionDate
                arrayListMap.add(mapKey)
                i++
            }
            uohBottomSheetOptionAdapter.uohItemMapKeyList = arrayListMap
            uohBottomSheetOptionAdapter.filterType = UohConsts.TYPE_FILTER_DATE
            if ((filterStatus.equals(PARAM_SEMUA_TRANSAKSI, true) || filterStatus.equals(PARAM_MARKETPLACE, true)) && !isReset) {
                uohBottomSheetOptionAdapter.selectedKey = "2"
            } else {
                uohBottomSheetOptionAdapter.selectedKey = currFilterDateKey
            }
            uohBottomSheetOptionAdapter.isReset = isReset
            uohBottomSheetOptionAdapter.notifyDataSetChanged()
        }
        if ((filterStatus.equals(PARAM_SEMUA_TRANSAKSI, true) || filterStatus.equals(PARAM_MARKETPLACE, true)) && !isReset) {
            filter1?.title = arrayFilterDate[2]
        }
        filter1?.let {
            chips.add(it)
        }

        val typeStatus = if (filterStatus.isEmpty() || filterStatus.equals(PARAM_SEMUA_TRANSAKSI, true)
                || filterStatus.equals(PARAM_MARKETPLACE, true)) {
            ChipsUnify.TYPE_NORMAL
        } else {
            ChipsUnify.TYPE_SELECTED
        }
        filter2 = SortFilterItem(ALL_STATUS, typeStatus, ChipsUnify.SIZE_SMALL)
        filter2?.listener = {
            uohBottomSheetOptionAdapter = UohBottomSheetOptionAdapter(this)
            showBottomSheetFilterOptions(UohConsts.CHOOSE_FILTERS)
            val arrayListMap = arrayListOf<HashMap<String, String>>()
            orderList.filters.forEach { option ->
                val mapKey = HashMap<String, String>()
                mapKey[option] = option
                arrayListMap.add(mapKey)
            }
            uohBottomSheetOptionAdapter.uohItemMapKeyList = arrayListMap
            uohBottomSheetOptionAdapter.filterType = UohConsts.TYPE_FILTER_STATUS
            uohBottomSheetOptionAdapter.selectedKey = currFilterStatusKey
            uohBottomSheetOptionAdapter.isReset = isReset
            uohBottomSheetOptionAdapter.notifyDataSetChanged()
        }
        if (filterStatus.isNotEmpty() && !isReset) {
            filter2?.title = currFilterStatusLabel
        }
        filter2?.let { chips.add(it) }

        val typeCategory = if (filterStatus.equals(PARAM_MARKETPLACE, true)) {
            ChipsUnify.TYPE_NORMAL
        } else {
            ChipsUnify.TYPE_SELECTED
        }

        filter3 = SortFilterItem(ALL_CATEGORIES, typeCategory, ChipsUnify.SIZE_SMALL)
        filter3?.listener = {
            uohBottomSheetOptionAdapter = UohBottomSheetOptionAdapter(this)
            showBottomSheetFilterOptions(UohConsts.CHOOSE_CATEGORIES)
            val arrayListMap = arrayListOf<HashMap<String, String>>()
            val mapKeyDefault = HashMap<String, String>()
            mapKeyDefault[ALL_CATEGORIES] = UohConsts.ALL_CATEGORIES_TRANSACTION
            arrayListMap.add(mapKeyDefault)
            orderList.categories.forEach { category ->
                val mapKey = HashMap<String, String>()
                mapKey[category.value] = category.label
                arrayListMap.add(mapKey)
            }
            uohBottomSheetOptionAdapter.uohItemMapKeyList = arrayListMap
            uohBottomSheetOptionAdapter.filterType = UohConsts.TYPE_FILTER_CATEGORY

            if (filterStatus.equals(PARAM_MARKETPLACE, true) && !isReset) {
                uohBottomSheetOptionAdapter.selectedKey = PARAM_MARKETPLACE
            } else {
                uohBottomSheetOptionAdapter.selectedKey = currFilterCategoryKey
            }

            uohBottomSheetOptionAdapter.isReset = isReset
            uohBottomSheetOptionAdapter.notifyDataSetChanged()
        }
        if (filterStatus.equals(PARAM_SEMUA_TRANSAKSI, true) && !isReset) {
            filter3?.title = ALL_CATEGORIES

        } else if (filterStatus.equals(PARAM_MARKETPLACE, true) && !isReset) {
            filter3?.title = orderList.categories.first().label
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
            userSession?.userId?.let { it1 -> UohAnalytics.clickXChipsToClearFilter(it1) }
        }

        filter1?.refChipUnify?.setChevronClickListener {  }
        filter2?.refChipUnify?.setChevronClickListener {  }
        filter3?.refChipUnify?.setChevronClickListener {  }
    }

    private fun resetFilter() {
        filterStatus = ""

        currFilterDateKey = ""
        currFilterDateLabel = ""
        tempFilterDateKey = ""
        tempFilterDateLabel = ""

        currFilterStatusKey = ""
        currFilterStatusLabel = ""
        tempFilterStatusKey = ""
        tempFilterStatusLabel = ""

        currFilterCategoryKey = ""
        currFilterCategoryLabel = ""
        tempFilterCategoryKey = ""
        tempFilterCategoryLabel = ""

        isFilterClicked = false
        isReset = true
        uoh_sort_filter?.resetAllFilters()
        filter1?.title = ALL_DATE
        filter2?.title = ALL_STATUS
        filter3?.title = ALL_CATEGORIES
        paramUohOrder = UohListParam()
        setInitialValue()
    }

    /*private fun renderTicker() {
        ticker_info?.visible()

        if (orderList.tickers.size > 1) {
            val listTickerData = arrayListOf<TickerData>()
            orderList.tickers.forEach {
                var desc = it.text
                if (it.action.appUrl.isNotEmpty() && it.action.label.isNotEmpty()) {
                    desc += " ${getString(R.string.buyer_ticker_info_selengkapnya)
                            .replace(TICKER_URL, it.action.appUrl)
                            .replace(TICKER_LABEL, it.action.label)}"
                }
                listTickerData.add(TickerData(it.title, desc, UohUtils.getTickerType(it.text), true))
            }
            context?.let {
                val adapter = TickerPagerAdapter(it, listTickerData)
                adapter.setPagerDescriptionClickEvent(object : TickerPagerCallback {
                    override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
                        onTickerDetailInfoClicked(linkUrl.toString())
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
                            .replace(TICKER_URL, URLDecoder.decode(it.action.appUrl, UohConsts.UTF_8))
                            .replace(TICKER_LABEL, it.action.label)}"
                }
                ticker_info?.setHtmlDescription(desc)
                ticker_info?.tickerType = UohUtils.getTickerType(it.type)
                ticker_info?.setDescriptionClickEvent(object : TickerCallback {
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {
                        onTickerDetailInfoClicked(linkUrl.toString())
                    }

                    override fun onDismiss() {
                    }

                })
            }
        }
    }*/

    private fun renderOrderList() {
        refreshHandler?.finishRefresh()

        val listOrder = arrayListOf<UohTypeData>()

        if (!onLoadMore && orderList.tickers.isNotEmpty()) {
            listOrder.add(UohTypeData(orderList, UohConsts.TYPE_TICKER))
        }

        orderList.orders.forEach {
            listOrder.add(UohTypeData(it, UohConsts.TYPE_ORDER_LIST))
        }

        if (!onLoadMore) {
            uohItemAdapter.addList(listOrder)
        } else {
            uohItemAdapter.appendList(listOrder)
            scrollRecommendationListener.updateStateAfterGetData()
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

    private fun showBottomSheetFilterOptions(title: String) {
        val viewBottomSheet = View.inflate(context, R.layout.bottomsheet_option_uoh, null).apply {
            rv_option?.apply {
                layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
                adapter = uohBottomSheetOptionAdapter
            }

            btn_apply?.setOnClickListener {
                isReset = false
                currFilterType = tempFilterType
                when (currFilterType) {
                    UohConsts.TYPE_FILTER_DATE -> {
                        currFilterDateKey = tempFilterDateKey
                        currFilterDateLabel = tempFilterDateLabel
                        if (tempFilterDateKey != "0") {
                            filter1?.type = ChipsUnify.TYPE_SELECTED
                        } else {
                            filter1?.type = ChipsUnify.TYPE_NORMAL
                        }
                        var dateOption = ""
                        if (currFilterDateKey.toInt() == 3) {
                            if (paramUohOrder.createTimeStart.isNotEmpty() && paramUohOrder.createTimeEnd.isNotEmpty()) {
                                val splitStartDate = paramUohOrder.createTimeStart.split('-')
                                val splitEndDate = paramUohOrder.createTimeEnd.split('-')
                                dateOption = "${splitStartDate[2]}/${splitStartDate[1]}/${splitStartDate[0]} - ${splitEndDate[2]}/${splitEndDate[1]}/${splitEndDate[0]}"
                                filter1?.title = dateOption
                            }
                        } else {
                            dateOption = currFilterDateLabel

                            if (currFilterDateKey == "0") {
                                filter1?.title = ALL_DATE
                            } else {
                                filter1?.title = currFilterDateLabel
                            }
                        }
                        userSession?.userId?.let { it1 -> UohAnalytics.clickTerapkanOnDateFilterChips(dateOption, it1) }
                    }
                    UohConsts.TYPE_FILTER_STATUS -> {
                        currFilterStatusKey = tempFilterStatusKey
                        currFilterStatusLabel = tempFilterStatusLabel
                        if (tempFilterStatusKey != ALL_TRANSACTIONS) {
                            filter2?.type = ChipsUnify.TYPE_SELECTED
                            filter2?.title = currFilterStatusLabel
                        } else {
                            filter2?.type = ChipsUnify.TYPE_NORMAL
                            filter2?.title = ALL_STATUS
                        }
                        userSession?.userId?.let { it1 -> UohAnalytics.clickTerapkanOnStatusFilterChips(currFilterStatusLabel, it1) }
                    }
                    UohConsts.TYPE_FILTER_CATEGORY -> {
                        currFilterCategoryKey = tempFilterCategoryKey
                        currFilterCategoryLabel = tempFilterCategoryLabel
                        if (tempFilterCategoryKey != ALL_CATEGORIES) {
                            filter3?.type = ChipsUnify.TYPE_SELECTED
                            filter3?.title = currFilterCategoryLabel
                        } else {
                            filter3?.type = ChipsUnify.TYPE_NORMAL
                            filter3?.title = ALL_CATEGORIES
                        }
                        userSession?.userId?.let { it1 -> UohAnalytics.clickTerapkanOnCategoryFilterChips(currFilterCategoryLabel, it1) }
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

    private fun showBottomSheetFinishOrder(index: Int, orderId: String, isFromKebabMenu: Boolean) {
        val viewBottomSheet = View.inflate(context, R.layout.bottomsheet_finish_order_uoh, null).apply {

            btn_finish_order?.setOnClickListener {
                bottomSheetKebabMenu?.dismiss()
                bottomSheetFinishOrder?.dismiss()
                chosenOrder = uohItemAdapter.getDataAtIndex(index)
                uohItemAdapter.showLoaderAtIndex(index)
                currIndexNeedUpdate = index

                val paramFinishOrder = userSession?.userId?.let { it1 -> UohFinishOrderParam(orderId = orderId, userId = it1) }
                if (paramFinishOrder != null) {
                    uohListViewModel.doFinishOrder(GraphqlHelper.loadRawString(resources, R.raw.uoh_finish_order), paramFinishOrder)
                }

                userSession?.userId?.let { it1 -> UohAnalytics.clickSelesaiOnBottomSheetFinishTransaction(it1) }
            }

            btn_ajukan_komplain?.setOnClickListener {
                RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, URL_RESO.replace(REPLACE_ORDER_ID, orderId)))
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

    private fun showBottomSheetSendEmail(gqlGroup: String, orderData: UohListOrder.Data.UohOrders.Order) {
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
                val flightQueryParam = Gson().fromJson(orderData.metadata.queryParams, FlightQueryParams::class.java)
                val invoiceId = flightQueryParam.invoiceId
                val email = "${viewBottomSheet.tf_email.textFieldInput.text}"
                uohListViewModel.doFlightResendEmail(GraphqlHelper.loadRawString(resources, R.raw.uoh_send_eticket_flight), invoiceId, email)

            } else if (gqlGroup.equals(GQL_TRAIN_EMAIL, true)) {
                val trainQueryParam = Gson().fromJson(orderData.metadata.queryParams, TrainQueryParams::class.java)
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
        tempFilterType = filterType

        when (filterType) {
            UohConsts.TYPE_FILTER_DATE -> {
                tempFilterDateKey = option
                tempFilterDateLabel = label
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
                        val startDateStr = getCalculatedFormattedDate("dd MMM yyyy", -90)
                        val endDateStr = Date().toFormattedString("dd MMM yyyy")
                        bottomSheetOption?.apply {
                            cl_choose_date?.visible()

                            if (defaultStartDateStr.isNotEmpty()) {
                                tf_start_date?.textFieldInput?.setText(defaultStartDateStr)
                            } else {
                                tf_start_date?.textFieldInput?.setText(startDateStr)
                            }
                            tf_start_date?.textFieldInput?.isFocusable = false
                            tf_start_date?.textFieldInput?.isClickable = true
                            tf_start_date?.textFieldInput?.setOnClickListener { showDatePicker(START_DATE) }

                            if (defaultEndDateStr.isNotEmpty()) {
                                tf_end_date?.textFieldInput?.setText(defaultEndDateStr)
                            } else {
                                tf_end_date?.textFieldInput?.setText(endDateStr)
                            }
                            tf_end_date?.textFieldInput?.isFocusable = false
                            tf_end_date?.textFieldInput?.isClickable = true
                            tf_end_date?.textFieldInput?.setOnClickListener { showDatePicker(END_DATE) }
                        }
                    }
                }
                userSession?.userId?.let { UohAnalytics.clickDateFilterChips(it) }
            }
            UohConsts.TYPE_FILTER_STATUS -> {
                tempFilterStatusKey = option
                tempFilterStatusLabel = label
                paramUohOrder.status = option
                userSession?.userId?.let { UohAnalytics.clickStatusFilterChips(it) }
            }
            UohConsts.TYPE_FILTER_CATEGORY -> {
                tempFilterCategoryKey = option
                tempFilterCategoryLabel = label
                paramUohOrder.verticalCategory = option
                userSession?.userId?.let { UohAnalytics.clickCategoryFilterChips(it) }
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

            val splitDate = if (flag.equals(START_DATE, true)) {
                if (paramUohOrder.createTimeStart.isNotEmpty()) {
                    paramUohOrder.createTimeStart.split('-')
                } else {
                    val chooseStartDate = getCalculatedFormattedDate("yyyy-MM-dd", -90)
                    chooseStartDate.split('-')
                }
            } else {
                if (paramUohOrder.createTimeEnd.isNotEmpty()) {
                    paramUohOrder.createTimeEnd.split('-')
                } else {
                    val chooseEndDate = Date().toFormattedString("yyyy-MM-dd")
                    chooseEndDate.split('-')
                }
            }

            splitDate.let {
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

    private fun showToasterAtc(message: String, type: Int) {
        val toasterSuccess = Toaster
        view?.let { v ->
            toasterSuccess.make(v, message, Toaster.LENGTH_SHORT, type, CTA_ATC, View.OnClickListener {
                RouteManager.route(context, ApplinkConst.CART)
                userSession?.userId?.let { it1 -> UohAnalytics.clickLihatButtonOnAtcSuccessToaster(it1) }
            })
        }
    }

    override fun onKebabMenuClicked(order: UohListOrder.Data.UohOrders.Order) {
        showBottomSheetKebabMenu()
        uohBottomSheetKebabMenuAdapter.addList(order)
        userSession?.userId?.let { UohAnalytics.clickThreeDotsMenu(order.verticalCategory, it) }
    }

    override fun onKebabItemClick(index: Int, orderData: UohListOrder.Data.UohOrders.Order) {
        val dotMenu = orderData.metadata.dotMenus[index]
        if (dotMenu.actionType.equals(TYPE_ACTION_BUTTON_LINK, true)) {
            val linkUrl = if (dotMenu.appURL.contains(UohConsts.WEBVIEW)) {
                dotMenu.webURL
            } else {
                dotMenu.appURL
            }
            RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, URLDecoder.decode(linkUrl, UohConsts.UTF_8)))
        } else {
            when {
                dotMenu.actionType.equals(GQL_FLIGHT_EMAIL, true) -> {
                    showBottomSheetSendEmail(GQL_FLIGHT_EMAIL, orderData)
                }
                dotMenu.actionType.equals(GQL_TRAIN_EMAIL, true) -> {
                    showBottomSheetSendEmail(GQL_TRAIN_EMAIL, orderData)
                }
                dotMenu.actionType.equals(GQL_MP_CHAT, true) -> {
                    RouteManager.route(context, URLDecoder.decode(dotMenu.appURL, UohConsts.UTF_8))
                }
                dotMenu.actionType.equals(GQL_ATC, true) -> {
                    bottomSheetKebabMenu?.dismiss()

                    if (orderData.metadata.listProducts.isNotEmpty()) {
                        val listOfStrings = Gson().fromJson(orderData.metadata.listProducts, mutableListOf<String>().javaClass)
                        val jsonArray: JsonArray = Gson().toJsonTree(listOfStrings).asJsonArray
                        uohListViewModel.doAtc(GraphqlHelper.loadRawString(resources, R.raw.buy_again), jsonArray)

                        // analytics
                        val arrayListProducts = arrayListOf<ECommerceAdd.Add.Products>()
                        var i = 0
                        orderData.metadata.products.forEach {
                            val objProduct = jsonArray.get(i).asJsonObject
                            arrayListProducts.add(ECommerceAdd.Add.Products(
                                    name = it.title,
                                    id = objProduct.get(EE_PRODUCT_ID).asString,
                                    price = objProduct.get(EE_PRODUCT_PRICE).asString,
                                    quantity = objProduct.get(EE_QUANTITY).asString,
                                    dimension79 = objProduct.get(EE_SHOP_ID).asString
                            ))
                            i++
                        }
                        userSession?.userId?.let { UohAnalytics.clickBeliLagiOnOrderCardMP("", it, arrayListProducts) }
                    }
                }
                dotMenu.actionType.equals(GQL_MP_FINISH, true) -> {
                    orderIdNeedUpdated = orderData.orderUUID
                    showBottomSheetFinishOrder(index, orderData.verticalID, true)
                }
                dotMenu.actionType.equals(GQL_TRACK, true) -> {
                    val applinkTrack = ApplinkConst.ORDER_TRACKING.replace(REPLACE_ORDER_ID, orderData.verticalID)
                    RouteManager.route(context, applinkTrack)
                }
                dotMenu.actionType.equals(GQL_LS_LACAK, true) -> {
                    val linkUrl = LS_LACAK_MWEB.replace(REPLACE_ORDER_ID, orderData.verticalID)
                    RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, URLDecoder.decode(linkUrl, UohConsts.UTF_8)))
                }
            }
        }
        userSession?.userId?.let { UohAnalytics.clickSecondaryOptionOnThreeDotsMenu(orderData.verticalCategory, dotMenu.label, it) }
    }

    override fun onListItemClicked(order: UohListOrder.Data.UohOrders.Order, index: Int) {
        val detailUrl = order.metadata.detailURL
        if (detailUrl.appTypeLink == WEB_LINK_TYPE) {
            RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, URLDecoder.decode(detailUrl.appURL, UohConsts.UTF_8)))
        } else if (detailUrl.appTypeLink == APP_LINK_TYPE) {
            RouteManager.route(context, URLDecoder.decode(detailUrl.appURL, UohConsts.UTF_8))
        }

        // analytics
        var jsonArray = JsonArray()
        if (order.metadata.listProducts.isNotEmpty()) {
            val listOfStrings = Gson().fromJson(order.metadata.listProducts, mutableListOf<String>().javaClass)
            jsonArray = Gson().toJsonTree(listOfStrings).asJsonArray
        }
        val arrayListProducts = arrayListOf<ECommerceClick.Products>()
        var i = 0
        order.metadata.products.forEach {
            var eeProductId = ""
            var eeProductPrice = ""
            if (order.metadata.listProducts.isNotEmpty()) {
                val objProduct = jsonArray.get(i).asJsonObject
                eeProductId = objProduct.get(EE_PRODUCT_ID).asString
                eeProductPrice = objProduct.get(EE_PRODUCT_PRICE).asString
            }
            arrayListProducts.add(ECommerceClick.Products(
                    name = it.title,
                    id = eeProductId,
                    price = eeProductPrice,
                    list = "/order list - ${order.verticalCategory}",
                    position = index.toString()
            ))
            i++
        }
        userSession?.userId?.let { UohAnalytics.clickOrderCard(order.verticalCategory, it, arrayListProducts) }
    }

    override fun onActionButtonClicked(order: UohListOrder.Data.UohOrders.Order, index: Int) {
        val button = order.metadata.buttons.first()
        if (button.actionType.equals(TYPE_ACTION_BUTTON_LINK, true)) {
            RouteManager.route(context, URLDecoder.decode(button.appURL, UohConsts.UTF_8))
        } else {
            when {
                button.actionType.equals(GQL_FINISH_ORDER, true) -> {
                    orderIdNeedUpdated = order.orderUUID
                    showBottomSheetFinishOrder(index, order.verticalID, false)
                }
                button.actionType.equals(GQL_ATC, true) -> {
                    if (order.metadata.listProducts.isNotEmpty()) {
                        val listOfStrings = Gson().fromJson(order.metadata.listProducts, mutableListOf<String>().javaClass)
                        val jsonArray: JsonArray = Gson().toJsonTree(listOfStrings).asJsonArray
                        uohListViewModel.doAtc(GraphqlHelper.loadRawString(resources, R.raw.buy_again), jsonArray)
                    }
                }
                button.actionType.equals(GQL_TRACK, true) -> {
                    val applinkTrack = ApplinkConst.ORDER_TRACKING.replace(REPLACE_ORDER_ID, order.verticalID)
                    RouteManager.route(context, applinkTrack)
                }
                button.actionType.equals(GQL_LS_FINISH, true) -> {
                    orderIdNeedUpdated = order.orderUUID
                    showBottomSheetLsFinishOrder(index, order.verticalID)
                }
                button.actionType.equals(GQL_LS_LACAK, true) -> {
                    val linkUrl = LS_LACAK_MWEB.replace(REPLACE_ORDER_ID, order.verticalID)
                    RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, URLDecoder.decode(linkUrl, UohConsts.UTF_8)))
                }
                button.actionType.equals(GQL_RECHARGE_BATALKAN, true) -> {
                    currIndexNeedUpdate = index
                    orderIdNeedUpdated = order.orderUUID
                    uohListViewModel.doRechargeSetFail(GraphqlHelper.loadRawString(resources, R.raw.recharge_set_fail), order.verticalID.toInt())
                }
            }
        }

        userSession?.userId?.let { UohAnalytics.clickPrimaryButtonOnOrderCard(order.verticalCategory, button.label, it) }
    }

    override fun onEmptyResultResetBtnClicked() {
        resetFilter()
        refreshHandler?.startRefresh()
        userSession?.userId?.let { UohAnalytics.clickResetFilterOnEmptyFilterResult(it) }
    }

    override fun onTickerDetailInfoClicked(url: String) {
        if (url.contains(ApplinkConst.WEBVIEW)) {
            RouteManager.route(context, url)
        } else {
            RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, url))
        }
    }

    override fun trackViewOrderCard(order: UohListOrder.Data.UohOrders.Order, index: Int) {
        var jsonArray = JsonArray()
        if (order.metadata.listProducts.isNotEmpty()) {
            val listOfStrings = Gson().fromJson(order.metadata.listProducts, mutableListOf<String>().javaClass)
            jsonArray = Gson().toJsonTree(listOfStrings).asJsonArray
        }
        val arrayListProducts = arrayListOf<ECommerceImpressions.Impressions>()
        var i = 0
        order.metadata.products.forEach {
            var eeProductId = ""
            var eeProductPrice = ""
            if (order.metadata.listProducts.isNotEmpty()) {
                val objProduct = jsonArray.get(i).asJsonObject
                eeProductId = objProduct.get(EE_PRODUCT_ID).asString
                eeProductPrice = objProduct.get(EE_PRODUCT_PRICE).asString
            }

            arrayListProducts.add(ECommerceImpressions.Impressions(
                    name = it.title,
                    id = eeProductId,
                    price = eeProductPrice,
                    list = "/order list - ${order.verticalCategory}",
                    position = index.toString()
            ))
            i++
        }
        UohAnalytics.viewOrderCard(order.verticalCategory, order.userID, arrayListProducts)
    }

    override fun trackMulaiBelanjaOnEmptyList() {
        userSession?.userId?.let { UohAnalytics.clickMulaiBelanjaOnEmptyOrderList(it) }
    }

    override fun trackProductViewRecommendation(recommendationItem: RecommendationItem, index: Int) {
        val productId = recommendationItem.productId.toString()
        val topAds = recommendationItem.isTopAds
        val url = recommendationItem.trackerImageUrl
        val productName = recommendationItem.name
        val imageUrl = recommendationItem.imageUrl

        var list = UohConsts.RECOMMENDATION_LIST_TRACK
        if (topAds) {
            list += UohConsts.RECOMMENDATION_LIST_TOPADS_TRACK
            activity?.let { TopAdsUrlHitter(it).hitImpressionUrl(UohListFragment::class.qualifiedName, url, productId, productName, imageUrl) }
        }

        UohAnalytics.productViewRecommendation(ECommerceImpressions.Impressions(
                name = productName,
                id = recommendationItem.productId.toString(),
                price = recommendationItem.price,
                category = recommendationItem.categoryBreadcrumbs,
                position = index.toString(),
                list = list
        ))
    }

    override fun trackProductClickRecommendation(recommendationItem: RecommendationItem, index: Int) {
        val productId = recommendationItem.productId.toString()
        val topAds = recommendationItem.isTopAds
        val clickUrl = recommendationItem.clickUrl
        val productName = recommendationItem.name
        val imageUrl = recommendationItem.imageUrl

        UohAnalytics.productClickRecommendation(ECommerceClick.Products(
                name = productName,
                id = recommendationItem.productId.toString(),
                price = recommendationItem.price,
                category = recommendationItem.categoryBreadcrumbs,
                position = index.toString()), topAds)

        if (topAds) activity?.let { TopAdsUrlHitter(it).hitClickUrl(UohListFragment::class.qualifiedName, clickUrl, productId, productName, imageUrl) }
        onProductClicked(productId)
    }

    private fun onProductClicked(productId: String) {
        activity?.let {
            val intent = RouteManager.getIntent(it, ApplinkConst.PRODUCT_INFO, productId)
            startActivity(intent)
        }
    }

    override fun trackAddToCartRecommendation(recommendationItem: RecommendationItem) {
        val product = ECommerceAddRecommendation.Add.ActionField.Product(
                name = recommendationItem.name,
                id = recommendationItem.productId.toString(),
                price = recommendationItem.price,
                category = recommendationItem.categoryBreadcrumbs,
                quantity = recommendationItem.quantity.toString(),
                dimension45 = recommendationItem.cartId

        )
        val arrayListProduct = arrayListOf<ECommerceAddRecommendation.Add.ActionField.Product>()
        arrayListProduct.add(product)

        UohAnalytics.productAtcRecommendation(listProduct = arrayListProduct, isTopads = recommendationItem.isTopAds)
    }
}