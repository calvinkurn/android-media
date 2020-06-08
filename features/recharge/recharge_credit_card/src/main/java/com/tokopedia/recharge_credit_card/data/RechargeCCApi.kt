package com.tokopedia.recharge_credit_card.data

import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.recharge_credit_card.PATH_CREDIT_CARD
import retrofit2.Response
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RechargeCCApi {

    @FormUrlEncoded
    @POST(PATH_CREDIT_CARD)
    suspend fun postCreditCard(@FieldMap map: MutableMap<String, String>): Response<DataResponse<CCRedirectUrl>>
}