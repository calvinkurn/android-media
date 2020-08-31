package com.tokopedia.fakeresponse.chuck

import android.database.Cursor

object Utils {

    val KEYWORDS_FOR_GQL = arrayListOf<String>("\"operationName\":", "\"query\":", "\"variables\":")

    fun cursorToTransactionEntity(it: Cursor): TransactionEntity {
        val responseBody = it.getString(it.getColumnIndex(TransactionEntityColumn.RESPONSE_BODY))
        val requestDate = it.getLong(it.getColumnIndex(TransactionEntityColumn.REQUEST_DATE))
        val requestBody = it.getString(it.getColumnIndex(TransactionEntityColumn.REQUEST_BODY))
        val url = it.getString(it.getColumnIndex(TransactionEntityColumn.URL))
        val method = it.getString(it.getColumnIndex(TransactionEntityColumn.METHOD))
        val host = it.getString(it.getColumnIndex(TransactionEntityColumn.HOST))
        val path = it.getString(it.getColumnIndex(TransactionEntityColumn.PATH))
        val responseCode = it.getInt(it.getColumnIndex(TransactionEntityColumn.RESPONSE_CODE))
        val id = it.getLong(it.getColumnIndex(TransactionEntityColumn.ID))

        var isGql = true
        KEYWORDS_FOR_GQL.forEach {
            if (!requestBody.isNullOrEmpty() && !requestBody.contains(it)) {
                isGql = false
                return@forEach
            }
        }
        if (requestBody.isNullOrEmpty()) {
            isGql = false
        }

        return TransactionEntity(responseBody = responseBody,
                requestBody = requestBody,
                responseCode = responseCode,
                path = path,
                host = host,
                method = method,
                url = url,
                requestDate = requestDate,
                id = id,
                isGql = isGql)
    }

    fun isGqlRequest(requestBody: String?): Boolean {
        var isGql = true
        Utils.KEYWORDS_FOR_GQL.forEach {
            if (!requestBody.isNullOrEmpty() && !requestBody.contains(it)) {
                isGql = false
                return@forEach
            }
        }
        if (requestBody.isNullOrEmpty()) {
            isGql = false
        }
        return isGql
    }
}