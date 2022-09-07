package com.tokopedia.loginregister.redefine_register_email.input_phone.domain.data

import com.google.gson.annotations.SerializedName

data class GetUserInfoModel(

    @SerializedName("profile")
    val profileInfo: ProfileInfo = ProfileInfo(),

    @SerializedName("shopBasicData")
    val shopInfo: ShopBasicData = ShopBasicData()

)

data class ProfileInfo(

    @SerializedName("user_id")
    val userId: String = "0",

    @SerializedName("full_name")
    val fullName: String = "",

    @SerializedName("first_name")
    val firstName: String = "",

    @SerializedName("email")
    val email: String = "",

    @SerializedName("bday")
    val birthday: String = "",

    @SerializedName("gender")
    val gender: String = "",

    @SerializedName("phone")
    val phone: String = "",

    @SerializedName("phone_masked")
    val phoneMasked: String = "",

    @SerializedName("phone_verified")
    val isPhoneVerified: Boolean = false,

    @SerializedName("profile_picture")
    val profilePicture: String = "",

    @SerializedName("created_password")
    val isCreatedPassword: Boolean = false,

    @SerializedName("isLoggedIn")
    val isLoggedIn: Boolean = false
)

data class ShopBasicData(
    @SerializedName("result")
    val shopData: ShopData = ShopData()
)

data class ShopData(
    @SerializedName("shopID")
    val shopId: String = "",
    @SerializedName("domain")
    val domain: String = "",
    @SerializedName("name")
    val shopName: String = "",
    @SerializedName("logo")
    val shopAvatar: String = "",
    @SerializedName("level")
    val shopLevel: Int = 0
)