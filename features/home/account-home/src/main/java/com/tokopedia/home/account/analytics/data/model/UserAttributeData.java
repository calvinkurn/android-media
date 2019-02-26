
package com.tokopedia.home.account.analytics.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserAttributeData {

    @SerializedName("shopInfoMoengage")
    @Expose
    private ShopInfoMoengage shopInfoMoengage = new ShopInfoMoengage();
    @SerializedName("profile")
    @Expose
    private Profile profile = new Profile();
    @SerializedName("wallet")
    @Expose
    private Wallet wallet = new Wallet();
    @SerializedName("balance")
    @Expose
    private Saldo saldo = new Saldo();
    @SerializedName("paymentAdminProfile")
    @Expose
    private PaymentAdminProfile paymentAdminProfile = new PaymentAdminProfile();
    @SerializedName("topadsDeposit")
    @Expose
    private TopadsDeposit topadsDeposit = new TopadsDeposit();
    @SerializedName("notifications")
    @Expose
    private Notifications notifications = new Notifications();

    public ShopInfoMoengage getShopInfoMoengage() {
        return shopInfoMoengage;
    }

    public void setShopInfoMoengage(ShopInfoMoengage shopInfoMoengage) {
        this.shopInfoMoengage = shopInfoMoengage;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public Saldo getSaldo() {
        return saldo;
    }

    public void setSaldo(Saldo saldo) {
        this.saldo = saldo;
    }

    public PaymentAdminProfile getPaymentAdminProfile() {
        return paymentAdminProfile;
    }

    public void setPaymentAdminProfile(PaymentAdminProfile paymentAdminProfile) {
        this.paymentAdminProfile = paymentAdminProfile;
    }

    public TopadsDeposit getTopadsDeposit() {
        return topadsDeposit;
    }

    public void setTopadsDeposit(TopadsDeposit topadsDeposit) {
        this.topadsDeposit = topadsDeposit;
    }

    public Notifications getNotifications() {
        return notifications;
    }

    public void setNotifications(Notifications notifications) {
        this.notifications = notifications;
    }
}
