package com.tokopedia.home.account.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.tokopedia.navigation_common.model.DepositModel;
import com.tokopedia.navigation_common.model.LePreapproveModel;
import com.tokopedia.navigation_common.model.PendingCashbackModel;
import com.tokopedia.navigation_common.model.ReputationShop;
import com.tokopedia.navigation_common.model.TokopointsSumCoupon;
import com.tokopedia.navigation_common.model.VccUserStatus;
import com.tokopedia.navigation_common.model.WalletModel;
import com.tokopedia.navigation_common.model.ProfileModel;
import com.tokopedia.navigation_common.model.UserShopInfoModel;
import com.tokopedia.navigation_common.model.TokopointsModel;
import com.tokopedia.navigation_common.model.NotificationsModel;
import com.tokopedia.user_identification_common.pojo.KycStatusPojo;

import java.util.ArrayList;
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
    private List<ReputationShop> reputationShops = new ArrayList<>();
    @SerializedName("tokopointsSumCoupon")
    @Expose
    private TokopointsSumCoupon tokopointsSumCoupon;
    @SerializedName("le_preapprove")
    @Expose
    private LePreapproveModel lePreapprove = new LePreapproveModel();

    @SerializedName("vcc_user_status")
    private VccUserStatus vccUserStatus = new VccUserStatus();

    @SerializedName("vcc_user_balance")
    private VccUserBalance vccUserBalance = new VccUserBalance();

    @SerializedName("kycStatus")
    private KycStatusPojo kycStatusPojo = new KycStatusPojo();

    private PendingCashbackModel pendingCashbackModel;

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
  
    public VccUserStatus getVccUserStatus() {
        return vccUserStatus;
    }

    public void setVccUserStatus(VccUserStatus vccUserStatus) {
        this.vccUserStatus = vccUserStatus;
    }

    public VccUserBalance getVccUserBalance() {
        return vccUserBalance;
    }

    public void setVccUserBalance(VccUserBalance vccUserBalance) {
        this.vccUserBalance = vccUserBalance;
    }

    public PendingCashbackModel getPendingCashbackModel() {
        return pendingCashbackModel;
    }

    public void setPendingCashbackModel(PendingCashbackModel pendingCashbackModel) {
        this.pendingCashbackModel = pendingCashbackModel;
    }

    public KycStatusPojo getKycStatusPojo() {
        return kycStatusPojo;
    }
}
