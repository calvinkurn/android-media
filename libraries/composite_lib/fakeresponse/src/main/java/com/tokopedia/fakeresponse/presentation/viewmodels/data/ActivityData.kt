package com.tokopedia.fakeresponse.presentation.viewmodels.data

import com.tokopedia.fakeresponse.db.entities.GqlRecord
import com.tokopedia.fakeresponse.db.entities.RestRecord
import java.util.*

data class AddGqlData(val gqlQueryName: String?, val customTag: String? = null, val response: String?)

data class AddRestData(val url: String?, val methodName: String, val response: String?, val customTag: String? = null)

fun AddGqlData.toGqlRecord(id: Int? = null): GqlRecord {
    val date = Date()

    val gql = GqlRecord(
            id = id,
            gqlOperationName = gqlQueryName ?: "",
            createdAt = date.time,
            updatedAt = date.time,
            enabled = true,
            customTag = customTag,
            response = response!!

    )
    return gql
}

fun AddRestData.toRestRecord(id: Int? = null): RestRecord {
    val date = Date()
    return RestRecord(
            id = id,
            url = url ?: "",
            httpMethod = methodName,
            createdAt = date.time,
            updatedAt = date.time,
            enabled = true,
            response = response ?: "",
            customTag = customTag
    )
}