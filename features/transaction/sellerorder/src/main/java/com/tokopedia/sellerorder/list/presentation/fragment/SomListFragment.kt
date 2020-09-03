package com.tokopedia.sellerorder.list.presentation.fragment

import android.animation.Animator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.view.RefreshHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.sellerhome.AppLinkMapperSellerHome
import com.tokopedia.coachmark.CoachMark
import com.tokopedia.coachmark.CoachMarkBuilder
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.config.GlobalConfig
import com.tokopedia.design.quickfilter.QuickFilterItem
import com.tokopedia.design.quickfilter.custom.CustomViewQuickFilterItem
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.getCalculatedFormattedDate
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.seller.active.common.service.UpdateShopActiveService
import com.tokopedia.seller_migration_common.isSellerMigrationEnabled
import com.tokopedia.seller_migration_common.presentation.widget.SellerMigrationGenericBottomSheet.Companion.createNewInstance
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.SomComponentInstance
import com.tokopedia.sellerorder.analytics.SomAnalytics
import com.tokopedia.sellerorder.analytics.SomAnalytics.eventClickOrder
import com.tokopedia.sellerorder.analytics.SomAnalytics.eventSubmitSearch
import com.tokopedia.sellerorder.common.errorhandler.SomErrorHandler
import com.tokopedia.sellerorder.common.presenter.model.Roles
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.common.util.SomConsts.ERROR_GET_USER_ROLES
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_STATUS_ID
import com.tokopedia.sellerorder.common.util.SomConsts.FROM_WIDGET_TAG
import com.tokopedia.sellerorder.common.util.SomConsts.LIST_ORDER_SCREEN_NAME
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_ORDER_ID
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_USER_ROLES
import com.tokopedia.sellerorder.common.util.SomConsts.RESULT_ACCEPT_ORDER
import com.tokopedia.sellerorder.common.util.SomConsts.RESULT_CONFIRM_SHIPPING
import com.tokopedia.sellerorder.common.util.SomConsts.RESULT_PROCESS_REQ_PICKUP
import com.tokopedia.sellerorder.common.util.SomConsts.RESULT_REJECT_ORDER
import com.tokopedia.sellerorder.common.util.SomConsts.RESULT_SET_DELIVERED
import com.tokopedia.sellerorder.common.util.SomConsts.SORT_ASCENDING
import com.tokopedia.sellerorder.common.util.SomConsts.SORT_DESCENDING
import com.tokopedia.sellerorder.common.util.SomConsts.STATUS_ALL_ORDER
import com.tokopedia.sellerorder.common.util.SomConsts.STATUS_CODE_ORDER_DELIVERED
import com.tokopedia.sellerorder.common.util.SomConsts.STATUS_CODE_ORDER_DELIVERED_DUE_LIMIT
import com.tokopedia.sellerorder.common.util.SomConsts.STATUS_DELIVERED
import com.tokopedia.sellerorder.common.util.SomConsts.STATUS_DONE
import com.tokopedia.sellerorder.common.util.SomConsts.STATUS_NEW_ORDER
import com.tokopedia.sellerorder.common.util.SomConsts.STATUS_ORDER_CANCELLED
import com.tokopedia.sellerorder.common.util.SomConsts.TAB_ACTIVE
import com.tokopedia.sellerorder.common.util.SomConsts.TAB_STATUS
import com.tokopedia.sellerorder.common.util.Utils
import com.tokopedia.sellerorder.detail.data.model.SomAcceptOrder
import com.tokopedia.sellerorder.detail.data.model.SomRejectOrder
import com.tokopedia.sellerorder.detail.presentation.activity.SomDetailActivity
import com.tokopedia.sellerorder.list.data.model.SomListFilter
import com.tokopedia.sellerorder.list.data.model.SomListOrder
import com.tokopedia.sellerorder.list.data.model.SomListOrderParam
import com.tokopedia.sellerorder.list.data.model.SomListTicker
import com.tokopedia.sellerorder.list.di.DaggerSomListComponent
import com.tokopedia.sellerorder.list.presentation.activity.SomFilterActivity
import com.tokopedia.sellerorder.list.presentation.adapter.SomListItemAdapter
import com.tokopedia.sellerorder.list.presentation.viewmodel.SomListViewModel
import com.tokopedia.sellerorder.requestpickup.data.model.SomProcessReqPickup
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.empty_list.*
import kotlinx.android.synthetic.main.fragment_som_list.*
import kotlinx.android.synthetic.main.partial_som_list_waiting_payment.*
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

