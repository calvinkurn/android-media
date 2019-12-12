package com.tokopedia.settingbank.banklist.v2.domain.usecase

import com.tokopedia.settingbank.banklist.v2.data.SettingBankApi
import com.tokopedia.settingbank.banklist.v2.domain.UploadDocumentPojo
import com.tokopedia.settingbank.banklist.v2.domain.mapper.UploadDocumentMapper
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class UploadDocumentUseCase @Inject constructor(val api: SettingBankApi,
                                                val mapper: UploadDocumentMapper) : UseCase<String>() {

    override fun createObservable(requestParams: RequestParams): Observable<String> {
        return api.uploadConfirmationDocument(requestParams.parameters).map(mapper)
    }

    companion object {
        private val PARAM_ACC_ID: String = "acc_id"
        private val PARAM_ACC_NAME: String = "acc_name"
        private val PARAM_ACC_NUMBER: String = "acc_number"
        private val PARAM_BANK_ID: String = "bank_id"
        private val PARAM_DOC_TYPE: String = "type"
        private val PARAM_DOCUMENT_NAME: String = "document_name"
        private val PARAM_DOCUMENT_MIME: String = "document_mime"
        private val PARAM_DOCUMENT_EXT: String = "document_ext"
        private val PARAM_DOCUMENT_BASE64: String = "document_base64"


        fun getParam(uploadDocumentPojo: UploadDocumentPojo): RequestParams {
            val requestParams: RequestParams = RequestParams.create()
            requestParams.putLong(PARAM_ACC_ID, uploadDocumentPojo.acc_id)
            requestParams.putString(PARAM_ACC_NAME, uploadDocumentPojo.acc_name)
            requestParams.putString(PARAM_ACC_NUMBER, uploadDocumentPojo.acc_name)
            requestParams.putLong(PARAM_BANK_ID, uploadDocumentPojo.bank_id)
            requestParams.putInt(PARAM_DOC_TYPE, uploadDocumentPojo.doc_type)
            requestParams.putString(PARAM_DOCUMENT_NAME, uploadDocumentPojo.document_name)
            requestParams.putString(PARAM_DOCUMENT_MIME, uploadDocumentPojo.document_mime)
            requestParams.putString(PARAM_DOCUMENT_EXT, uploadDocumentPojo.document_ext)
            requestParams.putString(PARAM_DOCUMENT_BASE64, uploadDocumentPojo.document_base64)
            return requestParams
        }
    }
}