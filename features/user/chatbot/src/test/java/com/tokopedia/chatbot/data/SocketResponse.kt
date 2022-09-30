package com.tokopedia.chatbot.data

import com.google.gson.GsonBuilder
import com.tokopedia.chatbot.websocket.ChatWebSocketResponse

object SocketResponse {

    const val RESPONSE_WITH_103_REPLY_MESSAGE = """
        {
  "type": "",
  "code": 103,
  "data": {
    "msg_id": 4058088,
    "from": "Tanya",
    "from_uid": 5515973,
    "from_user_name": "Tanya",
    "from_role": "User",
    "thumbnail": "https://ecs7.tokopedia.net/img/cache/100-square/user-1/2020/8/13/5515973/5515973_bf39448d-d1bd-410b-9e7b-b5acdf54555a.png",
    "is_bot": true,
    "reminder_ticker": null,
    "is_opposite": true,
    "to_uid": 9120466,
    "message": {
      "censored_reply": "Jangan khawatir, TANYA akan bantu solusikan kendalamu. Klik salah satu pilihan kendala yang kamu alami terlebih dulu, ya:",
      "original_reply": "Jangan khawatir, TANYA akan bantu solusikan kendalamu. Klik salah satu pilihan kendala yang kamu alami terlebih dulu, ya:",
      "timestamp": "2022-09-30T11:40:02.587052+07:00",
      "timestamp_fmt": "30 September 2022, 11:40 WIB",
      "timestamp_unix": 1664512802587,
      "timestamp_unix_nano": 1664512802587052000
    },
    "start_time": "2022-09-30T11:40:02.587052+07:00",
    "show_rating": false,
    "rating_status": 0,
    "to_buyer": true,
    "client_connect_time": "0001-01-01T00:00:00Z",
    "source": "chatbot_{\"name\":\"TANYA\",\"icon_url\":\"https://images.tokopedia.net/img/chatbot/tanya.png\",\"icon_url_dark\":\"https://images.tokopedia.net/img/chatbot/tanya-dark.png\"}"
  }
}
    """

    const val RESPONSE_WITH_204_END_TYPING = """
        {
  "type": "",
  "code": 204,
  "data": {
    "msg_id": 4058088,
    "from": "Tanya",
    "from_uid": 5515973,
    "from_user_name": "Tanya",
    "from_role": "User",
    "is_bot": true,
    "reminder_ticker": null,
    "is_opposite": true,
    "to_uid": 9120466,
    "to_buyer": true,
    "client_connect_time": "0001-01-01T00:00:00Z"
  }
}
    """

    const val RESPONSE_WITH_203_START_TYPING = """
        {
  "type": "",
  "code": 203,
  "data": {
    "msg_id": 4058088,
    "from": "Tanya",
    "from_uid": 5515973,
    "from_user_name": "Tanya",
    "from_role": "User",
    "is_bot": true,
    "reminder_ticker": null,
    "is_opposite": true,
    "to_uid": 9120466,
    "to_buyer": true,
    "client_connect_time": "0001-01-01T00:00:00Z"
  }
}
    """

    const val RESPONSE_WITH_301_READ_MESSAGE = """
        {
  "type": "",
  "code": 301,
  "data": {
    "msg_id": 4058088,
    "from": "Tanya",
    "from_uid": 5515973,
    "from_user_name": "Tanya",
    "from_role": "User",
    "is_bot": true,
    "reminder_ticker": null,
    "is_opposite": true,
    "to_uid": 9120466,
    "to_buyer": true,
    "client_connect_time": "0001-01-01T00:00:00Z"
  }
}
    """

    fun getResponse(response: String): ChatWebSocketResponse {
        return GsonBuilder().create()
            .fromJson<ChatWebSocketResponse>(
                response,
                ChatWebSocketResponse::class.java
        )
    }
}
