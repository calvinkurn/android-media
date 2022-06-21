package com.tokopedia.common.payment

import android.webkit.ConsoleMessage
import android.widget.ProgressBar
import com.tokopedia.abstraction.base.view.webview.CommonWebViewClient
import com.tokopedia.abstraction.base.view.webview.FilePickerInterface
import com.tokopedia.logger.ServerLogger

import com.tokopedia.logger.utils.Priority

class PaymentLoggingClient(filePickerInterface: FilePickerInterface, progressBar: ProgressBar?)
    : CommonWebViewClient(filePickerInterface, progressBar) {

    override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
        val msgType = consoleMessage.messageLevel()
        if (msgType == ConsoleMessage.MessageLevel.ERROR) {
            val map: MutableMap<String, String> = HashMap()
            map["type"] = "error"
            map["err"] = "web_console_error"
            map["desc"] = consoleMessage.message()
            ServerLogger.log(Priority.P1, "WEBVIEW_ERROR", map)
        }
        return true
    }
}