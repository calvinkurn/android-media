
package com.tokopedia.core.drawer2.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SellerDrawerData {

    @SerializedName("shopInfoMoengage")
    @Expose
    private ShopInfoMoengage shopInfoMoengage;
    @SerializedName("profile")
    @Expose
    private Profile profile;
    @SerializedName("address")
    @Expose
    private Address address;
    @SerializedName("wallet")
    @Expose
    private Wallet wallet;
    @SerializedName("saldo")
    @Expose
    private Saldo saldo;
    @SerializedName("paymentAdminProfile")
    @Expose
    private PaymentAdminProfile paymentAdminProfile;
    @SerializedName("topadsDeposit")
    @Expose
    private TopadsDeposit topadsDeposit;

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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
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

}
