package com.tokopedia.home.account.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.tokopedia.navigation_common.model.DepositModel;
import com.tokopedia.navigation_common.model.LePreapproveModel;
import com.tokopedia.navigation_common.model.ReputationShop;
import com.tokopedia.navigation_common.model.TokopointsSumCoupon;
import com.tokopedia.navigation_common.model.WalletModel;
import com.tokopedia.navigation_common.model.ProfileModel;
import com.tokopedia.navigation_common.model.UserShopInfoModel;
import com.tokopedia.navigation_common.model.TokopointsModel;
import com.tokopedia.navigation_common.model.NotificationsModel;

import java.util.List;

/**
 * @author okasurya on 7/20/18.
 */
public class AccountModel {
    @SerializedName("isAuthenticated")
    @Expose
    private Integer isAuthenticated;
    @SerializedName("saldo")
    @Expose
    private DepositModel deposit;
    @SerializedName("wallet")
    @Expose
    private WalletModel wallet;
    @SerializedName("profile")
    @Expose
    private ProfileModel profile;
    @SerializedName("userShopInfo")
    @Expose
    private UserShopInfoModel shopInfo;
    @SerializedName("tokopoints")
    @Expose
    private TokopointsModel tokopoints;
    @SerializedName("notifications")
    @Expose
    private NotificationsModel notifications;
    @SerializedName("reputation_shops")
    @Expose
    private List<ReputationShop> reputationShops;
    @SerializedName("tokopointsSumCoupon")
    @Expose
    private TokopointsSumCoupon tokopointsSumCoupon;
    @SerializedName("le_preapprove")
    @Expose
    private LePreapproveModel lePreapprove;

    public Integer getIsAuthenticated() {
        return isAuthenticated;
    }

    public void setIsAuthenticated(Integer isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
    }

    public DepositModel getDeposit() {
        return deposit;
    }

    public void setDeposit(DepositModel deposit) {
        this.deposit = deposit;
    }

    public WalletModel getWallet() {
        return wallet;
    }

    public void setWallet(WalletModel wallet) {
        this.wallet = wallet;
    }

    public ProfileModel getProfile() {
        return profile;
    }

    public void setProfile(ProfileModel profile) {
        this.profile = profile;
    }

    public UserShopInfoModel getShopInfo() {
        return shopInfo;
    }

    public void setShopInfo(UserShopInfoModel shopInfo) {
        this.shopInfo = shopInfo;
    }

    public TokopointsModel getTokopoints() {
        return tokopoints;
    }

    public void setTokopoints(TokopointsModel tokopoints) {
        this.tokopoints = tokopoints;
    }

    public NotificationsModel getNotifications() {
        return notifications;
    }

    public void setNotifications(NotificationsModel notifications) {
        this.notifications = notifications;
    }

    public List<ReputationShop> getReputationShops() {
        return reputationShops;
    }

    public void setReputationShops(List<ReputationShop> reputationShops) {
        this.reputationShops = reputationShops;
    }

    public TokopointsSumCoupon getTokopointsSumCoupon() {
        return tokopointsSumCoupon;
    }

    public void setTokopointsSumCoupon(TokopointsSumCoupon tokopointsSumCoupon) {
        this.tokopointsSumCoupon = tokopointsSumCoupon;
    }

    public LePreapproveModel getLePreapprove() {
        return lePreapprove;
    }

    public void setLePreapprove(LePreapproveModel lePreapprove) {
        this.lePreapprove = lePreapprove;
    }
}
