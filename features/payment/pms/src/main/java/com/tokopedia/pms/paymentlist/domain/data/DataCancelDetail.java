package com.tokopedia.pms.paymentlist.domain.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by zulfikarrahman on 7/9/18.
 */

public class DataCancelDetail {

    @SerializedName("cancelDetail")
    @Expose
    private CancelDetail cancelDetail;

    public CancelDetail getCancelDetail() {
        return cancelDetail;
    }

    public void setCancelDetail(CancelDetail cancelDetail) {
        this.cancelDetail = cancelDetail;
    }
}
