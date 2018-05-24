package com.tokopedia.instantloan.domain.model;

/**
 * Created by lavekush on 21/03/18.
 */

public class LoanProfileStatusModelDomain {
    private boolean isSubmitted;

    public LoanProfileStatusModelDomain(boolean isSubmitted) {
        this.isSubmitted = isSubmitted;
    }

    public boolean isSubmitted() {
        return isSubmitted;
    }

    public void setSubmitted(boolean submitted) {
        isSubmitted = submitted;
    }

    @Override
    public String toString() {
        return "LoanProfileStatusModelDomain{" +
                "isSubmitted=" + isSubmitted +
                '}';
    }
}
