package com.tokopedia.tokochat.stub.domain.response

import com.tokopedia.tokochat.test.base.BaseTokoChatTest.Companion.CHANNEL_ID_DUMMY

object ApiResponseStub {

    private const val GENERAL_RESPONSE_CODE = 200

    lateinit var generalEmptyResponse: ApiResponseModelStub

    /**
     * Chat Room
     */
    lateinit var connectionResponse: ApiResponseModelStub
    lateinit var profileResponse: ApiResponseModelStub
    lateinit var channelIdResponse: ApiResponseModelStub
    lateinit var channelDetailsResponse: ApiResponseModelStub
    lateinit var chatHistoryResponse: ApiResponseModelStub
    lateinit var imageAttachmentDownloadResponse: ApiResponseModelStub
    lateinit var imageAttachmentUploadResponse: ApiResponseModelStub
    lateinit var sendMessageResponse: ApiResponseModelStub

    /**
     * Chat List
     */
    lateinit var channelListResponse: ApiResponseModelStub

    init {
        reset()
    }

    fun reset() {
        generalEmptyResponse = ApiResponseModelStub(GENERAL_RESPONSE_CODE, "")

        /**
         * Chat Room
         */
        connectionResponse = ApiResponseModelStub(
            GENERAL_RESPONSE_CODE,
            responseJsonPath = "connection/success_get_connection.json"
        )
        profileResponse = ApiResponseModelStub(
            GENERAL_RESPONSE_CODE,
            responseJsonPath = "profile/success_get_profile.json"
        )
        channelIdResponse = ApiResponseModelStub(
            GENERAL_RESPONSE_CODE,
            responseJsonPath = "channel_id/success_get_channel_id.json"
        )
        channelDetailsResponse = ApiResponseModelStub(
            GENERAL_RESPONSE_CODE,
            responseJsonPath = "channel_details/success_get_channel_details.json"
        )
        chatHistoryResponse = ApiResponseModelStub(
            GENERAL_RESPONSE_CODE,
            responseJsonPath = "chat_history/success_get_chat_history.json"
        )
        imageAttachmentDownloadResponse = ApiResponseModelStub(
            GENERAL_RESPONSE_CODE,
            responseJsonPath = "image_attachment/success_get_image_attachment.json"
        )
        imageAttachmentUploadResponse = ApiResponseModelStub(
            GENERAL_RESPONSE_CODE,
            responseJsonPath = "image_attachment/success_upload_image_attachment.json"
        )
        sendMessageResponse = ApiResponseModelStub(
            GENERAL_RESPONSE_CODE,
            responseJsonPath = "send_message/success_send_message_attachment_image.json"
        )

        /**
         * Chat List
         */
        channelListResponse = ApiResponseModelStub(
            responseCode = GENERAL_RESPONSE_CODE,
            responseJsonPath = "channel_list/success_get_channel_list.json",
            responseEditor = {
                var result = it
                result = result.replace("1690441670772", System.currentTimeMillis().toString())
                result = result.replace("1690347441960", System.currentTimeMillis().toString())
                return@ApiResponseModelStub result
            }
        )
    }

    // API List
    /**
     * Chat Room
     */
    const val CONNECTION_API = "/v2/chat/connection"
    const val PROFILE_API = "/v1/chat/profile"
    const val CHANNEL_ID_API = "/v2/order"
    const val CHANNEL_API = "/v2/chat/channels/$CHANNEL_ID_DUMMY"
    const val IMAGE_URL_API = "/v1/image"
    const val IMAGE_UPLOAD_URL_API = "/v1/image/url"
    const val SEND_MESSAGE_API = "/v2/chat/channels/$CHANNEL_ID_DUMMY/message"

    /**
     * Chat List
     */
    const val CHANNEL_LIST = "v2/chat/channels?batch_size=10&timestamp="
    const val CHANNEL_LIST_MORE = "v2/chat/channels?batch_size=11&timestamp="

    // Values
    const val MESSAGES = "messages"
}
