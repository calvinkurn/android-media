package com.tokopedia.atc_common.domain.model.response.updatecartcounter

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 2019-08-15.
 */

data class UpdateCartCounter(
    @SerializedName("count")
    @Expose
    val count: Int
)
