package com.tokopedia.notifications

val generalResponse = """
    {
      "icon": "",
      "sound": "",
      "notificationId": "1",
      "source": "toko-cm",
      "tribe": "13829",
      "notificationType": "General",
      "userId": "5513570",
      "shopId": "",
      "transId": "MTU4NzEwNTE0MTQyMjI3Njg3OCMxNTY4MyMwIzU1MTM1NzAjdHJ1ZQ==:1",
      "userTransId": "MTU4NzEwNTE0MTQyMjI3Njg3OCMxNTY4MyMwIzU1MTM1NzAjdHJ1ZQ==",
      "notifcenterBlastId": "",
      "channel": "",
      "title": "Title 3",
      "desc": "Message 3",
      "message": "Message 3",
      "appLink": "tokopedia://referral",
      "collapsedImg": "",
      "expandedImg": "",
      "webhook_params": "{\"nc_notif_id\":\"\",\"nc_type_of_notif\":0}",
      "campaignUserToken": "MTU4NzEwNTE0MTQyMjI3Njg3OCMxNTY4MyMwIzU1MTM1NzAjdHJ1ZQ==:1",
      "campaignId": 15683,
      "parentId": 13829
    }
""".trimIndent()

val testResponse = arrayListOf(
        """
            {
                "icon": "",
                "sound": "",
                "notificationId": "-1",
                "source": "toko-cm",
                "tribe": "111",
                "notificationType": "General",
                "userId": "8966870",
                "shopId": "",
                "transId": "MTU4ODU2OTYwNzI1NjM4OTIwMyMxMTEjMCM4OTY2ODcwI3RydWUjMTEx:3",
                "userTransId": "MTU4ODU2OTYwNzI1NjM4OTIwMyMxMTEjMCM4OTY2ODcwI3RydWUjMTEx",
                "notifcenterBlastId": "",
                "channel": "",
                "title": "Halo Toppers",
                "desc": "Cek Tokopedia yuk ada banyak coupon buat kamu!",
                "message": "Cek Tokopedia yuk ada banyak coupon buat kamu!",
                "appLink": "tokopedia://home",
                "collapsedImg": "",
                "expandedImg": "",
                "campaignUserToken": "MTU4ODU2OTYwNzI1NjM4OTIwMyMxMTEjMCM4OTY2ODcwI3RydWUjMTEx:3",
                "campaignId": 111,
                "parentId": 111
            }     
        """.trimIndent(),
        """
            {
                "icon": "",
                "sound": "",
                "notificationId": "-1",
                "source": "toko-cm",
                "tribe": "111",
                "notificationType": "General",
                "userId": "8966870",
                "shopId": "",
                "transId": "MTU4ODU2OTYwNzI1NjM4OTIwMyMxMTEjMCM4OTY2ODcwI3RydWUjMTEx:3",
                "userTransId": "MTU4ODU2OTYwNzI1NjM4OTIwMyMxMTEjMCM4OTY2ODcwI3RydWUjMTEx",
                "notifcenterBlastId": "",
                "channel": "",
                "title": "Halo Toppers",
                "desc": "Cek Tokopedia yuk ada banyak coupon buat kamu!",
                "message": "Cek Tokopedia yuk ada banyak coupon buat kamu!",
                "appLink": "tokopedia://home",
                "collapsedImg": "",
                "expandedImg": "",
                "campaignUserToken": "MTU4ODU2OTYwNzI1NjM4OTIwMyMxMTEjMCM4OTY2ODcwI3RydWUjMTEx:3",
                "campaignId": 111,
                "parentId": 111
            }  
        """.trimIndent()
)