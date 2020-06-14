package com.tokopedia.talk_old.producttalk.view.data

import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.network.constant.TkpdBaseURL
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap
import rx.Observable

interface ProductTalkApi {

    @GET(TkpdBaseURL.KunyitTalk.GET_PRODUCT_TALK)
    fun getProductTalk(@QueryMap params: HashMap<String, Any>): Observable<Response<DataResponse<String>>>

}
