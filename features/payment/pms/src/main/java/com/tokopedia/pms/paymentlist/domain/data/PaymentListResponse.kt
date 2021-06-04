
package com.tokopedia.pms.paymentlist.domain.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PaymentList {

    @SerializedName("has_next_page")
    @Expose
    private boolean hasNextPage;

    @SerializedName("last_cursor")
    @Expose
    private String lastCursor;

    @SerializedName("payment_list")
    @Expose
    private List<PaymentListInside> paymentList = null;

    public List<PaymentListInside> getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(List<PaymentListInside> paymentList) {
        this.paymentList = paymentList;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public String getLastCursor() {
        return lastCursor;
    }

    public void setLastCursor(String lastCursor) {
        this.lastCursor = lastCursor;
    }
}
