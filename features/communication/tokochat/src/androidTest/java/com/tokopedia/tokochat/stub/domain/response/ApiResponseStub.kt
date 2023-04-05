package com.tokopedia.tokochat.stub.domain.response

import com.tokopedia.tokochat.test.base.BaseTokoChatTest.Companion.CHANNEL_ID_DUMMY

object ApiResponseStub {

    private const val GENERAL_RESPONSE_CODE = 200

    lateinit var generalEmptyResponse: Pair<Int, String>
    lateinit var connectionResponse: Pair<Int, String>
    lateinit var profileResponse: Pair<Int, String>
    lateinit var channelIdResponse: Pair<Int, String>
    lateinit var channelDetailsResponse: Pair<Int, String>
    lateinit var chatHistoryResponse: Pair<Int, String>
    lateinit var imageAttachmentDownloadResponse: Pair<Int, String>
    lateinit var imageAttachmentUploadResponse: Pair<Int, String>
    lateinit var sendMessageResponse: Pair<Int, String>

    init {
        reset()
    }

    fun reset() {
        generalEmptyResponse = Pair(GENERAL_RESPONSE_CODE, "")

        connectionResponse = Pair(
            GENERAL_RESPONSE_CODE,
            "connection/success_get_connection.json"
        )

        profileResponse = Pair(
            GENERAL_RESPONSE_CODE,
            "profile/success_get_profile.json"
        )

        channelIdResponse = Pair(
            GENERAL_RESPONSE_CODE,
            "channel_id/success_get_channel_id.json"
        )

        channelDetailsResponse = Pair(
            GENERAL_RESPONSE_CODE,
            "channel_details/success_get_channel_details.json"
        )

        chatHistoryResponse = Pair(
            GENERAL_RESPONSE_CODE,
            "chat_history/success_get_chat_history.json"
        )

        imageAttachmentDownloadResponse = Pair(
            GENERAL_RESPONSE_CODE,
            "image_attachment/success_get_image_attachment.json"
        )

        imageAttachmentUploadResponse = Pair(
            GENERAL_RESPONSE_CODE,
            "image_attachment/success_upload_image_attachment.json"
        )

        sendMessageResponse = Pair(
            GENERAL_RESPONSE_CODE,
            "send_message/success_send_message_attachment_image.json"
        )
    }

    // API List
    const val CONNECTION_API = "/v2/chat/connection"
    const val PROFILE_API = "/v1/chat/profile"
    const val CHANNEL_ID_API = "/v2/order"
    const val CHANNEL_API = "/v2/chat/channels/${CHANNEL_ID_DUMMY}"
    const val IMAGE_URL_API = "/v1/image"
    const val IMAGE_UPLOAD_URL_API = "/v1/image/url"
    const val SEND_MESSAGE_API = "/v2/chat/channels/${CHANNEL_ID_DUMMY}/message"

    // Values
    const val MESSAGES = "messages"
}
