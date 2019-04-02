package com.tokopedia.gm.statistic.domain.model.transaction.table;

/**
 * Created by normansyahputa on 7/18/17.
 */

public class Cell {

    private int productProductId;

    private String productProductName;

    private String productProductLink;

    private int transSum;

    private int orderSum;

    private int deliveredSum;

    private int rejectedSum;

    private int deliveredAmt;

    private int rejectedAmt;

    public int getProductProductId() {
        return productProductId;
    }

    public void setProductProductId(int productProductId) {
        this.productProductId = productProductId;
    }

    public String getProductProductName() {
        return productProductName;
    }

    public void setProductProductName(String productProductName) {
        this.productProductName = productProductName;
    }

    public String getProductProductLink() {
        return productProductLink;
    }

    public void setProductProductLink(String productProductLink) {
        this.productProductLink = productProductLink;
    }

    public int getTransSum() {
        return transSum;
    }

    public void setTransSum(int transSum) {
        this.transSum = transSum;
    }

    public int getOrderSum() {
        return orderSum;
    }

    public void setOrderSum(int orderSum) {
        this.orderSum = orderSum;
    }

    public int getDeliveredSum() {
        return deliveredSum;
    }

    public void setDeliveredSum(int deliveredSum) {
        this.deliveredSum = deliveredSum;
    }

    public int getRejectedSum() {
        return rejectedSum;
    }

    public void setRejectedSum(int rejectedSum) {
        this.rejectedSum = rejectedSum;
    }

    public int getDeliveredAmt() {
        return deliveredAmt;
    }

    public void setDeliveredAmt(int deliveredAmt) {
        this.deliveredAmt = deliveredAmt;
    }

    public int getRejectedAmt() {
        return rejectedAmt;
    }

    public void setRejectedAmt(int rejectedAmt) {
        this.rejectedAmt = rejectedAmt;
    }

}
