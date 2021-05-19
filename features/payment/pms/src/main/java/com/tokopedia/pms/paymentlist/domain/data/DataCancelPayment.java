package com.tokopedia.pms.paymentlist.domain.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by zulfikarrahman on 7/9/18.
 */

public class DataCancelPayment {

    @SerializedName("cancelPayment")
    @Expose
    private CancelPayment cancelPayment;

    public CancelPayment getCancelPayment() {
        return cancelPayment;
    }

    public void setCancelPayment(CancelPayment cancelPayment) {
        this.cancelPayment = cancelPayment;
    }
}
