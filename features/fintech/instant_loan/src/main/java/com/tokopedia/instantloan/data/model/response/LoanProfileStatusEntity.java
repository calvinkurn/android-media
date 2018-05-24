package com.tokopedia.instantloan.data.model.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lavekush on 29/03/18.
 */

public class LoanProfileStatusEntity {

    public boolean isSubmitted() {
        return isSubmitted;
    }

    public void setSubmitted(boolean submitted) {
        isSubmitted = submitted;
    }

    @SerializedName("is_submitted")
    private boolean isSubmitted;

    @Override
    public String toString() {
        return "LoanProfileStatusEntity{" +
                "isSubmitted=" + isSubmitted +
                '}';
    }
}
