package com.tokopedia.common_digital.cart.data.datasource

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.common_digital.cart.data.entity.requestbody.atc.RequestBodyAtcDigital
import com.tokopedia.common_digital.cart.data.entity.response.ResponseCartData
import com.tokopedia.common_digital.cart.data.mapper.ICartMapperData
import com.tokopedia.common_digital.cart.view.model.cart.CartDigitalInfoData
import com.tokopedia.common_digital.common.data.api.DigitalRestApi
import com.tokopedia.common_digital.product.data.response.TkpdDigitalResponse

import retrofit2.Response
import rx.Observable
import rx.functions.Func1

/**
 * Created by Rizky on 24/08/18.
 */
class DigitalAddToCartDataSource(private val digitalRestApi: DigitalRestApi, private val cartMapperData: ICartMapperData) {

    fun addToCart(
            requestBodyAtcDigital: RequestBodyAtcDigital, idemPotencyKeyHeader: String
    ): Observable<CartDigitalInfoData> {
        val jsonElement = JsonParser().parse(Gson().toJson(requestBodyAtcDigital))
        val requestBody = JsonObject()
        requestBody.add("data", jsonElement)
        return digitalRestApi
                .addToCart(requestBody, idemPotencyKeyHeader)
                .map { dataResponseResponse ->
                    cartMapperData.transformCartInfoData(
                            dataResponseResponse.body().data
                    )
                }
    }
}
