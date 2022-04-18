package com.tokopedia.product.detail.data.model.navbar

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NavBar(
    @SerializedName("name")
    @Expose
    val name: String = "",

    @SerializedName("items")
    @Expose
    val items: List<NavBarComponent> = emptyList()
)

data class NavBarComponent(
    @SerializedName("title")
    @Expose
    val title: String = "",

    @SerializedName("componentName")
    @Expose
    val componentName: String = ""
)