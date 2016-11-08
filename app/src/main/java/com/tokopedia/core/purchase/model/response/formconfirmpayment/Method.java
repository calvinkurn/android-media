package com.tokopedia.core.purchase.model.response.formconfirmpayment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Angga.Prasetiyo on 20/06/2016.
 */
public class Method {
    private static final String TAG = Method.class.getSimpleName();

    public static final CharSequence TRANSFER_ATM = "2";
    public static final CharSequence INTERNET_BANKING = "3";
    public static final CharSequence MOBILE_BANKING = "4";
    public static final CharSequence BALANCE_TOKOPEDIA = "5";
    public static final CharSequence CASH_DEPOSIT = "6";

    @SerializedName("method_id")
    @Expose
    private String methodId;
    @SerializedName("method_name")
    @Expose
    private String methodName;

    public String getMethodId() {
        return methodId;
    }

    public void setMethodId(String methodId) {
        this.methodId = methodId;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public String toString() {
        return methodName;
    }
}
