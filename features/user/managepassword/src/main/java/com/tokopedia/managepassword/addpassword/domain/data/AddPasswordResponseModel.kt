package com.tokopedia.managepassword.addpassword.domain.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AddPasswordResponseModel(
        @Expose
        @SerializedName("addPassword")
        val addPassword: AddPasswordData = AddPasswordData()
)