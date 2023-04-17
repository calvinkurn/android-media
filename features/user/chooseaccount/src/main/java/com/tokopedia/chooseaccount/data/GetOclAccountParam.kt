package com.tokopedia.chooseaccount.data

import com.google.gson.annotations.SerializedName

data class GetOclAccountParam (
    @SerializedName("ocl_jwt_token")
    val token: String = ""
)