/**
 * Created by fwidjaja on 2019-08-23.
 */
class SomListFragment : BaseDaggerFragment(), RefreshHandler.OnRefreshHandlerListener,
        SearchInputView.Listener, SearchInputView.ResetListener, SomListItemAdapter.ActionListener {

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val coachMark: CoachMark by lazy {
        CoachMarkBuilder().build()
    }

    private val coachMarkSearch: CoachMarkItem by lazy {
        CoachMarkItem(rl_search_filter, getString(R.string.coachmark_search), getString(R.string.coachmark_search_info))
    }

    private val coachMarkProduct: CoachMarkItem by lazy {
        CoachMarkItem(order_list_rv, getString(R.string.coachmark_product), getString(R.string.coachmark_product_info))
    }

    private val coachMarkFilter: CoachMarkItem by lazy {
        CoachMarkItem(filter_action_button, getString(R.string.coachmark_filter), getString(R.string.coachmark_filter_info))
    }

    private val FLAG_DETAIL = 3333
    private val FLAG_CONFIRM_REQ_PICKUP = 3553
    private val ANIMATION_DURATION_IN_MILIS = 250L
    private val ANIMATION_TYPE = "translationY"
    private val TRANSLATION_LENGTH = 500f

    private var searchKeyword = ""

    private lateinit var somListItemAdapter: SomListItemAdapter
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private var filterList: List<SomListFilter.Data.OrderFilterSom.StatusList> = listOf()
    private var orderList: SomListOrder.Data.OrderList = SomListOrder.Data.OrderList()
    private var mapOrderStatus = HashMap<String, List<Int>>()
    private var paramOrder = SomListOrderParam()
    private var refreshHandler: RefreshHandler? = null
    private var tabActive = ""
    private var tabStatus = ""
    private var filterStatusIdStr = ""
    private var filterStatusId = 0
    private var isFilterApplied = false
    private var defaultStartDate = ""
    private var defaultEndDate = ""
    private var nextOrderId = 0
    private var onLoadMore = false
    private var isFilterButtonAnimating = false
    private var _animator: Animator? = null
    private var isFromWidget: Boolean? = false
    private var textChangedJob: Job? = null

    private val somListViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[SomListViewModel::class.java]
    }

    private var userNotAllowedDialog: DialogUnify? = null

    companion object {
        private val TAG_COACHMARK = "coachMark"
        private const val REQUEST_FILTER = 2888
        private const val ERROR_GET_TICKERS = "Error when get tickers in seller order list page."
        private const val ERROR_GET_FILTER = "Error when get filters in seller order list page."
        private const val ERROR_GET_STATUS_LIST = "Error when get order status list in seller order list page."
        private const val ERROR_GET_ORDER_LIST = "Error when get list of order in seller order list page."
        private const val PAGE_NAME = "seller order list page."

        private val allowedRoles = listOf(Roles.MANAGE_SHOPSTATS, Roles.MANAGE_INBOX, Roles.MANAGE_TA, Roles.MANAGE_TX)

        @JvmStatic
        fun newInstance(bundle: Bundle): SomListFragment {
            return SomListFragment().apply {
                arguments = Bundle().apply {
                    putString(TAB_ACTIVE, bundle.getString(TAB_ACTIVE))
                    putString(TAB_STATUS, bundle.getString(TAB_STATUS))
                    putString(FILTER_STATUS_ID, bundle.getString(FILTER_STATUS_ID))
                    putBoolean(FROM_WIDGET_TAG, bundle.getBoolean(FROM_WIDGET_TAG))
                }
            }
        }
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        activity?.let {
            DaggerSomListComponent.builder()
                    .somComponent(SomComponentInstance.getSomComponent(it.application))
                    .build()
                    .inject(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            tabActive = arguments?.getString(TAB_ACTIVE).toString()
            tabStatus = arguments?.getString(TAB_STATUS).toString()
            filterStatusIdStr = arguments?.getString(FILTER_STATUS_ID).toString()
            filterStatusId = filterStatusIdStr.toIntOrNull() ?: 0
            isFromWidget = arguments?.getBoolean(FROM_WIDGET_TAG)
        }
        checkUserRole()
        getDefaultKeywordOptionFromApplink()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_som_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareLayout()
        setListeners()
        setInitialValue()
        observingUserRoles()
        observingTicker()
        observingFilter()
        observingStatusList()
        observingOrders()
        context?.let { UpdateShopActiveService.startService(it) }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden && !isUserRoleFetched()) checkUserRole()
        else if (hidden && isUserRoleFetched()) somListViewModel.clearUserRoles()
    }

    private fun isUserRoleFetched(): Boolean = somListViewModel.userRoleResult.value is Success

    private fun checkUserRole() {
        toggleSomLayout(true)
        somListViewModel.loadUserRoles(userSession.userId.toIntOrZero())
    }

    private fun getDefaultKeywordOptionFromApplink() {
        if (GlobalConfig.isSellerApp()) {
            context?.let {
                activity?.intent?.data?.apply {
                    searchKeyword = getQueryParameter(AppLinkMapperSellerHome.QUERY_PARAM_SEARCH).orEmpty()
                }
            }
        }
    }

    private fun showSellerMigrationTicker() {
        if (isSellerMigrationEnabled(context)) {
            somListSellerMigrationTicker.apply {
                tickerTitle = getString(com.tokopedia.seller_migration_common.R.string.seller_migration_generic_ticker_title)
                setHtmlDescription(getString(com.tokopedia.seller_migration_common.R.string.seller_migration_generic_ticker_content))
                setDescriptionClickEvent(object : TickerCallback {
                    override fun onDescriptionViewClick(charSequence: CharSequence) {
                        openSellerMigrationBottomSheet()
                    }

                    override fun onDismiss() {
                        // No Op
                    }
                })
                show()
            }
        }
    }

    private fun openSellerMigrationBottomSheet() {
        context?.let {
            val sellerMigrationBottomSheet: BottomSheetUnify = createNewInstance()
            sellerMigrationBottomSheet.show(childFragmentManager, "")
        }
    }

    private fun prepareLayout() {
        refreshHandler = RefreshHandler(swipe_refresh_layout, this)
        refreshHandler?.setPullEnabled(true)
        somListItemAdapter = SomListItemAdapter()
        somListItemAdapter.setActionListener(this)
        addEndlessScrollListener()
    }

    private fun addEndlessScrollListener() {
        order_list_rv?.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = somListItemAdapter
            scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager as LinearLayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int) {
                    onLoadMore = true
                    if (nextOrderId != 0) {
                        loadOrderList(nextOrderId)
                    }
                }
            }
            addOnScrollListener(scrollListener)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        _animator?.end()
                        ObjectAnimator.ofFloat(filter_action_button, ANIMATION_TYPE, 0f).apply {
                            duration = ANIMATION_DURATION_IN_MILIS
                            addListener(object : Animator.AnimatorListener {
                                override fun onAnimationRepeat(p0: Animator?) {
                                }

                                override fun onAnimationCancel(p0: Animator?) {
                                    isFilterButtonAnimating = false
                                }

                                override fun onAnimationStart(animation: Animator) {
                                    isFilterButtonAnimating = true
                                }

                                override fun onAnimationEnd(animation: Animator) {
                                    isFilterButtonAnimating = false
                                }
                            })
                            if (!isFilterButtonAnimating) {
                                start()
                            }
                        }
                    }
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0) {
                        ObjectAnimator.ofFloat(filter_action_button, ANIMATION_TYPE, TRANSLATION_LENGTH).apply {
                            duration = ANIMATION_DURATION_IN_MILIS
                            addListener(object : Animator.AnimatorListener {
                                override fun onAnimationRepeat(p0: Animator?) {
                                }

                                override fun onAnimationCancel(p0: Animator?) {
                                    isFilterButtonAnimating = false
                                    _animator = null
                                }

                                override fun onAnimationStart(animation: Animator) {
                                    isFilterButtonAnimating = true
                                    _animator = animation
                                }

                                override fun onAnimationEnd(animation: Animator) {
                                    isFilterButtonAnimating = false
                                    _animator = null

                                }
                            })
                            if (!isFilterButtonAnimating) {
                                start()
                            }
                        }
                    } else if (dy < 0) {
                        ObjectAnimator.ofFloat(filter_action_button, ANIMATION_TYPE, 0f).apply {
                            duration = ANIMATION_DURATION_IN_MILIS
                            addListener(object : Animator.AnimatorListener {
                                override fun onAnimationRepeat(p0: Animator?) {
                                }

                                override fun onAnimationCancel(p0: Animator?) {
                                    isFilterButtonAnimating = false
                                }

                                override fun onAnimationStart(animation: Animator) {
                                    isFilterButtonAnimating = true
                                }

                                override fun onAnimationEnd(animation: Animator) {
                                    isFilterButtonAnimating = false
                                }
                            })
                            if (!isFilterButtonAnimating) {
                                start()
                            }
                        }
                    }
                }
            })
        }
    }

    private fun setListeners() {
        if (GlobalConfig.isSellerApp()) {
            if (searchKeyword.isNotBlank()) {
                paramOrder.search = searchKeyword
                search_input_view.searchTextView.setText(searchKeyword)
                search_input_view.searchTextView.text?.length?.let { search_input_view.searchTextView.setSelection(it) }
            }
        }
        search_input_view?.setListener(this)
        search_input_view?.setResetListener(this)
        search_input_view?.searchTextView?.setOnClickListener {
            SomAnalytics.eventClickSearchBar()
            search_input_view?.searchTextView?.isCursorVisible = true
        }

        filter_action_button?.setOnClickListener {
            SomAnalytics.eventClickFilterButtonOnOrderList(tabActive)
            val intentFilter = context?.let { ctx -> SomFilterActivity.createIntent(ctx, paramOrder, tabActive) }
            startActivityForResult(intentFilter, REQUEST_FILTER)
        }
    }

    private fun setInitialValue() {
        defaultStartDate = getCalculatedFormattedDate("dd/MM/yyyy", -90)
        defaultEndDate = Date().toFormattedString("dd/MM/yyyy")
        paramOrder.startDate = defaultStartDate
        paramOrder.endDate = defaultEndDate
    }

    private fun loadTicker() {
        activity?.resources?.let {
            somListViewModel.loadTickerList(GraphqlHelper.loadRawString(it, R.raw.gql_som_ticker))
        }
    }

    private fun observingTicker() = somListViewModel.tickerListResult.observe(viewLifecycleOwner, Observer {
        when (it) {
            is Success -> {
                renderInfoTicker(it.data)
            }
            is Fail -> {
                SomErrorHandler.logExceptionToCrashlytics(it.throwable, ERROR_GET_TICKERS)
                ticker_info?.visibility = View.GONE
            }
        }
    })

    private fun observingFilter() {
        somListViewModel.filterResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    filterList = it.data.statusList
                    renderFilter()
                    if (filterStatusId != 0) {
                        loadStatusOrderList()
                    } else {
                        nextOrderId = 0
                        loadOrderList(nextOrderId)
                    }
                }
                is Fail -> {
                    SomErrorHandler.logExceptionToCrashlytics(it.throwable, ERROR_GET_FILTER)
                    quick_filter?.visibility = View.GONE
                }
            }
        })
    }

    private fun observingStatusList() = somListViewModel.statusOrderListResult.observe(viewLifecycleOwner, Observer {
        when (it) {
            is Success -> {
                it.data.forEach { statusList ->
                    if (statusList.id == filterStatusId) {
                        paramOrder.statusList = statusList.orderStatusIdList
                        return@forEach
                    }
                }
                loadOrderList(nextOrderId)
            }
            is Fail -> {
                SomErrorHandler.logExceptionToCrashlytics(it.throwable, ERROR_GET_STATUS_LIST)
                loadOrderList(nextOrderId)
            }
        }
    })

    private fun observingUserRoles() {
        somListViewModel.userRoleResult.observe(viewLifecycleOwner, Observer { result ->
            result?.run {
                when (result) {
                    is Success -> {
                        if (result.data.roles.any { allowedRoles.contains(it) }) {
                            onUserAllowedToViewSOM()
                        } else {
                            onUserNotAllowedToViewSOM()
                        }
                    }
                    is Fail -> {
                        SomErrorHandler.logExceptionToCrashlytics(result.throwable, String.format(ERROR_GET_USER_ROLES, PAGE_NAME))
                        toggleSomLayout(false)
                        renderErrorOrderList(getString(R.string.error_list_title), getString(R.string.error_list_desc))
                    }
                }
            }
        })
    }

    private fun onUserNotAllowedToViewSOM() {
        context?.run {
            if (userNotAllowedDialog == null) {
                userNotAllowedDialog = Utils.createUserNotAllowedDialog(this)
            }
            userNotAllowedDialog?.show()
        }
    }

    private fun onUserAllowedToViewSOM() {
        toggleSomLayout(false)
        loadTicker()
        loadFilterList()
        rl_search_filter.show()
        filterButton.show()
        activity?.let { SomAnalytics.sendScreenName(it, LIST_ORDER_SCREEN_NAME) }
        isFromWidget?.let {
            if (it) SomAnalytics.eventClickWidgetNewOrder()
        }
    }

    private fun toggleSomLayout(isLoading: Boolean) {
        progressBarSom?.showWithCondition(isLoading)
        layoutSom?.showWithCondition(!isLoading)
    }

    private fun loadStatusOrderList() {
        activity?.resources?.let {
            somListViewModel.loadStatusOrderList(GraphqlHelper.loadRawString(it, R.raw.gql_som_status_list))
        }
    }

    private fun loadOrderList(nextOrderId: Int) {
        paramOrder.nextOrderId = nextOrderId
        activity?.resources?.let {
            somListViewModel.loadOrderList(paramOrder)
        }
    }

    private fun loadFilterList() {
        activity?.resources?.let {
            somListViewModel.loadFilter(GraphqlHelper.loadRawString(it, R.raw.gql_som_filter))
        }
    }

    private fun renderInfoTicker(tickerList: List<SomListTicker.Data.OrderTickers.Tickers>) {
        if (tickerList.isNotEmpty()) {
            somListSellerMigrationTicker.hide()
            (ticker_info?.getChildAt(0) as CardView).useCompatPadding = false
            ticker_info?.visibility = View.VISIBLE
            if (tickerList.size > 1) {
                val listTickerData = arrayListOf<TickerData>()
                var indexTicker = 0
                tickerList.forEach {
                    if (it.isActive) {
                        listTickerData.add(TickerData("", it.shortDesc + " ${getString(R.string.ticker_info_selengkapnya)}", Ticker.TYPE_ANNOUNCEMENT, true, it.tickerId))
                        indexTicker++
                    }
                }

                context?.let {
                    val adapter = TickerPagerAdapter(it, listTickerData)
                    adapter.setPagerDescriptionClickEvent(object : TickerPagerCallback {
                        override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
                            RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, linkUrl))
                            SomAnalytics.eventClickSeeMoreOnTicker(itemData.toString())
                        }
                    })
                    ticker_info?.setDescriptionClickEvent(object : TickerCallback {
                        override fun onDescriptionViewClick(linkUrl: CharSequence) {}

                        override fun onDismiss() {
                            SomAnalytics.eventClickXOnTicker(listTickerData.first().itemData.toString())
                        }

                    })
                    ticker_info?.addPagerView(adapter, listTickerData)
                    SomAnalytics.eventViewTicker(listTickerData.first().itemData.toString())
                }
            } else {
                tickerList.first().let {
                    SomAnalytics.eventViewTicker("${it.tickerId}")
                    ticker_info?.setHtmlDescription(it.shortDesc + " ${getString(R.string.ticker_info_selengkapnya)}")
                    ticker_info?.tickerType = Ticker.TYPE_ANNOUNCEMENT
                    ticker_info?.setDescriptionClickEvent(object : TickerCallback {
                        override fun onDescriptionViewClick(linkUrl: CharSequence) {
                            RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, linkUrl))
                            SomAnalytics.eventClickSeeMoreOnTicker("${it.tickerId}")
                        }

                        override fun onDismiss() {
                            SomAnalytics.eventClickXOnTicker("${it.tickerId}")
                        }

                    })
                }
            }
        } else {
            ticker_info?.visibility = View.GONE
            showSellerMigrationTicker()
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
                if (it.key.equals(STATUS_ALL_ORDER, true) ||
                        it.key.equals(STATUS_DONE, true) ||
                        it.key.equals(STATUS_ORDER_CANCELLED, true)) {
                    paramOrder.sortBy = SORT_DESCENDING
                } else {
                    paramOrder.sortBy = SORT_ASCENDING
                }
                if (paramOrder.statusList.isEmpty()) {
                    if (tabStatus.equals(STATUS_DELIVERED, true)) {
                        val listPesananTiba = ArrayList<Int>()
                        if (it.orderStatusIdList.contains(STATUS_CODE_ORDER_DELIVERED)) listPesananTiba.add(STATUS_CODE_ORDER_DELIVERED)
                        if (it.orderStatusIdList.contains(STATUS_CODE_ORDER_DELIVERED_DUE_LIMIT)) listPesananTiba.add(STATUS_CODE_ORDER_DELIVERED_DUE_LIMIT)
                        paramOrder.statusList = listPesananTiba
                    } else {
                        paramOrder.statusList = it.orderStatusIdList
                    }
                }
            }

            listQuickFilter.add(filterItem)
            mapOrderStatus[it.key] = it.orderStatusIdList
            index++
        }

        quick_filter?.renderFilter(listQuickFilter, currentIndex)
        quick_filter?.setListener { keySelected ->
            filterStatusId = 0
            var tmpKeySelected = keySelected
            if (tmpKeySelected == "0") {
                tmpKeySelected = STATUS_ALL_ORDER
            }
            mapOrderStatus.forEach { (key, listOrderStatusId) ->
                if (tmpKeySelected.equals(key, true)) {
                    tabActive = tmpKeySelected
                    SomAnalytics.eventClickQuickFilter(tabActive)
                    if (listOrderStatusId.isNotEmpty()) {
                        this.paramOrder.statusList = listOrderStatusId
                        renderFilter()
                        refreshHandler?.startRefresh()
                    }
                    return@forEach
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun observingOrders() {
        somListViewModel.orderListResult.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Success -> {
                    orderList = it.data
                    nextOrderId = orderList.cursorOrderId
                    if (orderList.orders.isNotEmpty()) {
                        renderOrderList()
                        showCoachMarkProducts()
                    } else {
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
                    }
                }
                is Fail -> {
                    SomErrorHandler.logExceptionToCrashlytics(it.throwable, ERROR_GET_ORDER_LIST)
                    renderErrorOrderList(getString(R.string.error_list_title), getString(R.string.error_list_desc))
                }
            }
        })
    }

    private fun renderOrderList() {
        refreshHandler?.finishRefresh()
        empty_state_order_list?.visibility = View.GONE
        order_list_rv?.visibility = View.VISIBLE
        quick_filter?.visibility = View.VISIBLE

        if (!onLoadMore) {
            somListItemAdapter.addList(orderList.orders)
            renderWaitingForPaymentCard()
        } else {
            somListItemAdapter.appendList(orderList.orders)
            scrollListener.updateStateAfterGetData()
        }
    }

    private fun renderWaitingForPaymentCard() {
        if (tabActive == STATUS_NEW_ORDER || tabActive == STATUS_ALL_ORDER) {
            val filterResult = somListViewModel.filterResult.value
            if (filterResult is Success) {
                val amount = if (filterResult.data.waitingPaymentCounter.amount > 100) "99+"
                else filterResult.data.waitingPaymentCounter.amount.toString()
                tvTitle.text = getString(
                        R.string.som_list_order_waiting_payment_button_text,
                        filterResult.data.waitingPaymentCounter.text,
                        amount)
                somWaitingPaymentButton.visible()
            }
        } else {
            somWaitingPaymentButton.gone()
        }
    }

    private fun showCoachMarkProducts() {
        if (!coachMark.hasShown(activity, TAG_COACHMARK) && !GlobalConfig.isSellerApp()) {
            coachMark.show(activity, TAG_COACHMARK, arrayListOf(coachMarkSearch, coachMarkProduct, coachMarkFilter))
        }
    }

    private fun showCoachMarkProductsEmpty() {
        if (!coachMark.hasShown(activity, TAG_COACHMARK) && !GlobalConfig.isSellerApp()) {
            coachMark.show(activity, TAG_COACHMARK, arrayListOf(coachMarkSearch, coachMarkFilter))
        }
    }

    private fun renderFilterEmpty(title: String, desc: String) {
        refreshHandler?.finishRefresh()
        order_list_rv?.visibility = View.GONE
        quick_filter?.visibility = View.VISIBLE
        empty_state_order_list?.visibility = View.VISIBLE
        title_empty?.text = title
        desc_empty?.text = desc
        btn_cek_peluang?.visibility = View.GONE
        SomAnalytics.eventViewEmptyState(tabActive)
    }

    private fun renderErrorOrderList(title: String, desc: String) {
        val isUserRoleFetched = isUserRoleFetched()
        filterButton.showWithCondition(isUserRoleFetched)
        rl_search_filter.showWithCondition(isUserRoleFetched)
        refreshHandler?.finishRefresh()
        order_list_rv?.visibility = View.GONE
        quick_filter?.visibility = View.GONE
        empty_state_order_list?.visibility = View.VISIBLE
        title_empty?.text = title
        desc_empty?.text = desc
        ic_empty?.loadImageDrawable(R.drawable.ic_som_error_list)
        btn_cek_peluang?.apply {
            visibility = View.VISIBLE
            text = getString(R.string.retry_load_list)
            setOnClickListener {
                if (isUserRoleFetched()) {
                    refreshHandler?.startRefresh()
                } else {
                    checkUserRole()
                }
            }
        }
    }

    private fun renderEmptyOrderList() {
        refreshHandler?.finishRefresh()
        order_list_rv?.visibility = View.GONE
        quick_filter?.visibility = View.VISIBLE
        empty_state_order_list?.visibility = View.VISIBLE
        title_empty?.text = getString(R.string.empty_peluang_title)

        // Peluang Feature has been removed, thus we set text to empty and button is gone
        desc_empty?.text = ""
        btn_cek_peluang?.visibility = View.GONE
        SomAnalytics.eventViewEmptyState(tabActive)
    }

    override fun onSearchReset() {}

    override fun onSearchSubmitted(text: String?) {
        text?.let {
            eventSubmitSearch(text)
            paramOrder.search = text
            refreshHandler?.startRefresh()
        }
    }

    override fun onSearchTextChanged(text: String?) {
        textChangedJob?.cancel()
        textChangedJob = GlobalScope.launch(Dispatchers.Main) {
            delay(500L)
            text?.let {
                paramOrder.search = text
                refreshHandler?.startRefresh()
            }
        }
    }

    override fun onRefresh(view: View?) {
        addEndlessScrollListener()
        onLoadMore = false
        nextOrderId = 0
        loadOrderList(nextOrderId)
        loadFilterList()
        if (isFilterApplied) {
            if (paramOrder.startDate.equals(defaultStartDate, true) && paramOrder.endDate.equals(defaultEndDate, true)) {
                filter_action_button?.rightIconDrawable = null
            } else {
                filter_action_button?.rightIconDrawable = resources.getDrawable(R.drawable.ic_som_check)
            }
        } else filter_action_button?.rightIconDrawable = null
        activity?.let { SomAnalytics.sendScreenName(it, LIST_ORDER_SCREEN_NAME) }
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
                    filterStatusId = 0
                    paramOrder = data.getParcelableExtra(SomConsts.PARAM_LIST_ORDER)
                    isFilterApplied = checkFilterApplied(paramOrder)
                    tabActive = ""
                    renderFilter()
                    refreshHandler?.startRefresh()
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
                    data.hasExtra(RESULT_SET_DELIVERED) -> {
                        val msg = data.getStringExtra(RESULT_SET_DELIVERED)
                        refreshThenShowToasterOk(msg)
                    }
                    data.hasExtra(RESULT_SET_DELIVERED) -> {
                        val msg = data.getStringExtra(RESULT_SET_DELIVERED)
                        refreshThenShowToasterOk(msg)
                    }
                    data.hasExtra(RESULT_PROCESS_REQ_PICKUP) -> {
                        val resultProcessReqPickup = data.getParcelableExtra<SomProcessReqPickup.Data.MpLogisticRequestPickup>(RESULT_PROCESS_REQ_PICKUP)
                        refreshThenShowToasterOk(resultProcessReqPickup.listMessage.first())
                    }
                }
            }
        }
    }

    private fun refreshThenShowToasterOk(message: String) {
        refreshHandler?.startRefresh()
        view?.let { v ->
            val toasterSuccess = Toaster.build(v, message, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL, SomConsts.ACTION_OK)
            toasterSuccess.show()
        }
    }

    override fun onListItemClicked(orderId: String) {
        eventClickOrder(tabActive)
        val userRolesResult = somListViewModel.userRoleResult.value
        val userRoles = if (userRolesResult != null && userRolesResult is Success) userRolesResult.data else null
        Intent(activity, SomDetailActivity::class.java).apply {
            putExtra(PARAM_ORDER_ID, orderId)
            userRoles?.let {
                putExtra(PARAM_USER_ROLES, it)
            }
            startActivityForResult(this, FLAG_DETAIL)
        }
    }

    fun onChatIconClicked() {
        var orderStatusName = tabActive
        if (orderStatusName.isEmpty()) orderStatusName = STATUS_ALL_ORDER
        SomAnalytics.eventClickChatIconOnOrderList(orderStatusName)
    }
}