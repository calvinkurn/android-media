package com.tokopedia.saldodetails.feature_saldo_detail.domain.data

import com.google.gson.annotations.SerializedName

data class GqlMclLateCountResponse(
        @SerializedName("mcl_get_latedetails")
        var mclGetLatedetails: MclGetLatedetails? = null

) {
    override fun toString(): String {
        return "GqlMclLateCountResponse{" +
                "mcl_get_latedetails = '" + mclGetLatedetails + '\''.toString() +
                "}"
    }
}
