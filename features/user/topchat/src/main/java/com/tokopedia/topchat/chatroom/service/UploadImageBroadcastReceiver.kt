package com.tokopedia.topchat.chatroom.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.tokopedia.abstraction.constant.TkpdState

class UploadImageBroadcastReceiver(private val listener: UploadImageBroadcastListener): BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == UploadImageChatService.BROADCAST_UPLOAD_IMAGE) {
            when (intent.getIntExtra(TkpdState.ProductService.STATUS_FLAG, 0)) {
                TkpdState.ProductService.STATUS_DONE -> listener.onSuccessUploadImageWithService(intent)
                TkpdState.ProductService.STATUS_ERROR -> listener.onErrorUploadImageWithService(intent)
            }
        }
    }
}

interface UploadImageBroadcastListener {
    fun onSuccessUploadImageWithService(intent: Intent)
    fun onErrorUploadImageWithService(intent: Intent)
}