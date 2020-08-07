package com.tokopedia.home.account.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.home.account.data.model.tokopointshortcut.ShortcutResponse;
import com.tokopedia.home.account.data.pojo.UserProfileCompletion;
import com.tokopedia.navigation_common.model.DebitInstantModel;
import com.tokopedia.navigation_common.model.DepositModel;
import com.tokopedia.navigation_common.model.LePreapproveModel;
import com.tokopedia.navigation_common.model.MembershipSumUserCard;
import com.tokopedia.navigation_common.model.NotificationsModel;
import com.tokopedia.navigation_common.model.PendingCashbackModel;
import com.tokopedia.navigation_common.model.ProfileModel;
import com.tokopedia.navigation_common.model.ReputationShop;
import com.tokopedia.navigation_common.model.SaldoModel;
import com.tokopedia.navigation_common.model.TokopointsModel;
import com.tokopedia.navigation_common.model.TokopointsSumCoupon;
import com.tokopedia.navigation_common.model.UserShopInfoModel;
import com.tokopedia.navigation_common.model.VccUserStatus;
import com.tokopedia.navigation_common.model.WalletModel;
import com.tokopedia.user_identification_common.domain.pojo.KycStatusPojo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author okasurya on 7/20/18.
 *
 *
 * please use AccountDataModel.kt instead
 */

@Deprecated
public class AccountModel {
    @SerializedName("isAuthenticated")
    @Expose
    private Integer isAuthenticated;
    @SerializedName("wallet")
    @Expose
    private WalletModel wallet = new WalletModel();
    @SerializedName("profile")
    @Expose
    private ProfileModel profile = new ProfileModel();
    @SerializedName("userProfileCompletion")
    @Expose
    private UserProfileCompletion userProfileCompletion = new UserProfileCompletion();
    @SerializedName("userShopInfo")
    @Expose
    private UserShopInfoModel shopInfo = new UserShopInfoModel();
    @SerializedName("tokopoints")
    @Expose
    private TokopointsModel tokopoints = new TokopointsModel();
    @SerializedName("notifications")
    @Expose
    private NotificationsModel notifications = new NotificationsModel();
    @SerializedName("reputation_shops")
    @Expose
    private List<ReputationShop> reputationShops = new ArrayList<>();
    @SerializedName("tokopointsSumCoupon")
    @Expose
    private TokopointsSumCoupon tokopointsSumCoupon = new TokopointsSumCoupon();
    @SerializedName("membershipSumUserCard")
    @Expose
    private MembershipSumUserCard membershipSumUserCard = new MembershipSumUserCard();
    @SerializedName("le_preapprove")
    @Expose
    private LePreapproveModel lePreapprove = new LePreapproveModel();
    @SerializedName("openDebitSettings")
    @Expose
    private DebitInstantModel debitInstant = new DebitInstantModel();

    @SerializedName("vcc_user_status")
    @Expose
    private VccUserStatus vccUserStatus = new VccUserStatus();

    @SerializedName("vcc_user_balance")
    @Expose
    private VccUserBalance vccUserBalance = new VccUserBalance();

    @SerializedName("kycStatus")
    @Expose
    private KycStatusPojo kycStatusPojo = new KycStatusPojo();

    private SaldoModel saldoModel = new SaldoModel();

    private ShortcutResponse shortcutResponse = new ShortcutResponse();

    @SerializedName("isAffiliate")
    @Expose
    private boolean isAffiliate = false;

    @SerializedName("CheckEligible")
    @Expose
    private PremiumAccountResponse premiumAccountResponse;

    @SerializedName("balance")
    @Expose
    private DepositModel saldo = new DepositModel();

    private PendingCashbackModel pendingCashbackModel = new PendingCashbackModel();

    public Integer getIsAuthenticated() {
        return isAuthenticated;
    }

    public void setIsAuthenticated(Integer isAuthenticated) {

        this.isAuthenticated = isAuthenticated;
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

    public UserProfileCompletion getUserProfileCompletion() {
        return userProfileCompletion;
    }

    public void setUserProfileCompletion(UserProfileCompletion userProfileCompletion) {
        this.userProfileCompletion = userProfileCompletion;
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

    public boolean isAffiliate() {
        return isAffiliate;
    }

    public void setAffiliate(boolean affiliate) {
        isAffiliate = affiliate;
    }

    public SaldoModel getSaldoModel() {
        return saldoModel;
    }

    public void setSaldoModel(SaldoModel saldoModel) {
        this.saldoModel = saldoModel;
    }

    public DepositModel getSaldo() {
        return saldo;
    }

    public void setSaldo(DepositModel depositModel) {
        this.saldo = depositModel;
    }

    public ShortcutResponse getShortcutResponse() {
        return shortcutResponse;
    }

    public void setShortcutResponse(ShortcutResponse shortcutResponse) {
        this.shortcutResponse = shortcutResponse;
    }

    public DebitInstantModel getDebitInstant() {
        return debitInstant;
    }

    public void setDebitInstant(DebitInstantModel debitInstant) {
        this.debitInstant = debitInstant;
    }

    public MembershipSumUserCard getMembershipSumUserCard() {
        return membershipSumUserCard;
    }

    public void setMembershipSumUserCard(MembershipSumUserCard membershipSumUserCard) {
        this.membershipSumUserCard = membershipSumUserCard;
    }

    public PremiumAccountResponse getPremiumAccountResponse() {
        return premiumAccountResponse;
    }

    public void setPremiumAccountResponse(PremiumAccountResponse premiumAccountResponse) {
        this.premiumAccountResponse = premiumAccountResponse;
    }
}
