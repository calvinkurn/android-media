package com.tokopedia.saldodetails.response.model

import com.google.gson.annotations.SerializedName

data class MclGetLatedetails(
        @SerializedName("lateCount")
        var lateCount: Int = 0
) {
    override fun toString(): String {
        return "MclGetLatedetails{" +
                "lateCount = '" + lateCount + '\''.toString() +
                "}"
    }
}
