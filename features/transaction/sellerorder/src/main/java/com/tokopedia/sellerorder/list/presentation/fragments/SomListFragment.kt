package com.tokopedia.sellerorder.list.presentation.fragments

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.applink.sellerhome.AppLinkMapperSellerHome.QUERY_PARAM_SEARCH
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.coachmark.CoachMarkPreference
import com.tokopedia.config.GlobalConfig
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.seller.active.common.plt.som.SomListLoadTimeMonitoring
import com.tokopedia.seller.active.common.plt.som.SomListLoadTimeMonitoringActivity
import com.tokopedia.seller_migration_common.listener.SellerHomeFragmentListener
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
import com.tokopedia.sellerorder.common.navigator.SomNavigator.goToReturnToShipper
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
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_REQUEST_PICKUP
import com.tokopedia.sellerorder.common.util.SomConsts.RESULT_CONFIRM_SHIPPING
import com.tokopedia.sellerorder.common.util.SomConsts.STATUS_ALL_ORDER
import com.tokopedia.sellerorder.common.util.SomConsts.STATUS_NEW_ORDER
import com.tokopedia.sellerorder.common.util.SomConsts.TAB_ACTIVE
import com.tokopedia.sellerorder.common.util.SomConsts.TAB_STATUS
import com.tokopedia.sellerorder.common.util.Utils
import com.tokopedia.sellerorder.common.util.Utils.hideKeyboard
import com.tokopedia.sellerorder.common.util.Utils.setUserNotAllowedToViewSom
import com.tokopedia.sellerorder.common.util.Utils.updateShopActive
import com.tokopedia.sellerorder.databinding.FragmentSomListBinding
import com.tokopedia.sellerorder.databinding.SomListHeaderBinding
import com.tokopedia.sellerorder.common.presenter.bottomsheet.SomConfirmShippingBottomSheet
import com.tokopedia.sellerorder.filter.presentation.bottomsheet.SomFilterBottomSheet
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterCancelWrapper
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterUiModel
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterUiModelWrapper
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterUtil
import com.tokopedia.sellerorder.list.di.DaggerSomListComponent
import com.tokopedia.sellerorder.list.domain.model.SomListGetOrderListParam
import com.tokopedia.sellerorder.list.presentation.adapter.SomListOrderAdapter
import com.tokopedia.sellerorder.list.presentation.adapter.typefactories.SomListAdapterTypeFactory
import com.tokopedia.sellerorder.list.presentation.adapter.typefactories.SomListBulkProcessOrderTypeFactory
import com.tokopedia.sellerorder.list.presentation.adapter.viewholders.SomListOrderEmptyViewHolder
import com.tokopedia.sellerorder.list.presentation.adapter.viewholders.SomListOrderMultiSelectSectionViewHolder
import com.tokopedia.sellerorder.list.presentation.adapter.viewholders.SomListOrderViewHolder
import com.tokopedia.sellerorder.list.presentation.animator.SomItemAnimator
import com.tokopedia.sellerorder.list.presentation.bottomsheets.SomListBulkProcessOrderBottomSheet
import com.tokopedia.sellerorder.list.presentation.dialogs.SomListBulkAcceptOrderDialog
import com.tokopedia.sellerorder.list.presentation.dialogs.SomListBulkPrintDialog
import com.tokopedia.sellerorder.list.presentation.dialogs.SomListBulkRequestPickupDialog
import com.tokopedia.sellerorder.list.presentation.filtertabs.SomListOrderStatusFilterTab
import com.tokopedia.sellerorder.list.presentation.filtertabs.SomListSortFilterTab
import com.tokopedia.sellerorder.list.presentation.models.AllFailEligible
import com.tokopedia.sellerorder.list.presentation.models.AllNotEligible
import com.tokopedia.sellerorder.list.presentation.models.AllSuccess
import com.tokopedia.sellerorder.list.presentation.models.AllValidationFail
import com.tokopedia.sellerorder.list.presentation.models.FailRetry
import com.tokopedia.sellerorder.list.presentation.models.NotEligibleAndFail
import com.tokopedia.sellerorder.list.presentation.models.OptionalOrderData
import com.tokopedia.sellerorder.list.presentation.models.PartialSuccess
import com.tokopedia.sellerorder.list.presentation.models.PartialSuccessNotEligible
import com.tokopedia.sellerorder.list.presentation.models.PartialSuccessNotEligibleFail
import com.tokopedia.sellerorder.list.presentation.models.PlusIconInfo
import com.tokopedia.sellerorder.list.presentation.models.ServerFail
import com.tokopedia.sellerorder.list.presentation.models.SomListBulkProcessOrderDescriptionUiModel
import com.tokopedia.sellerorder.list.presentation.models.SomListBulkProcessOrderMenuItemUiModel
import com.tokopedia.sellerorder.list.presentation.models.SomListBulkProcessOrderProductUiModel
import com.tokopedia.sellerorder.list.presentation.models.SomListEmptyStateUiModel
import com.tokopedia.sellerorder.list.presentation.models.SomListFilterUiModel
import com.tokopedia.sellerorder.list.presentation.models.SomListMultiSelectSectionUiModel
import com.tokopedia.sellerorder.list.presentation.models.SomListOrderUiModel
import com.tokopedia.sellerorder.list.presentation.models.SomListOrderWrapperUiModel
import com.tokopedia.sellerorder.list.presentation.models.SomListTickerUiModel
import com.tokopedia.sellerorder.list.presentation.util.SomListCoachMarkManager
import com.tokopedia.sellerorder.list.presentation.viewmodels.SomListViewModel
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.activity.WaitingPaymentOrderActivity
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

