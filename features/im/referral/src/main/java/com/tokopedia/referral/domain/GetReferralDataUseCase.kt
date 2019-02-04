package com.tokopedia.referral.domain


import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.domain.RestRequestUseCase
import com.tokopedia.network.constant.TkpdBaseURL
import com.tokopedia.referral.Constants
import com.tokopedia.referral.data.ReferralCodeEntity
import com.tokopedia.usecase.RequestParams

import java.lang.reflect.Type
import java.util.ArrayList

import javax.inject.Inject

/**
 * Created by ashwanityagi on 22/01/18.
 */

class GetReferralDataUseCase @Inject
constructor() : RestRequestUseCase() {

    override fun buildRequest(requestParams: RequestParams): List<RestRequest> {
        val tempRequest = ArrayList<RestRequest>()
        val url = TkpdBaseURL.BASE_DOMAIN + Constants.ReferralApiPath.PATH_GET_REFERRAL_VOUCHER_CODE
        val token = object : TypeToken<DataResponse<ReferralCodeEntity>>() {

        }.type
        val postJson = JsonObject()
        postJson.add(data, requestParams.parameters[data] as JsonElement)
        val restReferralRequest = RestRequest.Builder(url, token)
                .setBody(postJson)
                .setRequestType(RequestType.POST)
                .build()
        tempRequest.add(restReferralRequest)
        return tempRequest
    }

    companion object {

        val userId = "user_id"
        val msisdn = "msisdn"
        val data = "data"
    }

}
