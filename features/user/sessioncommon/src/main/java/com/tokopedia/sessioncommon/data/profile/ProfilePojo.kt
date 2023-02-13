package com.tokopedia.sessioncommon.data.profile

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by nisie on 12/06/19.
 */
data class ProfilePojo(
        @SerializedName("profile")
        @Expose
        var profileInfo: ProfileInfo = ProfileInfo(),
        @SerializedName("shopBasicData")
        @Expose
        var shopInfo: ShopBasicData = ShopBasicData()
) {}

data class ProfileInfo(
        @SerializedName("user_id")
        @Expose
        var userId: String = "0",
        @SerializedName("full_name")
        @Expose
        var fullName: String = "",
        @SerializedName("first_name")
        @Expose
        var firstName: String = "",
        @SerializedName("email")
        @Expose
        var email: String = "",
        @SerializedName("bday")
        @Expose
        var birthday: String = "",
        @SerializedName("gender")
        @Expose
        var gender: String = "",
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
) {}


data class ShopBasicData(
        @SerializedName("result")
        @Expose
        var shopData: ShopData = ShopData()
) {}

data class ShopData(
        @SerializedName("shopID")
        @Expose
        var shopId: String = "",
        @SerializedName("domain")
        @Expose
        var domain: String = "",
        @SerializedName("name")
        @Expose
        var shopName: String = "",
        @SerializedName("logo")
        @Expose
        var shopAvatar: String = "",
        @SerializedName("level")
        @Expose
        var shopLevel: Int = 0,
        @SerializedName("avatarOriginal")
        @Expose
        var shopAvatarOriginal: String = ""
        ) {}
