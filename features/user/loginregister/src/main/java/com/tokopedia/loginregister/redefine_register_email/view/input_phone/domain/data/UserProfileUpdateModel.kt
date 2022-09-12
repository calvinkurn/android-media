package com.tokopedia.loginregister.redefine_register_email.view.input_phone.domain.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.ZERO

data class UserProfileUpdateModel(

    @SerializedName("userProfileUpdate")
    val data: UserProfileUpdate = UserProfileUpdate()

)

data class UserProfileUpdate(

    @SerializedName("isSuccess")
    val isSuccess: Int = Int.ZERO,

    @SerializedName("errors")
    val errors: List<String> = listOf()

)