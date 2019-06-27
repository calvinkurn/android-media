package com.tokopedia.common_digital.cart.data.datasource

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.common_digital.cart.data.entity.requestbody.checkout.RequestBodyCheckout
import com.tokopedia.common_digital.cart.data.entity.response.ResponseCheckoutData
import com.tokopedia.common_digital.cart.data.mapper.ICartMapperData
import com.tokopedia.common_digital.cart.view.model.checkout.InstantCheckoutData
import com.tokopedia.common_digital.common.data.api.DigitalRestApi
import com.tokopedia.common_digital.product.data.response.TkpdDigitalResponse

import retrofit2.Response
import rx.Observable
import rx.functions.Func1

/**
 * Created by Rizky on 28/08/18.
 */
class DigitalInstantCheckoutDataSource(private val digitalRestApi: DigitalRestApi, private val cartMapperData: ICartMapperData) {

    private val funcResponseToCheckoutDigitalData: Func1<Response<DataResponse<ResponseCheckoutData>>, InstantCheckoutData>
        get() = Func1 { tkpdDigitalResponseResponse ->
            cartMapperData.transformInstantCheckoutData(
                    tkpdDigitalResponseResponse.body().data
            )
        }

    fun instantCheckout(requestBodyCheckout: RequestBodyCheckout): Observable<InstantCheckoutData> {
        val jsonElement = JsonParser().parse(Gson().toJson(requestBodyCheckout))
        val requestBody = JsonObject()
        requestBody.add("data", jsonElement)
        return digitalRestApi.checkout(requestBody)
                .map(funcResponseToCheckoutDigitalData)
    }

}
