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

    val notRecognizedEvent = """
          {
            "code": 999,
            "data": {
              "msg_id": 0,
              "reply_time": 1638958002690827000
            }
          }
    """.trimIndent()

    fun generateReplyInvoice(
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
            "attachment_id": 1,
            "attachment": {
              "attributes": {
                "invoice_link": {
                  "attributes": {
                    "code": "INV/20211111/MPL/1753681791",
                    "create_time": "11 Nov 2021",
                    "href_url": "https://www.tokopedia.com/invoice?id=INV%2F20211111%2FMPL%2F1753681791&source=som",
                    "id": 994263470,
                    "image_url": "https://ecs7.tokopedia.net/img/cache/100-square/VqbcmM/2021/11/9/550b485e-6f0d-4e8f-8616-5e5fb125513b.jpg",
                    "status": "Pesanan Dibatalkan",
                    "status_id": 0,
                    "title": "g4m134r g4m134r",
                    "total_amount": "Rp 123"
                  },
                  "type": "marketplace",
                  "type_id": 1
                }
              },
              "fallback_attachment": {
                "html": "<div>https://www.tokopedia.com/invoice?id=INV%2F20211111%2FMPL%2F1753681791&source=som</div>",
                "message": "https://www.tokopedia.com/invoice?id=INV%2F20211111%2FMPL%2F1753681791&source=som"
              },
              "id": 1,
              "type": 7
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