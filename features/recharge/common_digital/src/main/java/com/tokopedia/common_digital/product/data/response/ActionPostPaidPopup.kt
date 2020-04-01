package com.tokopedia.common_digital.product.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ActionPostPaidPopup {
    @SerializedName("YES")
    @Expose
    var confirmAction: ActionConfirmPostPaidPopup? = null
}
