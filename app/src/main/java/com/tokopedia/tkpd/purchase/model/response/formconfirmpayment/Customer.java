package com.tokopedia.tkpd.purchase.model.response.formconfirmpayment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Angga.Prasetiyo on 20/06/2016.
 */
public class Customer {
    private static final String TAG = Customer.class.getSimpleName();

    @SerializedName("customer_id")
    @Expose
    private String customerId;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
