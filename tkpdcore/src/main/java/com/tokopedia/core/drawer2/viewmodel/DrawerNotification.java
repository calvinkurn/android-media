package com.tokopedia.core.drawer2.viewmodel;

/**
 * Created by nisie on 1/23/17.
 */

public class DrawerNotification {

    private int inboxMessage;
    private int inboxTalk;
    private int inboxReview;
    private int inboxTicket;
    private int inboxResCenter;

    private int purchaseOrderStatus;
    private int purchasePaymentConfirm;
    private int purchaseDeliveryConfirm;
    private int purchaseReorder;

    private int sellingNewOrder;
    private int sellingShippingConfirmation;
    private int sellingShippingStatus;

    public DrawerNotification() {
        this.inboxMessage = 0;
        this.inboxTalk = 0;
        this.inboxReview = 0;
        this.inboxTicket = 0;
        this.inboxResCenter = 0;

        this.purchaseOrderStatus = 0;
        this.purchasePaymentConfirm = 0;
        this.purchaseDeliveryConfirm = 0;
        this.purchaseReorder = 0;

        this.sellingNewOrder = 0;
        this.sellingShippingConfirmation = 0;
        this.sellingShippingStatus = 0;
    }

    public int getInboxMessage() {
        return inboxMessage;
    }

    public void setInboxMessage(int inboxMessage) {
        this.inboxMessage = inboxMessage;
    }

    public int getInboxTalk() {
        return inboxTalk;
    }

    public void setInboxTalk(int inboxTalk) {
        this.inboxTalk = inboxTalk;
    }

    public int getInboxReview() {
        return inboxReview;
    }

    public void setInboxReview(int inboxReview) {
        this.inboxReview = inboxReview;
    }

    public int getInboxTicket() {
        return inboxTicket;
    }

    public void setInboxTicket(int inboxTicket) {
        this.inboxTicket = inboxTicket;
    }

    public int getInboxResCenter() {
        return inboxResCenter;
    }

    public void setInboxResCenter(int inboxResCenter) {
        this.inboxResCenter = inboxResCenter;
    }

    public int getPurchaseOrderStatus() {
        return purchaseOrderStatus;
    }

    public void setPurchaseOrderStatus(int purchaseOrderStatus) {
        this.purchaseOrderStatus = purchaseOrderStatus;
    }

    public int getPurchasePaymentConfirm() {
        return purchasePaymentConfirm;
    }

    public void setPurchasePaymentConfirm(int purchasePaymentConfirm) {
        this.purchasePaymentConfirm = purchasePaymentConfirm;
    }

    public int getPurchaseDeliveryConfirm() {
        return purchaseDeliveryConfirm;
    }

    public void setPurchaseDeliveryConfirm(int purchaseDeliveryConfirm) {
        this.purchaseDeliveryConfirm = purchaseDeliveryConfirm;
    }

    public int getPurchaseReorder() {
        return purchaseReorder;
    }

    public void setPurchaseReorder(int purchaseReorder) {
        this.purchaseReorder = purchaseReorder;
    }

    public int getSellingNewOrder() {
        return sellingNewOrder;
    }

    public void setSellingNewOrder(int sellingNewOrder) {
        this.sellingNewOrder = sellingNewOrder;
    }

    public int getSellingShippingConfirmation() {
        return sellingShippingConfirmation;
    }

    public void setSellingShippingConfirmation(int sellingShippingConfirmation) {
        this.sellingShippingConfirmation = sellingShippingConfirmation;
    }

    public int getSellingShippingStatus() {
        return sellingShippingStatus;
    }

    public void setSellingShippingStatus(int sellingShippingStatus) {
        this.sellingShippingStatus = sellingShippingStatus;
    }
}
