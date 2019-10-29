package com.tokopedia.topchat.data

object MockDeleteChatList {

    val chatMoveToTrash = """
        {
          "data": {
            "chatMoveToTrash": {
              "list": [
                {
                  "IsSuccess": 1,
                  "DetailResponse": "Success",
                  "MsgID": 772426858
                }
              ]
            }
          }
        }
    """.trimIndent()

    val failChatMoveToTrash = """
        {
            "data": {
                "chatMoveToTrash": null
              },
              "errors": [
                {
                  "message": "Maaf, Permohonan Anda tidak dapat diproses saat ini. Mohon dicoba kembali.",
                  "path": [
                    "chatMoveToTrash"
                  ],
                  "extensions": {}
                }
              ]
        }
    """.trimIndent()

    val listOfChatMessage = """
        {
          "data": {
            "chatListMessage": {
              "list": [
                {
                  "msgID": 801710918,
                  "messageKey": "user-23824753~user-49032181",
                  "attributes": {
                    "contact": {
                      "id": 23824753,
                      "role": "Tokopedia Administrator",
                      "domain": "",
                      "name": "Tokopedia Info",
                      "shopStatus": 0,
                      "tag": "Official",
                      "thumbnail": "https://accounts.tokopedia.com/image/v1/u/23824753/user_thumbnail/mobile"
                    },
                    "lastReplyMessage": "Halo toppers!",
                    "readStatus": 1,
                    "unreads": 1,
                    "fraudStatus": 0
                  }
                },
                {
                  "msgID": 812174863,
                  "messageKey": "user-6976645~shop-6543079",
                  "attributes": {
                    "contact": {
                      "id": 6976645,
                      "role": "user",
                      "domain": "",
                      "name": "Ade Fulki Hadian",
                      "shopStatus": 0,
                      "tag": "Pengguna",
                      "thumbnail": "https://accounts.tokopedia.com/image/v1/u/6976645/user_thumbnail/mobile"
                    },
                    "lastReplyMessage": "Bisa dikirim hari ini ga? Terima kasih!",
                    "readStatus": 2,
                    "unreads": 0,
                    "fraudStatus": 0
                  }
                },
                {
                  "msgID": 772456122,
                  "messageKey": "user-49032181~shop-4471375",
                  "attributes": {
                    "contact": {
                      "id": 4471375,
                      "role": "shop",
                      "domain": "nillkinstoree888",
                      "name": "Nillkin Store 888",
                      "shopStatus": 1,
                      "tag": "Penjual",
                      "thumbnail": "https://imagerouter.tokopedia.com/image/v1/s/4471375/shop_xs_thumbnail/desktop"
                    },
                    "lastReplyMessage": "ok, saya coba",
                    "readStatus": 2,
                    "unreads": 0,
                    "fraudStatus": 0
                  }
                }
              ],
              "hasNext": true,
              "pagingNext": true,
              "showTimeMachine": 0
            }
          }
        }
    """.trimIndent()
}