package com.tokopedia.inbox.universalinbox.stub.data.response

import com.tokopedia.inbox.universalinbox.stub.common.util.AndroidFileUtil
import java.lang.reflect.Type

data class ResponseStub<T> (
    var filePath: String = "",
    var type: Type,
    var query: String = "",
    var isError: Boolean = false
) {

    var responseObject: T

    init {
        responseObject = convertToResponseObject()
    }

    private fun convertToResponseObject(): T {
        return AndroidFileUtil.parse(filePath, type)
    }

    fun editAndGetResponseObject(altercation: (T) -> Unit) {
        val response = convertToResponseObject()
        altercation(response)
        responseObject = response
    }
}
