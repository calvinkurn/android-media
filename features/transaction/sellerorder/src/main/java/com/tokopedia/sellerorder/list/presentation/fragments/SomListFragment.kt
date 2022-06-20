package com.tokopedia.sellerorder.list.presentation.fragments

import android.animation.Animator
import android.animation.LayoutTransition.CHANGING
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.os.Build
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
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.kotlin.extensions.view.visible
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
import com.tokopedia.sellerorder.common.util.Utils.hideKeyboard
import com.tokopedia.sellerorder.common.util.Utils.setUserNotAllowedToViewSom
import com.tokopedia.sellerorder.common.util.Utils.updateShopActive
import com.tokopedia.sellerorder.databinding.FragmentSomListBinding
import com.tokopedia.sellerorder.databinding.SomListHeaderBinding
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
import com.tokopedia.sellerorder.list.presentation.models.ServerFail
import com.tokopedia.sellerorder.list.presentation.models.SomListBulkProcessOrderDescriptionUiModel
import com.tokopedia.sellerorder.list.presentation.models.SomListBulkProcessOrderMenuItemUiModel
import com.tokopedia.sellerorder.list.presentation.models.SomListBulkProcessOrderProductUiModel
import com.tokopedia.sellerorder.list.presentation.models.SomListEmptyStateUiModel
import com.tokopedia.sellerorder.list.presentation.models.SomListFilterUiModel
import com.tokopedia.sellerorder.list.presentation.models.SomListOrderUiModel
import com.tokopedia.sellerorder.list.presentation.models.SomListTickerUiModel
import com.tokopedia.sellerorder.list.presentation.models.WaitingPaymentCounter
import com.tokopedia.sellerorder.list.presentation.viewmodels.SomListViewModel
import com.tokopedia.sellerorder.requestpickup.data.model.SomProcessReqPickup
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.activity.WaitingPaymentOrderActivity
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
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
import kotlin.math.abs

