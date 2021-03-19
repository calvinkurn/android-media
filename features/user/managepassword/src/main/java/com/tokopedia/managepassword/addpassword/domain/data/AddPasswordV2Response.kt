package com.tokopedia.managepassword.addpassword.domain.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AddPasswordV2Response(
        @Expose
        @SerializedName("addPasswordV2")
        val addPassword: AddPasswordData = AddPasswordData()
)