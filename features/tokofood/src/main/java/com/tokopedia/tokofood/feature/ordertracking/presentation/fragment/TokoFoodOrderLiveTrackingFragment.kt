package com.tokopedia.tokofood.feature.ordertracking.presentation.fragment

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.tokofood.common.util.TokofoodErrorLogger
import com.tokopedia.tokofood.databinding.FragmentTokofoodOrderTrackingBinding
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.BaseOrderTrackingTypeFactory
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.OrderTrackingAdapter
import com.tokopedia.tokofood.feature.ordertracking.presentation.toolbar.OrderTrackingToolbarHandler
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.DriverSectionUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderStatusLiveTrackingUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderTrackingEstimationUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.viewmodel.TokoFoodOrderTrackingViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.flow.collect

class TokoFoodOrderLiveTrackingFragment(
    private val binding: FragmentTokofoodOrderTrackingBinding?,
    private val viewModel: TokoFoodOrderTrackingViewModel,
    private val orderTrackingAdapter: OrderTrackingAdapter,
    private val toolbarHandler: OrderTrackingToolbarHandler?,
    private val initializeUnReadCounter: (String) -> Unit
) : LifecycleObserver {

    private var lifecycleOwner: LifecycleOwner? = null

    private var goFoodOrderNumber = ""

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun onResumeListener(owner: LifecycleOwner) {
        this.lifecycleOwner = owner
        observeOrderLiveTracking()
    }

    fun setSwipeRefreshDisabled() {
        binding?.orderTrackingSwipeRefresh?.isEnabled = false
    }

    private fun observeOrderLiveTracking() {
        lifecycleOwner?.lifecycleScope?.launchWhenResumed {
            viewModel.orderLiveTrackingStatus.collect {
                when (it) {
                    is Success -> {
                        updateAllOrderLiveTracking(it.data)
                        viewModel.updateOrderId(viewModel.getOrderId())
                    }
                    is Fail -> {
                        logExceptionToServerLogger(it.throwable)
                        viewModel.updateOrderId(viewModel.getOrderId())
                    }
                }
            }
        }
    }

    private fun updateAllOrderLiveTracking(orderStatusLiveTrackingUiModel: OrderStatusLiveTrackingUiModel) {
        with(orderStatusLiveTrackingUiModel) {
            setGoFoodOrderNumber(invoiceOrderNumberUiModel?.goFoodOrderNumber)
            toolbarHandler?.updateToolbarPoolBased(toolbarLiveTrackingUiModel)
            orderTrackingAdapter.updateLiveTrackingItem(tickerInfoData)
            orderTrackingAdapter.updateLiveTrackingItem(orderTrackingStatusInfoUiModel)
            orderTrackingAdapter.updateEtaLiveTracking(estimationUiModel)
            orderTrackingAdapter.updateLiveTrackingItem(estimationUiModel)
            updateDriverSectionUiModel(driverSectionUiModel)
            orderTrackingAdapter.updateLiveTrackingItem(invoiceOrderNumberUiModel)
        }
    }

    private fun updateDriverSectionUiModel(driverSectionUiModel: DriverSectionUiModel?) {
        val etaLiveTrackingIndex =
            orderTrackingAdapter.list.indexOfFirst { it is OrderTrackingEstimationUiModel }
        val driverSectionIndex =
            orderTrackingAdapter.list.indexOfFirst { it is DriverSectionUiModel }
        val newList = orderTrackingAdapter.list.toMutableList()
        driverSectionUiModel?.let { driverSectionModel ->
            if (etaLiveTrackingIndex != RecyclerView.NO_POSITION) {
                if (driverSectionIndex == RecyclerView.NO_POSITION) {
                    newList.add(
                        etaLiveTrackingIndex + Int.ONE,
                        driverSectionModel
                    )
                } else {
                    newList.set(
                        driverSectionIndex,
                        driverSectionModel
                    )
                }

                val updateNewItems = newList as? List<BaseOrderTrackingTypeFactory>
                updateNewItems?.let {
                    orderTrackingAdapter.updateOrderTracking(it.toList())
                }
            }
        }
    }

    private fun setGoFoodOrderNumber(goFoodOrderNumber: String?) {
        if (goFoodOrderNumber?.isNotBlank() == true && this.goFoodOrderNumber.isBlank()) {
            this.goFoodOrderNumber = goFoodOrderNumber
            initializeUnReadCounter(goFoodOrderNumber)
        }
    }

    private fun logExceptionToServerLogger(
        throwable: Throwable
    ) {
        TokofoodErrorLogger.logExceptionToServerLogger(
            TokofoodErrorLogger.PAGE.POST_PURCHASE,
            throwable,
            TokofoodErrorLogger.ErrorType.ERROR_POOL_POST_PURCHASE,
            viewModel.userSession.deviceId.orEmpty(),
            TokofoodErrorLogger.ErrorDescription.POOL_BASED_ERROR
        )
    }
}
