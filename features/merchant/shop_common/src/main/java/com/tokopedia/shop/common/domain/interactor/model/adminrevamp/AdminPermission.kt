package com.tokopedia.shop.common.domain.interactor.model.adminrevamp

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AdminPermission(
        @SerializedName("permission_id")
        @Expose
        val id: String? = ""
)