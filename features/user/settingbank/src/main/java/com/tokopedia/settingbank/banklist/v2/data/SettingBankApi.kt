package com.tokopedia.settingbank.banklist.v2.data

import com.tokopedia.settingbank.banklist.v2.domain.UploadDocumentRequest
import com.tokopedia.settingbank.banklist.v2.domain.UploadDocumentResponse
import retrofit2.Response
import retrofit2.http.*
import rx.Observable

interface SettingBankApi {

    @POST(SettingBankUrl.PATH_POST_DOCUMENT)
    fun uploadConfirmationDocument(@Body uploadDocumentRequest: UploadDocumentRequest):
            Observable<Response<UploadDocumentResponse>>


}