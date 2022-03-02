package com.tokopedia.profilecompletion.common

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.network.exception.MessageErrorException

fun GraphqlError.getErrorMessage(key: String): String {
    val json = this.extensions.developerMessage
    val arrayError = Gson().fromJson<Array<Map<String, String>>>(json, object: TypeToken<Array<Map<String, String>>>() {
    }.type)
    val value = arrayError.firstOrNull {
        it["key"] == key
    }
    val errorMessage = value?.get("value") ?: throw MessageErrorException(json)
    return errorMessage
}
