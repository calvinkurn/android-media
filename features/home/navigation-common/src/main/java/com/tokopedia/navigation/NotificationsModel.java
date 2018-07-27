package com.tokopedia.navigation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author okasurya on 7/21/18.
 */
public class NotificationsModel {
    @SerializedName("resolutionAs")
    @Expose
    private NotificationResolutionModel resolution;
    @SerializedName("buyerOrderStatus")
    @Expose
    private NotificationBuyerOrderModel buyerOrder;
    @SerializedName("sellerOrderStatus")
    @Expose
    private NotificationSellerOrderModel sellerOrder;
    @SerializedName("sellerInfo")
    @Expose
    private NotificationSellerInfoModel sellerInfo;
    @SerializedName("userSatisfactionSurvey")
    @Expose
    private Integer userSatisfaction;

    public NotificationResolutionModel getResolution() {
        return resolution;
    }

    public void setResolution(NotificationResolutionModel resolution) {
        this.resolution = resolution;
    }

    public NotificationBuyerOrderModel getBuyerOrder() {
        return buyerOrder;
    }

    public void setBuyerOrder(NotificationBuyerOrderModel buyerOrder) {
        this.buyerOrder = buyerOrder;
    }

    public NotificationSellerOrderModel getSellerOrder() {
        return sellerOrder;
    }

    public void setSellerOrder(NotificationSellerOrderModel sellerOrder) {
        this.sellerOrder = sellerOrder;
    }

    public NotificationSellerInfoModel getSellerInfo() {
        return sellerInfo;
    }

    public void setSellerInfo(NotificationSellerInfoModel sellerInfo) {
        this.sellerInfo = sellerInfo;
    }

    public Integer getUserSatisfaction() {
        return userSatisfaction;
    }

    public void setUserSatisfaction(Integer userSatisfaction) {
        this.userSatisfaction = userSatisfaction;
    }
}
