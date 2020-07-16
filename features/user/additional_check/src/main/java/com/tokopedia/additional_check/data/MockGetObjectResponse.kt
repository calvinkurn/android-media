package com.tokopedia.additional_check.data

import com.google.gson.Gson

/**
 * Created by Yoris Prayogo on 26/05/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

object MockGetObjectResponse {
    fun getObjectSuccess(): GetObjectPojo {
        return Gson().fromJson(responseAtcSuccess, GetObjectPojo::class.java)
    }
}

val responseAtcSuccess = """
    {
       "show_interrupt": {
           "popup_2fa": 1,
           "interval": 1,
           "show_skip": false
        }
   }
""".trimIndent()
