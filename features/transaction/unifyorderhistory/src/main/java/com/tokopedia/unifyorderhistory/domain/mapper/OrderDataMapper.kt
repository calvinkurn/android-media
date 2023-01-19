package com.tokopedia.unifyorderhistory.domain.mapper

import com.google.gson.JsonArray
import com.google.gson.JsonParser
import com.tokopedia.atc_common.data.model.request.AddToCartOccMultiCartParam
import com.tokopedia.atc_common.data.model.request.AddToCartOccMultiRequestParams
import com.tokopedia.unifyorderhistory.data.model.UohListOrder
import com.tokopedia.unifyorderhistory.util.UohConsts

object OrderDataMapper {

    fun mapOrderDataToOccParams(orderData: UohListOrder.UohOrders.Order): AddToCartOccMultiRequestParams {
        val listAtcOccMulti = arrayListOf<AddToCartOccMultiCartParam>()
        try {
            val jsonArray: JsonArray =
                JsonParser.parseString(orderData.metadata.listProducts).asJsonArray
            for (x in 0 until jsonArray.size()) {
                val objParam = jsonArray.get(x).asJsonObject
                listAtcOccMulti.add(
                    AddToCartOccMultiCartParam(
                        productId = objParam.get(UohConsts.PRODUCT_ID).asString,
                        shopId = objParam.get(UohConsts.SHOP_ID).asString,
                        quantity = objParam.get(UohConsts.QUANTITY).asString,
                        notes = objParam.get(UohConsts.NOTES).asString,
                        warehouseId = objParam.get(UohConsts.WAREHOUSE_ID).asString,

                        productName = objParam.get(UohConsts.PRODUCT_NAME).asString,
                        price = objParam.get(UohConsts.PRODUCT_PRICE).asString
                    )
                )
            }
        } catch (e: Exception) {
            // no op
        }
        return AddToCartOccMultiRequestParams(
            carts = listAtcOccMulti,
            userId = orderData.userID
        )
    }
}
