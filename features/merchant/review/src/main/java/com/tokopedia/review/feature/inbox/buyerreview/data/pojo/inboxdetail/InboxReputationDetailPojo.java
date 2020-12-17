
package com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InboxReputationDetailPojo {

    @SerializedName("reputation_id")
    @Expose
    private int reputationId;
    @SerializedName("review_inbox_data")
    @Expose
    private List<ReviewInboxDatum> reviewInboxData = null;
    @SerializedName("user_data")
    @Expose
    private UserData userData;
    @SerializedName("shop_data")
    @Expose
    private ShopData shopData;
    @SerializedName("invoice_ref_num")
    @Expose
    private String invoiceRefNum;
    @SerializedName("invoice_time")
    @Expose
    private String invoiceTime;
    @SerializedName("order_id")
    @Expose
    private String orderId;

    public int getReputationId() {
        return reputationId;
    }

    public void setReputationId(int reputationId) {
        this.reputationId = reputationId;
    }

    public List<ReviewInboxDatum> getReviewInboxData() {
        return reviewInboxData;
    }

    public void setReviewInboxData(List<ReviewInboxDatum> reviewInboxData) {
        this.reviewInboxData = reviewInboxData;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public ShopData getShopData() {
        return shopData;
    }

    public void setShopData(ShopData shopData) {
        this.shopData = shopData;
    }

    public String getInvoiceRefNum() {
        return invoiceRefNum;
    }

    public void setInvoiceRefNum(String invoiceRefNum) {
        this.invoiceRefNum = invoiceRefNum;
    }

    public String getInvoiceTime() {
        return invoiceTime;
    }

    public void setInvoiceTime(String invoiceTime) {
        this.invoiceTime = invoiceTime;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
