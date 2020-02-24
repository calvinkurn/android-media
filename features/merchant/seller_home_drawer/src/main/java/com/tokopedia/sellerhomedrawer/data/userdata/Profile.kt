package com.tokopedia.sellerhomedrawer.data.userdata

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Profile (

    @SerializedName("user_id")
    @Expose
    var userId: String? = "",
    @SerializedName("first_name")
    @Expose
    val firstName: String? = "",
    @SerializedName("full_name")
    @Expose
    var fullName: String? = "",
    @SerializedName("email")
    @Expose
    var email: String? = "",
    @SerializedName("gender")
    @Expose
    var gender: String? = "",
    @SerializedName("bday")
    @Expose
    var bday: String? = "",
    @SerializedName("age")
    @Expose
    var age: String? = "",
    @SerializedName("phone")
    @Expose
    var phone: String? = "",
    @SerializedName("register_date")
    @Expose
    var registerDate: String? = "",
    @SerializedName("profile_picture")
    @Expose
    var profilePicture: String? = "",
    @SerializedName("completion")
    @Expose
    var completion: Int? = 0

)