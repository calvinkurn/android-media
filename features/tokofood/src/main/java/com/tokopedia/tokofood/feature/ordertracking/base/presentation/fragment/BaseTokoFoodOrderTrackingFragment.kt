package com.tokopedia.tokofood.feature.ordertracking.base.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.removeObservers
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.FragmentTokofoodOrderTrackingBinding
import com.tokopedia.tokofood.feature.ordertracking.di.component.TokoFoodOrderTrackingComponent
import com.tokopedia.tokofood.feature.ordertracking.domain.constants.OrderStatusType
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.OrderTrackingAdapter
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.OrderTrackingAdapterTypeFactoryImpl
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.OrderTrackingListener
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.RecyclerViewPollerListener
import com.tokopedia.tokofood.feature.ordertracking.presentation.fragment.TokoFoodOrderLiveTrackingFragment
import com.tokopedia.tokofood.feature.ordertracking.presentation.navigator.OrderTrackingNavigator
import com.tokopedia.tokofood.feature.ordertracking.presentation.toolbar.OrderTrackingToolbarHandler
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.ActionButtonsUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderDetailResultUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderDetailToggleCtaUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderTrackingErrorUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.TemporaryFinishOrderUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.ToolbarLiveTrackingUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.viewmodel.TokoFoodOrderTrackingViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.lang.ref.WeakReference
import javax.inject.Inject

