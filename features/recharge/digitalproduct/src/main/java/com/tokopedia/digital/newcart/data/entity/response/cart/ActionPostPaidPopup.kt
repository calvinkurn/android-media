package com.tokopedia.digital.newcart.data.entity.response.cart

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ActionPostPaidPopup {
    @SerializedName("YES")
    @Expose
    var confirmAction: ActionConfirmPostPaidPopup? = null
}
