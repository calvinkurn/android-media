package com.tokopedia.topchat.chatroom.view.viewmodel

import com.tokopedia.topchat.chatroom.view.adapter.viewholder.factory.AttachmentPreviewFactory
import okhttp3.Interceptor

interface PreviewViewModel {
    fun type(attachmentPreviewFactory: AttachmentPreviewFactory): Int
    fun sendTo(messageId: String, opponentId: String, listInterceptor: List<Interceptor>)
}