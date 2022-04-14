package com.tokopedia.shopdiscount.manage.data.mapper

import com.tokopedia.shopdiscount.common.data.request.DoSlashPriceReservationRequest
import com.tokopedia.shopdiscount.common.data.request.RequestHeader
import javax.inject.Inject

class UpdateDiscountRequestMapper @Inject constructor() {

    fun map(requestId: String, productIds: List<String>): DoSlashPriceReservationRequest {
        return DoSlashPriceReservationRequest(
            requestHeader = RequestHeader(),
            action = DoSlashPriceReservationRequest.DoSlashPriceReservationAction.RESERVE,
            requestId = requestId,
            state = DoSlashPriceReservationRequest.DoSlashPriceReservationState.EDIT,
            listProductData = productIds.mapIndexed { position, productId ->
                DoSlashPriceReservationRequest.SlashPriceReservationProduct(
                    productId,
                    position.toString()
                )
            }
        )
    }

}