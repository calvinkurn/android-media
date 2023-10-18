package com.tokopedia.chatbot.chatbot2.view.util

sealed class Attachment34RenderType {
    object RenderAttachment34 : Attachment34RenderType()
    object DonotRenderAttachment34 : Attachment34RenderType()

    companion object {
        fun mapTypeToDeviceType(type: String?): Attachment34RenderType {
            return when (type) {
                ANDROID -> RenderAttachment34
                ALL -> RenderAttachment34
                else -> DonotRenderAttachment34
            }
        }

        const val ANDROID = "android"
        const val ALL = "all"
    }
}
