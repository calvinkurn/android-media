package com.tokopedia.promocheckout.common.domain.model.event

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class User(
        @SerializedName("age")
        @Expose
        val age: Int = 0,
        @SerializedName("balance")
        @Expose
        val balance: Balance = Balance(),
        @SerializedName("bday")
        @Expose
        val bday: String = "",
        @SerializedName("client_id")
        @Expose
        val clientId: String = "",
        @SerializedName("completion")
        @Expose
        val completion: Int = 0,
        @SerializedName("created_password")
        @Expose
        val createdPassword: Boolean = false,
        @SerializedName("email")
        @Expose
        val email: String = "",
        @SerializedName("full_name")
        @Expose
        val fullName: String = "",
        @SerializedName("gender")
        @Expose
        val gender: String = "",
        @SerializedName("is_qa")
        @Expose
        val isQa: Boolean = false,
        @SerializedName("lang")
        @Expose
        val lang: String = "",
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("phone")
        @Expose
        val phone: String = "",
        @SerializedName("phone_masked")
        @Expose
        val phoneMasked: String = "",
        @SerializedName("phone_verified")
        @Expose
        val phoneVerified: Boolean = false,
        @SerializedName("profile_picture")
        @Expose
        val profilePicture: String = "",
        @SerializedName("register_date")
        @Expose
        val registerDate: String = "",
        @SerializedName("roles")
        @Expose
        val roles: Any = Any(),
        @SerializedName("status")
        @Expose
        val status: Int = 0,
        @SerializedName("user_id")
        @Expose
        val userId: Int = 0
)