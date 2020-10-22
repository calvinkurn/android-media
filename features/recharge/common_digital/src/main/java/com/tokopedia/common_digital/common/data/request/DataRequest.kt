package com.tokopedia.common_digital.common.data.request

import com.google.gson.annotations.SerializedName

/**
 * Created by User on 11/8/2017.
 */
data class DataRequest<T> (
    @SerializedName("data")
    var data: T? = null
)