package com.tokopedia.promocheckout.common.domain.model.clearpromo

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 18/03/19.
 */

data class ClearCacheAutoApplyStack(
        @SerializedName("Success")
        val success: Boolean = false
)