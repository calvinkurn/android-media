package com.tokopedia.shop.common.domain.interactor.model.adminrevamp

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AuthorizeAccessResponse(
        @SerializedName("rbacAuthorizeAccess")
        @Expose
        val authorizeAccessData: AuthorizeAccessData? = AuthorizeAccessData()
)

data class AuthorizeAccessData(
        @SerializedName("error")
        @Expose
        val error: String? = "",
        @SerializedName("is_authorized")
        @Expose
        val isAuthorized: Boolean? = false
)