package com.tokopedia.home.account.revamp.domain.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.home.account.data.model.PremiumAccountResponse
import com.tokopedia.home.account.data.model.tokopointshortcut.ShortcutResponse
import com.tokopedia.home.account.data.pojo.UserProfileCompletion
import com.tokopedia.navigation_common.model.*
import com.tokopedia.user_identification_common.domain.pojo.KycStatusPojo
import java.util.*


data class AccountDataModel(
        @SerializedName("isAuthenticated")
        var isAuthenticated: Int = 0,

        @SerializedName("wallet")
        @Expose
        var wallet: WalletModel = WalletModel(),

        @SerializedName("profile")
        @Expose
        var profile: ProfileModel = ProfileModel(),

        @SerializedName("userProfileCompletion")
        @Expose
        var userProfileCompletion: UserProfileCompletion = UserProfileCompletion(),

        @SerializedName("userShopInfo")
        @Expose
        var shopInfo: UserShopInfoDataModel = UserShopInfoDataModel(),

        @SerializedName("tokopoints")
        @Expose
        var tokopoints: TokopointsModel = TokopointsModel(),

        @SerializedName("notifications")
        @Expose
        var notifications: NotificationsModel = NotificationsModel(),

        @SerializedName("reputation_shops")
        @Expose
        var reputationShops: List<ReputationShop> = ArrayList(),

        @SerializedName("tokopointsSumCoupon")
        @Expose
        var tokopointsSumCoupon: TokopointsSumCoupon = TokopointsSumCoupon(),

        @SerializedName("membershipSumUserCard")
        @Expose
        var membershipSumUserCard: MembershipSumUserCard = MembershipSumUserCard(),

        @SerializedName("le_preapprove")
        @Expose
        var lePreapprove: LePreapproveModel = LePreapproveModel(),

        @SerializedName("openDebitSettings")
        @Expose
        var debitInstant: DebitInstantModel = DebitInstantModel(),

        @SerializedName("vcc_user_status")
        @Expose
        var vccUserStatus: VccUserStatus = VccUserStatus(),

        @SerializedName("vcc_user_balance")
        @Expose
        var vccUserBalanceDataModel: VccUserBalanceDataModel = VccUserBalanceDataModel(),

        @SerializedName("kycStatus")
        @Expose
        val kycStatusPojo: KycStatusPojo = KycStatusPojo(),

        @SerializedName("isAffiliate")
        @Expose
        var isAffiliate: Boolean = false,

        @SerializedName("CheckEligible")
        @Expose
        var premiumAccountResponse: PremiumAccountResponse = PremiumAccountResponse(),

        @SerializedName("balance")
        @Expose
        var saldo: DepositDataModel = DepositDataModel(),

        var shortcutResponse: ShortcutResponse = ShortcutResponse()
)