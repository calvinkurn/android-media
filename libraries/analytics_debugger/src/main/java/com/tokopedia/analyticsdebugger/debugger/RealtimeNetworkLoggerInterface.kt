package com.tokopedia.analyticsdebugger.debugger

/**
 * Created By : Jonathan Darwin on November 17, 2021
 */
interface RealtimeNetworkLoggerInterface {
    fun init(generalInfo: String)

    fun send(event: String, message: String)

    fun send(event: String)
}