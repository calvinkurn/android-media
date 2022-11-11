package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.SerializedName

data class PreValidateRedeemBase(
    @SerializedName("hachikoPreValidateRedeem")
    var preValidateRedeem: PreValidateRedeemBaseValue = PreValidateRedeemBaseValue()
)
