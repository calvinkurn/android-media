package com.tokopedia.common_digital.common

import com.tokopedia.network.constant.ErrorNetMessage

import java.io.IOException

/**
 * Created by Rizky on 13/08/18.
 */
class DigitalResponseErrorException : IOException {

    private var messageError: String? = null

    constructor() : super("Http Error : " + ErrorNetMessage.MESSAGE_ERROR_DEFAULT) {
        this.messageError = ErrorNetMessage.MESSAGE_ERROR_DEFAULT
    }

    constructor(message: String?) : super("Http Error : " + message!!) {
        if (!message.isEmpty())
            this.messageError = message
        else
            this.messageError = ErrorNetMessage.MESSAGE_ERROR_DEFAULT
    }

    override val message: String?
        get() = messageError

}
