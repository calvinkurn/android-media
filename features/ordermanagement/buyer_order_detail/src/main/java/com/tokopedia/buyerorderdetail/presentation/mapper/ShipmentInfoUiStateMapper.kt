package com.tokopedia.buyerorderdetail.presentation.mapper

import androidx.annotation.StringRes
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailMiscConstant
import com.tokopedia.buyerorderdetail.common.utils.ResourceProvider
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailResponse
import com.tokopedia.buyerorderdetail.domain.models.GetP0DataRequestState
import com.tokopedia.buyerorderdetail.presentation.model.PlainHeaderUiModel
import com.tokopedia.buyerorderdetail.presentation.model.ShipmentInfoUiModel
import com.tokopedia.buyerorderdetail.presentation.model.SimpleCopyableKeyValueUiModel
import com.tokopedia.buyerorderdetail.presentation.model.TickerUiModel
import com.tokopedia.buyerorderdetail.presentation.uistate.ShipmentInfoUiState

object ShipmentInfoUiStateMapper {

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
                shipment.shippingRefNum,
                orderStatusId,
                orderId,
                resourceProvider
            ),
            courierDriverInfoUiModel = mapCourierDriverInfoUiModel(shipment.driver),
            driverTippingInfoUiModel = mapDriverTippingInfoUiModel(
                driverTippingInfo,
                resourceProvider
            ),
            courierInfoUiModel = mapCourierInfoUiModel(shipment, meta),
            dropShipperInfoUiModel = mapDropShipperInfoUiModel(dropship, resourceProvider),
            headerUiModel = mapPlainHeader(resourceProvider.getShipmentInfoSectionHeader()),
            receiverAddressInfoUiModel = mapReceiverAddressInfoUiModel(
                shipment.receiver,
                resourceProvider
            ),
            ticker = mapTicker(
                shipment.shippingInfo,
                BuyerOrderDetailMiscConstant.TICKER_KEY_SHIPPING_INFO
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

    private fun mapCourierDriverInfoUiModel(driver: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Shipment.Driver): ShipmentInfoUiModel.CourierDriverInfoUiModel {
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

    private fun mapPlainHeader(@StringRes headerStringResId: Int): PlainHeaderUiModel {
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

    private fun mapStringRes(@StringRes resId: Int): com.tokopedia.buyerorderdetail.presentation.model.StringRes {
        return com.tokopedia.buyerorderdetail.presentation.model.StringRes(resId)
    }

    fun mapGetP0DataRequestStateToShipmentInfoUiState(
        getP0DataRequestState: GetP0DataRequestState,
        resourceProvider: ResourceProvider
    ): ShipmentInfoUiState {
        return when (getP0DataRequestState) {
            is GetP0DataRequestState.Requesting -> {
                when (val getBuyerOrderDetailRequestState = getP0DataRequestState.getBuyerOrderDetailRequestState) {
                    is GetBuyerOrderDetailRequestState.Requesting -> {
                        ShipmentInfoUiState.Loading
                    }
                    is GetBuyerOrderDetailRequestState.Success -> {
                        ShipmentInfoUiState.Showing(
                            mapShipmentInfoUiModel(
                                getBuyerOrderDetailRequestState.result.shipment,
                                getBuyerOrderDetailRequestState.result.meta,
                                getBuyerOrderDetailRequestState.result.orderId,
                                getBuyerOrderDetailRequestState.result.orderStatus.id,
                                getBuyerOrderDetailRequestState.result.dropship,
                                getBuyerOrderDetailRequestState.result.getDriverTippingInfo(),
                                resourceProvider
                            )
                        )
                    }
                    is GetBuyerOrderDetailRequestState.Error -> {
                        ShipmentInfoUiState.Error(getBuyerOrderDetailRequestState.throwable)
                    }
                }
            }
            is GetP0DataRequestState.Success -> {
                val getBuyerOrderDetailRequestState = getP0DataRequestState.getBuyerOrderDetailRequestState
                ShipmentInfoUiState.Showing(
                    mapShipmentInfoUiModel(
                        getBuyerOrderDetailRequestState.result.shipment,
                        getBuyerOrderDetailRequestState.result.meta,
                        getBuyerOrderDetailRequestState.result.orderId,
                        getBuyerOrderDetailRequestState.result.orderStatus.id,
                        getBuyerOrderDetailRequestState.result.dropship,
                        getBuyerOrderDetailRequestState.result.getDriverTippingInfo(),
                        resourceProvider
                    )
                )
            }
            is GetP0DataRequestState.Error -> {
                ShipmentInfoUiState.Error(getP0DataRequestState.getThrowable())
            }
            else -> {
                ShipmentInfoUiState.Loading
            }
        }
    }
}
