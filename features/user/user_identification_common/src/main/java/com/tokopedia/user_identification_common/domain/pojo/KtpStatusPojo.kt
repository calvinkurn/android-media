package com.tokopedia.user_identification_common.domain.pojo

//
// Created by Yoris Prayogo on 2019-10-29.
//

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class KtpStatusPojo {
    @Expose
    @SerializedName("isKTP")
    var valid: Boolean = false

    @Expose
    @SerializedName("bypass")
    var bypass: Boolean = false

    @Expose
    @SerializedName("error")
    var error: String = ""
}
