package com.tokopedia.user_identification_common.domain.pojo

//
// Created by Yoris Prayogo on 2019-10-29.
//

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CheckKtpStatusPojo {
    @Expose
    @SerializedName("ocrIsKTP")
    var ktpStatus: KtpStatusPojo? = null
}
