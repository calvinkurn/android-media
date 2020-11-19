package com.tokopedia.sellerorder.list.presentation.fragments

import android.animation.Animator
import android.animation.LayoutTransition.CHANGING
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.view.menu.MenuBuilder
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.listener.EndlessLayoutManagerListener
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.applink.order.DeeplinkMapperOrder.FILTER_CANCELLATION_REQUEST
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.coachmark.CoachMarkPreference
import com.tokopedia.config.GlobalConfig
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.SomComponentInstance
import com.tokopedia.sellerorder.analytics.SomAnalytics
import com.tokopedia.sellerorder.common.domain.model.SomAcceptOrderResponse
import com.tokopedia.sellerorder.common.domain.model.SomRejectOrderResponse
import com.tokopedia.sellerorder.common.domain.model.SomRejectRequestParam
import com.tokopedia.sellerorder.common.errorhandler.SomErrorHandler
import com.tokopedia.sellerorder.common.presenter.bottomsheet.SomOrderEditAwbBottomSheet
import com.tokopedia.sellerorder.common.presenter.bottomsheet.SomOrderRequestCancelBottomSheet
import com.tokopedia.sellerorder.common.presenter.model.PopUp
import com.tokopedia.sellerorder.common.presenter.model.Roles
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_ORDER_TYPE
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_STATUS_ID
import com.tokopedia.sellerorder.common.util.SomConsts.FROM_WIDGET_TAG
import com.tokopedia.sellerorder.common.util.SomConsts.TAB_ACTIVE
import com.tokopedia.sellerorder.common.util.SomConsts.TAB_STATUS
import com.tokopedia.sellerorder.common.util.Utils
import com.tokopedia.sellerorder.filter.presentation.bottomsheet.SomFilterBottomSheet
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterCancelWrapper
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterUiModel
import com.tokopedia.sellerorder.list.di.DaggerSomListComponent
import com.tokopedia.sellerorder.list.domain.mapper.FilterResultMapper
import com.tokopedia.sellerorder.list.domain.model.SomListGetOrderListParam
import com.tokopedia.sellerorder.list.presentation.adapter.SomListOrderAdapter
import com.tokopedia.sellerorder.list.presentation.adapter.typefactories.SomListAdapterTypeFactory
import com.tokopedia.sellerorder.list.presentation.adapter.viewholders.SomListOrderEmptyViewHolder
import com.tokopedia.sellerorder.list.presentation.adapter.viewholders.SomListOrderViewHolder
import com.tokopedia.sellerorder.list.presentation.bottomsheets.SomListBulkAcceptOrderBottomSheet
import com.tokopedia.sellerorder.list.presentation.dialogs.SomListBulkActionDialog
import com.tokopedia.sellerorder.list.presentation.filtertabs.SomListSortFilterTab
import com.tokopedia.sellerorder.list.presentation.models.SomListEmptyStateUiModel
import com.tokopedia.sellerorder.list.presentation.models.SomListFilterUiModel
import com.tokopedia.sellerorder.list.presentation.models.SomListOrderUiModel
import com.tokopedia.sellerorder.list.presentation.models.SomListTickerUiModel
import com.tokopedia.sellerorder.list.presentation.navigator.SomListNavigator.REQUEST_CONFIRM_REQUEST_PICKUP
import com.tokopedia.sellerorder.list.presentation.navigator.SomListNavigator.REQUEST_CONFIRM_SHIPPING
import com.tokopedia.sellerorder.list.presentation.navigator.SomListNavigator.REQUEST_DETAIL
import com.tokopedia.sellerorder.list.presentation.navigator.SomListNavigator.goToConfirmShippingPage
import com.tokopedia.sellerorder.list.presentation.navigator.SomListNavigator.goToRequestPickupPage
import com.tokopedia.sellerorder.list.presentation.navigator.SomListNavigator.goToSomOrderDetail
import com.tokopedia.sellerorder.list.presentation.navigator.SomListNavigator.goToTrackingPage
import com.tokopedia.sellerorder.list.presentation.viewmodels.SomListViewModel
import com.tokopedia.sellerorder.list.presentation.widget.DottedNotification
import com.tokopedia.sellerorder.requestpickup.data.model.SomProcessReqPickup
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.activity.WaitingPaymentOrderActivity
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_som_list.*
import kotlinx.coroutines.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.math.abs

