package com.tokopedia.topchat.chatroom.responses

object WebsocketResponses {

    val typing = """
        {
          "code": 203,
          "data": {
            "msg_id": 0,
            "from": "bcdua",
            "from_uid": 148201400,
            "from_user_name": "bcdua",
            "from_role": "Shop Owner",
            "is_opposite": true,
            "to_uid": 143252780,
            "to_buyer": true,
            "client_connect_time": "0001-01-01T00:00:00Z"
          }
        }
    """.trimIndent()

    val endTyping = """
        {
          "code": 204,
          "data": {
            "msg_id": 0,
            "from": "bcdua",
            "from_uid": 148201400,
            "from_user_name": "bcdua",
            "from_role": "Shop Owner",
            "is_opposite": true,
            "to_uid": 143252780,
            "to_buyer": true,
            "client_connect_time": "0001-01-01T00:00:00Z"
          }
        }
    """.trimIndent()

    val typingNotForMe = """
        {
          "code": 203,
          "data": {
            "msg_id": 99999,
            "from": "bcdua",
            "from_uid": 148201400,
            "from_user_name": "bcdua",
            "from_role": "Shop Owner",
            "is_opposite": true,
            "to_uid": 143252780,
            "to_buyer": true,
            "client_connect_time": "0001-01-01T00:00:00Z"
          }
        }
    """.trimIndent()

    val read = """
        {
          "code": 301,
          "data": {
            "msg_id": 0,
            "from": "bcdua",
            "from_uid": 148201400,
            "from_user_name": "bcdua",
            "from_role": "Shop Owner",
            "is_opposite": true,
            "to_uid": 143252780,
            "to_buyer": true,
            "client_connect_time": "0001-01-01T00:00:00Z"
          }
        }
    """.trimIndent()

    val deleteMsg = """
          {
            "code": 104,
            "data": {
              "msg_id": 0,
              "reply_time": 1638958002690827000
            }
          }
    """.trimIndent()

    fun generateReplyMsg(
        isOpposite: Boolean = false
    ) = """
        {
          "code": 103,
          "data": {
            "msg_id": 0,
            "from": "yunitatujuh",
            "from_uid": 143252780,
            "from_user_name": "yunitatujuh",
            "from_role": "User",
            "thumbnail": "https://imagerouter.tokopedia.com/image/v1/u/143252780/user_thumbnail/desktop",
            "is_opposite": $isOpposite,
            "to_uid": 143252780,
            "message": {
              "censored_reply": "a",
              "original_reply": "a",
              "timestamp": "2022-01-13T17:49:57.768548864+07:00",
              "timestamp_fmt": "13 January 2022, 17:49 WIB",
              "timestamp_unix": 1642070997768,
              "timestamp_unix_nano": 1642070997768549000
            },
            "start_time": "2022-01-13T10:49:56.462Z",
            "show_rating": false,
            "to_buyer": true,
            "local_id": "74506156-514c-4c79-ac62-aa0088a3be64",
            "client_connect_time": "0001-01-01T00:00:00Z",
            "source": "inbox"
          }
        }

    """.trimIndent()


}