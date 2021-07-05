package com.tokopedia.cart.data.model.response.cartlistcheckboxstate

import com.google.gson.annotations.SerializedName

data class SetCartlistCheckboxGqlResponse(
        @SerializedName("set_cartlist_checkbox_state")
        val setCartlistCheckboxStateResponse: SetCartlistCheckboxStateResponse = SetCartlistCheckboxStateResponse()
)

data class SetCartlistCheckboxStateResponse(
        @SerializedName("status")
        val status: String = "",
        @SerializedName("error_message")
        val errorMessage: List<String> = emptyList(),
        @SerializedName("data")
        val data: CheckboxDataResponse = CheckboxDataResponse()
)

data class CheckboxDataResponse(
        @SerializedName("success")
        val success: Int = 0
)