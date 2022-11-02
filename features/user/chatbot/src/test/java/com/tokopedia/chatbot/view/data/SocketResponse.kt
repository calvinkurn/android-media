package com.tokopedia.chatbot.view.data

import com.google.gson.GsonBuilder
import com.tokopedia.websocket.WebSocketResponse

object SocketResponse {

    const val DYNAMIC_ATTACHMENT_CODE_100_IS_ACTIVE_TRUE_RENDER_ANDROID = """
{
  "code": 103,
  "data": {
    "msg_id": 3181924,
    "from": "Tanya",
    "from_uid": 5515973,
    "from_user_name": "Tanya",
    "from_role": "User",
    "thumbnail": "https://ecs7.tokopedia.net/img/cache/100-square/user-1/2020/8/13/5515973/5515973_bf39448d-d1bd-410b-9e7b-b5acdf54555a.png",
    "is_bot": true,
    "reminder_ticker": null,
    "is_opposite": true,
    "to_uid": 5480593,
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
  "code": 103,
  "data": {
    "msg_id": 3181924,
    "from": "Tanya",
    "from_uid": 5515973,
    "from_user_name": "Tanya",
    "from_role": "User",
    "thumbnail": "https://ecs7.tokopedia.net/img/cache/100-square/user-1/2020/8/13/5515973/5515973_bf39448d-d1bd-410b-9e7b-b5acdf54555a.png",
    "is_bot": true,
    "reminder_ticker": null,
    "is_opposite": true,
    "to_uid": 5480593,
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
  "code": 103,
  "data": {
    "msg_id": 3181924,
    "from": "Tanya",
    "from_uid": 5515973,
    "from_user_name": "Tanya",
    "from_role": "User",
    "thumbnail": "https://ecs7.tokopedia.net/img/cache/100-square/user-1/2020/8/13/5515973/5515973_bf39448d-d1bd-410b-9e7b-b5acdf54555a.png",
    "is_bot": true,
    "reminder_ticker": null,
    "is_opposite": true,
    "to_uid": 5480593,
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
   "code":103,
   "data":{
      "msg_id":3181924,
      "from":"Tanya",
      "from_uid":5515973,
      "from_user_name":"Tanya",
      "from_role":"User",
      "thumbnail":"https://ecs7.tokopedia.net/img/cache/100-square/user-1/2020/8/13/5515973/5515973_bf39448d-d1bd-410b-9e7b-b5acdf54555a.png",
      "is_bot":true,
      "reminder_ticker":null,
      "is_opposite":true,
      "to_uid":5480593,
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
   "code":103,
   "data":{
      "msg_id":3181924,
      "from":"Tanya",
      "from_uid":5515973,
      "from_user_name":"Tanya",
      "from_role":"User",
      "thumbnail":"https://ecs7.tokopedia.net/img/cache/100-square/user-1/2020/8/13/5515973/5515973_bf39448d-d1bd-410b-9e7b-b5acdf54555a.png",
      "is_bot":true,
      "reminder_ticker":null,
      "is_opposite":true,
      "to_uid":5480593,
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
  "code": 103,
  "data": {
    "msg_id": 3181924,
    "from": "Tanya",
    "from_uid": 5515973,
    "from_user_name": "Tanya",
    "from_role": "User",
    "thumbnail": "https://ecs7.tokopedia.net/img/cache/100-square/user-1/2020/8/13/5515973/5515973_bf39448d-d1bd-410b-9e7b-b5acdf54555a.png",
    "is_bot": true,
    "reminder_ticker": null,
    "is_opposite": true,
    "to_uid": 5480593,
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
  "code": 103,
  "data": {
    "msg_id": 3181924,
    "from": "Tanya",
    "from_uid": 5515973,
    "from_user_name": "Tanya",
    "from_role": "User",
    "thumbnail": "https://ecs7.tokopedia.net/img/cache/100-square/user-1/2020/8/13/5515973/5515973_bf39448d-d1bd-410b-9e7b-b5acdf54555a.png",
    "is_bot": true,
    "reminder_ticker": null,
    "is_opposite": true,
    "to_uid": 5480593,
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
  "code": 103,
  "data": {
    "msg_id": 3181924,
    "from": "Tanya",
    "from_uid": 5515973,
    "from_user_name": "Tanya",
    "from_role": "User",
    "thumbnail": "https://ecs7.tokopedia.net/img/cache/100-square/user-1/2020/8/13/5515973/5515973_bf39448d-d1bd-410b-9e7b-b5acdf54555a.png",
    "is_bot": true,
    "reminder_ticker": null,
    "is_opposite": true,
    "to_uid": 5480593,
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
  "code": 103,
  "data": {
    "msg_id": 3181924,
    "from": "Tanya",
    "from_uid": 5515973,
    "from_user_name": "Tanya",
    "from_role": "User",
    "thumbnail": "https://ecs7.tokopedia.net/img/cache/100-square/user-1/2020/8/13/5515973/5515973_bf39448d-d1bd-410b-9e7b-b5acdf54555a.png",
    "is_bot": true,
    "reminder_ticker": null,
    "is_opposite": true,
    "to_uid": 5480593,
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
  "code": 103,
  "data": {
    "msg_id": 3181924,
    "from": "Tanya",
    "from_uid": 5515973,
    "from_user_name": "Tanya",
    "from_role": "User",
    "thumbnail": "https://ecs7.tokopedia.net/img/cache/100-square/user-1/2020/8/13/5515973/5515973_bf39448d-d1bd-410b-9e7b-b5acdf54555a.png",
    "is_bot": true,
    "reminder_ticker": null,
    "is_opposite": true,
    "to_uid": 5480593,
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
  "code": 103,
  "data": {
    "msg_id": 3181924,
    "from": "Tanya",
    "from_uid": 5515973,
    "from_user_name": "Tanya",
    "from_role": "User",
    "thumbnail": "https://ecs7.tokopedia.net/img/cache/100-square/user-1/2020/8/13/5515973/5515973_bf39448d-d1bd-410b-9e7b-b5acdf54555a.png",
    "is_bot": true,
    "reminder_ticker": null,
    "is_opposite": true,
    "to_uid": 5480593,
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
  "code": 103,
  "data": {
    "msg_id": 3181924,
    "from": "Tanya",
    "from_uid": 5515973,
    "from_user_name": "Tanya",
    "from_role": "User",
    "thumbnail": "https://ecs7.tokopedia.net/img/cache/100-square/user-1/2020/8/13/5515973/5515973_bf39448d-d1bd-410b-9e7b-b5acdf54555a.png",
    "is_bot": true,
    "reminder_ticker": null,
    "is_opposite": true,
    "to_uid": 5480593,
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

    fun getResponse(response: String): WebSocketResponse {
        return GsonBuilder().create()
            .fromJson<WebSocketResponse>(
                response,
                WebSocketResponse::class.java
            )
    }

}
