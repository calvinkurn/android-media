package com.tokopedia.tkpd.thankyou.data.pojo.marketplace.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by okasurya on 12/7/17.
 */

public class PaymentMethod {
    @SerializedName("defer")
    @Expose
    private Defer defer;
    @SerializedName("instant")
    @Expose
    private Instant instant;
    @SerializedName("transfer")
    @Expose
    private Transfer transfer;
    @SerializedName("method")
    @Expose
    private String method;

    public Defer getDefer() {
        return defer;
    }

    public void setDefer(Defer defer) {
        this.defer = defer;
    }

    public Instant getInstant() {
        return instant;
    }

    public void setInstant(Instant instant) {
        this.instant = instant;
    }

    public Transfer getTransfer() {
        return transfer;
    }

    public void setTransfer(Transfer transfer) {
        this.transfer = transfer;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
