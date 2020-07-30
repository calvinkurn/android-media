package com.tokopedia.topupbills.telco.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 10/05/19.
 */
class TelcoCustomDataCollection(
        @SerializedName("key")
        @Expose
        val key: String,
        @SerializedName("value")
        @Expose
        val value: String,
        @SerializedName("operator")
        @Expose
        val operator: TelcoOperator
)