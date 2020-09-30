package com.tokopedia.cart.data.model.response.undodeletecart

import com.google.gson.annotations.SerializedName

data class UndoDeleteCartGqlResponse(
        @SerializedName("undo_remove_product_cart")
        val undoDeleteCartDataResponse: UndoDeleteCartDataResponse = UndoDeleteCartDataResponse()
)

data class UndoDeleteCartDataResponse(
        @SerializedName("status")
        val status: String = "",
        @SerializedName("error_message")
        val errorMessage: List<String> = emptyList(),
        @SerializedName("data")
        val data: Data = Data()
)

data class Data(
        @SerializedName("success")
        val success: Int = 0,
        @SerializedName("message")
        val message: List<String> = emptyList()
)
