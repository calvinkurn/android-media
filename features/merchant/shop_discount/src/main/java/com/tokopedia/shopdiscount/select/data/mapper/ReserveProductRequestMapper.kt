package com.tokopedia.shopdiscount.select.data.mapper

import com.tokopedia.shopdiscount.common.data.request.DoSlashPriceReservationRequest
import com.tokopedia.shopdiscount.common.data.request.RequestHeader
import javax.inject.Inject

class ReserveProductRequestMapper @Inject constructor() {

    fun map(requestId: String, products: List<String>): DoSlashPriceReservationRequest {
        return DoSlashPriceReservationRequest(
            requestHeader = RequestHeader(),
            action = DoSlashPriceReservationRequest.DoSlashPriceReservationAction.RESERVE,
            requestId = requestId,
            state = DoSlashPriceReservationRequest.DoSlashPriceReservationState.CREATE.toString(),
            listProductData = products.mapIndexed { position, productId ->
                DoSlashPriceReservationRequest.SlashPriceReservationProduct(
                    productId,
                    position.toString()
                )
            }
        )
    }

}