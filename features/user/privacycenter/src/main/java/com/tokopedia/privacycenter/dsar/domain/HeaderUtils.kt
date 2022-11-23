package com.tokopedia.privacycenter.dsar.domain

import com.tokopedia.privacycenter.dsar.DsarConstants.APPLICATION_JSON
import com.tokopedia.privacycenter.dsar.DsarConstants.BEARER
import com.tokopedia.privacycenter.dsar.DsarConstants.HEADER_ACCEPT
import com.tokopedia.privacycenter.dsar.DsarConstants.HEADER_AUTHORIZATION
import com.tokopedia.privacycenter.dsar.DsarConstants.HEADER_CONTENT_TYPE

object HeaderUtils {
    fun createHeader(token: String = "", contentType: String = APPLICATION_JSON): HashMap<String, String> {
        val header = HashMap<String, String>()
        header[HEADER_ACCEPT] = APPLICATION_JSON
        header[HEADER_CONTENT_TYPE] = contentType
        if(token.isNotEmpty()) {
            header[HEADER_AUTHORIZATION] = "$BEARER $token"
        }
        return header
    }
}
