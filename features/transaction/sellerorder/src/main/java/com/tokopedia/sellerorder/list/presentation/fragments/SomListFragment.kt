package com.tokopedia.sellerorder.list.presentation.fragments

import android.animation.Animator
import android.animation.LayoutTransition.CHANGING
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.listener.EndlessLayoutManagerListener
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.applink.order.DeeplinkMapperOrder.FILTER_CANCELLATION_REQUEST
import com.tokopedia.applink.sellerhome.AppLinkMapperSellerHome.QUERY_PARAM_SEARCH
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.coachmark.CoachMarkPreference
import com.tokopedia.config.GlobalConfig
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.seller.active.common.plt.som.SomListLoadTimeMonitoring
import com.tokopedia.seller.active.common.plt.som.SomListLoadTimeMonitoringActivity
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.SomComponentInstance
import com.tokopedia.sellerorder.analytics.SomAnalytics
import com.tokopedia.sellerorder.common.domain.model.SomAcceptOrderResponse
import com.tokopedia.sellerorder.common.domain.model.SomRejectOrderResponse
import com.tokopedia.sellerorder.common.domain.model.SomRejectRequestParam
import com.tokopedia.sellerorder.common.errorhandler.SomErrorHandler
import com.tokopedia.sellerorder.common.navigator.SomNavigator
import com.tokopedia.sellerorder.common.navigator.SomNavigator.goToChangeCourierPage
import com.tokopedia.sellerorder.common.navigator.SomNavigator.goToConfirmShippingPage
import com.tokopedia.sellerorder.common.navigator.SomNavigator.goToPrintAwb
import com.tokopedia.sellerorder.common.navigator.SomNavigator.goToRequestPickupPage
import com.tokopedia.sellerorder.common.navigator.SomNavigator.goToSomOrderDetail
import com.tokopedia.sellerorder.common.navigator.SomNavigator.goToTrackingPage
import com.tokopedia.sellerorder.common.presenter.bottomsheet.SomOrderEditAwbBottomSheet
import com.tokopedia.sellerorder.common.presenter.bottomsheet.SomOrderRequestCancelBottomSheet
import com.tokopedia.sellerorder.common.presenter.dialogs.SomOrderHasRequestCancellationDialog
import com.tokopedia.sellerorder.common.presenter.model.PopUp
import com.tokopedia.sellerorder.common.presenter.model.SomPendingAction
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_ORDER_TYPE
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_STATUS_ID
import com.tokopedia.sellerorder.common.util.SomConsts.FROM_WIDGET_TAG
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_CONFIRM_SHIPPING
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_PRINT_AWB
import com.tokopedia.sellerorder.common.util.SomConsts.RESULT_CONFIRM_SHIPPING
import com.tokopedia.sellerorder.common.util.SomConsts.STATUS_ALL_ORDER
import com.tokopedia.sellerorder.common.util.SomConsts.STATUS_NEW_ORDER
import com.tokopedia.sellerorder.common.util.SomConsts.TAB_ACTIVE
import com.tokopedia.sellerorder.common.util.SomConsts.TAB_STATUS
import com.tokopedia.sellerorder.common.util.Utils.setUserNotAllowedToViewSom
import com.tokopedia.sellerorder.filter.presentation.bottomsheet.SomFilterBottomSheet
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterCancelWrapper
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterUiModel
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterUiModelWrapper
import com.tokopedia.sellerorder.list.di.DaggerSomListComponent
import com.tokopedia.sellerorder.list.domain.model.SomListGetOrderListParam
import com.tokopedia.sellerorder.list.presentation.adapter.SomListOrderAdapter
import com.tokopedia.sellerorder.list.presentation.adapter.typefactories.SomListAdapterTypeFactory
import com.tokopedia.sellerorder.list.presentation.adapter.typefactories.SomListBulkProcessOrderTypeFactory
import com.tokopedia.sellerorder.list.presentation.adapter.viewholders.SomListOrderEmptyViewHolder
import com.tokopedia.sellerorder.list.presentation.adapter.viewholders.SomListOrderViewHolder
import com.tokopedia.sellerorder.list.presentation.animator.SomFadeRightAnimator
import com.tokopedia.sellerorder.list.presentation.bottomsheets.SomListBulkProcessOrderBottomSheet
import com.tokopedia.sellerorder.list.presentation.dialogs.SomListBulkAcceptOrderDialog
import com.tokopedia.sellerorder.list.presentation.dialogs.SomListBulkPrintDialog
import com.tokopedia.sellerorder.list.presentation.filtertabs.SomListSortFilterTab
import com.tokopedia.sellerorder.list.presentation.models.*
import com.tokopedia.sellerorder.list.presentation.viewmodels.SomListViewModel
import com.tokopedia.sellerorder.list.presentation.widget.DottedNotification
import com.tokopedia.sellerorder.requestpickup.data.model.SomProcessReqPickup
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.activity.WaitingPaymentOrderActivity
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_som_list.*
import kotlinx.coroutines.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.math.abs

