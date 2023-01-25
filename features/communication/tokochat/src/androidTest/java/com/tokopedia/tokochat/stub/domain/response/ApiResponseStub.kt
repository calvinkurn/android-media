package com.tokopedia.tokochat.stub.domain.response

object ApiResponseStub {

    private const val GENERAL_RESPONSE_CODE = 200

    lateinit var generalEmptyResponse: Pair<Int, String>
    lateinit var connectionResponse: Pair<Int, String>
    lateinit var profileResponse: Pair<Int, String>
    lateinit var channelIdResponse: Pair<Int, String>
    lateinit var channelDetailsResponse: Pair<Int, String>
    lateinit var chatHistoryResponse: Pair<Int, String>

    init {
        reset()
    }

    fun reset() {
        generalEmptyResponse = Pair(GENERAL_RESPONSE_CODE, "")

        connectionResponse = Pair(
            GENERAL_RESPONSE_CODE, "connection/success_get_connection.json")

        profileResponse = Pair(
            GENERAL_RESPONSE_CODE, "profile/success_get_profile.json")

        channelIdResponse = Pair(
            GENERAL_RESPONSE_CODE, "channel_id/success_get_channel_id.json")

        channelDetailsResponse = Pair(
            GENERAL_RESPONSE_CODE, "channel_details/success_get_channel_details.json")

        chatHistoryResponse = Pair(
            GENERAL_RESPONSE_CODE, "chat_history/success_get_chat_history.json")
    }
}
