package com.tokopedia.fakeresponse.presentation.viewmodels.data

import com.tokopedia.fakeresponse.db.entities.GqlRecord
import com.tokopedia.fakeresponse.db.entities.RestRecord
import java.util.*

data class AddGqlData(
    val gqlQueryName: String?,
    val customTag: String? = null,
    val response: String?,
    val isResponseSuccess: Boolean = true,
    val isDelayResponse: Boolean = false
)

data class AddRestData(
    val url: String?,
    val methodName: String,
    val response: String?,
    val customTag: String? = null,
    val isResponseSuccess: Boolean = true
)

fun AddGqlData.toGqlRecord(id: Int? = null, createdAt: Long? = null): GqlRecord {
    val date = Date()
    val currentTime = date.time

    val gql = GqlRecord(
        id = id,
        gqlOperationName = gqlQueryName ?: "",
        createdAt = createdAt ?: currentTime,
        updatedAt = currentTime,
        enabled = true,
        customTag = customTag ?: "",
        response = response!!,
        isResponseSuccess = isResponseSuccess,
        isDelayResponse = isDelayResponse
    )
    return gql
}

fun AddRestData.toRestRecord(id: Int? = null, createdAt: Long? = null): RestRecord {
    val date = Date()
    val currentTime = date.time
    return RestRecord(
        id = id,
        url = url ?: "",
        httpMethod = methodName,
        createdAt = createdAt ?: currentTime,
        updatedAt = currentTime,
        enabled = true,
        response = response ?: "",
        customTag = customTag ?: "",
        isResponseSuccess = isResponseSuccess
    )
}