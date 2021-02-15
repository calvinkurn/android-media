package com.tokopedia.cart.data.model.request

import com.google.gson.annotations.SerializedName
import com.tokopedia.purchase_platform.common.feature.localizationchooseaddress.request.ChosenAddress

data class AdditionalParams(
        @SerializedName("chosen_address")
        val chosenAddress: ChosenAddress = ChosenAddress()
)