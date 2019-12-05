package com.tokopedia.settingbank.banklist.v2.data

import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.settingbank.banklist.domain.pojo.SetDefaultBankAccountPojo
import com.tokopedia.settingbank.banklist.v2.domain.UploadDocumentResponse
import retrofit2.Response
import retrofit2.http.*
import rx.Observable

interface SettingBankApi {

    @FormUrlEncoded
    @POST(SettingBankUrl.PATH_POST_DOCUMENT)
    fun uploadConfirmationDocument(@FieldMap params: HashMap<String, Any>):
            Observable<Response<DataResponse<UploadDocumentResponse>>>


}