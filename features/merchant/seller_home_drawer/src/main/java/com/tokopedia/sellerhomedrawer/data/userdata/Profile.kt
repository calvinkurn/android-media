package com.tokopedia.sellerhomedrawer.data.userdata

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Profile {

    @SerializedName("user_id")
    @Expose
    var userId: String? = null
    @SerializedName("first_name")
    @Expose
    val firstName: String? = null
    @SerializedName("full_name")
    @Expose
    var fullName: String? = null
    @SerializedName("email")
    @Expose
    var email: String? = null
    @SerializedName("gender")
    @Expose
    var gender: String? = null
    @SerializedName("bday")
    @Expose
    var bday: String? = null
    @SerializedName("age")
    @Expose
    var age: String? = null
    @SerializedName("phone")
    @Expose
    var phone: String? = null
    @SerializedName("register_date")
    @Expose
    var registerDate: String? = null
    @SerializedName("profile_picture")
    @Expose
    var profilePicture: String? = null
    @SerializedName("completion")
    @Expose
    var completion: Int? = null

}