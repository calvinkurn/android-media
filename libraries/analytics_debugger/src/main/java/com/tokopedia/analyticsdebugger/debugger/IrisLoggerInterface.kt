package com.tokopedia.analyticsdebugger.debugger

interface IrisLoggerInterface {
    fun putSendIrisEvent(data: String, rowCount: Int)

    fun putSaveIrisEvent(data: String)

    fun openSaveActivity()
    fun openSendActivity()

}
