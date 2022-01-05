package com.tokopedia.gm.common.data.source.cloud

import javax.inject.Inject
import com.tokopedia.gm.common.data.source.cloud.api.GMCommonApi
import com.tokopedia.gm.common.data.source.cloud.model.RequestCashbackModel
import com.tokopedia.network.data.model.response.DataResponse
import retrofit2.Response
import rx.Observable

/**
 * @author hendry on 4/4/17.
 */
class GMCommonCloudDataSource @Inject constructor(private val gmCommonApi: GMCommonApi) {
    fun setCashback(requestCashbackModel: RequestCashbackModel?): Observable<Response<DataResponse<String?>?>?> {
        return gmCommonApi.setCashback(requestCashbackModel)
    }
}