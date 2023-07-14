package com.tokopedia.kotlin.util

/**
 * @author by milhamj on 29/01/19.
 */
class ContainNullException : RuntimeException {
    constructor() : super()
    constructor(message: String?) : super(message)
}