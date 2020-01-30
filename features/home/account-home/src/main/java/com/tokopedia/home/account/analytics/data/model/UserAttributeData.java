
package com.tokopedia.home.account.analytics.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.navigation_common.model.SaldoModel;

public class UserAttributeData {

    @SerializedName("userShopInfo")
    @Expose
    private UserShopInfo userShopInfo = new UserShopInfo();
    @SerializedName("profile")
    @Expose
    private Profile profile = new Profile();
    @SerializedName("paymentAdminProfile")
    @Expose
    private PaymentAdminProfile paymentAdminProfile = new PaymentAdminProfile();
    @SerializedName("topadsDeposit")
    @Expose
    private TopadsDeposit topadsDeposit = new TopadsDeposit();
    @SerializedName("notifications")
    @Expose
    private Notifications notifications = new Notifications();

    private SaldoModel saldo = new SaldoModel();

    public UserShopInfo getUserShopInfo() {
        return userShopInfo;
    }

    public void setUserShopInfo(UserShopInfo userShopInfo) {
        this.userShopInfo = userShopInfo;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public SaldoModel getSaldo() {
        return saldo;
    }

    public void setSaldo(SaldoModel saldo) {
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
