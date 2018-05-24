package com.tokopedia.instantloan.view.model;

/**
 * Created by lavekush on 20/03/18.
 */

public class LoanProfileStatusViewModel {

    private boolean isSubmitted;

    public LoanProfileStatusViewModel(boolean isSubmitted) {
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
