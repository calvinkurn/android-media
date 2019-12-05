package com.tokopedia.settingbank.banklist.v2.domain.mapper

import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.settingbank.banklist.v2.domain.UploadDocumentResponse
import retrofit2.Response
import rx.functions.Func1

class UploadDocumentMapper : Func1<Response<DataResponse<UploadDocumentResponse>>, Boolean> {

    override fun call(response: Response<DataResponse<UploadDocumentResponse>>): Boolean {
        val body = response.body()
        if (body != null) {
            if (body.header.messages.isEmpty() ||
                    body.header.messages[0].isBlank()) {
                val pojo: UploadDocumentResponse = body.data
                return (pojo.status == 200)
            } else {
                throw MessageErrorException(body.header.messages[0])
            }
        } else {
            throw MessageErrorException("")
        }
    }

}