package com.tokopedia.fakeresponse.chuck

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.fakeresponse.data.models.SearchType

data class TransactionEntity(@SerializedName(TransactionEntityColumn.RESPONSE_BODY) val responseBody: String? = null,
                             @SerializedName(TransactionEntityColumn.REQUEST_BODY) val requestBody: String? = null,
                             @SerializedName(TransactionEntityColumn.RESPONSE_CODE) val responseCode: Int? = null,
                             @SerializedName(TransactionEntityColumn.PATH) val path: String? = null,
                             @SerializedName(TransactionEntityColumn.HOST) val host: String? = null,
                             @SerializedName(TransactionEntityColumn.REQUEST_HEADERS) val requestHeaders: String? = null,
                             @SerializedName(TransactionEntityColumn.METHOD) val method: String? = null,
                             @SerializedName(TransactionEntityColumn.RESPONSE_HEADERS) val responseHeaders: String? = null,
                             @SerializedName(TransactionEntityColumn.URL) val url: String? = null,
                             @SerializedName(TransactionEntityColumn.REQUEST_DATE) val requestDate: Long? = null,
                             @SerializedName(TransactionEntityColumn.ID) val id: Long? = null,
                             val isGql:Boolean = false,
                             @Expose(serialize = false)
                             override var isSelectedForExport:Boolean = false,
                             override var isInExportMode: Boolean = false
): SearchType

object TransactionEntityColumn {
    const val RESPONSE_BODY = "responseBody"
    const val REQUEST_BODY = "requestBody"
    const val RESPONSE_CODE = "responseCode"
    const val REQUEST_HEADERS = "requestHeaders"
    const val METHOD = "method"
    const val RESPONSE_HEADERS = "responseHeaders"
    const val URL = "url"
    const val REQUEST_DATE = "requestDate"
    const val PATH = "path"
    const val HOST = "host"
    const val ID = "id"
}

