package com.tokopedia.loginregister.redefine_register_email.domain.data

import com.google.gson.annotations.SerializedName

data class GetUserInfoModel (
    @SerializedName("profile")
    var profileInfo: ProfileInfo = ProfileInfo(),
    @SerializedName("shopBasicData")
    var shopInfo: ShopBasicData = ShopBasicData()
)

data class ProfileInfo(
    @SerializedName("user_id")
    var userId: String = "0",
    @SerializedName("full_name")
    var fullName: String = "",
    @SerializedName("first_name")
    var firstName: String = "",
    @SerializedName("email")
    var email: String = "",
    @SerializedName("bday")
    var birthday: String = "",
    @SerializedName("gender")
    var gender: String = "",
    @SerializedName("phone")
    var phone: String = "",
    @SerializedName("phone_masked")
    var phoneMasked: String = "",
    @SerializedName("phone_verified")
    var isPhoneVerified: Boolean = false,
    @SerializedName("profile_picture")
    var profilePicture: String = "",
    @SerializedName("created_password")
    var isCreatedPassword: Boolean = false,
    @SerializedName("isLoggedIn")
    var isLoggedIn: Boolean = false
)

data class ShopBasicData(
    @SerializedName("result")
    var shopData: ShopData = ShopData()
)

data class ShopData(
    @SerializedName("shopID")
    var shopId: String = "",
    @SerializedName("domain")
    var domain: String = "",
    @SerializedName("name")
    var shopName: String = "",
    @SerializedName("logo")
    var shopAvatar: String = "",
    @SerializedName("level")
    var shopLevel: Int = 0
)