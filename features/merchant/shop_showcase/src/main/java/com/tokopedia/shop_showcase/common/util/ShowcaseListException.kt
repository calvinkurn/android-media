package com.tokopedia.shop_showcase.common.util


class ShowcaseListException : Exception {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}