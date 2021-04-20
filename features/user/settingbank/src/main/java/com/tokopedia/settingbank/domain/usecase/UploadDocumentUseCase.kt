package com.tokopedia.settingbank.domain.usecase

import com.tokopedia.settingbank.data.SettingBankApi
import com.tokopedia.settingbank.domain.model.DocumentType
import com.tokopedia.settingbank.domain.model.UploadDocumentPojo
import com.tokopedia.settingbank.domain.model.UploadDocumentRequest
import com.tokopedia.settingbank.domain.mapper.UploadDocumentMapper
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class UploadDocumentUseCase @Inject constructor(val api: SettingBankApi,
                                                val mapper: UploadDocumentMapper) : UseCase<String>() {

    override fun createObservable(requestParams: RequestParams): Observable<String> {
        val uploadDocumentRequest = requestParams.parameters[DATA] as UploadDocumentRequest
        return api.uploadConfirmationDocument(uploadDocumentRequest).map(mapper)
    }

    companion object {

        const val DATA = "data"

        fun getParam(uploadDocumentPojo: UploadDocumentPojo): RequestParams {
            val requestParams: RequestParams = RequestParams.create()
            requestParams.putObject(DATA, getUploadRequest(uploadDocumentPojo))
            return requestParams
        }

        private fun getUploadRequest(uploadDocumentPojo: UploadDocumentPojo): UploadDocumentRequest {
            val documentType = DocumentType(
                    uploadDocumentPojo.doc_type,
                    uploadDocumentPojo.document_name,
                    uploadDocumentPojo.document_mime,
                    uploadDocumentPojo.document_ext,
                    uploadDocumentPojo.document_base64
            )
            return UploadDocumentRequest(
                    uploadDocumentPojo.acc_id,
                    uploadDocumentPojo.acc_name ?: "",
                    uploadDocumentPojo.acc_number ?: "",
                    uploadDocumentPojo.bank_id,
                    documentType
            )
        }
    }
}