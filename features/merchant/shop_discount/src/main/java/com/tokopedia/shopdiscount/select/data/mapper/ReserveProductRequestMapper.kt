package com.tokopedia.shopdiscount.select.data.mapper

import com.tokopedia.shopdiscount.common.data.request.DoSlashPriceReservationRequest
import com.tokopedia.shopdiscount.common.data.request.RequestHeader
import com.tokopedia.shopdiscount.select.domain.entity.ReservableProduct
import javax.inject.Inject

class ReserveProductRequestMapper @Inject constructor() {

    fun map(requestId: String, products: List<ReservableProduct>): DoSlashPriceReservationRequest {
        return DoSlashPriceReservationRequest(
            requestHeader = RequestHeader(),
            action = DoSlashPriceReservationRequest.DoSlashPriceReservationAction.RESERVE,
            requestId = requestId,
            state = DoSlashPriceReservationRequest.DoSlashPriceReservationState.CREATE,
            listProductData = products.mapIndexed { position, product ->
                DoSlashPriceReservationRequest.SlashPriceReservationProduct(
                    product.id,
                    position.toString()
                )
            }
        )
    }

}