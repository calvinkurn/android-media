package com.tokopedia.settingbank.data

import com.tokopedia.settingbank.domain.model.UploadDocumentRequest
import com.tokopedia.settingbank.domain.model.UploadDocumentResponse
import retrofit2.Response
import retrofit2.http.*
import rx.Observable

interface SettingBankApi {

    @POST(SettingBankUrl.PATH_POST_DOCUMENT)
    fun uploadConfirmationDocument(@Body uploadDocumentRequest: UploadDocumentRequest):
            Observable<Response<UploadDocumentResponse>>


}