package com.tokopedia.wallet.ovoactivation.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 10/10/18.
 */
class ErrorModelEntity(
    @SerializedName("message")
    @Expose
    val message: String? = null)
