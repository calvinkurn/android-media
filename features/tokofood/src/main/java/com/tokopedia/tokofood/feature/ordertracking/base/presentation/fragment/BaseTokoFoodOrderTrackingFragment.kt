package com.tokopedia.tokofood.feature.ordertracking.base.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.tokofood.DeeplinkMapperTokoFood
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.removeObservers
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.tokofood.common.util.TokofoodErrorLogger
import com.tokopedia.tokofood.common.util.TokofoodExt.showErrorToaster
import com.tokopedia.tokofood.common.util.TokofoodRouteManager
import com.tokopedia.tokofood.databinding.FragmentTokofoodOrderTrackingBinding
import com.tokopedia.tokofood.feature.ordertracking.analytics.TokoFoodPostPurchaseAnalytics
import com.tokopedia.tokofood.feature.ordertracking.di.component.DaggerTokoFoodOrderTrackingComponent
import com.tokopedia.tokofood.feature.ordertracking.domain.constants.OrderStatusType
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.OrderTrackingAdapter
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.OrderTrackingAdapterTypeFactoryImpl
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.OrderTrackingListener
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.RecyclerViewPollerListener
import com.tokopedia.tokofood.feature.ordertracking.presentation.bottomsheet.DriverCallBottomSheet
import com.tokopedia.tokofood.feature.ordertracking.presentation.fragment.TokoFoodOrderLiveTrackingFragment
import com.tokopedia.tokofood.feature.ordertracking.presentation.navigator.OrderTrackingNavigator
import com.tokopedia.tokofood.feature.ordertracking.presentation.toolbar.OrderTrackingToolbarHandler
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.ActionButtonsUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.DriverPhoneNumberUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.DriverSectionUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.MerchantDataUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderDetailResultUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderDetailToggleCtaUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderTrackingErrorUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.TemporaryFinishOrderUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.ToolbarLiveTrackingUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder.TrackingWrapperUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.viewmodel.TokoFoodOrderTrackingViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import javax.inject.Inject

