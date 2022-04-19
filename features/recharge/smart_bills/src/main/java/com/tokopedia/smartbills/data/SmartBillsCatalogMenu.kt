package com.tokopedia.smartbills.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SmartBillsCatalogMenu(
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("label")
        @Expose
        val label: String = "",
        @SerializedName("icon")
        @Expose
        val icon: String = "",
        @SerializedName("app_link")
        @Expose
        val applink: String = "",
        @SerializedName("slug_name")
        @Expose
        val slugName: String = "",
)