package com.tokopedia.chatbot.data

import com.google.gson.GsonBuilder
import com.tokopedia.chatbot.chatbot2.websocket.ChatWebSocketResponse

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
    "thumbnail": "https://images.tokopedia.net/img/cache/100-square/user-1/2020/8/13/5515973/5515973_bf39448d-d1bd-410b-9e7b-b5acdf54555a.png",
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

    const val RESPONSE_WITH_CODE_NOT_HANDLED = """
        {
  "type": "",
  "code": 1000,
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

    const val ATTACHMENT_13_OPEN_CSAT = """
        {
  "type": "",
  "code": 103,
  "data": {
    "msg_id": 4058088,
    "from": "Tanya",
    "from_uid": 5515973,
    "from_user_name": "Tanya",
    "from_role": "User",
    "thumbnail": "https://images.tokopedia.net/img/cache/100-square/user-1/2020/8/13/5515973/5515973_bf39448d-d1bd-410b-9e7b-b5acdf54555a.png",
    "is_bot": true,
    "reminder_ticker": null,
    "is_opposite": true,
    "to_uid": 9120466,
    "message": {
      "censored_reply": "",
      "original_reply": "",
      "timestamp": "2022-09-30T13:02:58.769837+07:00",
      "timestamp_fmt": "30 September 2022, 13:02 WIB",
      "timestamp_unix": 1664517778769,
      "timestamp_unix_nano": 1664517778769837000
    },
    "start_time": "2022-09-30T13:02:58.769837+07:00",
    "attachment_id": 1,
    "attachment": {
      "attributes": {
        "chatbot_session_id": "4058088_1664517753",
        "fallback_attachment": {
          "html": "Mohon segera upgrade version tokopedia anda",
          "message": "Mohon segera upgrade version tokopedia anda"
        },
        "livechat_session_id": "",
        "points": [
          {
            "caption": "Tidak Memuaskan",
            "description": "Apa yang perlu TANYA (Virtual Assistant) perbaiki?",
            "score": 1
          },
          {
            "caption": "Kurang Memuaskan",
            "description": "Apa yang perlu TANYA (Virtual Assistant) perbaiki?",
            "score": 2
          },
          {
            "caption": "Biasa Saja",
            "description": "Apa yang perlu TANYA (Virtual Assistant) perbaiki?",
            "score": 3
          },
          {
            "caption": "Memuaskan",
            "description": "Apa yang perlu TANYA (Virtual Assistant) perbaiki?",
            "score": 4
          },
          {
            "caption": "Sangat Memuaskan",
            "description": "Apa yang sebaiknya TANYA (Virtual Assistant) pertahankan?",
            "score": 5
          }
        ],
        "reason_title": "Beri saran untuk Tokopedia Care",
        "reasons": [
          "Kecepatan respons",
          "Kejelasan bahasa",
          "Waktu penyelesaian",
          "Pemahaman masalah",
          "Solusi yang diberikan"
        ],
        "show_other_reason": true,
        "title": "Penilaian Tokopedia Care dari Anda",
        "trigger_rule_type": "chatbot"
      },
      "fallback_attachment": {
        "html": "<div></div>"
      },
      "id": 1,
      "type": 13
    },
    "show_rating": false,
    "rating_status": 0,
    "to_buyer": true,
    "client_connect_time": "0001-01-01T00:00:00Z",
    "source": ""
  }
}
    """

    const val ATTACHMENT_14_UPDATE_TOOLBAR = """
        {
  "type": "",
  "code": 103,
  "data": {
    "msg_id": 4058088,
    "from": "Tanya",
    "from_uid": 5515973,
    "from_user_name": "Tanya",
    "from_role": "User",
    "thumbnail": "https://images.tokopedia.net/img/cache/100-square/user-1/2020/8/13/5515973/5515973_bf39448d-d1bd-410b-9e7b-b5acdf54555a.png",
    "is_bot": true,
    "reminder_ticker": null,
    "is_opposite": true,
    "to_uid": 9120466,
    "message": {
      "censored_reply": "",
      "original_reply": "",
      "timestamp": "2022-09-30T13:09:32.614342+07:00",
      "timestamp_fmt": "30 September 2022, 13:09 WIB",
      "timestamp_unix": 1664518172614,
      "timestamp_unix_nano": 1664518172614342000
    },
    "start_time": "2022-09-30T13:09:32.614342+07:00",
    "attachment_id": 1,
    "attachment": {
      "attributes": {
        "agent_type": "bot",
        "badge_image": {
          "dark": "",
          "light": ""
        },
        "profile_image": "https://images.tokopedia.net/img/chatbot/tanya.png",
        "profile_image_dark": "https://images.tokopedia.net/img/chatbot/tanya-dark.png",
        "profile_name": "TANYA"
      },
      "fallback_attachment": {
        "html": "<div></div>"
      },
      "id": 1,
      "type": 14
    },
    "show_rating": false,
    "rating_status": 0,
    "to_buyer": true,
    "client_connect_time": "0001-01-01T00:00:00Z",
    "source": ""
  }
}
    """

    const val ATTACHMENT_14_UPDATE_TOOLBAR_ATTACHMENT_NULL = """
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
      "censored_reply": "",
      "original_reply": "",
      "timestamp": "2022-09-30T13:09:32.614342+07:00",
      "timestamp_fmt": "30 September 2022, 13:09 WIB",
      "timestamp_unix": 1664518172614,
      "timestamp_unix_nano": 1664518172614342000
    },
    "start_time": "2022-09-30T13:09:32.614342+07:00",
    "attachment_id": 1,
    "attachment": null,
    "show_rating": false,
    "rating_status": 0,
    "to_buyer": true,
    "client_connect_time": "0001-01-01T00:00:00Z",
    "source": ""
  }
}
    """

    const val ATTACHMENT_31_SESSION_CHANGE_WITH_MODE_AGENT = """
        {
  "type": "",
  "code": 103,
  "data": {
    "msg_id": 4058088,
    "from": "Tanya",
    "from_uid": 5515973,
    "from_user_name": "Tanya",
    "from_role": "User",
    "thumbnail": "https://images.tokopedia.net/img/cache/100-square/user-1/2020/8/13/5515973/5515973_bf39448d-d1bd-410b-9e7b-b5acdf54555a.png",
    "is_bot": true,
    "reminder_ticker": null,
    "is_opposite": true,
    "to_uid": 9120466,
    "message": {
      "censored_reply": "",
      "original_reply": "",
      "timestamp": "2022-09-30T13:09:42.943512423+07:00",
      "timestamp_fmt": "30 September 2022, 13:09 WIB",
      "timestamp_unix": 1664518182943,
      "timestamp_unix_nano": 1664518182943512000
    },
    "start_time": "2022-09-30T13:09:42.943512423+07:00",
    "attachment_id": 1,
    "attachment": {
      "attributes": {
        "session_change": {
          "mode": "agent",
          "session_id": "4058088_1664518172"
        }
      },
      "fallback_attachment": {
        "html": "<div></div>"
      },
      "id": 1,
      "type": 31
    },
    "show_rating": false,
    "rating_status": 0,
    "to_buyer": true,
    "client_connect_time": "0001-01-01T00:00:00Z",
    "source": ""
  }
}
    """

    const val ATTACHMENT_31_SESSION_CHANGE_WITH_MODE_BOT = """
        {
  "type": "",
  "code": 103,
  "data": {
    "msg_id": 4058088,
    "from": "Tanya",
    "from_uid": 5515973,
    "from_user_name": "Tanya",
    "from_role": "User",
    "thumbnail": "https://images.tokopedia.net/img/cache/100-square/user-1/2020/8/13/5515973/5515973_bf39448d-d1bd-410b-9e7b-b5acdf54555a.png",
    "is_bot": true,
    "reminder_ticker": null,
    "is_opposite": true,
    "to_uid": 9120466,
    "message": {
      "censored_reply": "",
      "original_reply": "",
      "timestamp": "2022-09-30T13:09:42.943512423+07:00",
      "timestamp_fmt": "30 September 2022, 13:09 WIB",
      "timestamp_unix": 1664518182943,
      "timestamp_unix_nano": 1664518182943512000
    },
    "start_time": "2022-09-30T13:09:42.943512423+07:00",
    "attachment_id": 1,
    "attachment": {
      "attributes": {
        "session_change": {
          "mode": "bot",
          "session_id": "4058088_1664518172"
        }
      },
      "fallback_attachment": {
        "html": "<div></div>"
      },
      "id": 1,
      "type": 31
    },
    "show_rating": false,
    "rating_status": 0,
    "to_buyer": true,
    "client_connect_time": "0001-01-01T00:00:00Z",
    "source": ""
  }
}
    """

    const val ATTACHMENT_31_SESSION_CHANGE_WITH_MODE_UNKNOWN = """
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
      "censored_reply": "",
      "original_reply": "",
      "timestamp": "2022-09-30T13:09:42.943512423+07:00",
      "timestamp_fmt": "30 September 2022, 13:09 WIB",
      "timestamp_unix": 1664518182943,
      "timestamp_unix_nano": 1664518182943512000
    },
    "start_time": "2022-09-30T13:09:42.943512423+07:00",
    "attachment_id": 1,
    "attachment": {
      "attributes": {
        "session_change": {
          "mode": "unknown",
          "session_id": "4058088_1664518172"
        }
      },
      "fallback_attachment": {
        "html": "<div></div>"
      },
      "id": 1,
      "type": 31
    },
    "show_rating": false,
    "rating_status": 0,
    "to_buyer": true,
    "client_connect_time": "0001-01-01T00:00:00Z",
    "source": ""
  }
}
    """

    const val ATTACHMENT_15_CHAT_DIVIDER = """
        {
  "type": "",
  "code": 103,
  "data": {
    "msg_id": 4058088,
    "from": "Tanya",
    "from_uid": 5515973,
    "from_user_name": "Tanya",
    "from_role": "User",
    "thumbnail": "https://images.tokopedia.net/img/cache/100-square/user-1/2020/8/13/5515973/5515973_bf39448d-d1bd-410b-9e7b-b5acdf54555a.png",
    "is_bot": true,
    "reminder_ticker": null,
    "is_opposite": true,
    "to_uid": 9120466,
    "message": {
      "censored_reply": "",
      "original_reply": "",
      "timestamp": "2022-09-30T13:09:33.270552+07:00",
      "timestamp_fmt": "30 September 2022, 13:09 WIB",
      "timestamp_unix": 1664518173270,
      "timestamp_unix_nano": 1664518173270552000
    },
    "start_time": "2022-09-30T13:09:33.270552+07:00",
    "attachment_id": 1,
    "attachment": {
      "attributes": {
        "devider": {
          "label": "CFS kami akan membalas chat kamu sekitar 10 menit kedepan. Mohon tunggu ya"
        }
      },
      "fallback_attachment": {
        "html": "<div></div>"
      },
      "id": 1,
      "type": 15
    },
    "show_rating": false,
    "rating_status": 0,
    "to_buyer": true,
    "notifications": [
      {
        "subject": "Tokopedia Care",
        "description": "CFS kami akan membalas chat kamu sekitar 10 menit kedepan. Mohon tunggu ya"
      }
    ],
    "client_connect_time": "0001-01-01T00:00:00Z",
    "source": ""
  }
}
    """

    const val DYNAMIC_ATTACHMENT_CODE_100_IS_ACTIVE_TRUE_RENDER_ANDROID = """
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
      "censored_reply": "",
      "original_reply": "",
      "timestamp": "2022-11-02T15:26:29.042566105+07:00",
      "timestamp_fmt": "02 November 2022, 15:26 WIB",
      "timestamp_unix": 1667377589042,
      "timestamp_unix_nano": 1667377589042566000
    },
    "start_time": "2020-07-23T15:59:44.997841231+07:00",
    "attachment_id": 1,
    "attachment": {
      "attributes": {
        "dynamic_attachment": {
          "attribute": {
            "content_code": 100,
            "dynamic_content": "{\"isActive\": true,\"placeholder\": \"Saya mau belanja dan coba pakai promo tapi muncul error kode promo tidak berlaku, padahal promo sudah sesuai s\u0026k.\",\"title\": \"Ceritakan kendalamu disini, ya\"}",
            "render_target": "android",
            "user_id": 5480593
          },
          "fallback": {
            "html": "\u003cdiv\u003eFallback\u003c/div\u003e",
            "message": "Fallback"
          },
          "is_log_history": true
        }
      },
      "fallback_attachment": {
        "html": "\u003cdiv\u003e\u003c/div\u003e"
      },
      "id": 1,
      "type": 34
    },
    "show_rating": false,
    "rating_status": 0,
    "to_buyer": true,
    "client_connect_time": "0001-01-01T00:00:00Z",
    "source": ""
  }
}
    """

    const val DYNAMIC_ATTACHMENT_CODE_100_IS_ACTIVE_FALSE_RENDER_ANDROID = """
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
      "censored_reply": "",
      "original_reply": "",
      "timestamp": "2022-11-02T15:26:29.042566105+07:00",
      "timestamp_fmt": "02 November 2022, 15:26 WIB",
      "timestamp_unix": 1667377589042,
      "timestamp_unix_nano": 1667377589042566000
    },
    "start_time": "2020-07-23T15:59:44.997841231+07:00",
    "attachment_id": 1,
    "attachment": {
      "attributes": {
        "dynamic_attachment": {
          "attribute": {
            "content_code": 100,
            "dynamic_content": "{\"isActive\": false,\"placeholder\": \"Saya mau belanja dan coba pakai promo tapi muncul error kode promo tidak berlaku, padahal promo sudah sesuai s\u0026k.\",\"title\": \"Ceritakan kendalamu disini, ya\"}",
            "render_target": "android",
            "user_id": 5480593
          },
          "fallback": {
            "html": "\u003cdiv\u003eFallback\u003c/div\u003e",
            "message": "Fallback"
          },
          "is_log_history": true
        }
      },
      "fallback_attachment": {
        "html": "\u003cdiv\u003e\u003c/div\u003e"
      },
      "id": 1,
      "type": 34
    },
    "show_rating": false,
    "rating_status": 0,
    "to_buyer": true,
    "client_connect_time": "0001-01-01T00:00:00Z",
    "source": ""
  }
}
    """

    const val DYNAMIC_ATTACHMENT_CODE_100_IS_ACTIVE_FALSE_RENDER_IOS = """
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
      "censored_reply": "",
      "original_reply": "",
      "timestamp": "2022-11-02T15:26:29.042566105+07:00",
      "timestamp_fmt": "02 November 2022, 15:26 WIB",
      "timestamp_unix": 1667377589042,
      "timestamp_unix_nano": 1667377589042566000
    },
    "start_time": "2020-07-23T15:59:44.997841231+07:00",
    "attachment_id": 1,
    "attachment": {
      "attributes": {
        "dynamic_attachment": {
          "attribute": {
            "content_code": 100,
            "dynamic_content": "{\"isActive\": false,\"placeholder\": \"Saya mau belanja dan coba pakai promo tapi muncul error kode promo tidak berlaku, padahal promo sudah sesuai s\u0026k.\",\"title\": \"Ceritakan kendalamu disini, ya\"}",
            "render_target": "ios",
            "user_id": 5480593
          },
          "fallback": {
            "html": "\u003cdiv\u003eFallback\u003c/div\u003e",
            "message": "Fallback"
          },
          "is_log_history": true
        }
      },
      "fallback_attachment": {
        "html": "\u003cdiv\u003e\u003c/div\u003e"
      },
      "id": 1,
      "type": 34
    },
    "show_rating": false,
    "rating_status": 0,
    "to_buyer": true,
    "client_connect_time": "0001-01-01T00:00:00Z",
    "source": ""
  }
}
    """

    const val DYNAMIC_ATTACHMENT_ATTACHMENT_NULL = """
{
  "type": "",
   "code":103,
   "data":{
      "msg_id":4058088,
      "from":"Tanya",
      "from_uid":5515973,
      "from_user_name":"Tanya",
      "from_role":"User",
      "thumbnail":"https://ecs7.tokopedia.net/img/cache/100-square/user-1/2020/8/13/5515973/5515973_bf39448d-d1bd-410b-9e7b-b5acdf54555a.png",
      "is_bot":true,
      "reminder_ticker":null,
      "is_opposite":true,
      "to_uid":9120466,
      "message":{
         "censored_reply":"",
         "original_reply":"",
         "timestamp":"2022-11-02T15:26:29.042566105+07:00",
         "timestamp_fmt":"02 November 2022, 15:26 WIB",
         "timestamp_unix":1667377589042,
         "timestamp_unix_nano":1667377589042566000
      },
      "start_time":"2020-07-23T15:59:44.997841231+07:00",
      "attachment_id":1,
      "attachment":null,
      "fallback_attachment":{
         "html":"\u003cdiv\u003e\u003c/div\u003e"
      },
      "id":1,
      "type":34
   },
   "show_rating":false,
   "rating_status":0,
   "to_buyer":true,
   "client_connect_time":"0001-01-01T00:00:00Z",
   "source":""
}
    """

    const val DYNAMIC_ATTACHMENT_DYNAMIC_ATTACHMENT_IS_NULL = """
{
  "type": "",
   "code":103,
   "data":{
      "msg_id":4058088,
      "from":"Tanya",
      "from_uid":5515973,
      "from_user_name":"Tanya",
      "from_role":"User",
      "thumbnail":"https://ecs7.tokopedia.net/img/cache/100-square/user-1/2020/8/13/5515973/5515973_bf39448d-d1bd-410b-9e7b-b5acdf54555a.png",
      "is_bot":true,
      "reminder_ticker":null,
      "is_opposite":true,
      "to_uid":9120466,
      "message":{
         "censored_reply":"",
         "original_reply":"",
         "timestamp":"2022-11-02T15:26:29.042566105+07:00",
         "timestamp_fmt":"02 November 2022, 15:26 WIB",
         "timestamp_unix":1667377589042,
         "timestamp_unix_nano":1667377589042566000
      },
      "start_time":"2020-07-23T15:59:44.997841231+07:00",
      "attachment_id":1,
      "attachment":{
         "attributes":{
            "dynamic_attachment":null
         },
         "fallback_attachment":{
            "html":"\u003cdiv\u003e\u003c/div\u003e"
         },
         "id":1,
         "type":34
      },
      "show_rating":false,
      "rating_status":0,
      "to_buyer":true,
      "client_connect_time":"0001-01-01T00:00:00Z",
      "source":""
   }
}
    """

    const val DYNAMIC_ATTACHMENT_DYNAMIC_ATTACHMENT_ATTRIBUTE_DYNAMIC_CONTENT_IS_NULL = """
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
      "censored_reply": "",
      "original_reply": "",
      "timestamp": "2022-11-02T15:26:29.042566105+07:00",
      "timestamp_fmt": "02 November 2022, 15:26 WIB",
      "timestamp_unix": 1667377589042,
      "timestamp_unix_nano": 1667377589042566000
    },
    "start_time": "2020-07-23T15:59:44.997841231+07:00",
    "attachment_id": 1,
    "attachment": {
      "attributes": {
        "dynamic_attachment": {
          "attribute": {
            "content_code": 100,
            "dynamic_content": null,
            "render_target": "android",
            "user_id": 5480593
          },
          "fallback": {
            "html": "\u003cdiv\u003eFallback\u003c/div\u003e",
            "message": "Fallback"
          },
          "is_log_history": true
        }
      },
      "fallback_attachment": {
        "html": "\u003cdiv\u003e\u003c/div\u003e"
      },
      "id": 1,
      "type": 34
    },
    "show_rating": false,
    "rating_status": 0,
    "to_buyer": true,
    "client_connect_time": "0001-01-01T00:00:00Z",
    "source": ""
  }
}
    """

    const val DYNAMIC_ATTACHMENT_DYNAMIC_ATTACHMENT_ATTRIBUTE_IS_NULL = """
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
      "censored_reply": "",
      "original_reply": "",
      "timestamp": "2022-11-02T15:26:29.042566105+07:00",
      "timestamp_fmt": "02 November 2022, 15:26 WIB",
      "timestamp_unix": 1667377589042,
      "timestamp_unix_nano": 1667377589042566000
    },
    "start_time": "2020-07-23T15:59:44.997841231+07:00",
    "attachment_id": 1,
    "attachment": {
      "attributes": {
        "dynamic_attachment": {
          "attribute": null,
          "fallback": {
            "html": "\u003cdiv\u003eFallback\u003c/div\u003e",
            "message": "Fallback"
          },
          "is_log_history": true
        }
      },
      "fallback_attachment": {
        "html": "\u003cdiv\u003e\u003c/div\u003e"
      },
      "id": 1,
      "type": 34
    },
    "show_rating": false,
    "rating_status": 0,
    "to_buyer": true,
    "client_connect_time": "0001-01-01T00:00:00Z",
    "source": ""
  }
}
    """

    const val DYNAMIC_ATTACHMENT_CODE_101_RENDER_ANDROID_HIDDEN_TRUE = """
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
      "censored_reply": "",
      "original_reply": "",
      "timestamp": "2022-11-02T18:21:31.89069273+07:00",
      "timestamp_fmt": "02 November 2022, 18:21 WIB",
      "timestamp_unix": 1667388091890,
      "timestamp_unix_nano": 1667388091890693000
    },
    "start_time": "2020-07-23T15:59:44.997841231+07:00",
    "attachment_id": 1,
    "attachment": {
      "attributes": {
        "dynamic_attachment": {
          "attribute": {
            "content_code": 101,
            "dynamic_content": "{\"isHidden\":true}",
            "render_target": "android",
            "user_id": 5480593
          },
          "fallback": {
            "html": "\u003cdiv\u003eFallback\u003c/div\u003e",
            "message": "Fallback"
          },
          "is_log_history": true
        }
      },
      "fallback_attachment": {
        "html": "\u003cdiv\u003e\u003c/div\u003e"
      },
      "id": 1,
      "type": 34
    },
    "show_rating": false,
    "rating_status": 0,
    "to_buyer": true,
    "client_connect_time": "0001-01-01T00:00:00Z",
    "source": ""
  }
}
    """

    const val DYNAMIC_ATTACHMENT_CODE_101_RENDER_ANDROID_HIDDEN_FALSE = """
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
      "censored_reply": "",
      "original_reply": "",
      "timestamp": "2022-11-02T18:21:31.89069273+07:00",
      "timestamp_fmt": "02 November 2022, 18:21 WIB",
      "timestamp_unix": 1667388091890,
      "timestamp_unix_nano": 1667388091890693000
    },
    "start_time": "2020-07-23T15:59:44.997841231+07:00",
    "attachment_id": 1,
    "attachment": {
      "attributes": {
        "dynamic_attachment": {
          "attribute": {
            "content_code": 101,
            "dynamic_content": "{\"isHidden\":false}",
            "render_target": "android",
            "user_id": 5480593
          },
          "fallback": {
            "html": "\u003cdiv\u003eFallback\u003c/div\u003e",
            "message": "Fallback"
          },
          "is_log_history": true
        }
      },
      "fallback_attachment": {
        "html": "\u003cdiv\u003e\u003c/div\u003e"
      },
      "id": 1,
      "type": 34
    },
    "show_rating": false,
    "rating_status": 0,
    "to_buyer": true,
    "client_connect_time": "0001-01-01T00:00:00Z",
    "source": ""
  }
}
    """

    const val DYNAMIC_ATTACHMENT_CODE_101_RENDER_IOS = """
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
      "censored_reply": "",
      "original_reply": "",
      "timestamp": "2022-11-02T18:21:31.89069273+07:00",
      "timestamp_fmt": "02 November 2022, 18:21 WIB",
      "timestamp_unix": 1667388091890,
      "timestamp_unix_nano": 1667388091890693000
    },
    "start_time": "2020-07-23T15:59:44.997841231+07:00",
    "attachment_id": 1,
    "attachment": {
      "attributes": {
        "dynamic_attachment": {
          "attribute": {
            "content_code": 101,
            "dynamic_content": "{\"isHidden\":true}",
            "render_target": "ios",
            "user_id": 5480593
          },
          "fallback": {
            "html": "\u003cdiv\u003eFallback\u003c/div\u003e",
            "message": "Fallback"
          },
          "is_log_history": true
        }
      },
      "fallback_attachment": {
        "html": "\u003cdiv\u003e\u003c/div\u003e"
      },
      "id": 1,
      "type": 34
    },
    "show_rating": false,
    "rating_status": 0,
    "to_buyer": true,
    "client_connect_time": "0001-01-01T00:00:00Z",
    "source": ""
  }
}
    """

    const val DYNAMIC_ATTACHMENT_CODE_101_DYNAMIC_CONTENT_NULL = """
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
      "censored_reply": "",
      "original_reply": "",
      "timestamp": "2022-11-02T18:21:31.89069273+07:00",
      "timestamp_fmt": "02 November 2022, 18:21 WIB",
      "timestamp_unix": 1667388091890,
      "timestamp_unix_nano": 1667388091890693000
    },
    "start_time": "2020-07-23T15:59:44.997841231+07:00",
    "attachment_id": 1,
    "attachment": {
      "attributes": {
        "dynamic_attachment": {
          "attribute": {
            "content_code": 101,
            "dynamic_content": null,
            "render_target": "android",
            "user_id": 5480593
          },
          "fallback": {
            "html": "\u003cdiv\u003eFallback\u003c/div\u003e",
            "message": "Fallback"
          },
          "is_log_history": true
        }
      },
      "fallback_attachment": {
        "html": "\u003cdiv\u003e\u003c/div\u003e"
      },
      "id": 1,
      "type": 34
    },
    "show_rating": false,
    "rating_status": 0,
    "to_buyer": true,
    "client_connect_time": "0001-01-01T00:00:00Z",
    "source": ""
  }
}
    """

    const val DYNAMIC_ATTACHMENT_CODE_102 = """
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
      "censored_reply": "",
      "original_reply": "",
      "timestamp": "2022-11-02T18:21:31.89069273+07:00",
      "timestamp_fmt": "02 November 2022, 18:21 WIB",
      "timestamp_unix": 1667388091890,
      "timestamp_unix_nano": 1667388091890693000
    },
    "start_time": "2020-07-23T15:59:44.997841231+07:00",
    "attachment_id": 1,
    "attachment": {
      "attributes": {
        "dynamic_attachment": {
          "attribute": {
            "content_code": 102,
            "dynamic_content": "{\"isHidden\":true}",
            "render_target": "android",
            "user_id": 5480593
          },
          "fallback": {
            "html": "\u003cdiv\u003eFallback\u003c/div\u003e",
            "message": "Fallback"
          },
          "is_log_history": true
        }
      },
      "fallback_attachment": {
        "html": "\u003cdiv\u003e\u003c/div\u003e"
      },
      "id": 1,
      "type": 34
    },
    "show_rating": false,
    "rating_status": 0,
    "to_buyer": true,
    "client_connect_time": "0001-01-01T00:00:00Z",
    "source": ""
  }
}
    """

    const val ATTACHMENT_NOT_AVAILABLE_RIGHT_NOW_SHOW_FALLBACK = """
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
      "censored_reply": "",
      "original_reply": "",
      "timestamp": "2022-11-02T18:21:31.89069273+07:00",
      "timestamp_fmt": "02 November 2022, 18:21 WIB",
      "timestamp_unix": 1667388091890,
      "timestamp_unix_nano": 1667388091890693000
    },
    "start_time": "2020-07-23T15:59:44.997841231+07:00",
    "attachment_id": 1,
    "attachment": {
      "attributes": {
        "dynamic_attachment": {
          "attribute": {
            "content_code": 102,
            "dynamic_content": "{\"isHidden\":true}",
            "render_target": "android",
            "user_id": 5480593
          },
          "fallback": {
            "html": "\u003cdiv\u003eFallback\u003c/div\u003e",
            "message": "Fallback"
          },
          "is_log_history": true
        }
      },
      "fallback_attachment": {
        "html": "\u003cdiv\u003e\u003c/div\u003e"
      },
      "id": 1,
      "type": 600
    },
    "show_rating": false,
    "rating_status": 0,
    "to_buyer": true,
    "client_connect_time": "0001-01-01T00:00:00Z",
    "source": ""
  }
}
    """

    const val JSON_NOT_FORMATTED_THROWS_EXCEPTION = """
{
   "type": "",
  "code": 103,
  "data": { {
    "msg_id": 4058088
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
      "censored_reply": "",
      "original_reply": "",
      "timestamp": "2022-11-02T18:21:31.89069273+07:00",
      "timestamp_fmt": "02 November 2022, 18:21 WIB",
      "timestamp_unix": 1667388091890,
      "timestamp_unix_nano": 1667388091890693000
    },
    "start_time": "2020-07-23T15:59:44.997841231+07:00",
    "attachment_id": 1,
    "attachment": {
      "attributes": {
        "dynamic_attachment": {
          "attribute": {
            "content_code": 102,
            "dynamic_content": "{\"isHidden\":true}",
            "render_target": "android",
            "user_id": 5480593
          },
          "fallback": {
            "html": "\u003cdiv\u003eFallback\u003c/div\u003e",
            "message": "Fallback"
          },
          "is_log_history": true
        }
      },
      "fallback_attachment": {
        "html": "\u003cdiv\u003e\u003c/div\u003e"
      },
      "id": 1,
      "type": 600
    },
    "show_rating": false,
    "rating_status": 0,
    "to_buyer": true,
    "client_connect_time": "0001-01-01T00:00:00Z",
    "source": ""
  }
}
    """

    fun getResponse(response: String): ChatWebSocketResponse {
        return GsonBuilder().create()
            .fromJson(
                response,
                ChatWebSocketResponse::class.java
            )
    }

    fun getOldResponse(response: String): com.tokopedia.chatbot.websocket.ChatWebSocketResponse {
        return GsonBuilder().create()
            .fromJson(
                response,
                com.tokopedia.chatbot.websocket.ChatWebSocketResponse::class.java
            )
    }
}
