package com.tokopedia.sellerorder.partial_order_fulfillment.presentation.model

import androidx.annotation.DimenRes
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.orderextension.presentation.model.StringRes

sealed interface PofFooterUiState {

    val priceEstimationData: PriceEstimationData
    val buttonData: SendPofButtonData

    data class Loading(
        override val priceEstimationData: PriceEstimationData = PriceEstimationData(),
        override val buttonData: SendPofButtonData = SendPofButtonData()
    ) : PofFooterUiState

    data class ReFetch(
        override val priceEstimationData: PriceEstimationData = PriceEstimationData(),
        override val buttonData: SendPofButtonData
    ) : PofFooterUiState

    data class ShowingPofRequestData(
        override val priceEstimationData: PriceEstimationData,
        override val buttonData: SendPofButtonData
    ) : PofFooterUiState

    data class ShowingPofResultData(
        override val priceEstimationData: PriceEstimationData,
        override val buttonData: SendPofButtonData = SendPofButtonData()
    ) : PofFooterUiState

    data class InitialLoadError(
        override val priceEstimationData: PriceEstimationData,
        override val buttonData: SendPofButtonData
    ) : PofFooterUiState

    data class ErrorReFetch(
        override val priceEstimationData: PriceEstimationData,
        override val buttonData: SendPofButtonData
    ) : PofFooterUiState

    data class PriceEstimationData(
        val title: StringRes = StringRes(Int.ZERO),
        val price: StringRes = StringRes(Int.ZERO),
        val showIcon: Boolean = false,
        val icon: Int = Int.ZERO,
        val iconEventData: UiEvent = UiEvent.None,
        @DimenRes val iconSize: Int = R.dimen.som_pof_footer_icon_size_chevron,
        val iconWrapped: Boolean = false
    )

    data class SendPofButtonData(
        val buttonText: StringRes = StringRes(Int.ZERO),
        val show: Boolean = false,
        val isButtonLoading: Boolean = false,
        val isButtonEnabled: Boolean = false,
        val eventData: UiEvent = UiEvent.None
    )
}
