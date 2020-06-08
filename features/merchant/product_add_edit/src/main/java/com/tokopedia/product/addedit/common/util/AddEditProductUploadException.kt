package com.tokopedia.product.addedit.common.util

/**
 * @author by milhamj on 22/04/20.
 */
class AddEditProductUploadException : Exception {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}