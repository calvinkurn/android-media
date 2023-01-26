package com.tokopedia.buyerorderdetail.presentation.mapper

import com.tokopedia.atc_common.domain.model.request.AddToCartMultiParam
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailDataRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailResponse
import com.tokopedia.buyerorderdetail.presentation.model.ProductListUiModel

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

    fun mapSingleAddToCartParams(
        product: ProductListUiModel.ProductUiModel,
        shopId: String,
        userId: String
    ): ArrayList<AddToCartMultiParam> {
        return arrayListOf(
            createAddToCartMultiParam(
                productId = product.productId,
                productName = product.productName,
                productPrice = product.price,
                quantity = product.quantity,
                notes = product.productNote,
                shopId = shopId,
                userId = userId
            )
        )
    }

    private fun GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Details.NonBundle.mapToAddToCartParam(
        shopId: String,
        userId: String
    ): AddToCartMultiParam {
        return createAddToCartMultiParam(
            productId = productId,
            productName = productName,
            productPrice = price,
            quantity = quantity,
            notes = notes,
            shopId = shopId,
            userId = userId
        )
    }

    private fun GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Details.Bundle.OrderDetail.mapToAddToCartParam(
        shopId: String,
        userId: String
    ): AddToCartMultiParam {
        return createAddToCartMultiParam(
            productId = productId,
            productName = productName,
            productPrice = price,
            quantity = quantity,
            notes = notes,
            shopId = shopId,
            userId = userId
        )
    }

    private fun createAddToCartMultiParam(
        productId: String,
        productName: String,
        productPrice: Double,
        quantity: Int,
        notes: String,
        shopId: String,
        userId: String
    ): AddToCartMultiParam {
        return AddToCartMultiParam(
            productId = productId,
            productName = productName,
            productPrice = productPrice,
            qty = quantity,
            notes = notes,
            shopId = shopId,
            custId = userId,
            warehouseId = "0"
        )
    }
}
