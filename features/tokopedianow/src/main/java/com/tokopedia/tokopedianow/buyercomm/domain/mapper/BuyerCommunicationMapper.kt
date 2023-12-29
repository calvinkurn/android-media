package com.tokopedia.tokopedianow.buyercomm.domain.mapper

import com.tokopedia.tokopedianow.buyercomm.domain.model.GetBuyerCommunication
import com.tokopedia.tokopedianow.buyercomm.domain.model.GetBuyerCommunication.GetBuyerCommunicationResponse
import com.tokopedia.tokopedianow.buyercomm.presentation.data.BuyerCommunicationData
import com.tokopedia.tokopedianow.buyercomm.presentation.data.ShipmentOptionData

object BuyerCommunicationMapper {

    fun mapToBuyerCommunicationData(
        response: GetBuyerCommunicationResponse
    ): BuyerCommunicationData {
        val shippingDetails = response.data.shippingDetails
        val locationDetails = response.data.locationDetails

        return BuyerCommunicationData(
            warehouseStatus = locationDetails.status,
            operationHour = locationDetails.operationHour,
            shipmentOptions = mapShipmentOptions(shippingDetails)
        )
    }

    private fun mapShipmentOptions(
        shippingDetails: GetBuyerCommunication.ShippingDetails
    ): List<ShipmentOptionData> {
        return shippingDetails.options.mapIndexed { index, options ->
            ShipmentOptionData(
                index = index,
                name = options.name,
                details = options.details.split("\n"),
                available = options.available
            )
        }
    }

}
