package com.tokopedia.tokofood.common.domain.additionalattributes

import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.localizationchooseaddress.common.ChosenAddress

data class CartAdditionalAttributesTokoFood(
    @SerializedName("chosen_address")
    @Expose
    val chosenAddress: ChosenAddress = ChosenAddress(),
    @SerializedName("source")
    @Expose
    val source: String = ""
) {
    fun generateString(): String = Gson().toJson(this)
}