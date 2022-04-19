package com.tokopedia.loginregister.external_register.ovo.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.loginregister.external_register.base.data.BaseError

/**
 * Created by Yoris Prayogo on 23/11/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

data class CheckOvoResponse(
        @Expose
        @SerializedName("goalCheckPhoneRegistration")
        val data: CheckOvoData = CheckOvoData()
)

data class CheckOvoData(
        @Expose
        @SerializedName("is_registered")
        val isRegistered: Boolean = false,
        @Expose
        @SerializedName("is_allow")
        val isAllow: Boolean = false,
        @Expose
        @SerializedName("errors")
        val errors: BaseError = BaseError()
)