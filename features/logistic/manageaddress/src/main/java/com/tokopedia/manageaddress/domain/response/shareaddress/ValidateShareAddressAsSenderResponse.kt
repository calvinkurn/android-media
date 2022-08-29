package com.tokopedia.manageaddress.domain.response.shareaddress

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.tokopedia.logisticCommon.domain.response.ErrorDefaultAddress

data class ValidateShareAddressAsSenderResponse(
    @SerializedName("KeroAddrValidateShareAddressRequestAsSender")
    val keroValidateShareAddressAsSender: ValidateShareAddressData? = null
) {
    data class ValidateShareAddressData(
        @SuppressLint("Invalid Data Type")
        @SerializedName("is_valid")
        val isValid: Boolean = false,
        @SerializedName("receiver_user_name")
        val receiverUserName: String? = null,
        @SerializedName("kero_addr_error")
        val error: ErrorDefaultAddress? = null
    )
}