package com.tokopedia.promousage.util.logger

import com.tokopedia.network.constant.ErrorNetMessage
import java.io.IOException

internal class PromoErrorException : IOException {

    private var messageError: String? = null

    constructor() : super(ErrorNetMessage.MESSAGE_ERROR_DEFAULT) {
        this.messageError = ErrorNetMessage.MESSAGE_ERROR_DEFAULT
    }

    constructor(message: String) : super(message) {
        if (!message.isEmpty()) {
            this.messageError = message
        } else {
            this.messageError = ErrorNetMessage.MESSAGE_ERROR_DEFAULT
        }
    }
}
