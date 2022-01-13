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
}