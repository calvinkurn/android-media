package com.tokopedia.opportunity.domain.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.network.entity.replacement.opportunitydata.OrderShipment;

/**
 * Created by nakama on 27/03/18.
 */

public class OpportunityDetail {

    @SerializedName("order_replacement_id")
    @Expose
    private int orderReplacementId;
    @SerializedName("order_id")
    @Expose
    private int orderOrderId;
    @SerializedName("replacement_multiplier_value")
    @Expose
    private int replacementMultiplierValue;
    @SerializedName("replacement_multiplier_value_str")
    @Expose
    private String replacementMultiplierValueStr;
    @SerializedName("replacement_multiplier_color")
    @Expose
    private String replacementMultiplierColor;
    @SerializedName("replacement_tnc")
    @Expose
    private String replacementTnc;

    @SerializedName("order_cashback_idr")
    @Expose
    private String orderCashbackIdr;
    @SerializedName("order_cashback")
    @Expose
    private String orderCashback;

    @SerializedName("order_open_amount")
    @Expose
    private String orderOpenAmount;
    @SerializedName("order_open_amount_idr")
    @Expose
    private String orderOpenAmountIdr;

    @SerializedName("order_deadline_left")
    @Expose
    private String orderDeadlineLeft;
    @SerializedName("order_deadline_color")
    @Expose
    private String orderDeadlineColor;

    @SerializedName("shipment")
    @Expose
    private OrderShipment shipment;

    @SerializedName("destination")
    @Expose
    private OpportunityDetailDestination destination;

    @SerializedName("detail")
    @Expose
    private OpportunityProduct detail;

    public int getOrderReplacementId() {
        return orderReplacementId;
    }

    public void setOrderReplacementId(int orderReplacementId) {
        this.orderReplacementId = orderReplacementId;
    }

    public int getOrderOrderId() {
        return orderOrderId;
    }

    public void setOrderOrderId(int orderOrderId) {
        this.orderOrderId = orderOrderId;
    }

    public int getReplacementMultiplierValue() {
        return replacementMultiplierValue;
    }

    public void setReplacementMultiplierValue(int replacementMultiplierValue) {
        this.replacementMultiplierValue = replacementMultiplierValue;
    }

    public String getReplacementMultiplierValueStr() {
        return replacementMultiplierValueStr;
    }

    public void setReplacementMultiplierValueStr(String replacementMultiplierValueStr) {
        this.replacementMultiplierValueStr = replacementMultiplierValueStr;
    }

    public String getReplacementMultiplierColor() {
        return replacementMultiplierColor;
    }

    public void setReplacementMultiplierColor(String replacementMultiplierColor) {
        this.replacementMultiplierColor = replacementMultiplierColor;
    }

    public String getReplacementTnc() {
        return replacementTnc;
    }

    public void setReplacementTnc(String replacementTnc) {
        this.replacementTnc = replacementTnc;
    }

    public String getOrderCashbackIdr() {
        return orderCashbackIdr;
    }

    public void setOrderCashbackIdr(String orderCashbackIdr) {
        this.orderCashbackIdr = orderCashbackIdr;
    }

    public String getOrderCashback() {
        return orderCashback;
    }

    public void setOrderCashback(String orderCashback) {
        this.orderCashback = orderCashback;
    }

    public String getOrderOpenAmount() {
        return orderOpenAmount;
    }

    public void setOrderOpenAmount(String orderOpenAmount) {
        this.orderOpenAmount = orderOpenAmount;
    }

    public String getOrderOpenAmountIdr() {
        return orderOpenAmountIdr;
    }

    public void setOrderOpenAmountIdr(String orderOpenAmountIdr) {
        this.orderOpenAmountIdr = orderOpenAmountIdr;
    }

    public String getOrderDeadlineLeft() {
        return orderDeadlineLeft;
    }

    public void setOrderDeadlineLeft(String orderDeadlineLeft) {
        this.orderDeadlineLeft = orderDeadlineLeft;
    }

    public String getOrderDeadlineColor() {
        return orderDeadlineColor;
    }

    public void setOrderDeadlineColor(String orderDeadlineColor) {
        this.orderDeadlineColor = orderDeadlineColor;
    }

    public OrderShipment getShipment() {
        return shipment;
    }

    public void setShipment(OrderShipment shipment) {
        this.shipment = shipment;
    }

    public OpportunityDetailDestination getDestination() {
        return destination;
    }

    public void setDestination(OpportunityDetailDestination destination) {
        this.destination = destination;
    }

    public OpportunityProduct getDetail() {
        return detail;
    }

    public void setDetail(OpportunityProduct detail) {
        this.detail = detail;
    }
}
