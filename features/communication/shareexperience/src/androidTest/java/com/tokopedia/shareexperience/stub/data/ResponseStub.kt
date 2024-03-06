package com.tokopedia.shareexperience.stub.data

import com.tokopedia.shareexperience.stub.common.AndroidFileUtil
import java.lang.reflect.Type

data class ResponseStub<T> (
    var filePath: String = "",
    var type: Type,
    var query: String = "",
    var isError: Boolean = false
) {

    var responseObject: T = convertToResponseObject()

    private fun convertToResponseObject(): T {
        return AndroidFileUtil.parse(filePath, type)
    }

    fun editAndGetResponseObject(altercation: (T) -> Unit) {
        val response = convertToResponseObject()
        altercation(response)
        responseObject = response
    }

    fun updateResponseObject() {
        responseObject = convertToResponseObject()
    }
}
