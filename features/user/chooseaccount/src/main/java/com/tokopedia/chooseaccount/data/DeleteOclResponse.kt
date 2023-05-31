package com.tokopedia.chooseaccount.data

import com.google.gson.annotations.SerializedName

data class DeleteOclResponse (
    @SerializedName("deleteOcl")
    val data: DeleteOclData
)

data class DeleteOclData(
    @SerializedName("ocl_jwt_token")
    val token: String = "",
    @SerializedName("error_code")
    val errorMsg: String = ""
)
