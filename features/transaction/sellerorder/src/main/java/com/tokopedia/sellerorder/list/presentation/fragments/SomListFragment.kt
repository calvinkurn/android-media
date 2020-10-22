package com.tokopedia.sellerorder.list.presentation.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.SomComponentInstance
import com.tokopedia.sellerorder.common.domain.model.SomAcceptOrderResponse
import com.tokopedia.sellerorder.common.domain.model.SomRejectOrderResponse
import com.tokopedia.sellerorder.common.domain.model.SomRejectRequestParam
import com.tokopedia.sellerorder.common.errorhandler.SomErrorHandler
import com.tokopedia.sellerorder.common.presenter.bottomsheet.SomOrderEditAwbBottomSheet
import com.tokopedia.sellerorder.common.presenter.bottomsheet.SomOrderRequestCancelBottomSheet
import com.tokopedia.sellerorder.common.presenter.model.PopUp
import com.tokopedia.sellerorder.common.presenter.model.Roles
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_STATUS_ID
import com.tokopedia.sellerorder.common.util.SomConsts.FROM_WIDGET_TAG
import com.tokopedia.sellerorder.common.util.SomConsts.TAB_ACTIVE
import com.tokopedia.sellerorder.common.util.SomConsts.TAB_STATUS
import com.tokopedia.sellerorder.common.util.Utils
import com.tokopedia.sellerorder.list.di.DaggerSomListComponent
import com.tokopedia.sellerorder.list.presentation.adapter.SomListOrderAdapter
import com.tokopedia.sellerorder.list.presentation.adapter.typefactories.SomListAdapterTypeFactory
import com.tokopedia.sellerorder.list.presentation.adapter.viewholders.SomListOrderViewHolder
import com.tokopedia.sellerorder.list.presentation.bottomsheets.SomListBulkAcceptOrderBottomSheet
import com.tokopedia.sellerorder.list.presentation.filtertabs.SomListSortFilterTab
import com.tokopedia.sellerorder.list.presentation.models.SomListEmptyStateUiModel
import com.tokopedia.sellerorder.list.presentation.models.SomListFilterUiModel
import com.tokopedia.sellerorder.list.presentation.models.SomListOrderUiModel
import com.tokopedia.sellerorder.list.presentation.models.SomListTickerUiModel
import com.tokopedia.sellerorder.list.presentation.navigator.SomListNavigator.REQUEST_CONFIRM_REQUEST_PICKUP
import com.tokopedia.sellerorder.list.presentation.navigator.SomListNavigator.REQUEST_CONFIRM_SHIPPING
import com.tokopedia.sellerorder.list.presentation.navigator.SomListNavigator.REQUEST_DETAIL
import com.tokopedia.sellerorder.list.presentation.navigator.SomListNavigator.REQUEST_FILTER
import com.tokopedia.sellerorder.list.presentation.navigator.SomListNavigator.goToConfirmShippingPage
import com.tokopedia.sellerorder.list.presentation.navigator.SomListNavigator.goToRequestPickupPage
import com.tokopedia.sellerorder.list.presentation.navigator.SomListNavigator.goToSomOrderDetail
import com.tokopedia.sellerorder.list.presentation.navigator.SomListNavigator.goToTrackingPage
import com.tokopedia.sellerorder.list.presentation.viewmodels.SomListViewModel
import com.tokopedia.sellerorder.requestpickup.data.model.SomProcessReqPickup
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.activity.WaitingPaymentOrderActivity
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_som_list.*
import kotlinx.coroutines.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class SomListFragment : BaseListFragment<Visitable<SomListAdapterTypeFactory>,
        SomListAdapterTypeFactory>(), SomListSortFilterTab.SomListSortFilterTabClickListener,
        TickerCallback, TickerPagerCallback, SearchInputView.Listener, SearchInputView.ResetListener,
        SomListOrderViewHolder.SomListOrderItemListener, CoroutineScope, SomListBulkAcceptOrderBottomSheet.SomListBulkAcceptOrderBottomSheetListener {

    companion object {
        private const val DELAY_SEARCH = 500L

        private const val TAG_BOTTOM_SHEET_BULK_ACCEPT = "bulkAcceptBottomSheet"

        private const val KEY_LAST_ACTIVE_FILTER = "lastActiveFilter"
        private const val KEY_LAST_SELECTED_ORDER_ID = "lastSelectedOrderId"

        private val allowedRoles = listOf(Roles.MANAGE_SHOPSTATS, Roles.MANAGE_INBOX, Roles.MANAGE_TA, Roles.MANAGE_TX)

        @JvmStatic
        fun newInstance(bundle: Bundle): SomListFragment {
            return SomListFragment().apply {
                arguments = Bundle().apply {
                    putString(FILTER_STATUS_ID, bundle.getString(FILTER_STATUS_ID))
                    putBoolean(FROM_WIDGET_TAG, bundle.getBoolean(FROM_WIDGET_TAG))
                    putString(TAB_ACTIVE, bundle.getString(TAB_ACTIVE))
                    putString(TAB_STATUS, bundle.getString(TAB_STATUS))
                }
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val masterJob = SupervisorJob()

    private val viewModel: SomListViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(SomListViewModel::class.java)
    }

    private val somListSortFilterTab: SomListSortFilterTab by lazy {
        SomListSortFilterTab(sortFilterSomList, this)
    }

    private var selectedOrderId: String = ""
    private var isRefreshingSelectedOrder: Boolean = false
    private var shouldScrollToTop: Boolean = false
    private var isJustRestored: Boolean = false // when restored, onSearchTextChanged is called which trigger unwanted refresh order list
    private var fromWidget: Boolean = false
    private var filterStatusId: Int = 0
    private var tabActive: String = ""
    private var somListBulkAcceptOrderBottomSheet: SomListBulkAcceptOrderBottomSheet? = null
    private var tickerPagerAdapter: TickerPagerAdapter? = null
    private var userNotAllowedDialog: DialogUnify? = null
    private var errorToaster: Snackbar? = null
    private var commonToaster: Snackbar? = null
    private var textChangeJob: Job? = null
    private var menu: Menu? = null

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + masterJob

    override fun getSwipeRefreshLayout(view: View?) = view?.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayoutSomList)
    override fun createAdapterInstance() = SomListOrderAdapter(adapterTypeFactory)
    override fun onItemClicked(t: Visitable<SomListAdapterTypeFactory>?) {}
    override fun getAdapterTypeFactory() = SomListAdapterTypeFactory(this)
    override fun getRecyclerViewResourceId() = R.id.rvSomList
    override fun getScreenName(): String = ""
    override fun initInjector() = inject()
    override fun onDismiss() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null && arguments != null) {
            filterStatusId = arguments?.getString(FILTER_STATUS_ID).orEmpty().toIntOrZero()
            fromWidget = arguments?.getBoolean(FROM_WIDGET_TAG) ?: false
            tabActive = arguments?.getString(TAB_ACTIVE).orEmpty()
        } else if (savedInstanceState != null) {
            isJustRestored = true
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
    }

    private fun Throwable.showErrorToaster() {
        if (this is UnknownHostException || this is SocketTimeoutException) {
            showNoInternetConnectionToaster()
        } else {
            showServerErrorToaster()
        }
    }

    private fun showNoInternetConnectionToaster() {
        showToasterError(getString(R.string.som_error_message_no_internet_connection))
    }

    private fun showServerErrorToaster() {
        showToasterError(getString(R.string.som_error_message_server_fault))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.menu_som_list, menu)
        this.menu = menu
        if (viewModel.waitingPaymentCounterResult.value is Success) {
            showWaitingPaymentOrderListMenu()
        } else {
            showWaitingPaymentOrderListMenuShimmer()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if ((item.itemId == R.id.som_list_action_waiting_payment_order ||
                        item.itemId == R.id.som_list_action_dotted_waiting_payment_order) &&
                viewModel.waitingPaymentCounterResult.value !is Fail) {
            showWaitingPaymentOrderListMenu()
            goToWaitingPaymentOrderListPage()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden && !isUserRoleFetched()) viewModel.getUserRoles()
        else if (hidden && isUserRoleFetched()) viewModel.clearUserRoles()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_DETAIL -> handleSomDetailActivityResult(resultCode, data)
            REQUEST_FILTER -> handleSomFilterActivityResult(resultCode, data)
            REQUEST_CONFIRM_SHIPPING -> handleSomConfirmShippingActivityResult(resultCode, data)
            REQUEST_CONFIRM_REQUEST_PICKUP -> handleSomRequestPickUpActivityResult(resultCode, data)
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        SomListOrderViewHolder.multiEditEnabled = false
        outState.putString(KEY_LAST_ACTIVE_FILTER, tabActive)
        outState.putString(KEY_LAST_SELECTED_ORDER_ID, selectedOrderId)
        super.onSaveInstanceState(outState)
    }

    override fun onPause() {
        childFragmentManager.fragments.forEach {
            if (it is BottomSheetUnify) it.dismiss()
        }
        super.onPause()
    }

    override fun loadInitialData() {
        SomListOrderViewHolder.multiEditEnabled = false
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
            status.key
        } else {
            viewModel.setStatusOrderFilter(emptyList())
            ""
        }
        if (SomListOrderViewHolder.multiEditEnabled) {
            SomListOrderViewHolder.multiEditEnabled = false
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
        }
    }

    override fun onDescriptionViewClick(linkUrl: CharSequence) {
        onTickerDescriptionClicked(linkUrl.toString())
    }

    override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
        onTickerDescriptionClicked(linkUrl.toString())
    }

    override fun onSearchSubmitted(text: String?) {
        viewModel.setSearchParam(text.orEmpty())
        shouldScrollToTop = true
        loadFilters(false)
        if (shouldReloadOrderListImmediately()) {
            refreshOrderList()
        }
    }

    override fun onSearchReset() {
        viewModel.setSearchParam("")
        shouldScrollToTop = true
        loadFilters(false)
        if (shouldReloadOrderListImmediately()) {
            refreshOrderList()
        }
    }

    override fun onSearchTextChanged(text: String?) {
        textChangeJob?.cancel()
        textChangeJob = launchCatchError(block = {
            delay(DELAY_SEARCH)
            viewModel.setSearchParam(text.orEmpty())
            if (!isJustRestored) {
                shouldScrollToTop = true
                loadFilters(false)
                if (shouldReloadOrderListImmediately()) {
                    refreshOrderList()
                }
            }
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
        showCommonToaster(getString(R.string.som_list_order_cannot_be_selected), Toaster.TYPE_ERROR)
    }

    override fun onOrderClicked(order: SomListOrderUiModel) {
        selectedOrderId = order.orderId
        goToSomOrderDetail(this, order, viewModel.userRoleResult.value)
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

    override fun onBulkAcceptOrderCompleted(totalSuccess: Int, totalFailed: Int) {
        loadFilters()
        if (shouldReloadOrderListImmediately()) {
            loadOrderList()
        }
        val stringBuilder = StringBuilder()
        stringBuilder.append(getString(R.string.som_list_bulk_accept_order_success_message, totalSuccess).takeIf { totalSuccess > 0 } ?: "")
        stringBuilder.append(getString(R.string.som_list_bulk_accept_order_failed_message, totalFailed).takeIf { totalFailed > 0 } ?: "")
        showCommonToaster(stringBuilder.toString())
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
                        if (rvSomList.visibility != View.VISIBLE) rvSomList.show()
                    }
                }
                is Fail -> showToasterError()
            }
        })
    }

    private fun observeTickers() {
        viewModel.tickerResult.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Success -> renderTickers(result.data)
                is Fail -> {
                    tickerSomList.gone()
                    showToasterError()
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
                is Success -> {
                    if (result.data.amount > 0) showDottedWaitingPaymentOrderListMenu()
                    else showWaitingPaymentOrderListMenu()
                }
                is Fail -> {
                    showWaitingPaymentOrderListMenu()
                    showToasterError()
                }
            }
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
                is Fail -> showToasterError(getString(R.string.som_list_failed_accept_order))
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
                        showToasterError(rejectOrderResponse.message.first())
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
                        showCommonToaster(successEditAwbResponse.mpLogisticEditRefNum.listMessage.first())
                        onActionCompleted()
                    } else {
                        showToasterError(getString(R.string.global_error))
                    }
                }
                is Fail -> {
                    val message = it.throwable.message.toString()
                    if (message.isNotEmpty()) {
                        showToasterError(message)
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
                    showCommonToaster(result.data.rejectCancelRequest.message)
                }
                is Fail -> {
                    SomErrorHandler.logExceptionToCrashlytics(result.throwable, SomConsts.ERROR_REJECT_CANCEL_ORDER)
                    result.throwable.showErrorToaster()
                }
            }
        })
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
            showCommonToaster(acceptOrderResponse.listMessage.firstOrNull())
        } else {
            showToasterError(acceptOrderResponse.listMessage.firstOrNull().orEmpty())
        }
    }

    private fun setupListeners() {
        tickerSomList.setDescriptionClickEvent(this)
        searchBarSomList.setListener(this)
        searchBarSomList.setResetListener(this)
        globalErrorSomList.setActionClickListener { loadInitialData() }

        tvSomListBulk.setOnClickListener {
            toggleBulkAction()
            resetOrderSelectedStatus()
            toggleBulkActionCheckboxVisibility()
            toggleBulkActionButtonVisibility()
            updateOrderCounter()
            checkBoxBulkAction.isChecked = false
            checkBoxBulkAction.setIndeterminate(false)
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

    private fun updateOrderCounter() {
        val text = if (SomListOrderViewHolder.multiEditEnabled) {
            val checkedCount = adapter.data.filterIsInstance<SomListOrderUiModel>().count { it.isChecked }
            getString(R.string.som_list_order_counter_multi_select_enabled, checkedCount)
        } else {
            getString(R.string.som_list_order_counter, somListSortFilterTab.getSelectedFilterOrderCount())
        }
        tvSomListOrderCounter.text = text
    }

    private fun handleSomFilterActivityResult(resultCode: Int, data: Intent?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
                        showCommonToaster(message)
                    }
                }
            }
        }
    }

    private fun handleRejectOrderResult(resultRejectOrder: SomRejectOrderResponse.Data.RejectOrder) {
        onActionCompleted()
        showCommonToaster(resultRejectOrder.message.firstOrNull())
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
        showCommonToaster(message)
    }

    private fun handleConfirmShippingResult(message: String?) {
        onActionCompleted()
        showCommonToaster(message)
    }

    private fun toggleBulkAction() {
        SomListOrderViewHolder.multiEditEnabled = !SomListOrderViewHolder.multiEditEnabled
    }

    private fun resetOrderSelectedStatus() {
        adapter.data.filterIsInstance<SomListOrderUiModel>().onEach { it.isChecked = false }
        adapter.notifyDataSetChanged()
    }

    private fun checkAllOrder() {
        adapter.data.onEach {
            if (it is SomListOrderUiModel && it.cancelRequest == 0) it.isChecked = true
        }
        adapter.notifyDataSetChanged()
    }

    private fun toggleBulkActionButtonVisibility() {
        val isAnyCheckedOrder = adapter.data.filterIsInstance<SomListOrderUiModel>().find { it.isChecked } != null
        containerBtnBulkAction.showWithCondition(SomListOrderViewHolder.multiEditEnabled && isAnyCheckedOrder)
    }

    private fun toggleBulkActionCheckboxVisibility() {
        checkBoxBulkAction.showWithCondition(SomListOrderViewHolder.multiEditEnabled)
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
        menu?.findItem(R.id.som_list_action_dotted_waiting_payment_order)?.isVisible = false
        menu?.findItem(R.id.som_list_action_waiting_payment_order)?.isVisible = false
    }

    private fun showWaitingPaymentOrderListMenu() {
        menu?.findItem(R.id.som_list_action_waiting_payment_order_shimmer)?.isVisible = false
        menu?.findItem(R.id.som_list_action_dotted_waiting_payment_order)?.isVisible = false
        menu?.findItem(R.id.som_list_action_waiting_payment_order)?.isVisible = true
    }

    private fun showDottedWaitingPaymentOrderListMenu() {
        menu?.findItem(R.id.som_list_action_waiting_payment_order_shimmer)?.isVisible = false
        menu?.findItem(R.id.som_list_action_dotted_waiting_payment_order)?.isVisible = true
        menu?.findItem(R.id.som_list_action_waiting_payment_order)?.isVisible = false
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
        somListLoading.gone()
        rvSomList.gone()
        multiEditViews.gone()
        val errorType = when (throwable) {
            is UnknownHostException, is SocketTimeoutException -> GlobalError.NO_CONNECTION
            else -> GlobalError.SERVER_ERROR
        }
        globalErrorSomList.setType(errorType)
        globalErrorSomList.show()
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
        tickerSomList.show()
    }

    private fun renderOrderList(data: List<SomListOrderUiModel>) {
        hideLoading()
        if (rvSomList.visibility != View.VISIBLE) rvSomList.show()
        // show only if current order list is based on current search keyword
        if (isLoadingInitialData && data.isEmpty()) {
            showEmptyState()
            multiEditViews.gone()
        } else if (data.firstOrNull()?.searchParam == searchBarSomList.searchText) {
            if (isLoadingInitialData) {
                (adapter as SomListOrderAdapter).updateOrders(data)
                tvSomListOrderCounter.text = getString(R.string.som_list_order_counter, somListSortFilterTab.getSelectedFilterOrderCount())
                multiEditViews.showWithCondition(somListSortFilterTab.shouldShowBulkAction())
                toggleBulkActionCheckboxVisibility()
                if (shouldScrollToTop) {
                    shouldScrollToTop = false
                    rvSomList.addOneTimeGlobalLayoutListener {
                        rvSomList.smoothScrollToPosition(0)
                    }
                }
            } else {
                adapter.addMoreData(data)
                updateBulkActionCheckboxStatus()
            }
        } else if (isRefreshingSelectedOrder) {
            isRefreshingSelectedOrder = false
            data.firstOrNull().let { newOrder ->
                if (newOrder == null) {
                    (adapter as SomListOrderAdapter).removeOrder(selectedOrderId)
                    somListSortFilterTab.decrementOrderCount()
                    updateOrderCounter()
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
        isJustRestored = false
    }

    private fun createSomListEmptyStateModel(isTopAdsActive: Boolean): Visitable<SomListAdapterTypeFactory> {
        return if (!isTopAdsActive && somListSortFilterTab.isNewOrderFilterSelected()) {
            SomListEmptyStateUiModel(
                    title = getString(R.string.empty_peluang_title),
                    description = getString(R.string.empty_peluang_desc_non_topads_no_filter),
                    buttonText = getString(R.string.btn_cek_peluang_non_topads),
                    buttonAppLink = ApplinkConstInternalTopAds.TOPADS_CREATE_ADS,
                    showButton = true
            )
        } else {
            SomListEmptyStateUiModel(
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
        isLoadingInitialData = true
        loadOrderList()
    }

    private fun showToasterError(message: String = getString(R.string.som_list_error_some_information_cannot_be_loaded)) {
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

    private fun showCommonToaster(message: String?, toasterType: Int = Toaster.TYPE_NORMAL) {
        message?.run {
            if (commonToaster == null) {
                view?.let {
                    commonToaster = Toaster.build(it, message, Toaster.LENGTH_SHORT, toasterType, "")
                }
            }
            if (commonToaster?.isShown == false) {
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

    private fun shouldReloadOrderListImmediately(): Boolean = tabActive.isBlank() || tabActive == SomConsts.STATUS_ALL_ORDER
    private fun isUserRoleFetched(): Boolean = viewModel.userRoleResult.value is Success
}