package com.tokopedia.universal_sharing.data.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.network.data.model.response.BaseResponseError
import java.io.IOException

const val ERROR_KEY = "error"

data class BranchLinkErrorResponse(
        @SerializedName(ERROR_KEY)
        val error: BranchLinkError?,
) : BaseResponseError() {
    override fun getErrorKey(): String {
        return ERROR_KEY
    }

    override fun hasBody(): Boolean {
        return error != null
    }

    override fun createException(): IOException {
        return IOException(error?.message ?: "")
    }

}

data class BranchLinkError(
        @SerializedName("code")
        val status: Int,

        @SerializedName("message")
        val message: String
)

