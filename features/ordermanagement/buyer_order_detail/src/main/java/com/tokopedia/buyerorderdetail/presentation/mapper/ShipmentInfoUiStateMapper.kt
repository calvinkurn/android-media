package com.tokopedia.buyerorderdetail.presentation.mapper

import android.net.Uri
import androidx.annotation.StringRes
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailMiscConstant
import com.tokopedia.buyerorderdetail.common.utils.ResourceProvider
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailDataRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailResponse
import com.tokopedia.buyerorderdetail.presentation.model.OwocBomDetailSectionUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PlainHeaderUiModel
import com.tokopedia.buyerorderdetail.presentation.model.ShipmentInfoUiModel
import com.tokopedia.buyerorderdetail.presentation.model.SimpleCopyableKeyValueUiModel
import com.tokopedia.buyerorderdetail.presentation.model.TickerUiModel
import com.tokopedia.buyerorderdetail.presentation.uistate.ShipmentInfoUiState
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.logisticCommon.util.LogisticImageDeliveryHelper
import com.tokopedia.logisticCommon.util.LogisticImageDeliveryHelper.DEFAULT_OS_TYPE
import com.tokopedia.logisticCommon.util.LogisticImageDeliveryHelper.IMAGE_SMALL_SIZE
import com.tokopedia.user.session.UserSessionInterface

object ShipmentInfoUiStateMapper {

    private const val QUERY_PARAM_POD_IMAGE_ID = "image_id"

