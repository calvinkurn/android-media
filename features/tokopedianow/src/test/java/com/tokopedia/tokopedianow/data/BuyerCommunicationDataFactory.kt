package com.tokopedia.tokopedianow.data

import com.tokopedia.tokopedianow.buyercomm.domain.model.GetBuyerCommunication.Background
import com.tokopedia.tokopedianow.buyercomm.domain.model.GetBuyerCommunication.GetBuyerCommunicationData
import com.tokopedia.tokopedianow.buyercomm.domain.model.GetBuyerCommunication.GetBuyerCommunicationResponse
import com.tokopedia.tokopedianow.buyercomm.domain.model.GetBuyerCommunication.LocationDetails
import com.tokopedia.tokopedianow.buyercomm.domain.model.GetBuyerCommunication.Options
import com.tokopedia.tokopedianow.buyercomm.domain.model.GetBuyerCommunication.ShippingDetails
import com.tokopedia.tokopedianow.buyercomm.domain.model.GetBuyerCommunication.ShopDetails

object BuyerCommunicationDataFactory {

    fun createBuyerCommunicationResponse(
        logoURL: String = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/10/31/2ad7e0eb-3433-489a-8530-f503d4e41c94.png",
        title: String = "Tokopedia NOW!",
        status: String = "Buka",
        operationHour: String = "Senin - Minggu | 05:00 - 23:59 WIB",
        shippingHint: String = "Tiba besok, 07:00 - 09:00",
        shippingOptions: List<Options> = listOf(
            Options(
                name = "2 Jam Tiba",
                details = "Bebas pilih tanggal dan waktu\nGratis ongkir, min. belanja Rp75.000",
                available = true
            )
        ),
        backgroundColor: String = "#3681D2",
        animationURL: String = "https://assets.tokopedia.net/asts/tokonow/thematic/animation/Day.json",
        backgroundImageUrl: String = "https://images.tokopedia.net/img/tokonow/thematic/image/Cloud.png"
    ): GetBuyerCommunicationResponse {
        return GetBuyerCommunicationResponse(
            data = GetBuyerCommunicationData(
                shopDetails = ShopDetails(logoURL, title),
                locationDetails = LocationDetails(status, operationHour),
                shippingDetails = ShippingDetails(
                    hint = shippingHint,
                    options = shippingOptions
                ),
                background = Background(
                    color = backgroundColor,
                    animationURL = animationURL,
                    imageURL = backgroundImageUrl
                )
            )
        )
    }
}
