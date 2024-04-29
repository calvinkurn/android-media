package com.tokopedia.tokopedianow.shoppinglist.domain.model

import com.google.gson.annotations.SerializedName

data class SaveShoppingListStateActionParam(
    @SerializedName("productID")
    val productId: String,
    @SerializedName("isSelected")
    val isSelected: Boolean
)
