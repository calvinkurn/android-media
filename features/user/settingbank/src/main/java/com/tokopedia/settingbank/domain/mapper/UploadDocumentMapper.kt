package com.tokopedia.settingbank.domain.mapper

import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.settingbank.domain.model.UploadDocumentResponse
import retrofit2.Response
import rx.functions.Func1
import javax.inject.Inject

class UploadDocumentMapper @Inject constructor() : Func1<Response<UploadDocumentResponse>, String> {

    override fun call(response: Response<UploadDocumentResponse>): String {
        val body = response.body()
        if (body != null) {
            if (body.status == 200) {
                return body.message
            } else {
                throw MessageErrorException(body.message)
            }
        } else {
            throw MessageErrorException("")
        }
    }

}