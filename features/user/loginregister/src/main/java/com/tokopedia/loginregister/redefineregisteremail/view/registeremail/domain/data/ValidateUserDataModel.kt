package com.tokopedia.loginregister.redefineregisteremail.view.registeremail.domain.data

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class ValidateUserDataModel(

	@SerializedName("validate_user_data")
	val validateUserData: ValidateUserData = ValidateUserData()

)

data class ValidateUserData(

	@SerializedName("error_password")
	val errorPassword: String = "",

	@SuppressLint("Invalid Data Type")
	@SerializedName("is_valid")
	val isValid: Boolean = false,

	@SerializedName("error_fullname")
	val errorFullName: String = "",

	@SerializedName("is_exist")
	val isExist: Boolean = false,

	@SerializedName("error_email")
	val errorEmail: String = "",

	@SerializedName("error")
	val error: String = "",

	@SerializedName("error_phone")
	val errorPhone: String = ""

)
