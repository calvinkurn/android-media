package com.tokopedia.settingbank.banklist.v2.domain.usecase

import com.tokopedia.settingbank.banklist.v2.data.SettingBankApi
import com.tokopedia.settingbank.banklist.v2.domain.mapper.UploadDocumentMapper
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class UploadDocumentUseCase @Inject constructor(val api: SettingBankApi,
                                                val mapper: UploadDocumentMapper) : UseCase<Boolean>() {

    override fun createObservable(requestParams: RequestParams): Observable<Boolean> {
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


        fun getParam(accountId: Long, accountName : String, accountNumber :String,
                     bankID : Long, documentType: Int, documentName : String, documentMime: String,
                     documentExt : String, documentBase64: String): RequestParams {
            val requestParams: RequestParams = RequestParams.create()
            requestParams.putLong(PARAM_ACC_ID, accountId)
            requestParams.putString(PARAM_ACC_NAME, accountName)
            requestParams.putString(PARAM_ACC_NUMBER, accountNumber)
            requestParams.putLong(PARAM_BANK_ID, bankID)
            requestParams.putInt(PARAM_DOC_TYPE, documentType)
            requestParams.putString(PARAM_DOCUMENT_NAME, documentName)
            requestParams.putString(PARAM_DOCUMENT_MIME, documentMime)
            requestParams.putString(PARAM_DOCUMENT_EXT, documentExt)
            requestParams.putString(PARAM_DOCUMENT_BASE64, documentBase64)
            return requestParams
        }
    }
}