open class SomListFragment :
    BaseListFragment<Visitable<SomListAdapterTypeFactory>,
        SomListAdapterTypeFactory>(),
    SomListSortFilterTab.SomListSortFilterTabClickListener,
    TickerCallback,
    TickerPagerCallback,
    TextWatcher,
    SomListOrderViewHolder.SomListOrderItemListener,
    CoroutineScope,
    SomListBulkProcessOrderBottomSheet.SomListBulkProcessOrderBottomSheetListener,
    SomFilterBottomSheet.SomFilterFinishListener,
    SomListOrderEmptyViewHolder.SomListEmptyStateListener,
    SomListBulkPrintDialog.SomListBulkPrintDialogClickListener,
    SellerHomeFragmentListener,
    SomListOrderStatusFilterTab.Listener,
    SomListOrderMultiSelectSectionViewHolder.Listener {

    companion object {
        private const val DELAY_SEARCH = 500L
        private const val BUTTON_ENTER_LEAVE_ANIMATION_DURATION = 300L
        private const val TICKER_ENTER_LEAVE_ANIMATION_DURATION = 300L
        private const val TICKER_ENTER_LEAVE_ANIMATION_DELAY = 10L
        private const val RV_TOP_POSITION = 0
        private const val SEARCH_BAR_MARGIN_START_MAIN_APP = 0
        private const val SEARCH_BAR_MARGIN_START_SELLER_APP = 14
        private const val SEARCH_BAR_MARGIN_END = 12

        private const val KEY_LAST_SELECTED_ORDER_ID = "lastSelectedOrderId"
        private const val SHARED_PREF_SOM_LIST_TAB_COACH_MARK = "somListTabCoachMark"

        @JvmStatic
        fun newInstance(bundle: Bundle): SomListFragment {
            return SomListFragment().apply {
                arguments = Bundle().apply {
                    putString(FILTER_STATUS_ID, bundle.getString(FILTER_STATUS_ID))
                    putBoolean(FROM_WIDGET_TAG, bundle.getBoolean(FROM_WIDGET_TAG))
                    putString(TAB_ACTIVE, bundle.getString(TAB_ACTIVE))
                    putString(TAB_STATUS, bundle.getString(TAB_STATUS))
                    putString(QUERY_PARAM_SEARCH, bundle.getString(QUERY_PARAM_SEARCH))
                    putString(FILTER_ORDER_TYPE, bundle.getString(FILTER_ORDER_TYPE))
                    putString(SomConsts.COACHMARK_KEY, bundle.getString(SomConsts.COACHMARK_KEY))
                }
            }
        }
    }

    private val masterJob = SupervisorJob()

    private val somListOrderStatusFilterTab: SomListOrderStatusFilterTab? by lazy {
        somListBinding?.somListTabFilter?.let {
            SomListOrderStatusFilterTab(it, this)
        }
    }
    private val somListSortFilterTab: SomListSortFilterTab? by lazy {
        somListBinding?.sortFilterSomList?.let {
            SomListSortFilterTab(it, this)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private val maskTouchListener = View.OnTouchListener { _, event ->
        if (event.action == MotionEvent.ACTION_DOWN) {
            view.hideKeyboard()
        }
        false
    }

    private val somListLayoutManager by lazy { somListBinding?.rvSomList?.layoutManager as? LinearLayoutManager }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    private var bulkAcceptButtonEnterAnimation: ValueAnimator? = null
    private var bulkAcceptButtonLeaveAnimation: ValueAnimator? = null
    private var isWaitingPaymentOrderPageOpened: Boolean = false
    private var shouldScrollToTop: Boolean = false
    private var skipSearch: Boolean =
        false // when restored, onSearchTextChanged is called which trigger unwanted refresh order list
    private var canDisplayOrderData = false
    private var canMultiAcceptOrder = false
    private var somOrderHasCancellationRequestDialog: SomOrderHasRequestCancellationDialog? = null
    private var somListBulkProcessOrderBottomSheet: SomListBulkProcessOrderBottomSheet? = null
    private var orderRequestCancelBottomSheet: SomOrderRequestCancelBottomSheet? = null
    private var somOrderEditAwbBottomSheet: SomOrderEditAwbBottomSheet? = null
    private var bulkAcceptOrderDialog: SomListBulkAcceptOrderDialog? = null
    private var bulkRequestPickupDialog: SomListBulkRequestPickupDialog? = null
    private var tickerPagerAdapter: TickerPagerAdapter? = null
    private var errorToaster: Snackbar? = null
    private var commonToaster: Snackbar? = null
    private var textChangeJob: Job? = null
    private var pendingAction: SomPendingAction? = null
    private var tickerIsReady = false
    private var coachMarkManager: SomListCoachMarkManager? = null

    protected var somListLoadTimeMonitoring: SomListLoadTimeMonitoring? = null
    protected var selectedOrderId: String = ""
    protected val viewModel: SomListViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(SomListViewModel::class.java)
    }

    private val autoTabbingCoachMark: CoachMark2? by lazy {
        somListBinding?.root?.context?.let {
            CoachMark2(it)
        }
    }

    // The isEnabledCoachmark allows the EP team to automate tests with coachmark hiding on the SOM Page
    private val isEnabledCoachmark: Boolean by lazy {
        arguments?.getString(SomConsts.COACHMARK_KEY).orEmpty() != SomConsts.COACHMARK_DISABLED
    }

    protected var somListBinding by autoClearedNullable<FragmentSomListBinding> {
        somListBulkProcessOrderBottomSheet?.clearViewBinding()
        orderRequestCancelBottomSheet?.clearViewBinding()
        somOrderEditAwbBottomSheet?.clearViewBinding()
    }

    protected var somListHeaderBinding by autoClearedNullable<SomListHeaderBinding>()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + masterJob

    override fun getSwipeRefreshLayoutResourceId() = R.id.swipeRefreshLayoutSomList
    override fun getSwipeRefreshLayout(view: View?) =
        view?.findViewById<SwipeRefreshLayout>(swipeRefreshLayoutResourceId)

    override fun createAdapterInstance() = SomListOrderAdapter(adapterTypeFactory)
    override fun onItemClicked(t: Visitable<SomListAdapterTypeFactory>?) {}
    override fun getAdapterTypeFactory() = SomListAdapterTypeFactory(this, this, this)
    override fun getRecyclerViewResourceId() = R.id.rvSomList
    override fun getRecyclerView(view: View?) =
        view?.findViewById<RecyclerView>(recyclerViewResourceId)

    override fun getScreenName(): String = ""
    override fun initInjector() = inject()
    override fun onDismiss() {
        animateOrderTicker(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getActivityPltPerformanceMonitoring()
        if (savedInstanceState == null && arguments != null) {
            setTabActiveFromAppLink()
            arguments?.getString(FILTER_ORDER_TYPE)?.toLongOrNull()
                ?.let { viewModel.addOrderTypeFilter(it) }
        } else if (savedInstanceState != null) {
            skipSearch = true
            selectedOrderId = savedInstanceState.getString(KEY_LAST_SELECTED_ORDER_ID).orEmpty()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(false)
        somListBinding = FragmentSomListBinding.inflate(inflater, container, false)
        somListHeaderBinding = somListBinding?.somListToolbar
        return somListBinding?.root
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
        observeSomListHeaderIconsInfo()
        observeOrderList()
        observeRefreshOrder()
        observeAcceptOrder()
        observeRejectCancelRequest()
        observeRejectOrder()
        observeEditAwb()
        observeBulkAcceptOrder()
        observeBulkAcceptOrderStatus()
        observeBulkRequestPickup()
        observeBulkRequestPickupFinalResult()
        observeValidateOrder()
        observeIsAdminEligible()
        observeRefreshOrderRequest()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            SomAnalytics.sendScreenName(SomConsts.LIST_ORDER_SCREEN_NAME)
            coachMarkManager?.showCoachMark()
        } else {
            coachMarkManager?.dismissCoachMark()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        getSwipeRefreshLayout(view)?.isRefreshing = viewModel.isRefreshingOrder()
        when (requestCode) {
            SomNavigator.REQUEST_DETAIL -> handleSomDetailActivityResult(resultCode, data)
            SomNavigator.REQUEST_CONFIRM_SHIPPING -> handleSomConfirmShippingActivityResult(
                resultCode,
                data
            )
            SomNavigator.REQUEST_CONFIRM_REQUEST_PICKUP -> handleSomRequestPickUpActivityResult(
                resultCode,
                data
            )
            SomNavigator.REQUEST_CHANGE_COURIER -> handleSomChangeCourierActivityResult(
                resultCode,
                data
            )
            SomNavigator.REQUEST_RETURN_TO_SHIPPER -> handleSomReturnToShipperActivityResult(
                resultCode
            )
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(KEY_LAST_SELECTED_ORDER_ID, selectedOrderId)
        super.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        if (!isHidden) coachMarkManager?.showCoachMark()
        updateShopActive()
    }

    override fun onPause() {
        dismissBottomSheets()
        coachMarkManager?.dismissCoachMark()
        super.onPause()
        if (bulkAcceptButtonEnterAnimation?.isRunning == true) bulkAcceptButtonEnterAnimation?.end()
        if (bulkAcceptButtonLeaveAnimation?.isRunning == true) bulkAcceptButtonLeaveAnimation?.end()
    }

    override fun onDestroy() {
        cleanupResources()
        super.onDestroy()
    }

    override fun onFragmentBackPressed(): Boolean {
        return dismissBottomSheets()
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
        coachMarkManager?.dismissCoachMark()
    }

    override fun hideLoading() {
        if (!viewModel.isRefreshingOrder()) {
            getSwipeRefreshLayout(view)?.apply {
                isEnabled = true
                isRefreshing = false
            }
            hideOrderShimmer()
            adapter?.hideLoading()
        }
    }

    override fun onTabClicked(
        quickFilter: SomListFilterUiModel.QuickFilter,
        shouldScrollToTop: Boolean
    ) {
        if (quickFilter.isChecked) {
            if (quickFilter.isOrderTypeFilter()) {
                viewModel.addOrderTypeFilter(quickFilter.id)
            } else if (quickFilter.isShippingFilter()) {
                viewModel.addShippingFilter(quickFilter.id)
            }
            SomAnalytics.eventClickStatusFilter(listOf(quickFilter.id.toString()), quickFilter.name)
        } else {
            if (quickFilter.isOrderTypeFilter()) {
                viewModel.removeOrderTypeFilter(quickFilter.id)
            } else if (quickFilter.isShippingFilter()) {
                viewModel.removeShippingFilter(quickFilter.id)
            }
        }
        if (viewModel.isMultiSelectEnabled) {
            context.let { context ->
                if (context == null || !DeviceScreenInfo.isTablet(context)) {
                    somListLayoutManager?.findFirstVisibleItemPosition()?.let {
                        somListLayoutManager?.findViewByPosition(it)
                            ?.findViewById<View>(R.id.btnQuickAction)
                            ?.addOneTimeGlobalLayoutListener {
                                refreshOrders(
                                    shouldScrollToTop = shouldScrollToTop,
                                    refreshFilter = true
                                )
                            }
                    }
                } else {
                    refreshOrders(
                        shouldScrollToTop = shouldScrollToTop,
                        refreshFilter = true
                    )
                }
            }
            onToggleMultiSelectClicked()
        } else {
            refreshOrders(shouldScrollToTop = shouldScrollToTop, refreshFilter = true)
        }
    }

    override fun onParentSortFilterClicked() {
        val cacheManager = context?.let { SaveInstanceCacheManager(it, true) }
        cacheManager?.put(
            SomFilterBottomSheet.KEY_SOM_LIST_GET_ORDER_PARAM,
            viewModel.getDataOrderListParams()
        )
        viewModel.getSomFilterUi().let { somFilterList ->
            val somFilterUiModelWrapper = SomFilterUiModelWrapper(somFilterList)
            cacheManager?.put(SomFilterBottomSheet.KEY_SOM_FILTER_LIST, somFilterUiModelWrapper)
            val somFilterBottomSheet = SomFilterBottomSheet.createInstance(
                viewModel.getDataOrderListParams().statusList,
                cacheManager?.id.orEmpty()
            )
            somFilterBottomSheet.setSomFilterFinishListener(this)
            somFilterBottomSheet.show(childFragmentManager)
        }
        somListOrderStatusFilterTab?.getSelectedFilterStatus().let {
            val selectedFilterKeys = arrayListOf<String>()
            if (it.isNullOrBlank()) {
                selectedFilterKeys.add(Int.ZERO, STATUS_ALL_ORDER)
            } else {
                selectedFilterKeys.add(Int.ZERO, it)
            }
            SomAnalytics.eventClickFilter(selectedFilterKeys)
        }
    }

    override fun onClickOrderStatusFilterTab(
        status: SomListFilterUiModel.Status,
        shouldScrollToTop: Boolean
    ) {
        viewModel.setStatusOrderFilter(status.id, status.key)
        setDefaultSortByValue()
        SomAnalytics.eventClickStatusFilter(status.id.map { it.toString() }, status.status)
        if (viewModel.isMultiSelectEnabled) {
            context.let { context ->
                if (context == null || !DeviceScreenInfo.isTablet(context)) {
                    val firstVisibleItemIndex = somListLayoutManager?.findFirstVisibleItemPosition().orZero()
                    val lastVisibleItemIndex = somListLayoutManager?.findLastVisibleItemPosition().orZero()
                    val rangeVisibleItemIndex = firstVisibleItemIndex..lastVisibleItemIndex
                    for (i in rangeVisibleItemIndex) {
                        val viewHolderView = somListLayoutManager?.findViewByPosition(i)
                        if (viewHolderView != null) {
                            viewHolderView.findViewById<View>(R.id.btnQuickAction)
                                ?.addOneTimeGlobalLayoutListener {
                                    refreshOrders(
                                        shouldScrollToTop = shouldScrollToTop,
                                        refreshFilter = true
                                    )
                                }
                        } else if (i == lastVisibleItemIndex) {
                            refreshOrders(
                                shouldScrollToTop = shouldScrollToTop,
                                refreshFilter = true
                            )
                        }
                    }
                } else {
                    refreshOrders(
                        shouldScrollToTop = shouldScrollToTop,
                        refreshFilter = true
                    )
                }
            }
            onToggleMultiSelectClicked()
        } else {
            refreshOrders(shouldScrollToTop = shouldScrollToTop, refreshFilter = true)
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
                val loadOrderListImmediately = shouldReloadOrderListImmediately()
                shouldScrollToTop = true
                loadFilters(showShimmer = false, loadOrders = !loadOrderListImmediately)
                if (loadOrderListImmediately) {
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
                    RouteManager.route(
                        context,
                        String.format("%s?url=%s", ApplinkConst.WEBVIEW, linkUrl)
                    )
                } else {
                    RouteManager.route(context, linkUrl)
                }
            }
        }
    }

    override fun onCheckChanged() {
        (adapter as? SomListOrderAdapter)?.updateSomListMultiSelectSectionUiModel(
            SomListMultiSelectSectionUiModel(
                isEnabled = viewModel.isMultiSelectEnabled,
                totalOrder = somListOrderStatusFilterTab?.getSelectedFilterOrderCount().orZero(),
                totalSelected = adapter.data.filterIsInstance<SomListOrderUiModel>().count { it.isChecked },
                totalSelectable = adapter.data.count { it is SomListOrderUiModel && !it.isOrderWithCancellationRequest() }
            )
        )
        toggleBulkActionButtonVisibility()
    }

    override fun onCheckBoxClickedWhenDisabled() {
        showCommonToaster(
            view,
            context?.resources?.getString(R.string.som_list_order_cannot_be_selected).orEmpty(),
            Toaster.TYPE_ERROR
        )
    }

    override fun onStartAdvertiseButtonClicked() {
        SomAnalytics.eventClickStartAdvertise(
            somListOrderStatusFilterTab?.getSelectedFilterStatus().orEmpty(),
            somListOrderStatusFilterTab?.getSelectedFilterStatusName().orEmpty()
        )
    }

    override fun onOrderClicked(order: SomListOrderUiModel) {
        selectedOrderId = order.orderId
        goToSomOrderDetail(this, order.orderId)
        SomAnalytics.eventClickOrderCard(order.orderStatusId, order.status)
    }

    override fun onTrackButtonClicked(orderId: String, url: String) {
        goToTrackingPage(context, orderId, url)
    }

    override fun onConfirmShippingButtonClicked(
        actionName: String,
        orderId: String,
        skipValidateOrder: Boolean
    ) {
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

    override fun onConfirmShippingAutoButtonClicked(popUp: PopUp?) {
        popUp?.apply {
            if (popUp.template.code != "") {
                SomConfirmShippingBottomSheet.instance(context, view, popUp)
            }
        }
    }

    override fun onAcceptOrderButtonClicked(
        actionName: String,
        orderId: String,
        skipValidateOrder: Boolean
    ) {
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

    override fun onRequestPickupButtonClicked(
        actionName: String,
        orderId: String,
        skipValidateOrder: Boolean
    ) {
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
                selectedOrderId = order.orderId
                orderRequestCancelBottomSheet = orderRequestCancelBottomSheet?.apply {
                    setupBuyerRequestCancelBottomSheet(this, order)
                } ?: SomOrderRequestCancelBottomSheet(it.context).apply {
                    setupBuyerRequestCancelBottomSheet(this, order)
                }
                orderRequestCancelBottomSheet?.init(it)
                orderRequestCancelBottomSheet?.show()
                return
            }
        }
        showCommonToaster(view, "Terjadi kesalahan, silahkan coba lagi.")
    }

    override fun onViewComplaintButtonClicked(order: SomListOrderUiModel) {
        RouteManager.route(
            context,
            String.format(
                "%s?url=%s",
                ApplinkConst.WEBVIEW,
                order.buttons.firstOrNull()?.url.orEmpty()
            )
        )
    }

    override fun onEditAwbButtonClicked(orderId: String) {
        view?.let {
            if (it is ViewGroup) {
                somOrderEditAwbBottomSheet = somOrderEditAwbBottomSheet?.apply {
                    setupSomOrderEditAwbBottomSheet(this, orderId)
                } ?: SomOrderEditAwbBottomSheet(it.context).apply {
                    setupSomOrderEditAwbBottomSheet(this, orderId)
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

    override fun onFinishBindOrder(view: View, itemIndex: Int) {
        if (somListBinding?.rvSomList?.scrollState == RecyclerView.SCROLL_STATE_IDLE) {
            coachMarkManager?.showCoachMark()
        }
    }

    override fun onReturnToShipper(orderId: String) {
        selectedOrderId = orderId
        goToReturnToShipper(this, orderId)
    }

    override fun onBulkProcessOrderButtonClicked() {
        viewModel.bulkAcceptOrder(getSelectedOrderIds())
        SomAnalytics.eventClickBulkAcceptOrder(userSession.userId, userSession.shopId, getSelectedOrderIds())
    }

    override fun onMenuItemClicked(keyAction: String) {
        when (keyAction) {
            KEY_PRINT_AWB -> {
                context?.let { context ->
                    SomListBulkPrintDialog(context).run {
                        setTitle(
                            getString(
                                R.string.som_list_bulk_print_dialog_title,
                                getSelectedOrderIds().size
                            )
                        )
                        setListener(this@SomListFragment)
                        show()
                    }
                    SomAnalytics.eventClickBulkPrintAwb(userSession.userId)
                }
            }

            KEY_REQUEST_PICKUP -> {
                showProgressBulkRequestPickupDialog(getSelectedOrderIds().size.toLong().orZero())
                viewModel.bulkRequestPickup(getSelectedOrderIds())
                SomAnalytics.eventClickBulkRequestPickup(
                    userSession.userId,
                    userSession.shopId,
                    getSelectedOrderIds()
                )
            }
        }
    }

    override fun onPrintButtonClicked(markAsPrinted: Boolean) {
        goToPrintAwb(activity, view, getSelectedOrderIds(), markAsPrinted)
        SomAnalytics.eventClickYesOnBulkPrintAwb(userSession.userId)
    }

    override fun onScrollToTop() {
        somListBinding?.rvSomList?.post {
            somListBinding?.rvSomList?.smoothScrollToPosition(RV_TOP_POSITION)
        }
    }

    override fun onToggleMultiSelectClicked() {
        toggleBulkAction()
        resetMultiSelectState()
        toggleBulkActionButtonVisibility()
    }

    override fun onCheckAllOrderClicked() {
        (adapter as? SomListOrderAdapter)?.checkAllOrder(
            SomListMultiSelectSectionUiModel(
                isEnabled = viewModel.isMultiSelectEnabled,
                totalOrder = somListOrderStatusFilterTab?.getSelectedFilterOrderCount().orZero(),
                totalSelected = adapter.data.count { it is SomListOrderUiModel && !it.isOrderWithCancellationRequest() },
                totalSelectable = adapter.data.count { it is SomListOrderUiModel && !it.isOrderWithCancellationRequest() }
            )
        )
        toggleBulkActionButtonVisibility()
    }

    override fun onUncheckAllOrderClicked() {
        resetMultiSelectState()
        toggleBulkActionButtonVisibility()
    }

    private fun setupBuyerRequestCancelBottomSheet(
        somOrderRequestCancelBottomSheet: SomOrderRequestCancelBottomSheet,
        order: SomListOrderUiModel
    ) {
        somOrderRequestCancelBottomSheet.apply {
            setListener(object :
                    SomOrderRequestCancelBottomSheet.SomOrderRequestCancelBottomSheetListener {
                    override fun onAcceptOrder(actionName: String) {
                        onAcceptOrderButtonClicked(actionName, selectedOrderId, true)
                    }

                    override fun onRejectOrder(reasonBuyer: String) {
                        SomAnalytics.eventClickButtonTolakPesananPopup(
                            "${order.orderStatusId}",
                            order.status
                        )
                        val orderRejectRequest = SomRejectRequestParam(
                            orderId = selectedOrderId,
                            rCode = Int.ZERO.toString(),
                            reason = reasonBuyer
                        )
                        rejectOrder(orderRejectRequest)
                    }

                    override fun onRejectCancelRequest() {
                        SomAnalytics.eventClickButtonTolakPesananPopup(
                            "${order.orderStatusId}",
                            order.status
                        )
                        rejectCancelOrder(selectedOrderId)
                    }
                })
            init(
                order.buttons.firstOrNull()?.popUp ?: PopUp(),
                order.cancelRequestOriginNote,
                order.orderStatusId
            )
            hideKnob()
            showCloseButton()
        }
    }

    private fun setupSomOrderEditAwbBottomSheet(
        somOrderEditAwbBottomSheet: SomOrderEditAwbBottomSheet,
        orderId: String
    ) {
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

    private fun cleanupResources() {
        bulkAcceptButtonEnterAnimation = null
        bulkAcceptButtonLeaveAnimation = null
        somOrderHasCancellationRequestDialog = null
        somListBulkProcessOrderBottomSheet = null
        orderRequestCancelBottomSheet = null
        somOrderEditAwbBottomSheet = null
        bulkAcceptOrderDialog = null
        bulkRequestPickupDialog = null
        tickerPagerAdapter = null
        errorToaster = null
        commonToaster = null
        textChangeJob = null
        pendingAction = null
        coachMarkManager = null
        somListLoadTimeMonitoring = null
    }

    private fun setDefaultSortByValue() {
        viewModel.setSortOrderBy(SomFilterUtil.getDefaultSortBy(viewModel.getTabActive()))
    }

    private fun getOrderBy(orderId: String): String {
        return (
            adapter.data.firstOrNull {
                it is SomListOrderUiModel && it.orderId == orderId
            } as? SomListOrderUiModel
            )?.orderResi.orEmpty()
    }

    private fun showBulkAcceptOrderDialog(orderCount: Int) {
        if (bulkAcceptOrderDialog?.getDialogUnify()?.isShowing == true) {
            bulkAcceptOrderDialog?.dismiss()
        }
        context?.let { context ->
            bulkAcceptOrderDialog = SomListBulkAcceptOrderDialog(context).apply {
                init()
                setOnDismiss {
                    val loadOrderListImmediately = shouldReloadOrderListImmediately()
                    onToggleMultiSelectClicked()
                    loadFilters(loadOrders = !loadOrderListImmediately)
                    if (loadOrderListImmediately) {
                        loadOrderList()
                    } else {
                        getSwipeRefreshLayout(view)?.isRefreshing = true
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
        activity?.window?.decorView?.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                com.tokopedia.unifyprinciples.R.color.Unify_Background
            )
        )
        showPlusOrderListMenuShimmer()
        showWaitingPaymentOrderListMenuShimmer()
        setupRecyclerView()
        setupHeader()
        setupSearchBar()
        setupListeners()
        setupMasks()
        coachMarkManager = SomListCoachMarkManager(somListBinding, userSession.userId, isEnabledCoachmark)
    }

    private fun setupMasks() {
        somListBinding?.run {
            somListUpperMask.setOnTouchListener(maskTouchListener)
            somListLowerMask.setOnTouchListener(maskTouchListener)
            somListLeftMask.setOnTouchListener(maskTouchListener)
            somListRightMask.setOnTouchListener(maskTouchListener)
        }
    }

    private fun setupSearchBar() {
        val searchParam = arguments?.getString(QUERY_PARAM_SEARCH).orEmpty()
        somListHeaderBinding?.searchBarSomList?.searchBarTextField?.setText(searchParam)
    }

    private fun observeLoadingStatus() {
        viewModel.isLoadingOrder.observe(viewLifecycleOwner) { isLoading ->
            if (!isLoading) hideLoading()
        }
    }

    private fun observeTopAdsCategory() {
        viewModel.topAdsCategoryResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    if (adapter.data.filterIsInstance<SomListEmptyStateUiModel>().isNotEmpty()) {
                        showEmptyState()
                        somListBinding?.rvSomList?.show()
                    }
                }
                is Fail -> {
                    showToasterError(view)
                    SomErrorHandler.logExceptionToServer(
                        errorTag = SomErrorHandler.SOM_TAG,
                        throwable = result.throwable,
                        errorType =
                        SomErrorHandler.SomMessage.GET_TOP_ADS_CATEGORY_ERROR,
                        deviceId = userSession.deviceId.orEmpty()
                    )
                }
            }
        }
    }

    private fun observeTickers() {
        viewModel.tickerResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> renderTickers(result.data)
                is Fail -> {
                    somListBinding?.tickerSomList?.gone()
                    showToasterError(view)
                    SomErrorHandler.logExceptionToServer(
                        errorTag = SomErrorHandler.SOM_TAG,
                        throwable = result.throwable,
                        errorType =
                        SomErrorHandler.SomMessage.GET_TICKERS_ERROR,
                        deviceId = userSession.deviceId.orEmpty()
                    )
                }
            }
        }
    }

    private fun observeFilters() {
        viewModel.filterResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    onSuccessGetFilter(result)
                }
                is Fail -> {
                    showGlobalError(result.throwable)
                    SomErrorHandler.logExceptionToServer(
                        errorTag = SomErrorHandler.SOM_TAG,
                        throwable = result.throwable,
                        errorType =
                        SomErrorHandler.SomMessage.FILTER_DATA_ON_ORDER_LIST_ERROR,
                        deviceId = userSession.deviceId.orEmpty()
                    )
                }
                else -> showGlobalError(Throwable())
            }
            somListBinding?.shimmerViews?.gone()
        }
    }

    private fun observeSomListHeaderIconsInfo() {
        viewModel.somListHeaderIconsInfoResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Fail -> {
                    showToasterError(view)
                    SomErrorHandler.logExceptionToServer(
                        errorTag = SomErrorHandler.SOM_TAG,
                        throwable = result.throwable,
                        errorType =
                        SomErrorHandler.SomMessage.GET_WAITING_PAYMENT_COUNTER_ERROR,
                        deviceId = userSession.deviceId.orEmpty()
                    )
                }
                else -> {
                    // no op
                }
            }
            updateHeaderMenu()
        }
    }

    private fun observeOrderList() {
        viewModel.orderListWrapperResult.observe(viewLifecycleOwner) { result ->
            somListLoadTimeMonitoring?.startRenderPerformanceMonitoring()
            somListBinding?.rvSomList?.addOneTimeGlobalLayoutListener {
                stopLoadTimeMonitoring()
                animateOrderTicker(true)
            }
            when (result) {
                is Success -> renderOrderList(result.data)
                is Fail -> {
                    showGlobalError(result.throwable)
                    SomErrorHandler.logExceptionToServer(
                        errorTag = SomErrorHandler.SOM_TAG,
                        throwable = result.throwable,
                        errorType =
                        SomErrorHandler.SomMessage.GET_ORDER_LIST_ERROR,
                        deviceId = userSession.deviceId.orEmpty()
                    )
                }
            }
        }
    }

    private fun observeRefreshOrder() {
        viewModel.refreshOrderResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> onRefreshOrderSuccess(result.data)
                is Fail -> onRefreshOrderFailed()
            }
        }
    }

    private fun observeAcceptOrder() {
        viewModel.acceptOrderResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> onAcceptOrderSuccess(result.data.acceptOrder, false)
                is Fail -> {
                    showToasterError(
                        view,
                        context?.resources?.getString(R.string.som_list_failed_accept_order).orEmpty(),
                        canRetry = false
                    )
                    SomErrorHandler.logExceptionToServer(
                        errorTag = SomErrorHandler.SOM_TAG,
                        throwable = result.throwable,
                        errorType =
                        SomErrorHandler.SomMessage.ACCEPT_ORDER_ERROR,
                        deviceId = userSession.deviceId.orEmpty()
                    )
                }
            }
        }
    }

    private fun observeRejectOrder() {
        viewModel.rejectOrderResult.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    val rejectOrderResponse = it.data.rejectOrder
                    if (rejectOrderResponse.success == 1) {
                        handleRejectOrderResult(rejectOrderResponse, false)
                    } else {
                        showToasterError(
                            view,
                            rejectOrderResponse.message.first(),
                            canRetry = false
                        )
                    }
                }
                is Fail -> {
                    it.throwable.showErrorToaster()
                    SomErrorHandler.logExceptionToServer(
                        errorTag = SomErrorHandler.SOM_TAG,
                        throwable = it.throwable,
                        errorType =
                        SomErrorHandler.SomMessage.REJECT_ORDER_ERROR,
                        deviceId = userSession.deviceId.orEmpty()
                    )
                }
            }
        }
    }

    private fun observeEditAwb() {
        viewModel.editRefNumResult.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    val successEditAwbResponse = it.data
                    if (successEditAwbResponse.mpLogisticEditRefNum.listMessage.isNotEmpty()) {
                        showCommonToaster(
                            view,
                            successEditAwbResponse.mpLogisticEditRefNum.listMessage.first()
                        )
                        onActionCompleted(false, selectedOrderId)
                    } else {
                        showToasterError(view, context?.resources?.getString(R.string.global_error).orEmpty(), canRetry = false)
                    }
                }
                is Fail -> {
                    val message = context?.run {
                        SomErrorHandler.getErrorMessage(it.throwable, this)
                    }.orEmpty()
                    if (message.isNotEmpty()) {
                        showToasterError(
                            view,
                            message,
                            context?.resources?.getString(R.string.som_list_button_ok).orEmpty(),
                            canRetry = false
                        )
                    } else {
                        it.throwable.showErrorToaster()
                    }
                    SomErrorHandler.logExceptionToServer(
                        errorTag = SomErrorHandler.SOM_TAG,
                        throwable = it.throwable,
                        errorType =
                        SomErrorHandler.SomMessage.CHANGE_AWB_ERROR,
                        deviceId = userSession.deviceId.orEmpty()
                    )
                }
            }
        }
    }

    private fun observeRejectCancelRequest() {
        viewModel.rejectCancelOrderResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    onActionCompleted(false, selectedOrderId)
                    showCommonToaster(view, result.data.rejectCancelRequest.message)
                }

                is Fail -> {
                    SomErrorHandler.logExceptionToCrashlytics(
                        result.throwable,
                        SomConsts.ERROR_REJECT_CANCEL_ORDER
                    )
                    SomErrorHandler.logExceptionToServer(
                        errorTag = SomErrorHandler.SOM_TAG,
                        throwable = result.throwable,
                        errorType =
                        SomErrorHandler.SomMessage.REJECT_CANCEL_REQUEST_ERROR,
                        deviceId = userSession.deviceId.orEmpty()
                    )
                    result.throwable.showErrorToaster()
                }
            }
        }
    }

    private fun observeBulkAcceptOrder() {
        viewModel.bulkAcceptOrderResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    if (somListBulkProcessOrderBottomSheet?.isShowing() == true) {
                        somListBulkProcessOrderBottomSheet?.dismiss()
                    }
                    showBulkAcceptOrderDialog(result.data.data.totalOrder)
                }
                is Fail -> {
                    if (somListBulkProcessOrderBottomSheet?.isShowing() == true) {
                        somListBulkProcessOrderBottomSheet?.onBulkAcceptOrderFailed()
                        showCommonToaster(view, "Terjadi kesalahan.", Toaster.TYPE_ERROR)
                    } else {
                        result.throwable.showErrorToaster()
                    }
                    SomErrorHandler.logExceptionToServer(
                        errorTag = SomErrorHandler.SOM_TAG,
                        throwable = result.throwable,
                        errorType =
                        SomErrorHandler.SomMessage.BULK_ACCEPT_ORDER_ERROR,
                        deviceId = userSession.deviceId.orEmpty()
                    )
                }
            }
        }
    }

    private fun observeBulkAcceptOrderStatus() {
        /*
            let n be the number of order the user select
            let s be the number of accepted order
            let f be the number of failed to accept order
            there is 4 possibility in this process:
            1. s == n which mean that all selected order is accepted (show success accept all order)
            2. f == n which mean that all selected order cannot be accepted (show failed accept all order)
            3. s < n && f == 0 which mean that some order is accepted but some orders have unknown state
               (maybe because the server is busy) (show partial success and can retry recheck remaining order status)
            4. s < n && f > 0 && s + f < n which mean that some order is accepted and some order cannot
               be accepted and there's some orders that have unknown state (maybe because the server is busy)
               (show partial success and error, can retry to recheck remaining order status)
            5. s < n && f > 0 && s + f == n which mean that some order is accepted and some order
               cannot be accepted (show partial success and error, cannot retry recheck remaining order status)
            6. s == 0 && f > 0 && s + f < n which mean that some order cannot be accepted and there's
               some order that have unknown state (maybe because the server is busy)
               (show partial error, can retry to recheck remaining order status)
            7. s == 0 && f == 0 which mean that all order still have unknown state (maybe because the server is busy)
               (show in progress state, can retry to recheck remaining order status)
         */
        viewModel.bulkAcceptOrderStatusResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    val orderCount = result.data.data.totalOrder
                    val successCount = result.data.data.success
                    val failedCount = result.data.data.fail
                    val processed = successCount + failedCount
                    val unprocessed = orderCount - processed
                    when {
                        successCount == orderCount -> { // case 1
                            newShowSuccessAcceptAllOrderDialog(successCount)
                        }
                        failedCount == orderCount -> {
                            newShowFailedAcceptAllOrderDialog(failedCount, orderCount)
                        }
                        successCount > Int.ZERO && failedCount == Int.ZERO && processed < orderCount -> {
                            newShowPartialSuccessAcceptOrderDialog(successCount, unprocessed)
                        }
                        successCount > Int.ZERO && failedCount > Int.ZERO && processed < orderCount -> {
                            newShowPartialMixedAcceptOrderDialog(successCount, failedCount, unprocessed, orderCount)
                        }
                        successCount > Int.ZERO && failedCount > Int.ZERO && processed == orderCount -> {
                            newShowSuccessMixedAcceptOrderDialog(successCount, failedCount, orderCount)
                        }
                        successCount == Int.ZERO && failedCount > Int.ZERO && processed < orderCount -> {
                            newShowPartialFailedAcceptOrderDialog(failedCount, unprocessed, orderCount)
                        }
                        successCount == Int.ZERO && failedCount == Int.ZERO -> {
                            newShowUnprocessedAcceptOrderDialog(orderCount)
                        }
                    }
                    SomAnalytics.eventBulkAcceptOrder(
                        getSelectedOrderStatusCodes().joinToString(","),
                        getSelectedOrderStatusNames().joinToString(","),
                        successCount,
                        userSession.userId,
                        userSession.shopId
                    )
                }
                is Fail -> {
                    newShowFailedAcceptAllOrderDialog(
                        getSelectedOrderIds().size,
                        getSelectedOrderIds().size
                    )
                    SomErrorHandler.logExceptionToServer(
                        errorTag = SomErrorHandler.SOM_TAG,
                        throwable = result.throwable,
                        errorType =
                        SomErrorHandler.SomMessage.GET_BULK_ACCEPT_ORDER_STATUS_ERROR,
                        deviceId = userSession.deviceId.orEmpty()
                    )
                }
            }
        }
    }

    private fun showOnProgressAcceptAllOrderDialog(orderCount: Int) {
        bulkAcceptOrderDialog?.run {
            hidePrimaryButton()
            hideSecondaryButton()
            setTitle(context?.resources?.getString(R.string.som_list_bulk_accept_dialog_title_on_progress, orderCount).orEmpty())
            setDescription(context?.resources?.getString(R.string.som_list_bulk_accept_dialog_description_on_progress).orEmpty())
            showOnProgress()
        }
    }

    private fun newShowSuccessAcceptAllOrderDialog(successCount: Int) {
        bulkAcceptOrderDialog?.run {
            setTitle(context?.resources?.getString(R.string.som_list_bulk_accept_dialog_title_success, successCount).orEmpty())
            setDescription(context?.resources?.getString(R.string.som_list_bulk_accept_dialog_description_success_accept_all_orders).orEmpty())
            showSuccess()
            setPrimaryButton(context?.resources?.getString(R.string.som_list_bulk_accept_dialog_primary_button_success_accept_all_orders).orEmpty()) {
                dismissAndRunAction()
            }
            hideSecondaryButton()
        }
    }

    private fun newShowFailedAcceptAllOrderDialog(failedCount: Int, orderCount: Int) {
        bulkAcceptOrderDialog?.run {
            setTitle(context?.resources?.getString(R.string.som_list_bulk_accept_dialog_title_failed, failedCount).orEmpty())
            setDescription(context?.resources?.getString(R.string.som_list_bulk_accept_dialog_description_failed).orEmpty())
            showFailed()
            setPrimaryButton(context?.resources?.getString(R.string.som_list_bulk_accept_dialog_primary_button_failed).orEmpty()) {
                showOnProgressAcceptAllOrderDialog(orderCount)
                viewModel.bulkAcceptOrder(getSelectedOrderIds())
            }
            setSecondaryButton(context?.resources?.getString(R.string.som_list_bulk_accept_dialog_secondary_button_failed).orEmpty()) { dismiss() }
        }
    }

    private fun newShowPartialSuccessAcceptOrderDialog(successCount: Int, unprocessedCount: Int) {
        bulkAcceptOrderDialog?.run {
            setTitle(context?.resources?.getString(R.string.som_list_bulk_accept_dialog_title_partial_success, successCount).orEmpty())
            setDescription(context?.resources?.getString(R.string.som_list_bulk_accept_dialog_description_partial_success, unprocessedCount).orEmpty())
            showSuccess()
            setPrimaryButton(context?.resources?.getString(R.string.som_list_bulk_accept_dialog_primary_button_partial_success).orEmpty()) {
                showOnProgressAcceptAllOrderDialog(unprocessedCount)
                viewModel.retryGetBulkAcceptOrderStatus()
            }
            setSecondaryButton(context?.resources?.getString(R.string.som_list_bulk_accept_dialog_secondary_button_partial_success).orEmpty()) { dismiss() }
        }
    }

    private fun newShowPartialMixedAcceptOrderDialog(
        successCount: Int,
        failedCount: Int,
        unprocessed: Int,
        orderCount: Int
    ) {
        bulkAcceptOrderDialog?.run {
            setTitle(context?.resources?.getString(R.string.som_list_bulk_accept_dialog_title_partial_mixed, successCount).orEmpty())
            setDescription(context?.resources?.getString(R.string.som_list_bulk_accept_dialog_description_partial_mixed, failedCount, unprocessed).orEmpty())
            showSuccess()
            setPrimaryButton(context?.resources?.getString(R.string.som_list_bulk_accept_dialog_primary_button_partial_mixed).orEmpty()) {
                showOnProgressAcceptAllOrderDialog(orderCount)
                viewModel.bulkAcceptOrder(getSelectedOrderIds())
            }
            setSecondaryButton(context?.resources?.getString(R.string.som_list_bulk_accept_dialog_secondary_button_partial_mixed).orEmpty()) { dismiss() }
        }
    }

    private fun newShowSuccessMixedAcceptOrderDialog(
        successCount: Int,
        failedCount: Int,
        orderCount: Int
    ) {
        bulkAcceptOrderDialog?.run {
            setTitle(context?.resources?.getString(R.string.som_list_bulk_accept_dialog_title_success_mixed, successCount).orEmpty())
            setDescription(context?.resources?.getString(R.string.som_list_bulk_accept_dialog_description_success_mixed, failedCount).orEmpty())
            showSuccess()
            setPrimaryButton(context?.resources?.getString(R.string.som_list_bulk_accept_dialog_primary_button_success_mixed).orEmpty()) {
                showOnProgressAcceptAllOrderDialog(orderCount)
                viewModel.bulkAcceptOrder(getSelectedOrderIds())
            }
            setSecondaryButton(context?.resources?.getString(R.string.som_list_bulk_accept_dialog_secondary_button_success_mixed).orEmpty()) { dismiss() }
        }
    }

    private fun newShowPartialFailedAcceptOrderDialog(failedCount: Int, unprocessed: Int, orderCount: Int) {
        bulkAcceptOrderDialog?.run {
            setTitle(context?.resources?.getString(R.string.som_list_bulk_accept_dialog_title_partial_failed, failedCount).orEmpty())
            setDescription(context?.resources?.getString(R.string.som_list_bulk_accept_dialog_description_partial_failed, failedCount, unprocessed).orEmpty())
            showFailed()
            setPrimaryButton(context?.resources?.getString(R.string.som_list_bulk_accept_dialog_primary_button_partial_failed).orEmpty()) {
                showOnProgressAcceptAllOrderDialog(orderCount)
                viewModel.retryGetBulkAcceptOrderStatus()
            }
            setSecondaryButton(context?.resources?.getString(R.string.som_list_bulk_accept_dialog_secondary_button_partial_failed).orEmpty()) { dismiss() }
        }
    }

    private fun newShowUnprocessedAcceptOrderDialog(orderCount: Int) {
        bulkAcceptOrderDialog?.run {
            setTitle(context?.resources?.getString(R.string.som_list_bulk_accept_dialog_title_unprocessed).orEmpty())
            setDescription(context?.resources?.getString(R.string.som_list_bulk_accept_dialog_description_unprocessed, orderCount).orEmpty())
            showFailed()
            setPrimaryButton(context?.resources?.getString(R.string.som_list_bulk_accept_dialog_primary_button_unprocessed).orEmpty()) {
                showOnProgressAcceptAllOrderDialog(orderCount)
                viewModel.retryGetBulkAcceptOrderStatus()
            }
            setSecondaryButton(context?.resources?.getString(R.string.som_list_bulk_accept_dialog_secondary_button_unprocessed).orEmpty()) { dismiss() }
        }
    }

    private fun observeBulkRequestPickupFinalResult() {
        observe(viewModel.bulkRequestPickupFinalResult) {
            when (it) {
                is AllSuccess -> {
                    showAllSuccessBulkRequestPickupDialog(it.totalSuccess)
                }
                is PartialSuccess -> {
                    showPartialSuccessRequestPickup(it.totalSuccess, it.orderIdListFail)
                }
                is PartialSuccessNotEligibleFail -> {
                    showPartialSuccessNotEligibleFailRequestPickup(
                        it.totalSuccess,
                        it.totalNotEligible,
                        it.orderIdListFail
                    )
                }
                is NotEligibleAndFail -> {
                    showNotEligibleAndFailRequestPickup(it.totalNotEligible, it.orderIdListFail)
                }
                is FailRetry -> {
                    showErrorBulkRequestPickupStatus()
                }
                is AllValidationFail -> {
                    showErrorBulkRequestPickupStatus()
                }
                is AllFailEligible -> {
                    showAllFailEligibleBulkRequestPickup(it.orderIdListFail)
                }
                is AllNotEligible -> {
                    showErrorBulkRequestPickupStatus()
                }
                is ServerFail -> {
                    bulkRequestPickupDialog?.dismiss()
                    showGlobalError(it.throwable)
                }
                is PartialSuccessNotEligible -> {
                    showPartialSuccessNotEligibleRequestPickup(it.totalSuccess, it.totalNotEligible)
                }
            }
        }
    }

    private fun observeBulkRequestPickup() {
        observe(viewModel.bulkRequestPickupResult) {
            when (it) {
                is Success -> {
                    if (somListBulkProcessOrderBottomSheet?.isShowing() == true) {
                        somListBulkProcessOrderBottomSheet?.dismiss()
                    }
                }
                is Fail -> {
                    showErrorBulkRequestPickup()
                    SomErrorHandler.logExceptionToServer(
                        errorTag = SomErrorHandler.SOM_TAG,
                        throwable = it.throwable,
                        errorType =
                        SomErrorHandler.SomMessage.BULK_REQUEST_PICKUP_ERROR,
                        deviceId = userSession.deviceId.orEmpty()
                    )
                }
            }
        }
    }

    private fun showPartialSuccessRequestPickup(totalSuccess: Int, orderIdsFail: List<String>) {
        bulkRequestPickupDialog?.run {
            setTitle(
                context?.resources?.getString(
                    R.string.som_list_bulk_request_pickup_title_success,
                    totalSuccess.toString()
                ).orEmpty()
            )
            setDescription(
                context?.resources?.getString(
                    R.string.som_list_bulk_request_pickup_partial_partial_fail_success,
                    orderIdsFail.size.toString()
                ).orEmpty()
            )
            setPrimaryButton(
                context?.resources?.getString(
                    R.string.som_list_bulk_request_pickup_dialog_primary_button_partial_success_retry_pickup_order,
                    orderIdsFail.size.toString()
                ).orEmpty()
            ) {
                refreshData()
                showProgressBulkRequestPickupDialog(orderIdsFail.size.toLong().orZero())
                viewModel.bulkRequestPickup(orderIdsFail)
            }
            setSecondaryButton(context?.resources?.getString(R.string.som_list_bulk_request_pickup_dialog_secondary_button_partial_success_can_retry).orEmpty()) {
                dismissAndRunAction()
            }
            showSuccess()
        }
    }

    private fun showPartialSuccessNotEligibleRequestPickup(
        totalSuccess: Int,
        totalNotEligible: Int
    ) {
        bulkRequestPickupDialog?.run {
            setTitle(
                context?.resources?.getString(
                    R.string.som_list_bulk_request_pickup_title_success,
                    totalSuccess.toString()
                ).orEmpty()
            )
            setDescription(
                context?.resources?.getString(
                    R.string.som_list_bulk_request_pickup_desc_partial_success_not_eligible,
                    totalNotEligible.toString()
                ).orEmpty()
            )
            setPrimaryButton(context?.resources?.getString(R.string.understand).orEmpty()) {
                dismissAndRunAction()
            }
            hideSecondaryButton()
            showSuccess()
        }
    }

    private fun showPartialSuccessNotEligibleFailRequestPickup(
        totalSuccess: Int,
        totalNotEligible: Int,
        orderIdsFail: List<String>
    ) {
        bulkRequestPickupDialog?.run {
            setTitle(
                context?.resources?.getString(
                    R.string.som_list_bulk_request_pickup_title_success,
                    totalSuccess.toString()
                ).orEmpty()
            )
            setDescription(
                context?.resources?.getString(
                    R.string.som_list_bulk_request_pickup_partial_all_fail_success,
                    totalNotEligible.toString(),
                    orderIdsFail.size.toString()
                ).orEmpty()
            )
            setPrimaryButton(
                context?.resources?.getString(
                    R.string.som_list_bulk_request_pickup_dialog_primary_button_partial_success_retry_pickup_order,
                    orderIdsFail.size.toString()
                ).orEmpty()
            ) {
                refreshData()
                showProgressBulkRequestPickupDialog(orderIdsFail.size.toLong().orZero())
                viewModel.bulkRequestPickup(orderIdsFail)
            }
            setSecondaryButton(context?.resources?.getString(R.string.som_list_bulk_request_pickup_dialog_secondary_button_partial_success_can_retry).orEmpty()) {
                dismissAndRunAction()
            }
            showSuccess()
        }
    }

    private fun showNotEligibleAndFailRequestPickup(
        totalNotEligible: Int,
        orderIdsFail: List<String>
    ) {
        bulkRequestPickupDialog?.run {
            setTitle(
                context?.resources?.getString(
                    R.string.som_list_bulk_request_pickup_title_not_eligible_fail,
                    totalNotEligible.toString()
                ).orEmpty()
            )
            setDescription(
                context?.resources?.getString(
                    R.string.som_list_bulk_request_pickup_partial_partial_fail_success,
                    orderIdsFail.size.toString()
                ).orEmpty()
            )
            setPrimaryButton(
                context?.resources?.getString(
                    R.string.som_list_bulk_request_pickup_dialog_primary_button_partial_success_retry_pickup_order,
                    orderIdsFail.size.toString()
                ).orEmpty()
            ) {
                showProgressBulkRequestPickupDialog(orderIdsFail.size.toLong().orZero())
                viewModel.bulkRequestPickup(orderIdsFail)
            }
            setSecondaryButton(context?.resources?.getString(R.string.som_list_bulk_request_pickup_dialog_secondary_button_partial_success_can_retry).orEmpty()) {
                dismissAndRunAction()
            }
            showFailed()
        }
    }

    private fun showErrorBulkRequestPickup() {
        bulkRequestPickupDialog?.run {
            setTitle(context?.resources?.getString(R.string.som_list_bulk_request_pickup_title_fail).orEmpty())
            setDescription(context?.resources?.getString(R.string.som_list_bulk_request_pickup_desc_fail_error).orEmpty())
            setPrimaryButton(context?.resources?.getString(R.string.som_list_bulk_request_pickup_dialog_primary_button_partial_success_can_retry).orEmpty()) {
                showProgressBulkRequestPickupDialog(getSelectedOrderIds().size.toLong().orZero())
                viewModel.bulkRequestPickup(getSelectedOrderIds())
            }
            setSecondaryButton(context?.resources?.getString(R.string.som_list_bulk_request_pickup_dialog_secondary_button_partial_success_can_retry).orEmpty()) {
                dismiss()
            }
            showFailed()
        }
    }

    private fun showErrorBulkRequestPickupStatus() {
        bulkRequestPickupDialog?.run {
            setTitle(context?.resources?.getString(R.string.som_list_bulk_request_pickup_title_fail).orEmpty())
            setDescription(context?.resources?.getString(R.string.som_list_bulk_request_pickup_desc_fail_all_validation).orEmpty())
            setPrimaryButton(context?.resources?.getString(R.string.understand).orEmpty()) {
                dismissAndRunAction()
            }
            hideSecondaryButton()
            showFailed()
        }
    }

    private fun showAllFailEligibleBulkRequestPickup(orderIdsFail: List<String>) {
        val totalFail = orderIdsFail.size.toLong().toString()
        bulkRequestPickupDialog?.run {
            setTitle(context?.resources?.getString(R.string.som_list_bulk_request_pickup_title_fail).orEmpty())
            setDescription(
                context?.resources?.getString(
                    R.string.som_list_bulk_request_pickup_partial_partial_fail_success,
                    totalFail
                ).orEmpty()
            )
            setPrimaryButton(
                context?.resources?.getString(
                    R.string.som_list_bulk_request_pickup_dialog_primary_button_partial_success_retry_pickup_order,
                    totalFail
                ).orEmpty()
            ) {
                showProgressBulkRequestPickupDialog(totalFail.toLongOrZero())
                viewModel.bulkRequestPickup(orderIdsFail)
            }
            setSecondaryButton(context?.resources?.getString(R.string.som_list_bulk_request_pickup_dialog_secondary_button_partial_success_can_retry).orEmpty()) {
                dismissAndRunAction()
            }
            showFailed()
        }
    }

    private fun showProgressBulkRequestPickupDialog(orderCount: Long) {
        initBulkRequestPickupDialog()
        bulkRequestPickupDialog?.run {
            hidePrimaryButton()
            hideSecondaryButton()
            setTitle(
                context?.resources?.getString(
                    R.string.som_list_bulk_request_pickup_loading,
                    orderCount.toString()
                ).orEmpty()
            )
            setDescription(context?.resources?.getString(R.string.som_list_bulk_request_pickup_desc_loading).orEmpty())
            showOnProgress()
            show()
        }
    }

    private fun showAllSuccessBulkRequestPickupDialog(orderCount: Int) {
        bulkRequestPickupDialog?.run {
            setPrimaryButton(context?.resources?.getString(R.string.understand).orEmpty()) {
                dismissAndRunAction()
            }
            hideSecondaryButton()
            setTitle(
                context?.resources?.getString(
                    R.string.som_list_bulk_request_pickup_title_success,
                    orderCount.toString()
                ).orEmpty()
            )
            setDescription(context?.resources?.getString(R.string.som_list_bulk_request_pickup_desc_success).orEmpty())
            showSuccess()
        }
    }

    private fun initBulkRequestPickupDialog() {
        if (bulkRequestPickupDialog?.getDialogUnify()?.isShowing == true) {
            bulkRequestPickupDialog?.dismiss()
        }
        context?.let { context ->
            bulkRequestPickupDialog = SomListBulkRequestPickupDialog(context).apply {
                setOnDismiss {
                    val loadOrderListImmediately = shouldReloadOrderListImmediately()
                    onToggleMultiSelectClicked()
                    loadFilters(loadOrders = !loadOrderListImmediately)
                    if (loadOrderListImmediately) {
                        loadOrderList()
                    } else {
                        getSwipeRefreshLayout(view)?.isRefreshing = true
                    }
                }
            }
        }
    }

    private fun observeValidateOrder() {
        viewModel.validateOrderResult.observe(viewLifecycleOwner) { result ->
            getSwipeRefreshLayout(view)?.isRefreshing = viewModel.isRefreshingOrder()
            when (result) {
                is Success -> onSuccessValidateOrder(result.data)
                is Fail -> {
                    onFailedValidateOrder()
                    SomErrorHandler.logExceptionToServer(
                        errorTag = SomErrorHandler.SOM_TAG,
                        throwable = result.throwable,
                        errorType =
                        SomErrorHandler.SomMessage.VALIDATE_ORDER_ERROR,
                        deviceId = userSession.deviceId.orEmpty()
                    )
                }
            }
        }
    }

    private fun observeIsAdminEligible() {
        viewModel.isOrderManageEligible.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    result.data.let { (isSomListEligible, isMultiAcceptEligible) ->
                        canDisplayOrderData = isSomListEligible
                        canMultiAcceptOrder = isMultiAcceptEligible
                        if (isSomListEligible) {
                            somListBinding?.somAdminPermissionView?.hide()
                            loadInitialData()
                        } else {
                            showAdminPermissionError()
                        }
                    }
                }
                is Fail -> {
                    showGlobalError(result.throwable)
                    SomErrorHandler.logExceptionToServer(
                        errorTag = SomErrorHandler.SOM_TAG,
                        throwable = result.throwable,
                        errorType =
                        SomErrorHandler.SomMessage.GET_ADMIN_PERMISSION_ERROR,
                        deviceId = userSession.deviceId.orEmpty()
                    )
                }
            }
        }
    }

    private fun observeRefreshOrderRequest() {
        viewModel.refreshOrderRequest.observe(viewLifecycleOwner) { request ->
            onReceiveRefreshOrderRequest(request.first, request.second)
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

    private fun showEmptyState(emptyStateUiModel: SomListEmptyStateUiModel? = null) {
        val newItems = arrayListOf(createSomListEmptyStateModel(emptyStateUiModel, viewModel.isTopAdsActive()))
        (adapter as? SomListOrderAdapter)?.updateOrders(newItems)
    }

    protected fun loadTopAdsCategory() {
        viewModel.getTopAdsCategory()
    }

    protected fun loadTickers() {
        viewModel.getTickers()
    }

    protected fun loadWaitingPaymentOrderCounter() {
        showPlusOrderListMenuShimmer()
        showWaitingPaymentOrderListMenuShimmer()
        viewModel.getHeaderIconsInfo()
    }

    protected fun loadFilters(showShimmer: Boolean = true, loadOrders: Boolean) {
        if (showShimmer) {
            somListBinding?.run {
                somListTabFilter.invisible()
                sortFilterSomList.invisible()
                shimmerViews.show()
            }
        }
        viewModel.getFilters(loadOrders)
    }

    private fun loadOrderList() {
        if (isLoadingInitialData) {
            viewModel.resetNextOrderId()
            if (adapter.dataSize > 0) {
                getSwipeRefreshLayout(view)?.isRefreshing = true
            } else {
                somListBinding?.run {
                    rvSomList.gone()
                    showOrderShimmer()
                }
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

    protected open fun onAcceptOrderSuccess(
        acceptOrderResponse: SomAcceptOrderResponse.Data.AcceptOrder,
        refreshOrder: Boolean
    ) {
        if (acceptOrderResponse.success == 1) {
            onActionCompleted(refreshOrder, selectedOrderId)
            showCommonToaster(view, acceptOrderResponse.listMessage.firstOrNull())
        } else {
            showToasterError(
                view,
                acceptOrderResponse.listMessage.firstOrNull().orEmpty(),
                canRetry = false
            )
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
        somListBinding?.run {
            tickerSomList.setDescriptionClickEvent(this@SomListFragment)

            globalErrorSomList.setActionClickListener {
                scrollViewErrorState.gone()
                loadInitialData()
            }

            btnBulkAction.setOnClickListener {
                when (btnBulkAction.text.toString()) {
                    context?.resources?.getString(R.string.som_list_bulk_accept_order_button).orEmpty() -> showBulkAcceptOrderBottomSheet()
                    context?.resources?.getString(R.string.som_list_bulk_confirm_shipping_order_button).orEmpty() -> showBulkProcessOrderBottomSheet()
                }
            }
        }
        somListHeaderBinding?.searchBarSomList?.searchBarTextField?.apply {
            addTextChangedListener(this@SomListFragment)
            setOnEditorActionListener { _, _, _ ->
                hideKeyboard()
                true
            }
        }
    }

    private fun handleSomDetailActivityResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            when {
                data.hasExtra(RESULT_CONFIRM_SHIPPING) -> handleConfirmShippingResult(
                    data.getStringExtra(
                        RESULT_CONFIRM_SHIPPING
                    )
                )
                data.hasExtra(SomConsts.RESULT_ACCEPT_ORDER) -> {
                    data.getParcelableExtra<SomAcceptOrderResponse.Data.AcceptOrder>(SomConsts.RESULT_ACCEPT_ORDER)
                        ?.let { resultAcceptOrder ->
                            onAcceptOrderSuccess(resultAcceptOrder, true)
                        }
                }
                data.hasExtra(SomConsts.RESULT_PROCESS_REQ_PICKUP) -> {
                    data.getStringExtra(SomConsts.RESULT_PROCESS_REQ_PICKUP)?.let { message ->
                        handleRequestPickUpResult(message)
                    }
                }
                data.hasExtra(SomConsts.RESULT_REJECT_ORDER) -> {
                    data.getParcelableExtra<SomRejectOrderResponse.Data.RejectOrder>(SomConsts.RESULT_REJECT_ORDER)
                        ?.let { resultRejectOrder ->
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

    private fun handleRejectOrderResult(
        resultRejectOrder: SomRejectOrderResponse.Data.RejectOrder,
        shouldRefreshOrder: Boolean
    ) {
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
            data.getStringExtra(SomConsts.RESULT_PROCESS_REQ_PICKUP)?.let { message ->
                handleRequestPickUpResult(message)
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

    private fun handleSomReturnToShipperActivityResult(resultCode: Int) {
        if (resultCode == Activity.RESULT_OK || resultCode == Activity.RESULT_FIRST_USER) {
            onActionCompleted(true, selectedOrderId)
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

    protected fun toggleBulkAction(enable: Boolean = !viewModel.isMultiSelectEnabled) {
        viewModel.isMultiSelectEnabled = enable
    }

    protected fun resetMultiSelectState() {
        (adapter as? SomListOrderAdapter)?.resetOrderSelectedStatus(
            SomListMultiSelectSectionUiModel(
                isEnabled = viewModel.isMultiSelectEnabled,
                totalOrder = somListOrderStatusFilterTab?.getSelectedFilterOrderCount().orZero(),
                totalSelected = Int.ZERO,
                totalSelectable = adapter.data.count { it is SomListOrderUiModel && !it.isOrderWithCancellationRequest() }
            )
        )
    }

    private fun toggleBulkActionButtonVisibility() {
        val isAnyCheckedOrder = adapter.data.filterIsInstance<SomListOrderUiModel>().find {
            it.isChecked
        } != null
        if (viewModel.isMultiSelectEnabled && isAnyCheckedOrder) {
            animateBulkAcceptOrderButtonEnter()
        } else {
            animateBulkAcceptOrderButtonLeave()
        }
    }

    private fun showPlusOrderListMenu(plusIconInfo: PlusIconInfo) {
        somListHeaderBinding?.run {
            loaderSomListMenuPlus.gone()
            icSomListMenuPlus.run {
                loadImage(plusIconInfo.logoUrl)
                setOnClickListener {
                    SomNavigator.openAppLink(context, plusIconInfo.eduUrl)
                }
                show()
            }
        }
    }

    private fun showPlusOrderListMenuShimmer() {
        somListHeaderBinding?.run {
            if (canDisplayOrderData) {
                loaderSomListMenuPlus.show()
                icSomListMenuPlus.gone()
            }
        }
    }

    private fun hidePlusOrderListMenu() {
        somListHeaderBinding?.run {
            loaderSomListMenuPlus.gone()
            icSomListMenuPlus.gone()
        }
    }

    private fun showWaitingPaymentOrderListMenuShimmer() {
        somListHeaderBinding?.run {
            if (canDisplayOrderData) {
                loaderSomListMenuWaitingPayment.show()
                icSomListMenuWaitingPayment.gone()
                icSomListMenuWaitingPaymentDot.gone()
            }
        }
    }

    private fun showWaitingPaymentOrderListMenu() {
        somListHeaderBinding?.run {
            loaderSomListMenuWaitingPayment.gone()
            icSomListMenuWaitingPayment.show()
            icSomListMenuWaitingPaymentDot.gone()
        }
    }

    private fun showDottedWaitingPaymentOrderListMenu() {
        somListHeaderBinding?.run {
            loaderSomListMenuWaitingPayment.gone()
            icSomListMenuWaitingPayment.show()
            icSomListMenuWaitingPaymentDot.show()
        }
    }

    private fun hideWaitingPaymentOrderListMenu() {
        somListHeaderBinding?.run {
            loaderSomListMenuWaitingPayment.gone()
            icSomListMenuWaitingPayment.gone()
            icSomListMenuWaitingPaymentDot.gone()
        }
    }

    private fun showGlobalError(throwable: Throwable) {
        coachMarkManager?.dismissCoachMark()
        somListBinding?.run {
            hideOrderShimmer()
            rvSomList.gone()
        }
        animateBulkAcceptOrderButtonLeave()
        getSwipeRefreshLayout(view)?.apply {
            isRefreshing = false
            isEnabled = false
        }
        val errorType = when (throwable) {
            is UnknownHostException, is SocketTimeoutException -> GlobalError.NO_CONNECTION
            else -> GlobalError.SERVER_ERROR
        }
        somListBinding?.run {
            globalErrorSomList.setType(errorType)
            sortFilterSomList.hide()
            scrollViewErrorState.show()
        }
        errorToaster?.dismiss()
        stopLoadTimeMonitoring()
    }

    private fun showAdminPermissionError() {
        coachMarkManager?.dismissCoachMark()
        somListBinding?.run {
            hideOrderShimmer()
            rvSomList.gone()
        }
        animateBulkAcceptOrderButtonLeave()
        somListHeaderBinding?.searchBarSomList?.gone()
        somListBinding?.shimmerViews?.gone()
        getSwipeRefreshLayout(view)?.apply {
            isRefreshing = false
            isEnabled = false
        }
        somListBinding?.run {
            sortFilterSomList.gone()
            scrollViewErrorState.gone()
        }
        errorToaster?.dismiss()
        hidePlusOrderListMenu()
        hideWaitingPaymentOrderListMenu()
        somListBinding?.somAdminPermissionView?.setUserNotAllowedToViewSom {
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
        somListBinding?.tickerSomList?.let { tickerView ->
            val visibility = tickerView.visibility
            tickerView.addPagerView(tickerPagerAdapter, activeTickers)
            tickerView.post {
                tickerView.visibility = visibility
                tickerIsReady = activeTickers.isNotEmpty()
                animateOrderTicker(true)
            }
        }
    }

    protected open fun renderOrderList(data: SomListOrderWrapperUiModel) {
        skipSearch = false
        somListBinding?.globalErrorSomList?.hide()
        if (somListBinding?.rvSomList?.visibility != View.VISIBLE) somListBinding?.rvSomList?.show()
        // show only if current order list is based on current search keyword
        if (isLoadingInitialData && data.somListOrders.isEmpty()) {
            showEmptyState(data.somListEmptyStateUiModel)
            toggleBulkActionButtonVisibility()
        } else if (data.somListOrders.firstOrNull()?.searchParam == somListHeaderBinding?.searchBarSomList?.searchBarTextField?.text?.toString().orEmpty()) {
            if (isLoadingInitialData) {
                val shouldShowMultiSelectSection = somListOrderStatusFilterTab
                    ?.shouldShowBulkAction()
                    ?.and(canMultiAcceptOrder)
                    .orFalse()
                    .and(data.somListOrders.isNotEmpty())
                val newItems = if (shouldShowMultiSelectSection) {
                    ArrayList<Visitable<SomListAdapterTypeFactory>>(data.somListOrders).apply {
                        add(
                            Int.ZERO,
                            SomListMultiSelectSectionUiModel(
                                isEnabled = viewModel.isMultiSelectEnabled,
                                totalOrder = somListOrderStatusFilterTab?.getSelectedFilterOrderCount().orZero(),
                                totalSelected = Int.ZERO,
                                totalSelectable = data.somListOrders.count { !it.isOrderWithCancellationRequest() }
                            )
                        )
                    }
                } else {
                    data.somListOrders
                }
                (adapter as? SomListOrderAdapter)?.updateOrders(newItems)
                toggleBulkActionButtonVisibility()
                if (shouldScrollToTop) {
                    shouldScrollToTop = false
                    somListBinding?.rvSomList?.addOneTimeGlobalLayoutListener {
                        somListBinding?.rvSomList?.smoothScrollToPosition(0)
                    }
                }
            } else {
                val newItems = ArrayList(adapter.data)
                val multiSelectSectionIndex = newItems.indexOfFirst { it is SomListMultiSelectSectionUiModel }
                val emptyStateIndex = newItems.size.dec()
                val updatedData = data.somListOrders.map { it.copy(multiSelectEnabled = viewModel.isMultiSelectEnabled) }
                newItems.addAll(updatedData)
                newItems.getOrNull(multiSelectSectionIndex)?.let {
                    if (it is SomListMultiSelectSectionUiModel) {
                        newItems[multiSelectSectionIndex] = SomListMultiSelectSectionUiModel(
                            isEnabled = viewModel.isMultiSelectEnabled,
                            totalOrder = somListOrderStatusFilterTab?.getSelectedFilterOrderCount().orZero(),
                            totalSelected = newItems.filterIsInstance<SomListOrderUiModel>().count { it.isChecked },
                            totalSelectable = newItems.count { it is SomListOrderUiModel && !it.isOrderWithCancellationRequest() }
                        )
                    }
                }
                newItems.getOrNull(emptyStateIndex)?.let {
                    if (it is SomListEmptyStateUiModel) {
                        newItems.removeAt(emptyStateIndex)
                    }
                }
                (adapter as? SomListOrderAdapter)?.updateOrders(newItems)
            }
            coachMarkManager?.showCoachMark()
        }
        updateScrollListenerState(viewModel.hasNextPage())
        isLoadingInitialData = false
    }

    protected open fun onRefreshOrderSuccess(result: OptionalOrderData) {
        val order = result.order
        if (order == null) {
            (adapter as? SomListOrderAdapter)?.removeOrder(result.orderId)
            checkLoadMore()
        } else {
            (adapter as? SomListOrderAdapter)?.updateOrder(order)
        }
        if (!(adapter as? SomListOrderAdapter)?.hasOrder().orFalse()) {
            (adapter as? SomListOrderAdapter)?.removeMultiSelectSection()
            toggleBulkAction(false)
            toggleBulkActionButtonVisibility()
            showEmptyState(result.somListEmptyStateUiModel)
        }
    }

    private fun onRefreshOrderFailed() {
        showToasterError(
            view,
            context?.resources?.getString(R.string.som_list_failed_refresh_order).orEmpty()
        )
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

    private fun createSomListEmptyStateModel(
        somListEmptyStateUiModel: SomListEmptyStateUiModel?,
        isTopAdsActive: Boolean
    ): Visitable<SomListAdapterTypeFactory> {
        return somListEmptyStateUiModel?.let {
            if (Utils.isEnableOperationalGuideline()) {
                it
            } else {
                getOldEmptyState(isTopAdsActive)
            }
        } ?: getOldEmptyState(isTopAdsActive)
    }

    private fun getOldEmptyState(
        isTopAdsActive: Boolean
    ): SomListEmptyStateUiModel {
        val isSellerApp = true
        val isNewOrderFilterSelected =
            somListOrderStatusFilterTab?.isNewOrderFilterSelected() == true
        val isNonStatusOrderFilterApplied = somListSortFilterTab?.isNonStatusOrderFilterApplied(
            somListOrderStatusFilterTab?.getSelectedFilterStatus()
        ) == true
        val isSearchQueryApplied =
            somListHeaderBinding?.searchBarSomList?.searchBarTextField?.text?.isNotBlank() == true

        return if (isSellerApp && !isTopAdsActive && isNewOrderFilterSelected &&
            !isNonStatusOrderFilterApplied && !isSearchQueryApplied
        ) {
            SomListEmptyStateUiModel(
                imageUrl = SomConsts.SOM_LIST_EMPTY_STATE_NO_FILTER_ILLUSTRATION,
                title = context?.resources?.getString(R.string.empty_peluang_title).orEmpty(),
                description = context?.resources?.getString(R.string.empty_peluang_desc_non_topads_no_filter)
                    .orEmpty(),
                buttonText = context?.resources?.getString(R.string.btn_cek_peluang_non_topads)
                    .orEmpty(),
                buttonAppLink = ApplinkConstInternalTopAds.TOPADS_CREATE_ADS,
                showButton = true
            )
        } else if (isNonStatusOrderFilterApplied || isSearchQueryApplied) {
            SomListEmptyStateUiModel(
                imageUrl = SomConsts.SOM_LIST_EMPTY_STATE_WITH_FILTER_ILLUSTRATION,
                title = context?.resources?.getString(R.string.som_list_empty_state_not_found_title)
                    .orEmpty()
            )
        } else {
            SomListEmptyStateUiModel(
                imageUrl = SomConsts.SOM_LIST_EMPTY_STATE_NO_FILTER_ILLUSTRATION,
                title = context?.resources?.getString(R.string.empty_peluang_title)
                    .orEmpty(),
                description = context?.resources?.getString(R.string.som_list_empty_state_description_no_topads_no_filter)
                    .orEmpty()
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
        if (viewModel.isMultiSelectEnabled) onToggleMultiSelectClicked()
        somListBinding?.scrollViewErrorState?.gone()
        isLoadingInitialData = true
        loadOrderList()
    }

    private fun showToasterError(
        view: View?,
        message: String = context?.resources?.getString(R.string.som_list_error_some_information_cannot_be_loaded)
            .orEmpty(),
        buttonMessage: String = context?.resources?.getString(R.string.btn_reload).orEmpty(),
        canRetry: Boolean = true
    ) {
        view?.let {
            if (canRetry) {
                errorToaster = Toaster.build(
                    it,
                    message,
                    Toaster.LENGTH_INDEFINITE,
                    Toaster.TYPE_ERROR,
                    buttonMessage
                ) {
                    refreshFailedRequests()
                }
            } else {
                errorToaster = Toaster.build(
                    it,
                    message,
                    Toaster.LENGTH_INDEFINITE,
                    Toaster.TYPE_ERROR,
                    context?.resources?.getString(R.string.som_list_button_ok).orEmpty()
                ) {
                    errorToaster?.dismiss()
                }
            }
        }
        if (errorToaster?.isShown == false) {
            errorToaster?.show()
        }
    }

    private fun showCommonToaster(
        view: View?,
        message: String?,
        toasterType: Int = Toaster.TYPE_NORMAL
    ) {
        message?.run {
            view?.let {
                commonToaster?.dismiss()
                commonToaster = Toaster.build(
                    it,
                    message,
                    Toaster.LENGTH_SHORT,
                    toasterType,
                    context?.resources?.getString(R.string.som_list_button_ok).orEmpty()
                )
                commonToaster?.show()
            }
        }
    }

    private fun refreshFailedRequests() {
        if (viewModel.somListHeaderIconsInfoResult.value is Fail) {
            showPlusOrderListMenuShimmer()
            showWaitingPaymentOrderListMenuShimmer()
            viewModel.getHeaderIconsInfo()
        }
        if (viewModel.tickerResult.value is Fail) {
            somListBinding?.tickerSomList?.gone()
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
                    somListBulkProcessOrderBottomSheet =
                        SomListBulkProcessOrderBottomSheet(it.context)
                }
                somListBulkProcessOrderBottomSheet?.let { bottomSheet ->
                    val items = arrayListOf<Visitable<SomListBulkProcessOrderTypeFactory>>().apply {
                        add(
                            SomListBulkProcessOrderDescriptionUiModel(
                                context?.resources?.getString(R.string.som_list_bottom_sheet_bulk_accept_order_description)
                                    .orEmpty(),
                                false
                            )
                        )
                        addAll(
                            getOrdersProducts(
                                adapter.data.filterIsInstance<SomListOrderUiModel>()
                                    .filter { it.isChecked }
                            )
                        )
                    }
                    bottomSheet.init(it)
                    bottomSheet.setTitle(
                        context?.resources?.getString(R.string.som_list_bulk_accept_order_button)
                            .orEmpty()
                    )
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
                    somListBulkProcessOrderBottomSheet =
                        SomListBulkProcessOrderBottomSheet(fragmentView.context)
                }
                somListBulkProcessOrderBottomSheet?.let { bottomSheet ->
                    val items = arrayListOf<Visitable<SomListBulkProcessOrderTypeFactory>>().apply {
                        add(
                            SomListBulkProcessOrderMenuItemUiModel(
                                KEY_PRINT_AWB,
                                context?.resources?.getString(R.string.som_list_bulk_print_button)
                                    .orEmpty(),
                                true
                            )
                        )
                        if (GlobalConfig.isSellerApp()) {
                            add(
                                SomListBulkProcessOrderMenuItemUiModel(
                                    KEY_REQUEST_PICKUP,
                                    context?.resources?.getString(R.string.som_list_bulk_request_pickup_button)
                                        .orEmpty(),
                                    isEligibleRequestPickup()
                                )
                            )
                        }
                    }
                    bottomSheet.init(fragmentView)
                    bottomSheet.setTitle(
                        context?.resources?.getString(R.string.som_list_bulk_confirm_shipping_order_button)
                            .orEmpty()
                    )
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

    private fun isEligibleRequestPickup(): Boolean {
        return adapter.data.filterIsInstance<SomListOrderUiModel>().filter { it.isChecked }.any {
            it.buttons.firstOrNull()?.key == KEY_REQUEST_PICKUP
        }
    }

    private fun getOrdersProducts(orders: List<SomListOrderUiModel>): List<SomListBulkProcessOrderProductUiModel> {
        val products = orders.map { it.orderProduct }.flatten().groupBy { it.productId }
        return products.filter { it.value.isNotEmpty() }.map {
            SomListBulkProcessOrderProductUiModel(
                productName = it.value.first().productName,
                picture = it.value.first().picture,
                amount = it.value.sumOf { prod -> prod.quantity }
            )
        }
    }

    private fun setInitialOrderListParams() {
        setDefaultSortByValue()
        val searchParam = arguments?.getString(QUERY_PARAM_SEARCH).orEmpty()
        viewModel.setSearchParam(searchParam)
    }

    private fun Throwable.showErrorToaster() {
        if (!somListBinding?.scrollViewErrorState?.isVisible.orFalse()) {
            if (this is UnknownHostException || this is SocketTimeoutException) {
                showNoInternetConnectionToaster()
            } else {
                showServerErrorToaster()
            }
        }
    }

    private fun showNoInternetConnectionToaster() {
        showToasterError(
            view,
            context?.resources?.getString(R.string.som_error_message_no_internet_connection)
                .orEmpty(),
            canRetry = false
        )
    }

    private fun showServerErrorToaster() {
        showToasterError(
            view,
            context?.resources?.getString(R.string.som_error_message_server_fault).orEmpty(),
            canRetry = false
        )
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

    private fun shouldReloadOrderListImmediately(): Boolean =
        viewModel.getTabActive().isBlank() || viewModel.getTabActive() == STATUS_ALL_ORDER

    override fun onClickShowOrderFilter(
        filterData: SomListGetOrderListParam,
        somFilterUiModelList: List<SomFilterUiModel>,
        idFilter: String
    ) {
        this.shouldScrollToTop = true
        isLoadingInitialData = true
        viewModel.updateSomFilterUiModelList(somFilterUiModelList)
        viewModel.updateGetOrderListParams(filterData)
        loadFilters(showShimmer = false, loadOrders = true)
        getSwipeRefreshLayout(view)?.isRefreshing = true
    }

    override fun onClickOverlayBottomSheet(filterCancelWrapper: SomFilterCancelWrapper) {
        viewModel.updateSomFilterUiModelList(filterCancelWrapper.somFilterUiModelList)
        val orderListParam = viewModel.getDataOrderListParams()
        orderListParam.statusList = filterCancelWrapper.orderStatusIdList
        viewModel.updateGetOrderListParams(orderListParam)
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
        somListBinding?.btnBulkAction?.text =
            when (somListOrderStatusFilterTab?.getSelectedFilterStatus()) {
                STATUS_NEW_ORDER -> context?.resources?.getString(R.string.som_list_bulk_accept_order_button)
                    .orEmpty()

                KEY_CONFIRM_SHIPPING -> context?.resources?.getString(R.string.som_list_bulk_confirm_shipping_order_button)
                    .orEmpty()

                else -> ""
            }
        somListBinding?.containerBtnBulkAction?.visible()
        bulkAcceptButtonEnterAnimation =
            somListBinding?.containerBtnBulkAction?.animateSlide(
                somListBinding?.containerBtnBulkAction?.translationY.orZero(),
                Float.ZERO
            )
    }

    private fun animateBulkAcceptOrderButtonLeave() {
        if (bulkAcceptButtonEnterAnimation?.isRunning == true) bulkAcceptButtonEnterAnimation?.cancel()
        bulkAcceptButtonLeaveAnimation = somListBinding?.containerBtnBulkAction?.animateSlide(
            somListBinding?.containerBtnBulkAction?.translationY.orZero(),
            somListBinding?.containerBtnBulkAction?.height?.toFloat().orZero()
        )
        bulkAcceptButtonLeaveAnimation?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                somListBinding?.containerBtnBulkAction?.gone()
                bulkAcceptButtonLeaveAnimation?.removeListener(this)
            }

            override fun onAnimationCancel(animation: Animator) {
                bulkAcceptButtonLeaveAnimation?.removeListener(this)
            }

            override fun onAnimationStart(animation: Animator) {}
        })
    }

    private fun animateOrderTicker(isEnter: Boolean) {
        Handler(Looper.getMainLooper()).postDelayed({
            val shouldAnimateTicker =
                (isEnter && tickerIsReady && (somListBinding?.tickerSomList?.visibility == View.INVISIBLE || somListBinding?.tickerSomList?.visibility == View.GONE)) || !isEnter
            if (adapter.data.isNotEmpty() && shouldAnimateTicker) {
                val enterValue: Float
                val exitValue: Float
                if (isEnter) {
                    enterValue = Float.ZERO
                    exitValue = 1f
                } else {
                    enterValue = 1f
                    exitValue = Float.ZERO
                }
                somListBinding?.tickerSomList?.run {
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
                            override fun onAnimationStart(p0: Animator) {}

                            override fun onAnimationEnd(p0: Animator) {
                                tickerIsReady = false
                                if (!isEnter) {
                                    this@run.invisible()
                                }
                            }

                            override fun onAnimationCancel(p0: Animator) {}

                            override fun onAnimationRepeat(p0: Animator) {}
                        })
                    }

                    animator.start()
                }
            }
        }, TICKER_ENTER_LEAVE_ANIMATION_DELAY)
    }

    private fun translateTickerConstrainedLayout(translation: Float) {
        somListBinding?.run {
            sortFilterSomList.translationY = translation
            sortFilterShimmer1.translationY = translation
            sortFilterShimmer2.translationY = translation
            sortFilterShimmer3.translationY = translation
            sortFilterShimmer4.translationY = translation
            sortFilterShimmer5.translationY = translation
            val params =
                (somListBinding?.swipeRefreshLayoutSomList?.layoutParams as? ViewGroup.MarginLayoutParams)
            params?.topMargin = translation.toInt()
            swipeRefreshLayoutSomList.layoutParams = params
            containerBtnBulkAction.translationY = translation
            scrollViewErrorState.translationY = translation
            somAdminPermissionView.translationY = translation
        }
    }

    private fun refreshOrders(shouldScrollToTop: Boolean, refreshFilter: Boolean) {
        this.shouldScrollToTop = shouldScrollToTop
        val loadOrderListImmediately = shouldReloadOrderListImmediately()
        if (refreshFilter) {
            loadFilters(showShimmer = false, loadOrders = !loadOrderListImmediately)
        }

        if (loadOrderListImmediately || !refreshFilter) {
            refreshOrderList()
        } else {
            getSwipeRefreshLayout(view)?.isRefreshing = true
        }
    }

    private fun getActivityPltPerformanceMonitoring() {
        somListLoadTimeMonitoring =
            (activity as? SomListLoadTimeMonitoringActivity)?.getSomListLoadTimeMonitoring()
    }

    private fun stopLoadTimeMonitoring() {
        somListLoadTimeMonitoring?.stopRenderPerformanceMonitoring()
        (activity as? SomListLoadTimeMonitoringActivity)?.loadTimeMonitoringListener?.onStopPltMonitoring()
    }

    private fun onFailedValidateOrder() {
        showToasterError(
            view,
            context?.resources?.getString(R.string.som_error_validate_order).orEmpty(),
            SomConsts.ACTION_OK,
            canRetry = false
        )
    }

    private fun onSuccessValidateOrder(valid: Boolean) {
        val pendingAction = pendingAction ?: return
        if (valid) {
            pendingAction.action.invoke()
        } else {
            context?.let { context ->
                val somOrderHasCancellationRequestDialog =
                    somOrderHasCancellationRequestDialog ?: SomOrderHasRequestCancellationDialog(
                        context
                    )
                this.somOrderHasCancellationRequestDialog = somOrderHasCancellationRequestDialog
                somOrderHasCancellationRequestDialog.apply {
                    setupActionButton(pendingAction.actionName, pendingAction.action)
                    setupGoToOrderDetailButton {
                        selectedOrderId = pendingAction.orderId
                        goToSomOrderDetail(this@SomListFragment, pendingAction.orderId)
                    }
                    show()
                }
            }
        }
    }

    private fun updateHeaderMenu() {
        if (canDisplayOrderData) {
            val headerIconsInfoResult = viewModel.somListHeaderIconsInfoResult.value
            if (headerIconsInfoResult is Success) {
                if (!isWaitingPaymentOrderPageOpened && headerIconsInfoResult.data.waitingPaymentCounter.amount > 0) {
                    showDottedWaitingPaymentOrderListMenu()
                } else {
                    showWaitingPaymentOrderListMenu()
                }
                headerIconsInfoResult.data.plusIconInfo?.let {
                    showPlusOrderListMenu(it)
                } ?: hidePlusOrderListMenu()
            } else {
                showPlusOrderListMenuShimmer()
                showWaitingPaymentOrderListMenuShimmer()
            }
        } else {
            hidePlusOrderListMenu()
            hideWaitingPaymentOrderListMenu()
        }
    }

    private fun setupRecyclerView() {
        somListBinding?.rvSomList?.layoutManager = somListLayoutManager
        somListBinding?.rvSomList?.itemAnimator = SomItemAnimator()
    }

    private fun setupHeader() {
        (activity as? AppCompatActivity)?.supportActionBar?.hide()
        somListHeaderBinding?.icSomListNavigationBack?.run {
            showWithCondition(showBackButton())
            setOnClickListener {
                activity?.onBackPressed()
            }
        }
        somListHeaderBinding?.icSomListMenuWaitingPayment?.setOnClickListener {
            goToWaitingPaymentOrderListPage()
            val headerIconsInfoResult = viewModel.somListHeaderIconsInfoResult.value
            if (headerIconsInfoResult is Success) {
                SomAnalytics.eventClickWaitingPaymentOrderCard(
                    viewModel.getTabActive(),
                    headerIconsInfoResult.data.waitingPaymentCounter.amount,
                    userSession.userId,
                    userSession.shopId
                )
            }
            isWaitingPaymentOrderPageOpened = true
            updateHeaderMenu()
        }
        somListHeaderBinding?.searchBarSomList?.setMargin(
            if (GlobalConfig.isSellerApp()) {
                SEARCH_BAR_MARGIN_START_SELLER_APP.toPx()
            } else {
                SEARCH_BAR_MARGIN_START_MAIN_APP.toPx()
            },
            Int.ZERO,
            SEARCH_BAR_MARGIN_END.toPx(),
            Int.ZERO
        )
        updateHeaderMenu()
    }

    private fun showBackButton(): Boolean = !GlobalConfig.isSellerApp()

    private fun showOrderShimmer() {
        somListBinding?.loaderSomList1?.root?.show()
        somListBinding?.loaderSomList2?.root?.show()
    }

    private fun hideOrderShimmer() {
        somListBinding?.loaderSomList1?.root?.gone()
        somListBinding?.loaderSomList2?.root?.gone()
    }

    private fun setTabActiveFromAppLink() {
        if (Utils.isEnableOperationalGuideline()) {
            setTabActiveFromAppLinkOg()
        } else {
            setTabActiveFromApplinkOld()
        }
    }

    private fun setTabActiveFromAppLinkOg() {
        val tabActive = arguments?.getString(TAB_ACTIVE).orEmpty()
        val tabActiveFilter =
            if (tabActive == SomConsts.STATUS_HISTORY || tabActive == STATUS_ALL_ORDER) String.EMPTY else tabActive
        viewModel.setTabActiveFromAppLink(tabActiveFilter)
        if (tabActiveFilter.isBlank()) {
            viewModel.setFirstPageOpened(true)
        }
    }

    private fun setTabActiveFromApplinkOld() {
        val tabActive = arguments?.getString(TAB_ACTIVE)
            ?: if (GlobalConfig.isSellerApp()) SomConsts.STATUS_NEW_ORDER else STATUS_ALL_ORDER
        viewModel.setTabActiveFromAppLink(tabActive)
    }

    protected fun dismissBottomSheets(): Boolean {
        var bottomSheetDismissed = false
        childFragmentManager.fragments.forEach {
            if (it is BottomSheetUnify && it !is SomFilterBottomSheet) {
                it.dismiss()
                bottomSheetDismissed = true
            }
        }
        bottomSheetDismissed =
            somListBulkProcessOrderBottomSheet?.dismiss() == true || bottomSheetDismissed
        bottomSheetDismissed =
            orderRequestCancelBottomSheet?.dismiss() == true || bottomSheetDismissed
        bottomSheetDismissed = somOrderEditAwbBottomSheet?.dismiss() == true || bottomSheetDismissed
        return bottomSheetDismissed
    }

    protected open fun loadAllInitialData() {
        if (viewModel.isMultiSelectEnabled) onToggleMultiSelectClicked()
        val loadOrderListImmediately = shouldReloadOrderListImmediately()
        isLoadingInitialData = true
        somListLoadTimeMonitoring?.startNetworkPerformanceMonitoring()
        toggleBulkAction(false)
        loadTopAdsCategory()
        loadTickers()
        loadWaitingPaymentOrderCounter()
        loadFilters(loadOrders = !loadOrderListImmediately)
        if (loadOrderListImmediately) {
            loadOrderList()
        }
    }

    protected open fun onReceiveRefreshOrderRequest(orderId: String, invoice: String) {
        viewModel.refreshSelectedOrder(orderId, invoice)
    }

    protected open fun onSuccessGetFilter(result: Success<SomListFilterUiModel>) {
        val somFilterUiModel = result.data

        somListOrderStatusFilterTab?.show(somFilterUiModel)
        somListSortFilterTab?.show(somFilterUiModel)
        somListSortFilterTab?.updateCounterSortFilter(
            somFilterUiModelList = viewModel.getSomFilterUi(),
            somListFilterUiModel = somFilterUiModel,
            somListGetOrderListParam = viewModel.getDataOrderListParams()
        )

        val highLightStatusKey = somFilterUiModel.highLightedStatusKey

        if (getShouldRefreshOrderAutoTabbing(highLightStatusKey)) {
            val statusIds = somFilterUiModel.statusList.find { it.key == highLightStatusKey }?.id.orEmpty()
            if (statusIds.isNotEmpty()) {
                viewModel.setStatusOrderFilter(statusIds, highLightStatusKey)
            }
            viewModel.setSortOrderBy(SomFilterUtil.getDefaultSortBy(highLightStatusKey))
            showCoachMarkAutoTabbing(highLightStatusKey)
            refreshOrders(shouldScrollToTop, false)
        } else if (result.data.refreshOrder) {
            refreshOrders(shouldScrollToTop, false)
        }

        if (viewModel.getIsFirstPageOpened()) {
            viewModel.setFirstPageOpened(false)
        }
    }

    private fun getShouldRefreshOrderAutoTabbing(highLightStatusKey: String): Boolean {
        // this case to handle when there is a highlightedStatusKey (all_order, new_order, confirm_shipping) from backend
        return viewModel.getIsFirstPageOpened() &&
            highLightStatusKey.isNotBlank() &&
            viewModel.getTabActiveFromAppLink().isBlank()
    }

    private fun showCoachMarkAutoTabbing(highLightStatusKey: String) {
        if (isEnabledCoachmark && highLightStatusKey in listOf(STATUS_NEW_ORDER, KEY_CONFIRM_SHIPPING)) {
            context?.let {
                if (!CoachMarkPreference.hasShown(it, SHARED_PREF_SOM_LIST_TAB_COACH_MARK)) {
                    val coachMarkMessage = getCoachMarkMessageAutoTabbing(it, highLightStatusKey)
                    if (coachMarkMessage.isNotBlank()) {
                        val tabPosition =
                            somListOrderStatusFilterTab?.somListFilterUiModel?.statusList?.indexOfFirst { status -> status.key == highLightStatusKey }
                        if (tabPosition == -Int.ONE || tabPosition == null) return
                        val tabLayoutViewPosition = somListBinding?.somListTabFilter?.tabLayout?.getTabAt(
                            tabPosition
                        )?.customView ?: return

                        val coachMarkItem = CoachMark2Item(
                            anchorView = tabLayoutViewPosition,
                            title = String.EMPTY,
                            description = coachMarkMessage,
                            position = CoachMark2.POSITION_BOTTOM
                        )

                        autoTabbingCoachMark?.run {
                            onFinishListener = {
                                CoachMarkPreference.setShown(
                                    it,
                                    SHARED_PREF_SOM_LIST_TAB_COACH_MARK,
                                    true
                                )
                            }
                            isDismissed = false
                            showCoachMark(
                                step = arrayListOf(coachMarkItem)
                            )
                        }
                    }
                }
            }
        }
    }

    private fun getCoachMarkMessageAutoTabbing(context: Context, highLightStatusKey: String): String {
        return when (highLightStatusKey) {
            STATUS_NEW_ORDER -> {
                context.getString(com.tokopedia.sellerorder.R.string.som_operational_guideline_new_order_tooltip_text)
                    .orEmpty()
            }
            KEY_CONFIRM_SHIPPING -> {
                context.getString(com.tokopedia.sellerorder.R.string.som_operational_guideline_confirm_shipping_tooltip_text)
                    .orEmpty()
            }
            else -> String.EMPTY
        }
    }
}
