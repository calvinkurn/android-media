package com.tokopedia.loginregister.external_register.ovo.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.loginregister.external_register.base.data.BaseError

/**
 * Created by Yoris Prayogo on 23/11/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

data class ActivateOvoResponse (
    @Expose
    @SerializedName("activateInitRegis")
    val activateOvoData: ActivateOvoData = ActivateOvoData()
)

data class ActivateOvoData(
        @Expose
        @SerializedName("activationUrl")
        val activationUrl: String = "",
        @Expose
        @SerializedName("goalKey")
        val goalKey: String = "",
        @SerializedName("errors")
        val error: BaseError = BaseError()
)