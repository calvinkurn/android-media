package com.tokopedia.sessioncommon.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by nisie on 6/19/17.
 * Used by AccountService.java (InstrumentationAuthHelper)
 */
class GetUserInfoData {
    @SerializedName("user_id")
    @Expose
    var userId: String = ""

    @SerializedName("full_name")
    @Expose
    var fullName = ""

    @SerializedName("first_name")
    @Expose
    var firstName = ""

    @SerializedName("name")
    @Expose
    var name = ""

    @SerializedName("email")
    @Expose
    var email = ""

    @SerializedName("gender")
    @Expose
    var gender = 0

    @SerializedName("bday")
    @Expose
    var bday = ""

    @SerializedName("age")
    @Expose
    var age = 0

    @SerializedName("phone")
    @Expose
    var phone = ""

    @SerializedName("phone_masked")
    @Expose
    var phoneMasked = ""

    @SerializedName("register_date")
    @Expose
    var registerDate = ""

    @SerializedName("status")
    @Expose
    var status = 0

    @SerializedName("lang")
    @Expose
    var lang = ""

    @SerializedName("created_password")
    @Expose
    var isCreatedPassword = false

    @SerializedName("phone_verified")
    @Expose
    var isPhoneVerified = false

    @SerializedName("roles")
    @Expose
    var roles: List<Int> = ArrayList<Int>()

    @SerializedName("profile_picture")
    @Expose
    var profilePicture = ""

    @SerializedName("client_id")
    @Expose
    var clientId = ""

    @SerializedName("completion")
    @Expose
    var completion = 0

    @SerializedName("create_password_list")
    @Expose
    val createPasswordList: List<String> = ArrayList<String>()
}
