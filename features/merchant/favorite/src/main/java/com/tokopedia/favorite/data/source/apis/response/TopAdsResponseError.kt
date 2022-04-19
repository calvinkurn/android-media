package com.tokopedia.favorite.data.source.apis.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.network.data.model.response.BaseResponseError
import com.tokopedia.network.exception.ResponseErrorException
import java.io.IOException

class TopAdsResponseError : BaseResponseError() {

    companion object {
        private const val ERROR_KEY = "errors"
    }

    @SerializedName(ERROR_KEY)
    @Expose
    var errors: List<Error>? = null

    override fun getErrorKey(): String {
        return ERROR_KEY
    }

    override fun hasBody(): Boolean {
        return errors != null && errors!!.isNotEmpty()
    }

    override fun createException(): IOException {
        return ResponseErrorException(errors.toString())
    }

}
