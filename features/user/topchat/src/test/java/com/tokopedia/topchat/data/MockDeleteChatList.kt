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

}