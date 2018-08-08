package com.tokopedia.core.notification.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by noiz354 on 5/9/16.
 */
public class Notification
{
    @SerializedName("status")
    String status;

    @SerializedName("data")
    Data data;

    @SerializedName("config")
    String config;

    @SerializedName("server_process_time")
    String server_process_time;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getServer_process_time() {
        return server_process_time;
    }

    public void setServer_process_time(String server_process_time) {
        this.server_process_time = server_process_time;
    }

    public class Data {
        @SerializedName("incr_notif")
        String incr_notif;

        @SerializedName("total_cart")
        String total_cart;

        @SerializedName("total_notif")
        String total_notif;

        @SerializedName("sales")
        Sales sales;

        @SerializedName("inbox")
        Inbox inbox;

        @SerializedName("purchase")
        Purchase purchase;

        @SerializedName("resolution")
        String resolution;

        public String getIncr_notif() {
            return incr_notif;
        }

        public void setIncr_notif(String incr_notif) {
            this.incr_notif = incr_notif;
        }

        public String getTotal_cart() {
            return total_cart;
        }

        public void setTotal_cart(String total_cart) {
            this.total_cart = total_cart;
        }

        public String getTotal_notif() {
            return total_notif;
        }

        public void setTotal_notif(String total_notif) {
            this.total_notif = total_notif;
        }

        public Sales getSales() {
            return sales;
        }

        public void setSales(Sales sales) {
            this.sales = sales;
        }

        public Inbox getInbox() {
            return inbox;
        }

        public void setInbox(Inbox inbox) {
            this.inbox = inbox;
        }

        public Purchase getPurchase() {
            return purchase;
        }

        public void setPurchase(Purchase purchase) {
            this.purchase = purchase;
        }

        public String getResolution() {
            return resolution;
        }

        public void setResolution(String resolution) {
            this.resolution = resolution;
        }
    }

    @Parcel
    public static class Sales {
        @SerializedName("sales_new_order")
        String sales_new_order;

        @SerializedName("sales_shipping_status")
        String sales_shipping_status;

        @SerializedName("sales_shipping_confirm")
        String sales_shipping_confirm;

        public String getSales_new_order() {
            return sales_new_order;
        }

        public void setSales_new_order(String sales_new_order) {
            this.sales_new_order = sales_new_order;
        }

        public String getSales_shipping_status() {
            return sales_shipping_status;
        }

        public void setSales_shipping_status(String sales_shipping_status) {
            this.sales_shipping_status = sales_shipping_status;
        }

        public String getSales_shipping_confirm() {
            return sales_shipping_confirm;
        }

        public void setSales_shipping_confirm(String sales_shipping_confirm) {
            this.sales_shipping_confirm = sales_shipping_confirm;
        }
    }

    @Parcel
    public static class Purchase {
        @SerializedName("purchase_order_status")
        String purchase_order_status;

        @SerializedName("purchase_payment_conf")
        String purchase_payment_conf;

        @SerializedName("purchase_payment_confirm")
        String purchase_payment_confirm;

        @SerializedName("purchase_delivery_confirm")
        String purchase_delivery_confirm;

        @SerializedName("purchase_reorder")
        String purchase_reorder;

        public String getPurchase_order_status() {
            return purchase_order_status;
        }

        public void setPurchase_order_status(String purchase_order_status) {
            this.purchase_order_status = purchase_order_status;
        }

        public String getPurchase_payment_conf() {
            return purchase_payment_conf;
        }

        public void setPurchase_payment_conf(String purchase_payment_conf) {
            this.purchase_payment_conf = purchase_payment_conf;
        }

        public String getPurchase_payment_confirm() {
            return purchase_payment_confirm;
        }

        public void setPurchase_payment_confirm(String purchase_payment_confirm) {
            this.purchase_payment_confirm = purchase_payment_confirm;
        }

        public String getPurchase_delivery_confirm() {
            return purchase_delivery_confirm;
        }

        public void setPurchase_delivery_confirm(String purchase_delivery_confirm) {
            this.purchase_delivery_confirm = purchase_delivery_confirm;
        }

        public String getPurchase_reorder() {
            return purchase_reorder;
        }

        public void setPurchase_reorder(String purchase_reorder) {
            this.purchase_reorder = purchase_reorder;
        }
    }

    @Parcel
    public static class Inbox {
        @SerializedName("inbox_wishlist")
        String inbox_wishlist;

        @SerializedName("inbox_talk")
        String inbox_talk;

        @SerializedName("inbox_review")
        String inbox_review;

        @SerializedName("inbox_friend")
        String inbox_friend;

        @SerializedName("inbox_reputation")
        String inbox_reputation;

        @SerializedName("inbox_ticket")
        String inbox_ticket;

        @SerializedName("inbox_message")
        String inbox_message;

        public String getInbox_wishlist() {
            return inbox_wishlist;
        }

        public void setInbox_wishlist(String inbox_wishlist) {
            this.inbox_wishlist = inbox_wishlist;
        }

        public String getInbox_talk() {
            return inbox_talk;
        }

        public void setInbox_talk(String inbox_talk) {
            this.inbox_talk = inbox_talk;
        }

        public String getInbox_review() {
            return inbox_review;
        }

        public void setInbox_review(String inbox_review) {
            this.inbox_review = inbox_review;
        }

        public String getInbox_friend() {
            return inbox_friend;
        }

        public void setInbox_friend(String inbox_friend) {
            this.inbox_friend = inbox_friend;
        }

        public String getInbox_reputation() {
            return inbox_reputation;
        }

        public void setInbox_reputation(String inbox_reputation) {
            this.inbox_reputation = inbox_reputation;
        }

        public String getInbox_ticket() {
            return inbox_ticket;
        }

        public void setInbox_ticket(String inbox_ticket) {
            this.inbox_ticket = inbox_ticket;
        }

        public String getInbox_message() {
            return inbox_message;
        }

        public void setInbox_message(String inbox_message) {
            this.inbox_message = inbox_message;
        }
    }
}
