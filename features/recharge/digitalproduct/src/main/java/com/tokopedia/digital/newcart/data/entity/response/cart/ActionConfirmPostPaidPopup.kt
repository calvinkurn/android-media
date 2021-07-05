package com.tokopedia.digital.newcart.data.entity.response.cart

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ActionConfirmPostPaidPopup {
    @SerializedName("title")
    @Expose
    var title: String? = null
}
