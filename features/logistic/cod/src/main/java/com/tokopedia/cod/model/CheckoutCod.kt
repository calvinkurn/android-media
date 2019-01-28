package com.tokopedia.cod.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by fajarnuha on 07/01/19.
 */
data class CheckoutCod(@SerializedName("header")
                       @Expose
                       var header: Header? = null,
                       @SerializedName("data")
                       @Expose
                       var data: Data? = null,
                       @SerializedName("status")
                       @Expose
                       var status: String? = null)