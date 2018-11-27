package com.tokopedia.payment.setting.detail.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DataResponseDeleteCC {

    @SerializedName("remove_credit_card")
    @Expose
    var removeCreditCard: RemoveCreditCard? = null

}
