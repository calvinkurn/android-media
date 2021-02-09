package com.tokopedia.topchat.chatroom.service

import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver

class UploadImageServiceReceiver(handler: Handler, private val uploadImageReceiver: UploadImageReceiver?) : ResultReceiver(handler) {
    override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
        uploadImageReceiver?.onReceiveResult(resultCode, resultData)
    }
}

interface UploadImageReceiver {
    fun onReceiveResult(resultCode: Int, resultData: Bundle?)
}