package com.tokopedia.payment.setting.authenticate.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Data {

    @SerializedName("checkWhiteListStatus")
    @Expose
    var checkWhiteListStatus: CheckWhiteListStatus? = null

}
