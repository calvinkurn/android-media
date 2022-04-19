package com.tokopedia.purchase_platform.common.exception

import com.tokopedia.abstraction.common.network.exception.ResponseErrorException

class CartResponseErrorException : ResponseErrorException {
    constructor(message: String?) : super(message)
    constructor() : super()

    companion object {
        private const val serialVersionUID = -3672249531132491023L
    }
}