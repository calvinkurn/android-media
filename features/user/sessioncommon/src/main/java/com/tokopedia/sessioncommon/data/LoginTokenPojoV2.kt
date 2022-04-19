package com.tokopedia.sessioncommon.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Yoris Prayogo on 19/02/21.
 * Copyright (c) 2021 PT. Tokopedia All rights reserved.
 */

data class LoginTokenPojoV2 (
        @SerializedName("login_token_v2")
        @Expose
        var loginToken: LoginToken = LoginToken()
)