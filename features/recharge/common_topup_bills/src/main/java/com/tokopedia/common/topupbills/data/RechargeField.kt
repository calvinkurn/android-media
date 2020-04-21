package com.tokopedia.common.topupbills.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RechargeField (
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("value")
        @Expose
        val value: String = ""
) {
        fun getMap(): Map<String, String> {
                return mapOf(PARAM_NAME to name, PARAM_VALUE to value)
        }

        companion object {
                const val PARAM_NAME = "name"
                const val PARAM_VALUE = "value"
        }
}