package com.tokopedia.profilecompletion.addphone.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CheckPhonePojo(
        @SerializedName("checkMsisdn")
        @Expose
        var checkMsisdn: CheckMsisdn = CheckMsisdn()
)

data class CheckMsisdn(
        @SerializedName("isExist")
        @Expose
        var isExist: Boolean = false,
        @SerializedName("errors")
        @Expose
        var errorMessage: ArrayList<String> = arrayListOf()
)