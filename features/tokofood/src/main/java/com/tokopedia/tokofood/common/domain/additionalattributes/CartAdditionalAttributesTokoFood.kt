package com.tokopedia.tokofood.common.domain.additionalattributes

import com.google.gson.annotations.SerializedName
import com.tokopedia.tokofood.common.address.TokoFoodChosenAddress

data class CartAdditionalAttributesTokoFood(
    @SerializedName("chosen_address")
    val chosenAddress: TokoFoodChosenAddress = TokoFoodChosenAddress()
)
