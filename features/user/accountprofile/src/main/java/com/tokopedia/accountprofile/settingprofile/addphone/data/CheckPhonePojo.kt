package com.tokopedia.accountprofile.settingprofile.addphone.data

import com.google.gson.annotations.SerializedName

data class CheckPhonePojo(
    @SerializedName("checkMsisdn")
    var checkMsisdn: CheckMsisdn = CheckMsisdn()
)

data class CheckMsisdn(
    @SerializedName("isExist")
    var isExist: Boolean = false,
    @SerializedName("errors")
    var errorMessage: ArrayList<String> = arrayListOf()
)
