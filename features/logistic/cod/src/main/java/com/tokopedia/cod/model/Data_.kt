package com.tokopedia.cod.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by fajarnuha on 07/01/19.
 */
data class Data_(@SerializedName("payment_id")
                 @Expose
                 var paymentId: Int? = null,
                 @SerializedName("applink")
                 @Expose
                 var thanksApplink: String? = null)