package com.rahullohra.fakeresponse.domain.exceptions

import java.lang.Exception

class EmptyException(val msg:String):Exception(){
    override val message: String?
        get() = msg
}

class NoGqlNameException : Exception() {
    override val message: String?
        get() = "Either query name or custom name is empty"
}

class NoResponseException : Exception() {
    override val message: String?
        get() = "response is empty"
}