class BaseTokoFoodOrderTrackingFragment :
    BaseDaggerFragment(), RecyclerViewPollerListener, OrderTrackingListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var tracking: TokoFoodPostPurchaseAnalytics

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(TokoFoodOrderTrackingViewModel::class.java)
    }

    private val orderTrackingAdapterTypeFactory by lazy(LazyThreadSafetyMode.NONE) {
        OrderTrackingAdapterTypeFactoryImpl(this, this)
    }

    private val orderTrackingAdapter by lazy {
        OrderTrackingAdapter(orderTrackingAdapterTypeFactory)
    }

    private val navigator by lazy(LazyThreadSafetyMode.NONE) {
        OrderTrackingNavigator(this, tracking)
    }

    private val orderId by lazy {
        arguments?.getString(DeeplinkMapperTokoFood.PATH_ORDER_ID).orEmpty()
    }

    private var toolbarHandler: OrderTrackingToolbarHandler? = null

    private var binding by autoClearedNullable<FragmentTokofoodOrderTrackingBinding>()

    private var orderLiveTrackingFragment: TokoFoodOrderLiveTrackingFragment? = null

    private var delayAutoRefreshFinishOrderTempJob: Job? = null

    private var loaderDialog: LoaderDialog? = null

    override fun getScreenName(): String = ""

    override fun initInjector() {
        activity?.let {
            DaggerTokoFoodOrderTrackingComponent
                .builder()
                .baseAppComponent((it.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
        }
    }

    override fun onResume() {
        super.onResume()
        tracking.viewOrderDetailPage(viewModel.getMerchantData()?.merchantId.orEmpty())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTokofoodOrderTrackingBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupRvOrderTracking()
        fetchOrderDetail()
        observeOrderDetail()
        observeOrderCompletedLiveTracking()
        observeDriverPhoneNumber()
    }

    override fun onDestroy() {
        removeObservers(viewModel.orderDetailResult, viewModel.orderCompletedLiveTracking)
        toolbarHandler?.activity?.clear()
        orderLiveTrackingFragment?.let {
            lifecycle.removeObserver(it)
        }
        hideLoaderDriverCall()
        delayAutoRefreshFinishOrderTempJob?.cancel()
        loaderDialog = null
        delayAutoRefreshFinishOrderTempJob = null
        orderLiveTrackingFragment = null
        toolbarHandler = null
        super.onDestroy()
    }

    override fun onToggleClicked(
        orderDetailToggleCta: OrderDetailToggleCtaUiModel,
        isExpandable: Boolean
    ) {
        if (isExpandable) {
            val newFoodItems =
                viewModel.getFoodItems().slice(Int.ONE..viewModel.getFoodItems().size - Int.ONE)
            val newToggleCta = orderDetailToggleCta.copy(isExpand = true)
            orderTrackingAdapter.expandOrderDetail(newFoodItems)
            orderTrackingAdapter.updateItem(orderDetailToggleCta, newToggleCta)
        } else {
            val newToggleCta = orderDetailToggleCta.copy(isExpand = false)
            orderTrackingAdapter.collapseOrderDetail()
            orderTrackingAdapter.updateItem(orderDetailToggleCta, newToggleCta)
        }
    }

    override fun onTickerLinkClick(linkUrl: String) {
        context?.let {
            TokofoodRouteManager.routePrioritizeInternal(it, linkUrl)
        }
    }

    override fun onAutoRefreshTempFinishOrder(orderDetailResultUiModel: OrderDetailResultUiModel) {
        delayAutoRefreshFinishOrderTempJob?.cancel()
        delayAutoRefreshFinishOrderTempJob =
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                delay(TWO_SECONDS)
                with(orderDetailResultUiModel) {
                    orderTrackingAdapter.removeOrderTrackingData()
                    orderTrackingAdapter.updateOrderTracking(orderDetailList)
                    setToolbarLiveTracking(toolbarLiveTrackingUiModel, orderDetailResultUiModel.orderStatusKey)
                }
            }
    }

    override fun onClickDriverCall() {
        tracking.clickCallDriverIcon(orderId, viewModel.getMerchantData()?.merchantId.orEmpty())
        showLoaderDriverCall()
        viewModel.fetchDriverPhoneNumber(orderId)
    }

    override fun onErrorActionClicked() {
        fetchOrderDetail()
    }

    override fun onInvoiceOrderClicked(invoiceNumber: String, invoiceUrl: String) {
        navigator.goToPrintInvoicePage(invoiceUrl, invoiceNumber)
    }

    override val parentPool: RecyclerView.RecycledViewPool
        get() = binding?.rvOrderTracking?.recycledViewPool ?: RecyclerView.RecycledViewPool()

    private fun showLoaderDriverCall() {
        context?.let {
            loaderDialog = LoaderDialog(it).apply {
                dialog.setCancelable(false)
                dialog.setCanceledOnTouchOutside(false)
            }
            loaderDialog?.show()
        }
    }

    private fun hideLoaderDriverCall() {
        if (loaderDialog?.dialog?.isShowing == true) loaderDialog?.dialog?.dismiss()
    }

    private fun setupToolbar() {
        toolbarHandler = OrderTrackingToolbarHandler(WeakReference(activity), binding)
    }

    private fun setupRvOrderTracking() {
        binding?.rvOrderTracking?.run {
            layoutManager = LinearLayoutManager(context)
            adapter = orderTrackingAdapter
        }
    }

    private fun fetchOrderDetail() {
        orderTrackingAdapter.run {
            removeOrderTrackingData()
            hideError()
            showLoadingShimmer(LoadingModel())
        }
        viewModel.fetchOrderDetail(orderId)
    }

    private fun observeOrderDetail() {
        observe(viewModel.orderDetailResult) {
            hideViews()
            when (it) {
                is Success -> {
                    orderTrackingAdapter.updateOrderTracking(it.data.orderDetailList)
                    setupViews(
                        it.data.orderStatusKey,
                        it.data.actionButtonsUiModel,
                        it.data.toolbarLiveTrackingUiModel,
                        it.data.merchantData,
                    )
                    fetchOrderLiveTracking(orderId)
                }
                is Fail -> {
                    logExceptionToServerLogger(
                        it.throwable,
                        TokofoodErrorLogger.ErrorType.ERROR_PAGE,
                        TokofoodErrorLogger.ErrorDescription.RENDER_PAGE_ERROR
                    )
                    hideStickyButtons()
                    orderTrackingAdapter.showError(OrderTrackingErrorUiModel(it.throwable))
                }
            }
        }
    }

    private fun observeDriverPhoneNumber() {
        observe(viewModel.driverPhoneNumber) {
            hideLoaderDriverCall()
            when (it) {
                is Success -> {
                    updateDriverCall(it.data)
                }
                is Fail -> {
                    logExceptionToServerLogger(
                        it.throwable,
                        TokofoodErrorLogger.ErrorType.ERROR_DRIVER_PHONE_NUMBER,
                        TokofoodErrorLogger.ErrorDescription.ERROR_DRIVER_PHONE_NUMBER
                    )
                    view?.showErrorToaster(
                        context?.getString(
                            com.tokopedia.tokofood.R.string.error_message_hit_driver_phone_number
                        ).orEmpty()
                    )
                }
            }
        }
    }

    private fun updateDriverCall(driverPhoneNumberUiModel: DriverPhoneNumberUiModel) {
        updateDriverCallState(driverPhoneNumberUiModel)
        showDriverCallBottomSheet(driverPhoneNumberUiModel)
    }

    private fun updateDriverCallState(driverPhoneNumberUiModel: DriverPhoneNumberUiModel) {
        val oldItem = orderTrackingAdapter.filterUiModel<DriverSectionUiModel>()
        oldItem?.let {
            val newItem = it.copy(isCallable = driverPhoneNumberUiModel.isCallable)
            orderTrackingAdapter.updateItem(oldItem, newItem)
        }
    }

    private fun showDriverCallBottomSheet(driverPhoneNumberUiModel: DriverPhoneNumberUiModel) {
        val driverCallBottomSheet = DriverCallBottomSheet.newInstance(
            driverPhoneNumberUiModel.phoneNumber,
            driverPhoneNumberUiModel.isCallable
        )
        driverCallBottomSheet.setTrackingListener {
            tracking.clickCallDriverCtaInBottomSheet(
                orderId, viewModel.getMerchantData()?.merchantId.orEmpty()
            )
        }
        driverCallBottomSheet.show(childFragmentManager)
    }

    private fun hideViews() {
        binding?.orderTrackingSwipeRefresh?.isRefreshing = false
        orderTrackingAdapter.hideLoadingShimmer()
    }

    private fun hideStickyButtons() {
        binding?.run {
            if (containerOrderTrackingHelpButton.isVisible) {
                containerOrderTrackingHelpButton.hide()
            }
            if (containerOrderTrackingActionsButton.isVisible) {
                containerOrderTrackingActionsButton.hide()
            }
        }
    }

    private fun observeOrderCompletedLiveTracking() {
        observe(viewModel.orderCompletedLiveTracking) {
            orderTrackingAdapter.removeOrderTrackingData()
            when (it) {
                is Success -> {
                    updateOrderCompleted(it.data)
                }
                is Fail -> {
                    logExceptionToServerLogger(
                        it.throwable,
                        TokofoodErrorLogger.ErrorType.ERROR_COMPLETED_ORDER_POST_PURCHASE,
                        TokofoodErrorLogger.ErrorDescription.RENDER_PAGE_ERROR
                    )
                    hideStickyButtons()
                    orderTrackingAdapter.showError(OrderTrackingErrorUiModel(it.throwable))
                }
            }
        }
    }

    private fun updateOrderCompleted(orderDetailResultUiModel: OrderDetailResultUiModel) {
        binding?.containerOrderTrackingHelpButton?.hide()
        with(orderDetailResultUiModel) {
            if (orderStatusKey == OrderStatusType.COMPLETED) {
                orderTrackingAdapter.updateOrderTracking(
                    listOf(
                        TemporaryFinishOrderUiModel(orderDetailResultUiModel)
                    )
                )
            } else {
                orderTrackingAdapter.updateOrderTracking(orderDetailList)
                updateViewsOrderCompleted(
                    actionButtonsUiModel,
                    toolbarLiveTrackingUiModel,
                    orderStatusKey,
                    orderDetailResultUiModel.merchantData
                )
            }
        }
    }

    private fun fetchOrderLiveTracking(orderId: String) {
        orderLiveTrackingFragment?.let {
            viewModel.updateOrderId(orderId)
        }
    }

    private fun setupViews(
        orderStatus: String,
        actionButtonsUiModel: ActionButtonsUiModel,
        toolbarLiveTrackingUiModel: ToolbarLiveTrackingUiModel,
        merchantData: MerchantDataUiModel,
    ) {
        binding?.run {
            if (orderStatus in listOf(OrderStatusType.COMPLETED, OrderStatusType.CANCELLED)) {
                updateViewsOrderCompleted(
                    actionButtonsUiModel,
                    toolbarLiveTrackingUiModel,
                    orderStatus,
                    merchantData
                )
            } else {
                orderLiveTrackingFragment = TokoFoodOrderLiveTrackingFragment(
                    binding,
                    viewModel,
                    orderTrackingAdapter,
                    toolbarHandler
                )
                orderLiveTrackingFragment?.let { lifecycle.addObserver(it) }
                updateViewsOrderLiveTracking(
                    actionButtonsUiModel,
                    toolbarLiveTrackingUiModel,
                    orderStatus
                )
            }
        }
    }

    private fun updateViewsOrderCompleted(
        actionButtonsUiModel: ActionButtonsUiModel,
        toolbarLiveTrackingUiModel: ToolbarLiveTrackingUiModel,
        orderStatus: String,
        merchantDataModel: MerchantDataUiModel
    ) {
        if (orderStatus == OrderStatusType.COMPLETED) {
            setupStickyActionButton(actionButtonsUiModel, merchantDataModel)
        } else {
            setupStickyHelpButton(actionButtonsUiModel.primaryActionButton)
        }
        setSwipeRefreshEnabled()
        setToolbarLiveTracking(toolbarLiveTrackingUiModel, orderStatus)
    }

    private fun updateViewsOrderLiveTracking(
        actionButtonsUiModel: ActionButtonsUiModel,
        toolbarLiveTrackingUiModel: ToolbarLiveTrackingUiModel,
        orderStatus: String
    ) {
        orderLiveTrackingFragment?.apply {
            setSwipeRefreshDisabled()
            setupStickyHelpButton(actionButtonsUiModel.primaryActionButton)
            setToolbarLiveTracking(toolbarLiveTrackingUiModel, orderStatus)
        }
    }

    private fun setupStickyHelpButton(primaryActionButton: ActionButtonsUiModel.ActionButton) {
        binding?.run {
            containerOrderTrackingActionsButton.hide()
            containerOrderTrackingHelpButton.apply {
                setOrderTrackingNavigator(navigator)
                setupHelpButton(
                    viewModel.getOrderId(),
                    primaryActionButton,
                    viewModel.getMerchantData()
                )
                show()
            }
        }
    }

    private fun setupStickyActionButton(
        actionButtons: ActionButtonsUiModel,
        merchantData: MerchantDataUiModel
    ) {
        binding?.run {
            containerOrderTrackingHelpButton.hide()
            containerOrderTrackingActionsButton.apply {
                setOrderTrackingNavigator(navigator)
                setupActionButtons(
                    TrackingWrapperUiModel(orderId, viewModel.getFoodItems(), merchantData),
                    actionButtons,
                    childFragmentManager
                )
                show()
            }
        }
    }

    private fun setToolbarLiveTracking(
        toolbarLiveTrackingUiModel: ToolbarLiveTrackingUiModel,
        orderStatus: String
    ) {
        toolbarHandler?.setToolbarLiveTracking(toolbarLiveTrackingUiModel)
        toolbarHandler?.setToolbarScrolling(orderStatus)
    }

    private fun setSwipeRefreshEnabled() {
        binding?.orderTrackingSwipeRefresh?.run {
            isEnabled = true
            setOnRefreshListener {
                fetchOrderDetail()
            }
        }
    }

    private fun logExceptionToServerLogger(
        throwable: Throwable,
        errorType: String,
        errorDesc: String
    ) {
        TokofoodErrorLogger.logExceptionToServerLogger(
            TokofoodErrorLogger.PAGE.POST_PURCHASE,
            throwable,
            errorType,
            viewModel.userSession.deviceId.orEmpty(),
            errorDesc
        )
    }

    companion object {

        const val TWO_SECONDS = 2000L

        fun newInstance(bundle: Bundle?): BaseTokoFoodOrderTrackingFragment {
            return if (bundle == null) {
                BaseTokoFoodOrderTrackingFragment()
            } else {
                BaseTokoFoodOrderTrackingFragment().apply {
                    arguments = bundle
                }
            }
        }
    }

}