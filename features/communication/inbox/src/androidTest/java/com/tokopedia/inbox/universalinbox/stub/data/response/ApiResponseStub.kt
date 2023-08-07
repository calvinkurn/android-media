package com.tokopedia.inbox.universalinbox.stub.data.response

object ApiResponseStub {

    private const val GENERAL_RESPONSE_CODE = 200

    lateinit var generalEmptyResponse: ApiResponseModelStub
    lateinit var connectionResponse: ApiResponseModelStub
    lateinit var profileResponse: ApiResponseModelStub
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

    /**
     * Chat List
     */
    const val CHANNEL_LIST = "v2/chat/channels?batch_size=10&timestamp="

    // Values
    const val MESSAGES = "messages"
}
