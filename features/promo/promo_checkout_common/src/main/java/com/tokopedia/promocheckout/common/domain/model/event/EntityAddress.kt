package com.tokopedia.promocheckout.common.domain.model.event

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class EntityAddress(
        @SerializedName("address")
        @Expose
        val address: String = "",
        @SerializedName("city")
        @Expose
        val city: String = "",
        @SerializedName("district")
        @Expose
        val district: String = "",
        @SerializedName("email")
        @Expose
        val email: String = "",
        @SerializedName("latitude")
        @Expose
        val latitude: String = "",
        @SerializedName("longitude")
        @Expose
        val longitude: String = "",
        @SerializedName("mobile")
        @Expose
        val mobile: String = "",
        @SerializedName("Name")
        @Expose
        val name: String = "",
        @SerializedName("name")
        @Expose
        val names: String = "",
        @SerializedName("state")
        @Expose
        val state: String = ""
)