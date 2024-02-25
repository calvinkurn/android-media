package com.tokopedia.tokopedianow.shoppinglist.domain.model

import com.google.gson.annotations.SerializedName

data class SaveShoppingListStateDataResponse(
    @SerializedName("TokonowSaveShoppingListState")
    val tokonowSaveShoppingListState: TokonowSaveShoppingListState
) {
    data class TokonowSaveShoppingListState(
        @SerializedName("header")
        val header: Header
    )

    data class Header(
        @SerializedName("error_code")
        val errorCode: String = "",
        @SerializedName("messages")
        val messages: List<Any> =  emptyList(),
        @SerializedName("process_time")
        val processTime: Double = 0.0,
        @SerializedName("reason")
        val reason: String = ""
    )
}
