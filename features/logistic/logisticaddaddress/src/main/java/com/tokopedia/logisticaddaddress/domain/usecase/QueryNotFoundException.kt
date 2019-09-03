package com.tokopedia.logisticaddaddress.domain.usecase

class QueryNotFoundException : Exception {
    constructor(): super()
    constructor(message: String?): super(message)
}