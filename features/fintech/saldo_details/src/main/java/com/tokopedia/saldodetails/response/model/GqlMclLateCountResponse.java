package com.tokopedia.saldodetails.response.model;

import com.google.gson.annotations.SerializedName;

public class GqlMclLateCountResponse {

    @SerializedName("mcl_get_latedetails")
    private MclGetLatedetails mclGetLatedetails;

    public void setMclGetLatedetails(MclGetLatedetails mclGetLatedetails) {
        this.mclGetLatedetails = mclGetLatedetails;
    }

    public MclGetLatedetails getMclGetLatedetails() {
        return mclGetLatedetails;
    }

    @Override
    public String toString() {
        return
                "GqlMclLateCountResponse{" +
                        "mcl_get_latedetails = '" + mclGetLatedetails + '\'' +
                        "}";
    }
}
