package com.tokopedia.tokofood.feature.ordertracking.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.FragmentTokofoodOrderTrackingBinding
import com.tokopedia.tokofood.feature.ordertracking.di.component.TokoFoodOrderTrackingComponent
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.BaseOrderTrackingTypeFactory
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.OrderTrackingAdapter
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.OrderTrackingAdapterTypeFactoryImpl
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.OrderTrackingListener
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.RecyclerViewPollerListener
import com.tokopedia.tokofood.feature.ordertracking.presentation.navigator.OrderTrackingNavigator
import com.tokopedia.tokofood.feature.ordertracking.presentation.toolbar.OrderTrackingToolbarHandler
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.ActionButtonsUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderDetailToggleCtaUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderLiveTrackingStatusEvent
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderStatusLiveTrackingUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderTrackingErrorUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.ToolbarLiveTrackingUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.viewmodel.TokoFoodOrderTrackingViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class TokoFoodOrderTrackingFragment : BaseDaggerFragment(), RecyclerViewPollerListener,
    OrderTrackingListener {

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

    private val toolbarHandler: OrderTrackingToolbarHandler by lazy(LazyThreadSafetyMode.NONE) {
        OrderTrackingToolbarHandler()
    }

    private var binding by autoClearedNullable<FragmentTokofoodOrderTrackingBinding>()

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
        setupActionBar()
        setupRvOrderTracking()
        setSwipeRefreshDisabled()
        observeOrderDetail()
        observeOrderLiveTracking()
        fetchOrderDetail()
    }

    override fun onDestroy() {
        viewModel.orderDetailResult.removeObservers(this)
        super.onDestroy()
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(TokoFoodOrderTrackingComponent::class.java).inject(this)
    }

    override val parentPool: RecyclerView.RecycledViewPool
        get() = binding?.rvOrderTracking?.recycledViewPool ?: RecyclerView.RecycledViewPool()

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

    private fun observeOrderDetail() {
        observe(viewModel.orderDetailResult) {
            orderTrackingAdapter.hideLoadingShimmer()
            when (it) {
                is Success -> {
                    val isCompletedOrder = it.data.isOrderCompleted
                    fetchOrderLiveTracking(isCompletedOrder)
                    orderTrackingAdapter.updateOrderTracking(it.data.orderDetailList)
                    setupViews(isCompletedOrder, it.data.actionButtonsUiModel)
                    setToolbarLiveTracking(it.data.toolbarLiveTrackingUiModel, isCompletedOrder)
                }
                is Fail -> {
                    orderTrackingAdapter.showError(OrderTrackingErrorUiModel(it.throwable))
                }
            }
        }
    }

    private fun observeOrderLiveTracking() {
        lifecycleScope.launchWhenResumed {
            viewModel.orderLiveTrackingStatus.collect {
                when (it) {
                    is OrderLiveTrackingStatusEvent.Success -> {
                        updateAllOrderLiveTracking(it.orderStatusLiveTrackingUiModel)
                    }
                    is OrderLiveTrackingStatusEvent.Error -> {

                    }
                }
            }
        }
    }

    private fun updateAllOrderLiveTracking(orderStatusLiveTrackingUiModel: OrderStatusLiveTrackingUiModel) {
        with(orderStatusLiveTrackingUiModel) {
            updateLiveTrackingItem(tickerInfoData)
            updateLiveTrackingItem(orderTrackingStatusInfoUiModel)
            orderTrackingAdapter.updateEtaLiveTracking(estimationUiModel)
            updateLiveTrackingItem(invoiceOrderNumberUiModel)
        }
    }

    private inline fun <reified T: BaseOrderTrackingTypeFactory> updateLiveTrackingItem(newItem: T?) {
        if (newItem != null) {
            val oldItem = orderTrackingAdapter.filterUiModel<T>()
            oldItem?.let {
                orderTrackingAdapter.updateItem(
                    oldItem,
                    newItem
                )
            }
        }
    }

    private fun fetchOrderLiveTracking(isOrderCompleted: Boolean) {
        if (!isOrderCompleted) {
            viewModel.fetchOrderLiveTracking()
        }
    }

    private fun fetchOrderDetail() {
        orderTrackingAdapter.run {
            hideError()
            showLoadingShimmer(LoadingModel())
        }
        viewModel.fetchOrderDetail(ORDER_TRACKING_RESOURCE)
    }

    private fun setupRvOrderTracking() {
        binding?.rvOrderTracking?.run {
            layoutManager = LinearLayoutManager(context)
            adapter = orderTrackingAdapter
        }
    }

    private fun setSwipeRefreshDisabled() {
        binding?.orderTrackingSwipeRefresh?.run {
            isEnabled = false
            isRefreshing = false
        }
    }

    private fun setupViews(isOrderCompleted: Boolean, actionButtonsUiModel: ActionButtonsUiModel) {
        binding?.run {
            if (isOrderCompleted) {
                containerOrderTrackingHelpButton.hide()
                containerOrderTrackingActionsButton.apply {
                    setOrderTrackingNavigator(navigator)
                    setupActionButtons(actionButtonsUiModel, childFragmentManager)
                    show()
                }
            } else {
                containerOrderTrackingActionsButton.hide()
                containerOrderTrackingHelpButton.apply {
                    setOrderTrackingNavigator(navigator)
                    setupHelpButton(actionButtonsUiModel.primaryActionButton)
                    show()
                }
            }
        }
    }

    private fun setupActionBar() {
        (activity as? AppCompatActivity)?.run {
            supportActionBar?.hide()
            setSupportActionBar(binding?.orderTrackingToolbar)
            supportActionBar?.run {
                title = getString(com.tokopedia.tokofood.R.string.title_tokofood_post_purchase)
            }
        }
    }

    private fun setToolbarLiveTracking(
        toolbarLiveTrackingUiModel: ToolbarLiveTrackingUiModel,
        isOrderCompleted: Boolean
    ) {
        if (!isOrderCompleted) {
            toolbarHandler.setToolbarLiveTracking(toolbarLiveTrackingUiModel)
            toolbarHandler.setToolbarScrolling(binding, isOrderCompleted)
        }
    }

    companion object {

        fun newInstance(): TokoFoodOrderTrackingFragment {
            return TokoFoodOrderTrackingFragment()
        }

        private val ORDER_TRACKING_RESOURCE = R.raw.ordertracking
        private val ORDER_DETAIL_RESOURCE = R.raw.orderdetail
    }
}