class SomListFragment : BaseListFragment<Visitable<SomListAdapterTypeFactory>,
        SomListAdapterTypeFactory>(), SomListSortFilterTab.SomListSortFilterTabClickListener,
        TickerCallback, TickerPagerCallback, SearchInputView.Listener,
        SomListOrderViewHolder.SomListOrderItemListener, CoroutineScope,
        SomListBulkAcceptOrderBottomSheet.SomListBulkAcceptOrderBottomSheetListener,
        SomFilterBottomSheet.SomFilterFinishListener,
        SomListOrderEmptyViewHolder.SomListEmptyStateListener {

    companion object {
        private const val DELAY_SEARCH = 500L
        private const val DELAY_COACHMARK = 500L
        private const val BUTTON_ENTER_LEAVE_ANIMATION_DURATION = 300L

        private const val TAG_BOTTOM_SHEET_BULK_ACCEPT = "bulkAcceptBottomSheet"

        private const val KEY_LAST_ACTIVE_FILTER = "lastActiveFilter"
        private const val KEY_LAST_SELECTED_ORDER_ID = "lastSelectedOrderId"

        private const val SHARED_PREF_NEW_SOM_LIST_COACH_MARK = "newSomListCoachMark"

        private val allowedRoles = listOf(Roles.MANAGE_SHOPSTATS, Roles.MANAGE_INBOX, Roles.MANAGE_TA, Roles.MANAGE_TX)

        @JvmStatic
        fun newInstance(bundle: Bundle): SomListFragment {
            return SomListFragment().apply {
                arguments = Bundle().apply {
                    putString(FILTER_STATUS_ID, bundle.getString(FILTER_STATUS_ID))
                    putBoolean(FROM_WIDGET_TAG, bundle.getBoolean(FROM_WIDGET_TAG))
                    putString(TAB_ACTIVE, bundle.getString(TAB_ACTIVE))
                    putString(TAB_STATUS, bundle.getString(TAB_STATUS))
                    putInt(FILTER_ORDER_TYPE, bundle.getInt(FILTER_ORDER_TYPE))
                }
            }
        }
    }

    private val masterJob = SupervisorJob()

    private val viewModel: SomListViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(SomListViewModel::class.java)
    }

    private val somListSortFilterTab: SomListSortFilterTab by lazy {
        SomListSortFilterTab(sortFilterSomList, this)
    }

    private val coachMark: CoachMark2? by lazy {
        context?.let { CoachMark2(it) }
    }

    private val somListLayoutManager by lazy { rvSomList.layoutManager as? LinearLayoutManager }

    private val recyclerViewScrollListener: RecyclerView.OnScrollListener by lazy {
        object : RecyclerView.OnScrollListener() {
            // to handle new order card coachmark (coachmark need to scroll along with the recyclerview and gone when new order card gone off screen)
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (coachMark?.currentIndex == 0) {
                    if (coachMark?.isDismissed == true && abs(dy) <= 100) {
                        reshowNewOrderCoachMark(dy < 0)
                    } else if (coachMark?.isDismissed == false) {
                        somListLayoutManager?.run {
                            val firstVisibleIndex = findFirstVisibleItemPosition()
                            val lastVisibleIndex = findLastVisibleItemPosition()
                            val currentNewOrderQuickActionButton = findViewByPosition(currentNewOrderWithCoachMark)?.findViewById<View>(R.id.btnQuickAction)
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
            }

            private fun reshowNewOrderCoachMark(isReversed: Boolean) {
                somListLayoutManager?.run {
                    val firstVisibleIndex = findFirstVisibleItemPosition()
                    val lastVisibleIndex = findLastVisibleItemPosition()
                    val visibleRange = firstVisibleIndex..lastVisibleIndex
                    (visibleRange.takeIf { !isReversed } ?: visibleRange.reversed()).forEach {
                        val order = adapter.data.getOrNull(it)
                        if (order is SomListOrderUiModel && order.orderStatusId == SomConsts.STATUS_CODE_ORDER_CREATED &&
                                order.buttons.firstOrNull()?.key == SomConsts.KEY_ACCEPT_ORDER && order.cancelRequest == 0) {
                            findViewByPosition(it)?.findViewById<View>(R.id.btnQuickAction)?.takeIf {
                                it.isVisible
                            }?.let { quickActionButton ->
                                if (getVisiblePercent(quickActionButton) == 0) {
                                    quickActionButton.post {
                                        currentNewOrderWithCoachMark = it
                                        coachMark?.showCoachMark(ArrayList(createCoachMarkItems(quickActionButton)), index = coachMarkIndexToShow)
                                        coachMark?.isDismissed = false
                                        shouldShowCoachMark = false
                                    }
                                    return@reshowNewOrderCoachMark
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    private var bulkAcceptButtonEnterAnimation: ValueAnimator? = null
    private var bulkAcceptButtonLeaveAnimation: ValueAnimator? = null
    private var isWaitingPaymentOrderPageOpened: Boolean = false
    private var isRefreshingSelectedOrder: Boolean = false
    private var currentNewOrderWithCoachMark: Int = -1
    private var shouldShowCoachMark: Boolean = false
    private var shouldScrollToTop: Boolean = false
    private var filterOrderType: Int = 0
    private var skipSearch: Boolean = false // when restored, onSearchTextChanged is called which trigger unwanted refresh order list
    private var coachMarkIndexToShow: Int = 0
    private var selectedOrderId: String = ""
    private var tabActive: String = ""
    private var somListBulkAcceptOrderBottomSheet: SomListBulkAcceptOrderBottomSheet? = null
    private var bulkAcceptOrderDialog: SomListBulkActionDialog? = null
    private var tickerPagerAdapter: TickerPagerAdapter? = null
    private var userNotAllowedDialog: DialogUnify? = null
    private var errorToaster: Snackbar? = null
    private var commonToaster: Snackbar? = null
    private var textChangeJob: Job? = null
    private var menu: Menu? = null
    private var filterDate = ""
    private var somFilterBottomSheet: SomFilterBottomSheet? = null

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + masterJob

    override fun getSwipeRefreshLayout(view: View?) = view?.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayoutSomList)
    override fun createAdapterInstance() = SomListOrderAdapter(adapterTypeFactory)
    override fun onItemClicked(t: Visitable<SomListAdapterTypeFactory>?) {}
    override fun getAdapterTypeFactory() = SomListAdapterTypeFactory(this, this)
    override fun getRecyclerViewResourceId() = R.id.rvSomList
    override fun getScreenName(): String = ""
    override fun initInjector() = inject()
    override fun onDismiss() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_som_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setInitialOrderListParams()
        observeUserRoles()
        observeTopAdsCategory()
        observeTickers()
        observeFilters()
        observeWaitingPaymentCounter()
        observeOrderList()
        observeAcceptOrder()
        observeRejectCancelRequest()
        observeRejectOrder()
        observeEditAwb()
        observeBulkAcceptOrder()
        observeBulkAcceptOrderStatus()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.menu_som_list, menu)
        this.menu = menu
        view?.postDelayed({
            reshowNewOrderCoachMark(adapter.data.filterIsInstance<SomListOrderUiModel>())
            reshowStatusFilterCoachMark()
            reshowWaitingPaymentOrderListCoachMark()
            reshowBulkAcceptOrderCoachMark()
        }, DELAY_COACHMARK)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
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
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if ((item.itemId == R.id.som_list_action_waiting_payment_order) && viewModel.waitingPaymentCounterResult.value !is Fail) {
            activity?.invalidateOptionsMenu()
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
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            SomAnalytics.sendScreenName(SomConsts.LIST_ORDER_SCREEN_NAME)
            if (!isUserRoleFetched()) viewModel.getUserRoles()
        } else {
            if (isUserRoleFetched()) viewModel.clearUserRoles()
            dismissCoachMark()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_DETAIL -> handleSomDetailActivityResult(resultCode, data)
            REQUEST_CONFIRM_SHIPPING -> handleSomConfirmShippingActivityResult(resultCode, data)
            REQUEST_CONFIRM_REQUEST_PICKUP -> handleSomRequestPickUpActivityResult(resultCode, data)
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
        view?.postDelayed({
            reshowNewOrderCoachMark(adapter.data.filterIsInstance<SomListOrderUiModel>())
            reshowStatusFilterCoachMark()
            reshowWaitingPaymentOrderListCoachMark()
            reshowBulkAcceptOrderCoachMark()
        }, DELAY_COACHMARK)
    }

    override fun onPause() {
        childFragmentManager.fragments.forEach {
            if (it is BottomSheetUnify && it !is SomFilterBottomSheet) it.dismiss()
        }
        dismissCoachMark()
        super.onPause()
        bulkAcceptButtonEnterAnimation?.end()
        bulkAcceptButtonLeaveAnimation?.end()
    }

    override fun getEndlessLayoutManagerListener(): EndlessLayoutManagerListener? {
        return EndlessLayoutManagerListener { somListLayoutManager }
    }

    override fun loadInitialData() {
        viewModel.isMultiSelectEnabled = false
        resetOrderSelectedStatus()
        isLoadingInitialData = true
        loadUserRoles()
        loadTopAdsCategory()
        loadTickers()
        loadWaitingPaymentOrderCounter()
        loadFilters()
        if (shouldReloadOrderListImmediately()) {
            loadOrderList()
        }
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
        getSwipeRefreshLayout(view)?.apply {
            isEnabled = true
            isRefreshing = false
        }
        somListLoading.gone()
        adapter.hideLoading()
    }

    override fun onTabClicked(status: SomListFilterUiModel.Status, shouldScrollToTop: Boolean) {
        tabActive = if (status.isChecked) {
            viewModel.setStatusOrderFilter(status.id)
            SomAnalytics.eventClickStatusFilter(status.key, status.status)
            status.key
        } else {
            viewModel.setStatusOrderFilter(emptyList())
            SomAnalytics.eventClickStatusFilter(SomConsts.STATUS_ALL_ORDER, SomConsts.STATUS_NAME_ALL_ORDER)
            ""
        }
        setDefaultSortByValue()
        if (viewModel.isMultiSelectEnabled) {
            viewModel.isMultiSelectEnabled = false
            resetOrderSelectedStatus()
            toggleBulkActionButtonVisibility()
            toggleBulkActionCheckboxVisibility()
            checkBoxBulkAction.isChecked = false
            checkBoxBulkAction.setIndeterminate(false)
        }
        this.shouldScrollToTop = shouldScrollToTop
        loadFilters(false)
        if (shouldReloadOrderListImmediately()) {
            refreshOrderList()
        } else {
            getSwipeRefreshLayout(view)?.isRefreshing = true
        }
    }

    override fun onParentSortFilterClicked() {
        val somFilterList = somListSortFilterTab.getSomFilterUi()
        somFilterBottomSheet = SomFilterBottomSheet.createInstance(
                somListSortFilterTab.getSelectedFilterStatusName(),
                viewModel.getDataOrderListParams().statusList,
                somFilterList,
                filterDate,
                filterOrderType != 0
        )
        somFilterBottomSheet?.setSomFilterFinishListener(this)
        somFilterBottomSheet?.isAdded?.let {
            if (!(it)) {
                somFilterBottomSheet?.show(childFragmentManager)
            }
        }
    }

    override fun onDescriptionViewClick(linkUrl: CharSequence) {
        onTickerDescriptionClicked(linkUrl.toString())
    }

    override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
        onTickerDescriptionClicked(linkUrl.toString())
    }

    override fun onSearchSubmitted(text: String?) {
        if (textChangeJob?.isActive == false && getSwipeRefreshLayout(view)?.isRefreshing == false) {
            skipSearch = true
            textChangeJob?.cancel()
            viewModel.setSearchParam(text.orEmpty())
            shouldScrollToTop = true
            loadFilters(false)
            if (shouldReloadOrderListImmediately()) {
                refreshOrderList()
            } else {
                getSwipeRefreshLayout(view)?.isRefreshing = true
            }
            SomAnalytics.eventSubmitSearch(text.orEmpty())
        }
    }

    override fun onSearchTextChanged(text: String?) {
        textChangeJob?.cancel()
        textChangeJob = launchCatchError(block = {
            delay(DELAY_SEARCH)
            viewModel.setSearchParam(text.orEmpty())
            if (!skipSearch) {
                shouldScrollToTop = true
                loadFilters(false)
                if (shouldReloadOrderListImmediately()) {
                    refreshOrderList()
                } else {
                    getSwipeRefreshLayout(view)?.isRefreshing = true
                }
                SomAnalytics.eventSubmitSearch(text.orEmpty())
            }
            skipSearch = false
        }, onError = {
            // TODO: Log to crashlytics
        })
    }

    private fun onTickerDescriptionClicked(linkUrl: String) {
        if (linkUrl.isNotBlank()) {
            context?.let { context ->
                RouteManager.route(context, linkUrl)
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
                somListSortFilterTab.getSelectedFilterStatus(),
                somListSortFilterTab.getSelectedFilterStatusName())
    }

    override fun onOrderClicked(order: SomListOrderUiModel) {
        selectedOrderId = order.orderId
        goToSomOrderDetail(this, order, viewModel.userRoleResult.value)
        SomAnalytics.eventClickOrderCard(order.orderStatusId, order.status)
    }

    override fun onTrackButtonClicked(orderId: String, url: String) {
        selectedOrderId = orderId
        goToTrackingPage(context, orderId, url)
    }

    override fun onConfirmShippingButtonClicked(orderId: String) {
        selectedOrderId = orderId
        goToConfirmShippingPage(this, orderId)
    }

    override fun onAcceptOrderButtonClicked(orderId: String) {
        selectedOrderId = orderId
        viewModel.acceptOrder(orderId)
    }

    override fun onRequestPickupButtonClicked(orderId: String) {
        selectedOrderId = orderId
        goToRequestPickupPage(this, orderId)
    }

    override fun onRespondToCancellationButtonClicked(order: SomListOrderUiModel) {
        selectedOrderId = order.orderId
        SomOrderRequestCancelBottomSheet().apply {
            setListener(object : SomOrderRequestCancelBottomSheet.SomOrderRequestCancelBottomSheetListener {
                override fun onAcceptOrder() {
                    onAcceptOrderButtonClicked(selectedOrderId)
                }

                override fun onRejectOrder(reasonBuyer: String) {
                    val orderRejectRequest = SomRejectRequestParam(
                            orderId = selectedOrderId,
                            rCode = "0",
                            reason = reasonBuyer
                    )
                    rejectOrder(orderRejectRequest)
                }

                override fun onRejectCancelRequest() {
                    rejectCancelOrder(selectedOrderId)
                }
            })
            init(order.buttons.firstOrNull()?.popUp
                    ?: PopUp(), order.cancelRequestOriginNote, order.orderStatusId)
            show(this@SomListFragment.childFragmentManager, SomOrderRequestCancelBottomSheet.TAG)
        }
    }

    override fun onViewComplaintButtonClicked(order: SomListOrderUiModel) {
        RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, order.buttons.firstOrNull()?.url.orEmpty()))
    }

    override fun onEditAwbButtonClicked(orderId: String) {
        selectedOrderId = orderId
        SomOrderEditAwbBottomSheet().apply {
            setListener(object : SomOrderEditAwbBottomSheet.SomOrderEditAwbBottomSheetListener {
                override fun onEditAwbButtonClicked(cancelNotes: String) {
                    viewModel.editAwb(orderId, cancelNotes)
                }
            })
            show(this@SomListFragment.childFragmentManager, SomOrderEditAwbBottomSheet.TAG)
        }
    }

    override fun onFinishBindNewOrder(view: View, itemIndex: Int) {
        context?.let { context ->
            if (!CoachMarkPreference.hasShown(context, SHARED_PREF_NEW_SOM_LIST_COACH_MARK)) {
                rvSomList.addOnScrollListener(recyclerViewScrollListener)
                setCoachMarkStepListener()
                coachMark?.onFinishListener = { rvSomList.removeOnScrollListener(recyclerViewScrollListener) }
                CoachMarkPreference.setShown(context, SHARED_PREF_NEW_SOM_LIST_COACH_MARK, true)
                shouldShowCoachMark = true
                reshowNewOrderCoachMark(adapter.data.filterIsInstance<SomListOrderUiModel>())
            }
            return@let
        }
    }

    override fun isMultiSelectEnabled(): Boolean = viewModel.isMultiSelectEnabled

    override fun onBulkAcceptOrderButtonClicked() {
        viewModel.bulkAcceptOrder(getSelectedOrderIds())
    }

    private fun setDefaultSortByValue() {
        if (!somListSortFilterTab.isSortByAppliedManually()) {
            if (tabActive == SomConsts.KEY_CONFIRM_SHIPPING) {
                viewModel.setSortOrderBy(SomConsts.SORT_BY_PAYMENT_DATE_ASCENDING)
            } else {
                viewModel.setSortOrderBy(SomConsts.SORT_BY_PAYMENT_DATE_DESCENDING)
            }
        }
    }

    private fun showBulkAcceptOrderDialog(orderCount: Int) {
        context?.let { context ->
            if (bulkAcceptOrderDialog == null) {
                bulkAcceptOrderDialog = SomListBulkActionDialog(context).apply {
                    init()
                    setOnDismiss {
                        resetOrderSelectedStatus()
                        toggleBulkAction()
                        toggleBulkActionButtonVisibility()
                        toggleBulkActionCheckboxVisibility()
                        toggleTvSomListBulkText()
                        loadFilters()
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
        showWaitingPaymentOrderListMenuShimmer()
        rvSomList.layoutManager = somListLayoutManager
        rvSomList.setItemViewCacheSize(6)
        bulkActionCheckBoxContainer.layoutTransition.enableTransitionType(CHANGING)
        setupListeners()
    }

    private fun observeUserRoles() {
        viewModel.userRoleResult.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Success -> {
                    if (!result.data.roles.any { allowedRoles.contains(it) }) {
                        onUserNotAllowedToViewSOM()
                    }
                }
                is Fail -> showGlobalError(result.throwable)
            }
        })
    }

    private fun observeTopAdsCategory() {
        viewModel.topAdsCategoryResult.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Success -> {
                    if (adapter.data.filterIsInstance<SomListEmptyStateUiModel>().isNotEmpty()) {
                        showEmptyState()
                        rvSomList.show()
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
                    tickerSomList.gone()
                    showToasterError(view)
                }
            }
        })
    }

    private fun observeFilters() {
        viewModel.filterResult.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Success -> {
                    if (tabActive.isNotBlank() && tabActive != SomConsts.STATUS_ALL_ORDER) {
                        result.data.statusList.find { it.key == tabActive }?.let { activeFilter ->
                            activeFilter.isChecked = true
                            somListSortFilterTab.selectTab(activeFilter)
                            refreshOrderList()
                        }
                    }
                    somListSortFilterTab.show(result.data)
                }
                is Fail -> showGlobalError(result.throwable)
            }
            shimmerViews.gone()
        })
    }

    private fun observeWaitingPaymentCounter() {
        viewModel.waitingPaymentCounterResult.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Fail -> {
                    showToasterError(view)
                }
            }
            activity?.invalidateOptionsMenu()
        })
    }

    private fun observeOrderList() {
        viewModel.orderListResult.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Success -> renderOrderList(result.data)
                is Fail -> showGlobalError(result.throwable)
            }
        })
    }

    private fun observeAcceptOrder() {
        viewModel.acceptOrderResult.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Success -> onAcceptOrderSuccess(result.data.acceptOrder)
                is Fail -> showToasterError(view, getString(R.string.som_list_failed_accept_order))
            }
        })
    }

    private fun observeRejectOrder() {
        viewModel.rejectOrderResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    val rejectOrderResponse = it.data.rejectOrder
                    if (rejectOrderResponse.success == 1) {
                        handleRejectOrderResult(rejectOrderResponse)
                        onActionCompleted()
                    } else {
                        showToasterError(view, rejectOrderResponse.message.first())
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
                        onActionCompleted()
                    } else {
                        showToasterError(view, getString(R.string.global_error))
                    }
                }
                is Fail -> {
                    val message = it.throwable.message.toString()
                    if (message.isNotEmpty()) {
                        showToasterError(view, message)
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
                    onActionCompleted()
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
                    if (somListBulkAcceptOrderBottomSheet?.isVisible == true) {
                        somListBulkAcceptOrderBottomSheet?.dismiss()
                    }
                    showBulkAcceptOrderDialog(getSelectedOrderIds().size)
                }
                is Fail -> {
                    if (somListBulkAcceptOrderBottomSheet?.isVisible == true) {
                        somListBulkAcceptOrderBottomSheet?.onBulkAcceptOrderFailed()
                        showCommonToaster(somListBulkAcceptOrderBottomSheet?.getChildViews(), "Terjadi kesalahan.", Toaster.TYPE_ERROR)
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
                            somListSortFilterTab.getSelectedFilterStatus(),
                            somListSortFilterTab.getSelectedFilterStatusName(),
                            successCount,
                            userSession.userId,
                            userSession.shopId)
                }
            }
        })
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

    private fun getSelectedOrderIds(): List<String> {
        return adapter.data.filterIsInstance<SomListOrderUiModel>()
                .filter { it.isChecked }
                .map { it.orderId }
    }

    private fun onActionCompleted() {
        refreshSelectedOrder()
    }

    private fun refreshSelectedOrder() {
        adapter.data.filterIsInstance<SomListOrderUiModel>().find { it.orderId == selectedOrderId }?.let { selectedOrder ->
            isRefreshingSelectedOrder = true
            getSwipeRefreshLayout(view)?.apply {
                isEnabled = true
                isRefreshing = true
            }
            viewModel.refreshSelectedOrder(selectedOrder.orderResi)
        }
    }

    private fun showEmptyState() {
        val newItems = arrayListOf(createSomListEmptyStateModel(viewModel.isTopAdsActive()))
        (adapter as SomListOrderAdapter).updateOrders(newItems)
    }

    private fun loadUserRoles() {
        viewModel.getUserRoles()
    }

    private fun loadTopAdsCategory() {
        viewModel.getTopAdsCategory()
    }

    private fun loadTickers() {
        viewModel.getTickers()
    }

    private fun loadWaitingPaymentOrderCounter() {
        showWaitingPaymentOrderListMenuShimmer()
        viewModel.getWaitingPaymentCounter()
    }

    private fun loadFilters(showShimmer: Boolean = true) {
        if (showShimmer) {
            sortFilterSomList.invisible()
            shimmerViews.show()
        }
        viewModel.getFilters()
    }

    private fun loadOrderList() {
        if (isLoadingInitialData) {
            viewModel.resetNextOrderId()
            if (adapter.dataSize > 0) {
                getSwipeRefreshLayout(view)?.isRefreshing = true
            } else {
                rvSomList.gone()
                somListLoading.show()
            }
        }
        viewModel.getOrderList()
    }

    private fun onAcceptOrderSuccess(acceptOrderResponse: SomAcceptOrderResponse.Data.AcceptOrder) {
        if (acceptOrderResponse.success == 1) {
            onActionCompleted()
            showCommonToaster(view, acceptOrderResponse.listMessage.firstOrNull())
        } else {
            showToasterError(view, acceptOrderResponse.listMessage.firstOrNull().orEmpty())
        }
    }

    private fun setupListeners() {
        tickerSomList.setDescriptionClickEvent(this)
        searchBarSomList.setListener(this)
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
            }
        }
    }

    private fun toggleTvSomListBulkText() {
        val textResId = if (viewModel.isMultiSelectEnabled) R.string.som_list_multi_select_cancel else R.string.som_list_multi_select
        tvSomListBulk.text = getString(textResId)
    }

    private fun updateOrderCounter() {
        val text = if (viewModel.isMultiSelectEnabled) {
            val checkedCount = adapter.data.filterIsInstance<SomListOrderUiModel>().count { it.isChecked }
            getString(R.string.som_list_order_counter_multi_select_enabled, checkedCount)
        } else {
            getString(R.string.som_list_order_counter, somListSortFilterTab.getSelectedFilterOrderCount())
        }
        tvSomListOrderCounter.text = text
    }

    private fun handleSomDetailActivityResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            when {
                data.hasExtra(SomConsts.RESULT_CONFIRM_SHIPPING) -> handleConfirmShippingResult(data.getStringExtra(SomConsts.RESULT_CONFIRM_SHIPPING))
                data.hasExtra(SomConsts.RESULT_ACCEPT_ORDER) -> {
                    data.getParcelableExtra<SomAcceptOrderResponse.Data.AcceptOrder>(SomConsts.RESULT_ACCEPT_ORDER)?.let { resultAcceptOrder ->
                        onAcceptOrderSuccess(resultAcceptOrder)
                    }
                }
                data.hasExtra(SomConsts.RESULT_PROCESS_REQ_PICKUP) -> {
                    data.getParcelableExtra<SomProcessReqPickup.Data.MpLogisticRequestPickup>(SomConsts.RESULT_PROCESS_REQ_PICKUP)?.let { resultProcessReqPickup ->
                        handleRequestPickUpResult(resultProcessReqPickup.listMessage.firstOrNull())
                    }
                }
                data.hasExtra(SomConsts.RESULT_REJECT_ORDER) -> {
                    data.getParcelableExtra<SomRejectOrderResponse.Data.RejectOrder>(SomConsts.RESULT_REJECT_ORDER)?.let { resultRejectOrder ->
                        handleRejectOrderResult(resultRejectOrder)
                    }
                }
                data.hasExtra(SomConsts.RESULT_SET_DELIVERED) -> {
                    data.getStringExtra(SomConsts.RESULT_SET_DELIVERED)?.let { message ->
                        onActionCompleted()
                        showCommonToaster(view, message)
                    }
                }
            }
        }
    }

    private fun handleRejectOrderResult(resultRejectOrder: SomRejectOrderResponse.Data.RejectOrder) {
        onActionCompleted()
        showCommonToaster(view, resultRejectOrder.message.firstOrNull())
    }

    private fun handleSomConfirmShippingActivityResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (data.hasExtra(SomConsts.RESULT_CONFIRM_SHIPPING)) {
                handleConfirmShippingResult(data.getStringExtra(SomConsts.RESULT_CONFIRM_SHIPPING))
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

    private fun handleRequestPickUpResult(message: String?) {
        onActionCompleted()
        showCommonToaster(view, message)
    }

    private fun handleConfirmShippingResult(message: String?) {
        onActionCompleted()
        showCommonToaster(view, message)
    }

    private fun toggleBulkAction() {
        viewModel.isMultiSelectEnabled = !viewModel.isMultiSelectEnabled
    }

    private fun resetOrderSelectedStatus() {
        adapter.data.filterIsInstance<SomListOrderUiModel>().onEach { it.isChecked = false }.run {
            adapter.notifyItemRangeChanged(0, size, Bundle().apply { putBoolean(SomListOrderViewHolder.TOGGLE_SELECTION, true) })
        }
    }

    private fun checkAllOrder() {
        adapter.data.onEach { if (it is SomListOrderUiModel && it.cancelRequest == 0) it.isChecked = true }
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
        checkBoxBulkAction.showWithCondition(viewModel.isMultiSelectEnabled)
    }

    private fun updateBulkActionCheckboxStatus() {
        val groupedOrders = adapter.data
                .filter { it is SomListOrderUiModel && it.cancelRequest == 0 }
                .groupBy { (it as SomListOrderUiModel).isChecked }
        val newIndeterminateStatus = groupedOrders[true]?.size.orZero() > 0 && groupedOrders[false]?.size.orZero() > 0
        val newCheckedStatus = groupedOrders[true]?.size.orZero() > 0
        if (newCheckedStatus != checkBoxBulkAction.isChecked) {
            checkBoxBulkAction.isChecked = newCheckedStatus
        }
        if (newIndeterminateStatus != checkBoxBulkAction.getIndeterminate()) {
            checkBoxBulkAction.setIndeterminate(newIndeterminateStatus)
        }
    }

    private fun showWaitingPaymentOrderListMenuShimmer() {
        menu?.findItem(R.id.som_list_action_waiting_payment_order_shimmer)?.isVisible = true
        menu?.findItem(R.id.som_list_action_waiting_payment_order)?.isVisible = false
    }

    private fun showWaitingPaymentOrderListMenu() {
        context?.let {
            menu?.findItem(R.id.som_list_action_waiting_payment_order_shimmer)?.isVisible = false
            menu?.findItem(R.id.som_list_action_waiting_payment_order)?.apply {
                icon = DottedNotification(it, R.drawable.ic_som_list_waiting_payment_button_icon, false)
                isVisible = true
            }
        }
    }

    private fun showDottedWaitingPaymentOrderListMenu() {
        context?.let {
            menu?.findItem(R.id.som_list_action_waiting_payment_order_shimmer)?.isVisible = false
            menu?.findItem(R.id.som_list_action_waiting_payment_order)?.apply {
                icon = DottedNotification(it, R.drawable.ic_som_list_waiting_payment_button_icon, true)
                isVisible = true
            }
        }
    }

    private fun onUserNotAllowedToViewSOM() {
        context?.run {
            rvSomList.gone()
            if (userNotAllowedDialog == null) {
                userNotAllowedDialog = Utils.createUserNotAllowedDialog(this)
            }
            userNotAllowedDialog?.show()
        }
    }

    private fun showGlobalError(throwable: Throwable) {
        dismissCoachMark()
        somListLoading.gone()
        rvSomList.gone()
        multiEditViews.gone()
        btnBulkAction.gone()
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
        tickerSomList.addPagerView(tickerPagerAdapter, activeTickers)
        tickerSomList.showWithCondition(data.isNotEmpty())
    }

    private fun renderOrderList(data: List<SomListOrderUiModel>) {
        skipSearch = false
        hideLoading()
        if (rvSomList.visibility != View.VISIBLE) rvSomList.show()
        // show only if current order list is based on current search keyword
        if (isLoadingInitialData && data.isEmpty()) {
            showEmptyState()
            multiEditViews.gone()
            toggleBulkActionButtonVisibility()
        } else if (data.firstOrNull()?.searchParam == searchBarSomList.searchText.orEmpty()) {
            if (isLoadingInitialData) {
                (adapter as SomListOrderAdapter).updateOrders(data)
                tvSomListOrderCounter.text = getString(R.string.som_list_order_counter, somListSortFilterTab.getSelectedFilterOrderCount())
                multiEditViews.showWithCondition(somListSortFilterTab.shouldShowBulkAction())
                toggleTvSomListBulkText()
                toggleBulkActionCheckboxVisibility()
                toggleBulkActionButtonVisibility()
                if (shouldScrollToTop) {
                    shouldScrollToTop = false
                    rvSomList.addOneTimeGlobalLayoutListener {
                        rvSomList.smoothScrollToPosition(0)
                    }
                }
                if (coachMark?.currentIndex == 0) {
                    dismissCoachMark(true)
                } else if (coachMark?.currentIndex == 3 && !multiEditViews.isVisible) {
                    dismissCoachMark(true)
                }
            } else {
                adapter.addMoreData(data)
                rvSomList.post {
                    updateBulkActionCheckboxStatus()
                }
            }
            rvSomList?.postDelayed({
                reshowNewOrderCoachMark(data)
                reshowStatusFilterCoachMark()
                reshowWaitingPaymentOrderListCoachMark()
                reshowBulkAcceptOrderCoachMark()
            }, DELAY_COACHMARK)
        } else if (isRefreshingSelectedOrder) {
            isRefreshingSelectedOrder = false
            data.firstOrNull().let { newOrder ->
                if (newOrder == null) {
                    (adapter as SomListOrderAdapter).removeOrder(selectedOrderId)
                    somListSortFilterTab.decrementOrderCount()
                    updateOrderCounter()
                    checkLoadMore()
                } else {
                    (adapter as SomListOrderAdapter).updateOrder(newOrder)
                }
            }
            selectedOrderId = ""
            if (adapter.dataSize == 0) {
                multiEditViews.showWithCondition(adapter.dataSize > 0)
                showEmptyState()
            }
        }
        updateScrollListenerState(viewModel.hasNextPage())
        isLoadingInitialData = false
    }

    private fun getFirstNewOrder(orders: List<SomListOrderUiModel>): Int {
        return orders.indexOfFirst {
            it.orderStatusId == SomConsts.STATUS_CODE_ORDER_CREATED && it.buttons.isNotEmpty() && it.cancelRequest == 0
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
        return if (!isTopAdsActive && somListSortFilterTab.isNewOrderFilterSelected() &&
                !somListSortFilterTab.isFilterApplied() && searchBarSomList.searchText.isEmpty()) {
            SomListEmptyStateUiModel(
                    imageUrl = SomConsts.SOM_LIST_EMPTY_STATE_NO_FILTER_ILLUSTRATION,
                    title = getString(R.string.empty_peluang_title),
                    description = getString(R.string.empty_peluang_desc_non_topads_no_filter),
                    buttonText = getString(R.string.btn_cek_peluang_non_topads),
                    buttonAppLink = ApplinkConstInternalTopAds.TOPADS_CREATE_ADS,
                    showButton = true
            )
        } else if (somListSortFilterTab.isFilterApplied() || searchBarSomList.searchText.isNotEmpty()) {
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
        scrollViewErrorState.gone()
        isLoadingInitialData = true
        loadOrderList()
    }

    private fun showToasterError(view: View?, message: String = getString(R.string.som_list_error_some_information_cannot_be_loaded)) {
        if (errorToaster == null) {
            view?.let {
                errorToaster = Toaster.build(
                        it,
                        message,
                        Toaster.LENGTH_INDEFINITE,
                        Toaster.TYPE_ERROR,
                        getString(R.string.btn_reload),
                        View.OnClickListener {
                            refreshFailedRequests()
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
                commonToaster = Toaster.build(it, message, Toaster.LENGTH_SHORT, toasterType, "")
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
            tickerSomList.gone()
            viewModel.getTickers()
        }
        if (viewModel.userRoleResult.value is Fail) {
            viewModel.getUserRoles()
        }
    }

    private fun rejectOrder(orderRejectRequestParam: SomRejectRequestParam) {
        activity?.resources?.let {
            viewModel.rejectOrder(orderRejectRequestParam)
        }
    }

    private fun rejectCancelOrder(orderId: String) {
        if (orderId.isNotBlank()) {
            viewModel.rejectCancelOrder(orderId)
        }
    }

    private fun showBulkAcceptOrderBottomSheet() {
        if (somListBulkAcceptOrderBottomSheet == null) {
            somListBulkAcceptOrderBottomSheet = SomListBulkAcceptOrderBottomSheet()
        }
        somListBulkAcceptOrderBottomSheet?.let {
            it.setOrders(adapter.data.filterIsInstance<SomListOrderUiModel>().filter { it.isChecked })
            it.setListener(this@SomListFragment)
            it.show(childFragmentManager, TAG_BOTTOM_SHEET_BULK_ACCEPT)
        }
    }

    private fun setInitialOrderListParams() {
        val orderTypes = if (filterOrderType == 0) {
            emptyList()
        } else {
            somListSortFilterTab.addCounter(1)
            listOf(filterOrderType)
        }
        setDefaultSortByValue()
        viewModel.setOrderTypeFilter(orderTypes)
    }

    private fun Throwable.showErrorToaster() {
        if (globalErrorSomList.isVisible) {
            if (this is UnknownHostException || this is SocketTimeoutException) {
                showNoInternetConnectionToaster()
            } else {
                showServerErrorToaster()
            }
        }
    }

    private fun showNoInternetConnectionToaster() {
        showToasterError(view, getString(R.string.som_error_message_no_internet_connection))
    }

    private fun showServerErrorToaster() {
        showToasterError(view, getString(R.string.som_error_message_server_fault))
    }

    private fun shouldReloadOrderListImmediately(): Boolean = tabActive.isBlank() || tabActive == SomConsts.STATUS_ALL_ORDER
    private fun isUserRoleFetched(): Boolean = viewModel.userRoleResult.value is Success

    override fun onClickShowOrderFilter(
            filterData: SomListGetOrderListParam, somFilterUiModelList: List<SomFilterUiModel>,
            idFilter: String, filterDate: String, isRequestCancelFilterApplied: Boolean) {
        this.filterDate = filterDate
        this.filterOrderType = if (isRequestCancelFilterApplied) FILTER_CANCELLATION_REQUEST else 0
        viewModel.updateGetOrderListParams(filterData)
        somListSortFilterTab.updateSomListFilterUi(somFilterUiModelList)
        somListSortFilterTab.updateCounterSortFilter(filterDate)
        val selectedStatusFilterKey = somFilterUiModelList.find {
            it.nameFilter == SomConsts.FILTER_STATUS_ORDER
        }?.somFilterData?.find {
            it.isSelected
        }?.key
        tabActive = selectedStatusFilterKey.orEmpty()
        val somListFilter = FilterResultMapper.convertToMapSomListFilterUiModel(
                somFilterUiModelList, idFilter, somListSortFilterTab.getSomListFilterUiModel())
        somListFilter.statusList.find { it.key == selectedStatusFilterKey }.let {
            if (it != null) {
                somListSortFilterTab.selectTab(it)
            } else {
                somListSortFilterTab.selectTabReset()
                somListSortFilterTab.show(somListFilter)
                somListSortFilterTab.updateCounterFilter()
            }
            loadFilters(false)
            if (shouldReloadOrderListImmediately()) {
                refreshOrderList()
            } else {
                getSwipeRefreshLayout(view)?.isRefreshing = true
            }
        }
        setDefaultSortByValue()
    }

    override fun onClickOverlayBottomSheet(filterCancelWrapper: SomFilterCancelWrapper) {
        somListSortFilterTab.updateSomListFilterUi(filterCancelWrapper.somFilterUiModelList)
        val orderListParam = viewModel.getDataOrderListParams()
        orderListParam.statusList = filterCancelWrapper.orderStatusIdList
        viewModel.updateGetOrderListParams(orderListParam)
        this.filterDate = filterCancelWrapper.filterDate
    }

    private fun setCoachMarkStepListener() {
        coachMark?.setStepListener(object : CoachMark2.OnStepListener {
            override fun onStep(currentIndex: Int, coachMarkItem: CoachMark2Item) {
                when (currentIndex) {
                    0 -> {
                        shouldShowCoachMark = true
                        coachMark?.isDismissed = true
                        coachMarkIndexToShow = 0
                        if (tabActive.isNotBlank() && tabActive != SomConsts.STATUS_ALL_ORDER) {
                            viewModel.resetGetOrderListParam()
                            somListSortFilterTab.clear()
                            skipSearch = true
                            searchBarSomList.searchText = ""
                            somListSortFilterTab.unselectCurrentStatusFilter()
                        } else {
                            rvSomList?.post {
                                reshowNewOrderCoachMark(adapter.data.filterIsInstance<SomListOrderUiModel>())
                            }
                        }
                    }
                    2, 3 -> {
                        if (currentIndex == 3 && tabActive == SomConsts.STATUS_NEW_ORDER) return
                        if (currentIndex == 2 && tabActive == SomConsts.STATUS_ALL_ORDER) return
                        viewModel.resetGetOrderListParam()
                        somListSortFilterTab.clear()
                        skipSearch = true
                        searchBarSomList.searchText = ""
                        coachMarkIndexToShow = currentIndex
                        // if user press "Lanjut" after waiting payment coachmark, auto select new order filter, if user press "Balik"
                        // auto deselect new order to show all order
                        val targetStatusFilter = if (currentIndex == 3 && tabActive != SomConsts.STATUS_NEW_ORDER) {
                            SomConsts.STATUS_NEW_ORDER
                        } else ""
                        if (targetStatusFilter.isNotBlank()) {
                            shouldShowCoachMark = true
                            coachMark?.isDismissed = true
                            viewModel.filterResult.value?.let {
                                if (it is Success) {
                                    it.data.statusList.find { it.key == targetStatusFilter }?.let {
                                        somListSortFilterTab.selectTab(it)
                                        it.isChecked = true
                                        onTabClicked(it, true)
                                    }
                                }
                            }
                        } else {
                            somListSortFilterTab.unselectCurrentStatusFilter()
                        }
                    }
                }
            }
        })
    }

    private fun createCoachMarkItems(firstNewOrderView: View): ArrayList<CoachMark2Item> {
        return arrayListOf<CoachMark2Item>().apply {
            add(CoachMark2Item(firstNewOrderView, getString(R.string.som_list_coachmark_new_order_card_title), getString(R.string.som_list_coachmark_new_order_card_description)))
            add(CoachMark2Item(sortFilterSomList, getString(R.string.som_list_coachmark_sort_filter_title), getString(R.string.som_list_coachmark_sort_filter_description)))
            (menu as MenuBuilder).visibleItems.firstOrNull()?.let { visibleMenu ->
                activity?.findViewById<View>(visibleMenu.itemId)?.let {
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
                rvSomList.removeOnScrollListener(recyclerViewScrollListener)
            coachMarkIndexToShow = coachMark?.currentIndex.orZero()
            coachMark?.dismissCoachMark()
        }
    }

    // show new order coachmark if previously hidden and current condition is meet requirement
    // the requirement to show new order coachmark is:
    // 1. no filter applied & no search keyword applied
    // 2. last coachmark index is 0
    // 3. and there is any new order (we can tell by check get filter result, and we need to scroll through until new order card is showed)
    // only call this method after rendering order list
    private fun reshowNewOrderCoachMark(newOrders: List<SomListOrderUiModel>) {
        if (shouldShowCoachMark && coachMarkIndexToShow == 0 &&
                (tabActive.isBlank() || tabActive == SomConsts.STATUS_ALL_ORDER) &&
                !somListSortFilterTab.isFilterApplied() && searchBarSomList.searchText.toString().isBlank()) {
            // check whether the user has any new order
            val filterResult = viewModel.filterResult.value
            if (filterResult is Success) {
                val hasNewOrder = filterResult.data.statusList.find {
                    it.key == SomConsts.STATUS_NEW_ORDER
                }?.amount.orZero() > 0
                if (hasNewOrder) {
                    val firstNewOrderViewPosition = getFirstNewOrder(newOrders)
                    var firstNewOrderViewPositionInAdapter = getFirstNewOrder(adapter.data.filterIsInstance<SomListOrderUiModel>())
                    if (firstNewOrderViewPosition != -1) {
                        firstNewOrderViewPositionInAdapter = adapter.data.indexOf(newOrders[firstNewOrderViewPosition])
                        if (firstNewOrderViewPositionInAdapter != -1) {
                            rvSomList.stopScroll()
                            somListLayoutManager?.scrollToPositionWithOffset(firstNewOrderViewPositionInAdapter, 0)
                            rvSomList?.run {
                                (layoutManager?.findViewByPosition(firstNewOrderViewPositionInAdapter)?.findViewById<View>(R.id.btnQuickAction))?.let {
                                    it.post {
                                        if (getVisiblePercent(it) == 0) {
                                            currentNewOrderWithCoachMark = firstNewOrderViewPositionInAdapter
                                            addOnScrollListener(recyclerViewScrollListener)
                                            coachMark?.isDismissed = false
                                            coachMark?.showCoachMark(ArrayList(createCoachMarkItems(it)), index = coachMarkIndexToShow)
                                            shouldShowCoachMark = false
                                        }
                                    }
                                }
                            }
                        }
                    } else if (firstNewOrderViewPositionInAdapter == -1) {
                        rvSomList.stopScroll()
                        somListLayoutManager?.scrollToPositionWithOffset(adapter.dataSize - 1, 0)
                    }
                }
            }
        }
    }

    private fun reshowStatusFilterCoachMark() {
        if (shouldShowCoachMark && coachMarkIndexToShow == 1 && sortFilterSomList.isVisible) {
            currentNewOrderWithCoachMark = -1
            coachMark?.isDismissed = false
            coachMark?.showCoachMark(ArrayList(createCoachMarkItems(rvSomList)), index = coachMarkIndexToShow)
            shouldShowCoachMark = false
        }
    }

    private fun reshowWaitingPaymentOrderListCoachMark() {
        val waitingPaymentOrderListCountResult = viewModel.waitingPaymentCounterResult.value
        if (shouldShowCoachMark && coachMarkIndexToShow == 2 && waitingPaymentOrderListCountResult is Success) {
            currentNewOrderWithCoachMark = -1
            coachMark?.isDismissed = false
            coachMark?.showCoachMark(ArrayList(createCoachMarkItems(rvSomList)), index = coachMarkIndexToShow)
            shouldShowCoachMark = false
        }
    }

    private fun reshowBulkAcceptOrderCoachMark() {
        if (shouldShowCoachMark && coachMarkIndexToShow == 3 && tvSomListBulk.isVisible) {
            currentNewOrderWithCoachMark = -1
            coachMark?.isDismissed = false
            coachMark?.showCoachMark(ArrayList(createCoachMarkItems(rvSomList)), index = coachMarkIndexToShow)
            shouldShowCoachMark = false
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
        if (bulkAcceptButtonLeaveAnimation?.isRunning == true) bulkAcceptButtonLeaveAnimation?.end()
        containerBtnBulkAction?.visible()
        bulkAcceptButtonEnterAnimation = containerBtnBulkAction?.animateSlide(containerBtnBulkAction?.height?.toFloat()
                ?: 0f, 0f)
    }

    private fun animateBulkAcceptOrderButtonLeave() {
        if (bulkAcceptButtonEnterAnimation?.isRunning == true) bulkAcceptButtonEnterAnimation?.end()
        bulkAcceptButtonLeaveAnimation = containerBtnBulkAction?.animateSlide(0f, containerBtnBulkAction?.height?.toFloat()
                ?: 0f)
        bulkAcceptButtonLeaveAnimation?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {}

            override fun onAnimationEnd(animation: Animator?) {
                containerBtnBulkAction?.gone()
            }

            override fun onAnimationCancel(animation: Animator?) {}

            override fun onAnimationStart(animation: Animator?) {}
        })
    }
}
