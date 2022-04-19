package com.tokopedia.tokofood.feature.ordertracking.presentation.fragment

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.lifecycleScope
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokofood.databinding.FragmentTokofoodOrderTrackingBinding
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.OrderTrackingAdapter
import com.tokopedia.tokofood.feature.ordertracking.presentation.navigator.OrderTrackingNavigator
import com.tokopedia.tokofood.feature.ordertracking.presentation.toolbar.OrderTrackingToolbarHandler
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.ActionButtonsUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderLiveTrackingStatusEvent
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderStatusLiveTrackingUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.viewmodel.TokoFoodOrderTrackingViewModel
import kotlinx.coroutines.flow.collect


class TokoFoodOrderLiveTrackingFragment(
    private val binding: FragmentTokofoodOrderTrackingBinding?,
    private val viewModel: TokoFoodOrderTrackingViewModel,
    private val orderTrackingAdapter: OrderTrackingAdapter,
    private val navigator: OrderTrackingNavigator,
    private val toolbarHandler: OrderTrackingToolbarHandler?
) : LifecycleObserver {

    private var lifecycleOwner: LifecycleOwner? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun onResumeListener(owner: LifecycleOwner) {
        this.lifecycleOwner = owner
        observeOrderLiveTracking()
    }

    fun setupStickyButton(primaryActionButton: ActionButtonsUiModel.ActionButton) {
        binding?.run {
            containerOrderTrackingActionsButton.hide()
            containerOrderTrackingHelpButton.apply {
                setOrderTrackingNavigator(navigator)
                setupHelpButton(primaryActionButton)
                show()
            }
        }
    }

    fun setSwipeRefreshDisabled() {
        binding?.orderTrackingSwipeRefresh?.isEnabled = false
    }

    private fun observeOrderLiveTracking() {
        lifecycleOwner?.lifecycleScope?.launchWhenResumed {
            viewModel.orderLiveTrackingStatus.collect {
                when (it) {
                    is OrderLiveTrackingStatusEvent.Success -> {
                        updateAllOrderLiveTracking(it.orderStatusLiveTrackingUiModel)
                        viewModel.updateOrderId(it.orderStatusLiveTrackingUiModel.orderStatusKey)
                    }
                    is OrderLiveTrackingStatusEvent.Error -> {

                    }
                }
            }
        }
    }

    private fun updateAllOrderLiveTracking(orderStatusLiveTrackingUiModel: OrderStatusLiveTrackingUiModel) {
        with(orderStatusLiveTrackingUiModel) {
            toolbarHandler?.updateToolbarPoolBased(toolbarLiveTrackingUiModel)
            orderTrackingAdapter.updateLiveTrackingItem(tickerInfoData)
            orderTrackingAdapter.updateLiveTrackingItem(orderTrackingStatusInfoUiModel)
            orderTrackingAdapter.updateEtaLiveTracking(estimationUiModel)
            orderTrackingAdapter.updateLiveTrackingItem(invoiceOrderNumberUiModel)
        }
    }
}