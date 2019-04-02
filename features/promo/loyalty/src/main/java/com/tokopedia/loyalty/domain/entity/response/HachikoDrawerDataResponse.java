package com.tokopedia.loyalty.domain.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sachinbansal on 5/15/18.
 */

public class HachikoDrawerDataResponse {

    @SerializedName("tokopoints")
    @Expose
    private GqlTokoPointDrawerDataResponse gqlTokoPointDrawerDataResponse;

    @SerializedName("tokopointsSumCoupon")
    private TokopointsSumCoupon tokopointsSumCoupon;

    public GqlTokoPointDrawerDataResponse getGqlTokoPointDrawerDataResponse() {
        return gqlTokoPointDrawerDataResponse;
    }

    public void setGqlTokoPointDrawerDataResponse(GqlTokoPointDrawerDataResponse gqlTokoPointDrawerDataResponse) {
        this.gqlTokoPointDrawerDataResponse = gqlTokoPointDrawerDataResponse;
    }

    public TokopointsSumCoupon getTokopointsSumCoupon() {
        return tokopointsSumCoupon;
    }

    public void setTokopointsSumCoupon(TokopointsSumCoupon tokopointsSumCoupon) {
        this.tokopointsSumCoupon = tokopointsSumCoupon;
    }
}
