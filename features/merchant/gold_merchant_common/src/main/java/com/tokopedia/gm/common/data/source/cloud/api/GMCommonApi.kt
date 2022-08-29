package com.tokopedia.gm.common.data.source.cloud.api

import com.tokopedia.gm.common.constant.GMCommonUrl
import com.tokopedia.gm.common.data.source.cloud.model.RequestCashbackModel
import com.tokopedia.network.data.model.response.DataResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import rx.Observable

interface GMCommonApi {
    @POST(GMCommonUrl.SET_CASHBACK_PRODUCTS)
    fun setCashback(@Body cashback: RequestCashbackModel?): Observable<Response<DataResponse<String?>?>?>
}