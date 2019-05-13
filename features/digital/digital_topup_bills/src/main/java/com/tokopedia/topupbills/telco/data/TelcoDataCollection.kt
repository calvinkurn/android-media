package com.tokopedia.topupbills.telco.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 10/05/19.
 */
class TelcoDataCollection(
        @SerializedName("id")
        @Expose
        val id: Int,
        @SerializedName("key")
        @Expose
        val key: String,
        @SerializedName("value")
        @Expose
        val value: String,
        @SerializedName("selected")
        @Expose
        val selected: Boolean = false,
        @SerializedName("filter_mapping")
        @Expose
        val filterMapping: String,
        @SerializedName("group")
        @Expose
        val group: String,
        @SerializedName("product")
        @Expose
        val product: TelcoProduct,
        @SerializedName("operator")
        @Expose
        val operator: TelcoOperator
)