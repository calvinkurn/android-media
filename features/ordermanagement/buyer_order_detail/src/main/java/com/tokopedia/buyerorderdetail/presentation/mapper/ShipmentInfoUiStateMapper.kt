package com.tokopedia.buyerorderdetail.presentation.mapper

import androidx.annotation.StringRes
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailMiscConstant
import com.tokopedia.buyerorderdetail.common.utils.ResourceProvider
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailDataRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailResponse
import com.tokopedia.buyerorderdetail.domain.models.GetOrderResolutionRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetP1DataRequestState
import com.tokopedia.buyerorderdetail.presentation.model.PlainHeaderUiModel
import com.tokopedia.buyerorderdetail.presentation.model.ShipmentInfoUiModel
import com.tokopedia.buyerorderdetail.presentation.model.SimpleCopyableKeyValueUiModel
import com.tokopedia.buyerorderdetail.presentation.model.TickerUiModel
import com.tokopedia.buyerorderdetail.presentation.uistate.ShipmentInfoUiState

object ShipmentInfoUiStateMapper {

    fun map(
        getBuyerOrderDetailDataRequestState: GetBuyerOrderDetailDataRequestState,
        currentState: ShipmentInfoUiState,
        resourceProvider: ResourceProvider
    ): ShipmentInfoUiState {
        val p1DataRequestState = getBuyerOrderDetailDataRequestState.getP1DataRequestState
        val getBuyerOrderDetailRequestState = getBuyerOrderDetailDataRequestState
            .getP0DataRequestState
            .getBuyerOrderDetailRequestState
        return when (getBuyerOrderDetailRequestState) {
            is GetBuyerOrderDetailRequestState.Requesting -> {
                mapOnGetBuyerOrderDetailRequesting(currentState)
            }
            is GetBuyerOrderDetailRequestState.Complete.Error -> {
                mapOnGetBuyerOrderDetailError(
                    getBuyerOrderDetailRequestState,
                    p1DataRequestState,
                    currentState
                )
            }
            is GetBuyerOrderDetailRequestState.Complete.Success -> {
                mapOnGetBuyerOrderDetailSuccess(
                    getBuyerOrderDetailRequestState,
                    p1DataRequestState,
                    currentState,
                    resourceProvider
                )
            }
        }
    }

    private fun mapOnGetBuyerOrderDetailRequesting(
        currentState: ShipmentInfoUiState
    ): ShipmentInfoUiState {
        return if (currentState is ShipmentInfoUiState.HasData) {
            mapOnReloading(currentState)
        } else {
            mapOnLoading()
        }
    }

    private fun mapOnGetBuyerOrderDetailSuccess(
        buyerOrderDetailRequestState: GetBuyerOrderDetailRequestState.Complete.Success,
        p1DataRequestState: GetP1DataRequestState,
        currentState: ShipmentInfoUiState,
        resourceProvider: ResourceProvider
    ): ShipmentInfoUiState {
        return when (p1DataRequestState) {
            is GetP1DataRequestState.Requesting -> {
                mapOnP1Requesting(
                    buyerOrderDetailRequestState, p1DataRequestState, currentState, resourceProvider
                )
            }
            is GetP1DataRequestState.Complete -> {
                mapOnP1Complete(buyerOrderDetailRequestState, resourceProvider)
            }
        }
    }

    private fun mapOnGetBuyerOrderDetailError(
        buyerOrderDetailRequestState: GetBuyerOrderDetailRequestState.Complete.Error,
        p1DataRequestState: GetP1DataRequestState,
        currentState: ShipmentInfoUiState,
    ): ShipmentInfoUiState {
        return when (p1DataRequestState) {
            is GetP1DataRequestState.Requesting -> {
                mapOnP1Requesting(
                    buyerOrderDetailRequestState,
                    p1DataRequestState,
                    currentState
                )
            }
            is GetP1DataRequestState.Complete -> {
                mapOnError(buyerOrderDetailRequestState.throwable)
            }
        }
    }

    private fun mapOnP1Requesting(
        buyerOrderDetailRequestState: GetBuyerOrderDetailRequestState.Complete.Success,
        p1DataRequestState: GetP1DataRequestState.Requesting,
        currentState: ShipmentInfoUiState,
        resourceProvider: ResourceProvider
    ): ShipmentInfoUiState {
        return when (p1DataRequestState.getOrderResolutionRequestState) {
            is GetOrderResolutionRequestState.Requesting -> {
                mapOnOrderResolutionRequesting(
                    currentState,
                    buyerOrderDetailRequestState,
                    resourceProvider
                )
            }
            else -> {
                mapOnOrderResolutionComplete(buyerOrderDetailRequestState, resourceProvider)
            }
        }
    }

