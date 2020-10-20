package com.tokopedia.sellerorder.list.presentation.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
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
import com.tokopedia.sellerorder.common.domain.model.SomAcceptOrder
import com.tokopedia.sellerorder.common.presenter.model.Roles
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_STATUS_ID
import com.tokopedia.sellerorder.common.util.SomConsts.FROM_WIDGET_TAG
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_CURR_IS_CHANGE_SHIPPING
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_ORDER_ID
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_SELLER
import com.tokopedia.sellerorder.common.util.SomConsts.TAB_ACTIVE
import com.tokopedia.sellerorder.common.util.SomConsts.TAB_STATUS
import com.tokopedia.sellerorder.common.util.Utils
import com.tokopedia.sellerorder.confirmshipping.presentation.activity.SomConfirmShippingActivity
import com.tokopedia.sellerorder.detail.data.model.SomRejectOrder
import com.tokopedia.sellerorder.detail.presentation.activity.SomDetailActivity
import com.tokopedia.sellerorder.list.di.SomListComponentInstance
import com.tokopedia.sellerorder.list.presentation.adapter.SomListOrderAdapter
import com.tokopedia.sellerorder.list.presentation.adapter.typefactories.SomListAdapterTypeFactory
import com.tokopedia.sellerorder.list.presentation.adapter.viewholders.SomListOrderViewHolder
import com.tokopedia.sellerorder.list.presentation.bottomsheets.SomListBulkAcceptOrderBottomSheet
import com.tokopedia.sellerorder.list.presentation.filtertabs.SomListSortFilterTab
import com.tokopedia.sellerorder.list.presentation.models.SomListEmptyStateUiModel
import com.tokopedia.sellerorder.list.presentation.models.SomListFilterUiModel
import com.tokopedia.sellerorder.list.presentation.models.SomListOrderUiModel
import com.tokopedia.sellerorder.list.presentation.models.SomListTickerUiModel
import com.tokopedia.sellerorder.list.presentation.viewmodels.SomListViewModel
import com.tokopedia.sellerorder.requestpickup.data.model.SomProcessReqPickup
import com.tokopedia.sellerorder.requestpickup.presentation.activity.SomConfirmReqPickupActivity
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.activity.WaitingPaymentOrderActivity
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_som_detail.*
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
        private const val REQEUST_DETAIL = 999
        private const val REQUEST_FILTER = 998
        private const val REQUEST_CONFIRM_SHIPPING = 997
        private const val REQUEST_CONFIRM_REQUEST_PICKUP = 996

        private const val TAG_BOTTOM_SHEET_BULK_ACCEPT = "bulkAcceptBottomSheet"

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

    private var needToRefreshOrder: Boolean = false
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
        if (arguments != null) {
            filterStatusId = arguments?.getString(FILTER_STATUS_ID).orEmpty().toIntOrZero()
            fromWidget = arguments?.getBoolean(FROM_WIDGET_TAG) ?: false
            tabActive = arguments?.getString(TAB_ACTIVE).orEmpty()
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
            REQEUST_DETAIL -> handleSomDetailActivityResult(resultCode, data)
            REQUEST_FILTER -> handleSomFilterActivityResult(resultCode, data)
            REQUEST_CONFIRM_SHIPPING -> handleSomConfirmShippingActivityResult(resultCode, data)
            REQUEST_CONFIRM_REQUEST_PICKUP -> handleSomRequestPickUpActivityResult(resultCode, data)
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun loadData(page: Int) {
        if (page == defaultInitialPage) {
            globalErrorSomList.gone()
            sortFilterSomList.invisible()
            multiEditViews.gone()
            shimmerViews.show()
            showWaitingPaymentOrderListMenuShimmer()
            viewModel.getUserRoles()
            viewModel.getTopAdsCategory()
            viewModel.getTickers()
            refreshFilter(false)
            viewModel.getWaitingPaymentCounter()
            if (tabActive.isBlank()) {
                viewModel.getOrderList()
            }
        } else {
            viewModel.getOrderList()
        }
    }

    override fun onTabClicked(status: SomListFilterUiModel.Status) {
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
            checkBoxBulkAction.isChecked = false
            checkBoxBulkAction.setIndeterminate(false)
        }
        getSwipeRefreshLayout(view)?.isEnabled = false
        refreshOrderList()
    }

    override fun onDescriptionViewClick(linkUrl: CharSequence) {
        onTickerDescriptionClicked(linkUrl.toString())
    }

    override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
        onTickerDescriptionClicked(linkUrl.toString())
    }

    override fun onSearchSubmitted(text: String?) {
        viewModel.setSearchParam(text.orEmpty())
        refreshOrderList()
    }

    override fun onSearchReset() {
        viewModel.setSearchParam("")
        refreshOrderList()
    }

    override fun onSearchTextChanged(text: String?) {
        textChangeJob?.cancel()
        textChangeJob = launchCatchError(block = {
            delay(500)
            viewModel.setSearchParam(text.orEmpty())
            refreshOrderList()
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
    }

    override fun onCheckBoxClickedWhenDisabled() {
        showCommonToaster(getString(R.string.som_list_order_cannot_be_selected), Toaster.TYPE_ERROR)
    }

    override fun onOrderClicked(order: SomListOrderUiModel) {
        goToSomOrderDetail(order)
    }

    override fun onTrackButtonClicked(orderId: String, url: String) {
        goToTrackingPage(orderId, url)
    }

    override fun onConfirmShippingButtonClicked(orderId: String) {
        goToConfirmShippingPage(orderId)
    }

    override fun onAcceptOrderButtonClicked(orderId: String) {
        viewModel.acceptOrder(orderId)
    }

    override fun onRequestPickupButtonClicked(orderId: String) {
        goToRequestPickupPage(orderId)
    }

    override fun onBulkAcceptOrderCompleted(totalSuccess: Int, totalFailed: Int) {
        val stringBuilder = StringBuilder()
        stringBuilder.append(getString(R.string.som_list_bulk_accept_order_success_message, totalSuccess).takeIf { totalSuccess > 0 }
                ?: "")
        stringBuilder.append(getString(R.string.som_list_bulk_accept_order_failed_message, totalFailed).takeIf { totalFailed > 0 }
                ?: "")
        refreshFilter(true)
    }

    private fun inject() {
        activity?.let {
            SomListComponentInstance.getSomComponent(it.application).inject(this)
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
                        createSomListEmptyStateModel(viewModel.isTopAdsActive())
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
                    if (tabActive.isNotBlank()) {
                        result.data.statusList.find { it.key == tabActive }?.let { activeFilter ->
                            activeFilter.isChecked = true
                            onTabClicked(activeFilter)
                            somListSortFilterTab.selectTab(activeFilter)
                        }
                    } else if (needToRefreshOrder) {
                        refreshOrderList()
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
                is Fail -> {
                    rvSomList.gone()
                    multiEditViews.gone()
                    globalErrorSomList.show()
                }
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

    private fun onAcceptOrderSuccess(acceptOrder: SomAcceptOrder.Data.AcceptOrder) {
        if (acceptOrder.success == 1) {
            refreshFilter(true)
            showCommonToaster(acceptOrder.listMessage.firstOrNull())
        } else {
            showToasterError(acceptOrder.listMessage.firstOrNull().orEmpty())
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
        }

        btnBulkAction.setOnClickListener {
            when (btnBulkAction.text) {
                getString(R.string.som_list_bulk_accept_order_button) -> showBulkAcceptOrderBottomSheet()
            }
        }
    }

    private fun handleSomFilterActivityResult(resultCode: Int, data: Intent?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun handleSomDetailActivityResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            when {
                data.hasExtra(SomConsts.RESULT_CONFIRM_SHIPPING) -> handleConfirmShippingResult(data.getStringExtra(SomConsts.RESULT_CONFIRM_SHIPPING))
                data.hasExtra(SomConsts.RESULT_ACCEPT_ORDER) -> {
                    data.getParcelableExtra<SomAcceptOrder.Data.AcceptOrder>(SomConsts.RESULT_ACCEPT_ORDER)?.let { resultAcceptOrder ->
                        onAcceptOrderSuccess(resultAcceptOrder)
                    }
                }
                data.hasExtra(SomConsts.RESULT_PROCESS_REQ_PICKUP) -> {
                    data.getParcelableExtra<SomProcessReqPickup.Data.MpLogisticRequestPickup>(SomConsts.RESULT_PROCESS_REQ_PICKUP)?.let { resultProcessReqPickup ->
                        handleRequestPickUpResult(resultProcessReqPickup.listMessage.firstOrNull())
                    }
                }
                data.hasExtra(SomConsts.RESULT_REJECT_ORDER) -> {
                    data.getParcelableExtra<SomRejectOrder.Data.RejectOrder>(SomConsts.RESULT_REJECT_ORDER)?.let { resultRejectOrder ->
                        refreshFilter(true)
                        showCommonToaster(resultRejectOrder.message.firstOrNull())
                    }
                }
                data.hasExtra(SomConsts.RESULT_SET_DELIVERED) -> {
                    data.getStringExtra(SomConsts.RESULT_SET_DELIVERED)?.let { message ->
                        refreshFilter(true)
                        showCommonToaster(message)
                    }
                }
            }
        }
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
        refreshFilter(true)
        showCommonToaster(message)
    }

    private fun handleConfirmShippingResult(message: String?) {
        refreshFilter(true)
        showCommonToaster(message)
    }

    private fun toggleBulkAction() {
        SomListOrderViewHolder.multiEditEnabled = !SomListOrderViewHolder.multiEditEnabled
    }

    private fun resetOrderSelectedStatus() {
        adapter.data.onEach { (it as SomListOrderUiModel).isChecked = false }
        adapter.notifyDataSetChanged()
    }

    private fun checkAllOrder() {
        adapter.data.onEach {
            if (it is SomListOrderUiModel && it.cancelRequest == 0) it.isChecked = true
        }
        adapter.notifyDataSetChanged()
    }

    private fun toggleBulkActionButtonVisibility() {
        containerBtnBulkAction.showWithCondition(SomListOrderViewHolder.multiEditEnabled)
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
        val errorType = when (throwable) {
            is UnknownHostException, is SocketTimeoutException -> GlobalError.NO_CONNECTION
            else -> GlobalError.SERVER_ERROR
        }
        somGlobalError.setType(errorType)
        somGlobalError.show()
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
        if (adapter.dataSize == 0 && data.isEmpty()) {
            val newItems = arrayListOf(createSomListEmptyStateModel(viewModel.isTopAdsActive()))
            (adapter as SomListOrderAdapter).updateOrders(newItems)
            multiEditViews.gone()
        } else if (data.firstOrNull()?.searchParam == searchBarSomList.searchText) { // show only if current order list is based on current search keyword
            if (adapter.dataSize == 0) {
                (adapter as SomListOrderAdapter).updateOrders(data)
                tvSomListOrderCounter.text = getString(R.string.som_list_order_counter, somListSortFilterTab.getSelectedFilterOrderCount())
                multiEditViews.showWithCondition(somListSortFilterTab.shouldShowBulkAction())
            } else {
                adapter.addMoreData(data)
                updateBulkActionCheckboxStatus()
            }
        }
        updateScrollListenerState(viewModel.hasNextPage())
        rvSomList.show()
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
        adapter.clearAllElements()
        showLoading()
        viewModel.resetNextOrderId()
        viewModel.getOrderList()
    }

    private fun refreshFilter(needToRefreshOrder: Boolean) {
        this.needToRefreshOrder = needToRefreshOrder
        viewModel.getFilters()
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

    private fun goToSomOrderDetail(item: SomListOrderUiModel) {
        val userRolesResult = viewModel.userRoleResult.value
        val userRoles = if (userRolesResult != null && userRolesResult is Success) userRolesResult.data else null
        Intent(activity, SomDetailActivity::class.java).apply {
            putExtra(PARAM_ORDER_ID, item.orderId)
            userRoles?.let {
                putExtra(SomConsts.PARAM_USER_ROLES, it)
            }
            startActivityForResult(this, REQEUST_DETAIL)
        }
    }

    private fun goToTrackingPage(orderId: String, url: String) {
        var routingAppLink: String = ApplinkConst.ORDER_TRACKING.replace("{order_id}", orderId)
        val uriBuilder = Uri.Builder()
        uriBuilder.appendQueryParameter(ApplinkConst.Query.ORDER_TRACKING_URL_LIVE_TRACKING, url)
        uriBuilder.appendQueryParameter(ApplinkConst.Query.ORDER_TRACKING_CALLER, PARAM_SELLER)
        routingAppLink += uriBuilder.toString()
        RouteManager.route(context, routingAppLink)
    }

    private fun goToConfirmShippingPage(orderId: String) {
        Intent(activity, SomConfirmShippingActivity::class.java).apply {
            putExtra(PARAM_ORDER_ID, orderId)
            putExtra(PARAM_CURR_IS_CHANGE_SHIPPING, false)
            startActivityForResult(this, REQUEST_CONFIRM_SHIPPING)
        }
    }

    private fun goToRequestPickupPage(orderId: String) {
        Intent(activity, SomConfirmReqPickupActivity::class.java).apply {
            putExtra(PARAM_ORDER_ID, orderId)
            startActivityForResult(this, REQUEST_CONFIRM_REQUEST_PICKUP)
        }
    }

    private fun isUserRoleFetched(): Boolean = viewModel.userRoleResult.value is Success
}