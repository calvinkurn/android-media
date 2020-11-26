package com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail;

import java.util.List;

/**
 * @author by nisie on 8/19/17.
 */

public class ReviewDomain {

    private List<ReviewItemDomain> data = null;
    private int reputationId;
    private UserDataDomain userData;
    private ShopDataDomain shopData;
    private String invoiceRefNum;
    private String invoiceTime;
    private String orderId;

    public ReviewDomain(List<ReviewItemDomain> data,
                        int reputationId, UserDataDomain userData,
                        ShopDataDomain shopData, String invoiceRefNum,
                        String invoiceTime, String orderId) {
        this.data = data;
        this.reputationId = reputationId;
        this.userData = userData;
        this.shopData = shopData;
        this.invoiceRefNum = invoiceRefNum;
        this.invoiceTime = invoiceTime;
        this.orderId = orderId;
    }

    public List<ReviewItemDomain> getData() {
        return data;
    }

    public int getReputationId() {
        return reputationId;
    }

    public UserDataDomain getUserData() {
        return userData;
    }

    public ShopDataDomain getShopData() {
        return shopData;
    }

    public String getInvoiceRefNum() {
        return invoiceRefNum;
    }

    public String getInvoiceTime() {
        return invoiceTime;
    }

    public String getOrderId() {
        return orderId;
    }
}
