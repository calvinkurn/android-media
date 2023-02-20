package com.tokopedia.privacycenter.remote

import com.tokopedia.privacycenter.ui.dsar.DsarConstants

object HeaderUtils {
    fun createHeader(token: String = "", contentType: String = DsarConstants.APPLICATION_JSON): HashMap<String, String> {
        val header = HashMap<String, String>()
        header[DsarConstants.HEADER_ACCEPT] = DsarConstants.APPLICATION_JSON
        header[DsarConstants.HEADER_CONTENT_TYPE] = contentType
        if (token.isNotEmpty()) {
            header[DsarConstants.HEADER_AUTHORIZATION] = "${DsarConstants.BEARER} $token"
        }
        return header
    }
}
