package com.tokopedia.gm.common.data.source.cloud.api

import com.tokopedia.gm.common.constant.GMCommonUrl
import com.tokopedia.gm.common.data.source.cloud.model.RequestCashbackModel
import com.tokopedia.gm.common.data.source.cloud.model.ShopScoreResult
import com.tokopedia.network.data.model.response.DataResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import rx.Observable

interface GMCommonApi {
    @POST(GMCommonUrl.SET_CASHBACK_PRODUCTS)
    fun setCashback(@Body cashback: RequestCashbackModel?): Observable<Response<DataResponse<String?>?>?>

    @GET(GMCommonUrl.SHOPS_SCORE_STATUS + "{shopId}")
    fun getShopScoreDetail(@Path("shopId") shopId: String?): Observable<Response<DataResponse<ShopScoreResult?>?>?>
}