open class SomListFragment : BaseListFragment<Visitable<SomListAdapterTypeFactory>,
        SomListAdapterTypeFactory>(), SomListSortFilterTab.SomListSortFilterTabClickListener,
        TickerCallback, TickerPagerCallback, TextWatcher,
        SomListOrderViewHolder.SomListOrderItemListener, CoroutineScope,
        SomListBulkProcessOrderBottomSheet.SomListBulkProcessOrderBottomSheetListener,
        SomFilterBottomSheet.SomFilterFinishListener,
        SomListOrderEmptyViewHolder.SomListEmptyStateListener, SomListBulkPrintDialog.SomListBulkPrintDialogClickListener, Toolbar.OnMenuItemClickListener {

    companion object {
        private const val DELAY_SEARCH = 500L
        private const val BUTTON_ENTER_LEAVE_ANIMATION_DURATION = 300L
        private const val TICKER_ENTER_LEAVE_ANIMATION_DURATION = 300L
        private const val TICKER_ENTER_LEAVE_ANIMATION_DELAY = 10L
        private const val COACHMARK_INDEX_ITEM_NEW_ORDER = 0
        private const val COACHMARK_INDEX_ITEM_FILTER = 1
        private const val COACHMARK_INDEX_ITEM_WAITING_PAYMENT = 2
        private const val COACHMARK_INDEX_ITEM_BULK_ACCEPT = 3
        private const val COACHMARK_ITEM_COUNT_SELLERAPP = 4
        private const val COACHMARK_ITEM_COUNT_MAINAPP = 3
        private const val KEY_LAST_ACTIVE_FILTER = "lastActiveFilter"

        private const val KEY_LAST_SELECTED_ORDER_ID = "lastSelectedOrderId"

        const val SHARED_PREF_NEW_SOM_LIST_COACH_MARK = "newSomListCoachMark"
        const val DELAY_COACHMARK = 500L

        @JvmStatic
        fun newInstance(bundle: Bundle): SomListFragment {
            return SomListFragment().apply {
                arguments = Bundle().apply {
                    putString(FILTER_STATUS_ID, bundle.getString(FILTER_STATUS_ID))
                    putBoolean(FROM_WIDGET_TAG, bundle.getBoolean(FROM_WIDGET_TAG))
                    putString(TAB_ACTIVE, bundle.getString(TAB_ACTIVE))
                    putString(TAB_STATUS, bundle.getString(TAB_STATUS))
                    putString(QUERY_PARAM_SEARCH, bundle.getString(QUERY_PARAM_SEARCH))
                    putInt(FILTER_ORDER_TYPE, bundle.getInt(FILTER_ORDER_TYPE))
                }
            }
        }
    }

    private val masterJob = SupervisorJob()

    private val somListSortFilterTab: SomListSortFilterTab? by lazy {
        sortFilterSomList?.let {
            SomListSortFilterTab(it, this)
        }
    }

    private val somListLayoutManager by lazy { rvSomList?.layoutManager as? LinearLayoutManager }

    private val recyclerViewScrollListener: RecyclerView.OnScrollListener by lazy {
        object : RecyclerView.OnScrollListener() {
            // to handle new order card coachmark (coachmark need to scroll along with the recyclerview and gone when new order card gone off screen)
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (coachMark?.currentIndex == newOrderCoachMarkItemPosition) {
                    if (coachMark?.isDismissed == true && abs(dy) <= 100) {
                        reshowNewOrderCoachMark(dy < 0)
                    } else if (coachMark?.isDismissed == false) {
                        if (somListLayoutManager == null) {
                            return
                        }
                        val layoutManager = somListLayoutManager!!
                        val firstVisibleIndex = layoutManager.findFirstVisibleItemPosition()
                        val lastVisibleIndex = layoutManager.findLastVisibleItemPosition()
                        val currentNewOrderQuickActionButton = layoutManager.findViewByPosition(currentNewOrderWithCoachMark)?.findViewById<View>(R.id.btnQuickAction)
                        if (coachMark?.isDismissed == false && (currentNewOrderWithCoachMark !in firstVisibleIndex..lastVisibleIndex ||
                                        (currentNewOrderQuickActionButton != null && getVisiblePercent(currentNewOrderQuickActionButton) == -1))) {
                            coachMark?.setOnDismissListener {
                                coachMark?.setOnDismissListener { }
                                reshowNewOrderCoachMark(dy < 0)
                            }
                            dismissCoachMark(false)
                        }
                    }
                }
            }

            private fun reshowNewOrderCoachMark(isReversed: Boolean) {
                if (somListLayoutManager == null) {
                    return
                }
                val layoutManager = somListLayoutManager!!
                val firstVisibleIndex = layoutManager.findFirstVisibleItemPosition()
                val lastVisibleIndex = layoutManager.findLastVisibleItemPosition()
                val visibleRange = firstVisibleIndex..lastVisibleIndex
                (visibleRange.takeIf { !isReversed } ?: visibleRange.reversed()).forEach {
                    val order = adapter.data.getOrNull(it)
                    if (order is SomListOrderUiModel && order.orderStatusId == SomConsts.STATUS_CODE_ORDER_CREATED &&
                            order.buttons.firstOrNull()?.key == SomConsts.KEY_ACCEPT_ORDER && !isOrderWithCancellationRequest(order)) {
                        layoutManager.findViewByPosition(it)?.findViewById<View>(R.id.btnQuickAction)?.takeIf {
                            it.isVisible
                        }?.let { quickActionButton ->
                            if (getVisiblePercent(quickActionButton) == 0) {
                                quickActionButton.post {
                                    createCoachMarkItems(quickActionButton).run {
                                        if (activity?.isFinishing != false) return@post
                                        if (size == coachMarkItemCount) {
                                            currentNewOrderWithCoachMark = it
                                            coachMark?.isDismissed = false
                                            shouldShowCoachMark = false
                                            coachMark?.showCoachMark(this, index = coachMarkIndexToShow)
                                        } else {
                                            reshowNewOrderCoachMark(isReversed)
                                        }
                                    }
                                }
                                return@reshowNewOrderCoachMark
                            }
                        }
                    }
                }
            }
        }
    }

    private val fadeRightAnimator by lazy {
        context?.let {
            SomFadeRightAnimator(it)
        } ?: defaultItemAnimator
    }
    private val defaultItemAnimator by lazy { DefaultItemAnimator() }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    private var bulkAcceptButtonEnterAnimation: ValueAnimator? = null
    private var bulkAcceptButtonLeaveAnimation: ValueAnimator? = null
    private var isWaitingPaymentOrderPageOpened: Boolean = false
    private var currentNewOrderWithCoachMark: Int = -1
    private var shouldScrollToTop: Boolean = false
    private var filterOrderType: Int = 0
    private var skipSearch: Boolean = false // when restored, onSearchTextChanged is called which trigger unwanted refresh order list
    private var canDisplayOrderData = false
    private var canMultiAcceptOrder = false
    private var somOrderHasCancellationRequestDialog: SomOrderHasRequestCancellationDialog? = null
    private var somListBulkProcessOrderBottomSheet: SomListBulkProcessOrderBottomSheet? = null
    private var orderRequestCancelBottomSheet: SomOrderRequestCancelBottomSheet? = null
    private var somOrderEditAwbBottomSheet: SomOrderEditAwbBottomSheet? = null
    private var bulkAcceptOrderDialog: SomListBulkAcceptOrderDialog? = null
    private var tickerPagerAdapter: TickerPagerAdapter? = null
    private var errorToaster: Snackbar? = null
    private var commonToaster: Snackbar? = null
    private var textChangeJob: Job? = null
    private var filterDate = ""
    private var somFilterBottomSheet: SomFilterBottomSheet? = null
    private var pendingAction: SomPendingAction? = null
    private var tickerIsReady = false
    private var wasChangingTab = false

    protected var tabActive: String = ""
    protected var coachMarkIndexToShow: Int = 0
    protected var somListLoadTimeMonitoring: SomListLoadTimeMonitoring? = null
    protected var shouldShowCoachMark: Boolean = false
    protected var selectedOrderId: String = ""
    protected open val coachMarkItemCount: Int
        get() {
            return if (GlobalConfig.isSellerApp()) COACHMARK_ITEM_COUNT_SELLERAPP else COACHMARK_ITEM_COUNT_MAINAPP
        }
    protected open val newOrderCoachMarkItemPosition: Int
        get() {
            return COACHMARK_INDEX_ITEM_NEW_ORDER
        }
    protected open val filterChipCoachMarkItemPosition: Int
        get() {
            return COACHMARK_INDEX_ITEM_FILTER
        }
    protected open val waitingPaymentCoachMarkItemPosition: Int
        get() {
            return COACHMARK_INDEX_ITEM_WAITING_PAYMENT
        }
    protected open val bulkProcessCoachMarkItemPosition: Int
        get() {
            return COACHMARK_INDEX_ITEM_BULK_ACCEPT
        }
    protected val viewModel: SomListViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(SomListViewModel::class.java)
    }

    protected val coachMark: CoachMark2? by lazy {
        context?.let {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                CoachMark2(it)
            } else null
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + masterJob

    override fun getSwipeRefreshLayoutResourceId() = R.id.swipeRefreshLayoutSomList
    override fun getSwipeRefreshLayout(view: View?) = view?.findViewById<SwipeRefreshLayout>(swipeRefreshLayoutResourceId)
    override fun createAdapterInstance() = SomListOrderAdapter(adapterTypeFactory)
    override fun onItemClicked(t: Visitable<SomListAdapterTypeFactory>?) {}
    override fun getAdapterTypeFactory() = SomListAdapterTypeFactory(this, this)
    override fun getRecyclerViewResourceId() = R.id.rvSomList
    override fun getRecyclerView(view: View?) = view?.findViewById<RecyclerView>(recyclerViewResourceId)
    override fun getScreenName(): String = ""
    override fun initInjector() = inject()
    override fun onDismiss() {
        animateOrderTicker(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getActivityPltPerformanceMonitoring()
        if (savedInstanceState == null && arguments != null) {
            tabActive = arguments?.getString(TAB_ACTIVE).orEmpty()
            filterOrderType = arguments?.getInt(FILTER_ORDER_TYPE, 0).orZero()
        } else if (savedInstanceState != null) {
            skipSearch = true
            tabActive = savedInstanceState.getString(KEY_LAST_ACTIVE_FILTER).orEmpty()
            selectedOrderId = savedInstanceState.getString(KEY_LAST_SELECTED_ORDER_ID).orEmpty()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(false)
        return inflater.inflate(R.layout.fragment_som_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getActivityPltPerformanceMonitoring()
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setInitialOrderListParams()
        observeLoadingStatus()
        observeTopAdsCategory()
        observeTickers()
        observeFilters()
        observeWaitingPaymentCounter()
        observeOrderList()
        observeRefreshOrder()
        observeAcceptOrder()
        observeRejectCancelRequest()
        observeRejectOrder()
        observeEditAwb()
        observeBulkAcceptOrder()
        observeBulkAcceptOrderStatus()
        observeValidateOrder()
        observeIsAdminEligible()
        observeRefreshOrderRequest()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            SomAnalytics.sendScreenName(SomConsts.LIST_ORDER_SCREEN_NAME)
        } else {
            dismissCoachMark()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        getSwipeRefreshLayout(view)?.isRefreshing = viewModel.isRefreshingOrder()
        when (requestCode) {
            SomNavigator.REQUEST_DETAIL -> handleSomDetailActivityResult(resultCode, data)
            SomNavigator.REQUEST_CONFIRM_SHIPPING -> handleSomConfirmShippingActivityResult(resultCode, data)
            SomNavigator.REQUEST_CONFIRM_REQUEST_PICKUP -> handleSomRequestPickUpActivityResult(resultCode, data)
            SomNavigator.REQUEST_CHANGE_COURIER -> handleSomChangeCourierActivityResult(resultCode, data)
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(KEY_LAST_ACTIVE_FILTER, tabActive)
        outState.putString(KEY_LAST_SELECTED_ORDER_ID, selectedOrderId)
        super.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        tryReshowCoachMark()
    }

    override fun onPause() {
        dismissBottomSheets()
        dismissCoachMark()
        super.onPause()
        if (bulkAcceptButtonEnterAnimation?.isRunning == true) bulkAcceptButtonEnterAnimation?.end()
        if (bulkAcceptButtonLeaveAnimation?.isRunning == true) bulkAcceptButtonLeaveAnimation?.end()
    }

    override fun getEndlessLayoutManagerListener(): EndlessLayoutManagerListener? {
        return EndlessLayoutManagerListener { somListLayoutManager }
    }

    override fun loadData(page: Int) {
        loadOrderList()
    }

    override fun onSwipeRefresh() {
        shouldScrollToTop = true
        loadInitialData()
        dismissCoachMark()
    }

    override fun hideLoading() {
        if (!viewModel.isRefreshingOrder()) {
            getSwipeRefreshLayout(view)?.apply {
                isEnabled = true
                isRefreshing = false
            }
            somListLoading?.gone()
            adapter?.hideLoading()
        }
    }

    override fun onTabClicked(status: SomListFilterUiModel.Status, shouldScrollToTop: Boolean, refreshFilter: Boolean) {
        if (status.key != tabActive && refreshFilter) {
            wasChangingTab = true
        }
        rvSomList?.itemAnimator =
                if (wasChangingTab && !refreshFilter) {
                    defaultItemAnimator
                } else {
                    fadeRightAnimator
                }
        if (status.key == tabActive) {
            wasChangingTab = false
        }
        tabActive = if (status.isChecked) {
            if (somListSortFilterTab?.isStatusFilterAppliedFromAdvancedFilter == true)
                viewModel.setStatusOrderFilter(viewModel.getDataOrderListParams().statusList)
            else
                viewModel.setStatusOrderFilter(status.id)
            if (refreshFilter) {
                SomAnalytics.eventClickStatusFilter(status.id.map { it.toString() }, status.status)
            }
            status.key
        } else {
            viewModel.setStatusOrderFilter(emptyList())
            if (refreshFilter) {
                SomAnalytics.eventClickStatusFilter(somListSortFilterTab?.getAllStatusCodes().orEmpty(), SomConsts.STATUS_NAME_ALL_ORDER)
            }
            ""
        }
        setDefaultSortByValue()
        if (viewModel.isMultiSelectEnabled) {
            context.let { context ->
                if (context == null || !DeviceScreenInfo.isTablet(context)) {
                    somListLayoutManager?.findFirstVisibleItemPosition()?.let {
                        somListLayoutManager?.findViewByPosition(it)?.findViewById<View>(R.id.btnQuickAction)?.addOneTimeGlobalLayoutListener {
                            refreshOrdersOnTabClicked(shouldScrollToTop, refreshFilter)
                        }
                    }
                } else {
                    refreshOrdersOnTabClicked(shouldScrollToTop, refreshFilter)
                }
            }
            viewModel.isMultiSelectEnabled = false
            resetOrderSelectedStatus()
            toggleBulkActionButtonVisibility()
            toggleBulkActionCheckboxVisibility()
            checkBoxBulkAction.isChecked = false
            checkBoxBulkAction.setIndeterminate(false)
            checkBoxBulkAction.skipAnimation()
        } else {
            refreshOrdersOnTabClicked(shouldScrollToTop, refreshFilter)
        }
    }

    override fun onParentSortFilterClicked() {
        val cacheManager = context?.let { SaveInstanceCacheManager(it, true) }
        cacheManager?.put(SomFilterBottomSheet.KEY_SOM_LIST_GET_ORDER_PARAM, viewModel.getDataOrderListParams())
        somListSortFilterTab?.getSomFilterUi()?.let { somFilterList ->
            val somFilterUiModelWrapper = SomFilterUiModelWrapper(somFilterList)
            cacheManager?.put(SomFilterBottomSheet.KEY_SOM_FILTER_LIST, somFilterUiModelWrapper)
            somFilterBottomSheet = SomFilterBottomSheet.createInstance(
                    somListSortFilterTab?.getSelectedFilterStatusName().orEmpty(),
                    somListSortFilterTab?.isStatusFilterAppliedFromAdvancedFilter ?: false,
                    viewModel.getDataOrderListParams().statusList,
                    filterDate,
                    filterOrderType != 0,
                    cacheManager?.id.orEmpty()
            )
            somFilterBottomSheet?.setSomFilterFinishListener(this)
            somFilterBottomSheet?.isAdded?.let {
                if (!(it)) {
                    somFilterBottomSheet?.show(childFragmentManager)
                }
            }
        }
        somListSortFilterTab?.getSelectedFilterStatus().let {
            val selectedFilterKeys = arrayListOf<String>()
            selectedFilterKeys.addAll(somListSortFilterTab?.getSelectedFilterKeys().orEmpty())
            if (it.isNullOrBlank()) {
                selectedFilterKeys.add(0, STATUS_ALL_ORDER)
            } else {
                selectedFilterKeys.add(0, it)
            }
            SomAnalytics.eventClickFilter(selectedFilterKeys)
        }
    }

    override fun onDescriptionViewClick(linkUrl: CharSequence) {
        onTickerDescriptionClicked(linkUrl.toString())
    }

    override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
        onTickerDescriptionClicked(linkUrl.toString())
    }

    override fun afterTextChanged(s: Editable?) {
        textChangeJob?.cancel()
        textChangeJob = launchCatchError(block = {
            delay(DELAY_SEARCH)
            viewModel.setSearchParam(s?.toString().orEmpty())
            if (!skipSearch) {
                shouldScrollToTop = true
                loadFilters(showShimmer = false, loadOrders = true)
                if (shouldReloadOrderListImmediately()) {
                    refreshOrderList()
                } else {
                    getSwipeRefreshLayout(view)?.isRefreshing = true
                }
                SomAnalytics.eventSubmitSearch(s?.toString().orEmpty())
            }
            skipSearch = false
        }, onError = {
            // TODO: Log to crashlytics
        })
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    private fun onTickerDescriptionClicked(linkUrl: String) {
        if (linkUrl.isNotBlank()) {
            context?.let { context ->
                if (linkUrl.startsWith(SomConsts.PREFIX_HTTP)) {
                    RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, linkUrl))
                } else {
                    RouteManager.route(context, linkUrl)
                }
            }
        }
    }

    override fun onCheckChanged() {
        updateBulkActionCheckboxStatus()
        toggleBulkActionButtonVisibility()
        updateOrderCounter()
    }

    override fun onCheckBoxClickedWhenDisabled() {
        showCommonToaster(view, getString(R.string.som_list_order_cannot_be_selected), Toaster.TYPE_ERROR)
    }

    override fun onStartAdvertiseButtonClicked() {
        SomAnalytics.eventClickStartAdvertise(
                somListSortFilterTab?.getSelectedFilterStatus().orEmpty(),
                somListSortFilterTab?.getSelectedFilterStatusName().orEmpty())
    }

    override fun onOrderClicked(order: SomListOrderUiModel) {
        selectedOrderId = order.orderId
        goToSomOrderDetail(this, order.orderId)
        SomAnalytics.eventClickOrderCard(order.orderStatusId, order.status)
    }

    override fun onTrackButtonClicked(orderId: String, url: String) {
        goToTrackingPage(context, orderId, url)
    }

    override fun onConfirmShippingButtonClicked(actionName: String, orderId: String, skipValidateOrder: Boolean) {
        getSwipeRefreshLayout(view)?.isRefreshing = true
        if (!skipValidateOrder) {
            pendingAction = SomPendingAction(actionName, orderId) {
                selectedOrderId = orderId
                goToConfirmShippingPage(this, orderId)
            }
            viewModel.validateOrders(listOf(orderId))
        } else {
            selectedOrderId = orderId
            goToConfirmShippingPage(this, orderId)
        }
    }

    override fun onAcceptOrderButtonClicked(actionName: String, orderId: String, skipValidateOrder: Boolean) {
        rvSomList?.itemAnimator = fadeRightAnimator
        getSwipeRefreshLayout(view)?.isRefreshing = true
        if (!skipValidateOrder) {
            pendingAction = SomPendingAction(actionName, orderId) {
                val invoice = getOrderBy(orderId)
                viewModel.acceptOrder(orderId, invoice)
            }
            viewModel.validateOrders(listOf(orderId))
        } else {
            val invoice = getOrderBy(orderId)
            viewModel.acceptOrder(orderId, invoice)
        }
    }

    override fun onRequestPickupButtonClicked(actionName: String, orderId: String, skipValidateOrder: Boolean) {
        rvSomList?.itemAnimator = fadeRightAnimator
        getSwipeRefreshLayout(view)?.isRefreshing = true
        if (!skipValidateOrder) {
            pendingAction = SomPendingAction(actionName, orderId) {
                selectedOrderId = orderId
                goToRequestPickupPage(this, orderId)
            }
            viewModel.validateOrders(listOf(orderId))
        } else {
            selectedOrderId = orderId
            goToRequestPickupPage(this, orderId)
        }
    }

    override fun onRespondToCancellationButtonClicked(order: SomListOrderUiModel) {
        view?.let {
            if (it is ViewGroup) {
                rvSomList?.itemAnimator = fadeRightAnimator
                selectedOrderId = order.orderId
                orderRequestCancelBottomSheet = orderRequestCancelBottomSheet?.apply {
                    setupBuyerRequestCancelBottomSheet(this, it, order)
                } ?: SomOrderRequestCancelBottomSheet(it.context).apply {
                    setupBuyerRequestCancelBottomSheet(this, it, order)
                }
                orderRequestCancelBottomSheet?.init(it)
                orderRequestCancelBottomSheet?.show()
                return
            }
        }
        showCommonToaster(view, "Terjadi kesalahan, silahkan coba lagi.")
    }

    override fun onViewComplaintButtonClicked(order: SomListOrderUiModel) {
        RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, order.buttons.firstOrNull()?.url.orEmpty()))
    }

    override fun onEditAwbButtonClicked(orderId: String) {
        view?.let {
            if (it is ViewGroup) {
                somOrderEditAwbBottomSheet = somOrderEditAwbBottomSheet?.apply {
                    setupSomOrderEditAwbBottomSheet(this, it, orderId)
                } ?: SomOrderEditAwbBottomSheet(it.context).apply {
                    setupSomOrderEditAwbBottomSheet(this, it, orderId)
                }
                somOrderEditAwbBottomSheet?.init(it)
                somOrderEditAwbBottomSheet?.show()
                return
            }
        }
        showCommonToaster(view, "Terjadi kesalahan, silahkan coba lagi.")
    }

    override fun onChangeCourierClicked(orderId: String) {
        selectedOrderId = orderId
        goToChangeCourierPage(this, orderId)
    }

    override fun onFinishBindNewOrder(view: View, itemIndex: Int) {
        context?.let { context ->
            if (!CoachMarkPreference.hasShown(context, SHARED_PREF_NEW_SOM_LIST_COACH_MARK) &&
                    Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                rvSomList?.addOnScrollListener(recyclerViewScrollListener)
                setCoachMarkStepListener()
                coachMark?.onFinishListener = { rvSomList?.removeOnScrollListener(recyclerViewScrollListener) }
                CoachMarkPreference.setShown(context, SHARED_PREF_NEW_SOM_LIST_COACH_MARK, true)
                shouldShowCoachMark = true
                reshowNewOrderCoachMark()
            }
            return@let
        }
    }

    override fun isMultiSelectEnabled(): Boolean = viewModel.isMultiSelectEnabled

    override fun onBulkProcessOrderButtonClicked() {
        viewModel.bulkAcceptOrder(getSelectedOrderIds())
    }

    override fun onMenuItemClicked(keyAction: String) {
        when (keyAction) {
            KEY_PRINT_AWB -> {
                context?.let { context ->
                    SomListBulkPrintDialog(context)?.run {
                        setTitle(getString(R.string.som_list_bulk_print_dialog_title, getSelectedOrderIds().size))
                        setListener(this@SomListFragment)
                        show()
                    }
                    SomAnalytics.eventClickBulkPrintAwb(userSession.userId)
                }
            }
        }
    }

    override fun onPrintButtonClicked(markAsPrinted: Boolean) {
        goToPrintAwb(activity, view, getSelectedOrderIds(), markAsPrinted)
        SomAnalytics.eventClickYesOnBulkPrintAwb(userSession.userId)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return if ((item?.itemId == R.id.som_list_action_waiting_payment_order) && viewModel.waitingPaymentCounterResult.value !is Fail) {
            goToWaitingPaymentOrderListPage()
            val waitingPaymentOrderCounterResult = viewModel.waitingPaymentCounterResult.value
            if (waitingPaymentOrderCounterResult is Success) {
                SomAnalytics.eventClickWaitingPaymentOrderCard(
                        tabActive,
                        waitingPaymentOrderCounterResult.data.amount,
                        userSession.userId,
                        userSession.shopId)
            }
            isWaitingPaymentOrderPageOpened = true
            updateToolbarMenu()
            true
        } else {
            false
        }
    }

    private fun setupBuyerRequestCancelBottomSheet(somOrderRequestCancelBottomSheet: SomOrderRequestCancelBottomSheet, view: ViewGroup, order: SomListOrderUiModel) {
        somOrderRequestCancelBottomSheet.apply {
            setListener(object : SomOrderRequestCancelBottomSheet.SomOrderRequestCancelBottomSheetListener {
                override fun onAcceptOrder(actionName: String) {
                    onAcceptOrderButtonClicked(actionName, selectedOrderId, true)
                }

                override fun onRejectOrder(reasonBuyer: String) {
                    SomAnalytics.eventClickButtonTolakPesananPopup("${order.orderStatusId}", order.status)
                    val orderRejectRequest = SomRejectRequestParam(
                            orderId = selectedOrderId,
                            rCode = "0",
                            reason = reasonBuyer
                    )
                    rejectOrder(orderRejectRequest)
                }

                override fun onRejectCancelRequest() {
                    SomAnalytics.eventClickButtonTolakPesananPopup("${order.orderStatusId}", order.status)
                    rejectCancelOrder(selectedOrderId)
                }
            })
            init(order.buttons.firstOrNull()?.popUp ?: PopUp(), order.cancelRequestOriginNote, order.orderStatusId)
            hideKnob()
            showCloseButton()
        }
    }

    private fun setupSomOrderEditAwbBottomSheet(somOrderEditAwbBottomSheet: SomOrderEditAwbBottomSheet, it: ViewGroup, orderId: String) {
        somOrderEditAwbBottomSheet.apply {
            setListener(object : SomOrderEditAwbBottomSheet.SomOrderEditAwbBottomSheetListener {
                override fun onEditAwbButtonClicked(cancelNotes: String) {
                    val invoice = getOrderBy(orderId)
                    viewModel.editAwb(orderId, cancelNotes, invoice)
                }
            })
            hideKnob()
            showCloseButton()
        }
    }

    private fun setDefaultSortByValue() {
        if (somListSortFilterTab?.isSortByAppliedManually() != true) {
            if (tabActive == KEY_CONFIRM_SHIPPING) {
                viewModel.setSortOrderBy(SomConsts.SORT_BY_PAYMENT_DATE_ASCENDING)
            } else {
                viewModel.setSortOrderBy(SomConsts.SORT_BY_PAYMENT_DATE_DESCENDING)
            }
        }
    }

    private fun getOrderBy(orderId: String): String {
        return (adapter.data.firstOrNull {
            it is SomListOrderUiModel && it.orderId == orderId
        } as? SomListOrderUiModel)?.orderResi.orEmpty()
    }

    private fun showBulkAcceptOrderDialog(orderCount: Int) {
        context?.let { context ->
            if (bulkAcceptOrderDialog == null) {
                bulkAcceptOrderDialog = SomListBulkAcceptOrderDialog(context).apply {
                    init()
                    setOnDismiss {
                        resetOrderSelectedStatus()
                        toggleBulkAction()
                        toggleBulkActionButtonVisibility()
                        toggleBulkActionCheckboxVisibility()
                        toggleTvSomListBulkText()
                        loadFilters(loadOrders = true)
                        if (shouldReloadOrderListImmediately()) {
                            loadOrderList()
                        } else {
                            getSwipeRefreshLayout(view)?.isRefreshing = true
                        }
                    }
                }
            }
            showOnProgressAcceptAllOrderDialog(orderCount)
            bulkAcceptOrderDialog?.show()
        }
    }

    private fun inject() {
        activity?.let {
            DaggerSomListComponent.builder()
                    .somComponent(SomComponentInstance.getSomComponent(it.application))
                    .build()
                    .inject(this)
        }
    }

    private fun setupViews() {
        activity?.window?.decorView?.setBackgroundColor(ContextCompat.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.Unify_N0))
        showWaitingPaymentOrderListMenuShimmer()
        rvSomList?.layoutManager = somListLayoutManager
        bulkActionCheckBoxContainer.layoutTransition.enableTransitionType(CHANGING)
        setupToolbar()
        setupSearchBar()
        setupListeners()
    }

    private fun setupSearchBar() {
        val searchParam = arguments?.getString(QUERY_PARAM_SEARCH).orEmpty()
        searchBarSomList.searchBarTextField.setText(searchParam)
    }

    private fun observeLoadingStatus() {
        viewModel.isLoadingOrder.observe(viewLifecycleOwner, Observer { isLoading ->
            if (!isLoading) hideLoading()
        })
    }

    private fun observeTopAdsCategory() {
        viewModel.topAdsCategoryResult.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Success -> {
                    if (adapter.data.filterIsInstance<SomListEmptyStateUiModel>().isNotEmpty()) {
                        showEmptyState()
                        rvSomList?.show()
                    }
                }
                is Fail -> showToasterError(view)
            }
        })
    }

    private fun observeTickers() {
        viewModel.tickerResult.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Success -> renderTickers(result.data)
                is Fail -> {
                    tickerSomList?.gone()
                    showToasterError(view)
                }
            }
        })
    }

    private fun observeFilters() {
        viewModel.filterResult.observe(viewLifecycleOwner, object : Observer<Result<SomListFilterUiModel>> {
            var realtimeDataChangeCount = 0
            override fun onChanged(result: Result<SomListFilterUiModel>?) {
                when (result) {
                    is Success -> {
                        realtimeDataChangeCount = onSuccessGetFilter(result, realtimeDataChangeCount)
                    }
                    is Fail -> showGlobalError(result.throwable)
                    else -> showGlobalError(Throwable())
                }
                shimmerViews.gone()
            }
        })
    }

    private fun observeWaitingPaymentCounter() {
        viewModel.waitingPaymentCounterResult.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Fail -> {
                    showToasterError(view)
                }
            }
            updateToolbarMenu()
        })
    }

    private fun observeOrderList() {
        viewModel.orderListResult.observe(viewLifecycleOwner, Observer { result ->
            somListLoadTimeMonitoring?.startRenderPerformanceMonitoring()
            rvSomList.addOneTimeGlobalLayoutListener {
                stopLoadTimeMonitoring()
                animateOrderTicker(true)
            }
            when (result) {
                is Success -> renderOrderList(result.data)
                is Fail -> showGlobalError(result.throwable)
            }
        })
    }

    private fun observeRefreshOrder() {
        viewModel.refreshOrderResult.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Success -> onRefreshOrderSuccess(result.data)
                is Fail -> onRefreshOrderFailed()
            }
        })
    }

    private fun observeAcceptOrder() {
        viewModel.acceptOrderResult.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Success -> onAcceptOrderSuccess(result.data.acceptOrder, false)
                is Fail -> showToasterError(view, getString(R.string.som_list_failed_accept_order), canRetry = false)
            }
        })
    }

    private fun observeRejectOrder() {
        viewModel.rejectOrderResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    val rejectOrderResponse = it.data.rejectOrder
                    if (rejectOrderResponse.success == 1) {
                        handleRejectOrderResult(rejectOrderResponse, false)
                    } else {
                        showToasterError(view, rejectOrderResponse.message.first(), canRetry = false)
                    }
                }
                is Fail -> {
                    it.throwable.showErrorToaster()
                }
            }
        })
    }

    private fun observeEditAwb() {
        viewModel.editRefNumResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    val successEditAwbResponse = it.data
                    if (successEditAwbResponse.mpLogisticEditRefNum.listMessage.isNotEmpty()) {
                        showCommonToaster(view, successEditAwbResponse.mpLogisticEditRefNum.listMessage.first())
                        onActionCompleted(false, selectedOrderId)
                    } else {
                        showToasterError(view, getString(R.string.global_error), canRetry = false)
                    }
                }
                is Fail -> {
                    val message = it.throwable.message.toString()
                    if (message.isNotEmpty()) {
                        showToasterError(view, message, getString(R.string.som_list_button_ok), canRetry = false)
                    } else {
                        it.throwable.showErrorToaster()
                    }
                }
            }
        })
    }

    private fun observeRejectCancelRequest() {
        viewModel.rejectCancelOrderResult.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Success -> {
                    onActionCompleted(false, selectedOrderId)
                    showCommonToaster(view, result.data.rejectCancelRequest.message)
                }
                is Fail -> {
                    SomErrorHandler.logExceptionToCrashlytics(result.throwable, SomConsts.ERROR_REJECT_CANCEL_ORDER)
                    result.throwable.showErrorToaster()
                }
            }
        })
    }

    private fun observeBulkAcceptOrder() {
        viewModel.bulkAcceptOrderResult.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Success -> {
                    if (somListBulkProcessOrderBottomSheet?.isShowing() == true) {
                        somListBulkProcessOrderBottomSheet?.dismiss()
                    }
                    showBulkAcceptOrderDialog(getSelectedOrderIds().size)
                }
                is Fail -> {
                    if (somListBulkProcessOrderBottomSheet?.isShowing() == true) {
                        somListBulkProcessOrderBottomSheet?.onBulkAcceptOrderFailed()
                        showCommonToaster(view, "Terjadi kesalahan.", Toaster.TYPE_ERROR)
                    } else {
                        result.throwable.showErrorToaster()
                    }
                }
            }
        })
    }

    private fun observeBulkAcceptOrderStatus() {
        /*
            there is 4 possibility in this process
            1. All order confirmed as accepted because we receive a response which saying that m order
               is accepted from n total order where m == n
            2. Some order is confirmed as accepted and the rest might already accepted or not because
               after getting response which saying that m order from n total order is accepted where
               m < n but after receiving that response, the next 19 times check status is always
               failed (no internet or backend problem)
            3. Some order is accepted and some order is not yet accepted because we receive a response
               after check status for 20 times and the response is saying that m order is accepted from n
               total order where m < n
            4. No order is processed because 20th response is saying that 0 success and 0 failed
            5. Unknown state because check status always failed
         */
        viewModel.bulkAcceptOrderStatusResult.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                null -> {
                    showFailedAcceptAllOrderDialog(getSelectedOrderIds().size, true) // fifth case
                }
                is Success -> {
                    val orderCount = result.data.data.totalOrder
                    val successCount = result.data.data.success
                    val failedCount = orderCount - successCount
                    bulkAcceptOrderDialog?.apply {
                        if (successCount > 0) {
                            if (failedCount == 0) {
                                showSuccessAcceptAllOrderDialog(successCount) // first case
                            } else {
                                showPartialSuccessAcceptAllOrderDialog(successCount, failedCount, result.data.data.shouldRecheck) // second case
                            }
                        } else if (failedCount > 0) {
                            showFailedAcceptAllOrderDialog(orderCount, false) // third case
                        } else {
                            showFailedAcceptAllOrderDialog(orderCount, true) // fourth case
                        }
                    }
                    SomAnalytics.eventBulkAcceptOrder(
                            getSelectedOrderStatusCodes().joinToString(","),
                            getSelectedOrderStatusNames().joinToString(","),
                            successCount,
                            userSession.userId,
                            userSession.shopId)
                }
            }
        })
    }

    private fun observeValidateOrder() {
        viewModel.validateOrderResult.observe(viewLifecycleOwner, Observer { result ->
            getSwipeRefreshLayout(view)?.isRefreshing = viewModel.isRefreshingOrder()
            when (result) {
                is Success -> onSuccessValidateOrder(result.data)
                is Fail -> onFailedValidateOrder()
            }
        })
    }

    private fun observeIsAdminEligible() {
        viewModel.isOrderManageEligible.observe(viewLifecycleOwner, Observer { result ->
            when(result) {
                is Success -> {
                    result.data.let { (isSomListEligible, isMultiAcceptEligible) ->
                        canDisplayOrderData = isSomListEligible
                        canMultiAcceptOrder = isMultiAcceptEligible
                        if (isSomListEligible) {
                            somAdminPermissionView?.hide()
                            loadInitialData()
                        } else {
                            showAdminPermissionError()
                        }
                    }
                }
                is Fail -> {
                    showGlobalError(result.throwable)
                }
            }
        })
    }

    private fun observeRefreshOrderRequest() {
        viewModel.refreshOrderRequest.observe(viewLifecycleOwner, Observer { request ->
            onReceiveRefreshOrderRequest(request.first, request.second)
        })
    }

    private fun selectFilterTab(result: Success<SomListFilterUiModel>, realtimeDataChangeCount: Int) {
        if (tabActive.isNotBlank() && tabActive != STATUS_ALL_ORDER) {
            result.data.statusList.find { it.key == tabActive }?.let { activeFilter ->
                activeFilter.isChecked = true
                /*  refresh only on:
                    1. 2nd..n-th realtime (cloud) data
                    2. First realtime (cloud) data with any differences from the previous cached data (if first realtime data is coming after cached data)
                    3. First data
                 */
                if (shouldRefreshOrders(activeFilter.id, result.data.refreshOrder, realtimeDataChangeCount)) {
                    onTabClicked(activeFilter, shouldScrollToTop, false)
                }
            }
        }
    }

    private fun shouldRefreshOrders(ids: List<Int>, refreshOrder: Boolean, realtimeDataChangeCount: Int): Boolean {
        return refreshOrder && (realtimeDataChangeCount >= 1 || (realtimeDataChangeCount == 0 && viewModel.isOrderStatusIdsChanged(ids)))
    }

    private fun showOnProgressAcceptAllOrderDialog(orderCount: Int) {
        bulkAcceptOrderDialog?.run {
            hidePrimaryButton()
            hideSecondaryButton()
            setTitle(getString(R.string.som_list_bulk_accept_dialog_title_on_progress, orderCount))
            setDescription(getString(R.string.som_list_bulk_accept_dialog_description_on_progress))
            showOnProgress()
        }
    }

    private fun showSuccessAcceptAllOrderDialog(successCount: Int) {
        bulkAcceptOrderDialog?.run {
            setTitle(getString(R.string.som_list_bulk_accept_dialog_title_success, successCount))
            setDescription(getString(R.string.som_list_bulk_accept_dialog_description_success))
            showSuccess()
            setPrimaryButton(getString(R.string.som_list_bulk_accept_dialog_primary_button_success)) {
                dismissAndRunAction()
            }
            hideSecondaryButton()
        }
    }

    private fun showPartialSuccessAcceptAllOrderDialog(successCount: Int, failedCount: Int, canRetry: Boolean) {
        bulkAcceptOrderDialog?.run {
            setTitle(getString(R.string.som_list_bulk_accept_dialog_title_success, successCount))
            if (canRetry) {
                setDescription(getString(R.string.som_list_bulk_accept_dialog_description_partial_success_can_retry, failedCount))
                showSuccess()
                setPrimaryButton(getString(R.string.som_list_bulk_accept_dialog_primary_button_partial_success_can_retry)) {
                    showOnProgressAcceptAllOrderDialog(successCount + failedCount)
                    viewModel.retryGetBulkAcceptOrderStatus()
                }
                setSecondaryButton(getString(R.string.som_list_bulk_accept_dialog_secondary_button_partial_success_can_retry)) { dismiss() }
            } else {
                setDescription(getString(R.string.som_list_bulk_accept_dialog_description_partial_success_can_retry, failedCount))
                showSuccess()
                setPrimaryButton(getString(R.string.som_list_bulk_accept_dialog_primary_button_partial_success_cant_retry)) {
                    dismissAndRunAction()
                }
                setSecondaryButton("")
            }
        }
    }

    private fun showFailedAcceptAllOrderDialog(orderCount: Int, shouldRetryCheck: Boolean) {
        bulkAcceptOrderDialog?.run {
            setTitle(getString(R.string.som_list_bulk_accept_dialog_title_failed, orderCount))
            setDescription(getString(R.string.som_list_bulk_accept_dialog_description_failed))
            showFailed()
            setPrimaryButton(getString(R.string.som_list_bulk_accept_dialog_primary_button_failed)) {
                showOnProgressAcceptAllOrderDialog(orderCount)
                if (shouldRetryCheck) {
                    viewModel.retryGetBulkAcceptOrderStatus()
                } else {
                    viewModel.bulkAcceptOrder(getSelectedOrderIds())
                }
            }
            setSecondaryButton(getString(R.string.som_list_bulk_accept_dialog_secondary_button_failed)) { dismiss() }
        }
    }

    private fun getSelectedOrderStatusCodes(): List<Int> {
        return adapter.data.filterIsInstance<SomListOrderUiModel>()
                .filter { it.isChecked }
                .map { it.orderStatusId }
                .distinct()
    }

    private fun getSelectedOrderStatusNames(): List<String> {
        return adapter.data.filterIsInstance<SomListOrderUiModel>()
                .filter { it.isChecked }
                .map { it.status }
                .distinct()
    }

    private fun getSelectedOrderIds(): List<String> {
        return adapter.data.filterIsInstance<SomListOrderUiModel>()
                .filter { it.isChecked }
                .map { it.orderId }
    }

    protected open fun onActionCompleted(refreshOrder: Boolean, orderId: String) {
        if (!viewModel.isRefreshingAllOrder()) {
            if (refreshOrder) {
                val invoice = getOrderBy(orderId)
                if (invoice.isNotEmpty()) {
                    getSwipeRefreshLayout(view)?.apply {
                        isEnabled = true
                        isRefreshing = true
                    }
                    loadFilters(showShimmer = false, loadOrders = false)
                    viewModel.refreshSelectedOrder(orderId, invoice)
                }
            }
        }
    }

    private fun showEmptyState() {
        val newItems = arrayListOf(createSomListEmptyStateModel(viewModel.isTopAdsActive()))
        (adapter as? SomListOrderAdapter)?.updateOrders(newItems)
    }

    protected fun loadTopAdsCategory() {
        viewModel.getTopAdsCategory()
    }

    protected fun loadTickers() {
        viewModel.getTickers()
    }

    protected fun loadWaitingPaymentOrderCounter() {
        showWaitingPaymentOrderListMenuShimmer()
        viewModel.getWaitingPaymentCounter()
    }

    protected fun loadFilters(showShimmer: Boolean = true, loadOrders: Boolean) {
        if (showShimmer) {
            sortFilterSomList.invisible()
            shimmerViews.show()
        }
        viewModel.getFilters(loadOrders)
    }

    private fun loadOrderList() {
        if (isLoadingInitialData) {
            viewModel.resetNextOrderId()
            if (adapter.dataSize > 0) {
                getSwipeRefreshLayout(view)?.isRefreshing = true
            } else {
                rvSomList?.gone()
                somListLoading.show()
            }
        }
        viewModel.getOrderList()
    }

    private fun checkAdminPermission() {
        if (!userSession.isShopOwner) {
            somListLoadTimeMonitoring?.startNetworkPerformanceMonitoring()
        }
        viewModel.getAdminPermission()
    }

    protected open fun onAcceptOrderSuccess(acceptOrderResponse: SomAcceptOrderResponse.Data.AcceptOrder, refreshOrder: Boolean) {
        if (acceptOrderResponse.success == 1) {
            onActionCompleted(refreshOrder, selectedOrderId)
            showCommonToaster(view, acceptOrderResponse.listMessage.firstOrNull())
        } else {
            showToasterError(view, acceptOrderResponse.listMessage.firstOrNull().orEmpty(), canRetry = false)
        }
    }

    override fun loadInitialData() {
        if (canDisplayOrderData) {
            loadAllInitialData()
        } else {
            checkAdminPermission()
        }
    }

    private fun setupListeners() {
        tickerSomList?.setDescriptionClickEvent(this)
        searchBarSomList.searchBarTextField.apply {
            addTextChangedListener(this@SomListFragment)
            setOnEditorActionListener { _, _, _ ->
                hideKeyboard()
                true
            }
        }

        globalErrorSomList.setActionClickListener {
            scrollViewErrorState.gone()
            loadInitialData()
        }

        tvSomListBulk.setOnClickListener {
            toggleBulkAction()
            resetOrderSelectedStatus()
            toggleBulkActionCheckboxVisibility()
            toggleBulkActionButtonVisibility()
            updateOrderCounter()
            checkBoxBulkAction.isChecked = false
            checkBoxBulkAction.setIndeterminate(false)
            checkBoxBulkAction.skipAnimation()
            toggleTvSomListBulkText()
        }

        checkBoxBulkAction.setOnClickListener {
            if (checkBoxBulkAction.isChecked) {
                checkAllOrder()
                toggleBulkActionButtonVisibility()
            } else {
                checkBoxBulkAction.setIndeterminate(false)
                resetOrderSelectedStatus()
                toggleBulkActionButtonVisibility()
            }
            updateOrderCounter()
        }

        btnBulkAction.setOnClickListener {
            when (btnBulkAction.text) {
                getString(R.string.som_list_bulk_accept_order_button) -> showBulkAcceptOrderBottomSheet()
                getString(R.string.som_list_bulk_confirm_shipping_order_button) -> showBulkProcessOrderBottomSheet()
            }
        }
        setupSearchBarLayoutChangesListener()
    }

    private fun setupSearchBarLayoutChangesListener() {
        (searchBarSomList?.searchBarTextField?.parent as? View)?.viewTreeObserver?.addOnPreDrawListener {
            context?.run {
                val searchBarContainer = searchBarSomList?.searchBarTextField?.parent as? View
                val horizontalPadding = resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2).toInt()
                val verticalPadding = resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3).toInt()
                if (searchBarContainer?.paddingBottom != verticalPadding || searchBarContainer?.paddingTop != verticalPadding) {
                    bulkActionCheckBoxContainer?.layoutTransition?.disableTransitionType(CHANGING)
                    searchBarContainer?.setPadding(
                            horizontalPadding,
                            verticalPadding,
                            horizontalPadding,
                            verticalPadding)
                    return@addOnPreDrawListener false
                }
            }
            bulkActionCheckBoxContainer?.layoutTransition?.disableTransitionType(CHANGING)
            true
        }
    }

    private fun toggleTvSomListBulkText() {
        context?.run {
            val textResId = if (viewModel.isMultiSelectEnabled) R.string.som_list_multi_select_cancel else R.string.som_list_multi_select
            tvSomListBulk?.text = getString(textResId)
        }
    }

    private fun updateOrderCounter() {
        multiEditViews?.showWithCondition((somListSortFilterTab?.shouldShowBulkAction()?.and(canMultiAcceptOrder) ?: false) && GlobalConfig.isSellerApp())
        context?.run {
            val text = if (viewModel.isMultiSelectEnabled) {
                val checkedCount = adapter.data.filterIsInstance<SomListOrderUiModel>().count { it.isChecked }
                getString(R.string.som_list_order_counter_multi_select_enabled, checkedCount)
            } else {
                getString(R.string.som_list_order_counter, somListSortFilterTab?.getSelectedFilterOrderCount().orZero())
            }
            tvSomListOrderCounter?.text = text
        }
    }

    private fun handleSomDetailActivityResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            when {
                data.hasExtra(RESULT_CONFIRM_SHIPPING) -> handleConfirmShippingResult(data.getStringExtra(RESULT_CONFIRM_SHIPPING))
                data.hasExtra(SomConsts.RESULT_ACCEPT_ORDER) -> {
                    data.getParcelableExtra<SomAcceptOrderResponse.Data.AcceptOrder>(SomConsts.RESULT_ACCEPT_ORDER)?.let { resultAcceptOrder ->
                        onAcceptOrderSuccess(resultAcceptOrder, true)
                    }
                }
                data.hasExtra(SomConsts.RESULT_PROCESS_REQ_PICKUP) -> {
                    data.getParcelableExtra<SomProcessReqPickup.Data.MpLogisticRequestPickup>(SomConsts.RESULT_PROCESS_REQ_PICKUP)?.let { resultProcessReqPickup ->
                        handleRequestPickUpResult(resultProcessReqPickup.listMessage.firstOrNull())
                    }
                }
                data.hasExtra(SomConsts.RESULT_REJECT_ORDER) -> {
                    data.getParcelableExtra<SomRejectOrderResponse.Data.RejectOrder>(SomConsts.RESULT_REJECT_ORDER)?.let { resultRejectOrder ->
                        handleRejectOrderResult(resultRejectOrder, true)
                    }
                }
                data.hasExtra(SomConsts.RESULT_SET_DELIVERED) -> {
                    data.getStringExtra(SomConsts.RESULT_SET_DELIVERED)?.let { message ->
                        onActionCompleted(true, selectedOrderId)
                        showCommonToaster(view, message)
                    }
                }
                data.hasExtra(SomConsts.RESULT_REFRESH_ORDER) -> {
                    if (data.getBooleanExtra(SomConsts.RESULT_REFRESH_ORDER, false)) {
                        onActionCompleted(true, selectedOrderId)
                    }
                }
            }
        }
    }

    private fun handleRejectOrderResult(resultRejectOrder: SomRejectOrderResponse.Data.RejectOrder, shouldRefreshOrder: Boolean) {
        onActionCompleted(shouldRefreshOrder, selectedOrderId)
        showCommonToaster(view, resultRejectOrder.message.firstOrNull())
    }

    private fun handleSomConfirmShippingActivityResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (data.hasExtra(RESULT_CONFIRM_SHIPPING)) {
                handleConfirmShippingResult(data.getStringExtra(RESULT_CONFIRM_SHIPPING))
            }
        }
    }

    private fun handleSomRequestPickUpActivityResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            data.getParcelableExtra<SomProcessReqPickup.Data.MpLogisticRequestPickup>(SomConsts.RESULT_PROCESS_REQ_PICKUP)?.let { resultProcessReqPickup ->
                handleRequestPickUpResult(resultProcessReqPickup.listMessage.firstOrNull())
            }
        }
    }

    private fun handleSomChangeCourierActivityResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            data.getStringExtra(RESULT_CONFIRM_SHIPPING).takeIf { !it.isNullOrBlank() }?.let {
                onActionCompleted(true, selectedOrderId)
                showCommonToaster(view, it)
            }
        }
    }

    private fun handleRequestPickUpResult(message: String?) {
        onActionCompleted(true, selectedOrderId)
        showCommonToaster(view, message)
    }

    private fun handleConfirmShippingResult(message: String?) {
        onActionCompleted(true, selectedOrderId)
        showCommonToaster(view, message)
    }

    private fun toggleBulkAction() {
        viewModel.isMultiSelectEnabled = !viewModel.isMultiSelectEnabled
    }

    protected fun resetOrderSelectedStatus() {
        adapter.data.filterIsInstance<SomListOrderUiModel>().onEach { it.isChecked = false }.run {
            adapter.notifyItemRangeChanged(0, size, Bundle().apply { putBoolean(SomListOrderViewHolder.TOGGLE_SELECTION, true) })
        }
    }

    private fun checkAllOrder() {
        adapter.data.onEach { if (it is SomListOrderUiModel && !isOrderWithCancellationRequest(it)) it.isChecked = true }
        adapter.notifyDataSetChanged()
    }

    private fun toggleBulkActionButtonVisibility() {
        val isAnyCheckedOrder = adapter.data.filterIsInstance<SomListOrderUiModel>().find { it.isChecked } != null
        if (viewModel.isMultiSelectEnabled && isAnyCheckedOrder) {
            animateBulkAcceptOrderButtonEnter()
        } else {
            animateBulkAcceptOrderButtonLeave()
        }
    }

    private fun toggleBulkActionCheckboxVisibility() {
        checkBoxBulkAction?.showWithCondition(viewModel.isMultiSelectEnabled)
    }

    private fun updateBulkActionCheckboxStatus() {
        val groupedOrders = adapter.data
                .filter { it is SomListOrderUiModel && !isOrderWithCancellationRequest(it) }
                .groupBy { (it as SomListOrderUiModel).isChecked }
        val newIndeterminateStatus = groupedOrders[true]?.size.orZero() > 0 && groupedOrders[false]?.size.orZero() > 0
        val newCheckedStatus = groupedOrders[true]?.size.orZero() > 0
        if (newCheckedStatus != checkBoxBulkAction?.isChecked) {
            checkBoxBulkAction?.isChecked = newCheckedStatus
        }
        if (newIndeterminateStatus != checkBoxBulkAction?.getIndeterminate()) {
            checkBoxBulkAction?.setIndeterminate(newIndeterminateStatus)
        }
    }

    private fun showWaitingPaymentOrderListMenuShimmer() {
        if (canDisplayOrderData) {
            som_list_toolbar?.menu?.findItem(R.id.som_list_action_waiting_payment_order_shimmer)?.isVisible = true
            som_list_toolbar?.menu?.findItem(R.id.som_list_action_waiting_payment_order)?.isVisible = false
        }
    }

    private fun showWaitingPaymentOrderListMenu() {
        context?.let {
            som_list_toolbar?.menu?.findItem(R.id.som_list_action_waiting_payment_order_shimmer)?.isVisible = false
            som_list_toolbar?.menu?.findItem(R.id.som_list_action_waiting_payment_order)?.apply {
                icon = DottedNotification(it, R.drawable.ic_som_list_waiting_payment_button_icon, false)
                isVisible = true
            }
        }
    }

    private fun showDottedWaitingPaymentOrderListMenu() {
        context?.let {
            som_list_toolbar?.menu?.findItem(R.id.som_list_action_waiting_payment_order_shimmer)?.isVisible = false
            som_list_toolbar?.menu?.findItem(R.id.som_list_action_waiting_payment_order)?.apply {
                icon = DottedNotification(it, R.drawable.ic_som_list_waiting_payment_button_icon, true)
                isVisible = true
            }
        }
    }

    private fun hideWaitingPaymentOrderListMenu() {
        context?.let {
            som_list_toolbar?.menu?.findItem(R.id.som_list_action_waiting_payment_order_shimmer)?.isVisible = false
            som_list_toolbar?.menu?.findItem(R.id.som_list_action_waiting_payment_order)?.isVisible = false
        }
    }

    private fun showGlobalError(throwable: Throwable) {
        dismissCoachMark()
        somListLoading.gone()
        rvSomList?.gone()
        multiEditViews.gone()
        animateBulkAcceptOrderButtonLeave()
        getSwipeRefreshLayout(view)?.apply {
            isRefreshing = false
            isEnabled = false
        }
        val errorType = when (throwable) {
            is UnknownHostException, is SocketTimeoutException -> GlobalError.NO_CONNECTION
            else -> GlobalError.SERVER_ERROR
        }
        globalErrorSomList.setType(errorType)
        sortFilterSomList.invisible()
        scrollViewErrorState.show()
        errorToaster?.dismiss()
        stopLoadTimeMonitoring()
    }

    private fun showAdminPermissionError() {
        dismissCoachMark()
        somListLoading?.gone()
        rvSomList?.gone()
        multiEditViews?.gone()
        animateBulkAcceptOrderButtonLeave()
        searchBarSomList?.gone()
        shimmerViews?.gone()
        getSwipeRefreshLayout(view)?.apply {
            isRefreshing = false
            isEnabled = false
        }
        sortFilterSomList.gone()
        scrollViewErrorState.gone()
        errorToaster?.dismiss()
        hideWaitingPaymentOrderListMenu()
        somAdminPermissionView?.setUserNotAllowedToViewSom {
            if (GlobalConfig.isSellerApp()) {
                RouteManager.route(context, ApplinkConstInternalSellerapp.SELLER_HOME)
            } else {
                activity?.finish()
            }
        }
    }

    private fun renderTickers(data: List<TickerData>) {
        val activeTickers = data.filter { (it.itemData as? SomListTickerUiModel)?.isActive == true }
                .onEach { it.type = Ticker.TYPE_ANNOUNCEMENT }
        var tickerPagerAdapter = tickerPagerAdapter
        if (tickerPagerAdapter == null) {
            tickerPagerAdapter = TickerPagerAdapter(context, activeTickers)
            tickerPagerAdapter.setPagerDescriptionClickEvent(this)
            this.tickerPagerAdapter = tickerPagerAdapter
        }
        tickerSomList?.addPagerView(tickerPagerAdapter, activeTickers)
        tickerIsReady = activeTickers.isNotEmpty()
        animateOrderTicker(true)
    }

    protected open fun renderOrderList(data: List<SomListOrderUiModel>) {
        skipSearch = false
        if (rvSomList?.visibility != View.VISIBLE) rvSomList?.show()
        // show only if current order list is based on current search keyword
        if (isLoadingInitialData && data.isEmpty()) {
            showEmptyState()
            multiEditViews?.gone()
            toggleBulkActionButtonVisibility()
        } else if (data.firstOrNull()?.searchParam == searchBarSomList.searchBarTextField.text.toString()) {
            if (isLoadingInitialData) {
                (adapter as SomListOrderAdapter).updateOrders(data)
                multiEditViews?.showWithCondition((somListSortFilterTab?.shouldShowBulkAction()?.and(canMultiAcceptOrder) ?: false) && GlobalConfig.isSellerApp())
                toggleTvSomListBulkText()
                toggleBulkActionCheckboxVisibility()
                toggleBulkActionButtonVisibility()
                if (shouldScrollToTop) {
                    shouldScrollToTop = false
                    rvSomList?.addOneTimeGlobalLayoutListener {
                        rvSomList?.smoothScrollToPosition(0)
                    }
                }
                if (coachMark?.currentIndex == newOrderCoachMarkItemPosition || (coachMark?.currentIndex == bulkProcessCoachMarkItemPosition && !multiEditViews.isVisible)) {
                    dismissCoachMark(true)
                }
            } else {
                val lastIndex = adapter.data.size - 1
                adapter.data.getOrNull(lastIndex)?.let { index ->
                    if (index is SomListEmptyStateUiModel) {
                        adapter.data.removeAt(lastIndex)
                        adapter.notifyItemRemoved(lastIndex)
                    }
                }
                (adapter as SomListOrderAdapter).updateOrders(adapter.data.plus(data))
                rvSomList?.post {
                    updateBulkActionCheckboxStatus()
                }
            }
            tryReshowCoachMark()
        }
        updateScrollListenerState(viewModel.hasNextPage())
        isLoadingInitialData = false
    }

    protected open fun onRefreshOrderSuccess(result: OptionalOrderData) {
        val order = result.order
        if (order == null) {
            (adapter as SomListOrderAdapter).removeOrder(result.orderId)
            checkLoadMore()
        } else {
            (adapter as SomListOrderAdapter).updateOrder(order)
        }
        if (adapter.dataSize == 0) {
            multiEditViews?.showWithCondition(adapter.dataSize > 0 && canMultiAcceptOrder)
            toggleBulkActionButtonVisibility()
            viewModel.isMultiSelectEnabled = false
            showEmptyState()
        }
    }

    private fun onRefreshOrderFailed() {
        showToasterError(view, getString(R.string.som_list_failed_refresh_order))
    }

    private fun getFirstNewOrder(orders: List<SomListOrderUiModel>): Int {
        return orders.indexOfFirst {
            it.orderStatusId == SomConsts.STATUS_CODE_ORDER_CREATED && it.buttons.isNotEmpty() && !isOrderWithCancellationRequest(it)
        }
    }

    private fun checkLoadMore() {
        somListLayoutManager?.apply {
            if (findLastVisibleItemPosition() == adapter.dataSize - 1) {
                if (viewModel.hasNextPage()) {
                    endlessRecyclerViewScrollListener.loadMoreNextPage()
                }
            }
        }
    }

    private fun createSomListEmptyStateModel(isTopAdsActive: Boolean): Visitable<SomListAdapterTypeFactory> {
        return if (GlobalConfig.isSellerApp() && !isTopAdsActive && somListSortFilterTab?.isNewOrderFilterSelected() == true &&
                somListSortFilterTab?.isFilterApplied() != true && searchBarSomList.searchBarTextField.text.isEmpty()) {
            SomListEmptyStateUiModel(
                    imageUrl = SomConsts.SOM_LIST_EMPTY_STATE_NO_FILTER_ILLUSTRATION,
                    title = getString(R.string.empty_peluang_title),
                    description = getString(R.string.empty_peluang_desc_non_topads_no_filter),
                    buttonText = getString(R.string.btn_cek_peluang_non_topads),
                    buttonAppLink = ApplinkConstInternalTopAds.TOPADS_CREATE_ADS,
                    showButton = true
            )
        } else if (somListSortFilterTab?.isFilterApplied() == true || searchBarSomList.searchBarTextField.text.isNotEmpty()) {
            SomListEmptyStateUiModel(
                    imageUrl = SomConsts.SOM_LIST_EMPTY_STATE_WITH_FILTER_ILLUSTRATION,
                    title = getString(R.string.som_list_empty_state_not_found_title)
            )
        } else {
            SomListEmptyStateUiModel(
                    imageUrl = SomConsts.SOM_LIST_EMPTY_STATE_NO_FILTER_ILLUSTRATION,
                    title = getString(R.string.empty_peluang_title),
                    description = getString(R.string.som_list_empty_state_description_no_topads_no_filter)
            )
        }
    }

    private fun goToWaitingPaymentOrderListPage() {
        context?.run {
            Intent(this, WaitingPaymentOrderActivity::class.java).apply {
                startActivity(this)
            }
        }
    }

    private fun refreshOrderList() {
        if (viewModel.isMultiSelectEnabled) {
            viewModel.isMultiSelectEnabled = false
            resetOrderSelectedStatus()
            toggleBulkActionButtonVisibility()
            toggleBulkActionCheckboxVisibility()
            checkBoxBulkAction.isChecked = false
            checkBoxBulkAction.setIndeterminate(false)
        }
        scrollViewErrorState.gone()
        isLoadingInitialData = true
        loadOrderList()
    }

    private fun showToasterError(
            view: View?,
            message: String = getString(R.string.som_list_error_some_information_cannot_be_loaded),
            buttonMessage: String = getString(R.string.btn_reload),
            canRetry: Boolean = true) {
        view?.let {
            if (canRetry) {
                errorToaster = Toaster.build(
                        it,
                        message,
                        Toaster.LENGTH_INDEFINITE,
                        Toaster.TYPE_ERROR,
                        buttonMessage,
                        View.OnClickListener {
                            refreshFailedRequests()
                        })
            } else {
                errorToaster = Toaster.build(
                        it,
                        message,
                        Toaster.LENGTH_INDEFINITE,
                        Toaster.TYPE_ERROR,
                        getString(R.string.som_list_button_ok),
                        View.OnClickListener {
                            errorToaster?.dismiss()
                        })
            }
        }
        if (errorToaster?.isShown == false) {
            errorToaster?.show()
        }
    }

    private fun showCommonToaster(view: View?, message: String?, toasterType: Int = Toaster.TYPE_NORMAL) {
        message?.run {
            view?.let {
                commonToaster?.dismiss()
                commonToaster = Toaster.build(
                        it,
                        message,
                        Toaster.LENGTH_SHORT,
                        toasterType,
                        getString(R.string.som_list_button_ok))
                commonToaster?.show()
            }
        }
    }

    private fun refreshFailedRequests() {
        if (viewModel.waitingPaymentCounterResult.value is Fail) {
            showWaitingPaymentOrderListMenuShimmer()
            viewModel.getWaitingPaymentCounter()
        }
        if (viewModel.tickerResult.value is Fail) {
            tickerSomList?.gone()
            viewModel.getTickers()
        }
        if (viewModel.topAdsCategoryResult.value is Fail) {
            loadTopAdsCategory()
        }
        if (viewModel.containsFailedRefreshOrder) {
            viewModel.retryRefreshSelectedOrder()
            getSwipeRefreshLayout(view)?.apply {
                isEnabled = true
                isRefreshing = true
            }
        }
    }

    private fun rejectOrder(orderRejectRequestParam: SomRejectRequestParam) {
        activity?.resources?.let {
            val invoice = getOrderBy(orderRejectRequestParam.orderId)
            viewModel.rejectOrder(orderRejectRequestParam, invoice)
        }
    }

    private fun rejectCancelOrder(orderId: String) {
        if (orderId.isNotBlank()) {
            val invoice = getOrderBy(orderId)
            viewModel.rejectCancelOrder(orderId, invoice)
        }
    }

    private fun showBulkAcceptOrderBottomSheet() {
        view?.let {
            if (it is ViewGroup) {
                if (somListBulkProcessOrderBottomSheet == null) {
                    somListBulkProcessOrderBottomSheet = SomListBulkProcessOrderBottomSheet(it.context)
                }
                somListBulkProcessOrderBottomSheet?.let { bottomSheet ->
                    val items = arrayListOf<Visitable<SomListBulkProcessOrderTypeFactory>>().apply {
                        add(SomListBulkProcessOrderDescriptionUiModel(getString(R.string.som_list_bottom_sheet_bulk_accept_order_description), false))
                        addAll(getOrdersProducts(adapter.data.filterIsInstance<SomListOrderUiModel>().filter { it.isChecked }))
                    }
                    bottomSheet.init(it)
                    bottomSheet.setTitle(getString(R.string.som_list_bulk_accept_order_button))
                    bottomSheet.setItems(items)
                    bottomSheet.showButtonAction()
                    bottomSheet.setListener(this@SomListFragment)
                    bottomSheet.hideKnob()
                    bottomSheet.showCloseButton()
                    bottomSheet.show()
                }
                return
            }
        }
        showCommonToaster(view, "Terjadi kesalahan, silahkan coba lagi.")
    }

    private fun showBulkProcessOrderBottomSheet() {
        view?.let { fragmentView ->
            if (fragmentView is ViewGroup) {
                if (somListBulkProcessOrderBottomSheet == null) {
                    somListBulkProcessOrderBottomSheet = SomListBulkProcessOrderBottomSheet(fragmentView.context)
                }
                somListBulkProcessOrderBottomSheet?.let { bottomSheet ->
                    val items = arrayListOf<Visitable<SomListBulkProcessOrderTypeFactory>>().apply {
                        add(SomListBulkProcessOrderMenuItemUiModel(
                                KEY_PRINT_AWB,
                                getString(R.string.som_list_bulk_print_button),
                                true
                        ))
                    }
                    bottomSheet.init(fragmentView)
                    bottomSheet.setTitle(getString(R.string.som_list_bulk_confirm_shipping_order_button))
                    bottomSheet.setItems(items)
                    bottomSheet.hideButtonAction()
                    bottomSheet.setListener(this@SomListFragment)
                    bottomSheet.hideKnob()
                    bottomSheet.showCloseButton()
                    bottomSheet.show()
                }
                return
            }
        }
        showCommonToaster(view, "Terjadi kesalahan, silahkan coba lagi.")
    }

    private fun getOrdersProducts(orders: List<SomListOrderUiModel>): List<SomListBulkProcessOrderProductUiModel> {
        val products = orders.map { it.orderProduct }.flatten().groupBy { it.productId }

        return products.filter { it.value.isNotEmpty() }.map {
            SomListBulkProcessOrderProductUiModel(
                    productName = it.value.first().productName,
                    picture = it.value.first().picture,
                    amount = it.value.size
            )
        }
    }

    private fun setInitialOrderListParams() {
        val orderTypes = if (filterOrderType == 0) {
            mutableSetOf()
        } else {
            somListSortFilterTab?.addCounter(1)
            mutableSetOf(filterOrderType)
        }
        setDefaultSortByValue()
        viewModel.setOrderTypeFilter(orderTypes)
        val searchParam = arguments?.getString(QUERY_PARAM_SEARCH).orEmpty()
        viewModel.setSearchParam(searchParam)
    }

    private fun Throwable.showErrorToaster() {
        if (!scrollViewErrorState.isVisible) {
            if (this is UnknownHostException || this is SocketTimeoutException) {
                showNoInternetConnectionToaster()
            } else {
                showServerErrorToaster()
            }
        }
    }

    private fun showNoInternetConnectionToaster() {
        showToasterError(view, getString(R.string.som_error_message_no_internet_connection), canRetry = false)
    }

    private fun showServerErrorToaster() {
        showToasterError(view, getString(R.string.som_error_message_server_fault), canRetry = false)
    }

    private fun getVisiblePercent(v: View): Int {
        if (v.isShown) {
            val r = Rect()
            val isVisible = v.getGlobalVisibleRect(r)
            return if (isVisible) {
                0
            } else {
                -1
            }
        }
        return -1
    }

    private fun shouldReloadOrderListImmediately(): Boolean = tabActive.isBlank() || tabActive == STATUS_ALL_ORDER

    override fun onClickShowOrderFilter(
            filterData: SomListGetOrderListParam, somFilterUiModelList: List<SomFilterUiModel>,
            idFilter: String, filterDate: String, isRequestCancelFilterApplied: Boolean) {
        this.filterDate = filterDate
        this.filterOrderType = if (isRequestCancelFilterApplied) FILTER_CANCELLATION_REQUEST else 0
        this.shouldScrollToTop = true
        isLoadingInitialData = true
        somListSortFilterTab?.updateSomListFilterUi(somFilterUiModelList)
        somListSortFilterTab?.updateCounterSortFilter(filterDate)
        val selectedStatusFilterKey = somFilterUiModelList.find {
            it.nameFilter == SomConsts.FILTER_STATUS_ORDER
        }?.somFilterData?.find {
            it.isSelected
        }?.key

        if (tabActive != selectedStatusFilterKey.orEmpty()) {
            wasChangingTab = true
        }

        tabActive = selectedStatusFilterKey.orEmpty()
        viewModel.updateGetOrderListParams(filterData)
        loadFilters(showShimmer = false, loadOrders = true)
        if (shouldReloadOrderListImmediately()) {
            loadOrderList()
        } else {
            getSwipeRefreshLayout(view)?.isRefreshing = true
        }
        setDefaultSortByValue()
    }

    override fun onClickOverlayBottomSheet(filterCancelWrapper: SomFilterCancelWrapper) {
        somListSortFilterTab?.updateSomListFilterUi(filterCancelWrapper.somFilterUiModelList)
        val orderListParam = viewModel.getDataOrderListParams()
        orderListParam.statusList = filterCancelWrapper.orderStatusIdList
        viewModel.updateGetOrderListParams(orderListParam)
        this.filterDate = filterCancelWrapper.filterDate
        this.filterOrderType = if (filterCancelWrapper.requestCancelFilterApplied) FILTER_CANCELLATION_REQUEST else 0
    }

    protected open fun setCoachMarkStepListener() {
        coachMark?.setStepListener(object : CoachMark2.OnStepListener {
            override fun onStep(currentIndex: Int, coachMarkItem: CoachMark2Item) {
                when (currentIndex) {
                    newOrderCoachMarkItemPosition -> {
                        shouldShowCoachMark = true
                        coachMark?.isDismissed = true
                        coachMarkIndexToShow = newOrderCoachMarkItemPosition
                        if (tabActive.isNotBlank() && tabActive != STATUS_ALL_ORDER) {
                            viewModel.resetGetOrderListParam()
                            somListSortFilterTab?.clear()
                            skipSearch = true
                            searchBarSomList.searchBarTextField.setText("")
                            somListSortFilterTab?.unselectCurrentStatusFilter()
                        } else {
                            rvSomList?.post {
                                reshowNewOrderCoachMark()
                            }
                        }
                    }
                    waitingPaymentCoachMarkItemPosition, bulkProcessCoachMarkItemPosition -> {
                        if (currentIndex == bulkProcessCoachMarkItemPosition && tabActive == STATUS_NEW_ORDER) return
                        if (currentIndex == waitingPaymentCoachMarkItemPosition && tabActive == STATUS_ALL_ORDER) return
                        viewModel.resetGetOrderListParam()
                        somListSortFilterTab?.clear()
                        skipSearch = true
                        searchBarSomList.searchBarTextField.setText("")
                        coachMarkIndexToShow = currentIndex
                        // if user press "Lanjut" after waiting payment coachmark, auto select new order filter, if user press "Balik"
                        // auto deselect new order to show all order
                        val targetStatusFilter = if (currentIndex == bulkProcessCoachMarkItemPosition && tabActive != STATUS_NEW_ORDER) {
                            STATUS_NEW_ORDER
                        } else ""
                        if (targetStatusFilter.isNotBlank()) {
                            shouldShowCoachMark = true
                            coachMark?.isDismissed = true
                            viewModel.filterResult.value?.let {
                                if (it is Success) {
                                    it.data.statusList.find { it.key == targetStatusFilter }?.let {
                                        somListSortFilterTab?.selectTab(it)
                                        it.isChecked = true
                                        onTabClicked(it, true)
                                    }
                                }
                            }
                        } else {
                            somListSortFilterTab?.unselectCurrentStatusFilter()
                        }
                    }
                }
            }
        })
    }

    protected open fun createCoachMarkItems(firstNewOrderView: View): ArrayList<CoachMark2Item> {
        return arrayListOf<CoachMark2Item>().apply {
            add(CoachMark2Item(firstNewOrderView, getString(R.string.som_list_coachmark_new_order_card_title), getString(R.string.som_list_coachmark_new_order_card_description)))
            add(CoachMark2Item(sortFilterSomList, getString(R.string.som_list_coachmark_sort_filter_title), getString(R.string.som_list_coachmark_sort_filter_description)))
            if (som_list_toolbar?.menu?.findItem(R.id.som_list_action_waiting_payment_order)?.isVisible == true) {
                activity?.findViewById<View>(R.id.som_list_action_waiting_payment_order)?.let {
                    add(CoachMark2Item(it, getString(R.string.som_list_coachmark_waiting_payment_title), getString(R.string.som_list_coachmark_waiting_payment_description)))
                }
            }
            if (GlobalConfig.isSellerApp()) {
                add(CoachMark2Item(tvSomListBulk, getString(R.string.som_list_coachmark_multi_select_title), getString(R.string.som_list_coachmark_multi_select_description)))
            }
        }
    }

    private fun dismissCoachMark(removeScrollListener: Boolean = true) {
        if (coachMark?.currentIndex != -1 && coachMark?.isDismissed == false) {
            shouldShowCoachMark = true
            if (removeScrollListener)
                rvSomList?.removeOnScrollListener(recyclerViewScrollListener)
            coachMarkIndexToShow = coachMark?.currentIndex.orZero()
            coachMark?.dismissCoachMark()
            coachMark?.isDismissed = true
        }
    }

    private fun reshowNewOrderCoachMark() {
        if (scrollViewErrorState?.isVisible == false && shouldShowCoachMark && coachMarkIndexToShow == newOrderCoachMarkItemPosition &&
                (tabActive.isBlank() || tabActive == STATUS_ALL_ORDER) &&
                somListSortFilterTab?.isFilterApplied() != true && searchBarSomList?.searchBarTextField?.text.isNullOrBlank()) {
            val firstNewOrderPosition = getFirstNewOrder(adapter.data.filterIsInstance<SomListOrderUiModel>())
            if (firstNewOrderPosition != -1) {
                rvSomList?.stopScroll()
                somListLayoutManager?.scrollToPositionWithOffset(firstNewOrderPosition, 0)
                rvSomList?.post {
                    somListLayoutManager?.findViewByPosition(firstNewOrderPosition)?.findViewById<UnifyButton>(R.id.btnQuickAction)?.let {
                        if (getVisiblePercent(it) == 0) {
                            CoachMarkPreference.setShown(it.context, SHARED_PREF_NEW_SOM_LIST_COACH_MARK, true)
                            rvSomList?.clearOnScrollListeners()
                            rvSomList?.addOnScrollListener(recyclerViewScrollListener)
                            currentNewOrderWithCoachMark = firstNewOrderPosition
                            shouldShowCoachMark = false
                            val coachMarkItems = createCoachMarkItems(it)
                            coachMark?.isDismissed = false
                            coachMark?.showCoachMark(step = coachMarkItems, index = coachMarkIndexToShow)
                        }
                    }
                }
            }
        }
    }

    protected open fun reshowStatusFilterCoachMark() {
        if (shouldShowFilterCoachMark()) {
            createCoachMarkItems(rvSomList).run {
                if (activity?.isFinishing != false) return
                if (size == coachMarkItemCount) {
                    currentNewOrderWithCoachMark = -1
                    coachMark?.isDismissed = false
                    shouldShowCoachMark = false
                    coachMark?.showCoachMark(this, index = coachMarkIndexToShow)
                } else {
                    tryReshowCoachMark()
                }
            }
        }
    }

    protected open fun reshowWaitingPaymentOrderListCoachMark() {
        if (shouldShowWaitingPaymentCoachMark(viewModel.waitingPaymentCounterResult.value)) {
            createCoachMarkItems(rvSomList).run {
                if (activity?.isFinishing != false) return
                if (size == coachMarkItemCount) {
                    currentNewOrderWithCoachMark = -1
                    coachMark?.isDismissed = false
                    shouldShowCoachMark = false
                    coachMark?.showCoachMark(this, index = coachMarkIndexToShow)
                } else {
                    tryReshowCoachMark()
                }
            }
        }
    }

    protected open fun reshowBulkAcceptOrderCoachMark() {
        if (shouldShowBulkAcceptOrderCoachMark()) {
            createCoachMarkItems(rvSomList).run {
                if (activity?.isFinishing != false) return
                if (size == coachMarkItemCount && GlobalConfig.isSellerApp()) {
                    currentNewOrderWithCoachMark = -1
                    coachMark?.isDismissed = false
                    shouldShowCoachMark = false
                    coachMark?.showCoachMark(this, index = coachMarkIndexToShow)
                    return
                } else {
                    tryReshowCoachMark()
                }
            }
        }
    }

    private fun View?.animateSlide(from: Float, to: Float): ValueAnimator {
        val animator = ValueAnimator.ofFloat(from, to)
        animator.duration = BUTTON_ENTER_LEAVE_ANIMATION_DURATION
        animator.addUpdateListener { valueAnimator ->
            context?.let {
                this?.translationY = (valueAnimator.animatedValue as? Float).orZero()
            }
        }
        animator.start()
        return animator
    }

    private fun animateBulkAcceptOrderButtonEnter() {
        if (bulkAcceptButtonLeaveAnimation?.isRunning == true) bulkAcceptButtonLeaveAnimation?.cancel()
        btnBulkAction.text = when (somListSortFilterTab?.getSelectedFilterStatus()) {
            STATUS_NEW_ORDER -> getString(R.string.som_list_bulk_accept_order_button)
            KEY_CONFIRM_SHIPPING -> getString(R.string.som_list_bulk_confirm_shipping_order_button)
            else -> ""
        }
        containerBtnBulkAction?.visible()
        bulkAcceptButtonEnterAnimation = containerBtnBulkAction?.animateSlide(containerBtnBulkAction?.translationY.orZero(), 0f)
    }

    private fun animateBulkAcceptOrderButtonLeave() {
        if (bulkAcceptButtonEnterAnimation?.isRunning == true) bulkAcceptButtonEnterAnimation?.cancel()
        bulkAcceptButtonLeaveAnimation = containerBtnBulkAction?.animateSlide(containerBtnBulkAction?.translationY.orZero(),
                containerBtnBulkAction?.height?.toFloat() ?: 0f)
        bulkAcceptButtonLeaveAnimation?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {}

            override fun onAnimationEnd(animation: Animator?) {
                containerBtnBulkAction?.gone()
                bulkAcceptButtonLeaveAnimation?.removeListener(this)
            }

            override fun onAnimationCancel(animation: Animator?) {
                bulkAcceptButtonLeaveAnimation?.removeListener(this)
            }

            override fun onAnimationStart(animation: Animator?) {}
        })
    }

    private fun animateOrderTicker(isEnter: Boolean) {
        Handler().postDelayed({
            val shouldAnimateTicker = (isEnter && tickerIsReady && (tickerSomList?.visibility == View.INVISIBLE || tickerSomList?.visibility == View.GONE)) || !isEnter
            if (adapter.data.isNotEmpty() && shouldAnimateTicker) {
                val enterValue: Float
                val exitValue: Float
                if (isEnter) {
                    enterValue = 0f
                    exitValue = 1f
                } else {
                    enterValue = 1f
                    exitValue = 0f
                }
                tickerSomList?.run {
                    val height = height.toFloat().orZero()
                    translationY = enterValue * height

                    show()

                    val animator = ValueAnimator.ofFloat(enterValue, exitValue).apply {
                        duration = TICKER_ENTER_LEAVE_ANIMATION_DURATION
                        addUpdateListener { valueAnimator ->
                            context?.let {
                                val animValue = (valueAnimator.animatedValue as? Float).orZero()
                                val translation = animValue * height
                                translationY = translation
                                alpha = animValue
                                translateTickerConstrainedLayout(translation)
                            }
                        }
                        addListener(object : Animator.AnimatorListener {
                            override fun onAnimationStart(p0: Animator?) {}

                            override fun onAnimationEnd(p0: Animator?) {
                                tickerIsReady = false
                                if (!isEnter) {
                                    this@run.invisible()
                                }
                            }

                            override fun onAnimationCancel(p0: Animator?) {}

                            override fun onAnimationRepeat(p0: Animator?) {}
                        })
                    }

                    animator.start()
                }
            }
        }, TICKER_ENTER_LEAVE_ANIMATION_DELAY)
    }

    private fun translateTickerConstrainedLayout(translation: Float) {
        searchBarSomList?.translationY = translation
        sortFilterSomList?.translationY = translation
        sortFilterShimmer1?.translationY = translation
        sortFilterShimmer2?.translationY = translation
        sortFilterShimmer3?.translationY = translation
        sortFilterShimmer4?.translationY = translation
        sortFilterShimmer5?.translationY = translation
        bulkActionCheckBoxContainer?.translationY = translation
        dividerMultiSelect?.translationY = translation
        val params = (swipeRefreshLayoutSomList?.layoutParams as? ViewGroup.MarginLayoutParams)
        params?.topMargin = translation.toInt()
        swipeRefreshLayoutSomList?.layoutParams = params
        containerBtnBulkAction?.translationY = translation
        scrollViewErrorState?.translationY = translation
        somAdminPermissionView?.translationY = translation
    }

    private fun refreshOrdersOnTabClicked(shouldScrollToTop: Boolean, refreshFilter: Boolean) {
        this.shouldScrollToTop = shouldScrollToTop
        if (refreshFilter) {
            loadFilters(showShimmer = false, loadOrders = true)
        }
        if (shouldReloadOrderListImmediately() || !refreshFilter) {
            refreshOrderList()
        } else {
            getSwipeRefreshLayout(view)?.isRefreshing = true
        }
    }

    protected open fun tryReshowCoachMark() {
        view?.postDelayed({
            reshowNewOrderCoachMark()
            reshowStatusFilterCoachMark()
            reshowWaitingPaymentOrderListCoachMark()
            reshowBulkAcceptOrderCoachMark()
        }, DELAY_COACHMARK)
    }

    private fun getActivityPltPerformanceMonitoring() {
        somListLoadTimeMonitoring = (activity as? SomListLoadTimeMonitoringActivity)?.getSomListLoadTimeMonitoring()
    }

    private fun stopLoadTimeMonitoring() {
        somListLoadTimeMonitoring?.stopRenderPerformanceMonitoring()
        (activity as? SomListLoadTimeMonitoringActivity)?.loadTimeMonitoringListener?.onStopPltMonitoring()
    }

    private fun hideKeyboard() {
        val context = context
        val view = view
        if (view != null && context != null) {
            KeyboardHandler.DropKeyboard(context, view)
        }
    }

    private fun onFailedValidateOrder() {
        showToasterError(view, getString(R.string.som_error_validate_order), SomConsts.ACTION_OK, canRetry = false)
    }

    private fun onSuccessValidateOrder(valid: Boolean) {
        val pendingAction = pendingAction ?: return
        if (valid) {
            pendingAction.action.invoke()
        } else {
            context?.let { context ->
                val somOrderHasCancellationRequestDialog = somOrderHasCancellationRequestDialog ?: SomOrderHasRequestCancellationDialog(context)
                this.somOrderHasCancellationRequestDialog = somOrderHasCancellationRequestDialog
                somOrderHasCancellationRequestDialog.apply {
                    setupActionButton(pendingAction.actionName, pendingAction.action)
                    setupGoToOrderDetailButton {
                        goToSomOrderDetail(this@SomListFragment, pendingAction.orderId)
                    }
                    show()
                }
            }
        }
    }

    private fun updateToolbarMenu() {
        if (canDisplayOrderData) {
            val waitingPaymentCounterResult = viewModel.waitingPaymentCounterResult.value
            if (waitingPaymentCounterResult is Success) {
                if (!isWaitingPaymentOrderPageOpened && waitingPaymentCounterResult.data.amount > 0) {
                    showDottedWaitingPaymentOrderListMenu()
                } else {
                    showWaitingPaymentOrderListMenu()
                }
            } else {
                showWaitingPaymentOrderListMenuShimmer()
            }
        } else {
            hideWaitingPaymentOrderListMenu()
        }
    }

    private fun isOrderWithCancellationRequest(order: SomListOrderUiModel) = order.cancelRequest == 1 && order.cancelRequestStatus != 0

    private fun setupToolbar() {
        activity?.run {
            (this as? AppCompatActivity)?.run {
                supportActionBar?.hide()
                som_list_toolbar?.inflateMenu(R.menu.menu_som_list)
                som_list_toolbar?.title = getString(R.string.title_som_list)
                som_list_toolbar?.isShowBackButton = showBackButton()
                som_list_toolbar?.setOnMenuItemClickListener(this@SomListFragment)
                som_list_toolbar?.setNavigationOnClickListener {
                    onBackPressed()
                }
                updateToolbarMenu()
                tryReshowCoachMark()
            }
        }
    }

    private fun showBackButton(): Boolean = !GlobalConfig.isSellerApp()

    protected fun dismissBottomSheets() {
        childFragmentManager.fragments.forEach {
            if (it is BottomSheetUnify && it !is SomFilterBottomSheet) it.dismiss()
        }
        somListBulkProcessOrderBottomSheet?.dismiss()
        orderRequestCancelBottomSheet?.dismiss()
        somOrderEditAwbBottomSheet?.dismiss()
    }

    protected open fun loadAllInitialData() {
        viewModel.isMultiSelectEnabled = false
        resetOrderSelectedStatus()
        isLoadingInitialData = true
        somListLoadTimeMonitoring?.startNetworkPerformanceMonitoring()
        loadTopAdsCategory()
        loadTickers()
        loadWaitingPaymentOrderCounter()
        loadFilters(loadOrders = true)
        if (shouldReloadOrderListImmediately()) {
            loadOrderList()
        }
    }

    protected open fun onReceiveRefreshOrderRequest(orderId: String, invoice: String) {
        viewModel.refreshSelectedOrder(orderId, invoice)
    }

    protected open fun shouldShowFilterCoachMark() = scrollViewErrorState?.isVisible == false &&
            shouldShowCoachMark && coachMarkIndexToShow == filterChipCoachMarkItemPosition &&
            sortFilterSomList?.isVisible == true && rvSomList != null

    protected open fun shouldShowWaitingPaymentCoachMark(waitingPaymentOrderListCountResult: Result<WaitingPaymentCounter>?) =
            scrollViewErrorState?.isVisible == false && shouldShowCoachMark && rvSomList != null &&
                    coachMarkIndexToShow == waitingPaymentCoachMarkItemPosition && waitingPaymentOrderListCountResult is Success

    protected open fun shouldShowBulkAcceptOrderCoachMark() = scrollViewErrorState?.isVisible == false &&
            shouldShowCoachMark && rvSomList != null && coachMarkIndexToShow == bulkProcessCoachMarkItemPosition &&
            tvSomListBulk?.isVisible == true && tabActive == SomConsts.STATUS_NEW_ORDER

    protected open fun onSuccessGetFilter(result: Success<SomListFilterUiModel>, realtimeDataChangeCount: Int): Int {
        /* apply result only if:
           1. First filter data (cache or cloud)
           2. Any filter data that is not from cache
         */
        if (realtimeDataChangeCount == 0 || !result.data.fromCache) {
            selectFilterTab(result, realtimeDataChangeCount)
            somListSortFilterTab?.show(result.data)
            updateOrderCounter()
        }
        return if (!result.data.fromCache) realtimeDataChangeCount + 1 else realtimeDataChangeCount
    }
}