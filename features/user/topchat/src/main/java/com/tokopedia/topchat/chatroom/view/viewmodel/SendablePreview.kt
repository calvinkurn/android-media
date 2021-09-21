package com.tokopedia.topchat.chatroom.view.viewmodel

import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.factory.AttachmentPreviewFactory
import okhttp3.Interceptor

interface SendablePreview {
    fun type(attachmentPreviewFactory: AttachmentPreviewFactory): Int
    fun generateMsgObj(
        messageId: String,
        opponentId: String,
        message: String,
        listInterceptor: List<Interceptor>,
        userLocationInfo: LocalCacheModel
    ): Any

    fun notEnoughRequiredData(): Boolean
}