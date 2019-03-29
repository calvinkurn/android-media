package com.tokopedia.settingbank.choosebank.data

import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.settingbank.choosebank.domain.pojo.BankListPojo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap
import rx.Observable

/**
 * @author by nisie on 7/2/18.
 */
interface BankListApi {

    @GET(BankListUrl.PATH_SEARCH_BANK_ACCOUNT)
    fun getBankList(@QueryMap params: HashMap<String, Any>):
            Observable<Response<DataResponse<BankListPojo>>>

}