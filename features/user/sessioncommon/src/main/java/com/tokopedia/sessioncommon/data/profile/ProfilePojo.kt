package com.tokopedia.sessioncommon.data.profile

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by nisie on 12/06/19.
 */
data class ProfilePojo(
        @SerializedName("profile")
        @Expose
        var profileInfo: ProfileInfo = ProfileInfo()
){}

data class ProfileInfo(
        @SerializedName("user_id")
        @Expose
        var userId: String = "",
        @SerializedName("full_name")
        @Expose
        var fullName: String = "",
        @SerializedName("first_name")
        @Expose
        var firstName: String = "",
        @SerializedName("email")
        @Expose
        var email: String = "",
        @SerializedName("phone")
        @Expose
        var phone: String = "",
        @SerializedName("phone_masked")
        @Expose
        var phoneMasked: String = "",
        @SerializedName("phone_verified")
        @Expose
        var isPhoneVerified: Boolean = false,
        @SerializedName("profile_picture")
        @Expose
        var profilePicture: String = "",
        @SerializedName("created_password")
        @Expose
        var isCreatedPassword: Boolean = false,
        @SerializedName("isLoggedIn")
        @Expose
        var isLoggedIn: Boolean = false
        ){}