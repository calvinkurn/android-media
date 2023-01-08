package com.tokopedia.broadcaster.revamp.util.error

/**
 * Created by meyta.taliti on 06/04/22.
 */
class BroadcasterException(private val mErrorType: BroadcasterErrorType) : Exception() {

    override val message: String
        get() = mErrorType.message

    val code: Int
        get() = mErrorType.code

    val errorType = mErrorType
}