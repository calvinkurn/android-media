package com.tokopedia.cod.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by fajarnuha on 07/01/19.
 */
data class Data(@SerializedName("success")
                @Expose
                var success: Int? = null,
                @SerializedName("error")
                @Expose
                var error: String? = null,
                @SerializedName("error_state")
                @Expose
                var errorState: Int? = null,
                @SerializedName("message")
                @Expose
                var message: String? = null,
                @SerializedName("data")
                @Expose
                var data: Data_? = null)