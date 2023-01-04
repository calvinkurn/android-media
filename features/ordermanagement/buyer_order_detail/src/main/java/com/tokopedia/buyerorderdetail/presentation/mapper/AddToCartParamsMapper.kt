package com.tokopedia.buyerorderdetail.presentation.mapper

import com.tokopedia.atc_common.domain.model.request.AddToCartMultiParam
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailDataRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailResponse
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero

object AddToCartParamsMapper {
    fun mapMultiAddToCartParams(
        buyerOrderDetailDataRequestState: GetBuyerOrderDetailDataRequestState,
        shopId: String,
        userId: String
    ): ArrayList<AddToCartMultiParam> {
        return if (buyerOrderDetailDataRequestState is GetBuyerOrderDetailDataRequestState.Complete) {
            buyerOrderDetailDataRequestState
                .getP0DataRequestState
                .getBuyerOrderDetailRequestState
                .let { getBuyerOrderDetailRequestState ->
                    if (getBuyerOrderDetailRequestState is GetBuyerOrderDetailRequestState.Complete.Success) {
                        ArrayList(
                            getBuyerOrderDetailRequestState.result.details?.nonBundles?.map { nonBundle ->
                                nonBundle.mapToAddToCartParam(shopId, userId)
                            }.orEmpty().plus(
                                getBuyerOrderDetailRequestState.result.details?.bundles?.map { bundle ->
                                    bundle.orderDetail.map { it.mapToAddToCartParam(shopId, userId) }
                                }.orEmpty().flatten()
                            )
                        )
                    } else {
                        arrayListOf()
                    }
                }
        } else {
            arrayListOf()
        }
    }

    private fun GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Details.NonBundle.mapToAddToCartParam(
        shopId: String,
        userId: String
    ): AddToCartMultiParam {
        return AddToCartMultiParam(
            productId = productId.toLongOrZero(),
            productName = productName,
            productPrice = price.toLong(),
            qty = quantity,
            notes = notes,
            shopId = shopId.toIntOrZero(),
            custId = userId.toIntOrZero()
        )
    }

    private fun GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Details.Bundle.OrderDetail.mapToAddToCartParam(
        shopId: String,
        userId: String
    ): AddToCartMultiParam {
        return AddToCartMultiParam(
            productId = productId.toLongOrZero(),
            productName = productName,
            productPrice = price.toLong(),
            qty = quantity,
            notes = notes,
            shopId = shopId.toIntOrZero(),
            custId = userId.toIntOrZero()
        )
    }
}
