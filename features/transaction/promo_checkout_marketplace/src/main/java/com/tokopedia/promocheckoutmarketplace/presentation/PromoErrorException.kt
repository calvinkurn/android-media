package com.tokopedia.promocheckoutmarketplace.presentation

import com.tokopedia.network.constant.ErrorNetMessage
import java.io.IOException

class PromoErrorException : IOException {

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
