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
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.view.RefreshHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.PARAM_DALAM_PROSES
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.PARAM_DEALS
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.PARAM_DIGITAL
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.PARAM_EVENTS
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.PARAM_E_TIKET
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.PARAM_GIFTCARDS
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.PARAM_HOTEL
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.PARAM_INSURANCE
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.PARAM_MARKETPLACE
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.PARAM_MARKETPLACE_DALAM_PROSES
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.PARAM_MODALTOKO
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.PARAM_PESAWAT
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.PARAM_SEMUA_TRANSAKSI
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.SOURCE_FILTER
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.request.AddToCartMultiParam
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.unifiedhistory.common.di.UohComponentInstance
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.ACTION_FINISH_ORDER
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.ALL_CATEGORIES
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.ALL_CATEGORIES_TRANSACTION
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.ALL_DATE
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.ALL_STATUS
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.APPLINK_BASE
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.APP_LINK_TYPE
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.CTA_ATC
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.CUSTOMER_ID
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.DALAM_PROSES
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.EE_PRODUCT_ID
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.EE_PRODUCT_PRICE
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.EE_QUANTITY
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.EE_SHOP_ID
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.EMAIL_MUST_NOT_BE_EMPTY
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.END_DATE
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.E_TIKET
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
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.NOTES
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.PRODUCT_ID
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.PRODUCT_NAME
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.PRODUCT_PRICE
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.QUANTITY
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.QUERY_PARAM_INVOICE
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.QUERY_PARAM_INVOICE_URL
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.REPLACE_ORDER_ID
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.SEMUA_TRANSAKSI
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.SHOP_ID
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.START_DATE
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.TYPE_ACTION_BUTTON_LINK
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.URL_RESO
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.VERTICAL_CATEGORY_DEALS
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.VERTICAL_CATEGORY_DEALS_SINGLE
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.VERTICAL_CATEGORY_DIGITAL
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.VERTICAL_CATEGORY_EVENTS
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.VERTICAL_CATEGORY_FLIGHT
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.VERTICAL_CATEGORY_GIFTCARD
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.VERTICAL_CATEGORY_HOTEL
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.VERTICAL_CATEGORY_INSURANCE
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.VERTICAL_CATEGORY_KEUANGAN
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.VERTICAL_CATEGORY_MODALTOKO
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.WAREHOUSE_ID
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
import com.tokopedia.design.utils.StringUtils
import com.tokopedia.kotlin.extensions.convertMonth
import com.tokopedia.kotlin.extensions.getCalculatedFormattedDate
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.toIntOrZero
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
    private var isFirstLoad = false
    private var gson = Gson()

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
        isFirstLoad = true
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
                    PARAM_DIGITAL -> {
                        status = SEMUA_TRANSAKSI
                        paramUohOrder.verticalCategory = VERTICAL_CATEGORY_DIGITAL
                    }
                    PARAM_EVENTS -> {
                        status = SEMUA_TRANSAKSI
                        paramUohOrder.verticalCategory = VERTICAL_CATEGORY_EVENTS
                    }
                    PARAM_DEALS -> {
                        status = SEMUA_TRANSAKSI
                        paramUohOrder.verticalCategory = VERTICAL_CATEGORY_DEALS_SINGLE
                    }
                    PARAM_PESAWAT -> {
                        status = SEMUA_TRANSAKSI
                        paramUohOrder.verticalCategory = VERTICAL_CATEGORY_FLIGHT
                    }
                    PARAM_GIFTCARDS -> {
                        status = SEMUA_TRANSAKSI
                        paramUohOrder.verticalCategory = VERTICAL_CATEGORY_GIFTCARD
                    }
                    PARAM_INSURANCE -> {
                        status = SEMUA_TRANSAKSI
                        paramUohOrder.verticalCategory = VERTICAL_CATEGORY_INSURANCE
                    }
                    PARAM_MODALTOKO -> {
                        status = SEMUA_TRANSAKSI
                        paramUohOrder.verticalCategory = VERTICAL_CATEGORY_MODALTOKO
                    }
                    PARAM_HOTEL -> {
                        status = SEMUA_TRANSAKSI
                        paramUohOrder.verticalCategory = VERTICAL_CATEGORY_HOTEL
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
        isFetchRecommendation = false
        onLoadMoreRecommendation = false
        currPage = 1
        currRecommendationListPage = 1
        uohItemAdapter.showLoader()
        loadOrderHistoryList("")
    }

    private fun setInitialValue() {
        paramUohOrder.page = 1
        arrayFilterDate = resources.getStringArray(R.array.filter_date)
    }

    private fun observingData() {
        observingOrderHistory()
        observingRecommendationList()
        observingFinishOrder()
        observingAtcMulti()
        observingLsFinishOrder()
        observingFlightResendEmail()
        observingTrainResendEmail()
        observingRechargeSetFail()
        observingAtc()
    }

    private fun prepareLayout() {
        refreshHandler = RefreshHandler(swipe_refresh_layout, this)
        refreshHandler?.setPullEnabled(true)

        uohItemAdapter = UohItemAdapter().apply {
            setActionListener(this@UohListFragment)
        }

        uohBottomSheetKebabMenuAdapter = UohBottomSheetKebabMenuAdapter(this)

        search_bar?.searchBarTextField?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                textChangedJob?.cancel()
                textChangedJob = GlobalScope.launch(Dispatchers.Main) {
                    delay(500L)

                    if (!isTyping) {
                        isTyping = true
                        resetFilter()
                    }

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

        search_bar?.searchBarIcon?.setOnClickListener {
            view?.let { context?.let { it1 -> UohUtils.hideKeyBoard(it1, it) } }
            search_bar?.searchBarTextField?.text?.clear()
        }

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
                    context?.getString(R.string.fail_cancellation)?.let { it1 -> showToaster(it1, Toaster.TYPE_ERROR) }
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

    private fun observingAtcMulti() {
        uohListViewModel.atcMultiResult.observe(this, androidx.lifecycle.Observer {
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

    private fun observingAtc() {
        uohListViewModel.atcResult.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is Success -> {
                    val msg = StringUtils.convertListToStringDelimiter(it.data.data.message, ",")
                    if (it.data.data.success == 1) {
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

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    @SuppressLint("SimpleDateFormat")
    private fun renderChipsFilter() {
        val chips = arrayListOf<SortFilterItem>()

        val typeDate = if (isReset || isFirstLoad) {
            ChipsUnify.TYPE_NORMAL
        } else {
            ChipsUnify.TYPE_SELECTED
        }

        filter1 = SortFilterItem(ALL_DATE, typeDate, ChipsUnify.SIZE_SMALL)
        filter1?.listener = {
            onClickFilterDate()
        }
        filter1?.let {
            chips.add(it)
        }

        val typeStatus = if (filterStatus.isEmpty() || filterStatus.equals(PARAM_SEMUA_TRANSAKSI, true)
                || filterStatus.equals(PARAM_MARKETPLACE, true)
                || filterStatus.equals(PARAM_DIGITAL, true)
                || filterStatus.equals(PARAM_EVENTS, true)
                || filterStatus.equals(PARAM_DEALS, true)
                || filterStatus.equals(PARAM_PESAWAT, true)
                || filterStatus.equals(PARAM_GIFTCARDS, true)
                || filterStatus.equals(PARAM_INSURANCE, true)
                || filterStatus.equals(PARAM_MODALTOKO, true)
                || filterStatus.equals(PARAM_HOTEL, true)) {
            ChipsUnify.TYPE_NORMAL
        } else {
            ChipsUnify.TYPE_SELECTED
        }
        filter2 = SortFilterItem(ALL_STATUS, typeStatus, ChipsUnify.SIZE_SMALL)
        filter2?.listener = {
            onClickFilterStatus()
        }
        if (filterStatus.isNotEmpty() && !isReset) {
            if (filterStatus.equals(PARAM_SEMUA_TRANSAKSI, true)) {
                filter2?.title = ALL_STATUS
            } else {
                filter2?.title = currFilterStatusLabel
            }
        }
        filter2?.let { chips.add(it) }

        val typeCategory = if (filterStatus.equals(PARAM_MARKETPLACE, true) ||
                filterStatus.equals(PARAM_MARKETPLACE_DALAM_PROSES, true) ||
                filterStatus.equals(PARAM_DIGITAL, true) ||
                filterStatus.equals(PARAM_EVENTS, true) ||
                filterStatus.equals(PARAM_DEALS, true) ||
                filterStatus.equals(PARAM_PESAWAT, true) ||
                filterStatus.equals(PARAM_GIFTCARDS, true) ||
                filterStatus.equals(PARAM_INSURANCE, true) ||
                filterStatus.equals(PARAM_MODALTOKO, true) ||
                filterStatus.equals(PARAM_HOTEL, true)) {
            ChipsUnify.TYPE_SELECTED
        } else {
            ChipsUnify.TYPE_NORMAL
        }

        filter3 = SortFilterItem(ALL_CATEGORIES, typeCategory, ChipsUnify.SIZE_SMALL)
        filter3?.listener = {
            onClickFilterCategory()
        }
        if (filterStatus.equals(PARAM_SEMUA_TRANSAKSI, true) && !isReset) {
            filter3?.title = ALL_CATEGORIES

        } else if ((filterStatus.equals(PARAM_MARKETPLACE, true) ||
                        filterStatus.equals(PARAM_MARKETPLACE_DALAM_PROSES, true)) && !isReset) {
            filter3?.title = orderList.categories.first().label

        } else if (filterStatus.equals(PARAM_DIGITAL, true) && !isReset) {
            filter3?.title = orderList.categories[1].label

        } else if ((filterStatus.equals(PARAM_EVENTS, true) || filterStatus.equals(PARAM_DEALS, true) ||
                filterStatus.equals(PARAM_PESAWAT, true) || filterStatus.equals(PARAM_HOTEL, true))
                && !isReset) {
            filter3?.title = orderList.categories[2].label

        } else if ((filterStatus.equals(PARAM_GIFTCARDS, true) || filterStatus.equals(PARAM_INSURANCE, true) ||
                filterStatus.equals(PARAM_MODALTOKO, true)) && !isReset) {
            filter3?.title = orderList.categories[3].label
        }
        filter3?.let { chips.add(it) }

        uoh_sort_filter?.addItem(chips)
        uoh_sort_filter?.sortFilterPrefix?.setOnClickListener {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd")
            val outputFormat = SimpleDateFormat("d MMM yyyy")
            val limitDate = inputFormat.parse(orderList.dateLimit)
            val limitDateStr = outputFormat.format(limitDate)
            view?.let { context?.let { it1 -> UohUtils.hideKeyBoard(it1, it) } }
            val resetMsg = resources.getString(R.string.uoh_reset_filter_msg).replace(UohConsts.DATE_LIMIT, limitDateStr)
            showToaster(resetMsg, Toaster.TYPE_NORMAL)
            resetFilter()
            refreshHandler?.startRefresh()
            userSession?.userId?.let { it1 -> UohAnalytics.clickXChipsToClearFilter(it1) }
        }

        filter1?.refChipUnify?.setChevronClickListener { onClickFilterDate() }
        filter2?.refChipUnify?.setChevronClickListener { onClickFilterStatus() }
        filter3?.refChipUnify?.setChevronClickListener { onClickFilterCategory() }
    }

    private fun onClickFilterDate() {
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
        tempFilterType = UohConsts.TYPE_FILTER_DATE
        if (tempFilterDateLabel.isEmpty()) tempFilterDateLabel = ALL_DATE
        if (tempFilterDateKey.isEmpty()) tempFilterDateKey = "0"

        uohBottomSheetOptionAdapter.uohItemMapKeyList = arrayListMap
        uohBottomSheetOptionAdapter.filterType = UohConsts.TYPE_FILTER_DATE
        uohBottomSheetOptionAdapter.selectedKey = currFilterDateKey
        uohBottomSheetOptionAdapter.isReset = isReset
        uohBottomSheetOptionAdapter.notifyDataSetChanged()
    }

    private fun onClickFilterStatus() {
        uohBottomSheetOptionAdapter = UohBottomSheetOptionAdapter(this)
        showBottomSheetFilterOptions(UohConsts.CHOOSE_FILTERS)
        val arrayListMap = arrayListOf<HashMap<String, String>>()
        orderList.filters.forEach { option ->
            val mapKey = HashMap<String, String>()
            mapKey[option] = option
            arrayListMap.add(mapKey)
        }
        tempFilterType = UohConsts.TYPE_FILTER_STATUS
        if (tempFilterStatusLabel.isEmpty()) tempFilterStatusLabel = SEMUA_TRANSAKSI
        if (tempFilterStatusKey.isEmpty()) tempFilterStatusKey = SEMUA_TRANSAKSI

        uohBottomSheetOptionAdapter.uohItemMapKeyList = arrayListMap
        uohBottomSheetOptionAdapter.filterType = UohConsts.TYPE_FILTER_STATUS
        uohBottomSheetOptionAdapter.selectedKey = currFilterStatusKey
        uohBottomSheetOptionAdapter.isReset = isReset
        uohBottomSheetOptionAdapter.notifyDataSetChanged()
    }

    private fun onClickFilterCategory() {
        uohBottomSheetOptionAdapter = UohBottomSheetOptionAdapter(this)
        showBottomSheetFilterOptions(UohConsts.CHOOSE_CATEGORIES)
        val arrayListMap = arrayListOf<HashMap<String, String>>()
        val mapKeyDefault = HashMap<String, String>()
        mapKeyDefault[ALL_CATEGORIES] = ALL_CATEGORIES_TRANSACTION
        arrayListMap.add(mapKeyDefault)
        orderList.categories.forEach { category ->
            val mapKey = HashMap<String, String>()
            mapKey[category.value] = category.label
            arrayListMap.add(mapKey)
        }
        uohBottomSheetOptionAdapter.uohItemMapKeyList = arrayListMap
        uohBottomSheetOptionAdapter.filterType = UohConsts.TYPE_FILTER_CATEGORY
        tempFilterType = UohConsts.TYPE_FILTER_CATEGORY
        if (tempFilterCategoryLabel.isEmpty()) tempFilterCategoryLabel = ALL_CATEGORIES_TRANSACTION
        if (tempFilterCategoryKey.isEmpty()) tempFilterCategoryKey = ALL_CATEGORIES

        if ((filterStatus.equals(PARAM_MARKETPLACE, true) ||
                        filterStatus.equals(PARAM_MARKETPLACE_DALAM_PROSES, true)) && !isReset) {
            uohBottomSheetOptionAdapter.selectedKey = PARAM_MARKETPLACE

        } else if (filterStatus.equals(PARAM_DIGITAL, true) && !isReset) {
            uohBottomSheetOptionAdapter.selectedKey = VERTICAL_CATEGORY_DIGITAL

        } else if (filterStatus.equals(PARAM_EVENTS, true) && !isReset) {
            uohBottomSheetOptionAdapter.selectedKey = VERTICAL_CATEGORY_DEALS

        } else if (filterStatus.equals(PARAM_DEALS, true) && !isReset) {
            uohBottomSheetOptionAdapter.selectedKey = VERTICAL_CATEGORY_DEALS

        } else if (filterStatus.equals(PARAM_PESAWAT, true) && !isReset) {
            uohBottomSheetOptionAdapter.selectedKey = VERTICAL_CATEGORY_DEALS

        } else if (filterStatus.equals(PARAM_GIFTCARDS, true) && !isReset) {
            uohBottomSheetOptionAdapter.selectedKey = VERTICAL_CATEGORY_KEUANGAN

        } else if (filterStatus.equals(PARAM_INSURANCE, true) && !isReset) {
            uohBottomSheetOptionAdapter.selectedKey = VERTICAL_CATEGORY_KEUANGAN

        } else if (filterStatus.equals(PARAM_MODALTOKO, true) && !isReset) {
            uohBottomSheetOptionAdapter.selectedKey = VERTICAL_CATEGORY_KEUANGAN

        } else if (filterStatus.equals(PARAM_HOTEL, true) && !isReset) {
            uohBottomSheetOptionAdapter.selectedKey = VERTICAL_CATEGORY_DEALS

        } else {
            uohBottomSheetOptionAdapter.selectedKey = currFilterCategoryKey
        }
        uohBottomSheetOptionAdapter.isReset = isReset
        uohBottomSheetOptionAdapter.notifyDataSetChanged()
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
        val searchBarIsNotEmpty = search_bar?.searchBarTextField?.text?.isNotEmpty() ?: false

        val emptyStatus: UohEmptyState?
        when {
            searchBarIsNotEmpty -> {
                emptyStatus = context?.let { context ->
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

        if (recommendationList.isNotEmpty()) {
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
                isFilterClicked = true
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
                        val dateOption: String
                        val labelTrackingDate: String
                        if (currFilterDateKey.isNotEmpty() && currFilterDateKey == "3") {
                            if (paramUohOrder.createTimeStart.isEmpty()) {
                                paramUohOrder.createTimeStart = orderList.dateLimit
                            }
                            if (paramUohOrder.createTimeEnd.isEmpty()) {
                                paramUohOrder.createTimeEnd = Date().toFormattedString("yyyy-MM-dd")
                            }
                            val splitStartDate = paramUohOrder.createTimeStart.split('-')
                            val splitEndDate = paramUohOrder.createTimeEnd.split('-')
                            dateOption = "${splitStartDate[2]}/${splitStartDate[1]}/${splitStartDate[0]} - ${splitEndDate[2]}/${splitEndDate[1]}/${splitEndDate[0]}"
                            filter1?.title = dateOption
                            labelTrackingDate = getString(R.string.tkpdtransaction_filter_custom_date)
                        } else {
                            dateOption = currFilterDateLabel

                            if (currFilterDateKey == "0") {
                                filter1?.title = ALL_DATE
                                labelTrackingDate = ALL_DATE
                            } else {
                                filter1?.title = currFilterDateLabel
                                labelTrackingDate = dateOption
                            }
                        }

                        userSession?.userId?.let { it1 -> UohAnalytics.clickTerapkanOnDateFilterChips(labelTrackingDate, it1) }
                    }
                    UohConsts.TYPE_FILTER_STATUS -> {
                        val labelTrackingStatus: String
                        currFilterStatusKey = tempFilterStatusKey
                        currFilterStatusLabel = tempFilterStatusLabel
                        if (tempFilterStatusKey != SEMUA_TRANSAKSI) {
                            filter2?.type = ChipsUnify.TYPE_SELECTED
                            filter2?.title = currFilterStatusLabel
                            labelTrackingStatus = currFilterStatusLabel
                        } else {
                            filter2?.type = ChipsUnify.TYPE_NORMAL
                            filter2?.title = ALL_STATUS
                            labelTrackingStatus = ALL_STATUS
                        }
                        userSession?.userId?.let { it1 -> UohAnalytics.clickTerapkanOnStatusFilterChips(labelTrackingStatus, it1) }
                    }
                    UohConsts.TYPE_FILTER_CATEGORY -> {
                        val labelTrackingCategory: String
                        currFilterCategoryKey = tempFilterCategoryKey
                        currFilterCategoryLabel = tempFilterCategoryLabel
                        if (tempFilterCategoryKey != ALL_CATEGORIES) {
                            filter3?.type = ChipsUnify.TYPE_SELECTED
                            filter3?.title = currFilterCategoryLabel
                            labelTrackingCategory = currFilterCategoryLabel
                        } else {
                            filter3?.type = ChipsUnify.TYPE_NORMAL
                            filter3?.title = ALL_CATEGORIES
                            labelTrackingCategory = ALL_CATEGORIES
                        }
                        userSession?.userId?.let { it1 -> UohAnalytics.clickTerapkanOnCategoryFilterChips(labelTrackingCategory, it1) }
                    }
                }
                bottomSheetOption?.dismiss()
                isFirstLoad = false
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

    private fun showBottomSheetKebabMenu(orderIndex: Int) {
        val viewBottomSheet = View.inflate(context, R.layout.bottomsheet_kebab_menu_uoh, null)
        uohBottomSheetKebabMenuAdapter._orderIndex = orderIndex
        viewBottomSheet.rv_kebab.adapter = uohBottomSheetKebabMenuAdapter
        viewBottomSheet.rv_kebab.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)

        bottomSheetKebabMenu = BottomSheetUnify().apply {
            setChild(viewBottomSheet)
            setTitle(UohConsts.OTHERS)
            setCloseClickListener { dismiss() }
        }

        fragmentManager?.let { bottomSheetKebabMenu?.show(it, getString(R.string.show_bottomsheet)) }
    }

    private fun showBottomSheetFinishOrder(index: Int, orderId: String, isFromKebabMenu: Boolean, status: String) {
        val viewBottomSheet = View.inflate(context, R.layout.bottomsheet_finish_order_uoh, null).apply {

            btn_finish_order?.setOnClickListener {
                bottomSheetKebabMenu?.dismiss()
                bottomSheetFinishOrder?.dismiss()
                chosenOrder = uohItemAdapter.getDataAtIndex(index)
                uohItemAdapter.showLoaderAtIndex(index)
                currIndexNeedUpdate = index

                var actionStatus = ""
                if (status.isNotEmpty() && status.toIntOrZero() < 600) actionStatus = ACTION_FINISH_ORDER

                val paramFinishOrder = userSession?.userId?.let { it1 ->
                    UohFinishOrderParam(orderId = orderId, userId = it1, action = actionStatus)
                }
                if (paramFinishOrder != null) {
                    uohListViewModel.doFinishOrder(GraphqlHelper.loadRawString(resources, R.raw.uoh_finish_order), paramFinishOrder)
                }

                userSession?.userId?.let { it1 -> UohAnalytics.clickSelesaiOnBottomSheetFinishTransaction(it1) }
            }

            btn_ajukan_komplain?.setOnClickListener {
                RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, URL_RESO.replace(REPLACE_ORDER_ID, orderId)))
                userSession?.userId?.let { it1 -> UohAnalytics.clickAjukanKomplainOnBottomSheetFinishTransaction(it1) }
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
        viewBottomSheet.tf_email?.textFieldInput?.addTextChangedListener(object : TextWatcher {
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
                val flightQueryParam = gson.fromJson(orderData.metadata.queryParams, FlightQueryParams::class.java)
                val invoiceId = flightQueryParam.invoiceId
                val email = "${viewBottomSheet.tf_email.textFieldInput.text}"
                uohListViewModel.doFlightResendEmail(GraphqlHelper.loadRawString(resources, R.raw.uoh_send_eticket_flight), invoiceId, email)

            } else if (gqlGroup.equals(GQL_TRAIN_EMAIL, true)) {
                val trainQueryParam = gson.fromJson(orderData.metadata.queryParams, TrainQueryParams::class.java)
                val invoiceId = trainQueryParam.invoiceId
                val email = "${viewBottomSheet.tf_email.textFieldInput.text}"
                val param = TrainResendEmailParam(bookCode = invoiceId, email = email)
                uohListViewModel.doTrainResendEmail(GraphqlHelper.loadRawString(resources, R.raw.uoh_send_eticket_train), param)
            }
            userSession?.userId?.let { it1 -> UohAnalytics.clickKirimOnBottomSheetSendEmail(it1, orderData.verticalCategory) }
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

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    @SuppressLint("SimpleDateFormat")
    override fun onOptionItemClick(option: String, label: String, filterType: Int) {
        isFilterClicked = true
        tempFilterType = filterType

        when (filterType) {
            UohConsts.TYPE_FILTER_DATE -> {
                tempFilterDateKey = option
                tempFilterDateLabel = label
                if (option.isNotEmpty()) {
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
                            val endDate = Date().toFormattedString("yyyy-MM-dd")
                            paramUohOrder.createTimeStart = startDate
                            paramUohOrder.createTimeEnd = endDate

                        }
                        option.toInt() == 2 -> {
                            bottomSheetOption?.apply {
                                cl_choose_date?.gone()
                            }
                            val endDate = Date().toFormattedString("yyyy-MM-dd")
                            paramUohOrder.createTimeStart = orderList.dateLimit
                            paramUohOrder.createTimeEnd = endDate

                        }
                        option.toInt() == 3 -> {
                            val inputFormat = SimpleDateFormat("yyyy-MM-dd")
                            val outputFormat = SimpleDateFormat("d MMM yyyy")
                            val startDateStrInput = inputFormat.parse(orderList.dateLimit)
                            val startDateStr = outputFormat.format(startDateStrInput)
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
                if (tempFilterCategoryKey == ALL_CATEGORIES) {
                    paramUohOrder.verticalCategory = ""
                } else {
                    paramUohOrder.verticalCategory = option
                }
                userSession?.userId?.let { UohAnalytics.clickCategoryFilterChips(it) }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showDatePicker(flag: String) {
        context?.let { context ->
            val minDate = Calendar.getInstance()

            val resultMinDate = orderList.dateLimit.split('-')
            if (resultMinDate.isNotEmpty()) {
                minDate.set(Calendar.YEAR, resultMinDate[0].toInt())
                minDate.set(Calendar.MONTH, resultMinDate[1].toInt())
                minDate.set(Calendar.DATE, resultMinDate[2].toInt())
            }
            val maxDate = Calendar.getInstance()
            val isEndDateFilled = paramUohOrder.createTimeEnd.isNotEmpty()
            if (isEndDateFilled && flag.equals(START_DATE, true)) {
                val splitEndDate = paramUohOrder.createTimeEnd.split('-')
                if (splitEndDate.isNotEmpty()) {
                    maxDate.set(splitEndDate[0].toInt(), splitEndDate[1].toInt() - 1, splitEndDate[2].toInt())
                }
            }
            val isStartDateFilled = paramUohOrder.createTimeStart.isNotEmpty()
            if (isStartDateFilled && flag.equals(END_DATE, true)) {
                val splitStartDate = paramUohOrder.createTimeStart.split('-')
                if (splitStartDate.isNotEmpty()) {
                    minDate.set(splitStartDate[0].toInt(), splitStartDate[1].toInt() - 1, splitStartDate[2].toInt())
                }
            }

            val currentDate = Calendar.getInstance()

            val splitDate = if (flag.equals(START_DATE, true)) {
                if (paramUohOrder.createTimeStart.isNotEmpty()) {
                    paramUohOrder.createTimeStart.split('-')
                } else {
                    val chooseStartDate = orderList.dateLimit
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

            if (splitDate.isNotEmpty()) {
                splitDate.let {
                    currentDate.set(it[0].toInt(), it[1].toInt() - 1, it[2].toInt())
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

    override fun onKebabMenuClicked(order: UohListOrder.Data.UohOrders.Order, orderIndex: Int) {
        showBottomSheetKebabMenu(orderIndex)
        uohBottomSheetKebabMenuAdapter.addList(order)
        userSession?.userId?.let { UohAnalytics.clickThreeDotsMenu(order.verticalCategory, it) }
    }

    override fun onKebabItemClick(index: Int, orderData: UohListOrder.Data.UohOrders.Order, orderIndex: Int) {
        val dotMenu = orderData.metadata.dotMenus[index]
        if (dotMenu.actionType.equals(TYPE_ACTION_BUTTON_LINK, true)) {
            if (dotMenu.appURL.contains(APPLINK_BASE)) {
                RouteManager.route(context, URLDecoder.decode(dotMenu.appURL, UohConsts.UTF_8))
            } else {
                val linkUrl = if (dotMenu.appURL.contains(UohConsts.WEBVIEW)) {
                    dotMenu.webURL
                } else {
                    dotMenu.appURL
                }
                RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, URLDecoder.decode(linkUrl, UohConsts.UTF_8)))
            }
        } else {
            when {
                dotMenu.actionType.equals(GQL_FLIGHT_EMAIL, true) -> {
                    showBottomSheetSendEmail(GQL_FLIGHT_EMAIL, orderData)
                }
                dotMenu.actionType.equals(GQL_TRAIN_EMAIL, true) -> {
                    showBottomSheetSendEmail(GQL_TRAIN_EMAIL, orderData)
                }
                dotMenu.actionType.equals(GQL_MP_CHAT, true) -> {
                    doChatSeller(dotMenu.appURL, orderData)
                }
                dotMenu.actionType.equals(GQL_ATC, true) -> {
                    bottomSheetKebabMenu?.dismiss()
                    atc(orderData)
                }
                dotMenu.actionType.equals(GQL_MP_FINISH, true) -> {
                    orderIdNeedUpdated = orderData.orderUUID
                    showBottomSheetFinishOrder(orderIndex, orderData.verticalID, true, orderData.verticalStatus)
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
            val listOfStrings = gson.fromJson(order.metadata.listProducts, mutableListOf<String>().javaClass)
            jsonArray = gson.toJsonTree(listOfStrings).asJsonArray
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
                    showBottomSheetFinishOrder(index, order.verticalID, false, order.verticalStatus)
                }
                button.actionType.equals(GQL_ATC, true) -> {
                    atc(order)
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
                    if (order.verticalID.isNotEmpty()) {
                        uohListViewModel.doRechargeSetFail(GraphqlHelper.loadRawString(resources, R.raw.recharge_set_fail), order.verticalID.toInt())
                    }
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
        if (url.contains(APPLINK_BASE)){
            RouteManager.route(context, url)
        } else {
            if (url.contains(ApplinkConst.WEBVIEW)) {
                RouteManager.route(context, url)
            } else {
                RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, url))
            }
        }
    }

    override fun trackViewOrderCard(order: UohListOrder.Data.UohOrders.Order, index: Int) {
        var jsonArray = JsonArray()
        if (order.metadata.listProducts.isNotEmpty()) {
            val listOfStrings = gson.fromJson(order.metadata.listProducts, mutableListOf<String>().javaClass)
            jsonArray = gson.toJsonTree(listOfStrings).asJsonArray
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

    override fun onMulaiBelanjaBtnClicked() {
        RouteManager.route(context, ApplinkConst.HOME)
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

        userSession?.userId?.let {
            UohAnalytics.productViewRecommendation(it, ECommerceImpressions.Impressions(
                name = productName,
                id = recommendationItem.productId.toString(),
                price = recommendationItem.price,
                category = recommendationItem.categoryBreadcrumbs,
                position = index.toString(),
                list = list
        ), topAds)
        }
    }

    override fun trackProductClickRecommendation(recommendationItem: RecommendationItem, index: Int) {
        val productId = recommendationItem.productId.toString()
        val topAds = recommendationItem.isTopAds
        val clickUrl = recommendationItem.clickUrl
        val productName = recommendationItem.name
        val imageUrl = recommendationItem.imageUrl

        userSession?.userId?.let {
            UohAnalytics.productClickRecommendation(ECommerceClick.Products(
                name = productName,
                id = recommendationItem.productId.toString(),
                price = recommendationItem.price,
                category = recommendationItem.categoryBreadcrumbs,
                position = index.toString()), topAds, it)
        }

        if (topAds) activity?.let { TopAdsUrlHitter(it).hitClickUrl(UohListFragment::class.qualifiedName, clickUrl, productId, productName, imageUrl) }
        onProductClicked(productId)
    }

    private fun onProductClicked(productId: String) {
        activity?.let {
            val intent = RouteManager.getIntent(it, ApplinkConst.PRODUCT_INFO, productId)
            startActivity(intent)
        }
    }

    override fun atcRecommendationItem(recommendationItem: RecommendationItem) {
        /*val listParamAtcMulti = arrayListOf<AddToCartMultiParam>()
        listParamAtcMulti.add(AddToCartMultiParam(
                productId = recommendationItem.productId,
                productName = recommendationItem.name,
                productPrice = recommendationItem.priceInt,
                qty = recommendationItem.quantity,
                notes = "",
                shopId = recommendationItem.shopId,
                category = recommendationItem.categoryBreadcrumbs))
        uohListViewModel.doAtcMulti(userSession?.userId ?: "", GraphqlHelper.loadRawString(resources, com.tokopedia.atc_common.R.raw.mutation_add_to_cart_multi), listParamAtcMulti)*/

        val atcParam = AddToCartRequestParams(
                productId = recommendationItem.productId.toLong(),
                productName = recommendationItem.name,
                price = recommendationItem.priceInt.toString(),
                quantity = recommendationItem.quantity,
                shopId = recommendationItem.shopId,
                category = recommendationItem.categoryBreadcrumbs)
        uohListViewModel.doAtc(atcParam)
        // analytics
        trackAtcRecommendationItem(recommendationItem)
    }

    private fun trackAtcRecommendationItem(recommendationItem: RecommendationItem) {
        val productId = recommendationItem.productId.toString()
        val productName = recommendationItem.name
        val productPrice = recommendationItem.price
        val productCategory = recommendationItem.categoryBreadcrumbs
        val qty = recommendationItem.quantity.toString()
        val imageUrl = recommendationItem.imageUrl
        val cartId = recommendationItem.cartId
        val isTopAds = recommendationItem.isTopAds
        val url = "${recommendationItem.clickUrl}&click_source=ATC_direct_click";

        val product = ECommerceAddRecommendation.Add.ActionField.Product(
                name = productName,
                id = productId,
                price = productPrice,
                category = productCategory,
                quantity = qty,
                dimension45 = cartId)
        val arrayListProduct = arrayListOf<ECommerceAddRecommendation.Add.ActionField.Product>()
        arrayListProduct.add(product)

        userSession?.userId?.let { UohAnalytics.productAtcRecommendation(userId = it,listProduct = arrayListProduct, isTopads = isTopAds) }
        if (isTopAds) activity?.let { TopAdsUrlHitter(it).hitClickUrl(UohListFragment::class.qualifiedName, url, productId, productName, imageUrl) }
    }

    private fun doChatSeller(appUrl: String, order: UohListOrder.Data.UohOrders.Order) {
        var invoiceCode = ""
        var invoiceUrl = ""

        try {
            val parser = JsonParser()
            val queryParams: JsonObject = parser.parse(order.metadata.queryParams).asJsonObject
            invoiceCode = queryParams.get(QUERY_PARAM_INVOICE).asString
            invoiceUrl = queryParams.get(QUERY_PARAM_INVOICE_URL).asString
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val intent = RouteManager.getIntent(context, URLDecoder.decode(appUrl, UohConsts.UTF_8))
        intent.putExtra(ApplinkConst.Chat.INVOICE_ID, order.verticalID)
        intent.putExtra(ApplinkConst.Chat.INVOICE_CODE, invoiceCode)
        if (order.metadata.products.isNotEmpty()) {
            intent.putExtra(ApplinkConst.Chat.INVOICE_TITLE, order.metadata.products.first().title)
            intent.putExtra(ApplinkConst.Chat.INVOICE_IMAGE_URL, order.metadata.products.first().imageURL)
        }
        intent.putExtra(ApplinkConst.Chat.INVOICE_DATE, order.metadata.paymentDateStr)
        intent.putExtra(ApplinkConst.Chat.INVOICE_URL, invoiceUrl)
        intent.putExtra(ApplinkConst.Chat.INVOICE_STATUS_ID, order.verticalStatus)
        intent.putExtra(ApplinkConst.Chat.INVOICE_STATUS, order.metadata.status.label)
        intent.putExtra(ApplinkConst.Chat.INVOICE_TOTAL_AMOUNT, order.metadata.totalPrice.value)
        startActivity(intent)
    }

    private fun atc(orderData: UohListOrder.Data.UohOrders.Order) {
        if (orderData.metadata.listProducts.isNotEmpty()) {
            val listOfStrings = gson.fromJson(orderData.metadata.listProducts, mutableListOf<String>().javaClass)
            val jsonArray: JsonArray = gson.toJsonTree(listOfStrings).asJsonArray
            val listParamAtcMulti = arrayListOf<AddToCartMultiParam>()
            for (x in 0 until jsonArray.size()) {
                val objParam = jsonArray.get(x).asJsonObject
                listParamAtcMulti.add(AddToCartMultiParam(
                        productId = objParam.get(PRODUCT_ID).asInt,
                        productName = objParam.get(PRODUCT_NAME).asString,
                        productPrice = objParam.get(PRODUCT_PRICE).asInt,
                        qty = objParam.get(QUANTITY).asInt,
                        notes = objParam.get(NOTES).asString,
                        shopId = objParam.get(SHOP_ID).asInt,
                        custId = objParam.get(CUSTOMER_ID).asInt,
                        warehouseId = objParam.get(WAREHOUSE_ID).asInt
                ))
            }

            uohListViewModel.doAtcMulti(userSession?.userId ?: "", GraphqlHelper.loadRawString(resources, com.tokopedia.atc_common.R.raw.mutation_add_to_cart_multi), listParamAtcMulti)

            // analytics
            val arrayListProducts = arrayListOf<ECommerceAdd.Add.Products>()
            orderData.metadata.products.forEachIndexed { index, product ->
                val objProduct = jsonArray.get(index).asJsonObject
                arrayListProducts.add(ECommerceAdd.Add.Products(
                        name = product.title,
                        id = objProduct.get(EE_PRODUCT_ID).asString,
                        price = objProduct.get(EE_PRODUCT_PRICE).asString,
                        quantity = objProduct.get(EE_QUANTITY).asString,
                        dimension79 = objProduct.get(EE_SHOP_ID).asString
                ))
            }
            userSession?.userId?.let { UohAnalytics.clickBeliLagiOnOrderCardMP("", it, arrayListProducts, orderData.verticalCategory) }
        }
    }
}