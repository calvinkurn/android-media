package com.tokopedia.sessioncommon

import com.tokopedia.network.exception.MessageErrorException

class SessionMessageErrorException(message: String?, errorCode: String?) : MessageErrorException(message) {
    var errorCode: String? = errorCode
        private set

}