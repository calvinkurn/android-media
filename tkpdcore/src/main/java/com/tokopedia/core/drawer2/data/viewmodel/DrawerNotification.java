package com.tokopedia.core.drawer2.data.viewmodel;

import com.tokopedia.core.util.GlobalConfig;

/**
 * Created by nisie on 1/23/17.
 */

public class DrawerNotification {

    public static final String CACHE_INBOX_MESSAGE = "CACHE_INBOX_MESSAGE";
    public static final String CACHE_INBOX_TALK = "CACHE_INBOX_TALK";
    public static final String CACHE_INBOX_REVIEW = "CACHE_INBOX_REVIEW";
    public static final String CACHE_INBOX_RESOLUTION_CENTER = "CACHE_INBOX_RESOLUTION_CENTER";
    public static final String CACHE_INBOX_TICKET = "CACHE_INBOX_TICKET";
    public static final String CACHE_INBOX_SELLER_INFO = "CACHE_INBOX_SELLER_INFO";

    public static final String CACHE_PURCHASE_ORDER_STATUS = "CACHE_PURCHASE_ORDER_STATUS";
    public static final String CACHE_PURCHASE_PAYMENT_CONF = "CACHE_PURCHASE_PAYMENT_CONF";
    public static final String CACHE_PURCHASE_PAYMENT_CONFIRM = "CACHE_PURCHASE_PAYMENT_CONFIRM";
    public static final String CACHE_PURCHASE_DELIVERY_CONFIRM = "CACHE_PURCHASE_DELIVERY_CONFIRM";
    public static final String CACHE_PURCHASE_REORDER = "CACHE_PURCHASE_REORDER";

    public static final String CACHE_SELLING_NEW_ORDER = "CACHE_SELLING_NEW_ORDER";
    public static final String CACHE_SELLING_SHIPPING_CONFIRMATION = "CACHE_SELLING_SHIPPING_CONFIRMATION";
    public static final String CACHE_SELLING_SHIPPING_STATUS = "CACHE_SELLING_SHIPPING_STATUS";

    public static final String CACHE_TOTAL_NOTIF = "CACHE_TOTAL_NOTIF";
    public static final String CACHE_INCR_NOTIF = "CACHE_INCR_NOTIF";
    public static final String CACHE_TOTAL_CART = "CACHE_INCR_NOTIF";
    public static final String IS_HAS_CART = "IS_HAS_CART";


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
    private int totalNotif;
    private int incrNotif;
    private int totalCart;

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

    public int getTotalNotif() {
        if (GlobalConfig.isSellerApp())
            return totalNotif - (getTotalPurchaseNotif());
        else
            return totalNotif;
    }

    public int getTotalPurchaseNotif() {
        return getPurchasePaymentConfirm() + getPurchaseDeliveryConfirm() +
                getPurchaseOrderStatus() + getPurchaseReorder();
    }

    public void setTotalNotif(int totalNotif) {
        this.totalNotif = totalNotif;
    }

    public boolean isUnread() {
        return incrNotif > 0;
    }

    public void setIncrNotif(int incrNotif) {
        this.incrNotif = incrNotif;
    }

    public int getIncrNotif() {
        return incrNotif;
    }

    public void setTotalCart(int totalCart) {
        this.totalCart = totalCart;
    }

    public int getTotalCart() {
        return totalCart;
    }

    public int getTotalInboxNotif() {
        return getInboxMessage() + getInboxTalk() + getInboxReview() + getInboxResCenter() + getInboxTicket();
    }

    public int getTotalSellingNotif() {
        return getSellingNewOrder() + getSellingShippingConfirmation() + getSellingShippingStatus();
    }
}
