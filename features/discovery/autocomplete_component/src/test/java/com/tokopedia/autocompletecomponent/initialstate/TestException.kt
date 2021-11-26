package com.tokopedia.autocompletecomponent.initialstate

/**
 * Exception used for Unit Test.
 * Override printStackTrace to not print any error, and use flag to indicate printStackTrace is called
 * */
class TestException(message: String = "") : Exception(message) {

    var isStackTracePrinted = false

    override fun printStackTrace() {
        isStackTracePrinted = true
    }
}