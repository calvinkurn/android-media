package com.tokopedia.sellerorder.orderextension.presentation.delegate

import com.tokopedia.sellerorder.orderextension.presentation.bottomsheet.SomBottomSheetOrderExtensionRequest
import com.tokopedia.sellerorder.orderextension.presentation.model.OrderExtensionRequestInfoUiModel

class SomBottomSheetOrderExtensionRequestManagerImpl : ISomBottomSheetOrderExtensionRequestManager {
    override var somBottomSheetOrderExtensionRequest: SomBottomSheetOrderExtensionRequest? = null

    private lateinit var _mediator: ISomBottomSheetOrderExtensionRequestManager.Mediator
    private lateinit var _listener: ISomBottomSheetOrderExtensionRequestManager.Listener

    override fun registerSomBottomSheetOrderExtensionRequest(
        mediator: ISomBottomSheetOrderExtensionRequestManager.Mediator,
        listener: ISomBottomSheetOrderExtensionRequestManager.Listener
    ) {
        _mediator = mediator
        _listener = listener
        registerLiveDataObservers()
    }

    override fun showSomBottomSheetOrderExtensionRequest() {
        _mediator.getSomOrderExtensionViewModel().getSomOrderExtensionRequestInfoLoadingState()
    }

    private fun registerLiveDataObservers() {
        _mediator
            .getSomOrderExtensionViewModel()
            .orderExtensionRequestInfo
            .run {
                removeObserver(::orderExtensionRequestInfoObserver)
                observe(_mediator.getViewLifecycleOwner(), ::orderExtensionRequestInfoObserver)
            }
    }

    private fun showBottomSheet(data: OrderExtensionRequestInfoUiModel) {
        somBottomSheetOrderExtensionRequest = somBottomSheetOrderExtensionRequest ?: createBottomSheet(data)
        reInitSomBottomSheetOrderExtensionRequest(data)
        somBottomSheetOrderExtensionRequest?.show()
    }

    private fun createBottomSheet(data: OrderExtensionRequestInfoUiModel): SomBottomSheetOrderExtensionRequest? {
        val context = _mediator.getContext() ?: return null
        val fragmentManager = _mediator.getFragmentManager() ?: return null
        return SomBottomSheetOrderExtensionRequest(
            fragmentManager = fragmentManager,
            context = context,
            orderId = _mediator.getSomOrderExtensionOrderId(),
            data = data,
            viewModel = _mediator.getSomOrderExtensionViewModel()
        )
    }

    private fun reInitSomBottomSheetOrderExtensionRequest(data: OrderExtensionRequestInfoUiModel) {
        _mediator.getBottomSheetContainer()?.let { container ->
            somBottomSheetOrderExtensionRequest?.run {
                setOrderId(_mediator.getSomOrderExtensionOrderId())
                setData(data)
                setOnDismiss { _listener.onSellerOrderExtensionRequestDismissed() }
                init(container)
            }
        }
    }

    private fun orderExtensionRequestInfoObserver(data: OrderExtensionRequestInfoUiModel) {
        if (data.success && data.completed) {
            _listener.onSuccessRequestOrderExtension(data.message)
        } else if (!data.success) {
            _listener.onFailedRequestOrderExtension(data.message, data.throwable)
        }
        showBottomSheet(data)
    }
}
