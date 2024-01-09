package com.tokopedia.logger.repository

interface InternalLoggerInterface {

    /**
     * In case we want to track the data sent into server.
     * This function can be used, in example in debug app, for debugging only,
     * to store the data sent into local sqlitedatabase
     * This can benefit, for example to compare the data in db, with the data sent.
     */
    fun putServerLoggerEvent(data: Any)
}