    private fun mapOnP1Requesting(
        buyerOrderDetailRequestState: GetBuyerOrderDetailRequestState.Complete.Error,
        p1DataRequestState: GetP1DataRequestState.Requesting,
        currentState: ShipmentInfoUiState
    ): ShipmentInfoUiState {
        return when (p1DataRequestState.getOrderResolutionRequestState) {
            is GetOrderResolutionRequestState.Requesting -> {
                mapOnOrderResolutionRequesting(
                    currentState
                )
            }
            else -> {
                mapOnOrderResolutionComplete(buyerOrderDetailRequestState)
            }
        }
    }

    private fun mapOnP1Complete(
        buyerOrderDetailRequestState: GetBuyerOrderDetailRequestState.Complete.Success,
        resourceProvider: ResourceProvider
    ): ShipmentInfoUiState {
        return mapOnDataReady(buyerOrderDetailRequestState.result, resourceProvider)
    }

    private fun mapOnOrderResolutionRequesting(
        currentState: ShipmentInfoUiState,
        buyerOrderDetailRequestState: GetBuyerOrderDetailRequestState.Complete.Success,
        resourceProvider: ResourceProvider
    ): ShipmentInfoUiState {
        return if (currentState is ShipmentInfoUiState.HasData) {
            mapOnReloading(buyerOrderDetailRequestState.result, resourceProvider)
        } else {
            mapOnLoading()
        }
    }

    private fun mapOnOrderResolutionRequesting(
        currentState: ShipmentInfoUiState
    ): ShipmentInfoUiState {
        return if (currentState is ShipmentInfoUiState.HasData) {
            mapOnReloading(currentState)
        } else {
            mapOnLoading()
        }
    }

    private fun mapOnOrderResolutionComplete(
        buyerOrderDetailRequestState: GetBuyerOrderDetailRequestState.Complete.Success,
        resourceProvider: ResourceProvider
    ): ShipmentInfoUiState {
        return mapOnDataReady(buyerOrderDetailRequestState.result, resourceProvider)
    }

    private fun mapOnOrderResolutionComplete(
        buyerOrderDetailRequestState: GetBuyerOrderDetailRequestState.Complete.Error
    ): ShipmentInfoUiState {
        return mapOnError(buyerOrderDetailRequestState.throwable)
    }

    private fun mapOnLoading(): ShipmentInfoUiState {
        return ShipmentInfoUiState.Loading
    }

    private fun mapOnReloading(
        currentState: ShipmentInfoUiState.HasData
    ): ShipmentInfoUiState {
        return ShipmentInfoUiState.HasData.Reloading(currentState.data)
    }

    private fun mapOnReloading(
        buyerOrderDetailData: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail,
        resourceProvider: ResourceProvider
    ): ShipmentInfoUiState {
        return ShipmentInfoUiState.HasData.Reloading(
            mapShipmentInfoUiModel(
                buyerOrderDetailData.shipment,
                buyerOrderDetailData.meta,
                buyerOrderDetailData.orderId,
                buyerOrderDetailData.orderStatus.id,
                buyerOrderDetailData.dropship,
                buyerOrderDetailData.getDriverTippingInfo(),
                resourceProvider
            )
        )
    }

    private fun mapOnDataReady(
        buyerOrderDetailData: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail,
        resourceProvider: ResourceProvider
    ): ShipmentInfoUiState {
        return ShipmentInfoUiState.HasData.Showing(
            mapShipmentInfoUiModel(
                buyerOrderDetailData.shipment,
                buyerOrderDetailData.meta,
                buyerOrderDetailData.orderId,
                buyerOrderDetailData.orderStatus.id,
                buyerOrderDetailData.dropship,
                buyerOrderDetailData.getDriverTippingInfo(),
                resourceProvider
            )
        )
    }

    private fun mapOnError(
        throwable: Throwable?
    ): ShipmentInfoUiState {
        return ShipmentInfoUiState.Error(throwable)
    }

    private fun mapShipmentInfoUiModel(
        shipment: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Shipment,
        meta: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Meta,
        orderId: String,
        orderStatusId: String,
        dropship: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Dropship,
        driverTippingInfo: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.LogisticSectionInfo?,
        resourceProvider: ResourceProvider
    ): ShipmentInfoUiModel {
        return ShipmentInfoUiModel(
            awbInfoUiModel = mapAwbInfoUiModel(
                shipment.shippingRefNum, orderStatusId, orderId, resourceProvider
            ),
            courierDriverInfoUiModel = mapCourierDriverInfoUiModel(shipment.driver),
            driverTippingInfoUiModel = mapDriverTippingInfoUiModel(
                driverTippingInfo, resourceProvider
            ),
            courierInfoUiModel = mapCourierInfoUiModel(shipment, meta),
            dropShipperInfoUiModel = mapDropShipperInfoUiModel(dropship, resourceProvider),
            headerUiModel = mapPlainHeader(resourceProvider.getShipmentInfoSectionHeader()),
            receiverAddressInfoUiModel = mapReceiverAddressInfoUiModel(
                shipment.receiver, resourceProvider
            ),
            ticker = mapTicker(
                shipment.shippingInfo, BuyerOrderDetailMiscConstant.TICKER_KEY_SHIPPING_INFO
            )
        )
    }

