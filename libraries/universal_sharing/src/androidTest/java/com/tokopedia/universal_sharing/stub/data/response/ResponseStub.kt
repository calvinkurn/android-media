package com.tokopedia.universal_sharing.stub.data.response

import com.tokopedia.universal_sharing.stub.common.util.AndroidFileUtil
import java.lang.reflect.Type

data class ResponseStub<T> (
    var filePath: String = "",
    var type: Type,
    var query: String = "",
    var error: Throwable? = null
) {

    var responseObject: T = convertToResponseObject()

    private fun convertToResponseObject(): T {
        return AndroidFileUtil.parse(filePath, type)
    }

    /**
     * For updating the value of current json file
     */
    fun editAndGetResponseObject(altercation: (T) -> Unit) {
        val response = convertToResponseObject()
        altercation(response)
        responseObject = response
    }

    /**
     * For updating the response from json file
     */
    fun updateResponseObject() {
        responseObject = convertToResponseObject()
    }
}
