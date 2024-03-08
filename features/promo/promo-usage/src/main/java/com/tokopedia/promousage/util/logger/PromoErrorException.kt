package com.tokopedia.promousage.util.logger

import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.network.constant.ErrorNetMessage

class PromoErrorException : ResponseErrorException {

    private var messageError: String? = null

    constructor() : super(ErrorNetMessage.MESSAGE_ERROR_DEFAULT) {
        this.messageError = ErrorNetMessage.MESSAGE_ERROR_DEFAULT
    }

    constructor(message: String) : super() {
        if (!message.isEmpty()) {
            this.messageError = message
        } else {
            this.messageError = ErrorNetMessage.MESSAGE_ERROR_DEFAULT
        }
    }

    override val message: String
        get() = messageError ?: ErrorNetMessage.MESSAGE_ERROR_DEFAULT
}
