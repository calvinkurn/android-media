package com.tokopedia.tkpd.thankyou.data.pojo.marketplace.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by fwidjaja on 18/04/19.
 */
public class BenefitByOrder {
    @SerializedName("order_id")
    @Expose
    private int orderId;

    @SerializedName("order_level_orders")
    @Expose
    private List<OrderLevel> listOrderLevel;

    @SerializedName("global_level_orders")
    @Expose
    private List<OrderLevel> listGlobalLevel;

    @SerializedName("total_discount")
    @Expose
    private int totalDiscount;

    @SerializedName("total_benefit")
    @Expose
    private int totalBenefit;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public List<OrderLevel> getListOrderLevel() {
        return listOrderLevel;
    }

    public void setListOrderLevel(List<OrderLevel> listOrderLevel) {
        this.listOrderLevel = listOrderLevel;
    }

    public List<OrderLevel> getListGlobalLevel() {
        return listGlobalLevel;
    }

    public void setListGlobalLevel(List<OrderLevel> listGlobalLevel) {
        this.listGlobalLevel = listGlobalLevel;
    }

    public int getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(int totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public int getTotalBenefit() {
        return totalBenefit;
    }

    public void setTotalBenefit(int totalBenefit) {
        this.totalBenefit = totalBenefit;
    }
}