    private fun mapAwbInfoUiModel(
        shippingRefNum: String,
        orderStatusId: String,
        orderId: String,
        resourceProvider: ResourceProvider
    ): ShipmentInfoUiModel.AwbInfoUiModel {
        return ShipmentInfoUiModel.AwbInfoUiModel(
            orderId = orderId,
            orderStatusId = orderStatusId,
            copyableText = shippingRefNum,
            copyLabel = mapStringRes(resourceProvider.getCopyLabelAwb()),
            copyMessage = mapStringRes(resourceProvider.getCopyMessageAwb()),
            label = mapStringRes(resourceProvider.getAwbLabel()),
        )
    }

    private fun mapCourierDriverInfoUiModel(
        driver: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Shipment.Driver
    ): ShipmentInfoUiModel.CourierDriverInfoUiModel {
        return ShipmentInfoUiModel.CourierDriverInfoUiModel(
            name = driver.name,
            phoneNumber = driver.phone,
            photoUrl = driver.photoUrl,
            plateNumber = driver.licenseNumber
        )
    }

    private fun mapDriverTippingInfoUiModel(
        driverTippingInfo: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.LogisticSectionInfo?,
        resourceProvider: ResourceProvider
    ): ShipmentInfoUiModel.DriverTippingInfoUiModel {
        return ShipmentInfoUiModel.DriverTippingInfoUiModel(
            imageUrl = driverTippingInfo?.imageUrl.orEmpty(),
            title = driverTippingInfo?.title.orEmpty(),
            description = resourceProvider.composeDriverTippingInfoDescription(driverTippingInfo)
        )
    }

    private fun mapCourierInfoUiModel(
        shipment: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Shipment,
        meta: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Meta
    ): ShipmentInfoUiModel.CourierInfoUiModel {
        return ShipmentInfoUiModel.CourierInfoUiModel(
            arrivalEstimation = composeETA(shipment.eta),
            courierNameAndProductName = shipment.shippingDisplayName,
            isFreeShipping = meta.isBebasOngkir,
            boBadgeUrl = meta.boImageUrl,
            etaChanged = shipment.etaIsUpdated,
            etaUserInfo = shipment.userUpdatedInfo
        )
    }

    private fun mapDropShipperInfoUiModel(
        dropship: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Dropship,
        resourceProvider: ResourceProvider
    ): SimpleCopyableKeyValueUiModel {
        return SimpleCopyableKeyValueUiModel(
            copyableText = formatDropshipperValue(dropship, resourceProvider),
            copyLabel = mapStringRes(resourceProvider.getCopyLabelDropshipper()),
            copyMessage = mapStringRes(resourceProvider.getCopyMessageDropshipper()),
            label = mapStringRes(resourceProvider.getDropshipperLabel())
        )
    }

    private fun mapPlainHeader(
        @StringRes headerStringResId: Int
    ): PlainHeaderUiModel {
        return PlainHeaderUiModel(
            header = mapStringRes(headerStringResId)
        )
    }

    private fun mapReceiverAddressInfoUiModel(
        receiver: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Shipment.Receiver,
        resourceProvider: ResourceProvider
    ): SimpleCopyableKeyValueUiModel {
        return SimpleCopyableKeyValueUiModel(
            copyableText = formatReceiverAddressValue(receiver, resourceProvider),
            copyLabel = mapStringRes(resourceProvider.getCopyLabelReceiverAddress()),
            copyMessage = mapStringRes(resourceProvider.getCopyMessageReceiverAddress()),
            label = mapStringRes(resourceProvider.getReceiverAddressLabel())
        )
    }

    private fun mapTicker(
        tickerInfo: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.TickerInfo,
        actionKey: String? = null
    ): TickerUiModel {
        return TickerUiModel(
            actionKey = actionKey ?: tickerInfo.actionKey,
            actionText = tickerInfo.actionText,
            actionUrl = tickerInfo.actionUrl,
            description = tickerInfo.text,
            type = tickerInfo.type
        )
    }

    private fun composeETA(eta: String): String {
        return if (eta.isBlank() || eta.startsWith("(") || eta.endsWith(")")) eta else "($eta)"
    }

    private fun formatDropshipperValue(
        dropship: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Dropship,
        resourceProvider: ResourceProvider
    ): String {
        return resourceProvider.composeDropshipperValue(dropship)
    }

    private fun formatReceiverAddressValue(
        receiver: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Shipment.Receiver,
        resourceProvider: ResourceProvider
    ): String {
        return resourceProvider.composeReceiverAddressValue(receiver)
    }

    private fun mapStringRes(
        @StringRes resId: Int
    ): com.tokopedia.buyerorderdetail.presentation.model.StringRes {
        return com.tokopedia.buyerorderdetail.presentation.model.StringRes(resId)
    }
}