    fun map(
        getBuyerOrderDetailDataRequestState: GetBuyerOrderDetailDataRequestState,
        currentState: ShipmentInfoUiState,
        resourceProvider: ResourceProvider,
        userSession: UserSessionInterface
    ): ShipmentInfoUiState {
        val getBuyerOrderDetailRequestState = getBuyerOrderDetailDataRequestState
            .getP0DataRequestState
            .getBuyerOrderDetailRequestState
        return when (getBuyerOrderDetailRequestState) {
            is GetBuyerOrderDetailRequestState.Requesting -> {
                mapOnGetBuyerOrderDetailRequesting(currentState)
            }
            is GetBuyerOrderDetailRequestState.Complete.Error -> {
                mapOnGetBuyerOrderDetailError(getBuyerOrderDetailRequestState.throwable)
            }
            is GetBuyerOrderDetailRequestState.Complete.Success -> {
                mapOnGetBuyerOrderDetailSuccess(
                    getBuyerOrderDetailRequestState.result,
                    resourceProvider,
                    userSession
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
        buyerOrderDetailData: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail,
        resourceProvider: ResourceProvider,
        userSession: UserSessionInterface
    ): ShipmentInfoUiState {
        return mapOnDataReady(buyerOrderDetailData, resourceProvider, userSession)
    }

    private fun mapOnGetBuyerOrderDetailError(
        throwable: Throwable?
    ): ShipmentInfoUiState {
        return mapOnError(throwable)
    }

    private fun mapOnLoading(): ShipmentInfoUiState {
        return ShipmentInfoUiState.Loading
    }

    private fun mapOnReloading(
        currentState: ShipmentInfoUiState.HasData
    ): ShipmentInfoUiState {
        return ShipmentInfoUiState.HasData.Reloading(currentState.data)
    }

    private fun mapOnDataReady(
        buyerOrderDetailData: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail,
        resourceProvider: ResourceProvider,
        userSession: UserSessionInterface
    ): ShipmentInfoUiState {
        return ShipmentInfoUiState.HasData.Showing(
            mapShipmentInfoUiModel(
                buyerOrderDetailData.additionalData.groupOrderData,
                buyerOrderDetailData.groupType,
                buyerOrderDetailData.shipment,
                buyerOrderDetailData.meta,
                buyerOrderDetailData.orderId,
                buyerOrderDetailData.orderStatus.id,
                buyerOrderDetailData.dropship,
                buyerOrderDetailData.getDriverTippingInfo(),
                buyerOrderDetailData.getPodInfo(),
                resourceProvider,
                userSession
            )
        )
    }

    private fun mapOnError(
        throwable: Throwable?
    ): ShipmentInfoUiState {
        return ShipmentInfoUiState.Error(throwable)
    }

    private fun mapShipmentInfoUiModel(
        owocSection: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.BomAdditionalData.GroupOrderData?,
        groupType: String,
        shipment: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Shipment,
        meta: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Meta,
        orderId: String,
        orderStatusId: String,
        dropship: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Dropship,
        driverTippingInfo: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.LogisticSectionInfo?,
        podInfo: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.LogisticSectionInfo?,
        resourceProvider: ResourceProvider,
        userSession: UserSessionInterface
    ): ShipmentInfoUiModel {
        return ShipmentInfoUiModel(
            owocInfoUiModel = mapOwocBomDetailSectionUiModel(
                owocSection,
                groupType
            ),
            awbInfoUiModel = mapAwbInfoUiModel(
                shipment.shippingRefNum,
                orderStatusId,
                orderId,
                resourceProvider
            ),
            courierDriverInfoUiModel = mapCourierDriverInfoUiModel(shipment),
            driverTippingInfoUiModel = mapDriverTippingInfoUiModel(
                driverTippingInfo,
                resourceProvider
            ),
            courierInfoUiModel = mapCourierInfoUiModel(shipment, meta, podInfo, userSession, orderId),
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

    private fun mapOwocBomDetailSectionUiModel(
        owocSection: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.BomAdditionalData.GroupOrderData?,
        groupType: String
    ): OwocBomDetailSectionUiModel? {
        return owocSection?.let {
            OwocBomDetailSectionUiModel(
                txId = owocSection.txId,
                groupType = groupType,
                sectionTitle = owocSection.title,
                sectionDesc = owocSection.description,
                imageUrl = owocSection.iconUrl
            )
        }
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
            label = mapStringRes(resourceProvider.getAwbLabel())
        )
    }

    private fun mapCourierDriverInfoUiModel(
        shipment: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Shipment
    ): ShipmentInfoUiModel.CourierDriverInfoUiModel {
        val driver = shipment.driver
        val buttonList = mapDriverButtonUiModel(shipment.buttons)
        return ShipmentInfoUiModel.CourierDriverInfoUiModel(
            name = driver.name,
            phoneNumber = driver.phone,
            photoUrl = driver.photoUrl,
            plateNumber = driver.licenseNumber,
            buttonList = buttonList
        )
    }

    private fun mapDriverButtonUiModel(
        buttons: List<GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Shipment.Button>
    ): List<ShipmentInfoUiModel.CourierDriverInfoUiModel.Button> {
        return buttons.map { button ->
            ShipmentInfoUiModel.CourierDriverInfoUiModel.Button(
                key = button.key,
                icon = button.icon,
                actionValue = button.actionType,
                value = button.value
            )
        }
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
        meta: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Meta,
        podInfo: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.LogisticSectionInfo?,
        userSession: UserSessionInterface,
        orderId: String
    ): ShipmentInfoUiModel.CourierInfoUiModel {
        return ShipmentInfoUiModel.CourierInfoUiModel(
            arrivalEstimation = composeETA(shipment.eta),
            courierNameAndProductName = shipment.shippingDisplayName,
            isFreeShipping = meta.isBebasOngkir,
            boBadgeUrl = meta.boImageUrl,
            etaChanged = shipment.etaIsUpdated,
            etaUserInfo = shipment.userUpdatedInfo,
            pod = mapPod(podInfo, orderId, userSession)
        )
    }

    private fun mapPod(
        podInfo: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.LogisticSectionInfo?,
        orderId: String,
        userSession: UserSessionInterface
    ): ShipmentInfoUiModel.CourierInfoUiModel.Pod? {
        return runCatching {
            podInfo?.let {
                Uri.parse(it.imageUrl).getQueryParameter(QUERY_PARAM_POD_IMAGE_ID)?.let { imageId ->
                    val completeImageUrl = LogisticImageDeliveryHelper.getDeliveryImage(
                        imageId,
                        orderId.toLongOrZero(),
                        IMAGE_SMALL_SIZE,
                        userSession.userId,
                        DEFAULT_OS_TYPE,
                        userSession.deviceId
                    )
                    ShipmentInfoUiModel.CourierInfoUiModel.Pod(
                        podPictureUrl = completeImageUrl,
                        podLabel = it.title,
                        podCtaText = it.action.name,
                        podCtaUrl = it.action.link,
                        accessToken = userSession.accessToken
                    )
                }
            }?.takeIf {
                it.podPictureUrl.isNotBlank() &&
                    it.podLabel.isNotBlank() &&
                    it.podCtaText.isNotBlank() &&
                    it.podCtaUrl.isNotBlank() &&
                    it.accessToken.isNotBlank()
            }
        }.getOrNull()
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