open class SomListFragment : BaseListFragment<Visitable<SomListAdapterTypeFactory>,
        SomListAdapterTypeFactory>(), SomListSortFilterTab.SomListSortFilterTabClickListener,
    TickerCallback, TickerPagerCallback, TextWatcher,
    SomListOrderViewHolder.SomListOrderItemListener, CoroutineScope,
    SomListBulkProcessOrderBottomSheet.SomListBulkProcessOrderBottomSheetListener,
    SomFilterBottomSheet.SomFilterFinishListener,
    SomListOrderEmptyViewHolder.SomListEmptyStateListener,
    SomListBulkPrintDialog.SomListBulkPrintDialogClickListener,
    SellerHomeFragmentListener, SomListOrderStatusFilterTab.Listener {

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
        private const val RECYCLER_VIEW_MIN_VERTICAL_SCROLL_THRESHOLD = 100
        private const val RV_TOP_POSITION = 0
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
                    putString(FILTER_ORDER_TYPE, bundle.getString(FILTER_ORDER_TYPE))
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

    private val recyclerViewScrollListener: RecyclerView.OnScrollListener by lazy {
        object : RecyclerView.OnScrollListener() {
            // to handle new order card coachmark (coachmark need to scroll along with the recyclerview and gone when new order card gone off screen)
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (coachMark?.currentIndex == newOrderCoachMarkItemPosition) {
                    if (coachMark?.isDismissed == true && abs(dy) <= RECYCLER_VIEW_MIN_VERTICAL_SCROLL_THRESHOLD) {
                        reshowNewOrderCoachMark(dy < Int.ZERO)
                    } else if (coachMark?.isDismissed == false) {
                        if (somListLayoutManager == null) {
                            return
                        }
                        val layoutManager = somListLayoutManager!!
                        val firstVisibleIndex = layoutManager.findFirstVisibleItemPosition()
                        val lastVisibleIndex = layoutManager.findLastVisibleItemPosition()
                        val currentNewOrderQuickActionButton =
                            layoutManager.findViewByPosition(currentNewOrderWithCoachMark)
                                ?.findViewById<View>(R.id.btnQuickAction)
                        if (coachMark?.isDismissed == false && (currentNewOrderWithCoachMark !in firstVisibleIndex..lastVisibleIndex ||
                                    (currentNewOrderQuickActionButton != null && getVisiblePercent(
                                        currentNewOrderQuickActionButton
                                    ) == -1))
                        ) {
                            coachMark?.setOnDismissListener {
                                coachMark?.setOnDismissListener { }
                                reshowNewOrderCoachMark(dy < Int.ZERO)
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
                        order.buttons.firstOrNull()?.key == SomConsts.KEY_ACCEPT_ORDER && !isOrderWithCancellationRequest(
                            order
                        )
                    ) {
                        layoutManager.findViewByPosition(it)
                            ?.findViewById<View>(R.id.btnQuickAction)?.takeIf {
                                it.isVisible
                            }?.let { quickActionButton ->
                                if (getVisiblePercent(quickActionButton) == Int.ZERO) {
                                    quickActionButton.post {
                                        createCoachMarkItems(quickActionButton).run {
                                            if (activity?.isFinishing != false) return@post
                                            if (size == coachMarkItemCount) {
                                                currentNewOrderWithCoachMark = it
                                                coachMark?.isDismissed = false
                                                shouldShowCoachMark = false
                                                coachMark?.showCoachMark(
                                                    this,
                                                    index = coachMarkIndexToShow
                                                )
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
    private var skipSearch: Boolean = false // when restored, onSearchTextChanged is called which trigger unwanted refresh order list
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
    private var filterDate = ""
    private var somFilterBottomSheet: SomFilterBottomSheet? = null
    private var pendingAction: SomPendingAction? = null
    private var tickerIsReady = false
    private var wasChangingTab = false

    protected var coachMarkIndexToShow: Int = Int.ZERO
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
    override fun getAdapterTypeFactory() = SomListAdapterTypeFactory(this, this)
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
            viewModel.setTabActiveFromAppLink(arguments?.getString(TAB_ACTIVE).orEmpty())
            viewModel.addOrderTypeFilter(arguments?.getString(FILTER_ORDER_TYPE, "0").toLongOrZero())
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
        observeWaitingPaymentCounter()
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
        } else {
            dismissCoachMark()
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
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(KEY_LAST_SELECTED_ORDER_ID, selectedOrderId)
        super.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        tryReshowCoachMark()
        updateShopActive()
    }

    override fun onPause() {
        dismissBottomSheets()
        dismissCoachMark()
        super.onPause()
        if (bulkAcceptButtonEnterAnimation?.isRunning == true) bulkAcceptButtonEnterAnimation?.end()
        if (bulkAcceptButtonLeaveAnimation?.isRunning == true) bulkAcceptButtonLeaveAnimation?.end()
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
        dismissCoachMark()
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
        status: SomListFilterUiModel.OrderType,
        shouldScrollToTop: Boolean,
        fromClickTab: Boolean
    ) {
        if (status.isChecked) {
            viewModel.addOrderTypeFilter(status.id)
        } else {
            viewModel.removeOrderTypeFilter(status.id)
        }
        if (fromClickTab) {
            wasChangingTab = fromClickTab
        }
        if (viewModel.isMultiSelectEnabled) {
            context.let { context ->
                if (context == null || !DeviceScreenInfo.isTablet(context)) {
                    somListLayoutManager?.findFirstVisibleItemPosition()?.let {
                        somListLayoutManager?.findViewByPosition(it)
                            ?.findViewById<View>(R.id.btnQuickAction)
                            ?.addOneTimeGlobalLayoutListener {
                                refreshOrdersOnTabClicked(shouldScrollToTop, fromClickTab)
                            }
                    }
                } else {
                    refreshOrdersOnTabClicked(shouldScrollToTop, fromClickTab)
                }
            }
            viewModel.isMultiSelectEnabled = false
            resetOrderSelectedStatus()
            toggleBulkActionButtonVisibility()
            toggleBulkActionCheckboxVisibility()
            somListBinding?.run {
                checkBoxBulkAction.isChecked = false
                checkBoxBulkAction.setIndeterminate(false)
                checkBoxBulkAction.skipAnimation()
            }
        } else {
            refreshOrdersOnTabClicked(shouldScrollToTop, fromClickTab)
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
            somFilterBottomSheet = SomFilterBottomSheet.createInstance(
                viewModel.getDataOrderListParams().statusList,
                filterDate,
                viewModel.getSelectedOrderTypeFilters(),
                viewModel.getSelectedSort(),
                cacheManager?.id.orEmpty()
            )
            somFilterBottomSheet?.setSomFilterFinishListener(this)
            somFilterBottomSheet?.isAdded?.let {
                if (!(it)) {
                    somFilterBottomSheet?.show(childFragmentManager)
                }
            }
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
        shouldScrollToTop: Boolean,
        fromClickTab: Boolean
    ) {
        if (status.key != viewModel.getTabActive() && fromClickTab) {
            wasChangingTab = true
        }
        somListBinding?.rvSomList?.itemAnimator =
            if (wasChangingTab && !fromClickTab) {
                defaultItemAnimator
            } else {
                fadeRightAnimator
            }
        if (status.key == viewModel.getTabActive()) {
            wasChangingTab = false
        }
        if (fromClickTab) {
            viewModel.setStatusOrderFilter(status.id)
            setDefaultSortByValue()
            SomAnalytics.eventClickStatusFilter(status.id.map { it.toString() }, status.status)
        } else {
            viewModel.setStatusOrderFilter(viewModel.getDataOrderListParams().statusList)
        }
        if (viewModel.isMultiSelectEnabled) {
            context.let { context ->
                if (context == null || !DeviceScreenInfo.isTablet(context)) {
                    somListLayoutManager?.findFirstVisibleItemPosition()?.let {
                        somListLayoutManager?.findViewByPosition(it)
                            ?.findViewById<View>(R.id.btnQuickAction)
                            ?.addOneTimeGlobalLayoutListener {
                                refreshOrdersOnTabClicked(shouldScrollToTop, fromClickTab)
                            }
                    }
                } else {
                    refreshOrdersOnTabClicked(shouldScrollToTop, fromClickTab)
                }
            }
            viewModel.isMultiSelectEnabled = false
            resetOrderSelectedStatus()
            toggleBulkActionButtonVisibility()
            toggleBulkActionCheckboxVisibility()
            somListBinding?.run {
                checkBoxBulkAction.isChecked = false
                checkBoxBulkAction.setIndeterminate(false)
                checkBoxBulkAction.skipAnimation()
            }
        } else {
            refreshOrdersOnTabClicked(shouldScrollToTop, fromClickTab)
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
        updateBulkActionCheckboxStatus()
        toggleBulkActionButtonVisibility()
        updateOrderCounter()
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

    override fun onAcceptOrderButtonClicked(
        actionName: String,
        orderId: String,
        skipValidateOrder: Boolean
    ) {
        somListBinding?.rvSomList?.itemAnimator = fadeRightAnimator
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
        somListBinding?.rvSomList?.itemAnimator = fadeRightAnimator
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
                somListBinding?.rvSomList?.itemAnimator = fadeRightAnimator
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

    override fun onFinishBindNewOrder(view: View, itemIndex: Int) {
        context?.let { context ->
            if (!CoachMarkPreference.hasShown(context, SHARED_PREF_NEW_SOM_LIST_COACH_MARK) &&
                Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP
            ) {
                somListBinding?.rvSomList?.addOnScrollListener(recyclerViewScrollListener)
                setCoachMarkStepListener()
                coachMark?.onFinishListener =
                    { somListBinding?.rvSomList?.removeOnScrollListener(recyclerViewScrollListener) }
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
                        rCode = "0",
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

    private fun setDefaultSortByValue() {
        if (viewModel.getTabActive() == KEY_CONFIRM_SHIPPING) {
            viewModel.setSortOrderBy(SomConsts.SORT_BY_PAYMENT_DATE_ASCENDING)
        } else {
            viewModel.setSortOrderBy(SomConsts.SORT_BY_PAYMENT_DATE_DESCENDING)
        }
    }

    private fun getOrderBy(orderId: String): String {
        return (adapter.data.firstOrNull {
            it is SomListOrderUiModel && it.orderId == orderId
        } as? SomListOrderUiModel)?.orderResi.orEmpty()
    }

    private fun showBulkAcceptOrderDialog(orderCount: Int) {
        if (bulkAcceptOrderDialog?.getDialogUnify()?.isShowing == true) {
            bulkAcceptOrderDialog?.dismiss()
        }
        context?.let { context ->
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
        showWaitingPaymentOrderListMenuShimmer()
        somListBinding?.rvSomList?.layoutManager = somListLayoutManager
        somListBinding?.bulkActionCheckBoxContainer?.layoutTransition?.enableTransitionType(CHANGING)
        setupToolbar()
        setupSearchBar()
        setupListeners()
        setupMasks()
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
        viewModel.filterResult.observe(
            viewLifecycleOwner,
            object : Observer<Result<SomListFilterUiModel>> {
                var realtimeDataChangeCount = Int.ZERO
                override fun onChanged(result: Result<SomListFilterUiModel>?) {
                    when (result) {
                        is Success -> {
                            realtimeDataChangeCount = onSuccessGetFilter(result, realtimeDataChangeCount)
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
            })
    }

    private fun observeWaitingPaymentCounter() {
        viewModel.waitingPaymentCounterResult.observe(viewLifecycleOwner) { result ->
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
            }
            updateToolbarMenu()
        }
    }

    private fun observeOrderList() {
        viewModel.orderListResult.observe(viewLifecycleOwner) { result ->
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

    private fun selectFilterTab(
        result: Success<SomListFilterUiModel>,
        realtimeDataChangeCount: Int
    ) {
        result.data.statusList.find { it.isChecked }?.let { activeFilter ->
            /*  refresh only on:
                1. 2nd..n-th realtime (cloud) data
                2. First realtime (cloud) data with any differences from the previous cached data (if first realtime data is coming after cached data)
                3. First data
             */
            if (shouldRefreshOrders(
                    result.data.refreshOrder,
                    realtimeDataChangeCount
                )
            ) {
                onClickOrderStatusFilterTab(activeFilter, shouldScrollToTop, false)
            }
        }
    }

    private fun shouldRefreshOrders(
        refreshOrder: Boolean,
        realtimeDataChangeCount: Int
    ): Boolean {
        return refreshOrder && (realtimeDataChangeCount >= 1 || (realtimeDataChangeCount == 0))
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
            somListBinding?.run {
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
        setupSearchBarLayoutChangesListener()
    }

    private fun setupSearchBarLayoutChangesListener() {
        (somListHeaderBinding?.searchBarSomList?.searchBarTextField?.parent as? View)?.viewTreeObserver?.addOnPreDrawListener {
            context?.run {
                val searchBarContainer = somListHeaderBinding?.searchBarSomList?.searchBarTextField?.parent as? View
                val horizontalPadding = resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2).toInt()
                val verticalPadding = resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3).toInt()
                if (searchBarContainer?.paddingBottom != verticalPadding || searchBarContainer.paddingTop != verticalPadding) {
                    somListBinding?.bulkActionCheckBoxContainer?.layoutTransition?.disableTransitionType(CHANGING)
                    searchBarContainer?.setPadding(
                        horizontalPadding,
                        verticalPadding,
                        horizontalPadding,
                        verticalPadding
                    )
                    return@addOnPreDrawListener false
                }
            }
            somListBinding?.bulkActionCheckBoxContainer?.layoutTransition?.disableTransitionType(CHANGING)
            true
        }
    }

    private fun toggleTvSomListBulkText() {
        context?.run {
            val textResId = if (viewModel.isMultiSelectEnabled) R.string.som_list_multi_select_cancel else R.string.som_list_multi_select
            somListBinding?.tvSomListBulk?.text = context?.resources?.getString(textResId).orEmpty()
        }
    }

    private fun updateOrderCounter() {
        somListBinding?.multiEditViews?.showWithCondition(
            (somListOrderStatusFilterTab?.shouldShowBulkAction()?.and(canMultiAcceptOrder)
                ?: false) && GlobalConfig.isSellerApp() && adapter.data.filterIsInstance<SomListOrderUiModel>()
                .isNotEmpty()
        )
        context?.run {
            val text = if (viewModel.isMultiSelectEnabled) {
                val checkedCount = adapter.data.filterIsInstance<SomListOrderUiModel>().count { it.isChecked }
                context?.resources?.getString(R.string.som_list_order_counter_multi_select_enabled, checkedCount).orEmpty()
            } else {
                context?.resources?.getString(
                    R.string.som_list_order_counter,
                    somListOrderStatusFilterTab?.getSelectedFilterOrderCount().orZero()
                ).orEmpty()
            }
            somListBinding?.tvSomListOrderCounter?.text = text
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
                    data.getParcelableExtra<SomProcessReqPickup.Data.MpLogisticRequestPickup>(
                        SomConsts.RESULT_PROCESS_REQ_PICKUP
                    )?.let { resultProcessReqPickup ->
                        handleRequestPickUpResult(resultProcessReqPickup.listMessage.firstOrNull())
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
            data.getParcelableExtra<SomProcessReqPickup.Data.MpLogisticRequestPickup>(SomConsts.RESULT_PROCESS_REQ_PICKUP)
                ?.let { resultProcessReqPickup ->
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
            adapter.notifyItemRangeChanged(
                0,
                size,
                Bundle().apply { putBoolean(SomListOrderViewHolder.TOGGLE_SELECTION, true) })
        }
    }

    private fun checkAllOrder() {
        adapter.data.onEach {
            if (it is SomListOrderUiModel && !isOrderWithCancellationRequest(it)) it.isChecked =
                true
        }
        adapter.notifyDataSetChanged()
    }

    private fun toggleBulkActionButtonVisibility() {
        val isAnyCheckedOrder =
            adapter.data.filterIsInstance<SomListOrderUiModel>().find { it.isChecked } != null
        if (viewModel.isMultiSelectEnabled && isAnyCheckedOrder) {
            animateBulkAcceptOrderButtonEnter()
        } else {
            animateBulkAcceptOrderButtonLeave()
        }
    }

    private fun toggleBulkActionCheckboxVisibility() {
        somListBinding?.checkBoxBulkAction?.showWithCondition(viewModel.isMultiSelectEnabled)
    }

    private fun updateBulkActionCheckboxStatus() {
        val groupedOrders = adapter.data
            .filter { it is SomListOrderUiModel && !isOrderWithCancellationRequest(it) }
            .groupBy { (it as SomListOrderUiModel).isChecked }
        val newIndeterminateStatus =
            groupedOrders[true]?.size.orZero() > 0 && groupedOrders[false]?.size.orZero() > 0
        val newCheckedStatus = groupedOrders[true]?.size.orZero() > 0
        if (newCheckedStatus != somListBinding?.checkBoxBulkAction?.isChecked) {
            somListBinding?.checkBoxBulkAction?.isChecked = newCheckedStatus
        }
        if (newIndeterminateStatus != somListBinding?.checkBoxBulkAction?.getIndeterminate()) {
            somListBinding?.checkBoxBulkAction?.setIndeterminate(newIndeterminateStatus)
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
        dismissCoachMark()
        somListBinding?.run {
            hideOrderShimmer()
            rvSomList.gone()
            multiEditViews.gone()
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
            sortFilterSomList.invisible()
            scrollViewErrorState.show()
        }
        errorToaster?.dismiss()
        stopLoadTimeMonitoring()
    }

    private fun showAdminPermissionError() {
        dismissCoachMark()
        somListBinding?.run {
            hideOrderShimmer()
            rvSomList.gone()
            multiEditViews.gone()
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

    protected open fun renderOrderList(data: List<SomListOrderUiModel>) {
        skipSearch = false
        if (somListBinding?.rvSomList?.visibility != View.VISIBLE) somListBinding?.rvSomList?.show()
        // show only if current order list is based on current search keyword
        if (isLoadingInitialData && data.isEmpty()) {
            showEmptyState()
            somListBinding?.multiEditViews?.gone()
            toggleBulkActionButtonVisibility()
        } else if (data.firstOrNull()?.searchParam == somListHeaderBinding?.searchBarSomList?.searchBarTextField?.text?.toString().orEmpty()) {
            if (isLoadingInitialData) {
                (adapter as SomListOrderAdapter).updateOrders(data)
                somListBinding?.multiEditViews?.showWithCondition(
                    (somListOrderStatusFilterTab?.shouldShowBulkAction()?.and(canMultiAcceptOrder)
                        ?: false) && GlobalConfig.isSellerApp()
                )
                toggleTvSomListBulkText()
                toggleBulkActionCheckboxVisibility()
                toggleBulkActionButtonVisibility()
                if (shouldScrollToTop) {
                    shouldScrollToTop = false
                    somListBinding?.rvSomList?.addOneTimeGlobalLayoutListener {
                        somListBinding?.rvSomList?.smoothScrollToPosition(0)
                    }
                }
                if (coachMark?.currentIndex == newOrderCoachMarkItemPosition || (coachMark?.currentIndex == bulkProcessCoachMarkItemPosition && !somListBinding?.multiEditViews?.isVisible.orFalse())) {
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
                somListBinding?.rvSomList?.post {
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
            somListBinding?.multiEditViews?.gone()
            toggleBulkActionButtonVisibility()
            viewModel.isMultiSelectEnabled = false
            showEmptyState()
        }
    }

    private fun onRefreshOrderFailed() {
        showToasterError(view, context?.resources?.getString(R.string.som_list_failed_refresh_order).orEmpty())
    }

    private fun getFirstNewOrder(orders: List<SomListOrderUiModel>): Int {
        return orders.indexOfFirst {
            it.orderStatusId == SomConsts.STATUS_CODE_ORDER_CREATED && it.buttons.isNotEmpty() && !isOrderWithCancellationRequest(
                it
            )
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
        return if (GlobalConfig.isSellerApp() && !isTopAdsActive && somListOrderStatusFilterTab?.isNewOrderFilterSelected() == true &&
            somListSortFilterTab?.isFilterApplied() != true && somListHeaderBinding?.searchBarSomList?.searchBarTextField?.text.isNullOrEmpty()
        ) {
            SomListEmptyStateUiModel(
                imageUrl = SomConsts.SOM_LIST_EMPTY_STATE_NO_FILTER_ILLUSTRATION,
                title = context?.resources?.getString(R.string.empty_peluang_title).orEmpty(),
                description = context?.resources?.getString(R.string.empty_peluang_desc_non_topads_no_filter).orEmpty(),
                buttonText = context?.resources?.getString(R.string.btn_cek_peluang_non_topads).orEmpty(),
                buttonAppLink = ApplinkConstInternalTopAds.TOPADS_CREATE_ADS,
                showButton = true
            )
        } else if (somListSortFilterTab?.isFilterApplied() == true || !somListHeaderBinding?.searchBarSomList?.searchBarTextField?.text.isNullOrEmpty()) {
            SomListEmptyStateUiModel(
                imageUrl = SomConsts.SOM_LIST_EMPTY_STATE_WITH_FILTER_ILLUSTRATION,
                title = context?.resources?.getString(R.string.som_list_empty_state_not_found_title).orEmpty()
            )
        } else {
            SomListEmptyStateUiModel(
                imageUrl = SomConsts.SOM_LIST_EMPTY_STATE_NO_FILTER_ILLUSTRATION,
                title = context?.resources?.getString(R.string.empty_peluang_title).orEmpty(),
                description = context?.resources?.getString(R.string.som_list_empty_state_description_no_topads_no_filter).orEmpty()
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
            somListBinding?.checkBoxBulkAction?.isChecked = false
            somListBinding?.checkBoxBulkAction?.setIndeterminate(false)
        }
        somListBinding?.scrollViewErrorState?.gone()
        isLoadingInitialData = true
        loadOrderList()
    }

    private fun showToasterError(
        view: View?,
        message: String = context?.resources?.getString(R.string.som_list_error_some_information_cannot_be_loaded).orEmpty(),
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
        if (viewModel.waitingPaymentCounterResult.value is Fail) {
            showWaitingPaymentOrderListMenuShimmer()
            viewModel.getWaitingPaymentCounter()
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
                                context?.resources?.getString(R.string.som_list_bottom_sheet_bulk_accept_order_description).orEmpty(),
                                false
                            )
                        )
                        addAll(
                            getOrdersProducts(
                                adapter.data.filterIsInstance<SomListOrderUiModel>()
                                    .filter { it.isChecked })
                        )
                    }
                    bottomSheet.init(it)
                    bottomSheet.setTitle(context?.resources?.getString(R.string.som_list_bulk_accept_order_button).orEmpty())
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
                                context?.resources?.getString(R.string.som_list_bulk_print_button).orEmpty(),
                                true
                            )
                        )
                        if (GlobalConfig.isSellerApp()) {
                            add(
                                SomListBulkProcessOrderMenuItemUiModel(
                                    KEY_REQUEST_PICKUP,
                                    context?.resources?.getString(R.string.som_list_bulk_request_pickup_button).orEmpty(),
                                    isEligibleRequestPickup()
                                )
                            )
                        }
                    }
                    bottomSheet.init(fragmentView)
                    bottomSheet.setTitle(context?.resources?.getString(R.string.som_list_bulk_confirm_shipping_order_button).orEmpty())
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
            context?.resources?.getString(R.string.som_error_message_no_internet_connection).orEmpty(),
            canRetry = false
        )
    }

    private fun showServerErrorToaster() {
        showToasterError(view, context?.resources?.getString(R.string.som_error_message_server_fault).orEmpty(), canRetry = false)
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
        filterData: SomListGetOrderListParam, somFilterUiModelList: List<SomFilterUiModel>,
        idFilter: String, filterDate: String
    ) {
        this.filterDate = filterDate
        this.shouldScrollToTop = true
        isLoadingInitialData = true
        viewModel.updateSomFilterUiModelList(somFilterUiModelList)
        somListSortFilterTab?.updateCounterSortFilter(filterDate, somFilterUiModelList)
        val selectedStatusFilterKey = somFilterUiModelList.find {
            it.nameFilter == SomConsts.FILTER_STATUS_ORDER
        }?.somFilterData?.find {
            it.isSelected
        }?.key

        if (viewModel.getTabActive() != selectedStatusFilterKey.orEmpty()) {
            wasChangingTab = true
        }

        viewModel.updateGetOrderListParams(filterData)
        loadFilters(showShimmer = false, loadOrders = true)
        if (shouldReloadOrderListImmediately()) {
            loadOrderList()
        } else {
            getSwipeRefreshLayout(view)?.isRefreshing = true
        }
    }

    override fun onClickOverlayBottomSheet(filterCancelWrapper: SomFilterCancelWrapper) {
        viewModel.updateSomFilterUiModelList(filterCancelWrapper.somFilterUiModelList)
        val orderListParam = viewModel.getDataOrderListParams()
        orderListParam.statusList = filterCancelWrapper.orderStatusIdList
        viewModel.updateGetOrderListParams(orderListParam)
        this.filterDate = filterCancelWrapper.filterDate
    }

    protected open fun setCoachMarkStepListener() {
        coachMark?.setStepListener(object : CoachMark2.OnStepListener {
            override fun onStep(currentIndex: Int, coachMarkItem: CoachMark2Item) {
                when (currentIndex) {
                    newOrderCoachMarkItemPosition -> {
                        shouldShowCoachMark = true
                        coachMark?.isDismissed = true
                        coachMarkIndexToShow = newOrderCoachMarkItemPosition
                        if (viewModel.getTabActive().isNotBlank() && viewModel.getTabActive() != STATUS_ALL_ORDER) {
                            viewModel.resetGetOrderListParam()
                            viewModel.clearSomFilterUiModelList()
                            skipSearch = true
                            somListHeaderBinding?.searchBarSomList?.searchBarTextField?.setText("")
                            viewModel.filterResult.value?.let {
                                if (it is Success) {
                                    it.data.statusList.find { it.key == STATUS_ALL_ORDER }?.let {
                                        somListOrderStatusFilterTab?.selectTab(it)
                                    }
                                }
                            }
                        } else {
                            somListBinding?.rvSomList?.post {
                                reshowNewOrderCoachMark()
                            }
                        }
                    }
                    waitingPaymentCoachMarkItemPosition, bulkProcessCoachMarkItemPosition -> {
                        if (currentIndex == bulkProcessCoachMarkItemPosition && viewModel.getTabActive() == STATUS_NEW_ORDER) return
                        if (currentIndex == waitingPaymentCoachMarkItemPosition && viewModel.getTabActive() == STATUS_ALL_ORDER) return
                        viewModel.resetGetOrderListParam()
                        viewModel.clearSomFilterUiModelList()
                        skipSearch = true
                        somListHeaderBinding?.searchBarSomList?.searchBarTextField?.setText("")
                        coachMarkIndexToShow = currentIndex
                        // if user press "Lanjut" after waiting payment coachmark, auto select new order filter, if user press "Balik"
                        // auto deselect new order to show all order
                        val targetStatusFilter =
                            if (currentIndex == bulkProcessCoachMarkItemPosition && viewModel.getTabActive() != STATUS_NEW_ORDER) {
                                STATUS_NEW_ORDER
                            } else STATUS_ALL_ORDER
                        shouldShowCoachMark = true
                        coachMark?.isDismissed = true
                        viewModel.filterResult.value?.let {
                            if (it is Success) {
                                it.data.statusList.find { it.key == targetStatusFilter }?.let {
                                    somListOrderStatusFilterTab?.selectTab(it)
                                }
                            }
                        }
                    }
                }
            }
        })
    }

    protected open fun createCoachMarkItems(firstNewOrderView: View?): ArrayList<CoachMark2Item> {
        return arrayListOf<CoachMark2Item>().apply {
            firstNewOrderView?.let {
                add(
                    CoachMark2Item(
                        it,
                        context?.resources?.getString(R.string.som_list_coachmark_new_order_card_title).orEmpty(),
                        context?.resources?.getString(R.string.som_list_coachmark_new_order_card_description).orEmpty()
                    )
                )
            }
            somListBinding?.sortFilterSomList?.let {
                add(
                    CoachMark2Item(
                        it,
                        context?.resources?.getString(R.string.som_list_coachmark_sort_filter_title).orEmpty(),
                        context?.resources?.getString(R.string.som_list_coachmark_sort_filter_description).orEmpty()
                    )
                )
            }
            if (somListHeaderBinding?.icSomListMenuWaitingPayment?.isVisible == true) {
                somListHeaderBinding?.icSomListMenuWaitingPayment?.let {
                    add(
                        CoachMark2Item(
                            it,
                            context?.resources?.getString(R.string.som_list_coachmark_waiting_payment_title).orEmpty(),
                            context?.resources?.getString(R.string.som_list_coachmark_waiting_payment_description).orEmpty()
                        )
                    )
                }
            }
            if (GlobalConfig.isSellerApp()) {
                somListBinding?.tvSomListBulk?.let {
                    add(
                        CoachMark2Item(
                            it,
                            context?.resources?.getString(R.string.som_list_coachmark_multi_select_title).orEmpty(),
                            context?.resources?.getString(R.string.som_list_coachmark_multi_select_description).orEmpty()
                        )
                    )
                }
            }
        }
    }

    private fun dismissCoachMark(removeScrollListener: Boolean = true) {
        if (coachMark?.currentIndex != -1 && coachMark?.isDismissed == false) {
            shouldShowCoachMark = true
            if (removeScrollListener)
                somListBinding?.rvSomList?.removeOnScrollListener(recyclerViewScrollListener)
            coachMarkIndexToShow = coachMark?.currentIndex.orZero()
            coachMark?.dismissCoachMark()
            coachMark?.isDismissed = true
        }
    }

    private fun reshowNewOrderCoachMark() {
        if (somListBinding?.scrollViewErrorState?.isVisible == false && shouldShowCoachMark && coachMarkIndexToShow == newOrderCoachMarkItemPosition &&
            (viewModel.getTabActive().isBlank() || viewModel.getTabActive() == STATUS_ALL_ORDER) &&
            somListSortFilterTab?.isFilterApplied() != true && somListHeaderBinding?.searchBarSomList?.searchBarTextField?.text.isNullOrBlank()
        ) {
            val firstNewOrderPosition =
                getFirstNewOrder(adapter.data.filterIsInstance<SomListOrderUiModel>())
            if (firstNewOrderPosition != -1) {
                somListBinding?.rvSomList?.stopScroll()
                somListLayoutManager?.scrollToPositionWithOffset(firstNewOrderPosition, 0)
                somListBinding?.rvSomList?.post {
                    somListLayoutManager?.findViewByPosition(firstNewOrderPosition)
                        ?.findViewById<UnifyButton>(R.id.btnQuickAction)?.let {
                            if (getVisiblePercent(it) == 0) {
                                CoachMarkPreference.setShown(
                                    it.context,
                                    SHARED_PREF_NEW_SOM_LIST_COACH_MARK,
                                    true
                                )
                                somListBinding?.rvSomList?.removeOnScrollListener(recyclerViewScrollListener)
                                somListBinding?.rvSomList?.addOnScrollListener(recyclerViewScrollListener)
                                currentNewOrderWithCoachMark = firstNewOrderPosition
                                shouldShowCoachMark = false
                                val coachMarkItems = createCoachMarkItems(it)
                                coachMark?.isDismissed = false
                                coachMark?.showCoachMark(
                                    step = coachMarkItems,
                                    index = coachMarkIndexToShow
                                )
                            }
                        }
                }
            }
        }
    }

    protected open fun reshowStatusFilterCoachMark() {
        if (shouldShowFilterCoachMark()) {
            createCoachMarkItems(somListBinding?.rvSomList).run {
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
            createCoachMarkItems(somListBinding?.rvSomList).run {
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
            createCoachMarkItems(somListBinding?.rvSomList).run {
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
        somListBinding?.btnBulkAction?.text = when (somListOrderStatusFilterTab?.getSelectedFilterStatus()) {
            STATUS_NEW_ORDER -> context?.resources?.getString(R.string.som_list_bulk_accept_order_button).orEmpty()
            KEY_CONFIRM_SHIPPING -> context?.resources?.getString(R.string.som_list_bulk_confirm_shipping_order_button).orEmpty()
            else -> ""
        }
        somListBinding?.containerBtnBulkAction?.visible()
        bulkAcceptButtonEnterAnimation =
            somListBinding?.containerBtnBulkAction?.animateSlide(somListBinding?.containerBtnBulkAction?.translationY.orZero(), Float.ZERO)
    }

    private fun animateBulkAcceptOrderButtonLeave() {
        if (bulkAcceptButtonEnterAnimation?.isRunning == true) bulkAcceptButtonEnterAnimation?.cancel()
        bulkAcceptButtonLeaveAnimation = somListBinding?.containerBtnBulkAction?.animateSlide(
            somListBinding?.containerBtnBulkAction?.translationY.orZero(),
            somListBinding?.containerBtnBulkAction?.height?.toFloat().orZero()
        )
        bulkAcceptButtonLeaveAnimation?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {}

            override fun onAnimationEnd(animation: Animator?) {
                somListBinding?.containerBtnBulkAction?.gone()
                bulkAcceptButtonLeaveAnimation?.removeListener(this)
            }

            override fun onAnimationCancel(animation: Animator?) {
                bulkAcceptButtonLeaveAnimation?.removeListener(this)
            }

            override fun onAnimationStart(animation: Animator?) {}
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
        somListBinding?.run {
            sortFilterSomList.translationY = translation
            sortFilterShimmer1.translationY = translation
            sortFilterShimmer2.translationY = translation
            sortFilterShimmer3.translationY = translation
            sortFilterShimmer4.translationY = translation
            sortFilterShimmer5.translationY = translation
            bulkActionCheckBoxContainer.translationY = translation
            dividerMultiSelect.translationY = translation
            val params = (somListBinding?.swipeRefreshLayoutSomList?.layoutParams as? ViewGroup.MarginLayoutParams)
            params?.topMargin = translation.toInt()
            swipeRefreshLayoutSomList.layoutParams = params
            containerBtnBulkAction.translationY = translation
            scrollViewErrorState.translationY = translation
            somAdminPermissionView.translationY = translation
        }
    }

    private fun refreshOrdersOnTabClicked(shouldScrollToTop: Boolean, refreshFilter: Boolean) {
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

    protected open fun tryReshowCoachMark() {
        view?.postDelayed({
            reshowNewOrderCoachMark()
            reshowStatusFilterCoachMark()
            reshowWaitingPaymentOrderListCoachMark()
            reshowBulkAcceptOrderCoachMark()
        }, DELAY_COACHMARK)
    }

    private fun getActivityPltPerformanceMonitoring() {
        somListLoadTimeMonitoring =
            (activity as? SomListLoadTimeMonitoringActivity)?.getSomListLoadTimeMonitoring()
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

    private fun isOrderWithCancellationRequest(order: SomListOrderUiModel) =
        order.cancelRequest == 1 && order.cancelRequestStatus != 0

    private fun setupToolbar() {
        (activity as? AppCompatActivity)?.supportActionBar?.hide()
        somListHeaderBinding?.icSomListNavigationBack?.run {
            showWithCondition(showBackButton())
            setOnClickListener {
                onFragmentBackPressed()
            }
        }
        somListHeaderBinding?.icSomListMenuWaitingPayment?.setOnClickListener {
            goToWaitingPaymentOrderListPage()
            val waitingPaymentOrderCounterResult = viewModel.waitingPaymentCounterResult.value
            if (waitingPaymentOrderCounterResult is Success) {
                SomAnalytics.eventClickWaitingPaymentOrderCard(
                    viewModel.getTabActive(),
                    waitingPaymentOrderCounterResult.data.amount,
                    userSession.userId,
                    userSession.shopId
                )
            }
            isWaitingPaymentOrderPageOpened = true
            updateToolbarMenu()
        }
        updateToolbarMenu()
        tryReshowCoachMark()
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

    protected open fun shouldShowFilterCoachMark() = somListBinding?.scrollViewErrorState?.isVisible == false &&
            shouldShowCoachMark && coachMarkIndexToShow == filterChipCoachMarkItemPosition &&
            somListBinding?.sortFilterSomList?.isVisible == true && somListBinding?.rvSomList != null

    protected open fun shouldShowWaitingPaymentCoachMark(waitingPaymentOrderListCountResult: Result<WaitingPaymentCounter>?) =
        somListBinding?.scrollViewErrorState?.isVisible == false && shouldShowCoachMark && somListBinding?.rvSomList != null &&
                coachMarkIndexToShow == waitingPaymentCoachMarkItemPosition && waitingPaymentOrderListCountResult is Success

    protected open fun shouldShowBulkAcceptOrderCoachMark() =
        somListBinding?.scrollViewErrorState?.isVisible == false &&
                shouldShowCoachMark && somListBinding?.rvSomList != null && coachMarkIndexToShow == bulkProcessCoachMarkItemPosition &&
                somListBinding?.tvSomListBulk?.isVisible == true && viewModel.getTabActive() == STATUS_NEW_ORDER

    protected open fun onSuccessGetFilter(
        result: Success<SomListFilterUiModel>,
        realtimeDataChangeCount: Int
    ): Int {
        /* apply result only if:
           1. First filter data (cache or cloud)
           2. Any filter data that is not from cache
         */
        if (realtimeDataChangeCount == 0 || !result.data.fromCache) {
            selectFilterTab(result, realtimeDataChangeCount)
            somListOrderStatusFilterTab?.show(result.data)
            somListSortFilterTab?.show(result.data)
            updateOrderCounter()
        }
        return if (!result.data.fromCache) realtimeDataChangeCount + 1 else realtimeDataChangeCount
    }
}