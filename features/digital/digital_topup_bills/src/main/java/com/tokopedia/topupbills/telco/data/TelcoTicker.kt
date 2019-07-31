package com.tokopedia.topupbills.telco.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 28/05/19.
 */
class TelcoTicker(
        @SerializedName("ID")
        @Expose
        val id: Int,
        @SerializedName("Name")
        @Expose
        val name: String,
        @SerializedName("Content")
        @Expose
        val content: String,
        @SerializedName("Type")
        @Expose
        val type: String,
        @SerializedName("Environment")
        @Expose
        val environment: String,
        @SerializedName("ActionLink")
        @Expose
        val actionLink: String,
        @SerializedName("ActionText")
        @Expose
        val actionText: String
)