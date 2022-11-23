package com.tokopedia.privacycenter.dsar.domain

object HeaderUtils {
    fun createHeader(token: String = "", contentType: String = "application/json"): HashMap<String, String> {
        val header = HashMap<String, String>()
        header["Accept"] = "application/json"
        header["Content-Type"] = contentType
        if(token.isNotEmpty()) {
            header["Authorization"] = "Bearer $token"
        }
        return header
    }
}