class BaseTokoFoodOrderTrackingFragment :
    BaseDaggerFragment(), RecyclerViewPollerListener, OrderTrackingListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(TokoFoodOrderTrackingViewModel::class.java)
    }

    private val orderTrackingAdapterTypeFactory by lazy(LazyThreadSafetyMode.NONE) {
        OrderTrackingAdapterTypeFactoryImpl(this, this)
    }

    private val orderTrackingAdapter by lazy {
        OrderTrackingAdapter(orderTrackingAdapterTypeFactory)
    }

    private val navigator by lazy {
        OrderTrackingNavigator(this)
    }

    private var toolbarHandler: OrderTrackingToolbarHandler? = null

    private var binding by autoClearedNullable<FragmentTokofoodOrderTrackingBinding>()

    private var orderLiveTrackingFragment: TokoFoodOrderLiveTrackingFragment? = null

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(TokoFoodOrderTrackingComponent::class.java).inject(this)
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
        fetchOrderDetail(ORDER_TRACKING_RESOURCE)
        observeOrderDetail()
        observeOrderCompletedLiveTracking()
    }

    override fun onDestroy() {
        removeObservers(
            viewModel.orderDetailResult,
            viewModel.orderDetailResult,
            viewModel.orderCompletedLiveTracking
        )
        toolbarHandler?.activity?.clear()
        orderLiveTrackingFragment?.let {
            lifecycle.removeObserver(it)
        }
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
        context?.let { RouteManager.route(it, linkUrl) }
    }

    override fun onAutoRefreshTempFinishOrder(orderDetailResultUiModel: OrderDetailResultUiModel) {
        with(orderDetailResultUiModel) {
            orderTrackingAdapter.removeOrderTrackingData()
            orderTrackingAdapter.updateOrderTracking(orderDetailList)
            updateViewsOrderCompleted(actionButtonsUiModel, toolbarLiveTrackingUiModel, orderStatus)
        }
    }

    override val parentPool: RecyclerView.RecycledViewPool
        get() = binding?.rvOrderTracking?.recycledViewPool ?: RecyclerView.RecycledViewPool()

    private fun setupToolbar() {
        toolbarHandler = OrderTrackingToolbarHandler(WeakReference(activity), binding)
    }

    private fun setupRvOrderTracking() {
        binding?.rvOrderTracking?.run {
            layoutManager = LinearLayoutManager(context)
            adapter = orderTrackingAdapter
        }
    }

    private fun fetchOrderDetail(resourceId: Int) {
        orderTrackingAdapter.run {
            removeOrderTrackingData()
            hideError()
            showLoadingShimmer(LoadingModel())
        }
        viewModel.fetchOrderDetail(resourceId)
    }

    private fun observeOrderDetail() {
        observe(viewModel.orderDetailResult) {
            hideViews()
            when (it) {
                is Success -> {
                    orderTrackingAdapter.updateOrderTracking(it.data.orderDetailList)
                    setupViews(
                        it.data.orderStatus,
                        it.data.actionButtonsUiModel,
                        it.data.toolbarLiveTrackingUiModel
                    )
                    fetchOrderLiveTracking(it.data.orderStatus)
                }
                is Fail -> {
                    orderTrackingAdapter.showError(OrderTrackingErrorUiModel(it.throwable))
                }
            }
        }
    }

    private fun hideViews() {
        binding?.orderTrackingSwipeRefresh?.isRefreshing = false
        orderTrackingAdapter.hideLoadingShimmer()
    }

    private fun observeOrderCompletedLiveTracking() {
        observe(viewModel.orderCompletedLiveTracking) {
            orderTrackingAdapter.removeOrderTrackingData()
            when (it) {
                is Success -> {
                    updateOrderCompleted(it.data)
                }
                is Fail -> {
                    orderTrackingAdapter.showError(OrderTrackingErrorUiModel(it.throwable))
                }
            }
        }
    }

    private fun updateOrderCompleted(orderDetailResultUiModel: OrderDetailResultUiModel) {
        binding?.containerOrderTrackingHelpButton?.hide()
        with(orderDetailResultUiModel) {
            if (orderStatus == OrderStatusType.COMPLETED) {
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
                    orderStatus
                )
            }
        }
    }

    private fun fetchOrderLiveTracking(orderStatus: String) {
        orderLiveTrackingFragment?.let {
            viewModel.updateOrderId(orderStatus)
        }
    }

    private fun setupViews(
        orderStatus: String,
        actionButtonsUiModel: ActionButtonsUiModel,
        toolbarLiveTrackingUiModel: ToolbarLiveTrackingUiModel
    ) {
        binding?.run {
            if (orderStatus in listOf(OrderStatusType.COMPLETED, OrderStatusType.CANCELLED)) {
                updateViewsOrderCompleted(
                    actionButtonsUiModel,
                    toolbarLiveTrackingUiModel,
                    orderStatus
                )
            } else {
                orderLiveTrackingFragment = TokoFoodOrderLiveTrackingFragment(
                    binding,
                    viewModel,
                    orderTrackingAdapter,
                    navigator,
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
        orderStatus: String
    ) {
        setupStickyButton(actionButtonsUiModel)
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
            setupStickyButton(actionButtonsUiModel.primaryActionButton)
            setToolbarLiveTracking(toolbarLiveTrackingUiModel, orderStatus)
        }
    }

    private fun setToolbarLiveTracking(
        toolbarLiveTrackingUiModel: ToolbarLiveTrackingUiModel,
        orderStatus: String
    ) {
        toolbarHandler?.setToolbarLiveTracking(toolbarLiveTrackingUiModel)
        toolbarHandler?.setToolbarScrolling(orderStatus)
    }

    private fun setupStickyButton(actionButtons: ActionButtonsUiModel) {
        binding?.run {
            containerOrderTrackingHelpButton.hide()
            containerOrderTrackingActionsButton.apply {
                setOrderTrackingNavigator(navigator)
                setupActionButtons(actionButtons, childFragmentManager)
                show()
            }
        }
    }

    private fun setSwipeRefreshEnabled() {
        binding?.orderTrackingSwipeRefresh?.run {
            isEnabled = true
            setOnRefreshListener {
                fetchOrderDetail(ORDER_DETAIL_RESOURCE)
            }
        }
    }

    companion object {

        fun newInstance(): BaseTokoFoodOrderTrackingFragment {
            return BaseTokoFoodOrderTrackingFragment()
        }

        private val ORDER_TRACKING_RESOURCE = R.raw.ordertracking
        private val ORDER_DETAIL_RESOURCE = R.raw.orderdetailsuccess
    }

}