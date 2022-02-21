package com.tokopedia.dg_transaction.testing.response.rest

import com.google.gson.reflect.TypeToken
import com.tokopedia.common_digital.atc.data.response.AttributesCart
import com.tokopedia.common_digital.atc.data.response.RelationshipsCart
import com.tokopedia.common_digital.atc.data.response.ResponseCartData
import com.tokopedia.network.data.model.response.DataResponse
import java.lang.reflect.Type

class DigitalAddToCartMockResponse: MockRestResponse<ResponseCartData>() {
    override fun getToken(): Type =
        object : TypeToken<DataResponse<ResponseCartData>>() {}.type

    override fun getResponse(): ResponseCartData {
        return ResponseCartData(
            type = "cart",
            id = "17211378-34-cc6fc5c050a0ffd85b1ad1a8271b9bf3",
            attributes = AttributesCart(
                categoryName = "Uang Elektronik",
                clientNumber = "6032984011276619",
                icon = "https://ecs7.tokopedia.net/img/cache/100-square/attachment/2019/11/15/21181130/21181130_5ee75889-94bc-45d3-9ce6-5ecf466fb385.png",
                isCouponActive = 1,
                crossSellingType = 0,
                operatorName = "Mandiri E-Money",
                price = "Rp22.500",
                pricePlain = 22500.0,
                smsState = "",
                userId = "17211378"
            ),
            relationships = RelationshipsCart(
                category = RelationshipsCart.Category(
                    data = RelationshipsCart.RelationData(type = "category", id = "34")
                ),
                operator = RelationshipsCart.Operator(
                    data = RelationshipsCart.RelationData(type = "operator", id = "578")
                ),
                product = RelationshipsCart.Product(
                    data = RelationshipsCart.RelationData(type = "product", id = "2069")
                )
            )
        )
    }
}