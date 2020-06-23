package com.tokopedia.atc_common.domain

import com.tokopedia.abstraction.common.network.exception.ResponseErrorException

class AddToCartResponseErrorException : ResponseErrorException {
    constructor(message: String?) : super(message) {}
    constructor() : super() {}
}