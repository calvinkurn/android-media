package com.tokopedia.shop.common.domain.interactor.model.adminrevamp

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AdminResponseDetail(
        @SerializedName("code")
        @Expose
        val code: Int? = 0,
        @SerializedName("error_message")
        @Expose
        val errorMessage: String? = ""
)